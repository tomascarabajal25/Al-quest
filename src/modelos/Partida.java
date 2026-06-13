package modelos;

import java.util.Objects;

import juego.ciudades.ordenamientos.EstadoDePartida;

import utils.ValidacionesUtiles;

public abstract class Partida {
	//ATRIBUTOS----------------------------------------------------------------------
	private String nombre;
	private Jugador jugador;
	private int puntajeActual;
	private EstadoDePartida estado;
	// NUEVO ATRIBUTO PARA EL CALLBACK------------------------------------------------
	private Runnable onFinalizadoCallback;
	/**
	 * nuevo atributo para manejo de skins, por defecto boy
	 */
	private String rutaSprites = "/assets/jugador/boy";
	
	//CONSTRUCTORES-----------------------------------------------------------------

    /**
     * Constructor del TDA Partida
     *
     * PRE:
     * -Nombre y jugador no deben ser nulos
     *
     * @param nombre: Nombre de la partida
     * @param jugador: Jugador de la partida
     */
	public Partida(String nombre, Jugador jugador) {
        ValidacionesUtiles.esDistintoDeNull(nombre, "nombre");
        ValidacionesUtiles.esDistintoDeNull(jugador, "jugador");

		setNombre(nombre);
		setJugador(jugador);
		setPuntaje(0);
		setEstado(EstadoDePartida.Creado);
	}
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
    /**
     * Inicia la partida
     */
	public abstract void iniciar();

    /**
     * Finaliza la partida
     */
	public abstract void finalizar();

    /**
     * Verifica el estado d ela partida
     * @return: Devuelve true si esta iniciada, false si no lo esta
     */
	public boolean estaIniciada() {
		return estado==EstadoDePartida.Iniciado;
	}

	/**
	 * pre: callback no nulo.
	 * post: asigna la acción a ejecutar cuando la partida finalice.
	 */
	public void setOnFinalizadoCallback(Runnable callback) {
		this.onFinalizadoCallback = callback;
	}

	/**
	 * post: ejecuta el callback registrado si existe uno disponible.
	 */
	protected void notificarFinalizacion() {
		if (this.onFinalizadoCallback != null) {
			this.onFinalizadoCallback.run();
		}
	}

	//GETTER SIMPLES-----------------------------------------------------------------
    /**
     * Getter del atributo nombre
     * @return: Devuelve la String atributo
     */
	public String getNombre() {
		return this.nombre;
	}

    /**
     * Getter del atributo jugador
     * @return: Devuelve el objeto jugador
     */
	public Jugador getJugador() {
		return this.jugador;
	}

    /**
     * Getter del atributo puntaje
     * @return: Devuelve el int del atributo
     */
	public int getPuntaje() {
		return this.puntajeActual;
	}

    /**
     * Getter del atributo estado
     * @return: Devuelve el estado guardado en el atributo
     */
	public EstadoDePartida getEstado() {
		return this.estado;
	}


	//GESTION DE SKINS
	/**
	 * @return ruta base de los sprites
	 * para que esta partida construya su Vista
	 */
	public String getRutaSprites(){
        return rutaSprites;
	}
	//SETTERS SIMPLES---------------------------------------------------------------
    /**
     * Setter del atributo estado
     *
     * PRE:
     * -Estado no debe ser nulo
     *
     * @param estado
     */
	protected void setEstado(EstadoDePartida estado) {
		ValidacionesUtiles.esDistintoDeNull(estado, "estado");
        this.estado=estado;
	}

    /**
     * Setter del atributo puntaje
     *
     * PRE:
     * -Puntaje debe ser mayor o igual a cero
     *
     * @param puntaje: nuevo puntaje
     */
	protected void setPuntaje(int puntaje) {
		ValidacionesUtiles.validarMayorOIgualACero(puntaje, "El puntaje no puede ser menor a 0");
		this.puntajeActual=puntaje;
	}

    /**
     * Setter del atributo jugador
     *
     * PRE:
     * -Jugador no debe ser nulo
     *
     * @param jugador: Jugador de la partida
     */
	private void setJugador(Jugador jugador) {
		ValidacionesUtiles.esDistintoDeNull(jugador, "El jugador no puede ser nulo");
		this.jugador=jugador;
	}

    /**
     * Setter del atributo nombre
     *
     * PRE:
     * -Nombre no debe ser nulo y debe cumplir con la longitud minuma
     *
     * @param nombre: Nombre
     */
	private void setNombre(String nombre) {
		ValidacionesUtiles.esDistintoDeNull(nombre, "El nombre no puede ser nulo");
		ValidacionesUtiles.validarMayorAUno(nombre.length(), "El nombre debe ser mas largo");
		this.nombre=nombre;
	}


	/**
	 * GESTION DE SKINS
	 * PRE: rutaSprites nopuede ser null.
	 * POST: proxima Vista que cree iniciar() va a usar esta ruta para sprites
	 * 		 tiene que invocarse antes de iniciar() para que funcione
	 */
	public void setRutaSprites(String rutaSprites) {
		ValidacionesUtiles.esDistintoDeNull(rutaSprites, "La ruta sprites no puede ser nula");
		this.rutaSprites = rutaSprites;
	}

	
	
}
