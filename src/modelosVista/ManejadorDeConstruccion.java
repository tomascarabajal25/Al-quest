package modelosVista;

import juego.configuracion.ConstantesConstrucciones;
import utils.ValidacionesUtiles;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import javax.imageio.ImageIO;

public class ManejadorDeConstruccion {
    //INTERFACES ----------------------------------------------------------------------------------------------
    //ENUMERADOS ----------------------------------------------------------------------------------------------
    //CONSTANTES ----------------------------------------------------------------------------------------------
    // Constantes movidas a Juego configuracion: juegoconfiguracion.ConstantesConstrucciones
    //ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
    //ATRIBUTOS -----------------------------------------------------------------------------------------------
    private Vista vistaDelJuego = null;
    protected Construccion[] construcciones = null;
    protected int[][] mapaDeConstruccionesNum = null;
    //ATRIBUTOS TRANSITORIOS ----------------------------------------------------------------------------------
    //CONSTRUCTORES -------------------------------------------------------------------------------------------

    /**
     * Constructor del TDA ManejadorDeConstruccion
     *
     * PRE:
     * -Vista no debe ser nulo
     *
     * @param vista: Vista
     */
    public ManejadorDeConstruccion(Vista vista) {
        ValidacionesUtiles.esDistintoDeNull(vista, "vista");

        setVistaDelJuego(vista);
        setConstrucciones(new Construccion[ConstantesConstrucciones.NUM_TILES]);
        this.mapaDeConstruccionesNum=new int[vistaDelJuego.getColumnasDelMundo()][vistaDelJuego.getFilasDelMundo()];

        cargarImagenesDeConstrucciones();

    }
    //METODOS ABSTRACTOS --------------------------------------------------------------------------------------
    //METODOS HEREDADOS (CLASE)--------------------------------------------------------------------------------
    //METODOS HEREDADOS (INTERFACE)----------------------------------------------------------------------------
    //METODOS DE CLASE ----------------------------------------------------------------------------------------
    //METODOS GENERALES ---------------------------------------------------------------------------------------
    //METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

    /**
     * Dibuja el mapa visible de la UI
     *
     * Pre:
     * -G2 no debe ser nulo
     *
     * @param g2
     */
    public void draw(Graphics2D g2) {
        ValidacionesUtiles.esDistintoDeNull(g2, "g2");

        int columnasDelMundo = 0;
        int filasDelMundo= 0;

        while(columnasDelMundo < this.vistaDelJuego.getColumnasDelMundo() && filasDelMundo < this.vistaDelJuego.getFilasDelMundo()) {

            int tileNum = this.mapaDeConstruccionesNum[columnasDelMundo][filasDelMundo];

            int worldX=columnasDelMundo*this.vistaDelJuego.getTamanio();
            int worldY=filasDelMundo*this.vistaDelJuego.getTamanio();

            int screenX= worldX - this.vistaDelJuego.getJugadorVista().getWorldX() + this.vistaDelJuego.getJugadorVista().getScreenX();
            int screenY= worldY - this.vistaDelJuego.getJugadorVista().getWorldY() + this.vistaDelJuego.getJugadorVista().getScreenY();

            if(worldX + this.vistaDelJuego.getTamanio() > this.vistaDelJuego.getJugadorVista().getWorldX() - this.vistaDelJuego.getJugadorVista().getScreenX() &&
               worldX - this.vistaDelJuego.getTamanio() < this.vistaDelJuego.getJugadorVista().getWorldX() + this.vistaDelJuego.getJugadorVista().getScreenX() &&
               worldY + this.vistaDelJuego.getTamanio() > this.vistaDelJuego.getJugadorVista().getWorldY() - this.vistaDelJuego.getJugadorVista().getScreenY()&&
               worldY - this.vistaDelJuego.getTamanio() < this.vistaDelJuego.getJugadorVista().getWorldY() + this.vistaDelJuego.getJugadorVista().getScreenY()) {

                g2.drawImage(this.construcciones[tileNum].getImagen(), screenX, screenY, this.vistaDelJuego.getTamanio(), this.vistaDelJuego.getTamanio(), null);
            }

            columnasDelMundo++;
            if(columnasDelMundo == this.vistaDelJuego.getColumnasDelMundo()) {
                columnasDelMundo = 0;
                filasDelMundo++;
            }
        }

    }

    /**
     * Carga las imagenes asociadas a las entidades que componen al mapa
     */
    public void cargarImagenesDeConstrucciones() {
        try {
            this.construcciones[ConstantesConstrucciones.TILE_GRASS] = new Construccion();
            this.construcciones[ConstantesConstrucciones.TILE_GRASS].setImagen(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(ConstantesConstrucciones.PATH_GRASS))));

