package prueba.supuesto8;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import prueba.supuesto8.excepciones.GestionExcepciones;
import prueba.supuesto8.excepciones.TipoExcepcion;
import prueba.supuesto8.modelo.Seccion;
import prueba.supuesto8.modelo.Valvula;

/**
 * 
 * Clase que gestiona el estado del sistema, recibe entradas del procesador de
 * comandos, comprueba que la orden es válida y si lo es cambia de estado. Es
 * una clase Singleton, sólo puede instanciarse una vez porque solo hay un
 * sistema que controlar y se accede siempre a la misma instancia de él.
 *
 */
public class Estado {
	private static final Logger LOG = LogManager.getLogger(Estado.class);

	private BigDecimal volumenDeposito = BigDecimal.valueOf(0);
	private Seccion selectorSeccion = Seccion.A;
	private final List<Valvula> listaValvulas;

	private static Estado estado;

	private Estado() { // Obtiene la lista de válvulas de la configuración
		this.listaValvulas = Configuracion.getListaValvulas();

	};

	/**
	 * Comprueba si hay ya una instancia de la clase estado, si la hay la devuelve y
	 * si no la hay crea una.
	 * 
	 * @return instancia de Estado
	 */
	public static Estado getInstance() {
		if (estado == null) {
			estado = new Estado();
			try {
				estado.setEstado();
			} catch (IOException e) {

				GestionExcepciones.mostrarAlerta(TipoExcepcion.ERROR, "Error de escritura al fijar el estado inicial");
				if (LOG.isErrorEnabled()) {
					LOG.error("Error de escritura al fijar el estado inicial " + e);
				}
			}
		}

		return estado;
	}

	/**
	 * Comprueba si el número de válvula está en la lista de válvulas del sistema
	 * "listaValvulas".
	 * 
	 * @param numeroValvula número de válvula a comprobar
	 * @return devuelve true si encuentra la válvula y flase en caso contrario
	 */
	public boolean existeValvula(Long numeroValvula) {
		boolean existe = false;

		for (Valvula valvula : listaValvulas) {

			if (valvula.getNumeroValvula().equals(numeroValvula)) {
				existe = true;
				break;
			}
		}
		if (LOG.isTraceEnabled()) {
			LOG.trace("La valvula " + numeroValvula + " existe: " + existe);
		}
		return existe;
	}

	/**
	 * Comprueba si hay alguna válvula abierta
	 * 
	 * @return true si hay válvulas abiertas, false si no las hay.
	 * @throws IOException
	 */
	private boolean valvulasAbiertas() throws IOException {
		int numeroValvulasAbiertas = 0;
		boolean valvulasAbiertas = false;
		for (Valvula valvula : listaValvulas) {
			if (valvula.isValvulaAbierta()) {
				numeroValvulasAbiertas++;
				if (LOG.isTraceEnabled()) {
					LOG.trace("Valvula " + valvula.getNumeroValvula() + " abierta");
				}
			}
		}
		if (numeroValvulasAbiertas > 1) {
			valvulasAbiertas = true;
			LecturaEscritura.escribirFicheroLog("Demasiadas válvulas abiertas");
			if (LOG.isDebugEnabled()) {
				LOG.debug("Demasiadas válvulas abiertas");
			}
		}
		
		return valvulasAbiertas;
	}

	/**
	 * Comprueba si hay válvulas abiertas en el sector
	 * 
	 * @param seccion, sección a comrobar.
	 * @return true si hay válvulas abiertas, false si no.
	 */
	private boolean valvulasSeccion(Seccion seccion) {
		boolean valvulasAbiertas = false;
		for (Valvula valvula : listaValvulas) {
			if ((valvula.isValvulaAbierta()) && (valvula.getSector().equals(seccion))) {
				if (LOG.isTraceEnabled()) {
					LOG.trace("Valvula " + valvula.getNumeroValvula() + " abierta");
				}
				valvulasAbiertas = true;
			}
		}
		return valvulasAbiertas;
	}

	/**
	 * Comprueba si el último estado del fichero estados.txt es correcto filtrando
	 * la entrada. Si no hay ninguna entrada o la entrada no es válida inicia en el
	 * estado por defecto.
	 * 
	 * @throws IOException excepción de lectura/escritura
	 */
	public void setEstado() throws IOException {
		String entradaEstado = LecturaEscritura.leerEstado();
		if (entradaEstado == null) {
			escribirEstado();
			setEstado();

			GestionExcepciones.mostrarAlerta(TipoExcepcion.INFO, "No hay estado inicial en estados.txt");
			if (LOG.isInfoEnabled()) {
				LOG.info("No hay estado inicial en estados.txt");
			}
		} else {
			String[] listaEntradaEstado = entradaEstado.split(" -- ");

			if ((listaEntradaEstado.length != 2) || (Util.compruebaFecha(listaEntradaEstado[0].trim()) == null)) {
				LecturaEscritura.escribirFicheroLog("Estado inicial no válido " + entradaEstado);
				if (LOG.isDebugEnabled()) {
					LOG.debug("Estado inicial no válido");
				}

			} else {
				String[] listaEstado = listaEntradaEstado[1].split("/");
				if (listaEstado.length != 2 + listaValvulas.size()) {
					LecturaEscritura.escribirFicheroLog("Estado inicial no válido " + entradaEstado);
					if (LOG.isDebugEnabled()) {
						LOG.debug("Estado inicial no válido " + entradaEstado);
					}

				} else {
					try {
						volumenDeposito = new BigDecimal(listaEstado[0]);
					} catch (NumberFormatException e) {
						LecturaEscritura.escribirFicheroLog("Volumen de depósto no válido");
						if (LOG.isDebugEnabled()) {
							LOG.debug("Volumen de depósto no válido " + e);
						}

					}
					try {
						selectorSeccion = Seccion.valueOf(listaEstado[1]);
					} catch (IllegalArgumentException e) {
						LecturaEscritura.escribirFicheroLog("Estado inicial no válido (seccion)");
						if (LOG.isDebugEnabled()) {
							LOG.debug("Estado inicial no válido (seccion) " + e);
						}
					}
					for (int i = 0; i < listaValvulas.size(); i++) {
						listaValvulas.get(i).setValvulaAbierta(Util.testValvula(listaEstado[i + 2]));
						if (LOG.isTraceEnabled()) {
							LOG.trace("Valvula " + listaValvulas.get(i) + " abierta "
									+ listaValvulas.get(i).isValvulaAbierta());
						}
					}
				}
			}

			valvulasAbiertas();
		}

	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Volumen Deposito: ").append(volumenDeposito).append("l ").append(" | Seccion:")
				.append(selectorSeccion).append("\n").append(listaValvulas);
		return builder.toString();
	}

