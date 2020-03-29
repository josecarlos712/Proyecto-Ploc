package types;

public class SensorProximidad {

	private int idSensor;
	private long timeStamp;
	private Boolean obstaculo;
	
	public SensorProximidad() {
		super();
	}

	public SensorProximidad(int idSensor, long timeStamp, Boolean obstaculo) {
		super();
		this.idSensor = idSensor;
		this.timeStamp = timeStamp;
		this.obstaculo = obstaculo;
	}

	public int getIdSensor() {
		return idSensor;
	}

	public void setIdSensor(int idSensor) {
		this.idSensor = idSensor;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Boolean getObstaculo() {
		return obstaculo;
	}

	public void setObstaculo(Boolean obstaculo) {
		this.obstaculo = obstaculo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idSensor;
		result = prime * result + ((obstaculo == null) ? 0 : obstaculo.hashCode());
		result = prime * result + (int) (timeStamp ^ (timeStamp >>> 32));
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
		SensorProximidad other = (SensorProximidad) obj;
		if (idSensor != other.idSensor)
			return false;
		if (obstaculo == null) {
			if (other.obstaculo != null)
				return false;
		} else if (!obstaculo.equals(other.obstaculo))
			return false;
		if (timeStamp != other.timeStamp)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SensorProximidad [idSensor=" + idSensor + ", timeStamp=" + timeStamp + ", obstaculo=" + obstaculo + "]";
	}
	
	
}
