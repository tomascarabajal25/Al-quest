package juego.ciudades.torresDeHanoi;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import juego.ciudades.ordenamientos.EstadoDePartida;
import juego.ciudades.torresDeHanoi.UI.MinijuegoHanoi;
import juego.configuracion.ConfiguracionDeHanoi;
import modelos.Jugador;
import modelos.Partida;
import modelosVista.Vista;
import utils.ValidacionesUtiles;

/**
 * Partida de Torres de Hanoi integrada en la Vista del juego.
 *
 * Responsabilidades:
 *  - Gestionar el ciclo de vida: Creado → Iniciado → Creado (finalizado).
 *  - Pedirle al jugador la dificultad (cantidad de discos) al iniciar.
 *  - Crear y mostrar la ventana con Vista.
 *  - Contener y exponer el motor lógico CiudadHanoi.
 *  - Calcular y guardar el puntaje al terminar.
 *
 * INVARIANTES:
 *  - ConfiguracionDeHanoi.DISCOS_MINIMOS <= cantidadDiscos <= ConfiguracionDeHanoi.DISCOS_MAXIMOS
 *  - juego != null después de un iniciar() exitoso
 *
 * Ciclo de vida:
 *   new PartidaHanoi(nombre, jugador) → iniciar() → [juego corre] → finalizar()
 */
public class PartidaHanoi extends Partida {

    // ATRIBUTOS

    /** Motor lógico del puzzle. Es null hasta que el jugador elige la dificultad. */
    private CiudadHanoi juegoHanoi;

    /** Cantidad de discos elegida para esta partida. */
    private int cantidadDiscos;

    /** Vista del mundo donde se monta el minijuego. Es null hasta iniciar(). */
    private Vista vista;

    /** Ventana gráfica de la ciudad. Es null hasta iniciar(). */
    private JFrame ventana;

    /** Controlador del minijuego de Hanoi. */
    private MinijuegoHanoi minijuego;

    // CONSTRUCTORES

    /**
     * Pre:
     * - nombre != null
     * - jugador != null
     *
     * Post:
     * - La partida queda creada en estado Creado. La cantidad de discos se
     *   define recién en iniciar(), de forma interactiva.
     *
     * @param nombre  nombre de la ciudad asociada a esta partida
     * @param jugador jugador que participa en la partida
     */
    public PartidaHanoi(String nombre, Jugador jugador) {
        super(nombre, jugador);
    }

    // METODOS DE COMPORTAMIENTO

