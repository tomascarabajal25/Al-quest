package tests.ciudades.testDeCiudadDeBusqueda;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import juego.ciudades.ciudad5.PartidaBusqueda;
import modelos.EstadoDePartida;
import modelos.Jugador;

/**
 * Clase de pruebas unitarias para PartidaBusqueda.
 * Orienta a comprobar el estado inicial y la correcta construcción del TDA.
 * * Cumple con las normas: JUnit 5, estructura Arrange/Act/Assert y nombres descriptivos.
 */
public class PartidaBusquedaTest {

    private Jugador jugadorPrueba;
    private String nombrePartidaPrueba;

    /**
     * Configuración inicial antes de cada prueba.
     * Se encarga de preparar los objetos comunes del entorno (Arrange).
     */
    @BeforeEach
    public void setUp() {
        jugadorPrueba = new Jugador("Tester");
        nombrePartidaPrueba = "Desafío de Búsqueda Binaria";
    }

    /**
     * Objetivo: Verificar que el constructor asigne correctamente los valores
     * iniciales provistos cuando son válidos.
     */
    @Test
    public void testConstructor_DebeAsignarAtributosCorrectamente_CuandoLosParametrosSonValidos() {
        // Arrange (Configurado en setUp)

        // Act
        PartidaBusqueda partida = new PartidaBusqueda(nombrePartidaPrueba, jugadorPrueba);

        // Assert
        assertNotNull(partida, "La instancia de la partida no debe ser nula.");
        assertEquals(nombrePartidaPrueba, partida.getNombre(), "El nombre asignado debe coincidir con el esperado.");
        assertEquals(jugadorPrueba, partida.getJugador(), "El jugador asignado debe coincidir con el esperado.");
    }

    /**
     * Objetivo: Validar que toda partida nueva inicie estrictamente 
     * en el estado de configuración "Creado".
     */
    @Test
    public void testConstructor_DebeEstablecerEstadoInicialEnCreado() {
        // Arrange (Configurado en setUp)

        // Act
        PartidaBusqueda partida = new PartidaBusqueda(nombrePartidaPrueba, jugadorPrueba);

        // Assert
        assertEquals(EstadoDePartida.Creado, partida.getEstado(), "Toda partida recién instanciada debe iniciar en estado Creado.");
    }

    /**
     * Objetivo: Comprobar que el puntaje acumulado de la partida comience en 0.
     */
    @Test
    public void testConstructor_DebeInicializarPuntajeEnCero() {
        // Arrange (Configurado en setUp)

        // Act
        PartidaBusqueda partida = new PartidaBusqueda(nombrePartidaPrueba, jugadorPrueba);

        // Assert
        assertEquals(0, partida.getPuntaje(), "El puntaje inicial de la partida debe ser 0.");
    }
}