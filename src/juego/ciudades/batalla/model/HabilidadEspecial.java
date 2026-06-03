package com.aiquest.juego.ciudades.batalla.model;

import com.aiquest.modelos.Jugador;

public interface HabilidadEspecial {
	void activar(Enemigo personaje, Jugador objetivo);
	void activar(Jugador personaje, Enemigo objetivo);
}
