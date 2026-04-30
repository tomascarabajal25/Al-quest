package ordenamientos;

import java.util.Collections;
import java.util.List;

public class OrdenadorSelection extends Ordenador {
    @Override
    public void ordenar(List<Caja> cajas) {
        int n = cajas.size();
        for (int i = 0; i < n - 1; i++) {
            int indiceMinimo = i;
            guardarPaso(cajas, i, indiceMinimo, "Buscando el menor");
            for (int j = i + 1; j < n; j++) {
                if (cajas.get(j).getVolumen() < cajas.get(indiceMinimo).getVolumen()) {
                    indiceMinimo = j;
                    guardarPaso(cajas, i, indiceMinimo, "Nuevo mínimo encontrado");
                }
            }
            // Solo hacemos el swap si el mínimo no es la posición actual
            if (indiceMinimo != i) {
                Collections.swap(cajas, i, indiceMinimo);
                guardarPaso(cajas, i, indiceMinimo, "Intercambiando con el mínimo");
            }
        }
    }
}