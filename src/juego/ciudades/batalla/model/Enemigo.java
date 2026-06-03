package com.aiquest.juego.ciudades.batalla.model;

import com.aiquest.modelos.Jugador;

public abstract class Enemigo implements HabilidadEspecial {
//ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
	private final String nombre;
	private final TipoEnemigo tipo;
	private int vida;
	private int fuerza;
	private int armadura;
	private final HabilidadEspecial habilidad;

//CONSTRUCTORES -------------------------------------------------------------------------------------------
	public Enemigo(String nombre, TipoEnemigo tipo, int vida, int fuerza, int armadura, HabilidadEspecial habilidad) {
		this.nombre = nombre;
		this.tipo = tipo;
		setVida(vida);
		setFuerza(fuerza);
		setArmadura(armadura);
		this.habilidad = habilidad;
	}

//GETTERS SIMPLES -----------------------------------------------------------------------------------------
	public int getVida() { return vida; }

	public int getFuerza() { return fuerza; }

	public int getArmadura() { return armadura; }

	public String getNombre() { return nombre; }

	public TipoEnemigo getTipo() { return tipo; }

	//SETTERS SIMPLES -----------------------------------------------------------------------------------------
	public void setVida(int vida) {
		this.vida = vida;
	}

	public void setFuerza(int fuerza) {
		this.fuerza = fuerza;
	}

	public void setArmadura(int armadura) {
		this.armadura = armadura;
	}

//METODOS HEREDADOS (INTERFACE)----------------------------------------------------------------------------
	public void usarHabilidadEspecial(Jugador jugador) {
		this.habilidad.activar(this, jugador);
	}
//METODOS DE CLASE ----------------------------------------------------------------------------------------

//METODOS GENERALES ---------------------------------------------------------------------------------------
//METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------
//METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------
	public boolean estaVivo() {
		return this.vida > 0;
	}
}
