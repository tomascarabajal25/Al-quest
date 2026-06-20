package modelosVista;

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
    // Estado para efectos de proximidad al agua
    private boolean cercaAgua = false;
    private long ultimoSonidoAgua = 0L;
    private static final long AGUA_COOLDOWN_MS = 5000; // 3 segundos entre reproducciones
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

    /**
     * Comprueba si la entidad está cerca de un tile de agua y dispara un efecto sonoro.
     * Reproduce el sonido una vez al entrar en proximidad y respeta un cooldown para no spamear.
     *
     * PRE: entidad no debe ser nulo
     */
    public void chequearProximidadAgua(EntidadVista entidad) {
        ValidacionesUtiles.esDistintoDeNull(entidad, "entidad");

        int worldX = entidad.getWorldX();
        int worldY = entidad.getWorldY();

        int tileCol = worldX / this.gp.getTamanio();
        int tileRow = worldY / this.gp.getTamanio();

        boolean encontrada = false;

        // comprobamos un radio de 1 tile alrededor de la entidad
        for (int c = tileCol - 1; c <= tileCol + 1 && !encontrada; c++) {
            for (int r = tileRow - 1; r <= tileRow + 1; r++) {
                if (c < 0 || r < 0 || c >= this.gp.getColumnasDelMundo() || r >= this.gp.getFilasDelMundo()) continue;
                int tileNum = this.gp.construccionesM.mapaDeConstruccionesNum[c][r];
                // En este proyecto el tile de agua se cargó como índice 2 en ManejadorDeConstruccion
                if (tileNum == 2) {
                    encontrada = true;
                    break;
                }
            }
        }

        long ahora = System.currentTimeMillis();

        if (encontrada) {
            if (!cercaAgua || (ahora - ultimoSonidoAgua) > AGUA_COOLDOWN_MS) {
                try {
                    this.gp.playEfecto(juego.configuracion.ConstantesSonido.AGUA);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ultimoSonidoAgua = ahora;
            }
            cercaAgua = true;
        } else {
            cercaAgua = false;
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