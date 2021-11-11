package prueba.supuesto8.excepciones;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class GestionExcepciones {
	
	
	public static void mostrarAlerta(TipoExcepcion tipo, String Texto) {
		JFrame dialogo = new JFrame();
		
		switch (tipo) {
		case ERROR:
			JOptionPane.showMessageDialog(dialogo, Texto,"Error",JOptionPane.ERROR_MESSAGE);
			break;
			
		case ADVERTENCIA:
			JOptionPane.showMessageDialog(dialogo, Texto,"Advertencia",JOptionPane.WARNING_MESSAGE);
			break;
			
		case INFO:
			JOptionPane.showMessageDialog(dialogo, Texto,"Informacion del sistema",JOptionPane.WARNING_MESSAGE);
			break;
			
		default:
			break;
		}
	}
	
}
