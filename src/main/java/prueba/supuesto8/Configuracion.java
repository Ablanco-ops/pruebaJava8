package prueba.supuesto8;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import prueba.supuesto8.excepciones.GestionExcepciones;
import prueba.supuesto8.excepciones.TipoExcepcion;
import prueba.supuesto8.modelo.Seccion;
import prueba.supuesto8.modelo.Valvula;

/**
 * 
 * Clase para la configuración de la aplicacion, mediante dos archivos externos obtenemos la configuración de válvulas del sistema (configvValvulas.json)
 * y la configuración del path de la configuración de válvulas, los ficheros de salida de estado, comandos y log, la capacidad del depósito y
 * el formato de tiempo a usar en la interfaz de usuario para el tiempo de los comandos y el log.
 *
 */
public class Configuracion {
	
	private static final Logger LOG = LogManager.getLogger(Procesador.class);
	
	private static final String PATH_CONFIG= "src/main/resources/config.xml";
	private static String pathConfigValvulas = "src/main/resources/configValvulas.json";
	private static String pathEstado = "src/main/resources/estados.txt";
	private static String pathComando = "src/main/resources/comandos.txt";
	private static String pathFicheroLog = "src/main/resources/fichero_log.txt";
	
	private static BigDecimal capacidadDeposito= new BigDecimal(16);
	
	private static DateTimeFormatter formatoTiempoLecturaEscritura = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");
	private static DateTimeFormatter formatoTiempoComandos=DateTimeFormatter.ofPattern("hh:mm");
	private static DateTimeFormatter formatoTiempoLog=DateTimeFormatter.ofPattern("hh:mm");
	
	private static long refrescoHilos = 200;
	private static long retrasoProcesos = 100;
	
	private static Properties configuracionProperties= new Properties();
	
	
	/**
	 * Método para obtener los parámetros del fichero de configuración config.xml
	 * @throws InvalidPropertiesFormatException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void setConfiguracion(){
		try {
			configuracionProperties.loadFromXML(new FileInputStream(PATH_CONFIG));
			
			pathConfigValvulas=configuracionProperties.getProperty("pathConfigValvulas");
			pathEstado=configuracionProperties.getProperty("pathEstado");
			pathComando=configuracionProperties.getProperty("pathComando");
			pathFicheroLog=configuracionProperties.getProperty("pathFicheroLog");
			
			capacidadDeposito=new BigDecimal(configuracionProperties.getProperty("capacidadDeposito")) ;
			
			formatoTiempoComandos=DateTimeFormatter.ofPattern(configuracionProperties.getProperty("formatoTiempoComandos")) ;
			formatoTiempoLog=DateTimeFormatter.ofPattern(configuracionProperties.getProperty("formatoTiempoLog")) ;
			
			refrescoHilos=Long.parseLong(configuracionProperties.getProperty("refrescohilos"));
			retrasoProcesos=Long.parseLong(configuracionProperties.getProperty("retrasoProcesos"));
			
		} catch (InvalidPropertiesFormatException e) {
			GestionExcepciones.mostrarAlerta(TipoExcepcion.ERROR, "Formato incorrecto en el archivo config.xml");
			if(LOG.isErrorEnabled()) {
				LOG.error("Formato incorrecto en el archivo config.xml");
			}
			
		} catch (FileNotFoundException e) {
			GestionExcepciones.mostrarAlerta(TipoExcepcion.ERROR, "config.xml no encontrado");
			if(LOG.isErrorEnabled()) {
				LOG.error("config.xml no encontrado");
			}
			
		} catch (IOException e) {
			GestionExcepciones.mostrarAlerta(TipoExcepcion.ERROR, "Error de lectura del archivo config.xml");
			if(LOG.isErrorEnabled()) {
				LOG.error("Error de lectura del archivo config.xml");
			}
			
		}
		
		
	}

	/**
	 * Método para obtener la configuración de las válvulas del archivo configValvulas.json. Filtra la entrada para no tener ninguna válvula
	 * con número identificativo repetido. En caso de no tener una configuración correcta en el archivo carga valores por defecto.
	 * @return lista de válvulas del sistema
	 */
	public static List<Valvula> getListaValvulas() {
		List<Valvula> listaValvulas = new ArrayList<Valvula>();
		
		try {
			final ObjectMapper objectMapper = new ObjectMapper();		//Usa la librería Jackson para serializar desde un json
			listaValvulas = objectMapper.readValue(new File(pathConfigValvulas), new TypeReference<List<Valvula>>() {
			});
			
		} catch ( IOException e) {
			if (LOG.isDebugEnabled()) {
				LOG.debug(e);
			}
		
		}
		
		for (int i=0;i<listaValvulas.size();i++) { //busca válvulas con números repetidos y las elimina
			for (int j=i+1;j<listaValvulas.size();j++) {
				if(listaValvulas.get(i).getNumeroValvula().equals(listaValvulas.get(j).getNumeroValvula())) {
					listaValvulas.remove(j);
				}
			}
		}
		
		if (listaValvulas.size() == 0) {
			listaValvulas.add(new Valvula(1, false, Seccion.A));
			listaValvulas.add(new Valvula(2, false, Seccion.A));
			listaValvulas.add(new Valvula(3, false, Seccion.B));
			listaValvulas.add(new Valvula(4, false, Seccion.B));
			
			GestionExcepciones.mostrarAlerta(TipoExcepcion.ERROR, "Configuración de válvulas no válida, cargando configuración por defecto");
			if (LOG.isDebugEnabled()) {
				LOG.debug("Cargando confguracion de valvulas por defecto");
			}
		}
		return listaValvulas;
	}

	public static BigDecimal getCapacidadDeposito() {
		return capacidadDeposito;
	}
	
	public static DateTimeFormatter getformatoTiempoLecturaEscritura() {
		return formatoTiempoLecturaEscritura;
	}
	
	public static DateTimeFormatter getFormatoTiempoComando() {
		return formatoTiempoComandos;
	} 
	
	public static DateTimeFormatter getFormatoTiempoLog() {
		return formatoTiempoLog;
	}
	
	public static String getPathComando() {
		return pathComando;
	}
	
	public static String getPathEstado() {
		return pathEstado;
	}
	
	public static String getPathFicheroLog() {
		return pathFicheroLog;
	}
	
	public static long getRefrescoHilos() {
		return refrescoHilos;
	}
	
	public static long getRetrasoProcesos() {
		return retrasoProcesos;
	}

}