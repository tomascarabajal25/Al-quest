package ordenamientos;

import java.util.ArrayList;
import java.util.List;

public class Ordenador {
    protected List<PasoOrdenamiento> historialDePasos = new ArrayList<>();

    protected void guardarPaso(List<Caja> estado, int i1, int i2, String mensaje) {
        historialDePasos.add(new PasoOrdenamiento(estado, i1, i2, mensaje));
    }

    public List<PasoOrdenamiento> getPasos() {
        return historialDePasos;
    }

    // no hace nada, los metodos hijos deben reemplazarlo
    protected void ordenar(List<Caja> cajas) {
        // Vacío 
    }
}