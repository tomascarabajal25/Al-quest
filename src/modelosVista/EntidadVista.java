package modelosVista;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import modelos.Entidad;
import utils.ValidacionesUtiles;

public class EntidadVista extends Entidad {
    //INTERFACES ----------------------------------------------------------------------------------------------
    //ENUMERADOS ----------------------------------------------------------------------------------------------
    //CONSTANTES ----------------------------------------------------------------------------------------------
    //ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
    //ATRIBUTOS -----------------------------------------------------------------------------------------------
    private BufferedImage up1 = null;
    private BufferedImage up2 = null;
    private BufferedImage down1 = null;
    private BufferedImage down2 = null;
    private BufferedImage right1 = null;
    private BufferedImage right2 = null;
    private BufferedImage left1 = null;
    private BufferedImage left2 = null;
    private int worldX;
    private int worldY;
    private int velocidad=4;
    private int spriteNum= 1;
    private int spriteCounter= 0;
    private Rectangle areaSolida = null;
    private boolean colisionOn = false;
    private Direccion direccion = null;
    //ATRIBUTOS TRANSITORIOS ----------------------------------------------------------------------------------
    //CONSTRUCTORES -------------------------------------------------------------------------------------------
    public EntidadVista(String nombre) {
        super(nombre);
    }
    //METODOS ABSTRACTOS --------------------------------------------------------------------------------------
    //METODOS HEREDADOS (CLASE)--------------------------------------------------------------------------------
    //METODOS HEREDADOS (INTERFACE)----------------------------------------------------------------------------
    //METODOS DE CLASE ----------------------------------------------------------------------------------------
    //METODOS GENERALES ---------------------------------------------------------------------------------------
    //METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

    /**
     * Cambia el valor de velocidad
     *
     * PRE:
     * -Velocidad debe ser mayor a cero
     *
     * @param velocidad: velocidad
     */
    public void cambiarVelocidad(int velocidad) {
        ValidacionesUtiles.validarMayorACero(velocidad, "velocidad");
        this.velocidad = velocidad;
    }
    //METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------
    //GETTERS REDEFINIDOS -------------------------------------------------------------------------------------
    //GETTERS INICIALIZADOS -----------------------------------------------------------------------------------
    //GETTERS COMPLEJOS ---------------------------------------------------------------------------------------
    //GETTERS SIMPLES -----------------------------------------------------------------------------------------
    /**
     * Getter del atributo up1
     * @return: Devuelve el valor del atributo up1
     */
    public BufferedImage getUp1() {
        return this.up1;
    }

    /**
     * Getter del atributo up2
     * @return: Devuelve el valor del atributo up2
     */
    public BufferedImage getUp2() {
        return this.up2;
    }

    /**
     * Getter del atributo down1
     * @return: Devuelve el valor del atributo down1
     */
    public BufferedImage getDown1() {
        return this.down1;
    }

    /**
     * Getter del atributo down2
     * @return: Devuelve el valor del atributo down2
     */
    public BufferedImage getDown2() {
        return this.down2;
    }

    /**
     * Getter del atributo right1
     * @return: Devuelve el valor del atributo right1
     */
    public BufferedImage getRight1() {
        return this.right1;
    }

    /**
     * Getter del atributo right2
     * @return: Devuelve el valor del atributo right2
     */
    public BufferedImage getRight2() {
        return this.right2;
    }

    /**
     * Getter del atributo left1
     * @return: Devuelve el valor del atributo left1
     */
    public BufferedImage getLeft1() {
        return this.left1;
    }

    /**
     * Getter del atributo left2
     * @return: Devuelve el valor del atributo left2
     */
    public BufferedImage getLeft2() {
        return this.left2;
    }

    /**
     * Getter del atributo worldX
     * @return: Devuelve el valor del atributo worldX
     */
    public int getWorldX() {
        return this.worldX;
    }

    /**
     * Getter del atributo worldY
     * @return: Devuelve el valor del atributo worldY
     */
    public int getWorldY() {
        return this.worldY;
    }

    /**
     * Getter del atributo velocidad
     * @return: Devuelve el valor del atributo velocidad
     */
    public int getVelocidad() {
        return this.velocidad;
    }

    /**
     * Getter del atributo spriteNum
     * @return: Devuelve el valor del atributo spriteNum
     */
    public int getSpriteNum() {
        return this.spriteNum;
    }

    /**
     * Getter del atributo spriteCounter
     * @return: Devuelve el valor del atributo spriteCounter
     */
    public int getSpriteCounter() {
        return this.spriteCounter;
    }

    /**
     * Getter del atributo areaSolida
     * @return: Devuelve el valor del atributo areaSolida
     */
    public Rectangle getAreaSolida() {
        return this.areaSolida;
    }

    /**
     * Getter del atributo colisionOn
     * @return: Devuelve el valor del atributo colisionOn
     */
    public boolean isColisionOn() {
        return this.colisionOn;
    }

    /**
     * Getter del atributo direccion
     * @return: Devuelve el valor del atributo direccion
     */
    public Direccion getDireccion() {
        return this.direccion;
    }
    //SETTERS COMPLEJOS----------------------------------------------------------------------------------------
    //SETTERS SIMPLES -----------------------------------------------------------------------------------------
    /**
     * Setter del atributo up1
     *
     * PRE
     * -up1 no debe ser nulo
     *
     * @param up1: imagen a guardar en el atributo
     */
	protected void setUp1(BufferedImage up1) {
        ValidacionesUtiles.esDistintoDeNull(up1, "up1");
        this.up1 = up1;
	}

