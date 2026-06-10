package modelosVista;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import javax.imageio.ImageIO;
import java.util.List;

import Juego.ciudades.recoleccionEnMatriz.ui.CartaVista;
import modelos.Jugador;
import utils.ValidacionesUtiles;

public class JugadorVista extends EntidadVista {
    //INTERFACES ----------------------------------------------------------------------------------------------
    //ENUMERADOS ----------------------------------------------------------------------------------------------
    //CONSTANTES ----------------------------------------------------------------------------------------------
    //ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
    //ATRIBUTOS -----------------------------------------------------------------------------------------------
    private Vista vistaDelJuego = null;
    private KeyHandler keyHa = null;
    private Jugador jugador = null;
    private List<ElementoVista> cartas = null;
    private final int screenX;
    private final int screenY;
    private int nivelActual;

    //ATRIBUTOS TRANSITORIOS ----------------------------------------------------------------------------------
    //CONSTRUCTORES -------------------------------------------------------------------------------------------
    public JugadorVista(Jugador jugador, KeyHandler key, int spawnCol, int spawnFila, String rutaSprites,Vista vista){
        super(jugador.getNombre());

        ValidacionesUtiles.esDistintoDeNull(jugador, "jugador");
        ValidacionesUtiles.esDistintoDeNull(key, "key");
        ValidacionesUtiles.esDistintoDeNull(vista, "vista");
        ValidacionesUtiles.esDistintoDeNull(rutaSprites, "rutaSprites");

        setJugador(jugador) ;
        setVista(vista);
        setKey(key);

        this.cartas = new ArrayList<>();
        this.screenX=vistaDelJuego.getAnchoDePantalla()/2 -(vistaDelJuego.getTamanio()/2);
        this.screenY=vistaDelJuego.getLargoDePantalla()/2 -(vistaDelJuego.getTamanio()/2);

        setWorldX(spawnCol * vistaDelJuego.getTamanio());
        setWorldY(spawnFila * vistaDelJuego.getTamanio());

        setAreaSolida(new Rectangle(8, 16, 32, 32));
        setDireccion(Direccion.ABAJO);;

        getImagenesDelJugador(rutaSprites);
    }
    //METODOS ABSTRACTOS --------------------------------------------------------------------------------------
    //METODOS HEREDADOS (CLASE)--------------------------------------------------------------------------------
    //METODOS HEREDADOS (INTERFACE)----------------------------------------------------------------------------
    //METODOS DE CLASE ----------------------------------------------------------------------------------------
    //METODOS GENERALES ---------------------------------------------------------------------------------------
    //METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

    /**
     * Actualiza el estado del jugador
     */
    public void actualizar() {
        if(this.keyHa.getUpPressed() || this.keyHa.getDownPressed() || this.keyHa.getLeftPressed() || this.keyHa.getRightPressed()) {
            if(this.keyHa.getUpPressed()) {
                setDireccion(Direccion.ARRIBA);
            }
            if(this.keyHa.getDownPressed()) {
                setDireccion(Direccion.ABAJO);
            }
            if(this.keyHa.getLeftPressed()) {
                setDireccion(Direccion.IZQUIERDA);
            }
            if(this.keyHa.getRightPressed()) {
                setDireccion(Direccion.DERECHA);
            }

            setColisionOn(false);
            this.vistaDelJuego.getChequeadorDeColision().chequearConstruccion(this);
            this.vistaDelJuego.getChequeadorDeColision().chequearCartas(this, this.cartas, this.nivelActual);

            System.out.println("COLISION = " + isColisionOn());
            if(!isColisionOn()) {
                System.out.println(
                        "ME MUEVO - colision=" + isColisionOn()
                );
                switch (getDireccion()) {
                    case ARRIBA: {
                        setWorldY(getWorldY()-getVelocidad());
                        break;
                    }
                    case ABAJO: {
                        setWorldY(getWorldY()+getVelocidad());
                        break;
                    }
                    case IZQUIERDA: {
                        setWorldX(getWorldX()-getVelocidad());
                        break;
                    }
                    case DERECHA: {
                        setWorldX(getWorldX()+getVelocidad());
                        break;
                    }
                }
            }

            setSpriteCounter(getSpriteCounter()+1);
            if(getSpriteCounter() >12) {
                if(getSpriteNum() == 1) {
                    setSpriteNum(2);;
                }
                else {
                    setSpriteNum(1);
                }
                setSpriteCounter(0);
            }
        }
    }

