package juego.ciudades.ciudad5;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import estructuras.arboles.ArbolBinarioDeBusqueda;
import modelos.Celda;
import modelos.Mapa;

/**
 * TDA que representa el desafío de la Ciudad de Búsqueda.
 * * Gestiona la indexación de palabras extraídas de un mapa utilizando
 * de forma paralela un árbol binario de búsqueda y una lista secuencial,
 * permitiendo comparar la eficiencia temporal de ambas estructuras.
 */
public class ciudadBusqueda {
	
	// CONSTANTES
	
	// ATRIBUTOS DE CLASE
	
	// ATRIBUTOS
	private ArbolBinarioDeBusqueda<PalabraConPosiciones> arbolDePalabras;
	private List<PalabraConPosiciones> listaDePalabras;
	
	// CONSTRUCTORES
	/**
	 * Inicializa el TDA de la Ciudad de Búsqueda a partir de un mapa de texto.
	 * * Pre:
	 * - El objeto texto no debe ser nulo.
	 * * Post:
	 * - Se indexan todas las palabras válidas del mapa tanto en el árbol binario como en la lista.
	 * * @param texto mapa que contiene las celdas con las palabras a indexar
	 */
	public ciudadBusqueda(Mapa texto) {
		crearArbolBinarioDeBusqueda(texto);
		crearLista(texto);
	}
	
	// METODOS DE CLASE
	
	// METODOS GENERALES
	
	// METODOS DE COMPORTAMIENTO
	/**
	 * Crea e indexa de forma secuencial la lista con las palabras extraídas del texto.
	 * * Pre:
	 * - El objeto texto no debe ser nulo.
	 * * Post:
	 * - Se inicializa la lista interna cargando los moldes de palabras con sus respectivas posiciones.
	 * * @param texto mapa que contiene las celdas con el texto base
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
     * Crea e indexa de forma jerárquica el árbol binario de búsqueda con las palabras del texto.
     * * Pre:
     * - El objeto texto no debe ser nulo.
     * * Post:
     * - Se inicializa el árbol interno insertando o actualizando los nodos con las palabras y posiciones.
     * * @param texto mapa que contiene las celdas con el texto base
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
     * Recorre las dimensiones de la matriz del mapa abstrayendo el filtrado de caracteres y limpieza.
     * * Delegar mediante la interfaz funcional el almacenamiento específico según la estructura elegida.
     * * Pre:
     * - El parámetro accion no debe ser nulo.
     * - El parámetro texto no debe ser nulo.
     * * Post:
     * - Procesa cada celda válida remitiendo los datos purificados a la acción suministrada.
     * * @param accion estrategia funcional para la manipulación de la palabra e índices
     * @param texto estructura espacial del mapa a iterar
     */
    private void recorrerYProcesarTexto(AccionPalabra accion, Mapa texto) {
        int ancho = texto.getAncho(); 
        int alto = texto.getAlto();   
        
        for (int i = 1; i <= ancho; i++) {
            for (int j = 1; j <= alto; j++) {
                Celda<?> celda = texto.getCeldaConPosicion(i, j);
                
                if (celda != null && celda.getContenido() != null) {
                	// Convertimos a String puro
                    String palabra = celda.getContenido().toString();
                    
                    // Limpiamos espacios invisibles y saltos de línea molestos
                    palabra = palabra.replaceAll("\\s+", "").trim();
                    
                    // Si no quedó vacío, lo mandamos a indexar
                    if (!palabra.isEmpty() && !palabra.equals("-")) {
                        accion.ejecutar(palabra, i, j);
                    }
                }
            }
        }
    }

    /**
     * Busca una palabra de forma secuencial en la lista interna.
     * * Pre:
     * - El parámetro palabraBuscada no debe ser nulo.
     * * Post:
     * - Retorna la lista de posiciones asociadas si se encuentra la palabra, o nulo en caso contrario.
     * * @param palabraBuscada cadena de caracteres que se desea localizar
     * @return colección de posiciones de la palabra o null si no existe
     */
    public List<Posicion> buscarPalabraPorLista(String palabraBuscada) {
        for (PalabraConPosiciones elemento : this.listaDePalabras) {
            if (elemento.getPalabra().equals(palabraBuscada)) {
                return elemento.getPosiciones(); 
            }
        }
        return null;
    }
    
