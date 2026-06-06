package Juego.ciudades.ciudad5;


import java.util.ArrayList;
import java.util.List;

import estructuras.arboles.ArbolBinarioDeBusqueda;
import modelos.Celda;
import modelos.Mapa;

public class ciudadBusqueda {
	
	private ArbolBinarioDeBusqueda<PalabraConPosiciones> arbolDePalabras;
	private List<PalabraConPosiciones> listaDePalabras;
	
	public ciudadBusqueda(Mapa texto) {
		
		crearArbolBinarioDeBusqueda(texto);
		crearLista(texto);
	}
	
	

	/**
     * post: crea la lista con las palabras de texto
     */
    private void crearLista(Mapa texto) {
        this.listaDePalabras = new ArrayList<>(); 
        
        // Ejecutamos el recorrido unificado pasando la acción para la lista
        recorrerYProcesarTexto((palabra, fila, columna) -> {
            PalabraConPosiciones moldeBusqueda = new PalabraConPosiciones(palabra, fila, columna);
            int indice = this.listaDePalabras.indexOf(moldeBusqueda);
            
            if (indice != -1) {
            	this.listaDePalabras.get(indice).agregarPosicion(fila, columna);
            } else {
            	this.listaDePalabras.add(moldeBusqueda);
            }
        },texto);
    }

    /**
     * post: crea el arbol con las palabras de texto
     * @return
     */
    private void crearArbolBinarioDeBusqueda(Mapa texto) {
        this.arbolDePalabras = new ArbolBinarioDeBusqueda<>();
        
        // Ejecutamos el recorrido unificado pasando la acción para el árbol
        recorrerYProcesarTexto((palabra, fila, columna) -> {
            PalabraConPosiciones moldeBusqueda = new PalabraConPosiciones(palabra, fila, columna);
            PalabraConPosiciones existente = this.arbolDePalabras.obtener(moldeBusqueda);
            
            if (existente != null) {
                existente.agregarPosicion(fila, columna);
            } else {
            	this.arbolDePalabras.insertar(moldeBusqueda);
            }
        },texto);

    }

    /**
     * Método unificado que se encarga ÚNICAMENTE de recorrer la matriz
     * y extraer el contenido válido de las celdas.
     */
    private void recorrerYProcesarTexto(AccionPalabra accion, Mapa texto) {
        int ancho = texto.getAncho(); 
        int alto = texto.getAlto();   
        
        for (int i = 1; i <= ancho; i++) {
            for (int j = 1; j <= alto; j++) {
                Celda<?> celda = texto.getCeldaConPosicion(i, j);
                
                if (celda != null && celda.getContenido() != null) {
                	// 1. Convertimos a String puro
                    String palabra = celda.getContenido().toString();
                    
                    // 2. Limpiamos espacios invisibles y saltos de línea molestos
                    palabra = palabra.replaceAll("\\s+", "").trim();
                    
                    // 3. Si no quedó vacío, lo mandamos a indexar
                    if (!palabra.isEmpty() && !palabra.equals("-")) {
                        accion.ejecutar(palabra, i, j);
                    }
                }
            }
        }
    }

    
	
    
    public List<Posicion> buscarPalabraPorLista(String palabraBuscada) {
        for (PalabraConPosiciones elemento : this.listaDePalabras) {
            if (elemento.getPalabra().equals(palabraBuscada)) {
                return elemento.getPosiciones(); 
            }
        }
        return null;
    }
    
    public long medirTiempoPorLista(String palabraBuscada) {
    	long inicioLista = System.nanoTime();
        buscarPalabraPorLista(palabraBuscada);
        long finLista = System.nanoTime();
        return  finLista - inicioLista;
	}



	public long medirTiempoPorArbol(String palabraBuscada) {
    	long inicioArbol = System.nanoTime();
        buscarPalabraMedianteArbol(palabraBuscada);
        long finArbol = System.nanoTime();
        return finArbol - inicioArbol;
    }
	
	public List<Posicion> buscarPalabraMedianteArbol(String palabraBuscada) {
	    PalabraConPosiciones encontrada = this.arbolDePalabras.obtener(new PalabraConPosiciones(palabraBuscada, 0, 0));
	    if (encontrada == null) {
	        return null; 
	    }
	    return encontrada.getPosiciones();
	}


	// Agregar este método en ciudadBusqueda.java
	 
	/**
	 * post: devuelve la lista de strings indexados (sin posiciones)
	 *       para que PartidaBusqueda pueda elegir palabras al azar
	 */
	public List<String> getPalabras() {
	    List<String> resultado = new ArrayList<>();
	    for (PalabraConPosiciones p : this.listaDePalabras) {
	        resultado.add(p.getPalabra());
	    }
	    return resultado;
	}
	public void iniciar() {
		
	}

	public void finalizar() {
		
	}

	

}
