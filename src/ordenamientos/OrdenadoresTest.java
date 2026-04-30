package ordenamientos;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

public class OrdenadoresTest{

   	private List<Caja> cajasDesordenadas;

    @BeforeEach
    public void setUp() {
        cajasDesordenadas = new ArrayList<>();
        // Volúmenes: 27, 1, 8
        cajasDesordenadas.add(new Caja(3.0, 3.0, 3.0));
        cajasDesordenadas.add(new Caja(1.0, 1.0, 1.0));
        cajasDesordenadas.add(new Caja(2.0, 2.0, 2.0));
    }

    @Test
    public void testOrdenadorBubble() {
        OrdenadorBubble bubble = new OrdenadorBubble();
        bubble.ordenar(cajasDesordenadas);

        // Verificamos que la lista original quedó ordenada
        assertEquals(1.0, cajasDesordenadas.get(0).getVolumen());
        assertEquals(8.0, cajasDesordenadas.get(1).getVolumen());
        assertEquals(27.0, cajasDesordenadas.get(2).getVolumen());
        
        // Verificamos el historial de pasos
        List<PasoOrdenamiento> pasos = bubble.getPasos();
        assertFalse(pasos.isEmpty(), "El historial de pasos no debería estar vacío");
        assertEquals("Inicio del ordenamiento", pasos.get(0).getAccion(), "El primer paso debería registrar el inicio");
        assertEquals("Ordenamiento finalizado", pasos.get(pasos.size() - 1).getAccion(), "El último paso debería registrar el final");
    }

    @Test
    public void testOrdenadorSelection() {
        OrdenadorSelection selection = new OrdenadorSelection();
        selection.ordenar(cajasDesordenadas);
        
        // Verificamos que la lista original quedó ordenada
        assertEquals(1.0, cajasDesordenadas.get(0).getVolumen());
        assertEquals(8.0, cajasDesordenadas.get(1).getVolumen());
        assertEquals(27.0, cajasDesordenadas.get(2).getVolumen());

        // Verificamos el historial de pasos
        List<PasoOrdenamiento> pasos = selection.getPasos();
        assertFalse(pasos.isEmpty(), "El historial de pasos no debería estar vacío");
	        
        // Verificamos que en algún momento haya buscado el menor
        boolean buscoMenor = pasos.stream().anyMatch(p -> p.getAccion().equals("Buscando el menor"));
        assertTrue(buscoMenor, "Debería haber registrado una acción de búsqueda de menor");
	    }
	}

