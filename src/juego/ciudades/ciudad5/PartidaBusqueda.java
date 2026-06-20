package juego.ciudades.ciudad5;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import juego.ciudades.ciudad5.UI.MinijuegoDesafio;
import juego.ciudades.ordenamientos.EstadoDePartida;
import juego.configuracion.ConfiguracionBusqueda;
import modelos.Jugador;
import modelos.Mapa;
import modelos.Partida;
import modelosVista.Vista;

public class PartidaBusqueda extends Partida {

    private Vista            vista;
    private JFrame           ventana;
    private MinijuegoDesafio minijuego;

    /** Se pone en true únicamente desde el callback de victoria, antes de llamar finalizar(). */
    private boolean gano = false;

    /**
     * pre:  nombre y jugador no nulos.
     * post: el TDA se crea listo para ser configurado dinámicamente en iniciar().
     */
    public PartidaBusqueda(String nombre, Jugador jugador) {
        super(nombre, jugador);
        setEstado(EstadoDePartida.Creado);
    }

    @Override
    public void iniciar() {
        setEstado(EstadoDePartida.Iniciado);

        String archivoElegido = (String) JOptionPane.showInputDialog(
            null,
            "Seleccioná el archivo de diccionario para el desafío:",
            "Configuración de Ciudad de Búsqueda",
            JOptionPane.QUESTION_MESSAGE,
            null,
            ConfiguracionBusqueda.ARCHIVOS_DISPONIBLES,
            ConfiguracionBusqueda.ARCHIVOS_DISPONIBLES[0]
        );

        if (archivoElegido == null) {
            finalizar();
            return;
        }

        Mapa mapaDePalabras = cargarDesdeArchivo(archivoElegido);

        if (mapaDePalabras == null) {
            JOptionPane.showMessageDialog(
                null,
                "Error crítico: no se pudo leer el archivo de palabras seleccionado.",
                "Error de Archivo",
                JOptionPane.ERROR_MESSAGE
            );
            finalizar();
            return;
        }

        this.vista = new Vista(
            ConfiguracionBusqueda.RUTA_MAPA_MUNDO,
            getJugador(),
            ConfiguracionBusqueda.PANTALLA_ANCHO_TILES,
            ConfiguracionBusqueda.PANTALLA_ALTO_TILES,
            getRutaSprites(),
            this.sonido
        );

        this.minijuego = new MinijuegoDesafio(mapaDePalabras, vista.getTamanio());
        vista.establecerMinijuego(minijuego);
        vista.getAdminObjt().setObjetos(minijuego.getPuertaLista(), minijuego.getPuertaArbol());

        // El flag gano se activa ANTES de llamar a finalizar, garantizando
        // que calcularPuntaje() no dependa del estado mutable del minijuego.
        minijuego.setOnFinalizadoCallback(() -> {
            gano = true;
            finalizar();
        });

        ventana = new JFrame("Ciudad de Búsqueda");
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setResizable(false);
        ventana.add(vista);
        ventana.pack();
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);

        vista.startGameThread();
        if (this.sonido != null) {
            this.sonido.playMusica(juego.configuracion.ConstantesSonido.BUSQUEDA);
        }
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
        if (this.sonido != null) {
			this.sonido.stopMusica();
			this.sonido.playMusica(juego.configuracion.ConstantesSonido.GLOBAL_AVENTURA);
		}

        notificarFinalizacion();
        
    }

    private int calcularPuntaje() {
        return gano ? ConfiguracionBusqueda.PUNTOS_VICTORIA : 0;
    }

    private static Mapa cargarDesdeArchivo(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {

            List<String[]> filas = new ArrayList<>();
            String linea;
            int maxColumnas = 0;

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

            Mapa mapa = new Mapa(maxColumnas, filas.size());

            for (int i = 0; i < filas.size(); i++) {
                String[] fila = filas.get(i);
                for (int j = 0; j < fila.length; j++) {
                    String palabra = fila[j].trim();
                    if (!palabra.isEmpty() && !palabra.equals("-")) {
                        mapa.ocuparCelda(palabra, j + 1, i + 1);
                    }
                }
            }

            return mapa;

        } catch (IOException e) {
            System.out.println("Error leyendo archivo: " + e.getMessage());
            return null;
        }
    }
}