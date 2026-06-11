package juego.ciudades.ciudad5;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import juego.ciudades.ciudad5.UI.MinijuegoDesafio;
import juego.ciudades.ordenamientos.EstadoDePartida;
import modelos.Jugador;
import modelos.Mapa;
import modelos.Partida;
import modelosVista.Vista;

public class PartidaBusqueda extends Partida {

    private Vista    vista;
    private JFrame   ventana;
    private MinijuegoDesafio minijuego;

    /**
     * pre:  nombre y jugador no nulos.
     * post: El TDA se crea listo para ser configurado dinámicamente en iniciar().
     */
    public PartidaBusqueda(String nombre, Jugador jugador) {
        super(nombre, jugador);
        setEstado(EstadoDePartida.Creado);
    }

    /**
     * post: Permite seleccionar un archivo, genera el Mapa de palabras estructurado,
     * inicializa la interfaz gráfica y arranca el bucle a 60 FPS.
     */
    @Override
    public void iniciar() {
        setEstado(EstadoDePartida.Iniciado);

        String[] opcionesArchivos = {"mapasBusqueda/diccionario_facil.txt", "mapasBusqueda/diccionario_medio.txt", "mapasBusqueda/diccionario_dificil.txt"};
        
        String archivoElegido = (String) JOptionPane.showInputDialog(
            null, 
            "Selecciona el archivo de diccionario para el desafío:", 
            "Configuración de Ciudad de Búsqueda",
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            opcionesArchivos, 
            opcionesArchivos[0]
        );

        // Si el usuario cancela o cierra el diálogo, se aborta limpiamente
        if (archivoElegido == null) {
            finalizar(); 
            return;
        }

        // 2. CONSTRUCCIÓN DEL MODELO (Tu función parsea el .txt y devuelve el objeto Mapa estructurado)
        Mapa mapaDePalabras = cargarDesdeArchivo(archivoElegido);
        
        // Validación de Robustez exigida por la cátedra: si falla la lectura del archivo, no rompemos el programa
        if (mapaDePalabras == null) {
            JOptionPane.showMessageDialog(null, "Error crítico: No se pudo leer el archivo de palabras seleccionado.", "Error de Archivo", JOptionPane.ERROR_MESSAGE);
            finalizar();
            return;
        }

        // 3. INICIALIZACIÓN DE LA VISTA Y PRESENTACIÓN (Paso del Mapa dinámico)
        // Nota: El mapa gráfico del mundo sigue cargándose por recurso (con barra '/')
        this.vista = new Vista("/maps/world01.txt", getJugador(), 24, 3, "/assets/jugador/boy");
        this.minijuego = new MinijuegoDesafio(mapaDePalabras, vista.getTamanio());
        
        vista.establecerMinijuego(minijuego);
        vista.getAdminObjt().setObjetos(minijuego.getPuertaLista(), minijuego.getPuertaArbol());
        
        // Enlaza el callback para el cierre automático al ganar/perder
        minijuego.setOnFinalizadoCallback(this::finalizar);

        // 4. CREACIÓN Y APERTURA DE LA VENTANA
        ventana = new JFrame();
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setResizable(false);
        ventana.setTitle("Ciudad de Búsqueda");
        ventana.add(vista);
        ventana.pack();
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);

        // Arranca el loop gráfico
        vista.startGameThread();
    }

    @Override
    public void finalizar() {
        setEstado(EstadoDePartida.Creado);
        setPuntaje(calcularPuntaje());

        if (vista != null) {
            vista.detenerHilo();
        }
        if (ventana != null) {
            ventana.dispose();
            ventana = null;
        }
        
        // Llama al Runnable para avisar a PartidaGeneral que desbloquee los caminos en el grafo
        notificarFinalizacion();
    }

    private int calcularPuntaje() {
        return minijuego != null && minijuego.isGanado() ? 1000 : 0;
    }

    /**
     * Carga un Mapa desde un archivo de texto plano.
     * Cada línea es una fila; las palabras dentro de la línea se separan por espacios.
     *
     * pre:  rutaArchivo no nula, el archivo existe y es legible
     * post: devuelve un Mapa con las palabras del archivo, o null si hubo error
     */
    private static Mapa cargarDesdeArchivo(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {

            java.util.List<String[]> filas = new java.util.ArrayList<>();
            String linea;
            int maxColumnas = 0;

            // Primera pasada: contar filas y columnas máximas
            while ((linea = br.readLine()) != null) {
                String[] tokens = linea.trim().split("\\s+");
                filas.add(tokens);
                if (tokens.length > maxColumnas) {
                    maxColumnas = tokens.length;
                }
            }

            if (filas.isEmpty()) {
                System.out.println("El archivo está vacío.");
                return null;
            }

            // CORRECCIÓN 1: El constructor pide Mapa(ancho, alto). 
            // maxColumnas representa el Ancho (X) y filas.size() representa el Alto (Y).
            Mapa mapa = new Mapa(maxColumnas, filas.size());

            for (int i = 0; i < filas.size(); i++) {
                String[] fila = filas.get(i);
                for (int j = 0; j < fila.length; j++) {
                    String palabra = fila[j].trim();
                    
                    if (!palabra.isEmpty() && !palabra.equals("-")) {
                        // CORRECCIÓN 2: Tu TDA empieza en 1, por ende sumamos +1 a los índices de los for.
                        // Además, ocuparCelda pide: ocuparCelda(contenido, ancho, alto)
                        // donde 'j' es la columna (ancho) e 'i' es la línea (alto).
                        int posicionAncho = j + 1;
                        int posicionAlto = i + 1;

                        mapa.ocuparCelda(palabra, posicionAncho, posicionAlto);
                        System.out.println("Cargado (" + posicionAncho + "," + posicionAlto + ") -> " + palabra);
                    }
                }
            }

            System.out.println("Archivo cargado con éxito: " + maxColumnas 
                    + " columnas (ancho) x " + filas.size() + " filas (alto).");
            return mapa;

        } catch (IOException e) {
            System.out.println("Error leyendo archivo: " + e.getMessage());
            return null;
        }
    }
}