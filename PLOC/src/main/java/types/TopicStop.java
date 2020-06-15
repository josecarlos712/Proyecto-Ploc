package types;

public class TopicStop {

	
	private Integer idDron;

	public TopicStop(Integer idDron) {
		super();
		this.idDron = idDron;
	}

	public TopicStop() {
		super();
	}

	public Integer getIdDron() {
		return idDron;
	}

	public void setIdDron(Integer idDron) {
		this.idDron = idDron;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idDron == null) ? 0 : idDron.hashCode());
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
		TopicStop other = (TopicStop) obj;
		if (idDron == null) {
			if (other.idDron != null)
				return false;
		} else if (!idDron.equals(other.idDron))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TopicStop [idDron=" + idDron + "]";
	}
	
	
	
}
