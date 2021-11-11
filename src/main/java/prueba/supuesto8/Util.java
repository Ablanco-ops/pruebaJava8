package prueba.supuesto8;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * Clase con métodos de utilidad para otras clases
 *
 */
public class Util {
	private static final Logger LOG = LogManager.getLogger(Util.class);
	
	public static String saltoLinea = System.lineSeparator();
	
	/**
	 * comprueba que el String recibido tiene una fecha correcta conforme al formato de Configuracion
	 * @param fecha
	 * @return devuelve una fecha o null si la entrada no es correcta
	 */
	public static LocalDateTime compruebaFecha(String fecha) {
		LocalDateTime tiempo= null;
		
		try {
			tiempo = LocalDateTime.parse(fecha, Configuracion.getformatoTiempoLecturaEscritura());
		}
		catch (DateTimeParseException e) {
			if(LOG.isDebugEnabled()) {
				LOG.debug("Fecha no válida: "+fecha);
			}
			
		}
		return tiempo;
	}
	
	/**
	 * Comprueba que la entrada se corresponde con un boolean, si no es correcto informa por consola
	 * @param valvulaEntrada
	 * @return
	 */
	public static boolean testValvula(String valvulaEntrada ){
		if((valvulaEntrada.equalsIgnoreCase("true")) || (valvulaEntrada.equalsIgnoreCase("false"))){
			return Boolean.parseBoolean(valvulaEntrada);
		}
		else{
			if(LOG.isDebugEnabled()) {
				LOG.debug("Estado inicial no válido (valvulas)");
			}
			
			return false;
		}

	}
	
	public static String formatoComando(String comando) {
		String tiempo = LocalDateTime.now().format(Configuracion.getformatoTiempoLecturaEscritura());
		return tiempo + " -- " + comando + " --";
	}
	
	public static String formatoEstado(String estado) {
		String tiempo = LocalDateTime.now().format(Configuracion.getformatoTiempoLecturaEscritura());
		return tiempo + " -- " + estado ;
	}
	
	public static String formatoEntradaLog(String log) {
		String tiempo = LocalDateTime.now().format(Configuracion.getformatoTiempoLecturaEscritura());
		return tiempo + " -- " + log ;
	}
	

}