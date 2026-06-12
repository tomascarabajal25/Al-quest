package modelosVista;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import javax.swing.JPanel;

import modelos.Jugador;
import modelos.Minijuego;
import utils.ValidacionesUtiles;

public class Vista extends JPanel implements Runnable{
    //INTERFACES ----------------------------------------------------------------------------------------------
    //ENUMERADOS ----------------------------------------------------------------------------------------------
    //CONSTANTES ----------------------------------------------------------------------------------------------
    //ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
    //ATRIBUTOS -----------------------------------------------------------------------------------------------
    private static final long serialVersionUID = 1L;

    //fps
    int FPS=60;

    //configuracion screen
    private final int tamanioOriginal=16; //16x16 pixeles
    private final int escala=3;

    private final int tamanio = tamanioOriginal * escala;
    private final int columnas = 16;
    private final int filas = 12;
    private final int anchoDePantalla= tamanio * columnas; //768pix
    private int largoDePantalla= tamanio*filas; //576pix
    private JugadorVista jugadorVista = null;

    //configuracion del mundo
    private final int columnasDelMundo=50;
    private final int filasDelMundo=50;
    protected ManejadorDeConstruccion construccionesM = null;
    protected KeyHandler keyhandler = null;
    private Thread hiloDelJuego = null;
    private AdministradorDeObjetos adminObjt = null;
    private ChequeadorDeColision chequeadorDeColision = null;
    private Vector<ObjetoVista> objetos = null;
    public Minijuego miniJuego = null;
    //ATRIBUTOS TRANSITORIOS ----------------------------------------------------------------------------------
    //CONSTRUCTORES -------------------------------------------------------------------------------------------
    /**
     * Constructor del TDA Vista sin keyhandler
     *
     * PRE:
     * -Jugador y rutaSprites no deben ser nulos
     * -SpawnCol y spawnFil deben ser mayores o iguales a cero
     *
     * @param rutaMundo:
     * @param jugador:
     * @param spawnCol:
     * @param spawnFil:
     * @param rutaSprites:
     */
    public Vista(String rutaMundo, Jugador jugador, int spawnCol, int spawnFil, String rutaSprites) {
        ValidacionesUtiles.esDistintoDeNull(rutaMundo, "rutaMundo");
        ValidacionesUtiles.esDistintoDeNull(jugador, "jugador");
        ValidacionesUtiles.esDistintoDeNull(rutaSprites, "rutaSprites");
        ValidacionesUtiles.validarMayorOIgualACero(spawnFil, "spawnFil");
        ValidacionesUtiles.validarMayorOIgualACero(spawnCol, "spawnCol");

        this.construccionesM = new ManejadorDeConstruccion(this);
        this.keyhandler = new KeyHandler();
        this.adminObjt = new AdministradorDeObjetos(this);
        this.chequeadorDeColision = new ChequeadorDeColision(this);
        this.objetos = new Vector<ObjetoVista>();

        this.setPreferredSize(new Dimension(anchoDePantalla, largoDePantalla));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyhandler);
        this.setFocusable(true);

        setJugadorVista(jugador, this.keyhandler, spawnCol, spawnFil, rutaSprites);
        this.construccionesM.loadMap(rutaMundo);

