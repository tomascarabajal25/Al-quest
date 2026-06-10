package modelosVista;

import utils.ValidacionesUtiles;

public class ChequeadorDeColision {
    //INTERFACES ----------------------------------------------------------------------------------------------
    //ENUMERADOS ----------------------------------------------------------------------------------------------
    //CONSTANTES ----------------------------------------------------------------------------------------------
    //ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
    //ATRIBUTOS -----------------------------------------------------------------------------------------------
    private Vista gp = null;
    //ATRIBUTOS TRANSITORIOS ----------------------------------------------------------------------------------
    //CONSTRUCTORES -------------------------------------------------------------------------------------------

    /**
     * Constructor del TDA ChequeadorDeColision
     *
     * PRE:
     * -Gp no debe ser nulo
     *
     * @param gp: Vista
     */
    public ChequeadorDeColision(Vista gp) {
        ValidacionesUtiles.esDistintoDeNull(gp, "gp");
        setGp(gp);
    }
    //METODOS ABSTRACTOS --------------------------------------------------------------------------------------
    //METODOS HEREDADOS (CLASE)--------------------------------------------------------------------------------
    //METODOS HEREDADOS (INTERFACE)----------------------------------------------------------------------------
    //METODOS DE CLASE ----------------------------------------------------------------------------------------
    //METODOS GENERALES ---------------------------------------------------------------------------------------
    //METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

    /**
     * Manejador de colisiones
     *
     * PRE:
     * -Entidad no debe ser nulo
     *
     * @param entidad: Objeto al que se le quiere manejar la colision
     */
    public void chequearConstruccion(EntidadVista entidad) {
        ValidacionesUtiles.esDistintoDeNull(entidad, "entity");

        int entityLeftWorldX = entidad.getWorldX() + entidad.getAreaSolida().x;
        int entityRightWorldX = entidad.getWorldX() + entidad.getAreaSolida().x + entidad.getAreaSolida().width;
        int entityTopWorldY = entidad.getWorldY() + entidad.getAreaSolida().y;
        int entityBottomWorldY = entidad.getWorldY() + entidad.getAreaSolida().y + entidad.getAreaSolida().height;

        int entityLeftCol = entityLeftWorldX / this.gp.getTamanio();
        int entityRightCol = entityRightWorldX / this.gp.getTamanio();
        int entityTopRow = entityTopWorldY / this.gp.getTamanio();
        int entityBottomRow = entityBottomWorldY / gp.getTamanio();

        int tileNum1, tileNum2;
        Direccion direction = entidad.getDireccion();

        switch (direction) {
            case Direccion.ARRIBA:
                entityTopRow = (entityTopWorldY - entidad.getVelocidad()) / this.gp.getTamanio();
                tileNum1 = this.gp.construccionesM.mapaDeConstruccionesNum[entityLeftCol][entityTopRow];
                tileNum2 = this.gp.construccionesM.mapaDeConstruccionesNum[entityRightCol][entityTopRow];

                if (this.gp.construccionesM.construcciones[tileNum1].getColision()== true || this.gp.construccionesM.construcciones[tileNum2].getColision()== true) {
                    entidad.setColisionOn(true);;
                }
                break;
            case Direccion.ABAJO:
                entityBottomRow = (entityBottomWorldY + entidad.getVelocidad()) / this.gp.getTamanio();
                tileNum1 = this.gp.construccionesM.mapaDeConstruccionesNum[entityLeftCol][entityBottomRow];
                tileNum2 = this.gp.construccionesM.mapaDeConstruccionesNum[entityRightCol][entityBottomRow];

                if (this.gp.construccionesM.construcciones[tileNum1].getColision()== true || this.gp.construccionesM.construcciones[tileNum2].getColision()== true) {
                    entidad.setColisionOn(true);;
                }
                break;
            case Direccion.IZQUIERDA:
                entityLeftCol = (entityLeftWorldX - entidad.getVelocidad()) / this.gp.getTamanio();
                tileNum1 = this.gp.construccionesM.mapaDeConstruccionesNum[entityLeftCol][entityTopRow];
                tileNum2 = this.gp.construccionesM.mapaDeConstruccionesNum[entityLeftCol][entityBottomRow];

                if (gp.construccionesM.construcciones[tileNum1].getColision()== true || gp.construccionesM.construcciones[tileNum2].getColision()== true) {
                    entidad.setColisionOn(true);;
                }
                break;
            case Direccion.DERECHA:
                entityRightCol = (entityRightWorldX + entidad.getVelocidad()) / this.gp.getTamanio();
                tileNum1 = this.gp.construccionesM.mapaDeConstruccionesNum[entityRightCol][entityTopRow];
                tileNum2 = this.gp.construccionesM.mapaDeConstruccionesNum[entityRightCol][entityBottomRow];

                if (this.gp.construccionesM.construcciones[tileNum1].getColision()== true || this.gp.construccionesM.construcciones[tileNum2].getColision()== true) {
                    entidad.setColisionOn(true);;
                }
                break;
        }
    }
    //METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------
    //GETTERS REDEFINIDOS -------------------------------------------------------------------------------------
    //GETTERS INICIALIZADOS -----------------------------------------------------------------------------------
    //GETTERS COMPLEJOS ---------------------------------------------------------------------------------------
    //GETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * Getter del atributo gp
     *
     * @return: Devuelve el valor del atributo gp
     */
    public Vista getGp() {
        return this.gp;
    }
    //SETTERS COMPLEJOS----------------------------------------------------------------------------------------
    //SETTERS SIMPLES -----------------------------------------------------------------------------------------
    /**
     * Setter del atributo gp
     *
     * PRE:
     * -Gp no debe ser nulo
     *
     * @param gp: vista
     */
    private void setGp(Vista gp) {
        ValidacionesUtiles.esDistintoDeNull(gp, "gp");
        this.gp = gp;
    }
}