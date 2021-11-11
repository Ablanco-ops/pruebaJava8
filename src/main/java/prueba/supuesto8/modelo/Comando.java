package prueba.supuesto8.modelo;

import java.io.IOException;
import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import prueba.supuesto8.Configuracion;
import prueba.supuesto8.LecturaEscritura;
import prueba.supuesto8.Util;

/**
 * Clase que implementa los comandos que serán procesados por la clase
 * Procesador
 *
 */
public class Comando {
	private static final Logger LOG = LogManager.getLogger(Comando.class);

	private TipoComando comando;
	private String parametro = null;
	private LocalDateTime tiempo;

	public Comando(TipoComando comando, String parametro, LocalDateTime tiempo) {
		this.comando = comando;
		this.parametro = parametro;
		this.tiempo = tiempo;
	}

	public TipoComando getComando() {
		return comando;
	}

	public void setComando(TipoComando comando) {
		this.comando = comando;
	}

	public String getParametro() {
		return parametro;
	}

	public void setParametro(String parametro) {
		this.parametro = parametro;
	}

	public LocalDateTime getTiempo() {
		return tiempo;
	}

	public void setTiempo(LocalDateTime tiempo) {
		this.tiempo = tiempo;
	}

	/**
	 * Este método toString es utilizado para mostrar en el interfaz gráfico los
	 * comandos emitidos.
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(tiempo.format(Configuracion.getFormatoTiempoComando())).append("  ").append(comando).append("  ")
				.append(parametro == null ? "" : parametro);
		return builder.toString();
	}

	/**
	 * Este método toma un String de entrada y lo trocea para extraer el momento en
	 * que fue solicitado, el tipo de comando y su parámetro si lo tuviera. En caso
	 * de que la fecha o el comando no existan o no sean válidos informa de ello por
	 * consola y en el fichero_log.
	 * 
	 * @param entradaComando Es el String de entrada que luego es filtrado.
	 * @return Crea un objeto Comando con esos atributos y lo devuelve, si la
	 *         entrada no es válida devuelve nulo.
	 * @throws IOException propaga una excepción de lectura/escritura.
	 */
	public static Comando crearComando(String entradaComando) throws IOException {
		Comando retornoComando = null;
		String[] splitEntrada = entradaComando.split("--");

		if ((splitEntrada.length != 2) || (Util.compruebaFecha(splitEntrada[0].trim()) == null)) {
			LOG.debug("Entrada de comando no válida: " + entradaComando);
			LecturaEscritura.escribirFicheroLog("Entrada de comando no válida: " + entradaComando);

		} else {
			LocalDateTime tiempo = Util.compruebaFecha(splitEntrada[0].trim());
			String comandoString = splitEntrada[1].trim();
			String[] splitComando = comandoString.split(" ");
			comandoString = splitComando[0];
			String parametro = null;

			if (splitComando.length == 2) {
				parametro = splitComando[1];
			}

			TipoComando comando = null;
			try {
				comando = TipoComando.valueOf(comandoString.toUpperCase());
			} catch (IllegalArgumentException e) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Error de lectura: " + e);
				}
			}

			if (comando != null) {
				retornoComando = new Comando(comando, parametro, tiempo);
				if (LOG.isTraceEnabled()) {
					LOG.trace(retornoComando.toString());
				}
			} 
			
			else {
				if (LOG.isDebugEnabled()) {
					LOG.debug("comando no válido: ");
				}				
			}

		}
		return retornoComando;

	}

}
