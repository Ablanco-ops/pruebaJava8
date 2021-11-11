package prueba.supuesto8.GestionAplicacion;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import prueba.supuesto8.Configuracion;
import prueba.supuesto8.Estado;
import prueba.supuesto8.excepciones.GestionExcepciones;
import prueba.supuesto8.excepciones.TipoExcepcion;
import prueba.supuesto8.interfazUsuario.GestionInterfaceUsuario;
import prueba.supuesto8.interfazUsuario.InterfaceUsuario;

/**
 * 
 * Clase encargada de iniciar la aplicacion
 *
 */
public class GestionAplicacion {
	private static final Logger LOG = LogManager.getLogger(ActualizaInterface.class);

	private static Estado estado = Estado.getInstance();
	private static InterfaceUsuario ventana = InterfaceUsuario.getInstance();
	private static ActualizaProcesador actualizaProcesador = new ActualizaProcesador();
	private static ActualizaInterface actualizaInterface = new ActualizaInterface();
	
	/**
	 * Este método inicia la aplicación: carga la configuración, inicia el interface gráfico, fija el estado del sistema y finalmente
	 * inicia los bucles del procesador de comandos y el interface de usuario.
	 * @throws IOException
	 */
	public static void iniciarAplicacion() throws IOException {
		Configuracion.setConfiguracion();
		iniciarInterfaceUsuario();
		estado.setEstado();
		actualizaProcesador();
		try {
			Thread.sleep(Configuracion.getRetrasoProcesos());	//Retraso entre la ejecucion del procesador de comandos y el interface gráfico.
		} catch (InterruptedException e) {
			
			GestionExcepciones.mostrarAlerta(TipoExcepcion.ERROR, "Error en el delay entre los bucles de procesador e interface");
			if (LOG.isErrorEnabled()) {
				LOG.error("Error en el delay entre los bucles de procesador e interface");
			}
		}
		actualizaInterface();
	}

	private static void iniciarInterfaceUsuario() throws IOException {
		ventana.inicializarVentana();		
		GestionInterfaceUsuario.setTexto();
	}

	private static void actualizaProcesador() {
		actualizaProcesador.start();
	}

	private static void actualizaInterface() {
		actualizaInterface.start();
	}

	
}
