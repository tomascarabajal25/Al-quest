package juego.ciudades.grafos.model;

import java.util.Collections;
import java.util.List;

public class ResultadoFlujo {
    private final int flujoMaximo;
    private final List<PasoFlujo> pasos;
    private final int[][] flujoFinal;

    public ResultadoFlujo(int flujoMaximo, List<PasoFlujo> pasos, int[][] flujoFinal) {
        this.flujoMaximo = flujoMaximo;
        this.pasos = Collections.unmodifiableList(pasos);
        this.flujoFinal = copiarMatriz(flujoFinal);
    }

    public int getFlujoMaximo() {
        return flujoMaximo;
    }

    public List<PasoFlujo> getPasos() {
        return pasos;
    }

    public int[][] getFlujoFinal() {
        return copiarMatriz(flujoFinal);
    }

    private int[][] copiarMatriz(int[][] original) {
        int[][] copia = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copia[i] = original[i].clone();
        }
        return copia;
    }
}
