package ciudades.testsDeTorresDeHanoi;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import juego.ciudades.torresDeHanoi.PartidaHanoi;
import juego.configuracion.ConfiguracionDeHanoi;
import juego.ciudades.torresDeHanoi.CiudadHanoi;
import modelos.Jugador;

import java.lang.reflect.Field;

public class PartidaHanoiTest {

    @Test
    public void calcularPuntaje_conResolucionPerfecta_debeOtorgarPuntajeMaximoMultiplicado() throws Exception {
        // Arrange
        Jugador jugador = new Jugador("Tester");
        PartidaHanoi partida = new PartidaHanoi("Hanoi", jugador, null);
        
        int cantidadDiscos = 3;
        int movimientosOptimos = 7; // El óptimo para 3 discos
        
        // Simular el estado interno sin abrir la ventana del juego
        inyectarEstadoInterno(partida, cantidadDiscos, movimientosOptimos);

        // Act
        int puntajeCalculado = partida.calcularPuntaje(); // ¡Sin parámetros!

        // Assert
        int puntajeEsperado = ConfiguracionDeHanoi.PUNTAJE_BASE_PERFECTO * cantidadDiscos; 
        assertEquals(puntajeEsperado, puntajeCalculado, "Si los movimientos son óptimos, debe dar el puntaje base perfecto escalado por la dificultad.");
    }

    @Test
    public void calcularPuntaje_conMovimientosDeMas_debeOtorgarPuntajeImperfecto() throws Exception {
        // Arrange
        Jugador jugador = new Jugador("Tester");
        PartidaHanoi partida = new PartidaHanoi("Hanoi", jugador, null);
        
        int cantidadDiscos = 3;
        int movimientosRealizados = 15; // El jugador cometió errores
        
        // Simular el estado interno sin abrir la ventana del juego
        inyectarEstadoInterno(partida, cantidadDiscos, movimientosRealizados);

        // Act
        int puntajeCalculado = partida.calcularPuntaje(); // ¡Sin parámetros!

        // Assert
        int puntajeEsperado = ConfiguracionDeHanoi.PUNTAJE_BASE_IMPERFECTO * cantidadDiscos;
        assertEquals(puntajeEsperado, puntajeCalculado, "Si el jugador superó el mínimo de movimientos, recibe un puntaje penalizado/imperfecto.");
    }

    /**
     * Método auxiliar de prueba
     */
    private void inyectarEstadoInterno(PartidaHanoi partida, int discos, int movimientos) throws Exception {
        // 1. Forzamos el valor de la variable privada 'cantidadDiscos'
        Field fieldDiscos = PartidaHanoi.class.getDeclaredField("cantidadDiscos");
        fieldDiscos.setAccessible(true);
        fieldDiscos.set(partida, discos);

        // 2. Creamos una CiudadHanoi falsa y le inyectamos los movimientos
        CiudadHanoi juegoFalso = new CiudadHanoi(discos);
        Field fieldMovimientos = CiudadHanoi.class.getDeclaredField("movimientos");
        fieldMovimientos.setAccessible(true);
        fieldMovimientos.set(juegoFalso, movimientos);

        // 🌟 SOLUCIÓN: Forzamos el contador de la torreC para que haGanado() devuelva true
        Object torreC = juegoFalso.getTorreC();
        Field fieldContNodo = torreC.getClass().getDeclaredField("contNodo");
        fieldContNodo.setAccessible(true);
        fieldContNodo.set(torreC, discos); // Simulamos que tiene todos los discos

        // 3. Forzamos la variable privada 'juego' para que apunte a nuestro juegoFalso
        Field fieldJuego = PartidaHanoi.class.getDeclaredField("juego");
        fieldJuego.setAccessible(true);
        fieldJuego.set(partida, juegoFalso);
    }
}