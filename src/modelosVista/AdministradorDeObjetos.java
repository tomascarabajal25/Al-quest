package modelosVista;

import utils.ValidacionesUtiles;

public class AdministradorDeObjetos {
    //INTERFACES ----------------------------------------------------------------------------------------------
    //ENUMERADOS ----------------------------------------------------------------------------------------------
    //CONSTANTES ----------------------------------------------------------------------------------------------
    //ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
    //ATRIBUTOS -----------------------------------------------------------------------------------------------
    private Vista vista = null;
    //ATRIBUTOS TRANSITORIOS ----------------------------------------------------------------------------------
    //CONSTRUCTORES -------------------------------------------------------------------------------------------

    /**
     * Constructor del TDA AdministradorDeObjetos
     *
     * PRE:
     * -Vista no debe ser nulo
     *
     * @param vista: Vista
     */
    public AdministradorDeObjetos(Vista vista) {
        ValidacionesUtiles.esDistintoDeNull(vista, "vista");
        setVista(vista);
    }
    //METODOS ABSTRACTOS --------------------------------------------------------------------------------------
    //METODOS HEREDADOS (CLASE)--------------------------------------------------------------------------------
    //METODOS HEREDADOS (INTERFACE)----------------------------------------------------------------------------
    //METODOS DE CLASE ----------------------------------------------------------------------------------------
    //METODOS GENERALES ---------------------------------------------------------------------------------------
    //METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

    /**
     * Agrega objetos a la vista
     *
     * PRE:
     * -Objetos no debe ser nulo
     *
     * @param objetos: Objetos a agregar a la vista
     */
    public void setObjetos(ObjetoVista ...objetos) {
        ValidacionesUtiles.esDistintoDeNull(objetos, "objetos");

        for (ObjetoVista objeto:objetos) {
            if (objeto != null) {
                vista.agregarObjeto(objeto);
            }
        }
    }
    //METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------
    //GETTERS REDEFINIDOS -------------------------------------------------------------------------------------
    //GETTERS INICIALIZADOS -----------------------------------------------------------------------------------
    //GETTERS COMPLEJOS ---------------------------------------------------------------------------------------
    //GETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * Getter del atributo vista
     *
     * @return: Devuelve el valor del atributo vista
     */
    public Vista getVista() {
        return this.vista;
    }
    //SETTERS COMPLEJOS----------------------------------------------------------------------------------------
    //SETTERS SIMPLES -----------------------------------------------------------------------------------------
    /**
     * Setter del atributo vista
     *
     * PRE:
     * -Vista no debe ser nulo
     *
     * @param vista: Vista
     */
    private void setVista(Vista vista) {
        ValidacionesUtiles.esDistintoDeNull(vista, "vista");
        this.vista = vista;
    }




}
