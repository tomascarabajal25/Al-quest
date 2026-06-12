package ciudades.testsDeLaCiudadDeOrdenamiento;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

import juego.ciudades.ordenamientos.AdministradorDePasos;
import juego.ciudades.ordenamientos.Caja;
import juego.ciudades.ordenamientos.PasoOrdenamiento;

public class PasosYAdministradorTest {

    @Test
    public void testPasoOrdenamientoInmutabilidadCopia() {
        List<Caja> listaOriginal = new ArrayList<>();
        listaOriginal.add(new Caja("Caja 1", 10, true));
        listaOriginal.add(new Caja("Caja 2", 20, true));

        PasoOrdenamiento<Caja> paso = new PasoOrdenamiento<>(listaOriginal, 0, 1, "Paso de prueba");

        // Modificamos la lista original externamente
        listaOriginal.set(0, new Caja("Modificada", 99, true));

        // La lista guardada en el paso NO debería haber cambiado
        assertNotEquals(listaOriginal.get(0).getTamaño(), paso.getCopiasEnEstePaso().get(0).getTamaño(),
                "El paso de ordenamiento debe almacenar una copia aislada de la lista");
        assertEquals(10, paso.getCopiasEnEstePaso().get(0).getTamaño());
    }

    @Test
    public void testAdministradorDePasosGuardaCorrectamente() {
        AdministradorDePasos<Caja> admin = new AdministradorDePasos<>();
        assertTrue(admin.getPasos().isEmpty());

        List<Caja> lista = List.of(new Caja("A", 5, true));
        PasoOrdenamiento<Caja> paso = new PasoOrdenamiento<>(lista, -1, -1, "Inicio");
        
        admin.guardarPaso(paso);
        assertEquals(1, admin.getPasos().size());
        assertEquals("Inicio", admin.getPasos().get(0).getAccion());
    }
}
