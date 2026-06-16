package juego.ciudades.hashing.ui;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import juego.ciudades.hashing.CiudadHashing;
import juego.ciudades.hashing.ElementoHash;
import modelosVista.Vista;

/**
 * Fabrica/construye el MinijuegoHashing:
 *  1) Crea un SlotVista por cada slot de la tabla
 *  2) los reparte por el mapa segun sus posiciones (fila, columna) recibidas.
 *  3) Los agrega a la Vista y crea el MinijuegoHashing.
 */
public class FabricaMinijuegoHashing {

    private FabricaMinijuegoHashing() {} //clase utilitaria estatica


    /**
     * PRE:
     * @param vista           no nula, ya inicializada con jugadorVista
     * @param ciudad          no nula
     * @param elementos       lista no nula con al menos 1 elemento a insertar
     * @param claves          lista no nula de claves a buscar (puede estar vacia)
     * @param posicionesSlots lista no nula con una posicion por cada slot de la tabla
     *                        (cada Point indica x = columna, y = fila, en celdas del mundo)
     *
     * POST: crea los SlotVista en las posiciones indicadas, los agrega a la Vista y registra el MinijuegoHashing.
     *
     * @return el MinijuegoHashing creado (ya registrado en la Vista)
     */
    public static MinijuegoHashing crear(Vista vista, CiudadHashing ciudad, List<ElementoHash> elementos,
                                         List<Integer> claves, List<Point> posicionesSlots){

        if (vista == null) {
            throw new IllegalArgumentException("ERROR: la Vista no puede ser nula.");
        }
        if (ciudad == null) {
            throw new IllegalArgumentException("ERROR: la ciudad no puede ser nula.");
        }
        if (elementos == null || elementos.isEmpty()) {
            throw new IllegalArgumentException("ERROR: se necesita al menos 1 elemento para insertar.");
        }
        if (claves == null) {
            throw new IllegalArgumentException("ERROR: la lista de claves a buscar no puede ser nula.");
        }
        if (posicionesSlots == null || posicionesSlots.size() != ciudad.getCantidadSlots()) {
            throw new IllegalArgumentException("ERROR: se necesita una posicion por cada slot de la tabla.");
        }

        List<SlotVista> slotsVista = new ArrayList<>();

        for (int i = 0; i < ciudad.getCantidadSlots(); i++) {
            Point celda = posicionesSlots.get(i);
            int worldX = celda.x * vista.getTamanio();
            int worldY = celda.y * vista.getTamanio();
            SlotVista sv = new SlotVista(ciudad, i, worldX, worldY);
            slotsVista.add(sv);
            vista.agregarObjeto(sv);
        }

        MinijuegoHashing minijuego = new MinijuegoHashing(ciudad, slotsVista, elementos, claves, vista);

        vista.establecerMinijuego(minijuego);
        return minijuego;
    }


    
}
