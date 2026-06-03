package com.aiquest.juego.ciudades.batalla.controller;

import com.aiquest.estructuras.pilas.Pila;
import com.aiquest.juego.ciudades.batalla.model.Accion;
import com.aiquest.juego.ciudades.batalla.model.TipoAccion;

import java.util.Map;

public class ManagerBatalla {
	Map<TipoAccion, Accion> acciones;

	public static void ejecutarAcciones(Pila<Accion> pilaAcciones) {
		while (!pilaAcciones.isEmpty()) {
			Accion accion = pilaAcciones.pop();
			accion.ejecutar();
		}
	}
}
