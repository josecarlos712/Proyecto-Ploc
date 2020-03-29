package types;

public class Ruta {

	private int idRuta;
	private long tiempoRuta; //El tiempo de la ruta viene dado en segundos, por eso usamos un long
	private String path; //El path será una serie de indicaciones que luego se interpretarán antes de mandarse
	
	public Ruta() {
		super();
	}

	public Ruta(int idRuta, long tiempoRuta, String path) {
		super();
		this.idRuta = idRuta;
		this.tiempoRuta = tiempoRuta;
		this.path = path;
	}

	public int getIdRuta() {
		return idRuta;
	}

	public void setIdRuta(int idRuta) {
		this.idRuta = idRuta;
	}

	public long getTiempoRuta() {
		return tiempoRuta;
	}

	public void setTiempoRuta(long tiempoRuta) {
		this.tiempoRuta = tiempoRuta;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idRuta;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result + (int) (tiempoRuta ^ (tiempoRuta >>> 32));
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
		Ruta other = (Ruta) obj;
		if (idRuta != other.idRuta)
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		if (tiempoRuta != other.tiempoRuta)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Ruta [idRuta=" + idRuta + ", tiempoRuta=" + tiempoRuta + ", path=" + path + "]";
	}
	
	
	
}