	/**
	 * Envía a LecturaEscritura el estado actual para que lo excriba en el fichero
	 * estados.txt
	 * 
	 * @throws IOException
	 */
	private void escribirEstado() throws IOException {
		String seccion = selectorSeccion == Seccion.A ? "A" : "B";
		String valvulas = "";
		for (Valvula valvula : listaValvulas) {
			valvulas = valvulas + "/" + valvula.isValvulaAbierta();
		}
		String muestra = volumenDeposito + "/" + seccion + valvulas;
		LecturaEscritura.escribirEstado(muestra);
		if (LOG.isTraceEnabled()) {
			LOG.trace("Solicitado escribir estado " + muestra);
		}
	}

	public void llenar() throws IOException {
		volumenDeposito = Configuracion.getCapacidadDeposito();// Obtiene la capaciadad del depósito de configuración
		escribirEstado();
		LecturaEscritura.escribirFicheroLog("Depósito lleno");
		if (LOG.isInfoEnabled()) {
			LOG.info("Deposito lleno");
		}
	}

	public void vaciar() throws IOException {

		if (valvulasAbiertas()) {
			LecturaEscritura.escribirFicheroLog("Valvulas abiertas, cerrar antes de vaciar deposito");
			if (LOG.isDebugEnabled()) {
				LOG.debug("Demasiadas válvulas abiertas");
			}
		} else {
			volumenDeposito = BigDecimal.valueOf(0);
			LecturaEscritura.escribirFicheroLog("Depósito vacio");
			escribirEstado();
			if (LOG.isInfoEnabled()) {
				LOG.info("Deposito vacio");
			}
		}

	}

	public void cambiarSector(Seccion seccion) throws IOException {
		if (valvulasAbiertas()) {
			LecturaEscritura.escribirFicheroLog("Valvulas abiertas, cerrar antes de cambiar de sección");
			if (LOG.isDebugEnabled()) {
				LOG.debug("Valvulas abiertas, cerrar antes de cambiar de sección");
			}
		} else {
			selectorSeccion = seccion;
			escribirEstado();
			LecturaEscritura.escribirFicheroLog("Sección " + seccion + " habilitada");
			if (LOG.isInfoEnabled()) {
				LOG.info("Sección " + seccion + " habilitada");
			}
		}

	}

	public void abrirValvula(long numeroValvula) throws IOException {

		if (valvulasSeccion(selectorSeccion)) {
			LecturaEscritura.escribirFicheroLog("Valvulas abierta en ese sector, cerrar antes de abrir otra válvula");
			LOG.error("Valvulas abierta, cerrar antes de abrir otra válvula");
			if (LOG.isDebugEnabled()) {
				LOG.debug("Valvulas abiertas, cerrar antes de abrir otra válvula");
			}

		} else {

			for (Valvula valvula : listaValvulas) {
				if (valvula.getNumeroValvula() == numeroValvula) {
					valvula.setValvulaAbierta(true);

					escribirEstado();

					LecturaEscritura.escribirFicheroLog("Válvula " + numeroValvula + " abierta");
					if (LOG.isInfoEnabled()) {
						LOG.info("Válvula " + numeroValvula + " abierta");
					}
				}
			}
		}
	}

	public void cerrarValvula(long numeroValvula) throws IOException {

		for (Valvula valvula : listaValvulas) {
			if (valvula.getNumeroValvula() == numeroValvula) {
				valvula.setValvulaAbierta(false);

				escribirEstado();

				LecturaEscritura.escribirFicheroLog("Válvula " + numeroValvula + " cerrada");
				if (LOG.isInfoEnabled()) {
					LOG.info("Válvula " + numeroValvula + " cerrada");
				}

			}
		}
	}
	
	public void terminar() throws IOException {
		for (Valvula valvula: listaValvulas) {
			if(valvula.isValvulaAbierta()) {
				cerrarValvula(valvula.getNumeroValvula());
			}
		}
		estado.vaciar();
		escribirEstado();
		LecturaEscritura.escribirFicheroLog("Finalizado apagado del sistema");
		
		if (LOG.isInfoEnabled()) {
			LOG.info("Finalizado apagado del sistema");
		}
		LecturaEscritura.borrarComando();
		System.exit(0);
	}
	
	

}
