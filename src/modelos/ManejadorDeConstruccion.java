package modelos;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

public class ManejadorDeConstruccion {
	private Vista vistaDelJuego;
	protected Construccion[] construcciones;
	protected int[][] mapaDeConstruccionesNum;
	
	public ManejadorDeConstruccion(Vista vista) {
		setVistaDelJuego(vista);
		setConstrucciones(new Construccion[10]);
		
		mapaDeConstruccionesNum=new int[vistaDelJuego.columnasDelMundo][vistaDelJuego.filasDelMundo];
		
		cargarImagenesDeConstrucciones();
		
	}
	
	
	
	public void draw(Graphics2D g2) {
        
        int columnasDelMundo = 0;
        int filasDelMundo= 0;

        
        while(columnasDelMundo < vistaDelJuego.columnasDelMundo && filasDelMundo < vistaDelJuego.filasDelMundo) {
            
            int tileNum = mapaDeConstruccionesNum[columnasDelMundo][filasDelMundo];
            
            int worldX=columnasDelMundo*vistaDelJuego.tamaño;
            int worldY=filasDelMundo*vistaDelJuego.tamaño;
            
            int screenX= worldX - vistaDelJuego.jugadorVista.getWorldX() +vistaDelJuego.jugadorVista.getScreenX();
            int screenY= worldY - vistaDelJuego.jugadorVista.getWorldY() +vistaDelJuego.jugadorVista.getScreenY();
            
            if(
            worldX + vistaDelJuego.tamaño > vistaDelJuego.jugadorVista.getWorldX()-vistaDelJuego.jugadorVista.getScreenX() &&
            worldX - vistaDelJuego.tamaño < vistaDelJuego.jugadorVista.getWorldX()+vistaDelJuego.jugadorVista.getScreenX() &&
            worldY + vistaDelJuego.tamaño >vistaDelJuego.jugadorVista.getWorldY()-vistaDelJuego.jugadorVista.getScreenY()&&
            worldY - vistaDelJuego.tamaño <vistaDelJuego.jugadorVista.getWorldY()+vistaDelJuego.jugadorVista.getScreenY()) {
            	
            	g2.drawImage(construcciones[tileNum].getImagen(), screenX, screenY, vistaDelJuego.tamaño, vistaDelJuego.tamaño, null);
            }
            	
            	
            columnasDelMundo++;
       
            if(columnasDelMundo == vistaDelJuego.columnasDelMundo) {
            	columnasDelMundo = 0;
            
                filasDelMundo++;
              
            }
        }
        
    }
	
	public void cargarImagenesDeConstrucciones() {
        
        try {
            
            construcciones[0] = new Construccion();
            construcciones[0].setImagen(ImageIO.read(getClass().getResourceAsStream("/assets/construcciones/grass.bmp"))); 
            
            construcciones[1] = new Construccion();
            construcciones[1].setImagen(ImageIO.read(getClass().getResourceAsStream("/assets/construcciones/wall.bmp"))) ;
            construcciones[1].setColision(true);
            
            construcciones[2] = new Construccion();
            construcciones[2].setImagen(ImageIO.read(getClass().getResourceAsStream("/assets/construcciones/water.bmp")));
            construcciones[2].setColision(true);
            
            construcciones[3] = new Construccion();
            construcciones[3].setImagen(ImageIO.read(getClass().getResourceAsStream("/assets/construcciones/earth.bmp"))); 
            
            construcciones[4] = new Construccion();
            construcciones[4].setImagen(ImageIO.read(getClass().getResourceAsStream("/assets/construcciones/tree.bmp"))) ;
            construcciones[4].setColision(true);
            
            construcciones[5] = new Construccion();
            construcciones[5].setImagen(ImageIO.read(getClass().getResourceAsStream("/assets/construcciones/sand.bmp")));
            
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String file) {
        
        try {
            InputStream is = getClass().getResourceAsStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            
            int col = 0;
            int row = 0;
            
            while(col < vistaDelJuego.columnasDelMundo && row < vistaDelJuego.filasDelMundo) {
                
                String line = br.readLine();
                if (line == null) break;

                while (col < vistaDelJuego.columnasDelMundo) {
                    String[] numbers = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    
                    mapaDeConstruccionesNum[col][row] = num;
                    col++;
                }
                
                if (col == vistaDelJuego.columnasDelMundo) {
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
        
        

	public Vista getVistaDelJuego() {
		return vistaDelJuego;
	}

	public void setVistaDelJuego(Vista vistaDelJuego) {
		this.vistaDelJuego = vistaDelJuego;
	}

	public Construccion[] getConstrucciones() {
		return construcciones;
	}
	
	

	public void setConstrucciones(Construccion[] construcciones) {
		this.construcciones = construcciones;
	}
	
	
}
