package ordenamientos;

import java.util.ArrayList;
import java.util.List;

public class PasoOrdenamiento {
    private List<Caja> cajasEnEstePaso;
    private int indice1;
    private int indice2;
    private String accion; 

    /*
     * Constructor
     * Post:
     * -Guarda una copia de como se encuentra la lista dsp del intercambio
     */
    public PasoOrdenamiento(List<Caja> estadoActual, int i1, int i2, String accion) {
        this.cajasEnEstePaso = new ArrayList<>(estadoActual);
        this.indice1 = i1;
        this.indice2 = i2;
        this.accion = accion;
    }

    /*
     * 
     */
    public List<Caja> getCajasEnEstePaso() {
        return cajasEnEstePaso;
    }

    public int getIndice1() { return indice1; }
    public int getIndice2() { return indice2; }
    public String getAccion() { return accion; }
}