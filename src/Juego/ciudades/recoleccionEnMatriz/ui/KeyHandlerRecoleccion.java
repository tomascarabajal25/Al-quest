package Juego.ciudades.recoleccionEnMatriz.ui;

import modelosVista.KeyHandler;
import utils.ValidacionesUtiles;

import java.awt.event.KeyEvent;

public class KeyHandlerRecoleccion extends KeyHandler {
    //INTERFACES ----------------------------------------------------------------------------------------------
    //ENUMERADOS ----------------------------------------------------------------------------------------------
    //CONSTANTES ----------------------------------------------------------------------------------------------
    //ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
    //ATRIBUTOS -----------------------------------------------------------------------------------------------
    private boolean ePressed = false;
    private boolean pPressed = false;
    private int cartaPresionada = 0;
    //ATRIBUTOS TRANSITORIOS ----------------------------------------------------------------------------------
    //CONSTRUCTORES -------------------------------------------------------------------------------------------
    //METODOS ABSTRACTOS --------------------------------------------------------------------------------------
    //METODOS HEREDADOS (CLASE)--------------------------------------------------------------------------------
    //METODOS HEREDADOS (INTERFACE)----------------------------------------------------------------------------
    //METODOS DE CLASE ----------------------------------------------------------------------------------------
    //METODOS GENERALES ---------------------------------------------------------------------------------------
    //METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

    /**
     * Metodo manejador para la teclas presionadas asociadas a la recoleccion de las cartar y a la mochila
     *
     * PRE:
     * -E no debe ser nulo
     *
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_E){
            setEPressed(true);
        }
        if (code == KeyEvent.VK_P){
            setPPressed(true);
        }

        if (code == KeyEvent.VK_1){
            setCartaPresionada(1);
        }
        if (code == KeyEvent.VK_2){
            setCartaPresionada(2);
        }
        if (code == KeyEvent.VK_3){
            setCartaPresionada(3);
        }
    }

    /**
     * Metodo manejador para la teclas liberadas asociadas a la recoleccion de las cartar y a la mochila
     *
     * PRE:
     * -E no debe ser nulo
     *
     * @param e the event to be processed
     */
    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_E){
            setEPressed(false);
        }
        if (code == KeyEvent.VK_P) {
            setPPressed(false);
        }
        if (code == KeyEvent.VK_1 || code == KeyEvent.VK_2 || code == KeyEvent.VK_3){
            cartaPresionada = 0;
        }
    }

    /**
     * Actualiza el estado del atributo ePressed
     *
     * PRE:
     * -Estado no debe  ser nulo
     *
     * @param estado: Estado nuevo
     */
    protected void modificarEstadoEPressed(boolean estado){
        ValidacionesUtiles.esDistintoDeNull(estado, "'estado");
        setEPressed(estado);
    }

    /**
     * Actualiza el estado del atributo pPressed
     *
     * PRE:
     * -Estado no debe ser nulo
     *
     * @param estado: Estado nuevo
     */
    protected void modificarEstadoPPressed(boolean estado){
        ValidacionesUtiles.esDistintoDeNull(estado, "'estado");
        setPPressed(estado);
    }

    /**
     * Restablecer el estado del atributo cartaPresionada a cero
     */
    public void restablecerCartaPresionada(){
        setCartaPresionada(0);
    }
    //METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------
    //GETTERS REDEFINIDOS -------------------------------------------------------------------------------------
    //GETTERS INICIALIZADOS -----------------------------------------------------------------------------------
    //GETTERS COMPLEJOS ---------------------------------------------------------------------------------------
    //GETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * Getter del atributo ePressed
     * @return: Devuelve el estado del atributo ePressed
     */
    public boolean getEPressed() {
        return this.ePressed;
    }

    /**
     * Getter del atributo pPressed
     * @return: Devuelve el estado del atributo pPressed
     */
    public boolean getPPressed() {
        return this.pPressed;
    }

    /**
     * Getter del atributo cartaPresionada
     * @return: Devuelve el valor del atributo cartaPresionada
     */
    public int getCartaPresionada() {
        return this.cartaPresionada;
    }

    //SETTERS COMPLEJOS----------------------------------------------------------------------------------------
    //SETTERS SIMPLES -----------------------------------------------------------------------------------------
    /**
     * Setter del atributo ePressed
     *
     * PRE:
     * -NuevoEstado no debe ser nulo
     */
    private void setEPressed(boolean nuevoEstado) {
        ValidacionesUtiles.esDistintoDeNull(nuevoEstado, "nuevoEstado");
        this.ePressed = nuevoEstado;
    }

    /**
     * Setter del atributo pPressed
     *
     * PRE:
     * -NuevoEstado no debe ser nulo
     */
    private void setPPressed(boolean nuevoEstado) {
        ValidacionesUtiles.esDistintoDeNull(nuevoEstado, "nuevoEstado");
        this.pPressed = nuevoEstado;
    }

    /**
     * Setter del atributo cartaPresionada
     *
     * PRE:
     * -Valor debe ser mayor a cero
     */
    private void setCartaPresionada(int valor) {
        ValidacionesUtiles.validarMayorACero(valor, "valor");
        this.cartaPresionada = valor;
    }



}