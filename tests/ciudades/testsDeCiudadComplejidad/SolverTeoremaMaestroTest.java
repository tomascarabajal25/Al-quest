package ciudades.testsDeCiudadComplejidad;

import juego.ciudades.complejidad.EcuacionRecurrencia;
import juego.ciudades.complejidad.PasoTeoremaMaestro;
import juego.ciudades.complejidad.SolverTeoremaMaestro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class SolverTeoremaMaestroTest {

    private SolverTeoremaMaestro solver;

    @BeforeEach
    public void setUp() {
        solver = new SolverTeoremaMaestro();
    }

    // --- resolver ---

    @Test
    public void testResolverDevuelveListaNoVacia() {
        EcuacionRecurrencia ec = new EcuacionRecurrencia(2, 2, "n");
        List<PasoTeoremaMaestro> pasos = solver.resolver(ec);
        assertFalse(pasos.isEmpty());
    }

    @Test
    public void testResolverPrimerPasoMuestraEcuacion() {
        EcuacionRecurrencia ec = new EcuacionRecurrencia(2, 2, "n");
        List<PasoTeoremaMaestro> pasos = solver.resolver(ec);
        assertTrue(pasos.get(0).getDescripcion().contains("T(n)"));
    }

    @Test
    public void testResolverUltimoPasoMuestraResultado() {
        EcuacionRecurrencia ec = new EcuacionRecurrencia(2, 2, "n");
        List<PasoTeoremaMaestro> pasos = solver.resolver(ec);
        String ultimo = pasos.get(pasos.size() - 1).getDescripcion();
        assertTrue(ultimo.contains("Caso") || ultimo.contains("T(n)"));
    }

    // --- getResultado ---

    @Test
    public void testCaso1FnMasLento() {
        // a=4, b=2 → log_2(4)=2, f(n)=n → exp=1 < 2 → Caso 1
        EcuacionRecurrencia ec = new EcuacionRecurrencia(4, 2, "n");
        assertEquals("O(n^2)", solver.getResultado(ec));
    }

    @Test
    public void testCaso2FnIgual() {
        // a=2, b=2 → log_2(2)=1, f(n)=n → exp=1 == 1 → Caso 2
        EcuacionRecurrencia ec = new EcuacionRecurrencia(2, 2, "n");
        assertEquals("O(n log n)", solver.getResultado(ec));
    }

    @Test
    public void testCaso3FnMasRapido() {
        // a=2, b=2 → log_2(2)=1, f(n)=n^2 → exp=2 > 1 → Caso 3
        EcuacionRecurrencia ec = new EcuacionRecurrencia(2, 2, "n^2");
        assertEquals("O(n^2)", solver.getResultado(ec));
    }

    @Test
    public void testCaso3FuncionConstante() {
        // a=1, b=2 → log_2(1)=0, f(n)=1 → exp=0 == 0 → Caso 2 con log_2(1)=0
        EcuacionRecurrencia ec = new EcuacionRecurrencia(1, 2, "1");
        assertNotNull(solver.getResultado(ec));
    }
}
