package modelosVista;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import modelos.Objeto;
import utils.ValidacionesUtiles;

public class ObjetoVista extends Objeto {
    //INTERFACES ----------------------------------------------------------------------------------------------
    //ENUMERADOS ----------------------------------------------------------------------------------------------
    //CONSTANTES ----------------------------------------------------------------------------------------------
    //ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
    //ATRIBUTOS -----------------------------------------------------------------------------------------------
    private BufferedImage imagen = null;
    private int worldX;
    private int worldY;
    //ATRIBUTOS TRANSITORIOS ----------------------------------------------------------------------------------
    //CONSTRUCTORES -------------------------------------------------------------------------------------------

    /**
     * onstructor del TDA ObjetoVista
     *
     * PRE:
     * -Nombre e imagen no deben ser nulos
     * -X e y deben ser mayores o iguales a cero
     *
     * @param x: posicion en x del objeto
     * @param y: posicion en y del objeto
     * @param nombre: Nombre del objeto
     * @param colision: Estado de colision del objeto
     * @param imagen: Imagen del objeto
     */
    public ObjetoVista(int x, int y,String nombre, boolean colision, BufferedImage imagen) {
        super(nombre, colision);
        ValidacionesUtiles.esDistintoDeNull(imagen, "imagen");
        ValidacionesUtiles.validarMayorOIgualACero(x, "x");
        ValidacionesUtiles.validarMayorOIgualACero(y, "y");

        setWorldX(x);
        setWorldY(y);
        setImagen(imagen);
    }
    //METODOS ABSTRACTOS --------------------------------------------------------------------------------------
    //METODOS HEREDADOS (CLASE)--------------------------------------------------------------------------------
    //METODOS HEREDADOS (INTERFACE)----------------------------------------------------------------------------
    //METODOS DE CLASE ----------------------------------------------------------------------------------------
    //METODOS GENERALES ---------------------------------------------------------------------------------------
    //METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

    /**
     * Dibuja el objeto si es visible
     *
     * PRE:
     * -G2 y bista no deben ser nulos
     *
     * @param g2:
     * @param vista:
     */
    public void draw(Graphics2D g2, Vista vista) {
        if (!estaEnPantalla(vista)) {
            return;
        }
        if (imagen == null) {
            return;
        }

        g2.drawImage(imagen, getScreenX(vista), getScreenY(vista), vista.getTamanio(), vista.getTamanio(), null);
    }

    /**
     * Verifica si el objeto esta dentro del area visible
     *
     * PRE:
     * -Vista no debe ser nulo
     *
     * @return: devuelve true si el objeto está dentro del área visible
     */
    protected boolean estaEnPantalla(Vista vista) {
        ValidacionesUtiles.esDistintoDeNull(vista, "vista");

        int jx = vista.getJugadorVista().getWorldX();
        int jy = vista.getJugadorVista().getWorldY();
        int sx = vista.getJugadorVista().getScreenX();
        int sy = vista.getJugadorVista().getScreenY();

        return worldX + vista.getTamanio() > jx - sx && worldX - vista.getTamanio() < jx + sx &&
               worldY + vista.getTamanio() > jy - sy && worldY - vista.getTamanio() < jy + sy;
    }
    //METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------
    //GETTERS REDEFINIDOS -------------------------------------------------------------------------------------
    //GETTERS INICIALIZADOS -----------------------------------------------------------------------------------
    //GETTERS COMPLEJOS ---------------------------------------------------------------------------------------
    //GETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * Getter del atributo imagen
     * @return: Devuelve el valor del atributo
     */
    public BufferedImage getImagen() {
        return this.imagen;
    }

    /**
     * Getter del atributo worldX
     * @return: Devuelve el valor del atributo
     */
    public int getWorldX() {
        return this.worldX;
    }

    /**
     * Getter del atributo worldY
     * @return: Devuelve el valor del atributo
     */
    public int getWorldY() {
        return this.worldY;
    }
    /**
     * post: posición X en pantalla relativa al jugador
     */
    protected int getScreenX(Vista vista) {
        return worldX - vista.getJugadorVista().getWorldX()
                + vista.getJugadorVista().getScreenX();
    }

    /**
     * post: posición Y en pantalla relativa al jugador
     */
    protected int getScreenY(Vista vista) {
        return worldY - vista.getJugadorVista().getWorldY()
                + vista.getJugadorVista().getScreenY();
    }
    //SETTERS COMPLEJOS----------------------------------------------------------------------------------------
    //SETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * Setter del atributo worldY
     *
     * PRE:
     * -WorldY debe ser mayor o igual a cero
     *
     * @param worldY:
     */
    public void setWorldY(int worldY) {
        ValidacionesUtiles.validarMayorOIgualACero(worldY, "worldY");
        this.worldY = worldY;
    }

    /**
     * Setter del atributo worldX
     *
     * PRE:
     * -WorldX debe ser mayor o igual a cero
     *
     * @param worldX:
     */
    public void setWorldX(int worldX) {
        ValidacionesUtiles.validarMayorOIgualACero(worldY, "worldX");
        this.worldX = worldX;
    }

    /**
     * Setter del atributo imagen
     *
     * PRE:
     * -Imagen no debe ser nulo
     *
     * @param imagen: imagen del objeto
     */
    protected void setImagen(BufferedImage imagen) {
        ValidacionesUtiles.esDistintoDeNull(imagen, "imagen");
        this.imagen = imagen;
    }

	

 
	
	



}
