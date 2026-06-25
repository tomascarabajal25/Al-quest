package juego.ciudades.torresDeHanoi;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import juego.ciudades.torresDeHanoi.UI.MinijuegoHanoi;
import juego.configuracion.ConfiguracionDeHanoi;
import modelos.EstadoDePartida;
import modelos.Jugador;
import modelos.Partida;
import modelos.Sonido;
import modelosVista.Vista;
import utils.ValidacionesUtiles;

/**
 * Partida de Torres de Hanoi integrada en la Vista del juego.
 *
 * Coordinadora del ciclo de vida de una sesión de juego de Torres de Hanoi.
 * Gestiona la interacción con el jugador, la creación del motor lógico (CiudadHanoi),
 * la interfaz gráfica y el cálculo del puntaje final.
 *
 * Responsabilidades:
 * - Gestionar el ciclo de vida: Creado → Iniciado → Creado (finalizado).
 * - Solicitar la dificultad (cantidad de discos) al iniciar.
 * - Crear y mostrar la ventana con Vista.
 * - Contener y exponer el motor lógico CiudadHanoi.
 * - Calcular y guardar el puntaje al terminar.
 *
 * INVARIANTES:
 * - ConfiguracionDeHanoi.DISCOS_MINIMOS <= cantidadDiscos <= ConfiguracionDeHanoi.DISCOS_MAXIMOS
 * - juegoHanoi != null después de un iniciar() exitoso
 *
 * Ciclo de vida:
 *   new PartidaHanoi(nombre, jugador, sonido) → iniciar() → [juego corre] → finalizar()
 */
public class PartidaHanoi extends Partida {

    // CONSTANTES

    // No hay constantes locales; se usan las de ConfiguracionDeHanoi

    // ATRIBUTOS DE CLASE

    // No hay atributos de clase

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
     * Construye una nueva partida de Torres de Hanoi.
     *
     * Pre:
     * - nombre != null
     * - jugador != null
     * - sonido puede ser null
     *
     * Post:
     * - La partida queda creada en estado Creado.
     * - juegoHanoi es null (se crea al llamar iniciar()).
     * - cantidadDiscos es 0 (se define al jugador elegir dificultad).
     * - vista y ventana son null (se crean en iniciar() si el jugador no cancela).
     *
     * @param nombre nombre de la ciudad asociada a esta partida
     * @param jugador jugador que participa en la partida
     * @param sonido sistema de sonido; puede ser null
     */
    public PartidaHanoi(String nombre, Jugador jugador, Sonido sonido) {
        super(nombre, jugador);
        setSonido(sonido);
    }

    // METODOS DE CLASE

    // No hay métodos de clase

    // METODOS GENERALES

    // Los métodos generales están al final de la clase

    // METODOS DE COMPORTAMIENTO

    /**
     * Inicia la partida de Torres de Hanoi.
     *
     * Pre:
     * - La partida debe estar en estado Creado (no debe estar ya iniciada).
     *
     * Post:
     * - Cambia el estado a Iniciado.
     * - Solicita al jugador elegir la dificultad (cantidad de discos).
     * - Si el jugador cancela:
     *   * La partida permanece en estado Creado.
     *   * juegoHanoi, vista y ventana quedan null.
     * - Si el jugador elige una dificultad:
     *   * Crea el motor lógico CiudadHanoi con la cantidad elegida.
     *   * Crea la Vista del mundo.
     *   * Crea el controlador MinijuegoHanoi.
     *   * Monta la interfaz gráfica en una ventana JFrame.
     *   * Inicia el hilo de renderizado a 60 FPS.
     *   * Reproduce la música de la ciudad.
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

        // 3. Creación de la infraestructura de vista (Mundo de Hanoi)
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
        // Si el jugador cierra la ventana con la X, debemos finalizar la partida
        // para detener música y limpiar recursos.
        attachCloseHandler(ventana);

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
     * Finaliza la partida de Torres de Hanoi.
     *
     * Pre:
     * - La partida debe estar en estado Iniciado.
     *
     * Post:
     * - Cambia el estado a Creado.
     * - Calcula y guarda el puntaje final.
     * - Si la Vista fue creada, detiene su hilo de renderizado.
     * - Si la ventana fue creada, cierra y libera recursos.
     * - Detiene la música de la partida y reproduce la música global.
     * - Notifica al sistema que la partida ha finalizado.
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
     * Permite que clases externas (p.ej. MinijuegoHanoi) actualicen el puntaje.
     *
     * Pre:
     * - puntos >= 0
     *
     * Post:
     * - Actualiza el puntaje de la partida al valor indicado.
     *
     * @param puntos puntaje a registrar para esta partida
     */
    public void actualizarPuntaje(int puntos) {
        this.setPuntaje(puntos);
    }

