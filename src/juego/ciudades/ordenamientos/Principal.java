package juego.ciudades.ordenamientos;


import java.util.List;

import juego.ciudades.ordenamientos.ui.RenderOrdenamiento;
import modelos.Jugador;

/**
 * Prueba del funcionamiento de partida ordenamientos con Interfaz Gráfica Animada
 */
public class Principal {
    public static void main(String[] args) {
        // 1. Inicialización normal de la partida
        Jugador jugador = new Jugador("Lucas");
        List<Caja> cajasOriginales = List.of(new Caja("A", 40), new Caja("B", 10), new Caja("C", 30),new Caja("D", 25), new Caja("C", 35));
        Ordenador<Caja> ordenador = new OrdenadorBubble<>("Bubble Sort");
        
        PartidaOrdenamientos<Caja> partida = new PartidaOrdenamientos<>("Wilde", jugador, cajasOriginales, ordenador);
        partida.iniciar(); // Ejecuta el ordenamiento en background y llena el historial
        
        // 🌟 AGREGAMOS ESTA LÍNEA AQUÍ: Abre la ventana y carga las imágenes fijas
        RenderOrdenamiento.visualizarSimulacion(partida);
        
        // 2. El MAIN determina el número aleatorio X y lo muestra
        int totalPasos = partida.getHistorialDePasos().size();
        int pasoAleatorioX = (int) (Math.random() * totalPasos); 
        
        System.out.println("--- DESAFÍO DE MEMORIA ---");
        System.out.println("¿Cómo se veían los tamaños de las cajas en el PASO NRO: " + pasoAleatorioX + "?");
        System.out.println("(Presioná el botón 'Iniciar Animación' en la ventana para ver el juego correr antes de arriesgar!)");
        
        // 3. El MAIN interactúa con el usuario y recupera los tamaños que él cree correctos
        // (Acá podés usar un Scanner para leer lo que meta el usuario por consola en vez de dejarlo fijo)
        List<Caja> cajasArriesgadasUsuario = List.of(
            new Caja("Temp1", 10),
            new Caja("Temp2", 30),
            new Caja("Temp3", 40),
            new Caja("Temp3", 25),
            new Caja("Temp3", 35)
        );
        
        // 4. Se invoca al método pasándole la lista del usuario y el número X que el Main guardó
        boolean resultado = partida.verificarEstadosDePasos(cajasArriesgadasUsuario, pasoAleatorioX);
        
        if(resultado) {
            System.out.println("🎉 ¡Ganaste! Recordaste la animación a la perfección.");
        } else {
            System.out.println("❌ ¡Incorrecto! Volvé a mirar la animación detenidamente.");
        }
    }
}