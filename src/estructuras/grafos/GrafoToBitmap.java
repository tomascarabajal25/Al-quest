package material.estructuras.grafos;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

import material.bitmap.Bitmap;
import material.bitmap.BitmapViewer;
// Importa las clases necesarias de tu proyecto (Bitmap ya está dada)

/**
 * Clase que se encarga de dibujar un Grafo en un objeto Bitmap.
 */
public class GrafoToBitmap {

    // --- CONSTANTES ---
    private static final int VERTEX_RADIUS = 15;
    private static final int PADDING = 50;
    private static final Color VERTEX_COLOR = Color.BLUE;
    private static final Color EDGE_COLOR = Color.GRAY;
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 22);

    private final int V; // Número de vértices
    private final int[][] adjMatrix; // Matriz de adyacencia (o podrías usar List<List<Integer>>)
    private final Bitmap bitmap;
    private final Map<Integer, Point2D.Double> vertexPositions;

    /**
     * Constructor.
     * @param adjMatrix Matriz de adyacencia del grafo.
     * @param width Ancho del bitmap.
     * @param height Alto del bitmap.
     */
    public GrafoToBitmap(int[][] adjMatrix, int width, int height) {
        if (adjMatrix == null || adjMatrix.length == 0) {
            throw new IllegalArgumentException("La matriz de adyacencia no puede ser nula o vacía.");
        }
        this.V = adjMatrix.length;
        this.adjMatrix = adjMatrix;
        this.bitmap = new Bitmap(width, height);
        this.vertexPositions = new HashMap<>();
        
        // Asignar posiciones geométricas a los vértices (disposición circular)
        calculateCircularPositions(width, height);
    }

    /**
     * Calcula las coordenadas (x, y) para cada vértice en un círculo.
     * Esto ayuda a dibujar el grafo de manera legible.
     * @param width Ancho total del área de dibujo.
     * @param height Alto total del área de dibujo.
     */
    private void calculateCircularPositions(int width, int height) {
        int centerX = width / 2;
        int centerY = height / 2;
        // El radio máximo para el círculo de vértices (dejando un margen)
        int maxRadius = Math.min(centerX, centerY) - PADDING;
        
        for (int i = 0; i < V; i++) {
            // Calcular el ángulo para el vértice i
            double angle = 2 * Math.PI * i / V;
            
            // Convertir coordenadas polares (radio, ángulo) a cartesianas (x, y)
            int x = (int) (centerX + maxRadius * Math.cos(angle));
            int y = (int) (centerY + maxRadius * Math.sin(angle));
            
            vertexPositions.put(i, new Point2D.Double(x, y));
        }
    }

    /**
     * Dibuja el grafo completo en el objeto Bitmap.
     */
    public Bitmap drawGraph() {
        // 1. Dibujar todas las aristas (líneas)
        drawEdges();

        // 2. Dibujar todos los vértices (círculos) y sus etiquetas
        drawVertices();
        
        return bitmap;
    }

    /**
     * Dibuja las aristas basándose en la matriz de adyacencia.
     */
    private void drawEdges() {
        for (int i = 0; i < V; i++) {
            for (int j = i + 1; j < V; j++) {
                // Si hay una arista (no dirigido, verificamos solo una vez)
                if (adjMatrix[i][j] >= 1) { 
                    Point2D.Double p1 = vertexPositions.get(i);
                    Point2D.Double p2 = vertexPositions.get(j);
                    
                    bitmap.drawText(String.valueOf(adjMatrix[i][j]), 
                    		(int) ((p1.getX() + p2.getX()) / 2 - 5), // Ajuste manual simple para centrar
                    		(int) ((p1.getY() + p2.getY()) / 2 - 5), // Ajuste manual simple para centrar
                            LABEL_FONT, 
                            TEXT_COLOR, 
                            Color.BLACK); 
                    
                    // Usar drawLine de la clase Bitmap
                    bitmap.drawLine((int) p1.getX(), (int) p1.getY(), 
                                    (int) p2.getX(), (int) p2.getY(), 
                                    EDGE_COLOR);
                }
            }
        }
    }

    /**
     * Dibuja los vértices y sus etiquetas.
     */
    private void drawVertices() {
        for (int i = 0; i < V; i++) {
            Point2D.Double p = vertexPositions.get(i);
            int x = (int) p.getX();
            int y = (int) p.getY();
            
            // Añadir la etiqueta (número del vértice)
            // Calculamos la posición del texto para que esté centrado o cerca del vértice.
            // Para simplificar, la posición (x, y) del texto será el centro del vértice.
            
            // NOTA: Tu método drawText requiere una posición y ajusta la caja de texto.
            // Aquí lo posicionamos en el centro.
            bitmap.drawText(String.valueOf(i), 
                            x - 5, // Ajuste manual simple para centrar
                            y + 5, // Ajuste manual simple para centrar
                            LABEL_FONT, 
                            TEXT_COLOR, 
                            Color.BLACK); // Fondo nulo, ya que el círculo es el fondo
            // Dibujar el círculo del vértice
            bitmap.drawCircle(x, y, VERTEX_RADIUS, VERTEX_COLOR);
        }
    }

    /**
     * Método de ejemplo para probar la funcionalidad.
     */
    public static void main(String[] args) {
        // Ejemplo de Matriz de Adyacencia (Grafo de 5 vértices: 0, 1, 2, 3, 4)
        // 0-1, 1-2, 2-3, 3-4, 4-0
        int[][] ejemploGrafo = {
            // 0 1 2 3 4
            { 0, 10, 55, 20, 25, 30}, // 0
            {10,  0,  1,  0,  0, 5}, // 1
            {55,  1,  0,  1,  0, 0}, // 2
            {20,  0,  1,  0,  1, 0}, // 3
            {25,  0,  0,  1,  0, 1},  // 4
            { 30,  5,  0,  0,  1, 0}
        };

        try {
            // Creamos el objeto para dibujar el grafo en un Bitmap de 600x600
            GrafoToBitmap drawer = new GrafoToBitmap(ejemploGrafo, 800, 800);
            Bitmap finalBitmap = drawer.drawGraph();
            
            BitmapViewer.showBitmaps(finalBitmap);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Asegúrate de que la clase 'ValidacionesUtiles' y las dependencias de Java AWT/Swing estén disponibles.");
        }
    }
}