            this.construcciones[ConstantesConstrucciones.TILE_WALL] = new Construccion();
            this.construcciones[ConstantesConstrucciones.TILE_WALL].setImagen(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(ConstantesConstrucciones.PATH_WALL)))) ;
            this.construcciones[ConstantesConstrucciones.TILE_WALL].setColision(true);

            this.construcciones[ConstantesConstrucciones.TILE_WATER] = new Construccion();
            this.construcciones[ConstantesConstrucciones.TILE_WATER].setImagen(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(ConstantesConstrucciones.PATH_WATER))));
            this.construcciones[ConstantesConstrucciones.TILE_WATER].setColision(true);

            this.construcciones[ConstantesConstrucciones.TILE_EARTH] = new Construccion();
            this.construcciones[ConstantesConstrucciones.TILE_EARTH].setImagen(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(ConstantesConstrucciones.PATH_EARTH))));

            this.construcciones[ConstantesConstrucciones.TILE_TREE] = new Construccion();
            this.construcciones[ConstantesConstrucciones.TILE_TREE].setImagen(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(ConstantesConstrucciones.PATH_TREE)))) ;
            this.construcciones[ConstantesConstrucciones.TILE_TREE].setColision(true);

            this.construcciones[ConstantesConstrucciones.TILE_SAND] = new Construccion();
            this.construcciones[ConstantesConstrucciones.TILE_SAND].setImagen(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(ConstantesConstrucciones.PATH_SAND))));
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga el archivo que contiene la distribucion del mapa
     *
     * PRE:
     * -File no debe ser nulo
     *
     * @param file
     */
    public void loadMap(String file) {
        ValidacionesUtiles.esDistintoDeNull(file, file);
        try {
            InputStream is = getClass().getResourceAsStream(file);
            assert is != null;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int row = 0;

            while (row < this.vistaDelJuego.getFilasDelMundo()) {
                String line = br.readLine();
                if (line == null) break;

                // split por cualquier cantidad de espacios en blanco
                String[] tokens = line.trim().split("\\s+");

                int maxCols = Math.min(tokens.length, this.vistaDelJuego.getColumnasDelMundo());
                for (int col = 0; col < maxCols; col++) {
                    try {
                        int num = Integer.parseInt(tokens[col]);
                        // validar rango para evitar IndexOutOfBounds
                        if (num >= 0 && num < ConstantesConstrucciones.NUM_TILES) {
                            this.mapaDeConstruccionesNum[col][row] = num;
                        } else {
                            // valor inválido en el mapa: usar tile por defecto (grass)
                            this.mapaDeConstruccionesNum[col][row] = ConstantesConstrucciones.TILE_GRASS;
                        }
                    } catch (NumberFormatException e) {
                        // token no es un número: usar tile por defecto
                        this.mapaDeConstruccionesNum[col][row] = ConstantesConstrucciones.TILE_GRASS;
                    }
                }

                // si la línea tiene menos columnas que el mapa, dejamos las celdas restantes
                // con el valor por defecto (0 / TILE_GRASS)

                row++;
            }
            br.close();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    //METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------
    //GETTERS REDEFINIDOS -------------------------------------------------------------------------------------
    //GETTERS INICIALIZADOS -----------------------------------------------------------------------------------
    //GETTERS COMPLEJOS ---------------------------------------------------------------------------------------
    //GETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * Getter del atributo vistaDelJuego
     * @return: Vista guardada en el atributo
     */
    public Vista getVistaDelJuego() {
        return this.vistaDelJuego;
    }

    /**
     * Getter del atributo construcciones
     * @return: Arreglo de construcciones guardado en el atributo
     */
    public Construccion[] getConstrucciones() {
        return this.construcciones;
    }
    //SETTERS COMPLEJOS----------------------------------------------------------------------------------------
    //SETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * Setter del atributo vistaDelJuego
     *
     * PRE:
     * -VistaDelJuego no debe ser nulo
     *
     * @param vistaDelJuego: Vista a guardar en el atributo
     */
    private void setVistaDelJuego(Vista vistaDelJuego) {
        ValidacionesUtiles.esDistintoDeNull(vistaDelJuego, "vistaDelJuego");
        this.vistaDelJuego = vistaDelJuego;
    }

    /**
     * Setter del atributo constructores
     *
     * PRE:
     * -Construcciones no debe ser nulo
     *
     * @param construcciones: Arreglo de construcciones a guardar en el atributo
     */
    private void setConstrucciones(Construccion[] construcciones) {
        ValidacionesUtiles.esDistintoDeNull(construcciones, "construcciones");
        this.construcciones = construcciones;
    }
	

	
	
	

        
        




	
	
}
