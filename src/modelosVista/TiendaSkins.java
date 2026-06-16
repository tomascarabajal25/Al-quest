package modelosVista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import modelos.PartidaGeneral;

/**
 * IMPORTANTE: A LA ULTIMA FECHA DE ACTUALIZACION, TIENE 10 SKINS +1 DEFAULT (11)
 * 
 * TDA TiendaSkins - permite al jugador comprar y equipar skins
 * 
 * Se abre desde VistaGlobal al presionar "T". Mientras esta abierto, juego se detiene
 * por lo tanto, se llama a detenerHilo() deste VistaGlobal. Cuando se cierra la pestaña
 * se llama a startGameThread() tambien desde vistaGlobal
 * 
 * Estructura de las skins:
 * -El nombre de cada skin es la primer palabra del archivo, hasta el "_"
 * -El nombre visible en la tienda deriva de ese nombre base
 * -Las skins solo se pueden comprar/equipar si sus 8 BMP existen.
 * -Si sus 8 bmp todavia no existen, la fila aparece "Proximamente", se deshabilita
 * esto es para no romper el juego. 
 *  
*/

public class TiendaSkins extends JDialog {
    
    private static final long serialVersionUID = 1L;

    //IMPORTANTE!! classpath donde viven los sprites del jugador
    //Modificar si cambiamos la carpeta base
    private static final String CARPETA = "/assets/jugador/";

    /**
     * Las skins del catalogo de personajes, formato:
     * {nombreBase , precioEnPuntos}
     * recordar que boy y lady son las default, pero lady hay que reclamarla.
     */
    private static final Object[][] CATALOGO = {
        { "boy",      0    },   // personaje original (siempre desbloqueado)
        { "lady",    0 },   // skin 8  (BMP listos) la idea es que sea default, como boy, NO OLVIDARME!!!!!
        { "captain",  0  },   // skin 1  (BMP listos)
        { "dinosaur", 0  },   // skin 2  (BMP listos)
        { "ivan",     0  },   // skin 3  (BMP listos)
        { "doggy",    0 },   // skin 4  (BMP listos)
        { "goblin",    0 },   // skin 5  (BMP listos)
        { "king",    0 },   // skin 6  (BMP listos)
        { "knight",    80 },   // skin 7  (BMP listos)
        { "roman",    0 },   // skin 9  (BMP listos)
        { "soldier",   0 },   // skin 10 (BMP listos)
        { "goku", 0}, //skin 11 (BMP NO listos, agregar si se encuentran imagenes)
        { "naruto", 0}, //skin 12 (BMP NO listos, agregar si se encuentran imagenes)
        { "yoda", 500000}, //skin 13 (BMP NO listos, agregar si se encuentran imagenes)
    };

    
    //Colores del dialogo
    private static final Color BG_DIALOGO     = new Color( 30,  30,  50);
    private static final Color BG_FILA        = new Color( 45,  45,  65);
    private static final Color BG_BTN_COMPRA  = new Color(140, 100,  20);
    private static final Color BG_BTN_EQUIPAR = new Color( 60,  80, 140);
    private static final Color BG_BTN_EQUIPADA= new Color( 40, 100,  60);
    private static final Color BG_BTN_PROXIMA = new Color( 70,  70,  70); // gris "proximamente"
    private static final Color BG_BTN_CERRAR  = new Color( 60,  60,  80);
    private static final Color COLOR_TITULO   = new Color(255, 220,  60);
    private static final Color COLOR_PUNTOS   = new Color(255, 200,  50);

    //referencias
    private final PartidaGeneral partida;
    private final VistaGlobal vista;
    /**
     * puntos disponibles, se actualiza post compra automaticamente
     */
    private JLabel labelPuntos;



