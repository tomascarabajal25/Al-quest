package juego.ciudades.ordenamientos;

import java.util.Collections;
import java.util.List;

import utils.ValidacionesUtiles;

public class OrdenadorSelection<T extends Comparable<T>> extends Ordenador<T> {
	//CONSTRUCTORES-----------------------------------------------------------------
	public OrdenadorSelection(String nombre) {
		super(nombre);
	}
	
	
	
	//METODOS DE COMPORTAMIENTO------------------------------------------------------
	/**
	 * Pre:
	 * elementos no nulo
	 * post:
	 * ordena mediante selectionsort la lista de elementos ingresados
	 */
    @Override
    public void ordenar(List<T> elementos) {
    	ValidacionesUtiles.esDistintoDeNull(elementos, "no se puede ordenar elementos nulos");
        int n = elementos.size();
        for (int i = 0; i < n - 1; i++) {
            int indiceMinimo = i;
            for (int j = i + 1; j < n; j++) {
                if (elementos.get(j).compareTo(elementos.get(indiceMinimo)) < 0) {
                    indiceMinimo = j;
                }
            }
            // solo hacemos el swap si el mínimo no es la posición actual
            if (indiceMinimo != i) {
                Collections.swap(elementos, i, indiceMinimo);
            }
        }
    }
    /**
	 * Pre:
	 * elementos no nulo
	 * administrador no nulo
	 * post:
	 * ordena mediante selectionsort la lista de elementos ingresados
	 * y guarda los pasos mediante el administrador
	 */
    @Override
    public void ordenar(List<T> elementos, AdministradorDePasos<T> administradorPasos) {
    	ValidacionesUtiles.esDistintoDeNull(elementos, "no se puede ordenar elementos nulos");
    	ValidacionesUtiles.esDistintoDeNull(administradorPasos, "El administrador no puede ser nulo");
        int n = elementos.size();
    
        administradorPasos.guardarPaso(new PasoOrdenamiento<>(elementos, -1, -1, "Inicio del ordenamiento"));
        for (int i = 0; i < n - 1; i++) {
            int indiceMinimo = i;
            //administradorPasos.guardarPaso(new PasoOrdenamiento<>(elementos, i, indiceMinimo, "Buscando el menor"));
         // lo comento pq podria estar bien mostrar cada comparacion en el bitmap, pero se vuelve mucha cantidad de bitmaps q no son tan necesarios para el juego
            for (int j = i + 1; j < n; j++) {
                if (elementos.get(j).compareTo(elementos.get(indiceMinimo)) < 0) {
                    indiceMinimo = j;
                    //administradorPasos.guardarPaso(new PasoOrdenamiento<>(elementos, i, indiceMinimo, "Nuevo mínimo encontrado")); 
                    
                }
            }
            // solo hacemos el swap si el mínimo no es la posición actual
            if (indiceMinimo != i) {
                Collections.swap(elementos, i, indiceMinimo);
                administradorPasos.guardarPaso(new PasoOrdenamiento<>(elementos, i, indiceMinimo, "Intercambiando con el mínimo"));
            }
        }
        administradorPasos.guardarPaso(new PasoOrdenamiento<>(elementos, -1, -1, "ordenamiento finalizado"));
    }
    
}