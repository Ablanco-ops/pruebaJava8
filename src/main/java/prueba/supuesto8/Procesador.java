package prueba.supuesto8;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import prueba.supuesto8.excepciones.GestionExcepciones;
import prueba.supuesto8.excepciones.TipoExcepcion;
import prueba.supuesto8.modelo.Comando;
import prueba.supuesto8.modelo.Seccion;

/**
 * 
 * Clase utilizada para procesar los comandos almacenados en comandos.txt.
 *
 */
public class Procesador {
	private static final Logger LOG = LogManager.getLogger(Procesador.class);

	private static Estado estado = Estado.getInstance();

	/**
	 * Este método toma las entradas de comandos.txt una a una, la transforma en un
	 * comando, la envía a procesarComando y borra la entrada
	 * 
	 * @throws IOException
	 */
	public static void procesarFicheroComandos() throws IOException {
		String entradaComando = LecturaEscritura.leerComando(true);

		Comando comando = null;

		while (entradaComando != null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Comando " + entradaComando + " leido");
				comando = Comando.crearComando(entradaComando);
			}
			if (comando != null) {
				procesarComando(comando);
			}
			LecturaEscritura.borrarComando();
			entradaComando = LecturaEscritura.leerComando(true);

		}
	}

	private static void procesarComando(Comando comando)  {
		try {
			switch (comando.getComando()) {
			case LLENAR_DEPOSITO:
				if (LOG.isDebugEnabled()) {
					LOG.debug("Llenando deposito");
				}
				LecturaEscritura.escribirFicheroLog("Procesando llenar depósito");
				estado.llenar();
				break;

			case VACIAR_DEPOSITO:
				if (LOG.isDebugEnabled()) {
					LOG.debug("Vaciando deposito");
				}
				LecturaEscritura.escribirFicheroLog("Procesando vaciar depósito");
				estado.vaciar();
				break;

			case SET_SECTOR:
				setSelector(comando.getParametro());
				break;

			case ABRIR_VALVULA:
				abrirValvula(comando.getParametro());
				break;

			case CERRAR_VALVULA:
				cerrarValvula(comando.getParametro());
				break;

			case TERMINAR:
				terminarAplicacion();
				break;

			default:
				break;
			}
		}
		catch (IOException e) {
			if(LOG.isErrorEnabled()) {
				LOG.error("Error de escritura "+e);
			}
			GestionExcepciones.mostrarAlerta(TipoExcepcion.ERROR, "Error de escritura");
		}
		

	}

	private static void setSelector(String selector) throws IOException {
		LecturaEscritura.escribirFicheroLog("Procesando habilitar seccion: " + selector);

		try {
			estado.cambiarSector(Seccion.valueOf(selector.toUpperCase()));
		} catch (IllegalArgumentException e) {

			LecturaEscritura.escribirFicheroLog("Parámetro erroneo en setSelector: " + selector);
			if (LOG.isDebugEnabled()) {
				LOG.debug("Parámetro erroneo en setSelector: " + selector);
			}
		}
	}

	/**
	 * Comprueba que el parámetro del comando es correcto y que la válvula existe y
	 * ordena a estado abrirla.
	 * 
	 * @param entradaValvula
	 * @throws IOException
	 */
	private static void abrirValvula(String entradaValvula) throws IOException {

		long numeroValvula = -1;
		LecturaEscritura.escribirFicheroLog("Procesando abrir válvula: " + entradaValvula);

		try {
			numeroValvula = Long.parseLong(entradaValvula);
		} catch (NumberFormatException e) {
			LecturaEscritura.escribirFicheroLog("Parámetro de válvula incorrecto" + entradaValvula);
			LOG.debug(e);
		}

		if (!estado.existeValvula(numeroValvula)) {
			LecturaEscritura.escribirFicheroLog("Número de válvula incorrecto: " + entradaValvula);
			LOG.debug("Parámetro erroneo en abrirVálvula: " + entradaValvula);
		} else {

			estado.abrirValvula(numeroValvula);
		}

	}

	// TODO: eliminar o cambiar metodo
	private static void cerrarValvula(String entradaValvula) throws IOException {
		long numeroValvula = -1;
		LecturaEscritura.escribirFicheroLog("Procesando cerrar válvula: " + entradaValvula);
		try {
			numeroValvula = Long.parseLong(entradaValvula);
		} catch (Exception e) {
			LOG.debug(e);
			LecturaEscritura.escribirFicheroLog("Parámetro de válvula incorrecto" + entradaValvula);
		}
		if (!estado.existeValvula(numeroValvula)) {
			LecturaEscritura.escribirFicheroLog("Número de válvula incorrecto: " + entradaValvula);
			LOG.debug("Parametro erroneo en cerrarVálvula: " + entradaValvula);
		} else {

			estado.cerrarValvula(numeroValvula);
		}

	}
	
	/**
	 * Este método nos pide confirmación antes de ejecutarse.
	 * @throws IOException
	 */
	private static void terminarAplicacion() throws IOException {
		JFrame ventana= new JFrame();
		Object[] opcionesDialogo = { "Si", "No" };
		int respuesta = JOptionPane.showOptionDialog(ventana, "¿Desea cerrar el sistema?", "Cerrar Sistema",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, 
				opcionesDialogo, 
				opcionesDialogo[1]);
		if (respuesta==0) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Apagando Sistema");
			}
			LecturaEscritura.escribirFicheroLog("Apagando sistema");
			estado.terminar();
		}
	}

}