    /**
     * Setter del atributo up2
     *
     * PRE
     * -up2 no debe ser nulo
     *
     * @param up2: imagen a guardar en el atributo
     */
	protected void setUp2(BufferedImage up2) {
        ValidacionesUtiles.esDistintoDeNull(up2, "up2");
        this.up2 = up2;
	}

    /**
     * Setter del atributo right1
     *
     * PRE
     * -right1 no debe ser nulo
     *
     * @param right1: imagen a guardar en el atributo
     */
	protected void setRight1(BufferedImage right1) {
        ValidacionesUtiles.esDistintoDeNull(right1, "right1");
        this.right1 = right1;
	}

    /**
     * Setter del atributo down1
     *
     * PRE
     * -down1 no debe ser nulo
     *
     * @param down1: imagen a guardar en el atributo
     */
    protected void setDown1(BufferedImage down1) {
        ValidacionesUtiles.esDistintoDeNull(down1, "down1");
        this.down1 = down1;
	}

    /**
     * Setter del atributo down2
     *
     * PRE
     * -down2 no debe ser nulo
     *
     * @param down2: imagen a guardar en el atributo
     */
    protected void setDown2(BufferedImage down2) {
        ValidacionesUtiles.esDistintoDeNull(down2, "down2");
        this.down2 = down2;
	}

    /**
     * Setter del atributo right2
     *
     * PRE
     * -right2 no debe ser nulo
     *
     * @param right2: imagen a guardar en el atributo
     */
    protected void setRight2(BufferedImage right2) {
        ValidacionesUtiles.esDistintoDeNull(right2, "right2");
        this.right2 = right2;
	}

    /**
     * Setter del atributo left1
     *
     * PRE
     * -left1 no debe ser nulo
     *
     * @param left1: imagen a guardar en el atributo
     */

    protected void setLeft1(BufferedImage left1) {
        ValidacionesUtiles.esDistintoDeNull(left1, "left1");
        this.left1 = left1;
	}

    /**
     * Setter del atributo left2
     *
     * PRE
     * -left2 no debe ser nulo
     *
     * @param left2: imagen a guardar en el atributo
     */
    protected void setLeft2(BufferedImage left2) {
        ValidacionesUtiles.esDistintoDeNull(left2, "left2");
        this.left2 = left2;
	}

    /**
     * Setter del atributo worldX
     *
     * PRE
     * -worldX debe ser mayor o igual a cero
     *
     * @param worldX: coordenada horizontal a guardar en el atributo
     */
    public void setWorldX(int worldX) {
        ValidacionesUtiles.validarMayorOIgualACero(worldX, "worldX");
        this.worldX = worldX;
	}

    /**
     * Setter del atributo velocidad
     *
     * PRE
     * -velocidad debe ser mayor o igual a cero
     *
     * @param velocidad: velocidad a guardar en el atributo
     */
	protected void setVelocidad(int velocidad) {
        ValidacionesUtiles.validarMayorOIgualACero(velocidad, "velocidad");
        this.velocidad = velocidad;
	}

    /**
     * Setter del atributo worldY
     *
     * PRE
     * -worldY debe ser mayor o igual a cero
     *
     * @param worldY: coordenada vertical a guardar en el atributo
     */
    public void setWorldY(int worldY) {
        ValidacionesUtiles.validarMayorOIgualACero(worldY, "worldY");
        this.worldY = worldY;
	}

    /**
     * Setter del atributo spriteNum
     *
     * PRE
     * -spriteNum debe ser mayor o igual a cero
     *
     * @param spriteNum: número de sprite a guardar en el atributo
     */
    protected void setSpriteNum(int spriteNum) {
        ValidacionesUtiles.validarMayorOIgualACero(spriteNum, "spriteNum");
        this.spriteNum = spriteNum;
	}

    /**
     * Setter del atributo spriteCounter
     *
     * PRE
     * -spriteCounter debe ser mayor o igual a cero
     *
     * @param spriteCounter: contador de sprites a guardar en el atributo
     */
    protected void setSpriteCounter(int spriteCounter) {
        ValidacionesUtiles.validarMayorOIgualACero(spriteCounter, "spriteCounter");
        this.spriteCounter = spriteCounter;
	}

    /**
     * Setter del atributo areaSolida
     *
     * PRE
     * -areaSolida no debe ser nulo
     *
     * @param areaSolida: área sólida a guardar en el atributo
     */
    protected void setAreaSolida(Rectangle areaSolida) {
        ValidacionesUtiles.esDistintoDeNull(areaSolida, "areaSolida");
        this.areaSolida = areaSolida;
	}

    /**
     * Setter del atributo colisionOn
     *
     * @param colisionOn: estado de colisión a guardar en el atributo
     */
	protected void setColisionOn(boolean colisionOn) {
        ValidacionesUtiles.esDistintoDeNull(colisionOn, "colisionOn");
        this.colisionOn = colisionOn;
	}

    /**
     * Setter del atributo direccion
     *
     * PRE
     * -direccion no debe ser nulo
     *
     * @param direccion: dirección a guardar en el atributo
     */
	protected void setDireccion(Direccion direccion) {
        ValidacionesUtiles.esDistintoDeNull(direccion, "direccion");
        this.direccion = direccion;
	}
	
	
	
	
	
	
	
}
