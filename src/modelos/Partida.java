package modelos;

import java.util.Objects;

import Juego.ciudades.ordenamientos.EstadoDePartida;
import utils.ValidacionesUtiles;

public abstract class Partida {
	//ATRIBUTOS----------------------------------------------------------------------
	private String nombre;
	private Jugador jugador;
	private int puntajeActual;
	private EstadoDePartida estado;

	
	//CONSTRUCTORES-----------------------------------------------------------------
	public Partida(String nombre, Jugador jugador) {
		setNombre(nombre);
		setJugador(jugador);
		setPuntaje(0);
		setEstado(EstadoDePartida.Creado);
	}
	//METODOS DE CLASES-------------------------------------------------------------
	//METODOS GENERALES-------------------------------------------------------------
	
	@Override
	public int hashCode() {
		return Objects.hash(estado, jugador, nombre, puntajeActual);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Partida other = (Partida) obj;
		return estado == other.estado && Objects.equals(jugador, other.jugador) && Objects.equals(nombre, other.nombre)
				&& puntajeActual == other.puntajeActual;
	}
	
	
	@Override
	public String toString() {
		return "Partida [nombre=" + nombre + ", jugador=" + jugador + ", puntajeActual=" + puntajeActual + ", estado="
				+ estado + "]";
	}

	//METODOS DE COMPORTAMIENTO------------------------------------------------------
	
	public abstract void iniciar();

	public abstract void finalizar();
	
	public boolean estaIniciada() {
		return estado==EstadoDePartida.Iniciado;
	}
	//GETTER SIMPLES-----------------------------------------------------------------
	public String getNombre() {
		return nombre;
	}

	public Jugador getJugador() {
		return jugador;
	}

	public int getPuntajeActual() {
		return puntajeActual;
	}

	public EstadoDePartida getEstado() {
		return estado;
	}

	//SETTERS SIMPLES---------------------------------------------------------------
	protected void setEstado(EstadoDePartida estado) {
		this.estado=estado;
	}


	private void setPuntaje(int puntaje) {
		ValidacionesUtiles.validarMayorOIgualACero(puntaje, "El puntaje no puede ser menor a 0");
		this.puntajeActual=puntaje;
	}


	private void setJugador(Jugador jugador) {
		ValidacionesUtiles.esDistintoDeNull(jugador, "El jugador no puede ser nulo");
		this.jugador=jugador;
	}


	private void setNombre(String nombre) {
		ValidacionesUtiles.esDistintoDeNull(nombre, "El nombre no puede ser nulo");
		ValidacionesUtiles.validarMayorAUno(nombre.length(), "El nombre debe ser mas largo");
		this.nombre=nombre;
	}
}
