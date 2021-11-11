package prueba.supuesto8.interfazUsuario;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import prueba.supuesto8.Estado;
import prueba.supuesto8.LecturaEscritura;
import prueba.supuesto8.Util;
import prueba.supuesto8.excepciones.GestionExcepciones;
import prueba.supuesto8.excepciones.TipoExcepcion;
import prueba.supuesto8.modelo.Comando;
import prueba.supuesto8.modelo.EntradasLog;
import prueba.supuesto8.modelo.TipoComando;

/**
 * 
 * Clase para el control de las entradas y salidas de datos del interface gráfico.
 *
 */
public class GestionInterfaceUsuario {
	private static final Logger LOG = LogManager.getLogger(GestionInterfaceUsuario.class);

	private static List<Comando> listaComandos = new ArrayList<Comando>();
	private static Estado estado=Estado.getInstance();	
	private static InterfaceUsuario ventana = InterfaceUsuario.getInstance();
	private static String listaComandosDelimiter =" , ";
	
	/**
	 * Fija el texto de los paneles del interface gráfico. 
	 */
	public static void setTexto(){ 
		try {
			ventana.setPaneles(estado.toString(), mostrarComandos(),
					mostrarLog(ventana.geTime()),setListaComandos());
		} catch (IOException e) {
			GestionExcepciones.mostrarAlerta(TipoExcepcion.ERROR, "Error de Lectura de archivos");
			if(LOG.isErrorEnabled()) {
				LOG.error("Error de lectura de archivos :"+e);
			}
		}
	}
	
	/**
	 * Recoge el comando introducido en el interface gráfico, lo formatea y en caso de ser válido lo envía a LecturaEscritura para escribirlo
	 *  en comandos.txt. También lo almacena en una lista para mostrarlo en pantalla.
	 * @param entrada
	 * @throws IOException
	 */
	public static void addComando(String entrada) throws IOException {
		try {
			Comando comando = Comando.crearComando(Util.formatoComando(entrada));
			if (comando != null) {
				listaComandos.add(Comando.crearComando(Util.formatoComando(entrada)));
				LecturaEscritura.escribirComando(entrada);
				if (LOG.isDebugEnabled()) {
					LOG.debug("Comando " + entrada + " introducido");
				}
			} else {
				LecturaEscritura.escribirFicheroLog("Comando ["+entrada+"] no válido");
				if (LOG.isDebugEnabled()) {
					LOG.debug("Comando " + entrada + " no válido");
				}
			}
		}
		catch (IOException e) {
			GestionExcepciones.mostrarAlerta(TipoExcepcion.ERROR, "Error de escritura de archivos");
			if(LOG.isErrorEnabled()) {
				LOG.error("Error de escritura de archivos :"+e);
			}
		}
		
	}
	
	/**
	 * Recoge las entradas del log de fichero_log.txt filtra las que son posteriores al inicio de la aplicación y las muestra en pantalla
	 * @param tiempo
	 * @return
	 * @throws IOException
	 */
	public static String mostrarLog(LocalDateTime tiempo) throws IOException {
		List<String> entradasLogActuales = new ArrayList<String>();
		EntradasLog.getEntradasLog();
		if (EntradasLog.listaEntradasLogs.size() > 0) {
			
			for (EntradasLog log : EntradasLog.listaEntradasLogs) {
				if (log.getTiempo().isAfter(tiempo)&&(!entradasLogActuales.contains(log.toString()))) {
					entradasLogActuales.add(log.toString());
					Collections.reverse(entradasLogActuales);
				}
			}
			return String.join(Util.saltoLinea, entradasLogActuales);
			
		}
		else {
			return "";
			
		}
		
	}

	public static String mostrarComandos() {
		List<String> listaComandoStrings = new ArrayList<String>();
		if (listaComandos.size() > 0) {
			for (Comando comando : listaComandos) {
				listaComandoStrings.add(comando.toString());
			}
			Collections.reverse(listaComandoStrings);
			return String.join(Util.saltoLinea, listaComandoStrings);
		} else {
			return "";
		}
	}
	
	public static String setListaComandos() {
		List<String> listaComandoStrings = new ArrayList<>();
		for(TipoComando tipoComando: TipoComando.values()) {
			listaComandoStrings.add(tipoComando.name());
		}
		return String.join(listaComandosDelimiter, listaComandoStrings);
	}

}
