package prueba.supuesto8.modelo;

/**
 * Modelo de las válvulas del sistema
 * @author PracticasSoftware2
 *
 */
public class Valvula {
	private Long numeroValvula;
    private boolean valvulaAbierta = false;
    private Seccion sector;

    public Valvula() {
        super();
    }

    public Valvula(long numeroValvula, boolean valvulaAbierta, Seccion sector) {
        this.numeroValvula = numeroValvula;
        this.valvulaAbierta = valvulaAbierta;
        this.sector = sector;
    }

    public Long getNumeroValvula() {
        return numeroValvula;
    }

    public void setNumeroValvula(long numeroValvula) {
        this.numeroValvula = numeroValvula;
    }

    public boolean isValvulaAbierta() {
        return valvulaAbierta;
    }

    public void setValvulaAbierta(boolean valvulaAbierta) {
        this.valvulaAbierta = valvulaAbierta;
    }

    public Seccion getSector() {
        return sector;
    }

    public void setSector(Seccion sector) {
        this.sector = sector;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		String estadoValvula;
		if (valvulaAbierta) {
			estadoValvula= "Abierta";
		}
		else {
			estadoValvula = "Cerrada";
		}
		builder.append("Valvula ").append(numeroValvula).append(" Sector: ").append(sector).append(" ")
				.append(estadoValvula).append(" | ");
		return builder.toString();
	}
    
}
