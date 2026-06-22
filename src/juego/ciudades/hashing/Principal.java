package juego.ciudades.hashing;



import juego.configuracion.ConstantesSonido;
import modelos.Jugador;
import modelos.Sonido;

/**
 * Main de prueba INDEPENDIENTE de la Ciudad 6 HASHING con interfaz grafica.
 * as
 * Sirve para probar la ciudad por separado, no depende del resto del juego.
 * Crea un jugador, una Vista con un mapa, define los elementos a insertar y las claves
 * a buscar, y arranca tambien la PartidaHAshing (que abre la ventana).
 * 
 * Cuando se integre al juego completo, sera PartidaAiQuest quien cree la
 * PartidaHashing, como dije antes, este Principal es solo un banco de pruebas.
 * 
 */

public class Principal {
	public static Sonido sonido = new Sonido();
	


    public static void main(String[] args) {
    	agregarSonido();

        Jugador jugador = new Jugador("Tester");

        PartidaHashing partida = new PartidaHashing("Ciudad Hashing", jugador,sonido);

                        
        //Iniciar, monta el minijuego en el mundo, abre la ventana y arranca hilo
        partida.iniciar();
    }



	private static void agregarSonido() {
		sonido.agregarSonido(ConstantesSonido.GLOBAL_AVENTURA, ConstantesSonido.RUTA_GLOBAL_AVENTURA);
        // Registrar pistas por ciudad (rutas por defecto, el usuario podrá reemplazarlas)
        sonido.agregarSonido(ConstantesSonido.HANOI, ConstantesSonido.RUTA_HANOI);
        sonido.agregarSonido(ConstantesSonido.RECOLECCION, ConstantesSonido.RUTA_RECOLECCION);
        sonido.agregarSonido(ConstantesSonido.REINAS, ConstantesSonido.RUTA_REINAS);
        sonido.agregarSonido(ConstantesSonido.LABERINTO, ConstantesSonido.RUTA_LABERINTO);
        sonido.agregarSonido(ConstantesSonido.ORDENAMIENTO, ConstantesSonido.RUTA_ORDENAMIENTO);
        sonido.agregarSonido(ConstantesSonido.BUSQUEDA, ConstantesSonido.RUTA_BUSQUEDA);
        sonido.agregarSonido(ConstantesSonido.HASHING, ConstantesSonido.RUTA_HASHING);
        sonido.agregarSonido(ConstantesSonido.GRAFOS, ConstantesSonido.RUTA_GRAFOS);
        sonido.agregarSonido(ConstantesSonido.BATALLA, ConstantesSonido.RUTA_BATALLA);
        sonido.agregarSonido(ConstantesSonido.COMPLEJIDAD, ConstantesSonido.RUTA_COMPLEJIDAD);
        // Efecto de proximidad a agua
        sonido.agregarSonido(ConstantesSonido.AGUA, ConstantesSonido.RUTA_AGUA);
        // sonido de los pasos
        sonido.agregarSonido(ConstantesSonido.PASO1, ConstantesSonido.RUTA_PASO1);
        sonido.agregarSonido(ConstantesSonido.PASO2, ConstantesSonido.RUTA_PASO2);
        sonido.agregarSonido(ConstantesSonido.VICTORIA, ConstantesSonido.RUTA_VICTORIA);
        // Sonido al abrir la Tienda de Skins
        sonido.agregarSonido(juego.configuracion.ConstantesSonido.TIENDA, juego.configuracion.ConstantesSonido.RUTA_TIENDA);
        sonido.agregarSonido(juego.configuracion.ConstantesSonido.TIENDA2, juego.configuracion.ConstantesSonido.RUTA_TIENDA2);
        // Sonidos al comprar una skin
       sonido.agregarSonido(juego.configuracion.ConstantesSonido.COMPRAR1, juego.configuracion.ConstantesSonido.RUTA_COMPRAR1);
        sonido.agregarSonido(juego.configuracion.ConstantesSonido.COMPRAR2, juego.configuracion.ConstantesSonido.RUTA_COMPRAR2);
        sonido.agregarSonido(juego.configuracion.ConstantesSonido.COMPRAR3, juego.configuracion.ConstantesSonido.RUTA_COMPRAR3);
        sonido.agregarSonido(juego.configuracion.ConstantesSonido.COMPRAR4, juego.configuracion.ConstantesSonido.RUTA_COMPRAR4);
        
	}
    



    
}