    /**
     * Carga las imagenes usadas para jugador
     *
     * PRE:
     * -Ruta no debe ser nulo
     *
     * @param ruta: Ruta de las imagenes
     */
    public void getImagenesDelJugador(String ruta) {
        ValidacionesUtiles.esDistintoDeNull(ruta, "ruta");

        try {
            setUp1(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(ruta + "_up_1.bmp"))));
            setUp2(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(ruta + "_up_2.bmp"))));
            setDown1(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(ruta + "_down_1.bmp"))));
            setDown2(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(ruta + "_down_2.bmp"))));
            setRight1(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(ruta + "_right_1.bmp"))));
            setRight2(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(ruta + "_right_2.bmp"))));
            setLeft1(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(ruta + "_left_1.bmp"))));
            setLeft2(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(ruta + "_left_2.bmp"))));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Dibuja al jugador en la UI
     *
     * PRE:
     * -G2 no debe ser nulo
     *
     * @param g2:
     */
    public void draw(Graphics2D g2) {
        ValidacionesUtiles.esDistintoDeNull(g2, "g2");

        BufferedImage image=null;
        switch (getDireccion()) {
            case Direccion.ARRIBA:
                if(getSpriteNum()==1) {
                    image=getUp1();
                }
                if(getSpriteNum() == 2) {
                    image=getUp2();
                }
                break;
            case Direccion.ABAJO:
                if(getSpriteNum()==1) {
                    image=getDown1();
                }
                if(getSpriteNum() == 2) {
                    image=getDown2();
                }
                break;
            case Direccion.IZQUIERDA:
                if(getSpriteNum()==1) {
                    image=getLeft1();
                }
                if(getSpriteNum() == 2) {
                    image=getLeft2();
                }
                break;
            case Direccion.DERECHA:
                if(getSpriteNum()==1) {
                    image=getRight1();
                }
                if(getSpriteNum() == 2) {
                    image=getRight2();
                }
                break;
        }
        g2.drawImage(image, screenX, screenY, vistaDelJuego.getTamanio(), vistaDelJuego.getTamanio(),null);
    }

    /**
     * Fuerza una colision
     */
    public void forzarColision() {
        System.out.println("FORZANDO");
        setColisionOn(true);
    }

    /**
     * Agrega una carta
     *
     * PRE:
     * -Carta no debe ser nulo
     */
    public void agregarCarta(ElementoVista carta){
        ValidacionesUtiles.esDistintoDeNull(carta, "carta");
        this.cartas.add(carta);
    }

    /**
     * NivelActual
     */
    public void establecerNivelActual(int nivel){
        ValidacionesUtiles.validarMayorACero(nivel, "nivel");
        this.nivelActual=nivel;
    }

    //METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------
    //GETTERS REDEFINIDOS -------------------------------------------------------------------------------------
    //GETTERS INICIALIZADOS -----------------------------------------------------------------------------------
    //GETTERS COMPLEJOS ---------------------------------------------------------------------------------------
    //GETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * Getter del atributo vistaDelJuego
     * @return: Devuelve la vista guardada en vistaDelJugador
     */
    public Vista getVistaDelJuego() {
        return this.vistaDelJuego;
    }

    /**
     * Getter del atributo jugador
     * @return: Devuelve el jugador
     */
    public Jugador getJugador() {
        return this.jugador;
    }

    /**
     * Getter del atributo cartas
     * @return: Devuelve la lista de cartas
     */
    public List<ElementoVista> getCartas() {
        return this.cartas;
    }

    /**
     * Getter del atributo KeyHa
     * @return: Devuelve el keyHandler guardado en keyHa
     */
    public KeyHandler getKeyHa() {
        return this.keyHa;
    }

    /**
     * Getter del atributo screenX
     * @return: Devuelve el valor del atributo
     */
    public int getScreenX() {
        return this.screenX;
    }

    /**
     * Getter del atributo screenY
     * @return: Devuelve el valor del atributo
     */
    public int getScreenY() {
        return this.screenY;
    }
    //SETTERS COMPLEJOS----------------------------------------------------------------------------------------
    //SETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * setter del atributo jugador
     *
     * PRE:
     * -Jugador no debe ser nulo
     *
     * @param jugador: jugador a guardar en el atributo
     */
    private void setJugador(Jugador jugador) {
        ValidacionesUtiles.esDistintoDeNull(jugador, "jugador");
        this.jugador=jugador;
    }

    /**
     * Setter del atributo vistaDelJuego
     *
     * PRE:
     * -Vista no debe ser nulo
     *
     * @param vista: Viasta a guardar
     */
    private void setVista(Vista vista) {
        ValidacionesUtiles.esDistintoDeNull(vista, "vista");
        this.vistaDelJuego=vista;
    }

    /**
     * Setter del atributo keyHa
     * @param key: KeyHandler
     */
    private void setKey(KeyHandler key){
        ValidacionesUtiles.esDistintoDeNull(key, "key");
        this.keyHa=key;
    }



	
	
	




}
