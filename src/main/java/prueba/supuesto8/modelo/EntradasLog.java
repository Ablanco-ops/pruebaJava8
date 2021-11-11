package prueba.supuesto8.modelo;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import prueba.supuesto8.Configuracion;
import prueba.supuesto8.LecturaEscritura;
import prueba.supuesto8.Util;

/**
 * 
 * Clase que implementa el modelo de entradas de log, contiene métodos para mostrarlas y para obtener las entradas a partir de Strings.  
 *
 */
public class EntradasLog {
	private static final Logger LOG = LogManager.getLogger(EntradasLog.class);
	
	private String textoLog;
	private LocalDateTime tiempo;
	
	public EntradasLog(String textoLog, LocalDateTime tiempo) {
		super();
		this.textoLog = textoLog;
		this.tiempo = tiempo;
	}

	public String getTextoLog() {
		return textoLog;
	}

	public void setTextoLog(String textoLog) {
		this.textoLog = textoLog;
	}

	public LocalDateTime getTiempo() {
		return tiempo;
	}

	public void setTiempo(LocalDateTime tiempo) {
		this.tiempo = tiempo;
	}
	
	
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(tiempo.format(Configuracion.getFormatoTiempoLog())).append(" -- ").append(textoLog);
		return builder.toString();
	}




	public static List<EntradasLog> listaEntradasLogs = new ArrayList<EntradasLog>();
	
	/**
	 * Método para obtener un ibjeto entradaLog a partir de un String, comprueba que la entrada sea válida, en caso de que lo sea crea una
	 * nueva entrada y la añade a la lista listaEntradasLogs
	 * @throws IOException
	 */
	public static void getEntradasLog() throws IOException {
		List<String> listaLogs = LecturaEscritura.leerLog();
		
		if(listaLogs != null) {		
			for(String log : listaLogs ) {
				
				String[] splitLog=log.split(" -- ");
				
				if((splitLog.length==2)||(Util.compruebaFecha(splitLog[0].trim())!=null)) {
					listaEntradasLogs.add(new EntradasLog(splitLog[1].trim(), Util.compruebaFecha(splitLog[0].trim())));
				}
				
				else {
					if(LOG.isDebugEnabled()) {
						LOG.debug("Entrada de log erronea: "+log);
					}
				}
			}
		}
		else {
			if(LOG.isDebugEnabled()) {
				LOG.debug("No hay entradas de log ");
			}
		}
	}
	
}
	
	
	
	

