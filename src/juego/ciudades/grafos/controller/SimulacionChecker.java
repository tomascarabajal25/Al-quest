package juego.ciudades.grafos.controller;

import javax.swing.Timer;

public class SimulacionChecker {
    private static final int DELAY_MS = 2000;

    private boolean flujoSimulado;
    private boolean caminoSimulado;
    private Timer timerFlujo;
    private Timer timerCamino;
    private final Runnable onCompletado;

    public SimulacionChecker(Runnable onCompletado) {
        this.onCompletado = onCompletado;
        this.flujoSimulado = false;
        this.caminoSimulado = false;
    }

    public void simularFlujo() {
        if (timerFlujo != null && timerFlujo.isRunning()) {
            timerFlujo.stop();
        }
        timerFlujo = new Timer(DELAY_MS, e -> {
            flujoSimulado = true;
            verificarCompletado();
        });
        timerFlujo.setRepeats(false);
        timerFlujo.start();
    }

    public void simularCamino() {
        if (timerCamino != null && timerCamino.isRunning()) {
            timerCamino.stop();
        }
        timerCamino = new Timer(DELAY_MS, e -> {
            caminoSimulado = true;
            verificarCompletado();
        });
        timerCamino.setRepeats(false);
        timerCamino.start();
    }

    public void reset() {
        if (timerFlujo != null) {
            timerFlujo.stop();
        }
        if (timerCamino != null) {
            timerCamino.stop();
        }
        flujoSimulado = false;
        caminoSimulado = false;
    }

    private void verificarCompletado() {
        if (flujoSimulado && caminoSimulado) {
            onCompletado.run();
        }
    }
}