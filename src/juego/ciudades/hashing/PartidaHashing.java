package juego.ciudades.hashing;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import juego.ciudades.hashing.ui.FabricaMinijuegoHashing;
import juego.ciudades.hashing.ui.MinijuegoHashing;
import juego.ciudades.ordenamientos.EstadoDePartida;
import modelos.Jugador;
import modelos.Partida;
import modelosVista.Vista;
import utils.ValidacionesUtiles;

/**
 * Partida de la ciudad 6 Hashing dentro del mundo 2D.
 * 
 * Funcionamiento:
 * -iniciar() crea la CiudadHashing, monta el MinijuegoHashing en la Vista,
 *  abre la ventana y arranca el hilo del juego.
 * -El minijuego avisa la victoria mediante onVictoria,
 *  de ahi se suma el puntaje y se finaliza la partida.
 * 
 */

public class PartidaHashing extends Partida {
    // CONSTANTES
    private static final int PUNTAJE_VICTORIA = 500;
    private static final String TITULO_VENTANA = "Ciudad de Hashing";
    private static final int FILA_SPAWN = 48;
    private static final int COL_SPAWN  = 1;
    private static final String RUTA_MAPA = "/maps/world_hashing.txt";

    // ATRIBUTOS
    private CiudadHashing ciudad;
    private MinijuegoHashing minijuego;
    private Vista vista;
    private JFrame ventana;

    // CONFIGURACIÓN DINÁMICA
    private int cantidadSlots;
    private List<ElementoHash> elementos;
    private List<Integer> clavesABuscar;
    
    // CONFIGURACIÓN ESTÁTICA
    private List<Point> posicionesSlots;

    /**
     * Constructor estandarizado de la Partida Hashing.
     */
    public PartidaHashing(String nombre, Jugador jugador) {
        super(nombre, jugador);
        setEstado(EstadoDePartida.Creado);
    }


    //METODOS DE COMPORTAMIENTO
    /**
     * PRE: la partida no tiene que estar iniciada. 
     * POST: crea la ciudad y el minijuego, los monta en la Vista, abre la ventana
     *       y arranca el hilo del juego.
     */
    @Override
    public void iniciar() {
        ValidacionesUtiles.validarFalso(estaIniciada(), "La partida ya ha sido iniciada");
        setEstado(EstadoDePartida.Iniciado);
     // 1. INPUT DEL JUGADOR (El usuario elige la cantidad de slots, elementos y claves)
        try {
            // A. Cantidad de Slots
            this.cantidadSlots = pedirEnteroValido("Ingrese la cantidad de SLOTS para la Tabla Hash:", 3, 7);

            // B. Ingresar los Elementos
            int cantElementos = pedirEnteroValido("¿Cuántos ELEMENTOS querés insertar en la tabla?", 1, this.cantidadSlots);
            
            this.elementos = new ArrayList<>();
            this.clavesABuscar = new ArrayList<>(); // Llenaremos esto automáticamente

            for (int i = 0; i < cantElementos; i++) {
                int clave = pedirEnteroValido("Ingrese la CLAVE (número) del elemento " + (i + 1) + ":", 0, 9999);
                String nombre = pedirStringValido("Ingrese el NOMBRE del elemento " + (i + 1) + ":");
                
                this.elementos.add(new ElementoHash(clave, nombre));
                
                // EL SISTEMA ELIGE EL DESAFÍO: 
                // Agregamos la clave recién creada a la lista de tareas a buscar.
                this.clavesABuscar.add(clave); 
            }

            // (Eliminamos por completo el paso C donde le preguntábamos al jugador)

        } catch (SecurityException e) {
            finalizar();
            return; // Aborta limpiamente si el usuario cancela
        }
        cargarPosicionesSlots(cantidadSlots);

        setVista(new Vista(RUTA_MAPA, getJugador(), COL_SPAWN, FILA_SPAWN, getRutaSprites(), this.sonido));

        this.ciudad = new CiudadHashing(cantidadSlots);

        this.minijuego = FabricaMinijuegoHashing.crear(vista, ciudad, elementos, clavesABuscar, posicionesSlots);

        // Cuando el jugador gane, esta partida suma puntaje y se finaliza
        minijuego.setOnVictoria(() -> {
            setPuntaje(PUNTAJE_VICTORIA*cantidadSlots);
            finalizar();
        });

        
        // Ventana del juego
        ventana = new JFrame();
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setResizable(false);
        ventana.setTitle(TITULO_VENTANA);
        ventana.add(vista);
        ventana.pack();
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
        vista.requestFocusInWindow();

        // Arranca el bucle del juego (run -> actualizar -> repaint a 60 fps)
        vista.startGameThread();
         if (this.sonido != null) {
        	 this.sonido.playMusica(juego.configuracion.ConstantesSonido.HASHING);			
         }
    }


