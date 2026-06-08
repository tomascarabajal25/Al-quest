package ciudad_3_laberinto;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.http.WebSocket.Listener;

import javax.swing.JOptionPane;
import javax.swing.Timer;

public class ControladorLaberinto {
    private static final String RUTA_LABERINTO = "recursos/laberinto.txt";
    private static final String RUTA_IMAGENES = "partidas/laberinto";
    private static final int INTERVALO_TIMER = 300;

    private Laberinto laberinto;

    private BacktrackingLaberinto backtracking;

    private GestorImagenes gestorImagenes;

    private PanelLaberinto panelLaberinto;

    private VentanaLaberinto ventana;

    private Timer timer;

    private boolean pausado;

    public ControladorLaberinto() {
        pausado = false;
        if (!cargarLaberinto()) {
            return;
        }
        inicializarComponentes();
        configurarTimer();
        configurarBotones();
        ventana.mostrar();
    }

    private boolean cargarLaberinto() {
        try {
            CargadorLaberinto cargador = new CargadorLaberinto();
            laberinto = cargador.cargar(RUTA_LABERINTO);
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                null,
                "No se pudo cargar el laberinto:\n" +e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }

    private void inicializarComponentes() {
        backtracking = new BacktrackingLaberinto(laberinto);
        gestorImagenes =  new GestorImagenes(RUTA_IMAGENES);
        panelLaberinto = new PanelLaberinto(laberinto);
        ventana = new VentanaLaberinto(panelLaberinto);
    }

    private void configurarTimer() {
        timer = new Timer(INTERVALO_TIMER, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                avanzarPaso();
            }
        });
    }

    private void avanzarPaso() {
        ResultadoPaso resultado = backtracking.avanzarPaso();

        panelLaberinto.repaint();
        ventana.setLabelPaso(gestorImagenes.getNumeroPaso() + 1);

        try {
            gestorImagenes.guardarPaso(laberinto);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(ventana,
            "No se pudo guardar la imagen: \n" + e.getMessage(),
            "Error",
            JOptionPane.WARNING_MESSAGE
            );
        }

        if (resultado == ResultadoPaso.SOLUCION_ENCONTRADA) {
            timer.stop();
            ventana.setLabelEstado("Solucion encontrada");
            ventana.getBotonIniciar().setEnabled(false);
            ventana.getBotonPausar().setEnabled(false);
            JOptionPane.showMessageDialog(
                ventana,
                "Laberinto resuelto en " + gestorImagenes.getNumeroPaso() + " pasos.",
                "Solucion encontrada",
            JOptionPane.INFORMATION_MESSAGE
            );
        } else if (resultado == ResultadoPaso.SIN_SOLUCION) {
            timer.stop();
            ventana.setLabelEstado("Sin solucion");
            ventana.getBotonIniciar().setEnabled(false);
            ventana.getBotonPausar().setEnabled(false);
            JOptionPane.showMessageDialog(
                ventana,
                "El laberinto no tiene solucion.",
                "Sin solucion",
                JOptionPane.WARNING_MESSAGE
            );
        } else {
            ventana.setLabelEstado("En progreso...");
        }
    }

    private void configurarBotones() {
        ventana.getBotonIniciar().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                accionIniciar();
            }
        });
        
        ventana.getBotonPausar().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                accionPausar();
            }
        });
    
        ventana.getBotonResetear().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                accionResetear();
            }
        });
    }

    private void accionIniciar() {
        timer.start();
        ventana.setLabelEstado("En progreso...");
        ventana.getBotonIniciar().setEnabled(false);
        ventana.getBotonPausar().setEnabled(true);
        ventana.getBotonResetear().setEnabled(true);
    }

    private void accionPausar() {
        if (pausado) {
            timer.start();
            pausado = false;
            ventana.getBotonPausar().setText("Pausar");
            ventana.setLabelEstado("En progreso...");
        } else {
            timer.stop();
            pausado = true;
            ventana.getBotonPausar().setText("Reanudar");
            ventana.setLabelEstado("Pausado");
        }
    }

    private void accionResetear() {
        timer.stop();
        pausado = false;

        laberinto.reiniciarLaberinto();
        backtracking = new BacktrackingLaberinto(laberinto);
        gestorImagenes.resetearContador();

        panelLaberinto.repaint();
        ventana.setLabelEstado("Listo");
        ventana.setLabelPaso(0);
        ventana.getBotonIniciar().setEnabled(true);
        ventana.getBotonPausar().setEnabled(false);
        ventana.getBotonPausar().setText("Pausar");
        ventana.getBotonResetear().setEnabled(false);
    }
}
