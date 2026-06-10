package tests.Ciudades.testsDeLaCiudadDeOrdenamiento;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import juego.ciudades.ordenamientos.AdministradorDePasos;
import juego.ciudades.ordenamientos.Caja;
import juego.ciudades.ordenamientos.OrdenadorBubble;
import juego.ciudades.ordenamientos.PasoOrdenamiento;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;


public class OrdenadorBubleTest {

    private OrdenadorBubble<Caja> ordenador;
    private List<Caja> listaDesordenada;

    @BeforeEach
    public void setUp() {
        ordenador = new OrdenadorBubble<>("Metodo Burbuja");
        listaDesordenada = new ArrayList<>();
        listaDesordenada.add(new Caja("Grande", 80, true));
        listaDesordenada.add(new Caja("Chica", 20, true));
        listaDesordenada.add(new Caja("Mediana", 50, true));
    }

    @Test
    public void testOrdenamientoEstandarAscendente() {
        ordenador.ordenar(listaDesordenada);
        
        assertEquals(20, listaDesordenada.get(0).getTamaño());
        assertEquals(50, listaDesordenada.get(1).getTamaño());
        assertEquals(80, listaDesordenada.get(2).getTamaño());
    }

    @Test
    public void testBubbleConAdministradorDePasosSinSpam() {
        AdministradorDePasos<Caja> admin = new AdministradorDePasos<>();
        ordenador.ordenar(listaDesordenada, admin);

        List<PasoOrdenamiento<Caja>> pasos = admin.getPasos();
        
        // Verificaciones de flujo
        assertFalse(pasos.isEmpty());
        assertEquals("Inicio del ordenamiento", pasos.get(0).getAccion());
        assertEquals("Ordenamiento finalizado", pasos.get(pasos.size() - 1).getAccion());

        // Comprobación de optimización: Al remover las "Comparaciones" continuas,
        // para esta lista corta no deberían generarse excesos de imágenes.
        boolean contieneMensajeComparando = pasos.stream()
                .anyMatch(p -> p.getAccion().contains("Comparando elementos"));
        
        assertFalse(contieneMensajeComparando, "Se eliminaron los pasos repetitivos de comparación para optimizar los BMPs");
    }
}
