package juego.ciudades.ciudad5.UI;

/**
 * Estados del minijuego de búsqueda en Ciudad 5.
 *
 * INACTIVO       → el jugador está explorando, nada activo
 * MOSTRANDO      → el jugador entró a la zona, se muestra la palabra y las puertas
 * ESPERANDO      → se espera que el jugador cruce una de las dos puertas
 * FEEDBACK       → se muestra si acertó o no (pausa breve antes de la siguiente ronda)
 * GANADO         → el jugador completó todas las rondas
 */
public enum EstadoMinijuego {
    INACTIVO,
    MOSTRANDO,
    ESPERANDO,
    FEEDBACK,
    GANADO
}