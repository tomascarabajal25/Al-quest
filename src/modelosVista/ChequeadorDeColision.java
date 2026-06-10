package modelosVista;

import Juego.ciudades.recoleccionEnMatriz.ui.CartaVista;
import utils.ValidacionesUtiles;
import java.util.List;

import java.awt.*;

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

    public void chequearCartas(EntidadVista entidad, List<ElementoVista> cartas, int nivel) {

        int futuroX = entidad.getWorldX();
        int futuroY = entidad.getWorldY();

        switch (entidad.getDireccion()) {
            case ARRIBA:
                futuroY -= entidad.getVelocidad();
                break;

            case ABAJO:
                futuroY += entidad.getVelocidad();
                break;

            case IZQUIERDA:
                futuroX -= entidad.getVelocidad();
                break;

            case DERECHA:
                futuroX += entidad.getVelocidad();
                break;
        }

        Rectangle futuro = new Rectangle(
                futuroX,
                futuroY,
                gp.getTamanio(),
                gp.getTamanio()
        );

        for (ElementoVista carta : cartas) {
            if (carta.isRecogido()) {
                continue;
            }

            Rectangle rectCarta = new Rectangle(
                    carta.getWorldX(),
                    carta.getWorldY(),
                    gp.getTamanio(),
                    gp.getTamanio()
            );

            if (futuro.intersects(rectCarta)) {
                entidad.setColisionOn(true);
                return;
            }
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