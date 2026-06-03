package com.aiquest.utils.bitmap;


import com.aiquest.utils.ValidacionesUtiles;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class BitmapViewerConMenu {

    private JFrame frame;
    private JPanel panel;
    private JPanel menuPanel;
    private List<Bitmap> bitmaps;

    // Nueva lista de acciones del menú
    private List<MenuAction> menuActions;

    /**
     * Constructor privado
     */
    public BitmapViewerConMenu(List<Bitmap> bitmaps, List<MenuAction> menuActions) {
        ValidacionesUtiles.esDistintoDeNull(bitmaps, "bitmaps");
        ValidacionesUtiles.validarMayorACero(bitmaps.size(), "bitmaps");

        this.bitmaps = bitmaps;
        this.menuActions = menuActions != null ? menuActions : List.of();

        SwingUtilities.invokeLater(this::createAndShowGUI);
    }

    /**
     * Muestra bitmaps sin menú
     */
    public static void showBitmaps(Bitmap... bitmaps) {
        new BitmapViewerConMenu(Arrays.asList(bitmaps), List.of());
    }

    /**
     * Muestra bitmaps + menú con acciones
     */
    public static void showBitmapsWithMenu(List<MenuAction> actions, Bitmap... bitmaps) {
        new BitmapViewerConMenu(Arrays.asList(bitmaps), actions);
    }

    /**
     * Crea la UI
     */
    private void createAndShowGUI() {
        frame = new JFrame("Visualizador de Bitmaps");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Panel con imágenes
        panel = new JPanel(new GridLayout(0, 3, 10, 10));

        for (Bitmap bmp : bitmaps) {
            JLabel label = new JLabel(new ImageIcon(bmp.getImage()));
            panel.add(label);
        }

        frame.add(new JScrollPane(panel), BorderLayout.CENTER);

        // ======================
        //   PANEL DE BOTONES
        // ======================
        menuPanel = new JPanel(new FlowLayout());

        for (MenuAction action : menuActions) {
            JButton btn = new JButton(action.getLabel());
            btn.addActionListener(e -> action.getRunnable().run());
            menuPanel.add(btn);
        }

        frame.add(menuPanel, BorderLayout.SOUTH);

        frame.setSize(1000, 800);
        frame.setVisible(true);

        // Refresca automáticamente cada 500 ms
        new Timer(500, e -> refreshImages()).start();
    }

    /**
     * Actualiza las imágenes
     */
    private void refreshImages() {
        Component[] comps = panel.getComponents();
        for (int i = 0; i < comps.length; i++) {
            if (comps[i] instanceof JLabel lbl) {
                lbl.setIcon(new ImageIcon(bitmaps.get(i).getImage()));
            }
        }
    }

    // -----------------------------------------------------
    // Clase para representar una acción de menú
    // -----------------------------------------------------
    public static class MenuAction {
        private final String label;
        private final Runnable runnable;

        public MenuAction(String label, Runnable runnable) {
            this.label = label;
            this.runnable = runnable;
        }

        public String getLabel() {
            return label;
        }

        public Runnable getRunnable() {
            return runnable;
        }
    }

	
}
