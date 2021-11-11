package prueba.supuesto8;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * Clase que realiza las operaciones de lectura y escritura en ficheros.
 *
 */
public class LecturaEscritura {
	private static final Logger LOG = LogManager.getLogger(LecturaEscritura.class);
	
	
	
	public static void escribirComando(String comando) throws IOException {
		try (FileWriter writer = new FileWriter(Configuracion.getPathComando(), true)) {

			writer.write(Util.formatoComando(comando)+Util.saltoLinea);
			escribirFicheroLog("comando "+ comando+ " introducido");
			
			if(LOG.isTraceEnabled()) {
				LOG.trace("comando "+ comando+ " escrito");
			}
		} 
	}
	/**
	 * Método para leer comandos de comandos.txt
	 * @param primeralinea true para devolver sólo la primera linea, false para devolver todas
	 * @return devuelve un String con las lineas solicitadas
	 * @throws IOException excepción de lectura 
	 */
	public static String leerComando(boolean primeralinea) throws IOException {

		FileReader archivo = new FileReader(Configuracion.getPathComando());
		String linea;
		ArrayList<String> listaComandos = new ArrayList<String>();

		try (BufferedReader lector = new BufferedReader(archivo)) {
			if(primeralinea) {
				
				if(LOG.isTraceEnabled()) {
					LOG.trace("comando "+ lector.readLine()+ " leido");
				}
				   return lector.readLine(); 
			}
			else {
				while ((linea = lector.readLine()) != null) {
					listaComandos.add(linea);
					
				}
				if(LOG.isTraceEnabled()) {
					LOG.trace("comandos leidos");
				}
				return String.join(Util.saltoLinea, listaComandos);
			}	
		} 
		
	}
	/**
	 * Metodo para borrar la primera linea del archivo comandos.txt. Lee todas las lineas del archivo y las almacena en una lista, 
	 * a continuación sobreescribe el archivo con "" y despues añade todas las lineas almacenadas excepto la primera.
	 * @throws IOException
	 */
	public static void borrarComando() throws IOException {
		FileReader archivo = new FileReader(Configuracion.getPathComando());
		String linea;
		ArrayList<String> listaComandos = new ArrayList<String>();

		try (BufferedReader lector = new BufferedReader(archivo)) {

			while ((linea = lector.readLine()) != null) {
				listaComandos.add(linea);

			}
		} 

		try (FileWriter writer = new FileWriter(Configuracion.getPathComando(), false)) {
			writer.write("");

		} 
		try (FileWriter writer = new FileWriter(Configuracion.getPathComando(), true)) {

			for (int i = 1; i < listaComandos.size(); i++) {
				writer.write(listaComandos.get(i) + Util.saltoLinea);
			}
		}
		if(LOG.isTraceEnabled()) {
			LOG.trace(listaComandos.get(0)+ " eliminado");
		}

	}

	public static void escribirEstado(String estado) throws IOException {

		try (FileWriter writer = new FileWriter(Configuracion.getPathEstado(), true)) {

			writer.write(Util.formatoEstado(estado)+Util.saltoLinea);
			if(LOG.isTraceEnabled()) {
				LOG.trace(estado+ " escrito");
			}
		} 
	}
	/**
	 * Almacena todas las lineas del archivo estados.txt y devuelve la última, en caso de no haber ninguna devuelve null.
	 * @return	String con la última linea
	 * @throws IOException
	 */
	public static String leerEstado() throws IOException {
		FileReader archivo = new FileReader(Configuracion.getPathEstado());
		try (BufferedReader lector = new BufferedReader(archivo)) {
			String linea;
			ArrayList<String> listaEstados= new ArrayList<String>();
			while ((linea=lector.readLine())!=null) {
				listaEstados.add(linea);
				if(LOG.isTraceEnabled()) {
					LOG.trace(linea+ " añadida a la lista");
				}
			}
			if(listaEstados.size()==0) {
				escribirFicheroLog("Sin estado inicial, arrancando estado inicial por defecto");
				if(LOG.isTraceEnabled()) {
					LOG.trace("Sin registros en estados.txt");
				}
				return null;
						
			}
			else {
				if(LOG.isTraceEnabled()) {
					LOG.trace(listaEstados.get(listaEstados.size()-1)+ " leido");
				}
				return listaEstados.get(listaEstados.size()-1);
			}
			
		}
	}
	
	public static void escribirFicheroLog(String log) throws IOException {
		try(FileWriter writer = new FileWriter(Configuracion.getPathFicheroLog(), true)){
			writer.write(Util.formatoEntradaLog(log)+Util.saltoLinea);
			if(LOG.isTraceEnabled()) {
				LOG.trace(Util.formatoEntradaLog(log)+ " escrito");
			}
		}
	}
	/**
	 * Devuelve las últimas 15 entradas del log
	 * @return Devuelve una lista de String con el contenido de fichero_log.txt
	 * @throws IOException
	 */
	public static List<String> leerLog() throws IOException {
		FileReader archivo = new FileReader(Configuracion.getPathFicheroLog());
		String linea;
		List<String> listaLog = new ArrayList<String>();

		try (BufferedReader lector = new BufferedReader(archivo)) {

			while (((linea = lector.readLine()) != null)) {
				listaLog.add(linea);
			}
		}
		if(listaLog.size()>=15) {
			if(LOG.isTraceEnabled()) {
				LOG.trace("retornada lista con 15 entradas");
			}
			return listaLog.subList(listaLog.size()-15,listaLog.size());
		}
		else if(listaLog.size()>0) {
			if(LOG.isTraceEnabled()) {
				LOG.trace("retornada lista con "+listaLog.size()+" entradas");
			}
			return listaLog.subList(0,listaLog.size());
		}
		else {
			if(LOG.isTraceEnabled()) {
				LOG.trace("no hay entradas de log");
			}
			return null;
		}
		
	}
	

}
