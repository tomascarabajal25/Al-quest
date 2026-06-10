package modelosVista;

import utils.ValidacionesUtiles;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    //INTERFACES ----------------------------------------------------------------------------------------------
    //ENUMERADOS ----------------------------------------------------------------------------------------------
    //CONSTANTES ----------------------------------------------------------------------------------------------
    //ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
    //ATRIBUTOS -----------------------------------------------------------------------------------------------
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    //ATRIBUTOS TRANSITORIOS ----------------------------------------------------------------------------------
    //CONSTRUCTORES -------------------------------------------------------------------------------------------
    //METODOS ABSTRACTOS --------------------------------------------------------------------------------------
    //METODOS HEREDADOS (CLASE)--------------------------------------------------------------------------------
    //METODOS HEREDADOS (INTERFACE)----------------------------------------------------------------------------
    //METODOS DE CLASE ----------------------------------------------------------------------------------------

    /**
     *
     * PRE:
     * -E no debe ser nulo
     *
     * @param e Evento
     */
    @Override
    public void keyTyped(KeyEvent e) {
        ValidacionesUtiles.esDistintoDeNull(e, "e");
    }


    /**
     * Manejador de la tecla al apretarla
     *
     * PRE:
     * -E no debe ser n ulo
     *
     * @param e: Evento
     */
    @Override
    public void keyPressed(KeyEvent e) {
        ValidacionesUtiles.esDistintoDeNull(e, "e");

        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP)
            this.upPressed = true;
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN)
            this.downPressed = true;
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT)
            this.leftPressed = true;
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT)
            this.rightPressed = true;


    }

    /**
     * Manejador de la tecla al liberarla
     *
     * PRE:
     * -E no debe ser n ulo
     *
     * @param e: Evento
     */
    @Override
    public void keyReleased(KeyEvent e) {
        ValidacionesUtiles.esDistintoDeNull(e, "e");

        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP)
            this.upPressed = false;
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN)
            this.downPressed = false;
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT)
            this.leftPressed = false;
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT)
            this.rightPressed = false;


    }
    //METODOS GENERALES ---------------------------------------------------------------------------------------
    //METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------
    //METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------
    //GETTERS REDEFINIDOS -------------------------------------------------------------------------------------
    //GETTERS INICIALIZADOS -----------------------------------------------------------------------------------
    //GETTERS COMPLEJOS ---------------------------------------------------------------------------------------
    //GETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * Getter del atributo upPressed
     * @return: Estado del atributo
     */
    public boolean getUpPressed() {
        return this.upPressed;
    }

    /**
     * Getter del atributo downPressed
     * @return: Estado del atributo
     */
    public boolean getDownPressed() {
        return this.downPressed;
    }

    /**
     * Getter del atributo leftPressed
     * @return: Estado del atributo
     */
    public boolean getLeftPressed() {
        return this.leftPressed;
    }

    /**
     * Getter del atributo rightPressed
     * @return: Estado del atributo
     */
    public boolean getRightPressed() {
        return this.rightPressed;
    }

    //SETTERS COMPLEJOS----------------------------------------------------------------------------------------
    //SETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * Setter de atributo upPressed
     * @param upPressed: nuevo estado del atributp
     */
    public void setUpPressed(boolean upPressed) {
        this.upPressed = upPressed;
    }

    /**
     * Setter de atributo downPressed
     * @param downPressed: nuevo estado del atributp
     */
    public void setDownPressed(boolean downPressed) {
        this.downPressed = downPressed;
    }

    /**
     * Setter de atributo leftPressed
     * @param leftPressed: nuevo estado del atributp
     */
    public void setLeftPressed(boolean leftPressed) {
        this.leftPressed = leftPressed;
    }

    /**
     * Setter de atributo rightPressed
     * @param rightPressed: nuevo estado del atributp
     */
    public void setRightPressed(boolean rightPressed) {
        this.rightPressed = rightPressed;
    }
}
