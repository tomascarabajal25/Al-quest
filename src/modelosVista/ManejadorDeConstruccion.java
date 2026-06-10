package modelosVista;

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
        setConstrucciones(new Construccion[10]);
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
            this.construcciones[0] = new Construccion();
            this.construcciones[0].setImagen(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/construcciones/grass.bmp"))));

            this.construcciones[1] = new Construccion();
            this.construcciones[1].setImagen(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/construcciones/wall.bmp")))) ;
            this.construcciones[1].setColision(true);

            this.construcciones[2] = new Construccion();
            this.construcciones[2].setImagen(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/construcciones/water.bmp"))));
            this.construcciones[2].setColision(true);

            this.construcciones[3] = new Construccion();
            this.construcciones[3].setImagen(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/construcciones/earth.bmp"))));

            this.construcciones[4] = new Construccion();
            this.construcciones[4].setImagen(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/construcciones/tree.bmp")))) ;
            this.construcciones[4].setColision(true);

            this.construcciones[5] = new Construccion();
            this.construcciones[5].setImagen(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/construcciones/sand.bmp"))));
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

            int col = 0;
            int row = 0;

            while(col < this.vistaDelJuego.getColumnasDelMundo() && row < this.vistaDelJuego.getFilasDelMundo()) {
                String line = br.readLine();
                if (line == null) break;

                while (col < this.vistaDelJuego.getColumnasDelMundo()) {
                    String[] numbers = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);

                    this.mapaDeConstruccionesNum[col][row] = num;
                    col++;
                }

                if (col == this.vistaDelJuego.getColumnasDelMundo()) {
                    col = 0;
                    row++;
                }
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
