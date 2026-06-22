package modelosVista;

import java.awt.event.KeyEvent;

import utils.ValidacionesUtiles;


public class KeyhandlerGlobal extends KeyHandler {
    //INTERFACES ----------------------------------------------------------------------------------------------
    //ENUMERADOS ----------------------------------------------------------------------------------------------
    //CONSTANTES ----------------------------------------------------------------------------------------------
    //ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
    //ATRIBUTOS -----------------------------------------------------------------------------------------------
    private boolean tiendaPressed = false;
    private boolean reiniciarPressed = false;
    private boolean meditarPressed = false;
    

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
    	super.keyPressed(e);

        int code = e.getKeyCode();

		if (code == KeyEvent.VK_X) 
			reiniciarPressed = true;
		
		if (code == KeyEvent.VK_T ) 
			tiendaPressed = true;

        if (code ==KeyEvent.VK_K){
            meditarPressed = true;
        }
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
    	super.keyReleased(e);

        int code = e.getKeyCode();
        

		if (code == KeyEvent.VK_X) 
			reiniciarPressed = false;
		
		if (code == KeyEvent.VK_T ) 
			tiendaPressed = false;

        if (code == KeyEvent.VK_K){
            meditarPressed = false;
        }
    }
   
    @Override
    public void reset() {
    	super.reset();
    	this.reiniciarPressed=false;
    	this.tiendaPressed=false;
        this.meditarPressed = false;
    }

    //METODOS GENERALES ---------------------------------------------------------------------------------------
    //METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------
    //METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------
    //GETTERS REDEFINIDOS -------------------------------------------------------------------------------------
    //GETTERS INICIALIZADOS -----------------------------------------------------------------------------------
    //GETTERS COMPLEJOS ---------------------------------------------------------------------------------------
    //GETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * Getter del atributo tiendaPressed
     * @return: Estado del atributo
     */
    public boolean getTiendaPressed() {
        return this.tiendaPressed;
    }

    /**
     * Getter del atributo reiniciarPressed
     * @return: Estado del atributo
     */
    public boolean getReiniciarPressed() {
        return this.reiniciarPressed;
    }


    /** 
     * Getter del atributo meditarPressed
     * @return: estado del atributo meditar
     */
    public boolean getMeditarPressed(){
        return this.meditarPressed;
    }




    //SETTERS COMPLEJOS----------------------------------------------------------------------------------------
    //SETTERS SIMPLES -----------------------------------------------------------------------------------------

    
    /**
     * Setter de atributo tiendaPressed
     * @param leftPressed: nuevo estado del atributp
     */
    public void setTiendaPressed(boolean tiendaPressed) {
        this.tiendaPressed = tiendaPressed;
    }

    /**
     * Setter de atributo reiniciarPressed
     * @param rightPressed: nuevo estado del atributp
     */
    public void setReiniciarPressed(boolean reiniciarPressed) {
        this.reiniciarPressed = reiniciarPressed;
    }
}