    /**
     * Pre:
     * - La partida no debe estar ya iniciada.
     *
     * Post:
     * - Se le pide al jugador la cantidad de discos mediante un diálogo.
     * - Si el jugador cancela, la partida se finaliza sin crear ventana.
     * - Si elige una dificultad, se crea el motor lógico, la Vista, el
     *   minijuego y la ventana, y arranca el hilo de renderizado a 60 FPS.
     */
    @Override
    public void iniciar() {
        ValidacionesUtiles.validarFalso(estaIniciada(), "La partida ya está iniciada");
        setEstado(EstadoDePartida.Iniciado);

        // 1. PEDIR LA DIFICULTAD AL JUGADOR
        Integer discosElegidos = pedirCantidadDeDiscos();

        if (discosElegidos == null) {
            finalizar();
            return;
        }

        this.cantidadDiscos = discosElegidos;

        // 2. Creación del motor lógico con la cantidad de discos elegida
        this.juegoHanoi = new CiudadHanoi(cantidadDiscos);

        // 3. Creación de la infraestructura de vista (Mundo 3 de Hanoi)
        this.vista = new Vista(
                ConfiguracionDeHanoi.RUTA_MAPA,
                getJugador(),
                ConfiguracionDeHanoi.SPAWN_JUGADOR_COLUMNA,
                ConfiguracionDeHanoi.SPAWN_JUGADOR_FILA,
                getRutaSprites(),
                this.sonido);

        // 4. Creación del controlador del minijuego pasándole la vista ya creada
        this.minijuego = new MinijuegoHanoi(getJugador(), vista.getTamanio(), this);

        // 5. Inyecciones y vinculaciones de comportamiento
        vista.establecerMinijuego(minijuego);

        // KeyListener para capturar el control de teclas del puzzle (1/2/3/R/ESC)
        vista.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evento) {
                if (minijuego.isActivo()) {
                    minijuego.procesarTecla(evento.getKeyChar());
                    evento.consume();
                }
            }
        });

        // Configuración del callback para que el minijuego avise al terminar
        minijuego.setOnFinalizadoCallback(this::finalizar);

        // 6. Despliegue de la interfaz gráfica
        ventana = new JFrame();
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setResizable(false);
        ventana.setTitle(ConfiguracionDeHanoi.TITULO_VENTANA);
        ventana.add(vista);
        ventana.pack();
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);

        // Asegura que la ventana tome el foco del teclado inmediatamente
        vista.requestFocusInWindow();

        // 7. Arranca el bucle principal de renderizado (60 FPS)
        vista.startGameThread();
        // Reproducir música de la ciudad si la instancia de sonido fue inyectada
        if (this.sonido != null) {
            this.sonido.playMusica(juego.configuracion.ConstantesSonido.HANOI);
        }
    }

    /**
     * Pre:
     * - estado == Iniciado
     *
     * Post:
     * - Calcula y guarda el puntaje final.
     * - Detiene el hilo de renderizado y cierra la ventana, si llegaron a
     *   crearse (puede no haber Vista/ventana si el jugador canceló la
     *   selección de dificultad).
     * - Notifica al mapa global para abrir los caminos correspondientes.
     */
    @Override
    public void finalizar() {
        ValidacionesUtiles.validarVerdadero(estaIniciada(), "La partida no está iniciada");
        setEstado(EstadoDePartida.Creado);
        setPuntaje(calcularPuntaje());

        // Si el jugador canceló el diálogo de dificultad, vista y ventana
        // nunca se crearon: validamos antes de usarlas.
        if (vista != null) {
            vista.detenerHilo();
        }

        if (ventana != null) {
            ventana.dispose();
            ventana = null;
        }

        // Restaurar música global al volver al mapa
        if (this.sonido != null) {
            this.sonido.stopMusica();
            this.sonido.playMusica(juego.configuracion.ConstantesSonido.GLOBAL_AVENTURA);
        }

        notificarFinalizacion();
    }

    /**
     * Permite que clases externas (p.ej. MinijuegoHanoi) registren el puntaje.
     *
     * @param puntos puntaje a registrar para esta partida
     */
    public void actualizarPuntaje(int puntos) {
        this.setPuntaje(puntos);
    }

    /**
     * Pide al jugador la cantidad de discos (dificultad) mediante un diálogo.
     *
     * Post:
     * - Devuelve un valor entre ConfiguracionDeHanoi.DISCOS_MINIMOS y
     *   ConfiguracionDeHanoi.DISCOS_MAXIMOS si el jugador eligió una opción.
     * - Devuelve null si el jugador cerró el diálogo sin elegir.
     *
     * @return cantidad de discos elegida, o null si se canceló
     */
    private Integer pedirCantidadDeDiscos() {
        Integer[] opcionesDiscos = generarOpcionesDeDiscos();

        return (Integer) JOptionPane.showInputDialog(
                null,
                ConfiguracionDeHanoi.MENSAJE_SELECCION_DIFICULTAD,
                ConfiguracionDeHanoi.TITULO_DIALOGO_CONFIGURACION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcionesDiscos,
                opcionesDiscos[0]);
    }

    /**
     * Genera las opciones de dificultad disponibles, una por cada cantidad
     * de discos entre DISCOS_MINIMOS y DISCOS_MAXIMOS (inclusive).
     *
     * Post:
     * - Devuelve un arreglo de longitud (DISCOS_MAXIMOS - DISCOS_MINIMOS + 1)
     *   con las cantidades de discos seleccionables, en orden creciente.
     *
     * @return arreglo con las cantidades de discos seleccionables
     */
    private Integer[] generarOpcionesDeDiscos() {
        int cantidadDeOpciones =
                ConfiguracionDeHanoi.DISCOS_MAXIMOS - ConfiguracionDeHanoi.DISCOS_MINIMOS + 1;
        Integer[] opciones = new Integer[cantidadDeOpciones];

        for (int indice = 0; indice < cantidadDeOpciones; indice++) {
            opciones[indice] = ConfiguracionDeHanoi.DISCOS_MINIMOS + indice;
        }

        return opciones;
    }

    /**
     * Calcula el puntaje obtenido en esta partida.
     *
     * Post:
     * - Devuelve 0 si todavía no se jugó o no se ganó.
     * - Si se ganó, devuelve PUNTAJE_BASE_PERFECTO o PUNTAJE_BASE_IMPERFECTO
     *   (según si se logró el mínimo de movimientos) multiplicado por la
     *   cantidad de discos de la partida.
     *
     * @return puntaje final de la partida
     */
    public int calcularPuntaje() {
        if (this.juegoHanoi == null || !this.juegoHanoi.haGanado()) {
            return 0;
        }

        int multiplicadorPorDificultad = this.juegoHanoi.getObjetivo();
        int puntajeBase = this.juegoHanoi.esPerfecto()
                ? ConfiguracionDeHanoi.PUNTAJE_BASE_PERFECTO
                : ConfiguracionDeHanoi.PUNTAJE_BASE_IMPERFECTO;

        return puntajeBase * multiplicadorPorDificultad;
    }

    // GETTERS

    /**
     * Devuelve el motor lógico del puzzle.
     *
     * Post:
     * - Devuelve null si iniciar() todavía no se ejecutó o si el jugador
     *   canceló la selección de dificultad.
     *
     * @return motor lógico de Torres de Hanoi
     */
    public CiudadHanoi getJuego() {
        return juegoHanoi;
    }

    /** @return cantidad de discos elegida para esta partida */
    public int getCantidadDeDiscos() {
        return cantidadDiscos;
    }

    // METODOS GENERALES

    @Override
    public String toString() {
        return "PartidaHanoi [cantidadDiscos=" + cantidadDiscos + ", juego=" + juegoHanoi + "]";
    }

    @Override
    public int hashCode() {
        final int numeroPrimo = 31;
        int resultado = super.hashCode();
        resultado = numeroPrimo * resultado + Objects.hash(cantidadDiscos, juegoHanoi);
        return resultado;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PartidaHanoi otraPartida = (PartidaHanoi) obj;
        return cantidadDiscos == otraPartida.cantidadDiscos
                && Objects.equals(juegoHanoi, otraPartida.juegoHanoi);
    }
}