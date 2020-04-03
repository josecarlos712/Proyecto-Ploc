package types;

public class Sensor {

	private int idSensor;
	private String tipo;
	private int tiempoAct;
	
	public Sensor() {
		super();
	}

	public Sensor(int idSensor, String tipo, int tiempoAct) {
		super();
		this.idSensor = idSensor;
		this.tipo = tipo;
		this.tiempoAct = tiempoAct;
	}

	public int getIdSensor() {
		return idSensor;
	}

	public void setIdSensor(int idSensor) {
		this.idSensor = idSensor;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getTiempoAct() {
		return tiempoAct;
	}

	public void setTiempoAct(int tiempoAct) {
		this.tiempoAct = tiempoAct;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idSensor;
		result = prime * result + tiempoAct;
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sensor other = (Sensor) obj;
		if (idSensor != other.idSensor)
			return false;
		if (tiempoAct != other.tiempoAct)
			return false;
		if (tipo == null) {
			if (other.tipo != null)
				return false;
		} else if (!tipo.equals(other.tipo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Sensores [idSensor=" + idSensor + ", tipo=" + tipo + ", tiempoAct=" + tiempoAct + "]";
	}
	
	
	
}
