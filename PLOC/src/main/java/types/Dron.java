package types;

public class Dron {

	private int idDron;
	private int pesoSoportado;
	private int bateria;
	private String estao;
	private String parkingPath;
	private int idSensor;
	private int idRuta;	
	
	public Dron() {
		super();
	}

	public Dron(int idDron, int pesoSoportado, int bateria, String estao, String parkingPath, int idSensor,
			int idRuta) {
		super();
		this.idDron = idDron;
		this.pesoSoportado = pesoSoportado;
		this.bateria = bateria;
		this.estao = estao;
		this.parkingPath = parkingPath;
		this.idSensor = idSensor;
		this.idRuta = idRuta;
	}

	public int getIdDron() {
		return idDron;
	}

	public void setIdDron(int idDron) {
		this.idDron = idDron;
	}

	public int getPesoSoportado() {
		return pesoSoportado;
	}

	public void setPesoSoportado(int pesoSoportado) {
		this.pesoSoportado = pesoSoportado;
	}

	public int getBateria() {
		return bateria;
	}

	public void setBateria(int bateria) {
		this.bateria = bateria;
	}

	public String getEstao() {
		return estao;
	}

	public void setEstao(String estao) {
		this.estao = estao;
	}

	public String getParkingPath() {
		return parkingPath;
	}

	public void setParkingPath(String parkingPath) {
		this.parkingPath = parkingPath;
	}

	public int getIdSensor() {
		return idSensor;
	}

	public void setIdSensor(int idSensor) {
		this.idSensor = idSensor;
	}

	public int getIdRuta() {
		return idRuta;
	}

	public void setIdRuta(int idRuta) {
		this.idRuta = idRuta;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bateria;
		result = prime * result + ((estao == null) ? 0 : estao.hashCode());
		result = prime * result + idDron;
		result = prime * result + idRuta;
		result = prime * result + idSensor;
		result = prime * result + ((parkingPath == null) ? 0 : parkingPath.hashCode());
		result = prime * result + pesoSoportado;
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
		Dron other = (Dron) obj;
		if (bateria != other.bateria)
			return false;
		if (estao == null) {
			if (other.estao != null)
				return false;
		} else if (!estao.equals(other.estao))
			return false;
		if (idDron != other.idDron)
			return false;
		if (idRuta != other.idRuta)
			return false;
		if (idSensor != other.idSensor)
			return false;
		if (parkingPath == null) {
			if (other.parkingPath != null)
				return false;
		} else if (!parkingPath.equals(other.parkingPath))
			return false;
		if (pesoSoportado != other.pesoSoportado)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Drones [idDron=" + idDron + ", pesoSoportado=" + pesoSoportado + ", bateria=" + bateria + ", estao="
				+ estao + ", parkingPath=" + parkingPath + ", idSensor=" + idSensor + ", idRuta=" + idRuta + "]";
	}
	
	
	
}
