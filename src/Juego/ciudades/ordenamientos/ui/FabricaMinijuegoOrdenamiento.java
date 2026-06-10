package Juego.ciudades.ordenamientos.ui;

import java.util.ArrayList;
import java.util.List;

import Juego.ciudades.ordenamientos.Caja;
import Juego.ciudades.ordenamientos.Ordenador;
import modelosVista.Vista;

/**
 * Fábrica que construye el MinijuegoOrdenamiento:
 *   1. Crea las CajaVista a partir de una lista de Caja
 *   2. Las posiciona en el mundo en una fila horizontal centrada
 *   3. Crea el MinijuegoOrdenamiento y lo registra en la Vista
 *
 * Uso típico desde la ciudad:
 * <pre>
 *   List&lt;Caja&gt; cajas = List.of(new Caja("A",40), new Caja("B",10), ...);
 *   Ordenador&lt;Caja&gt; ord = new OrdenadorBubble&lt;&gt;("Bubble Sort");
 *   FabricaMinijuegoOrdenamiento.crear(vista, cajas, ord, filaBase, colInicio);
 * </pre>
 */
public class FabricaMinijuegoOrdenamiento {

    private FabricaMinijuegoOrdenamiento() {}   // utilidad estática

    /**
     * Pre:
     * @param vista      no nula, ya inicializada con jugadorVista
     * @param cajas      lista no nula con al menos 2 elementos
     * @param ordenador  no nulo
     * @param filaBase   fila del mundo donde se colocan las cajas (en celdas)
     * @param colInicio  columna del mundo donde empieza la primera caja (en celdas)
     *
     * Post:
     * Crea las CajaVista, las agrega a la Vista y registra el MinijuegoOrdenamiento.
     * Las cajas quedan separadas 1 celda entre sí horizontalmente.
     *
     * @return el MinijuegoOrdenamiento creado (ya registrado en la Vista)
     */
    public static MinijuegoOrdenamiento crear(Vista vista,
                                               List<Caja> cajas,
                                               Ordenador<Caja> ordenador,
                                               int filaBase,
                                               int colInicio) {
        if (vista    == null) throw new IllegalArgumentException("Vista no puede ser nula");
        if (cajas    == null || cajas.size() < 2)
            throw new IllegalArgumentException("Se necesitan al menos 2 cajas");
        if (ordenador == null) throw new IllegalArgumentException("Ordenador no puede ser nulo");

        List<CajaVista> cajasVista = new ArrayList<>();

        for (int i = 0; i < cajas.size(); i++) {
            int worldX = (colInicio + i*3) * vista.getTamanio();
            int worldY =  filaBase       * vista.getTamanio();
            CajaVista cv = new CajaVista(cajas.get(i), worldX, worldY, i);
            cajasVista.add(cv);
        }

        MinijuegoOrdenamiento minijuego =
            new MinijuegoOrdenamiento(cajasVista, ordenador, vista);

        vista.setMinijuego(minijuego);
        return minijuego;
    }
}
