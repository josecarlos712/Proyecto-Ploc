package types;

public class Valor {

	private int idValor;
	private long timestamp;
	private boolean obs;
	private int idSensor;
	
	public Valor() {
		super();
	}

	public Valor(int idValor, long timestamp, boolean obs, int idSensor) {
		super();
		this.idValor = idValor;
		this.timestamp = timestamp;
		this.obs = obs;
		this.idSensor = idSensor;
	}

	public int getIdValor() {
		return idValor;
	}

	public void setIdValor(int idValor) {
		this.idValor = idValor;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isObs() {
		return obs;
	}

	public void setObs(boolean obs) {
		this.obs = obs;
	}

	public int getIdSensor() {
		return idSensor;
	}

	public void setIdSensor(int idSensor) {
		this.idSensor = idSensor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idSensor;
		result = prime * result + idValor;
		result = prime * result + (obs ? 1231 : 1237);
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
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
		Valor other = (Valor) obj;
		if (idSensor != other.idSensor)
			return false;
		if (idValor != other.idValor)
			return false;
		if (obs != other.obs)
			return false;
		if (timestamp != other.timestamp)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Valor [idValor=" + idValor + ", timestamp=" + timestamp + ", obs=" + obs + ", idSensor=" + idSensor
				+ "]";
	}
	
	
	
}
