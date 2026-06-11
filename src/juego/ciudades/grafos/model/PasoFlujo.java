package juego.ciudades.grafos.model;

import java.util.Collections;
import java.util.List;

public class PasoFlujo {
    private final List<String> camino;
    private final int cuelloDeBotella;
    private final int[][] flujoDespues;
    private final String descripcion;

    public PasoFlujo(List<String> camino, int cuelloDeBotella, int[][] flujoDespues, String descripcion) {
        this.camino = Collections.unmodifiableList(camino);
        this.cuelloDeBotella = cuelloDeBotella;
        this.flujoDespues = copiarMatriz(flujoDespues);
        this.descripcion = descripcion;
    }

    public List<String> getCamino() {
        return camino;
    }

    public int getCuelloDeBotella() {
        return cuelloDeBotella;
    }

    public int[][] getFlujoDespues() {
        return copiarMatriz(flujoDespues);
    }

    public String getDescripcion() {
        return descripcion;
    }

    private int[][] copiarMatriz(int[][] original) {
        int[][] copia = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copia[i] = original[i].clone();
        }
        return copia;
    }
}
