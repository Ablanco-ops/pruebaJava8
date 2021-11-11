package prueba.supuesto8.GestionAplicacion;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import prueba.supuesto8.Configuracion;
import prueba.supuesto8.interfazUsuario.InterfaceUsuario;

/**
 * Clase encargada de actualizar el interface gráfico
 * 
 *
 */
public class ActualizaInterface extends Thread {
private static final Logger LOG = LogManager.getLogger(ActualizaInterface.class);
	
	public boolean continuar=true;
	private static  InterfaceUsuario ventana = InterfaceUsuario.getInstance();
	


	@Override
	public void run() {
		
		while(continuar) {
			try {
				ventana.refrescarPaneles();
				Thread.sleep(Configuracion.getRefrescoHilos());
			} catch (InterruptedException | IOException e) {
				if(LOG.isErrorEnabled()) {
					LOG.error("Error en bucle de procesador: "+e);
				}
			}
		}
	}
	
	
}
