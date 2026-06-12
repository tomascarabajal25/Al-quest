package estructuras.hashing;

import java.util.ArrayList;
import java.util.List;

import estructuras.listas.ListaSimplementeEnlazada;

/**
 * Creo el HashTable con manejo de colisiones, va por encadenamiento
 * Cada posicion del array de slots va a ser una listaSimplementeEnlazada
 * de pares clave-valor, si dos claves tienen el mismo indice, conviven en la misma lista.
 *
 * Hago HashTable generico, luego al llamarlo decido los tipos:
 * @param <K> tipo clave
 * @param <V> tipo valor
 */

public class HashTable<K, V> {
    //ATRIBUTOS
    private List<ListaSimplementeEnlazada<EntradaHash<K, V>>> slots;
    private int cantidadSlots;
    private int cantidadElementos;

    /**
     * Constructor para inicializar la tabla hash con una capacidad dada.
     * PRE: cantidadSlots mayor a 0
     * POST: se crea la tabla hash, vacia con la cantidad indicada.
     *       cada Slot tendra una lista vacia.
     *
     * @param cantidadSlots tamaño del array para los Slots
     */
    public HashTable(int cantidadSlots) {
        if (cantidadSlots <= 0) {
            throw new IllegalArgumentException("ERROR: Cantidad de Slots debee ser mas de cero");
        }
        this.cantidadSlots = cantidadSlots;
        this.cantidadElementos = 0; //Lo inicializo en 0 ya que ahora no hay elementos en los slots.

        //Lista vacia en cada slot
        this.slots = new ArrayList<>(cantidadSlots);
        for (int i = 0; i < cantidadSlots; i++){
            this.slots.add(new ListaSimplementeEnlazada<>());
        }
    }


    //METODOS DE COMPORTAMIENTO
    /**
     * Ahora debo calcular el indice del slot donde va a caer una clave, pero no modifico la tabla.
     * Sirve para que la ciudad muestre a la jugador el indice esperado.
     *
     * PRE: La clave no puede ser nula
     * POST: Devuelve un entero, en el rango de 0 a (cantidadSlots-1)
     *
     * Voy a usar "%" , el indice sera clave % cantidadSlots (Los slots seran numero primo)
     * 
     * @param clave clave a hashear
     * @return indice del slot correspondiente
     */
    public int calcularIndice(K clave) {
        if (clave == null) {
            throw new IllegalArgumentException("ERROR: La clave (clave) no puede ser nula.");
        }
        return Math.abs(clave.hashCode() % this.cantidadSlots); 
    }

    /**
     * Inserto el par clave-valor en la tabla.
     * Si la clave ya existe, se sobreescribe el valor (no duplico).
     * Si dos claves distintas caen en el mismo slot, se encadena en la lista del slot.
     * 
     * PRE: clave y valor no deben ser nulos
     * POST: el par clave-valor, queda almacenado en el slot.
     * cantidadElemento solo si la clave no estaba previamente.
     * 
     * @param clave clave del par
     * @param valor valor asociado a la clave
     */
    public void insertar(K clave, V valor) {
        if (clave == null){
            throw new IllegalArgumentException("ERROR: La clave no debe ser null");
        }
        if (valor == null){
            throw new IllegalArgumentException("ERORR: El valor no debe ser null");
        }

        //Calculo indice
        int indice = calcularIndice(clave);
        ListaSimplementeEnlazada<EntradaHash<K, V>> slot = this.slots.get(indice);

        //Si la clave ya existe, sobreescribe sin sumar elementos.
        for (EntradaHash<K, V> entrada : slot) {
            if (entrada.getClave().equals(clave)) {
                entrada.setValor(valor);
                return;
            }
        }

        //Agrega la nueva entrada al final de la lista.
        slot.add(new EntradaHash<>(clave, valor));
        this.cantidadElementos++;
    }

    /**
     * Busca el valor asociado a una clave.
     * Logica muy similar a insertar.
     * 
     * PRE: la clave no puede ser nula
     * POST: Devuelve el valor asociado a la clave, si no encuentra la clave en la tabla, null.
     * 
     * @param clave clave a buscar
     * @return valor asociado, o null si no encuentra.
     */
    public V buscar(K clave) {
        if (clave == null) {
            throw new IllegalArgumentException("ERROR: La clave no puede ser null");
        }
        //Calculo indice
        int indice = calcularIndice(clave);
        ListaSimplementeEnlazada<EntradaHash<K, V>> slot = this.slots.get(indice);

        //Si la clave existe, devuelve el valor asociado.
        for (EntradaHash<K, V> entrada : slot) {
            if (entrada.getClave().equals(clave)) {
                return entrada.getValor();
            }
        }
        //No encuentra clave en la lista:
        return null;
    
    }

    /**
     * Indica si una clave esta en la tabla.
     *
     * PRE: Clave no nula
     * POST: Devuelve true si la clave existe en la tabla, false en caso contrario.
     */
    public boolean contiene(K clave) {
        return buscar(clave) != null;
    }


    //GETTERS COMPLEJOS
    /**
     * Devuelve el slot (lista de entradas) en una posicion dada.
     * Se va a usar para mostrar el contenido de la tabla.
     * 
     * PRE: indice en rango 0, cantidadSlots-1
     * POST: devuelve la lista del slot solicitado.
     */
    public ListaSimplementeEnlazada<EntradaHash<K, V>> getSlot(int indice) {
        if (indice < 0 || indice >= this.cantidadSlots) {
            throw new IllegalArgumentException("Error: indice esta fuera de rango:" +indice);
        }
        return this.slots.get(indice);
    }


    //GETTERS SIMPLES
    /**
     * @return cantidad total de slots en la tabla
     */
    public int getCantidadSlots() {
        return this.cantidadSlots;
    }

    /**
     * @return cantidad total de pares clave-valor almacenados (elementos).
     */
    public int getCantidadElementos() {
        return this.cantidadElementos;
    }


    //CLASES INTERNAS
    /**
     * Par clave-valor que vive dentro de cada slot encadenado.
     * Clase interna estatica: no depende de la instancia externa.
     */    
    public static class EntradaHash<K, V> {
        private final K clave;
        private V valor;

        //cada vez que algo se inserta en la tabla hash
        public EntradaHash(K clave, V valor) {
            this.clave = clave;
            this.valor = valor;
        }

        //acceso al dato clave, inmutable, no cambia
        public K getClave() {
            return this.clave;
        }

        //acceso al dato valor
        public V getValor() {
            return this.valor;
        }

        //acceso a valor, mutable, puede cambiar
        public void setValor(V valor) {
            this.valor = valor;
        }

        //para imprimirlo
        @Override
        public String toString() {
            return this.clave + " -> " + this.valor;
        }
    }


}