    /**
     * Mide el tiempo en nanosegundos incurrido en realizar la búsqueda sobre la lista secuencial.
     * * Pre:
     * - El parámetro palabraBuscada no debe ser nulo.
     * * Post:
     * - Retorna el diferencial temporal transcurrido durante la operación de consulta.
     * * @param palabraBuscada cadena de caracteres que se desea localizar
     * @return tiempo total transcurrido expresado en nanosegundos
     */
    public long medirTiempoPorLista(String palabraBuscada) {
    	long inicioLista = System.nanoTime();
        buscarPalabraPorLista(palabraBuscada);
        long finLista = System.nanoTime();
        return finLista - inicioLista;
	}

    /**
     * Mide el tiempo en nanosegundos incurrido en realizar la búsqueda sobre el árbol binario.
     * * Pre:
     * - El parámetro palabraBuscada no debe ser nulo.
     * * Post:
     * - Retorna el diferencial temporal transcurrido durante la operación de consulta.
     * * @param palabraBuscada cadena de caracteres que se desea localizar
     * @return tiempo total transcurrido expresado en nanosegundos
     */
	public long medirTiempoPorArbol(String palabraBuscada) {
    	long inicioArbol = System.nanoTime();
        buscarPalabraMedianteArbol(palabraBuscada);
        long finArbol = System.nanoTime();
        return finArbol - inicioArbol;
    }
	
	/**
	 * Busca una palabra de forma jerárquica dentro del árbol binario de búsqueda.
	 * * Pre:
	 * - El parámetro palabraBuscada no debe ser nulo.
	 * * Post:
	 * - Retorna la lista de posiciones asociadas si se encuentra la palabra, o nulo en caso contrario.
	 * * @param palabraBuscada cadena de caracteres que se desea localizar
	 * @return colección de posiciones de la palabra o null si no existe
	 */
	public List<Posicion> buscarPalabraMedianteArbol(String palabraBuscada) {
	    PalabraConPosiciones encontrada = this.arbolDePalabras.obtener(new PalabraConPosiciones(palabraBuscada, 0, 0));
	    if (encontrada == null) {
	        return null; 
	    }
	    return encontrada.getPosiciones();
	}

	//METODOS GENERALES ----------------------------------------
	@Override
	public int hashCode() {
		return Objects.hash(arbolDePalabras, listaDePalabras);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ciudadBusqueda other = (ciudadBusqueda) obj;
		return Objects.equals(arbolDePalabras, other.arbolDePalabras)
				&& Objects.equals(listaDePalabras, other.listaDePalabras);
	}

	@Override
	public String toString() {
		return "ciudadBusqueda [arbolDePalabras=" + arbolDePalabras + ", listaDePalabras=" + listaDePalabras + "]";
	}

	// GETTERS
	/**
	 * Obtiene una lista plana con todas las cadenas de texto que fueron indexadas.
	 * * Ideal para que la lógica externa de la partida pueda seleccionar palabras de forma aleatoria.
	 * * Post:
	 * - Devuelve una colección secuencial con las cadenas de texto puras (sin la información de sus posiciones).
	 * * @return lista que contiene únicamente los textos de las palabras indexadas
	 */
	public List<String> getPalabras() {
	    List<String> resultado = new ArrayList<>();
	    for (PalabraConPosiciones p : this.listaDePalabras) {
	        resultado.add(p.getPalabra());
	    }
	    return resultado;
	}

	

	public ArbolBinarioDeBusqueda<PalabraConPosiciones> getArbolDePalabras() {
		return arbolDePalabras;
	}

	

	public List<PalabraConPosiciones> getListaDePalabras() {
		return new ArrayList<>(listaDePalabras); // retorna una copia para preservar encapsulamiento	
	}

	

	// SETTERS
	private void setListaDePalabras(List<PalabraConPosiciones> listaDePalabras) {
		this.listaDePalabras = listaDePalabras;
	}
	private void setArbolDePalabras(ArbolBinarioDeBusqueda<PalabraConPosiciones> arbolDePalabras) {
		this.arbolDePalabras = arbolDePalabras;
	}
}