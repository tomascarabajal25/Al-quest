package juego.ciudades.ordenamientos.ui;

import java.util.ArrayList;
import java.util.List;

import juego.ciudades.ordenamientos.Caja;
import juego.ciudades.ordenamientos.Ordenador;
import juego.configuracion.ConfiguracionDeOrdenamientos;
import modelosVista.Vista;
import utils.ValidacionesUtiles;

/**
 * Fábrica que construye el MinijuegoOrdenamiento:
 *   1. Crea las CajaVista a partir de una lista de Caja.
 *   2. Las posiciona en el mundo en una fila horizontal usando la configuración centralizada.
 *   3. Crea el MinijuegoOrdenamiento y lo registra en la Vista.
 *
 * Uso típico desde la ciudad:
 * <pre>
 *   List&lt;Caja&gt; cajas = List.of(new Caja("A", 40, true), new Caja("B", 10, true), ...);
 *   Ordenador&lt;Caja&gt; ord = new OrdenadorBubble&lt;&gt;("Bubble Sort");
 *   FabricaMinijuegoOrdenamiento.crear(vista, cajas, ord);
 * </pre>
 */
public class FabricaMinijuegoOrdenamiento {

    private FabricaMinijuegoOrdenamiento() {} // utilidad estática

    /**
     * Pre:
     * @param vista     no nula, ya inicializada con jugadorVista
     * @param cajas     lista no nula con al menos 2 elementos
     * @param ordenador no nulo
     *
     * Post:
     * Crea las CajaVista, las agrega a la Vista y registra el MinijuegoOrdenamiento.
     * Las cajas quedan separadas CAJAS_SEPARACION_TILES celdas entre sí horizontalmente.
     *
     * @return el MinijuegoOrdenamiento creado (ya registrado en la Vista)
     */
    public static MinijuegoOrdenamiento crear(Vista vista,
                                               List<Caja> cajas,
                                               Ordenador<Caja> ordenador) {
    	ValidacionesUtiles.esDistintoDeNull(vista, "Vista no puede ser nula");
        ValidacionesUtiles.esDistintoDeNull(cajas, "Cajas no puede ser nula");
        ValidacionesUtiles.validarRangoNumerico(cajas.size(), 2, 10, "Se necesitan al menos 2 cajas, y menos de 10");
        ValidacionesUtiles.esDistintoDeNull(ordenador, "ordenador no puede ser nulo");
        List<CajaVista> cajasVista = new ArrayList<>();
        int tamaño = vista.getTamanio();

        for (int i = 0; i < cajas.size(); i++) {
            int worldX = (ConfiguracionDeOrdenamientos.CAJAS_COL_INICIO
                         + i * ConfiguracionDeOrdenamientos.CAJAS_SEPARACION_TILES) * tamaño;
            int worldY =  ConfiguracionDeOrdenamientos.CAJAS_FILA_BASE * tamaño;
            cajasVista.add(new CajaVista(cajas.get(i), worldX, worldY, i));
        }

        MinijuegoOrdenamiento minijuego =
            new MinijuegoOrdenamiento(cajasVista, ordenador, vista);

        vista.establecerMinijuego(minijuego);
        return minijuego;
    }
}