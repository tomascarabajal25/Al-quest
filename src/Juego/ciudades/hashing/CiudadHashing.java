package juego.ciudades.hashing;

import estructuras.hashing.HashTable;
import estructuras.hashing.HashTable.EntradaHash;
import estructuras.listas.ListaSimplementeEnlazada;
import modelos.Elemento;

/**
 * Logica completa de la Ciudad 6: Hashing.
 * 
 * Contiene la tabla hash y produce PasoHash, que la consola muestra.
 * No se involucra con jugador ni con puntaje, de eso se va a ocupar PartidaHashing.
 */

public class CiudadHashing {
    //ATRIBUTOS
    private final HashTable<Integer, Elemento> tabla;
    private final AdministradorPasosHash administrador;

    
    //CONSTRUCTORES
    /**
     * PRE: cantidadSlots > 0
     * POST: se crea la ciudad, con la tabla hash vacia.
     */
    public CiudadHashing(int cantidadSlots) {
        this.tabla = new HashTable<>(cantidadSlots);
        this.administrador = new AdministradorPasosHash();
    }


    //METODOS DE COMPORTAMIENTO
    /**
     * Voy a calcular el indice donde caeria una clave, NO modifico la tabla.
     * Se usa en el juego, este es el valor que el jugador debe adivinar.
     * PRE: la clave no puede ser null
     */
    public int calcularIndice(Integer clave) {
        //Manejo error:
        if (clave == null) {
            throw new IllegalArgumentException("ERROR: la clave no puede ser null.");
        }
        //Si funciona:
        return this.tabla.calcularIndice(clave);
    }

    /**
     * Va a insertar un par clave-valor en la tabla y registra el paso.
     * Si el slot ya contenia otra clave antes de la insercion, hubo colision.
     * 
     * PRE: clave y valor no pueden ser nulos.
     * POST: Tabla queda con el par almacenado y se agrega un PAsoHash al historial.
     */
    public PasoHash insertar(Integer clave, Elemento valor) {
        //manejo errores:
        if (clave == null){
            throw new IllegalArgumentException("ERROR: La clave no puede ser null.");
        }
        if (valor == null){
            throw new IllegalArgumentException("ERROR: El valor no puede ser null.");
        }

        //si no hay errores:
        int indice = this.tabla.calcularIndice(clave);
        boolean colisiono = this.tabla.getSlot(indice).size() > 0 && !this.tabla.contiene(clave);

        this.tabla.insertar(clave, valor);

        String descripcion = colisiono ? "¡¡¡Colision!!!: la clave " + clave + " cayo en el slot " + indice + "que ya tenia elementos. Se encadena al final."
                                       : "Clave " + clave + " fue insetrada en el slot " + indice + "(No hubo colision).";

        PasoHash paso = new PasoHash(PasoHash.TipoPaso.INSERTAR, clave.toString(), clave.hashCode(), indice, colisiono, true, descripcion);
        this.administrador.agregarPaso(paso);
        return paso;
    }


    /**
     * Busca una clave en la tabla y registra el paso.
     * PRE: la clave no puede ser null.
     * POST: se agrega un PasoHash al historial.
     * @return paso con isExito=true si la clave existe, si no existe, false.
     */
    public PasoHash buscar(Integer clave) {
        //manejo errores
        if (clave == null) {
            throw new IllegalArgumentException("ERROR: La clave no puede ser Null.");
        }
        
        //sin errores, continua
        int indice = this.tabla.calcularIndice(clave);
        Elemento encontrado = this.tabla.buscar(clave);
        boolean exito = (encontrado != null);

        String descripcion = exito ? "Clave " + clave + " encontrada en el slot " + indice + ":" + encontrado.getNombre()
                                   : "Clave " + clave + " no esta en la tabla (se reviso el slot " + indice + ")";

        PasoHash paso = new PasoHash(PasoHash.TipoPaso.BUSCAR, clave.toString(), clave.hashCode(), indice, false, exito, descripcion);
        this.administrador.agregarPaso(paso);
        return paso;
    }


    //GETTERS
    public HashTable<Integer, Elemento> getTabla() {
        return this.tabla;
    }

    public AdministradorPasosHash getAdministrador() {
        return this.administrador;
    }

    public int getCantidadSlots() {
        return this.tabla.getCantidadSlots();
    }

    public ListaSimplementeEnlazada<EntradaHash<Integer, Elemento>> getSlot(int indice) {
        return this.tabla.getSlot(indice);
    }
}
