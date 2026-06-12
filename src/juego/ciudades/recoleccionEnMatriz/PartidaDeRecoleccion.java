package juego.ciudades.recoleccionEnMatriz;

import juego.Constantes;
import juego.ciudades.recoleccionEnMatriz.ui.KeyHandlerRecoleccion;
import juego.ciudades.recoleccionEnMatriz.ui.MinijuegoRecoleccion;
import modelos.Jugador;
import modelos.Partida;
import modelosVista.Vista;
import utils.ValidacionesUtiles;

import javax.swing.*;

import java.util.Objects;

public class PartidaDeRecoleccion extends Partida {
	//ATRIBUTOS -----------------------------------------------------------------------------------------------
    private CiudadRecoleccion juego = null;
    private Vista vista;
    private JFrame ventana;
    private int ultimoNivel;

    //CONSTRUCTORES -------------------------------------------------------------------------------------------

    /**
     * Constructor del TDA partidaDeRecoleccion simplificado.
     * El TDA se crea listo para ser configurado dinámicamente en iniciar().
     *
     * @param nombre: Nombre de la ciudad
     * @param jugador: Jugador de la ciudad
     */
    public PartidaDeRecoleccion(String nombre, Jugador jugador) {
        super(nombre, jugador);

        setJuego(Constantes.FILAS_MAPA, Constantes.COLUMNAS_MAPA, Constantes.NIVELES_MAPA, Constantes.CAPACIDAD_MAXIMA_MOCHILA, getJugador());

    }
    //METODOS ABSTRACTOS --------------------------------------------------------------------------------------
    //METODOS HEREDADOS (CLASE)--------------------------------------------------------------------------------
    //METODOS HEREDADOS (INTERFACE)----------------------------------------------------------------------------
    //METODOS DE CLASE ----------------------------------------------------------------------------------------

    /**
     * Equals del TDA PartidaDeRecoleccion. Compara en base al atributo juego
     * @param o   the reference object with which to compare.
     * @return: true si son iguales, false si no lo son
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PartidaDeRecoleccion that = (PartidaDeRecoleccion) o;
        return Objects.equals(juego, that.juego);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), juego);
    }

    @Override
    public String toString() {
        return "PartidaDeRecoleccion{" +
                "juego=" + juego +
                '}';
    }

    //METODOS GENERALES ---------------------------------------------------------------------------------------
    //METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------
    @Override
    public void iniciar() {this.ultimoNivel = juego.getNivelActual();
        KeyHandlerRecoleccion key = new KeyHandlerRecoleccion();
        this.vista = new Vista(obtenerMapaPorNivel(this.juego.getNivelActual()), getJugador(), 24, 21, getRutaSprites(), key);

        this.ventana = new JFrame("Ciudad de Recolección");
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setResizable(false);
        ventana.add(vista);
        ventana.pack();
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);

        MinijuegoRecoleccion minijuego = new MinijuegoRecoleccion(juego, this, vista, key, ventana);
        vista.establecerMinijuego(minijuego);
        minijuego.setOnFinalizadoCallback(this::finalizar);

        vista.startGameThread();
    }

    /**
     * Actualiza la vista del juego
     */
    public void actualizar() {
        int nivelActual = juego.getNivelActual();

        if (nivelActual != ultimoNivel) {
            ultimoNivel = nivelActual;

            vista.cargarMapa(obtenerMapaPorNivel(nivelActual));
        }
    }

    @Override
    public void finalizar() {
        int puntos = 0;

        if (juego != null) {
            puntos = juego.finalizar();
        }
        this.setPuntaje(puntos);

        if (vista != null) {
            vista.detenerHilo();
        }

        if (ventana != null) {
            ventana.dispose();
            ventana = null;
        }

        notificarFinalizacion();
    }
    
    /**
     * Solicita un número entero mediante JOptionPane y lo valida de forma robusta.
     */
    private int pedirEnteroValido(String mensaje, int min, int max) {
        while (true) {
            String input = JOptionPane.showInputDialog(null, mensaje + " (" + min + " - " + max + "):", 
                    "Configuración de Recolección", JOptionPane.QUESTION_MESSAGE);
            
            // Si cancela o cierra la ventana, lanzamos excepción para abortar el 'iniciar()' limpiamente
            if (input == null) {
                throw new SecurityException("Configuración cancelada por el usuario.");
            }

            try {
                int valor = Integer.parseInt(input.trim());
                if (valor >= min && valor <= max) {
                    return valor;
                }
                JOptionPane.showMessageDialog(null, "Por favor, ingrese un número entre " + min + " y " + max + ".", 
                        "Valor fuera de rango", JOptionPane.WARNING_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Entrada inválida. Debe ingresar un número entero.", 
                        "Error de formato", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Devuelve el mapa correspondiente al nivel
     *
     * PRE:
     * -Nivel debe ser mayor a cero
     *
     * @param nivel
     * @return
     */
    private String obtenerMapaPorNivel(int nivel) {
        ValidacionesUtiles.validarMayorACero(nivel, "nivel");

        return switch (nivel) {
            case 1 -> "/maps/recoleccion/world_recoleccion_1.txt";
            case 2 -> "/maps/recoleccion/world_recoleccion_2.txt";
            case 3 -> "/maps/recoleccion/world_recoleccion_3.txt";
            default -> "/maps/recoleccion/world_recoleccion_1.txt";
        };
    }
    //METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------
    //GETTERS REDEFINIDOS -------------------------------------------------------------------------------------
    //GETTERS INICIALIZADOS -----------------------------------------------------------------------------------
    //GETTERS COMPLEJOS ---------------------------------------------------------------------------------------
    //GETTERS SIMPLES -----------------------------------------------------------------------------------------
    /**
     * Getter del atributo juego
     *
     * @return: Devuelve el atributo juego
     */
    public CiudadRecoleccion getJuego() {
        return this.juego;
    }
    //SETTERS COMPLEJOS----------------------------------------------------------------------------------------
    //SETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * Setter del atributo juego
     * @param filas: Filas del mapa del juego
     * @param columnas: Columnas del mapa del juego
     * @param niveles: Niveles del mapa del juego
     * @param maximoMochila: Maxima capacidad de la mochila del juego
     * @param jugador: Jugador del juego
     */
    private void setJuego(int filas, int columnas, int niveles, int maximoMochila, Jugador jugador){
        ValidacionesUtiles.validarMayorACero(filas, "filas");
        ValidacionesUtiles.validarMayorACero(columnas, "columnas");
        ValidacionesUtiles.validarMayorACero(niveles, "niveles");
        ValidacionesUtiles.validarMayorACero(maximoMochila, "maximoMochila");
        ValidacionesUtiles.esDistintoDeNull(jugador, "jugador");

        this.juego = new CiudadRecoleccion(filas, columnas, niveles, maximoMochila, jugador);
    }

}
