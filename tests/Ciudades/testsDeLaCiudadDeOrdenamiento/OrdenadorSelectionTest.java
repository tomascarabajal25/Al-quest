package tests.Ciudades.testsDeLaCiudadDeOrdenamiento;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.aiquest.juego.ciudades.ordenamientos.AdministradorDePasos;
import com.aiquest.juego.ciudades.ordenamientos.Caja;
import com.aiquest.juego.ciudades.ordenamientos.OrdenadorSelection;
import com.aiquest.juego.ciudades.ordenamientos.PasoOrdenamiento;

public class OrdenadorSelectionTest {

    private OrdenadorSelection<Caja> ordenador;
    private List<Caja> listaDesordenada;

    @BeforeEach
    public void setUp() {
        ordenador = new OrdenadorSelection<>("Metodo Seleccion");
        listaDesordenada = new ArrayList<>();
        listaDesordenada.add(new Caja("Caja 3", 99));
        listaDesordenada.add(new Caja("Caja 1", 11));
        listaDesordenada.add(new Caja("Caja 2", 44));
    }

    @Test
    public void testOrdenamientoEstandarAscendente() {
        ordenador.ordenar(listaDesordenada);

        // Comprobamos si el Bug de dirección (Mayor a Menor) fue solucionado
        assertEquals(11, listaDesordenada.get(0).getTamaño(), "El primero debe ser el menor");
        assertEquals(44, listaDesordenada.get(1).getTamaño());
        assertEquals(99, listaDesordenada.get(2).getTamaño());
    }

    @Test
    public void testSelectionConAdministradorDePasosControlado() {
        AdministradorDePasos<Caja> admin = new AdministradorDePasos<>();
        ordenador.ordenar(listaDesordenada, admin);

        List<PasoOrdenamiento<Caja>> pasos = admin.getPasos();

        // Control de volumen de pasos
        assertFalse(pasos.isEmpty());
        assertEquals("Inicio del ordenamiento", pasos.get(0).getAccion());

        // Con la optimización, el texto "Buscando el menor" o "Nuevo mínimo" ya no spamea la lista
        long pasosDeBusquedaIntermedia = pasos.stream()
                .filter(p -> p.getAccion().equals("Buscando el menor") || p.getAccion().equals("Nuevo mínimo encontrado"))
                .count();

        assertEquals(0, pasosDeBusquedaIntermedia, "Las búsquedas intermedias se omiten; solo se registran estados de animación útiles");
    }
}