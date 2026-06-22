package modelosVista;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import javax.imageio.ImageIO;
import java.util.List;

import modelos.Jugador;
import utils.ValidacionesUtiles;

public class JugadorVista extends EntidadVista {
    //INTERFACES ----------------------------------------------------------------------------------------------
    //ENUMERADOS ----------------------------------------------------------------------------------------------
    
    //CONSTANTES ----------------------------------------------------------------------------------------------
    
    //Meditacion
    
    //Ubicacion classpath de la carpeta donde estan los sets de meditacion
    private static final String CARPETA_MEDITACIONES = "/assets/meditaciones/";

    //Cantidad de frames (total de pngs) que copmletan la vuelta completa de la meditacion
    private static final int MEDITACION_CANT_FRAMES = 10;
    //Ver Meditacion.java para entender mas como funcionan las meditaciones, las cambie a un enum
    
    //ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
    //ATRIBUTOS -----------------------------------------------------------------------------------------------
    private Vista vistaDelJuego = null;
    private KeyHandler keyHa = null;
    private Jugador jugador = null;
    private List<ElementoVista> cartas = null;
    private final int screenX;
    private final int screenY;
    private int nivelActual;
    private String sonidoPaso= juego.configuracion.ConstantesSonido.PASO1;

    //Estado de la meditacion
    /**
     * ruta base de los sprites actualmente cargados.
     * esta parte se usa para derivar de que personaje se carga el set de meditacion
     */
    private String rutaSpritesActual = null;

    /**
     * mientras el jugador esta meditando es true, de esa manera no se mueve y se ve el aura
     */
    private boolean meditando = false;

    /**
     * configuracion de la meditacion del personaje equipado (puntos, velocidad y tamaño)
     * sera null si el personaje actual no tiene meditacion asociada.
     */
    private Meditacion meditacionActual = null;

    /**
     * Imagenes del aura de meditacion del personaje equipado (tambien puede ser vacio)
     */
    private List<BufferedImage> imagenesMeditacion = new ArrayList<>();

    /**
     * ruta base del set de meditacion ya cargado en memoria (cacheada, para no releerlo todo el tiempo)
     */
    private String rutaMeditacionCargada = null;

    /** 
     * indice de la imagen del aura que se esta mostrando
     */
    private int frameMeditacion = 0;

    /** 
     * el contador de frames del juego para avanzar con los pngs del aura
     */
    private int contadorMeditacion = 0;


    //ATRIBUTOS TRANSITORIOS ----------------------------------------------------------------------------------
    //CONSTRUCTORES -------------------------------------------------------------------------------------------
    public JugadorVista(Jugador jugador, KeyHandler key, int spawnCol, int spawnFila, String rutaSprites,Vista vista){
        super(jugador.getNombre());

        ValidacionesUtiles.esDistintoDeNull(jugador, "jugador");
        ValidacionesUtiles.esDistintoDeNull(key, "key");
        ValidacionesUtiles.esDistintoDeNull(vista, "vista");
        ValidacionesUtiles.esDistintoDeNull(rutaSprites, "rutaSprites");

        setJugador(jugador);
        setVista(vista);
        setKey(key);

        int[] pantalla = inicializarColeccionesYPosicion(spawnCol, spawnFila);
        this.screenX = pantalla[0];
        this.screenY = pantalla[1];

        setAreaSolida(new Rectangle(8, 16, 32, 32));
        setDireccion(Direccion.ABAJO);

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
        //Agrego esto para que el jugador se quede quieto mientras medita y no se mueva
        if (this.meditando) {
            actualizarAnimacionMeditacion();
            return;
        }

        manejarMovimientoYAnimacion();
    }

    // METODOS PRIVADOS AUXILIARES -----
    /**
     * Inicializa colecciones y posiciones del jugador en pantalla/world.
     */
    private int[] inicializarColeccionesYPosicion(int spawnCol, int spawnFila) {
        this.cartas = new ArrayList<>();
        int sx = vistaDelJuego.getAnchoDePantalla() / 2 - (vistaDelJuego.getTamanio() / 2);
        int sy = vistaDelJuego.getLargoDePantalla() / 2 - (vistaDelJuego.getTamanio() / 2);

        setWorldX(spawnCol * vistaDelJuego.getTamanio());
        setWorldY(spawnFila * vistaDelJuego.getTamanio());
        return new int[]{sx, sy};
    }

