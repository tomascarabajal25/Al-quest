package modelos;

import java.util.Objects;

public abstract class Entidad {
    private String nombre;

    public Entidad(String nombre) {
        setNombre(nombre);
    }

    @Override
	public String toString() {
		return "Entidad [nombre=" + nombre + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(nombre);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entidad other = (Entidad) obj;
		return Objects.equals(nombre, other.nombre);
	}


	public String getNombre() {
    	return nombre; 
    	}
    private void setNombre(String nombre) {
    	this.nombre=nombre;
    }
    }