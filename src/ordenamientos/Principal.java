package ordenamientos;

import java.util.List;

import modelos.Jugador;
/**
 * prueba del funcionamiento de partida ordenamientos
 */
public class Principal {
	public static void main(String[] args) {
	    // 1. Inicialización normal de la partida
	    Jugador jugador = new Jugador("Lucas");
	    List<Caja> cajasOriginales = List.of(new Caja("A", 40), new Caja("B", 10), new Caja("C", 30));
	    Ordenador<Caja> ordenador = new OrdenadorBubble<>("Bubble Sort");
	    
	    PartidaOrdenamientos<Caja> partida = new PartidaOrdenamientos<>("Wilde", jugador, cajasOriginales, ordenador);
	    partida.iniciar(); // Ejecuta el ordenamiento en background
	    
	    // 2. El MAIN determina el número aleatorio X y lo muestra
	    int totalPasos = partida.getHistorialDePasos().size();
	    int pasoAleatorioX = (int) (Math.random() * totalPasos); 
	    
	    System.out.println("--- DESAFÍO DE MEMORIA ---");
	    System.out.println("¿Cómo se veían los tamaños de las cajas en el PASO NRO: " + pasoAleatorioX + "?");
	    
	    // 3. El MAIN interactúa con el usuario y recupera los tamaños que él cree correctos
	    // Supongamos que por consola o clicks el usuario dice que el orden era: tamaño 10, luego 40, luego 30.
	    // El Main fabrica instancias temporales de Caja con esos tamaños elegidos:
	    List<Caja> cajasArriesgadasUsuario = List.of(
	        new Caja("Temp1", 10),
	        new Caja("Temp2", 30),
	        new Caja("Temp3", 40)
	    );
	    
	    // 4. Se invoca al método pasándole la lista del usuario y el número X que el Main guardó
	    boolean resultado = partida.verificarEstadosDePasos(cajasArriesgadasUsuario, pasoAleatorioX);
	    
	    if (resultado) {
	        System.out.println("¡Excelente! Reconstruiste el paso " + pasoAleatorioX + " a la perfección.");
	    } else {
	        System.out.println("Incorrecto. Los tamaños o el orden no coinciden con ese paso.");
	    }
	}
}
