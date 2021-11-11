package prueba.supuesto8.interfazUsuario;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.time.LocalDateTime;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import prueba.supuesto8.Procesador;
import prueba.supuesto8.excepciones.GestionExcepciones;
import prueba.supuesto8.excepciones.TipoExcepcion;

/**
 * 
 * Clase que crea el interface de usuario, es una clase singleton, solo permite una instancia para usar por la app.
 *
 */
public class InterfaceUsuario {
	private static final Logger LOG = LogManager.getLogger(Procesador.class);

	private final LocalDateTime tiempo = LocalDateTime.now();
	
	private JFrame frame;
	private JTextField introComandos = new JTextField();

	private JTextPane panelEstado = new JTextPane();
	private JTextPane panelComandos = new JTextPane();
	private JTextPane panelLog = new JTextPane();
	private JTextPane panelListaComandos = new JTextPane();

	private static InterfaceUsuario ventana;

	public static InterfaceUsuario getInstance() {
		if (ventana == null) {
			ventana = new InterfaceUsuario();
		}
		return ventana;
	}

	public LocalDateTime geTime() {
		return tiempo;
	}

	public void setPaneles(String estado, String comandos, String log, String listaComandos) {

		panelEstado.setText(estado);
		panelComandos.setText(comandos);
		panelLog.setText(log);
		panelListaComandos.setText(listaComandos);
	}
	
	

	public void refrescarPaneles() throws IOException {
		GestionInterfaceUsuario.setTexto();
		panelEstado.repaint();
		panelComandos.repaint();
		panelLog.repaint();
	}

	private void nuevoComando() {
		String nuevoComando = introComandos.getText();
		
		try {
			GestionInterfaceUsuario.addComando(nuevoComando);
		} catch (IOException e) {
			GestionExcepciones.mostrarAlerta(TipoExcepcion.ERROR, "Error al insertar comando");
			if (LOG.isDebugEnabled()) {
				LOG.debug("Error de escritura: " + e);
			}
		}
		introComandos.setText("");
	}
	
	

	/**
	 * @wbp.parser.entryPoint
	 */
	public void inicializarVentana() {
		frame = new JFrame();
		frame.setBounds(100, 100, 820, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel etiquetaEstado = new JLabel("ESTADO");
		etiquetaEstado.setBounds(50, 25, 75, 14);
		frame.getContentPane().add(etiquetaEstado);

		panelEstado.setEditable(false);
		panelEstado.setBounds(50, 50, 720, 50);
		frame.getContentPane().add(panelEstado);

		JLabel etiquetaComandos = new JLabel("COMANDOS");
		etiquetaComandos.setBounds(50, 125, 75, 14);
		frame.getContentPane().add(etiquetaComandos);

		panelComandos.setEditable(false);
		panelComandos.setBounds(50, 150, 350, 225);
		frame.getContentPane().add(panelComandos);

		JLabel etiquetaIntroComandos = new JLabel("LECTURA DE COMANDOS");
		etiquetaIntroComandos.setBounds(50, 495, 140, 14);
		frame.getContentPane().add(etiquetaIntroComandos);

		introComandos.setBounds(50, 520, 350, 20);
		frame.getContentPane().add(introComandos);
		introComandos.setColumns(10);
		introComandos.setActionCommand(null);
		introComandos.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				nuevoComando();
			}
		});

		JLabel etiquetaLog = new JLabel("LOG");
		etiquetaLog.setBounds(410, 125, 46, 14);
		frame.getContentPane().add(etiquetaLog);

		panelLog.setEditable(false);
		panelLog.setBounds(410, 150, 360, 390);
		frame.getContentPane().add(panelLog);
		panelListaComandos.setEditable(false);
		
		
		panelListaComandos.setBounds(50, 420, 350, 64);
		frame.getContentPane().add(panelListaComandos);
		
		JLabel etiquetaListaComandos = new JLabel("LISTA DE COMANDOS");
		etiquetaListaComandos.setBounds(50, 395, 140, 14);
		frame.getContentPane().add(etiquetaListaComandos);

		frame.setVisible(true);
		
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent evento) {
		        try {
					GestionInterfaceUsuario.addComando("terminar");
				} catch (IOException e) {
					GestionExcepciones.mostrarAlerta(TipoExcepcion.ERROR, "Error de escritura");
				}
		    }
		});

	}
}