    /**
     * Solicita al jugador elegir la dificultad mediante un diálogo interactivo.
     *
     * Post:
     * - Si el jugador selecciona una opción válida, devuelve la cantidad de discos.
     * - Si el jugador cierra el diálogo sin seleccionar, devuelve null.
     * - Las opciones van desde ConfiguracionDeHanoi.DISCOS_MINIMOS hasta
     *   ConfiguracionDeHanoi.DISCOS_MAXIMOS.
     *
     * @return cantidad de discos elegida, o null si se canceló la selección
     */
    private Integer pedirCantidadDeDiscos() {
        Integer[] opciones = generarOpcionesDeDiscos();

        return (Integer) JOptionPane.showInputDialog(
                null,
                ConfiguracionDeHanoi.MENSAJE_SELECCION_DIFICULTAD,
                ConfiguracionDeHanoi.TITULO_DIALOGO_CONFIGURACION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);
    }

    /**
     * Genera el arreglo de opciones de dificultad disponibles.
     *
     * Post:
     * - Devuelve un arreglo de Integer con valores desde
     *   ConfiguracionDeHanoi.DISCOS_MINIMOS hasta ConfiguracionDeHanoi.DISCOS_MAXIMOS
     *   (inclusive), en orden creciente.
     * - La longitud del arreglo es
     *   (DISCOS_MAXIMOS - DISCOS_MINIMOS + 1).
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
     * - Devuelve 0 si juegoHanoi es null o el jugador no ganó.
     * - Si se ganó:
     *   * Calcula PUNTAJE_BASE_PERFECTO si se logró con mínimo de movimientos.
     *   * Calcula PUNTAJE_BASE_IMPERFECTO en caso contrario.
     *   * Multiplica el puntaje base por la cantidad de discos.
     * - El puntaje varía según dificultad y perfección de la jugada.
     *
     * @return puntaje final de la partida (0 si no se ganó)
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
     * - Tras un iniciar() exitoso, devuelve una instancia válida de CiudadHanoi.
     *
     * @return motor lógico de Torres de Hanoi, o null
     */
    public CiudadHanoi getJuego() {
        return juegoHanoi;
    }

    /**
     * Post: no modifica el estado de la partida; solo lo consulta.
     *
     * @return cantidad de discos elegida para esta partida (0 si no se ha iniciado)
     */
    public int getCantidadDeDiscos() {
        return cantidadDiscos;
    }

    // SETTERS

    // Los setters son manejados por la clase padre Partida

    // METODOS GENERALES

    @Override
    public String toString() {
        StringBuilder representacion = new StringBuilder();
        representacion.append("PartidaHanoi{");
        representacion.append("nombre=").append(getNombre());
        representacion.append(", estado=").append(getEstado());
        representacion.append(", cantidadDiscos=").append(cantidadDiscos);
        representacion.append(", puntaje=").append(getPuntaje());
        if (juegoHanoi != null) {
            representacion.append(", movimientos=").append(juegoHanoi.getMovimientos());
            representacion.append(", ganador=").append(juegoHanoi.haGanado());
        }
        representacion.append("}");
        return representacion.toString();
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