package torresDeHanoi;

import utils.ValidacionesUtiles;

/**
 * Representa una instantánea (DTO) del estado actual del juego.
 * Es inmutable para garantizar que la vista no pueda alterar el modelo.
 */
public class EstadoHanoi {
    // 1. Atributos PRIVADOS y FINALES (no se pueden modificar después de creados)
    private final String[] torreA;
    private final String[] torreB;
    private final String[] torreC;
    private final int movimientos;
    private final double minMovimientos;

    // 2. Constructor que obliga a pasarle la "foto" completa de los datos
    public EstadoHanoi(String[] torreA, String[] torreB, String[] torreC, int movimientos, double minMovimientos) {
        ValidacionesUtiles.esDistintoDeNull(torreA, "La torre A no puede ser nula");
        ValidacionesUtiles.esDistintoDeNull(torreB, "La torre B no puede ser nula");
        ValidacionesUtiles.esDistintoDeNull(torreC, "La torre C no puede ser nula");
        
        // Clonamos los arreglos para que nadie desde afuera pueda modificar el array original
        this.torreA = torreA.clone();
        this.torreB = torreB.clone();
        this.torreC = torreC.clone();
        this.movimientos = movimientos;
        this.minMovimientos = minMovimientos;
    }

    // 3. SOLO GETTERS (Sin Setters, nadie puede alterarlo)
    public String[] getTorreA() { return torreA.clone(); }
    public String[] getTorreB() { return torreB.clone(); }
    public String[] getTorreC() { return torreC.clone(); }
    public int getMovimientos() { return movimientos; }
    public double getMinMovimientos() { return minMovimientos; }
}