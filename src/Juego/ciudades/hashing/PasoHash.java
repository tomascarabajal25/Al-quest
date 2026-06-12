package juego.ciudades.hashing;

/**
 * Archivo tipo DTO, no contiene logica.
 * Es inmutable, representa un paso ejecutado sobre la tabla hash
 * puede ser una insercion o una busqueda.
 * La ciudad lo usa para mostrarle al jugador que paso en cada operacion
 */

public class PasoHash {

    //ENUM
    //cada paso puede ser solo o insertar o buscar:
    public enum TipoPaso {INSERTAR, BUSCAR}

    //ATRIBUTOS
    //final porque es inmutable.
    //Se garantiza que el historico de pasos sea confiable.
    private final TipoPaso tipo;
    private final String claveTexto;
    private final int hashCalculado;
    private final int indiceSlot;
    private final boolean huboColision;
    private final boolean exito;
    private final String descripcion;


    //CONSTRUCTORES
    /**
     * Contructor del TDA PasoHash.
     * PRE: tipo, claveTexto y descripcion no pueden ser nulos.
     * POST: Se crea un paso, es inmutable, esta completamente seteado.
     */
    public PasoHash(TipoPaso tipo, String claveTexto, int hashCalculado, int indiceSlot, boolean huboColision, boolean exito, String descripcion){
        //Manejo errores
        if (tipo == null) {
            throw new IllegalArgumentException("ERROR: El tipo no puede ser null.");
        }
        if (claveTexto == null){
            throw new IllegalArgumentException("ERROR: la clave no puede ser null.");
        }
        if (descripcion == null){
            throw new IllegalArgumentException("ERROR: la descripcion no puede ser null.");
        }

        this.tipo = tipo;
        this.claveTexto = claveTexto;
        this.hashCalculado = hashCalculado;
        this.indiceSlot = indiceSlot;
        this.huboColision = huboColision;
        this.exito = exito;
        this.descripcion = descripcion;
    }


    //GETTERS SIMPLES
    public TipoPaso getTipo() {
        return tipo; 
    }
    
    public String getClaveTexto() {
        return claveTexto;
    }
    
    public int getHashCalculado() {
        return hashCalculado;
    }

    public int getIndiceSlot() {
        return indiceSlot;
    }

    public boolean isHuboColision() {
        return huboColision;
    }

    public boolean isExito() {
        return exito;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return "[" + tipo + "] clave= " + claveTexto + ", hash=" + hashCalculado + ", indice=" + indiceSlot + ", colision=" + huboColision + ", exito= " + exito + " |" + descripcion;
    }

}
