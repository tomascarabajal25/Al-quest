package juego.ciudades.grafos.model;

import java.util.Collections;
import java.util.List;

public class ResultadoCamino {
    private final List<String> camino;
    private final double costoTotal;
    private final List<PasoCamino> pasos;

    public ResultadoCamino(List<String> camino, double costoTotal, List<PasoCamino> pasos) {
        this.camino = Collections.unmodifiableList(camino);
        this.costoTotal = costoTotal;
        this.pasos = Collections.unmodifiableList(pasos);
    }

    public List<String> getCamino() {
        return camino;
    }

    public double getCostoTotal() {
        return costoTotal;
    }

    public List<PasoCamino> getPasos() {
        return pasos;
    }
}
