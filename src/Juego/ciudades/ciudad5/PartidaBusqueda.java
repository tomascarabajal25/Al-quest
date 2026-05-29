package Juego.ciudades.ciudad5;

import java.util.List;

import Juego.ciudades.ordenamientos.EstadoDePartida;
import modelos.Jugador;
import modelos.Mapa;
import modelos.Partida;
import utils.Teclado;

public class PartidaBusqueda extends Partida {
	private ciudadBusqueda juego;

	public PartidaBusqueda(String nombre, Jugador jugador,Mapa mapa) {
		super(nombre, jugador);
		setCiudadBusqueda(new ciudadBusqueda(mapa));
		setEstado(EstadoDePartida.Creado);
	}


	
	@Override
    public void iniciar() {
        setEstado(EstadoDePartida.Iniciado);
        
        // -------------------------------------------------------------
        // ¡ACÁ VA EL JUEGO PROPIO DE ESTA CIUDAD!
        // -------------------------------------------------------------
        
        System.out.println("=== BIENVENIDO A LA CIUDAD DE BÚSQUEDA ===");
        System.out.println("El texto ya ha sido indexado en el Árbol y en la Lista.");
        
        while (getEstado()==EstadoDePartida.Iniciado) {
            System.out.print("\nIngrese la palabra que desea buscar (o escriba 'SALIR' para irse): ");
            String palabraBuscada = Teclado.leerTexto().trim();
            
            if (palabraBuscada.equalsIgnoreCase("SALIR")) {
                finalizar();
                continue;
            }
            
            if (palabraBuscada.isEmpty()) {
                System.out.println("No ingresó ninguna palabra.");
                continue;
            }
            
            // 1. Cronometramos y buscamos en la Lista
            long tiempoLista = juego.medirTiempoPorLista(palabraBuscada);
            
            // 2. Cronometramos y buscamos en el Árbol
            long tiempoArbol = juego.medirTiempoPorArbol(palabraBuscada);
            
            // 3. Obtenemos los resultados reales para mostrárselos al usuario
            List<Posicion> posiciones = juego.buscarPalabraMedianteArbol(palabraBuscada);
            
            // 4. Mostramos el reporte comparativo por pantalla
            System.out.println("\n--- RESULTADOS DE BÚSQUEDA ---");
            if (posiciones == null) {
                System.out.println("La palabra '" + palabraBuscada + "' no existe en el texto.");
            } else {
                System.out.println("Palabra encontrada en las siguientes coordenadas [Fila, Columna]:");
                for (Posicion pos : posiciones) {
                    System.out.println(" -> Linea (Fila): " + pos.getLinea() + " | Indice (Columna): " + pos.getIndice());
                }
            }
            
            System.out.println("\n--- RENDIMIENTO COMPARATIVO ---");
            System.out.println("Tiempo en Lista Secuencial: " + tiempoLista + " nanosegundos.");
            System.out.println("Tiempo en Árbol Binario (ABB): " + tiempoArbol + " nanosegundos.");
            
            if (tiempoLista > tiempoArbol) {
                System.out.println("¡El Árbol fue " + (tiempoLista - tiempoArbol) + " ns más rápido que la lista!");
            } else {
                System.out.println("¡La lista fue más rápida! (Puede pasar en textos muy chicos o si la palabra estaba al principio).");
            }
        }
        
        
    }

    @Override
    public void finalizar() {
        setEstado(EstadoDePartida.Creado);
        setPuntaje(calcularPuntaje());
        System.out.println("Saliendo de la Ciudad de Búsqueda... Volviendo al mapa principal.");
    }

    private int calcularPuntaje() {
        return 100; 
    }
    private void setCiudadBusqueda(ciudadBusqueda ciudadBusqueda) {
		juego=ciudadBusqueda;
	}

}
	

