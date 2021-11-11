package prueba.supuesto8.GestionAplicacion;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import prueba.supuesto8.Configuracion;
import prueba.supuesto8.Procesador;

/**
 * 
 * Clase encargada del bucle del procesador de comandos
 *
 */
public class ActualizaProcesador extends Thread {
	
	private static final Logger LOG = LogManager.getLogger(ActualizaProcesador.class);
	
	boolean continuar=true;
	
	@Override
	public void run() {
		
		while(continuar) {
			try {
				Procesador.procesarFicheroComandos();
				Thread.sleep(Configuracion.getRefrescoHilos());
			} catch (IOException | InterruptedException e) {
				if(LOG.isErrorEnabled()) {
					LOG.error("Error en bucle de procesador: "+e);
				}
			}
		}
	}

}