    //CONSTRUCTOR
    /**
     * pre: partida != null, vista != null.
     * POST: dialogo modal construido y listo para setVisible(true)
     * 
     * @param partida orquestador central, fuente de puntos y skins
     * @param vista vista global del mapa, usada para aplicar el cambio de skin
     */
    public TiendaSkins(PartidaGeneral partida, VistaGlobal vista) {
        super((java.awt.Frame) null, "Tienda de Skins", true);
        this.partida = partida;
        this.vista   = vista;
        construirUI();
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }



    //UI
    /**
     * Arma el contenido del dialogo: encabezado, lista de skins y boton cerrar.
     * Se llama tambien al reconstruir la UI despues de comprar/equipar
     */
    private void construirUI() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BG_DIALOGO);

        //Parte del titlo
        JLabel titulo = new JLabel("  Tienda de Skins", SwingConstants.LEFT);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(COLOR_TITULO);
        titulo.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 12));
        add(titulo, BorderLayout.NORTH);

        //Parte de los puntos disponibles
        labelPuntos = new JLabel("Puntos disponibles: " + partida.getPuntajeTotal());
        labelPuntos.setFont(new Font("Arial", Font.BOLD, 13));
        labelPuntos.setForeground(COLOR_PUNTOS);
        labelPuntos.setBorder(BorderFactory.createEmptyBorder(0, 12, 4, 12));

        //parte de la lista de skins
        JPanel panelSkins = new JPanel();
        panelSkins.setLayout(new BoxLayout(panelSkins, BoxLayout.Y_AXIS));
        panelSkins.setBackground(BG_DIALOGO);
        panelSkins.setBorder(BorderFactory.createEmptyBorder(0, 10, 4, 10));

        List<String> desbloqueadas = partida.getSkinsDesbloqueadas();
        String       skinActual    = partida.getSkinActual();

        for (Object[] fila : CATALOGO) {
            String base = (String)fila[0];
            int precio = (int)fila[1];

            String ruta             = CARPETA + base; //ruta completa de las skins
            String nombre           = capitalizar(base); //el nombre visible
            boolean disponible      = skinDisponible(ruta); //se fija si estan los bmp de la skin
            boolean comprada        = desbloqueadas.contains(ruta);
            boolean equipada        = ruta.equals(skinActual);

            panelSkins.add(crearFilaSkin(ruta, nombre, precio, disponible, comprada, equipada));
            panelSkins.add(Box.createVerticalStrut(6));
        }

        //Lista de las skins en un JScrollPane, ya que como puse 11 no entraban
        //todas en pantalla, asi que se hace scrolleable verticalmente.
        JScrollPane scroll = new JScrollPane(
            panelSkins,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG_DIALOGO);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setPreferredSize(new Dimension(460, 320));

        //panel central, los puntos + lista
        JPanel centro = new JPanel(new BorderLayout(0, 6));
        centro.setBackground(BG_DIALOGO);
        centro.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
        centro.add(labelPuntos, BorderLayout.NORTH);
        centro.add(scroll,      BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);

        //el boton de cerrar
        JButton btnCerrar = new JButton("Cerrar tienda");
        btnCerrar.setBackground(BG_BTN_CERRAR);
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.addActionListener(e -> dispose());

        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(BG_DIALOGO);
        panelBoton.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        panelBoton.add(btnCerrar);
        add(panelBoton, BorderLayout.SOUTH);
    }



    /**
     * Crea el panel visual de una sola fila del catalogo
     * 
     * Pre: ruta != null, nombre != null, precio >=0.
     * POST: devuelve un JPanel con el nombre, precio y el boton segun el estado actual
     * los estados posibles son: Proximamente / Equipado / Equipar / Comprar. 
     */
    private JPanel crearFilaSkin(String ruta, String nombre, int precio,
                                 boolean disponible, boolean comprada, boolean equipada) {

        JPanel fila = new JPanel(new BorderLayout(10, 0));
        fila.setBackground(BG_FILA);

        fila.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 100), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        // Nombre + precio (las skins no disponibles se muestran en gris)
        String etiqueta = nombre + (precio > 0 ? "  —  " + precio + " puntos" : "  —  GRATIS");
        JLabel lblNombre = new JLabel(etiqueta);
        if (!disponible) {
            lblNombre.setForeground(new Color(150, 150, 150)); // [10 SKINS] gris "proximamente"
        } else {
            lblNombre.setForeground(equipada ? new Color(80, 220, 100) : Color.WHITE);
        }
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 13));
        fila.add(lblNombre, BorderLayout.CENTER);

        // Boton segun estado
        JButton btn;
        if (!disponible) {
            btn = new JButton("Proximamente");
            btn.setBackground(BG_BTN_PROXIMA);
            btn.setEnabled(false);
        } else if (equipada) {
            btn = new JButton("Equipada");
            btn.setBackground(BG_BTN_EQUIPADA);
            btn.setEnabled(false);
        } else if (comprada) {
            btn = new JButton("Equipar");
            btn.setBackground(BG_BTN_EQUIPAR);
            btn.addActionListener(e -> equiparSkin(ruta));
        } else {
            btn = new JButton("Comprar  " + precio + " pts");
            btn.setBackground(BG_BTN_COMPRA);
            btn.setEnabled(partida.getPuntajeTotal() >= precio);
            btn.addActionListener(e -> comprarYEquipar(ruta, precio));
        }

        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(150, 28));
        fila.add(btn, BorderLayout.EAST);

        return fila;
    }


    //ACCIONES
    /**
     * Intenta comprar la skin y si la compra le sale bien, la equipa automaticamente. 
     * PRE: ruta != null, precio >= 0
     * POST: si hay puntos suficientes, la skin es comprada, equipada y cambio en el momento del personaje
     * si no hay puntos, dialogo de advertencia, no pasa nada en el personaje
     */

    private void comprarYEquipar(String ruta, int precio) {
        if (partida.comprarSkin(ruta, precio)) {
            equiparSkin(ruta);
        } else {
            JOptionPane.showMessageDialog(
                this,
                "No tenes suficientes puntos para comprar esta skin.\n"
                + "Necesitas " + precio + " pts. Tenes " + partida.getPuntajeTotal() + " pts.",
                "NOT ENOUGH CASH, STRANGER",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    /**
     * Equipa la skin indicada, actualiza el modelo y aplica el cambio visual en el personaje
     * 
     * PRE: ruta != null y la skin yatiene que estar en skinsDesbloqueados
     * POST: skinActual en PartidaGeneral actualizado, el sprite cambia en el prox frame del loop
     *       como tenemos 60 fps, es instantaneo. 
     *       la UI del dialogo se reconstruye para reflejar el nuevo estado
     */
    private void equiparSkin(String ruta) {
        partida.setSkinActual(ruta);
        vista.cambiarSkinJugador(ruta);
        reconstruirUI();
    }

    /**
     * Reconstruye el contenido del dialogo para reflejar el estado actual
     * skin equipada y puntos actualizados despues de haber comprado
     */
    private void reconstruirUI() {
        getContentPane().removeAll();
        construirUI();
        revalidate();
        repaint();
    }



    //UTILIDADES
    /**
     * indica si una skin tiene sus sprites cargador en el classpath
     * se chequea un archivo representativo (_down_1.bmp) si existe, voy a asumir que todo
     * el set esta presente
     * 
     * @param ruta ruta base de la skin
     * @return true si el sprite existe y se puede comprar o equipar
     */
    private boolean skinDisponible(String ruta) {
        return getClass().getResourceAsStream(ruta + "_down_1.bmp") != null;
    }

    /**
     * Deriva el nombre visible a partir del nombre base de la skin
     * (primera letra en mayuscula). Ej: "dinosaur" -> "Dinosaur".
     * para mas elegancia.
     */
    private String capitalizar(String base) {
        if (base == null || base.isEmpty()) {
            return base;
        }
        return base.substring(0, 1).toUpperCase() + base.substring(1);
    }




}
