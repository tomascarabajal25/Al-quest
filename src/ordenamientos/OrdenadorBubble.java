package ordenamientos;

import java.util.Collections;
import java.util.List;

public class OrdenadorBubble<T extends Comparable<T>> extends Ordenador<T> { 
	//CONSTRUCTORES-----------------------------------------------------------------
	public OrdenadorBubble(String nombre) {
		super(nombre);
	}
	
	//METODOS DE COMPORTAMIENTO------------------------------------------------------
	/**
	 * Pre:
	 * elementos no nulo
	 * post:
	 * ordena mediante BubbleSort la lista de elementos ingresados
	 */
    @Override
    public void ordenar(List<T> elementos) {
        int n = elementos.size();

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (elementos.get(j).compareTo(elementos.get(j + 1)) > 0) {
                    Collections.swap(elementos, j, j + 1);
                }
            }
        }
    }
    /**
	 * Pre:
	 * elementos no nulo
	 * administrador no nulo
	 * post:
	 * ordena mediante BubbleSort la lista de elementos ingresados
	 * y guarda los pasos mediante el administrador
	 */
    @Override
    public void ordenar(List<T> elementos, AdministradorDePasos<T> administradorPasos) {
        int n = elementos.size();
        
        administradorPasos.guardarPaso(new PasoOrdenamiento<>(elementos, -1, -1, "Inicio del ordenamiento"));

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                
                //administradorPasos.guardarPaso(new PasoOrdenamiento<>(elementos, j, j + 1, "Comparando elementos"));
                
                if (elementos.get(j).compareTo(elementos.get(j + 1)) > 0) {
                    Collections.swap(elementos, j, j + 1);
                    
                    administradorPasos.guardarPaso(new PasoOrdenamiento<>(elementos, j, j + 1, "Intercambiando elementos"));
                }
            }
        }
        administradorPasos.guardarPaso(new PasoOrdenamiento<>(elementos, -1, -1, "Ordenamiento finalizado"));
    }
    
}