    /**
     * Carga las posiciones físicas estáticas y recorta la lista
     * para que coincida exactamente con la cantidad de slots elegida.
     */
    private void cargarPosicionesSlots(int cantidad) {
        List<Point> todasLasPosiciones = new ArrayList<>();
        todasLasPosiciones.add(new Point(4, 4));   
        todasLasPosiciones.add(new Point(40, 40)); 
        todasLasPosiciones.add(new Point(11, 9));  
        todasLasPosiciones.add(new Point(6, 40));  
        todasLasPosiciones.add(new Point(23, 22)); 
        todasLasPosiciones.add(new Point(40, 6));  
        todasLasPosiciones.add(new Point(38, 24)); 

        // Recortamos la lista para que tenga exactamente la misma cantidad que los slots
        this.posicionesSlots = new ArrayList<>(todasLasPosiciones.subList(0, cantidad));
    }

    private int pedirEnteroValido(String mensaje, int min, int max) {
        while (true) {
            String input = JOptionPane.showInputDialog(null, mensaje + " (" + min + " a " + max + "):", 
                    "Configuración Hashing", JOptionPane.QUESTION_MESSAGE);
            if (input == null) throw new SecurityException("Cancelado");
            try {
                int valor = Integer.parseInt(input.trim());
                if (valor >= min && valor <= max) return valor;
                JOptionPane.showMessageDialog(null, "El valor debe estar entre " + min + " y " + max + ".");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Entrada inválida. Ingrese un número entero.");
            }
        }
    }

    private String pedirStringValido(String mensaje) {
        while (true) {
            String input = JOptionPane.showInputDialog(null, mensaje, "Configuración Hashing", JOptionPane.QUESTION_MESSAGE);
            if (input == null) throw new SecurityException("Cancelado");
            if (!input.trim().isEmpty()) return input.trim();
            JOptionPane.showMessageDialog(null, "El campo no puede estar vacío.");
        }
    }


	/**
     * PRE: la partida debe estar iniciada
     * POST: detiene el hilo del juego y vuelve el estado a "Creado"
     */
    @Override
    public void finalizar() {
        ValidacionesUtiles.validarVerdadero(estaIniciada(), getNombre());
        setEstado(EstadoDePartida.Creado);
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


    //GETTERS
    public CiudadHashing getCiudad() { //Devuelve la logica de la ciudad (null si todavia no inicio)
        return this.ciudad;
    }

    public MinijuegoHashing getMinijuego() {
        return this.minijuego; //Devuelve el minijuego activo (null si todavia no inicio)
    }


    //METODOS GENERALES
    @Override
    public String toString() {
        return "PartidaHashing [slots=" + cantidadSlots + ", elementos=" + elementos.size()
                + ", clavesABuscar=" + clavesABuscar.size() + "]";
    }



    //SETTERS PRIVADOS
    private void setVista(Vista vista) {
        ValidacionesUtiles.esDistintoDeNull(vista, "vista");
        this.vista = vista;
    }



}
