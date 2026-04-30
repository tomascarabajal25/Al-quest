package ordenamientos;

import java.util.Collections;
import java.util.List;

public class OrdenadorBubble extends Ordenador {
    @Override
    public void ordenar(List<Caja> cajas) {
        int n = cajas.size();
        guardarPaso(cajas, -1, -1, "Inicio del ordenamiento");
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
            	guardarPaso(cajas, j, j + 1, "Comparando volúmenes");
                if (cajas.get(j).getVolumen() > cajas.get(j + 1).getVolumen()) {
                    Collections.swap(cajas, j, j + 1);
                    guardarPaso(cajas, j, j + 1, "Intercambiando cajas");
                }
            }
        }
        guardarPaso(cajas, -1, -1, "Ordenamiento finalizado");
    }
}