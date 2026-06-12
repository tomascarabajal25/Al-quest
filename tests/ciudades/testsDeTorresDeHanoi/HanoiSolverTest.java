package tests.ciudades.testsDeTorresDeHanoi;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import juego.ciudades.torresDeHanoi.Pila;
import juego.ciudades.torresDeHanoi.HanoiSolver;
import juego.ciudades.torresDeHanoi.Nodo;
import juego.ciudades.torresDeHanoi.ObservadorHanoi;

public class HanoiSolverTest {

    @Test
    public void resolverHanoi_conTresDiscos_debeCompletarseEnSieteMovimientosOptimos() {
        // Arrange
        Pila<Integer> torreOrigen = new Pila<>();
        Pila<Integer> torreDestino = new Pila<>();
        Pila<Integer> torreAuxiliar = new Pila<>();
        
        torreOrigen.push(new Nodo<Integer>(3));
        torreOrigen.push(new Nodo<Integer>(2));
        torreOrigen.push(new Nodo<Integer>(1));

        // Implementamos un Observador de prueba para contar los movimientos
        class ObservadorPrueba implements ObservadorHanoi {
            public int cantidadDePasos = 0;
            
            @Override
            public boolean onMovimiento(int paso) {
                this.cantidadDePasos = paso;
                return true; // Continúa resolviendo sin detenerse
            }
        }
        
        ObservadorPrueba observador = new ObservadorPrueba();
        HanoiSolver<Integer> solver = new HanoiSolver<>(observador);

        // Act
        solver.resolverHanoi(3, torreOrigen, torreAuxiliar ,torreDestino);

        // Assert
        assertEquals(7, observador.cantidadDePasos, "Para 3 discos, la solución matemática óptima es exactamente 7 movimientos.");
        assertTrue(torreOrigen.isEmpty(), "La torre origen debe quedar completamente vacía tras resolver el puzzle.");
        assertFalse(torreDestino.isEmpty(), "La torre destino debe contener todos los discos.");
    }
}