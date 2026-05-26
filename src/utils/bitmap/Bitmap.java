package utils.bitmap;

import utils.ValidacionesUtiles;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Bitmap {
//INTERFACES ----------------------------------------------------------------------------------------------
//ENUMERADOS ----------------------------------------------------------------------------------------------
//CONSTANTES ----------------------------------------------------------------------------------------------
//ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
//ATRIBUTOS -----------------------------------------------------------------------------------------------
	
	private int width;
    private int height;
    private BufferedImage image;
    private double distanceToCamera = 200;
    private javax.swing.JFrame ventana;
    private javax.swing.JLabel etiquetaImagen;
    
//ATRIBUTOS TRANSITORIOS ----------------------------------------------------------------------------------
//CONSTRUCTORES -------------------------------------------------------------------------------------------
    
    /**
     * Genera un bitmap de ancho y alto dados
     * @param width ancho en pixeles mayor a 0
     * @param height alto en pixeles mayor a 0
     */
    public Bitmap(int width, int height) {
    	ValidacionesUtiles.validarMayorACero(width, "ancho");
    	ValidacionesUtiles.validarMayorACero(height, "alto");
        this.width = width;
        this.height = height;
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }
    
//METODOS ABSTRACTOS --------------------------------------------------------------------------------------
//METODOS HEREDADOS (CLASE)--------------------------------------------------------------------------------
//METODOS HEREDADOS (INTERFACE)----------------------------------------------------------------------------
//METODOS DE CLASE ----------------------------------------------------------------------------------------
//METODOS GENERALES ---------------------------------------------------------------------------------------
//METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------
    
    /**
     * Dibuja un pixel de un color determinado
     * @param x: pixel entre 1 y ancho
     * @param y: pixel entre 1 y alto
     * @param color: no nulo
     */
    public void drawPixel(int x, int y, Color color) {
    	ValidacionesUtiles.validarRangoNumerico(x, 0, getWidth(), "ancho");
    	ValidacionesUtiles.validarRangoNumerico(y, 0, getHeight(), "alto");
    	ValidacionesUtiles.esDistintoDeNull(color, "color");
        if (x >= 0 && x < width && y >= 0 && y < height) {
            image.setRGB(x, y, color.getRGB());
        }
    }

    /**
     * Dibuja una linea del color dado
     * @param x1: pixel entre 1 y ancho (origen)
     * @param y1: pixel entre 1 y alto (origen)
     * @param x2: pixel entre 1 y ancho (destino)
     * @param y2: pixel entre 1 y alto (destino)
     * @param color: no nulo
     */
    public void drawLine(int x1, int y1, int x2, int y2, Color color) {
    	ValidacionesUtiles.validarRangoNumerico(x1, 0, getWidth(), "x1");
    	ValidacionesUtiles.validarRangoNumerico(x2, 0, getWidth(), "x2");
    	ValidacionesUtiles.validarRangoNumerico(y1, 0, getHeight(), "y1");
    	ValidacionesUtiles.validarRangoNumerico(y2, 0, getHeight(), "y2");
    	ValidacionesUtiles.esDistintoDeNull(color, "color");
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;
        int err = dx - dy;

        while (true) {
            drawPixel(x1, y1, color);
            if (x1 == x2 && y1 == y2) break;
            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x1 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y1 += sy;
            }
        }
    }

    /**
     * Dibuja un rectangulo del color dado
     * @param x1: pixel entre 1 y ancho (origen)
     * @param y1: pixel entre 1 y alto (origen)
     * @param x2: pixel entre 1 y ancho (destino)
     * @param y2: pixel entre 1 y alto (destino)
     * @param color: no nulo
     */    
    public void drawRectangle(int x, int y, int width, int height, Color color) {
    	ValidacionesUtiles.validarRangoNumerico(x, 0, getWidth(), "x1");
    	ValidacionesUtiles.validarRangoNumerico(y, 0, getHeight(), "1");
    	ValidacionesUtiles.validarRangoNumerico(width, 0, getWidth(), "ancho");
    	ValidacionesUtiles.validarRangoNumerico(height, 0, getHeight(), "alto");
    	ValidacionesUtiles.esDistintoDeNull(color, "color");
        drawLine(x, y, x + width, y, color);
        drawLine(x, y, x, y + height, color);
        drawLine(x + width, y, x + width, y + height, color);
        drawLine(x, y + height, x + width, y + height, color);
    }

    /**
     * Dibuja un circulo del color dado
     * @param centerX: pixel entre 1 y ancho (origen)
     * @param centerY: pixel entre 1 y alto (origen)
     * @param radius: mayor a cero
     * @param color
     */
    public void drawCircle(int centerX, int centerY, int radius, Color color) {
    	ValidacionesUtiles.validarRangoNumerico(centerX, 0, getWidth(), "x1");
    	ValidacionesUtiles.validarRangoNumerico(centerY, 0, getHeight(), "y1");
    	ValidacionesUtiles.validarRangoNumerico(radius, 0, Math.min(getWidth(), getHeight()), "ancho");
    	ValidacionesUtiles.esDistintoDeNull(color, "color");
        int x = radius;
        int y = 0;
        int err = 0;

        while (x >= y) {
            drawPixel(centerX + x, centerY + y, color);
            drawPixel(centerX + y, centerY + x, color);
            drawPixel(centerX - y, centerY + x, color);
            drawPixel(centerX - x, centerY + y, color);
            drawPixel(centerX - x, centerY - y, color);
            drawPixel(centerX - y, centerY - x, color);
            drawPixel(centerX + y, centerY - x, color);
            drawPixel(centerX + x, centerY - y, color);

            y++;
            if (err <= 0) {
                err += 2 * y + 1;
            }
            if (err > 0) {
                x--;
                err -= 2 * x + 1;
            }
        }
    }

    /**
     * Rellena el color de fondo
     * @param color
     */
    public void rellenar(Color color) {
    	for( int i = 0; i < this.getWidth(); i++) {
    		for(int j = 0; j < this.getHeight(); j++) {
    			this.drawPixel(i, j, color);
    		}
    	}
    }
    
    /**
     * Dibuja un tablero dentro del bitmap
     * @param x
     * @param y
     * @param casillas
     * @param ancho
     */
    public void dibujarTablero(int x, int y, int casillas, int ancho, Color color) {
    	for( int fila = 0; fila < casillas; fila++) {
    		for(int columna = 0; columna < casillas; columna++) {
    			drawRectangle(x + columna * ancho, 
    					      y + fila * ancho, 
    					      ancho, 
    					      ancho, 
    					      color);
    		}
    	}
    }
    
    /**
     * Dibuja un texto en la posicion dada
     * @param text: texto a dibujar
     * @param x: pixel entre 1 y ancho (origen)
     * @param y: pixel entre 1 y alto (origen)
     * @param font: fuente a utilizar, no nula
     * @param color: colo no nulo
     */
    public void drawText(String text, int x, int y, Font font, Color color, Color background) {
    	ValidacionesUtiles.validarRangoNumerico(x, 0, getWidth(), "x");
    	ValidacionesUtiles.validarRangoNumerico(y, 0, getHeight(), "y");
    	ValidacionesUtiles.esDistintoDeNull(font, "fuente");
    	ValidacionesUtiles.esDistintoDeNull(color, "color");
        Graphics2D g = image.createGraphics();

        if (background != null) {
        	g.setColor(background);
        }
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        int textAscent = fm.getAscent();
        int paddingSuperior = 5;
        int paddingInferior = 10;
        g.fillRect(x-paddingInferior, y - textAscent - paddingInferior, textWidth + (paddingSuperior*4), textHeight + (paddingSuperior*2));
        
        g.setFont(font);
        g.setColor(color);
        g.drawString(text, x, y);
        g.dispose();
    }

    public void pasteBitmap(Bitmap other, int x, int y) {
        Graphics2D g = image.createGraphics();
        g.drawImage(other.image, x, y, null);
        g.dispose();
    }

    public String saveToFile(String path) throws IOException {
    	File output = new File(path);
        ImageIO.write(image, "bmp", output);
        return output.getAbsolutePath();
    }

    public static Bitmap loadFromFile(String path) throws IOException {
        BufferedImage img = ImageIO.read(new File(path));
        Bitmap bmp = new Bitmap(img.getWidth(), img.getHeight());
        bmp.image = img;
        return bmp;
    }
    
    private int projectX(double x, double z) {
        return (int) (this.width / 2 + x * distanceToCamera / (z + distanceToCamera));
    }

    private int projectY(double y, double z) {
        return (int) (this.height / 2 - y * distanceToCamera / (z + distanceToCamera));
    }

    public void drawLine3D(double x1, double y1, double z1, double x2, double y2, double z2, Color color) {
        int x1p = projectX(x1, z1);
        int y1p = projectY(y1, z1);
        int x2p = projectX(x2, z2);
        int y2p = projectY(y2, z2);
        this.drawLine(x1p, y1p, x2p, y2p, color);
    }

    public void drawCube(double size, double cx, double cy, double cz, Color color) {
        double[][] vertices = new double[8][3];
        int i = 0;
        for (int dx = -1; dx <= 1; dx += 2) {
            for (int dy = -1; dy <= 1; dy += 2) {
                for (int dz = -1; dz <= 1; dz += 2) {
                    vertices[i][0] = cx + dx * size / 2;
                    vertices[i][1] = cy + dy * size / 2;
                    vertices[i][2] = cz + dz * size / 2;
                    i++;
                }
            }
        }

        int[][] edges = {
            {0, 1}, {1, 3}, {3, 2}, {2, 0},
            {4, 5}, {5, 7}, {7, 6}, {6, 4},
            {0, 4}, {1, 5}, {2, 6}, {3, 7}
        };

        for (int[] edge : edges) {
            double[] p1 = vertices[edge[0]];
            double[] p2 = vertices[edge[1]];
            drawLine3D(p1[0], p1[1], p1[2], p2[0], p2[1], p2[2], color);
        }
    }
    /**
     * Muestra el contenido actual del bitmap en una ventana.
     * Si la ventana ya existe, actualiza la imagen.
     */
    public void show() {
        if (ventana == null) {
            ventana = new javax.swing.JFrame("Visualizador de Ordenamiento");
            ventana.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
            
            // Creamos un contenedor para la imagen
            etiquetaImagen = new javax.swing.JLabel(new javax.swing.ImageIcon(this.image));
            ventana.add(etiquetaImagen);
            
            ventana.pack(); // Ajusta la ventana al tamaño de la imagen
            ventana.setLocationRelativeTo(null); // Centra en pantalla
            ventana.setVisible(true);
        } else {
            // Si ya existe, actualizamos la imagen y repintamos
            etiquetaImagen.setIcon(new javax.swing.ImageIcon(this.image));
            etiquetaImagen.repaint();
        }
    }
    
//METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------	
//GETTERS REDEFINIDOS -------------------------------------------------------------------------------------
//GETTERS INICIALIZADOS -----------------------------------------------------------------------------------
//GETTERS COMPLEJOS ---------------------------------------------------------------------------------------
//GETTERS SIMPLES -----------------------------------------------------------------------------------------
    
    /**
     * Devuelve la imagen en memoria
     * @return
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Devuelve el ancho
     * @return pixeles de ancho
     */
    public int getWidth() {
		return width;
	}
    
    /**
     * Devuelve el alto
     * @return pixeles de alto
     */
    public int getHeight() {
		return height;
	}
    
//SETTERS COMPLEJOS----------------------------------------------------------------------------------------	
//SETTERS SIMPLES -----------------------------------------------------------------------------------------
}

