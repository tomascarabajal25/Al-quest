package juego.ciudades.complejidad;

public class PasoTeoremaMaestro {

    private final String descripcion;

    /**
     * @param descripcion texto explicando qué se hizo en este paso
     */
    public PasoTeoremaMaestro(String descripcion) {
        this.descripcion = descripcion;
    }

    /** @return descripción del paso */
    public String getDescripcion() {
        return descripcion;
    }

    @Override   //redundante
    public String toString() {
        return descripcion;
    }
}
