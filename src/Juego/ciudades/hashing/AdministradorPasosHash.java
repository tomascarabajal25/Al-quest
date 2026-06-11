package juego.ciudades.hashing;

import java.util.ArrayList;
import java.util.List;

/**
 * Se ocupa de mantener el historial de PasoHash que fueron producidos durante la partida.
 * La consola (o UI cuando la apliquemos) consulta esta lista para mostrar el paso a paso.
 * Mismo patron que AdministradorDePasos de ordenamientos.
 */

public class AdministradorPasosHash {
    //ATRIBUTOS
    //Inmutable
    private final List<PasoHash> pasos;


    //CONSTRUCTORES
    public AdministradorPasosHash() {
        this.pasos = new ArrayList<>();
    }


    //METODOS DE COMPORTAMIENTO
    /**
     * Agrego un paso al historial.
     * PRE: El paso no puede ser nulo.
     * POST: El paso queda al final de la lista.
     */
    public void agregarPaso(PasoHash paso) {
        //Manejo error:
        if (paso == null) {
            throw new IllegalArgumentException("ERROR: El paso no puede ser nuloo.");
        }

        //Si no fue nulo, agrego el paso
        this.pasos.add(paso);
    }

    /**
     * Limpia el historial
     */
    public void limpiar() {
        this.pasos.clear();
    }


    //GETTERS
    public List<PasoHash> getPasos() {
        return this.pasos;
    }

    public PasoHash getUltimoPaso() {
        if (this.pasos.isEmpty()){
            return null;
        }

        return this.pasos.get(this.pasos.size() -1);
    }

    public int getCantidadPasos() {
        return this.pasos.size();
    }

}