    /**
     * Maneja el control del jugador, la detección de colisiones y la animación
     * de los sprites (incluye reproducción de efectos de paso).
     */
    private void manejarMovimientoYAnimacion() {
        if(this.keyHa.getUpPressed() || this.keyHa.getDownPressed() || this.keyHa.getLeftPressed() || this.keyHa.getRightPressed()) {
            if(this.keyHa.getUpPressed()) setDireccion(Direccion.ARRIBA);
            if(this.keyHa.getDownPressed()) setDireccion(Direccion.ABAJO);
            if(this.keyHa.getLeftPressed()) setDireccion(Direccion.IZQUIERDA);
            if(this.keyHa.getRightPressed()) setDireccion(Direccion.DERECHA);

            setColisionOn(false);
            this.vistaDelJuego.getChequeadorDeColision().chequearConstruccion(this);
            this.vistaDelJuego.getChequeadorDeColision().chequearCartas(this, this.cartas, this.nivelActual);

            if(!isColisionOn()) {
                switch (getDireccion()) {
                    case ARRIBA:
                        setWorldY(getWorldY() - getVelocidad());
                        break;
                    case ABAJO:
                        setWorldY(getWorldY() + getVelocidad());
                        break;
                    case IZQUIERDA:
                        setWorldX(getWorldX() - getVelocidad());
                        break;
                    case DERECHA:
                        setWorldX(getWorldX() + getVelocidad());
                        break;
                }

                actualizarSpriteCounter();
            }
        }
    }
    public void actualizarSpriteCounter() {
    	setSpriteCounter(getSpriteCounter() + 1);
        if(getSpriteCounter() > 12) {
            if(getSpriteNum() == 1) {
                setSpriteNum(2);
                // sonido del paso
                this.vistaDelJuego.playEfecto(sonidoPaso);
            } else {
                setSpriteNum(1);
                // cambio el sonido del paso
                sonidoPaso = sonidoPaso.equals(juego.configuracion.ConstantesSonido.PASO1)
                        ? juego.configuracion.ConstantesSonido.PASO2
                        : juego.configuracion.ConstantesSonido.PASO1;
            }
            setSpriteCounter(0);
 	
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

        //Nuevo para meditacion:
        //se recuerda la skin activa para ubicar su set de meditacion despues
        this.rutaSpritesActual = ruta;
        

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

    //Gestion de SKINS
    /**
     * cambia la skin del jugador en el momento, recarga los 8 bmp desde otra ruta
     * 
     * PRE: rutaSprites distinto de null, los archivos deben existir como los busca
     *                                    el programa en el classpath
     * POST: las imagenes del jugador se actualizan, cambio en el proximo frame,
     *       con esto no es necesario reiniciar el hilo
     * 
     * @param rutaSprites ruta base de la nueva skin
     */
    public void cambiarSkin(String rutaSprites) {
        ValidacionesUtiles.esDistintoDeNull(rutaSprites, "rutaSprites");
        getImagenesDelJugador(rutaSprites);
    }

    //Gestion de MEDITACION
    /** 
     * Poner al jugador en estado de meditacion
     * 
     * resuelve la meditacion del personaje consultando el catalogo en el TDA Meditacion, se
     * fija en base al nombre base de la skin. Por eso, la parte de meditacion de este archivo,
     * verla en simultaneo con el otro TDA.
     * 
     * De el catalogo con el nombre base, obtiene el resto de parametros
     * Si el personaje no figura en el catalogo, o figura pero no tiene imagenes, significa
     * que no sabe meditar ese personaje, devuelve false (y VistaGlobal le muestra al usuario el cartel)
     * 
     * POST: si el personaje tiene meditacion, se pone a meditar y se bloquea el movimiento hasta que se llame
     *       a detenerMeditacion, guarda su config en meditacionActual y devuelve true.
     *       si no tiene meditacion asociada, no cambia nada y devuelve false.
     * 
     * @return true si efectivamente empezo a meditar
     */
    public boolean iniciarMeditacion() {
        String base = baseDeRuta(this.rutaSpritesActual);
        Meditacion med = Meditacion.porPersonaje(base);
        
        if (med == null) {
            return false; //ya que no tiene meditacion ese personaje
        }

        String rutaBase = CARPETA_MEDITACIONES + med.getPrefijoImagenes();
        //para gestion de cache, solo va a releer del disco si cambia el set.
        if (!rutaBase.equals(this.rutaMeditacionCargada)){
            cargarImagenesMeditacion(rutaBase);
        }

        if (this.imagenesMeditacion.isEmpty()) {
            return false; //esto es por si figura en el catalogo pero faltan las imagenes, igual evitemos que pase eso.
        }

        this.meditacionActual = med;
        this.meditando = true;
        this.frameMeditacion = 0;
        this.contadorMeditacion = 0;
        //Para que mire de frente quieto mientras medita, mas elegante
        setDireccion(Direccion.ABAJO);
        setSpriteNum(1);
        return true;
    }

    /** 
     * @return puntaje por segundo de la meditacion activa, 0 si no esta meditando o no tiene.
     * 
     * Esta parte la usa VistaGlobal para darle el puntaje por segundo meditado y mostrarlo en la consola
     */
    public int getPuntosPorSegundoMeditacion(){
        return (this.meditacionActual !=null) ? this.meditacionActual.getPuntosPorSegundo() : 0;
    }

    /** 
     * saca al jugador del estado de meditacion y vuelve a habilitarse para que se mueva
     * POST: meditando = false
     * 
     * el puntaje lo maneja VistaGlobal
     */
    public void detenerMeditacion(){
        this.meditando = false;
    }

    /** 
     * @return true si el jugador esta meditando
     */
    public boolean isMeditando(){
        return this.meditando;
    }

    /** 
     * Carga en memoria el set de meditacion del personaje
     * 
     * funciona asi con los bugs:
     * si falta una imagen del set, la saltea y sigue con las que estan, no crashea.
     * Si no existe ninguna imagen del set, la lista queda vacia y el personaje no sabe meditar.
     * 
     * PRE: rutaBase no puede ser null (o sea, en la carpeta de meditaciones tiene que estar el aura llamada)
     * POST: imagenesMeditacion contiene las imagenes encontradas y 
     *      rutaMeditacionCargada queda seteada en rutaBase (cacheado)
     * 
     * @param rutaBase ruta base del set, sin el "_numero.png", tipo solo toma la primer palabra
     */
    private void cargarImagenesMeditacion(String rutaBase) {
        ValidacionesUtiles.esDistintoDeNull(rutaBase, "rutaBase");

        this.imagenesMeditacion =new ArrayList<>();
        this.rutaMeditacionCargada = rutaBase;

        for (int i = 1; i <= MEDITACION_CANT_FRAMES; i++){
            String ruta = rutaBase + "_" + i + ".png";

            try{
                java.io.InputStream is = getClass().getResourceAsStream(ruta);
                if (is != null){
                    this.imagenesMeditacion.add(ImageIO.read(is));
                }
            } catch (IOException e) {
                //Si la imagen es ilegible, se saltea y no corta la carga del set
                e.printStackTrace();
            }
        }
    }

    /** 
     * Avanza la animacion del aura cada cierta cantidad de frames del juego, pasando al sig png.
     * Esta en loop, si llega a la ultima, vuelve a la primera
     */
    private void actualizarAnimacionMeditacion(){
        if (this.imagenesMeditacion.isEmpty() || this.meditacionActual == null){
            return;
        }

        this.contadorMeditacion++;

        if (this.contadorMeditacion >= this.meditacionActual.getVelocidad()){
            this.contadorMeditacion = 0;
            this.frameMeditacion = (this.frameMeditacion + 1) % this.imagenesMeditacion.size();
        }
    }

    /** 
     * devuelve el nombre base de la skin a partir de la ruta de sprites (la carpeta jugador)
     * si es null la ruta, devuelve null
     */
    private String baseDeRuta(String ruta){
        if (ruta == null){
            return null;
        }
        int corte = ruta.lastIndexOf('/');
        return (corte >=0) ? ruta.substring(corte+1) : ruta;
    }

    /** 
     * dibuja el aura de meditacion sobre el jugador
     * se llama desde draw() despues de dibujar el sprite, para que el aura quede por arriba
     * del personaje y ya no quede mas abajo
     * 
     * los parametros, salen del catalogo de Meditacion, en el eje X siempre esta centrada en el pj
     * pero en el eje Y, desde el catalogo se modifican los valores y se va arreglando, es a ojo
     * 
     * PRE: g2 != null
     */
    private void dibujarAura(Graphics2D g2) {
        if (this.imagenesMeditacion.isEmpty() || this.meditacionActual == null){
            return;
        }
        BufferedImage aura = this.imagenesMeditacion.get(this.frameMeditacion);

        int ancho = this.meditacionActual.getAncho();
        int alto = this.meditacionActual.getAlto();
        int offsetY = this.meditacionActual.getOffsetY();

        //esto es el centrado en el eje X sobre el jugador, como dije antes, el Y desde offsetY
        int centroX = screenX + vistaDelJuego.getTamanio() /2;
        int centroY = screenY + vistaDelJuego.getTamanio() /2;
        int ax = centroX - ancho / 2;
        int ay = centroY - alto / 2 - offsetY;

        g2.drawImage(aura, ax, ay, ancho, alto, null);


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

        //Meditacion:
        //Si esta meditando, el aura se dibuja por arriba del personaje y no por abajo
        if (this.meditando) {
            dibujarAura(g2);
        }

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
