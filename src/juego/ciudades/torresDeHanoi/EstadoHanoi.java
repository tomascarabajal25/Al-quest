package juego.ciudades.torresDeHanoi;

import utils.ValidacionesUtiles;

/**
 * DTO inmutable que representa una instantánea del estado del juego.
 *
 * Las torres se exponen como int[] donde cada entero es el TAMAÑO del disco
 * (1 = más pequeño, N = más grande) y 0 significa slot vacío.
 *
 * Orden del arreglo: índice 0 = tope de la pila (disco más pequeño presente),
 * último índice no-cero = fondo (disco más grande presente).
 * La vista es responsable de invertir el orden al dibujar si lo necesita.
 */
public class EstadoHanoi {

    private final int[]  torreA;
    private final int[]  torreB;
    private final int[]  torreC;
    private final int    movimientos;
    private final double minMovimientos;

    public EstadoHanoi(int[] torreA, int[] torreB, int[] torreC,
                       int movimientos, double minMovimientos) {
        ValidacionesUtiles.esDistintoDeNull(torreA, "La torre A no puede ser nula");
        ValidacionesUtiles.esDistintoDeNull(torreB, "La torre B no puede ser nula");
        ValidacionesUtiles.esDistintoDeNull(torreC, "La torre C no puede ser nula");

        this.torreA         = torreA.clone();
        this.torreB         = torreB.clone();
        this.torreC         = torreC.clone();
        this.movimientos    = movimientos;
        this.minMovimientos = minMovimientos;
    }

    /** int[] donde cada valor > 0 es el tamaño del disco; 0 = slot vacío. */
    public int[]  getTorreA()        { return torreA.clone(); }
    public int[]  getTorreB()        { return torreB.clone(); }
    public int[]  getTorreC()        { return torreC.clone(); }
    public int    getMovimientos()   { return movimientos; }
    public double getMinMovimientos(){ return minMovimientos; }
}