        setUpJuego();
    }

    /**
     * Constructor del TDA Vista con keyhandler
     *
     * PRE:
     * -Jugador, key y rutaSprites no deben ser nulos
     * -SpawnCol y spawnFil deben ser mayores o iguales a cero
     *
     * @param rutaMundo:
     * @param jugador:
     * @param spawnCol:
     * @param spawnFil:
     * @param rutaSprites:
     * @param key:
     */
    public Vista(String rutaMundo, Jugador jugador, int spawnCol, int spawnFil, String rutaSprites, KeyHandler key) {
        ValidacionesUtiles.esDistintoDeNull(rutaMundo, "rutaMundo");
        ValidacionesUtiles.esDistintoDeNull(jugador, "jugador");
        ValidacionesUtiles.esDistintoDeNull(rutaSprites, "rutaSprites");
        ValidacionesUtiles.validarMayorOIgualACero(spawnFil, "spawnFil");
        ValidacionesUtiles.validarMayorOIgualACero(spawnCol, "spawnCol");
        ValidacionesUtiles.esDistintoDeNull(key, "key");


        this.construccionesM = new ManejadorDeConstruccion(this);
        this.adminObjt = new AdministradorDeObjetos(this);
        this.chequeadorDeColision = new ChequeadorDeColision(this);
        this.objetos = new Vector<ObjetoVista>();

        setKey(key);

        this.setPreferredSize(new Dimension(anchoDePantalla, largoDePantalla));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyhandler);   // ← usa el key inyectado
        this.setFocusable(true);
        setJugadorVista(jugador, key, spawnCol, spawnFil, rutaSprites);
        this.construccionesM.loadMap(rutaMundo);

        setUpJuego();
    }
    //METODOS ABSTRACTOS --------------------------------------------------------------------------------------
    //METODOS HEREDADOS (CLASE)--------------------------------------------------------------------------------
    //METODOS HEREDADOS (INTERFACE)----------------------------------------------------------------------------
    //METODOS DE CLASE ----------------------------------------------------------------------------------------
    //METODOS GENERALES ---------------------------------------------------------------------------------------
    //METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

    /**
     * Setea el juego
     */
    public void setUpJuego() {
        this.adminObjt.setObjetos();
    }

    /**
     * Agrega objeto a la vista
     *
     * PRE:
     * -Objeto no debe ser nulo
     *
     * @param objeto
     */
    public void agregarObjeto(ObjetoVista objeto) {
        ValidacionesUtiles.esDistintoDeNull(objeto, "objeto");
        this.objetos.add(objeto);
    }

    /**
     * Comienza el hilo del juego
     */
    public void startGameThread() {
        this.hiloDelJuego= new Thread(this);
        this.hiloDelJuego.start();
    }


    /**
     * Ejecuta la vista del juego
     */
    @Override
    public void run() {
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while(hiloDelJuego != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if(delta >= 1) {
                actualizar();
                repaint();
                delta--;
            }
        }
    }

    /**
     * Actualiza la vista
     */
    public void actualizar() {
        if (miniJuego != null) {
            miniJuego.actualizar(jugadorVista);
        }
        jugadorVista.actualizar();

    }

    /**
     * Pinta el componente con g
     *
     * PRE:
     * -G no debe ser null
     *
     * @param g:
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;
        construccionesM.draw(g2);
        for(ObjetoVista objeto:objetos) {
            objeto.draw(g2, this);
        }

        jugadorVista.draw(g2);
        if (miniJuego != null) {
            miniJuego.draw(g2, jugadorVista);
        }

        
    }

    /**
     * Detiene el hilo
     */
    public void detenerHilo() {
        hiloDelJuego = null; // el while(hiloDelJuego != null) termina solo
    }

    /**
     * Cambiar valor de minijuego
     *
     * PRE
     * -Minijuego
     *
     * @param minijuego: Minijuego a guardar
     */
    public void establecerMinijuego(Minijuego minijuego){
        ValidacionesUtiles.esDistintoDeNull(minijuego, "minijuego");
        setMinijuego(minijuego);
    }


    //Para manejo de skins
    /**
     * Cambia la skin del jugador en vivo delegando a jugadorVista
     * 
     * pre: rutaSprites distinto de null, los 8 bmp tienen que existir en el classpath
     *                                    patron: {ruta}_{direccion}_{num}.bmp
     * post: el jugador renderiza con los nuevos sprites (en el proximo frame)
     * 
     * @param rutaSprites ruta de la nueva skin
     */
    public void cambiarSkinJugador(String rutaSprites) {
        ValidacionesUtiles.esDistintoDeNull(rutaSprites, "rutaSprites");
        jugadorVista.cambiarSkin(rutaSprites);
    }

    /**
     * Permite redibujar el mapa del juego
     *
     * PRE:
     * -RutaMundo no debe ser nulo
     * POST:
     * -Redibuja el mapa de la vista del juego
     *
     * @param rutaMundo: nuevo mapa
     */
    public void cargarMapa(String rutaMundo) {
        ValidacionesUtiles.esDistintoDeNull(rutaMundo, "rutaMundo");

        this.objetos.clear();          // limpia objetos viejos
        this.construccionesM.loadMap(rutaMundo);

        this.adminObjt.setObjetos();   // reinyecta objetos del mapa

        repaint();
    }


    //METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------
    //GETTERS REDEFINIDOS -------------------------------------------------------------------------------------
    //GETTERS INICIALIZADOS -----------------------------------------------------------------------------------
    //GETTERS COMPLEJOS ---------------------------------------------------------------------------------------
    //GETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * Getter del atributo anchoDePantalla
     * @return: Devuelve el valor del atributo
     */
    public int getAnchoDePantalla() {
        return this.anchoDePantalla;
    }

    /**
     * Getter del atributo largoDePantalla
     * @return: Devuelve el valor del atributo
     */
    public int getLargoDePantalla() {
        return this.largoDePantalla;
    }

    /**
     * Getter del atributo tamanio
     * @return: Devuelve el valor del atributo
     */
    public int getTamanio(){
        return this.tamanio;
    }

    /**
     * Getter del atributo jugadorVista
     * @return: Devuelve la vista del jugador guardada en el atributo
     */
    public JugadorVista getJugadorVista() {
        return this.jugadorVista;
    }

    /**
     * Getter del atributo chequeadorDeColision
     * @return: Devuelve el valor del atributo
     */
    public ChequeadorDeColision getChequeadorDeColision() {
        return this.chequeadorDeColision;
    }

    /**
     * Getter del atributo columnasDelMundo
     * @return: valor del atributo
     */
    public int getColumnasDelMundo() {
        return this.columnasDelMundo;
    }

    /**
     * Getter del atributo filasDelMundo
     * @return: valor del atributo
     */
    public int getFilasDelMundo() {
        return this.filasDelMundo;
    }

    /**
     * Getter del atributo adminObjt
     * @return: Devuelve el valor del atributo
     */
    public AdministradorDeObjetos getAdminObjt(){
        return this.adminObjt;
    }

    /**
     * Getter del atributo minijuegos
     * @return: Devuelve del valor del atributo
     */
    public Minijuego getMinijuego(){
        return this.miniJuego;
    }
    //SETTERS COMPLEJOS----------------------------------------------------------------------------------------
    //SETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * Setter del atributo key
     *
     * PRE:
     * -Key no debe ser nulo
     *
     * @param key: Manejador de teclas
     */
    private void setKey(KeyHandler key) {
        ValidacionesUtiles.esDistintoDeNull(key, "key");
        this.keyhandler = key;
    }

    /**
     * Setter del atributo jugadorVista
     *
     * PRE:
     * -Jugador, key y rutaSprites no deben ser nulos
     * -SpawnCol y spawnFil deben ser mayores o iguales a cero
     *
     * @param jugador:
     * @param key:
     * @param spawnCol:
     * @param spawnFil:
     * @param rutaSprites:
     */
    private void setJugadorVista(Jugador jugador, KeyHandler key, int spawnCol, int spawnFil, String rutaSprites){
        ValidacionesUtiles.esDistintoDeNull(jugador, "jugador");
        ValidacionesUtiles.esDistintoDeNull(key, "key");
        ValidacionesUtiles.validarMayorOIgualACero(spawnFil, "spawnFil");
        ValidacionesUtiles.validarMayorOIgualACero(spawnCol, "spawnCol");
        ValidacionesUtiles.esDistintoDeNull(rutaSprites, "rutaSprites");

        this.jugadorVista = new JugadorVista(jugador, key, spawnCol, spawnFil, rutaSprites, this);;
    }

    /**
     * Setter del atributo miniJuego
     *
     * PRE:
     * -MiniJuego no debe ser nulo
     *
     * @param minijuego:
     */
    private void setMinijuego(Minijuego minijuego) {
        ValidacionesUtiles.esDistintoDeNull(minijuego, "minijuego");
        this.miniJuego = minijuego;
    }


	
	



		

	



}
