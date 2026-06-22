package modelosVista;

/**
 * TDA meditacion, es el catalogo de las meditaciones que tenemos.
 * 
 * Logica:
 * cada constante representa un tipo distinta de meditacion, se explica asi:
 * Set_De_Imagenes, Ritmo_De_Puntos_Por_Seg, Velocidad_De_Animacion, Medidas(ancho,
 * alto y cuando se corre verticalmente);
 * 
 * cada skin se asocia a una meditacion mediante su nombre base (el mismo
 * que aparece en la tienda). Si un personaje no esta en el catalogo que se ve
 * lineas mas abajo, ese personaje no podra meditar y se notificara que no posee
 * la habilidad.
 * 
 * Si necesitamos agregar una nueva meditacion:
 * Agregamos las 10 imagenes en la carpeta de las meditaciones
 * Y agregamos la constante con el personaje y sus parametros al catalogo
 */

public enum Meditacion {

    /**
     * Catalogo de meditaciones:
     * considerar que la velocidad son cada cuantos frames del juego corren los pngs del aura (
     * menor numero es mas rapidez)
     * El offsetY, se piensa como cuanto deberia subir el aura respecto del jugador (en pixeles)
     */

    //Formato de cada constante:
    //NOMBRE(personaje, prefijoImagenes, puntosPorSegundo, velocidad, ancho, alto, offsetY)
 
    GOKU("goku", "super", 1000, 4, 156, 299, 120), //config para aura 47
    NARUTO("naruto","especial",1000,6,190,310,115), //config para aura especial lenta
    CAPTAIN("captain","newbie",1,4,50,85,15), //config para aura newbie
    DEVCARABAJAL("devCarabajal","roja",25,4,108,230,78), //roja
    DEVMASSIN("devMassin","baja",10,3,60,105,25), //config para aura 15
    GOBLIN("goblin","negra",25,2,108,230,83), //config para aura negra
    DEVSORIA("devSoria","media",30,5,90,180,65), //config para aura 30 
    DEVBALLERIO("devBallerio","naranja",30,3,175,210,75), //config para aura naranja
    DEVHORLENT("devHorlent","azul",25,4,108,230,78), //config para aura azul
    ROMAN("roman","roja",25,4,108,230,78), //config para aura roja
    DEVPOMPONE("devPompone","especial",100,3,190,310,115); //config 2 para aura god

    /**
     * Configuraciones que hice y quedaron bien
     * "super", 100, 4, 156, 299, 120, //config para aura 47
     * "newbie",50,4,50,85,15, //config para aura newbie
     * "azul",50,4,108,230,78, //config para aura azul
     * "baja",50,3,60,105,25, //config para aura 15
     * especial,100,6,190,310,115, //config para aura god
     * media,50,5,90,180,65, //config para aura 30 
     * naranja,50,3,175,210,75, //config para aura naranja
     * negra,50,2,108,230,83, //config para aura negra
     * "roja",50,4,108,230,78, //config para aura roja
     * especial,100,3,190,310,115); config mas rapida para aura especial
     */


    //ATRIBUTOS
    private final String personaje;
    private final String prefijoImagenes;
    private final int puntosPorSegundo;
    private final int velocidad;
    private final int ancho;
    private final int alto;
    private final int offsetY;


    //CONSTRUCTOR
    /**
     * Pre: personaje y prefijoImagenes no pueden ser null; valores numericos coherentes
     *      puntosPorSegundo y velocidad deben ser > 0.
     * POST: queda construida la constante con todos sus parametros.
     */

    Meditacion(String personaje, String prefijoImagenes, int puntosPorSegundo, int velocidad,
                                                            int ancho, int alto, int offsetY) {
        this.personaje = personaje;
        this.prefijoImagenes = prefijoImagenes;
        this.puntosPorSegundo = puntosPorSegundo;
        this.velocidad = velocidad;
        this.ancho = ancho;
        this.alto = alto;
        this.offsetY = offsetY;
    }

    //Metodos
    /**
     * Devuelve la meditacion asociada al personaje por su nombre
     * 
     * Post: devuelve la Meditacion cuyo personaje coincide o null si el personaje no tiene.
     * 
     * @param personaje nombre base de la skin
     * @return la Meditacion asociada, o null si no existe
     */
    public static Meditacion porPersonaje(String personaje) {
        if (personaje==null){
            return null;
        }
        for (Meditacion m : values()) {
            if (m.personaje.equals(personaje)){
                return m;
            }
        }

        return null;
    }


    //GETTERS

    /**
     * @return nombre base del personaje que equipa la meditacion
     */
    public String getPersonaje(){
        return this.personaje;
    }

    /**
     * @return prefijo de los png del aura en la carpeta de meditaciones
     */
    public String getPrefijoImagenes() {
        return this.prefijoImagenes;
    }

    /**
     * @return puntaje que se gana por seg de la meditacion
     */
    public int getPuntosPorSegundo() {
        return this.puntosPorSegundo;
    }

    /**
     * @return cada cuantos frames del juego avanza el aura
     */
    public int getVelocidad() {
        return this.velocidad;
    }

    /**
     * @return ancho en pixeles con que se dibuja el aura (aparece en pantalla)
     */
    public int getAncho(){
        return this.ancho;
    }

    /**
     * @return alto en pixeles con que se dibuja el aura
     */
    public int getAlto(){
        return this.alto;
    }

    /**
     * @return offset en Y del aura con el jugador
     */
    public int getOffsetY(){
        return this.offsetY;
    }
    
}
