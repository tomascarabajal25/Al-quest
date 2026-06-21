package juego.ciudades.grafos.controller;

import juego.ciudades.grafos.model.*;
import juego.ciudades.grafos.view.*;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CiudadGrafos {
    private final PartidaGrafos partidaGrafos;
    private final GrafoFlujo modelo;
    private final VentanaGrafos ventana;
    private Timer timerAutoPlay;

    private List<?> pasosActuales;
    private int pasoActual;
    private boolean esFlujo;
    private boolean ciudadCompletada;
    private final SimulacionChecker simulacion;

    public CiudadGrafos(PartidaGrafos partidaGrafos) {
        this.partidaGrafos = partidaGrafos;
        this.modelo = new GrafoFlujo();
        this.ventana = new VentanaGrafos();
        // Si el usuario cierra la ventana, asegurarnos de finalizar la partida y detener cualquier recurso
        this.ventana.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (partidaGrafos != null) {
                    partidaGrafos.finalizar();
                }
            }
        });
        this.pasoActual = -1;
        this.ciudadCompletada = false;
        this.simulacion = new SimulacionChecker(() -> completarCiudad());
        configurarListeners();
    }

    public void iniciar() {
        ventana.mostrar();
    }

    private void configurarListeners() {
        PanelEntrada entrada = ventana.getPanelEntrada();
        PanelAlgoritmo alg = ventana.getPanelAlgoritmo();

        entrada.getBtnAgregarVertice().addActionListener(e -> agregarVertice());
        entrada.getBtnAgregarArista().addActionListener(e -> agregarArista());
        entrada.getBtnCargarLista().addActionListener(e -> cargarListaAdyacencia());
        entrada.getBtnLimpiar().addActionListener(e -> limpiarTodo());

        alg.getBtnFlujoMaximo().addActionListener(e -> resolverFlujo());
        alg.getBtnCaminoMinimo().addActionListener(e -> resolverCamino());

        alg.getBtnPasoSiguiente().addActionListener(e -> avanzarPaso());
        alg.getBtnPasoAnterior().addActionListener(e -> retrocederPaso());
        alg.getBtnAutoPlay().addActionListener(e -> iniciarAutoPlay());
        alg.getBtnDetener().addActionListener(e -> detenerAutoPlay());
    }

    private void agregarVertice() {
        String nombre = ventana.getPanelEntrada().getVertice();
        if (nombre.isEmpty()) return;

        modelo.agregarVertice(nombre);
        ventana.getPanelEntrada().actualizarCombos(modelo.getVertices());
        ventana.getPanelEntrada().limpiarCampos();
        ventana.getPanelGrafo().setModelo(modelo);
        ventana.getPanelAlgoritmo().setEstado("Construyendo grafo (" + modelo.getVertices().size() + " vertices)");
    }

    private void agregarArista() {
        PanelEntrada entrada = ventana.getPanelEntrada();
        String origen = entrada.getOrigen();
        String destino = entrada.getDestino();
        int capacidad = entrada.getCapacidad();

        if (origen.isEmpty() || destino.isEmpty() || capacidad <= 0) {
            JOptionPane.showMessageDialog(ventana, "Ingresa origen, destino y capacidad valida (>0)",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!modelo.getGrafo().existeVertice(origen) || !modelo.getGrafo().existeVertice(destino)) {
            JOptionPane.showMessageDialog(ventana, "Los vertices deben existir en el grafo",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        modelo.agregarArista(origen, destino, capacidad);
        entrada.limpiarCampos();
        ventana.getPanelGrafo().setModelo(modelo);
    }

    private void cargarListaAdyacencia() {
        String texto = ventana.getPanelEntrada().getListaAdyacencia();
        if (texto.isEmpty()) return;

        String[] lineas = texto.split("\n");
        int errores = 0;
        int aristasCargadas = 0;

        for (String linea : lineas) {
            String limpia = linea.trim();
            if (limpia.isEmpty()) continue;

            limpia = limpia.replace("[", "").replace("]", "");
            String[] partes = limpia.split(",");

            if (partes.length != 3) {
                errores++;
                continue;
            }

            String origen = partes[0].trim();
            String destino = partes[1].trim();
            int peso;

            try {
                peso = Integer.parseInt(partes[2].trim());
            } catch (NumberFormatException e) {
                errores++;
                continue;
            }

            if (origen.isEmpty() || destino.isEmpty() || peso <= 0) {
                errores++;
                continue;
            }

            if (!modelo.getGrafo().existeVertice(origen)) {
                modelo.agregarVertice(origen);
            }
            if (!modelo.getGrafo().existeVertice(destino)) {
                modelo.agregarVertice(destino);
            }

            modelo.agregarArista(origen, destino, peso);
            aristasCargadas++;
        }

        ventana.getPanelEntrada().actualizarCombos(modelo.getVertices());
        ventana.getPanelGrafo().setModelo(modelo);
        ventana.getPanelAlgoritmo().setEstado("Construyendo grafo (" + modelo.getVertices().size() + " vertices)");

        if (errores > 0) {
            JOptionPane.showMessageDialog(ventana,
                    errores + " linea(s) con formato incorrecto.\nUsa: [origen, destino, peso]",
                    "Error de formato", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void limpiarTodo() {
        detenerAutoPlay();
        modelo.reset();
        pasosActuales = null;
        pasoActual = -1;
        simulacion.reset();

        ventana.getPanelEntrada().actualizarCombos(modelo.getVertices());
        ventana.getPanelEntrada().limpiarCampos();
        ventana.getPanelEntrada().setEnabled(true);
        ventana.getPanelGrafo().setModelo(modelo);
        ventana.getPanelGrafo().limpiarResaltado();
        ventana.getPanelAlgoritmo().setNavegacionHabilitada(false);
        ventana.getPanelAlgoritmo().setAlgoritmosHabilitados(true);
        ventana.getPanelAlgoritmo().setEstado("Construyendo grafo");
        ventana.getPanelResultado().limpiar();
    }

    private void resolverFlujo() {
        String fuente = ventana.getPanelEntrada().getFuente();
        String sumidero = ventana.getPanelEntrada().getSumidero();

        if (fuente == null || sumidero == null || fuente.equals(sumidero)) {
            JOptionPane.showMessageDialog(ventana, "Selecciona fuente y sumidero distintos",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        modelo.setFuente(fuente);
        modelo.setSumidero(sumidero);

        ResultadoFlujo resultado = modelo.resolverFlujoMaximo();
        if (resultado == null || resultado.getPasos().isEmpty()) {
            JOptionPane.showMessageDialog(ventana, "No se encontro camino aumentante",
                    "Resultado", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        pasosActuales = resultado.getPasos();
        pasoActual = -1;
        esFlujo = true;

        ventana.getPanelResultado().limpiar();
        ventana.getPanelResultado().setResultado("Flujo Maximo: " + resultado.getFlujoMaximo());
        ventana.getPanelResultado().agregarLinea("=== Ford-Fulkerson ===");
        ventana.getPanelResultado().agregarLinea("Fuente: " + fuente + " | Sumidero: " + sumidero);

        for (int i = 0; i < resultado.getPasos().size(); i++) {
            ventana.getPanelResultado().agregarLinea("Paso " + (i + 1) + ": " + resultado.getPasos().get(i).getDescripcion());
        }
        ventana.getPanelResultado().agregarLinea("Flujo maximo = " + resultado.getFlujoMaximo());

        ventana.getPanelEntrada().setEnabled(false);
        ventana.getPanelAlgoritmo().setNavegacionHabilitada(true);
        ventana.getPanelAlgoritmo().setEstado("Flujo maximo resuelto - Navega los pasos");
    }

    private void resolverCamino() {
        PanelAlgoritmo alg = ventana.getPanelAlgoritmo();
        String origen = alg.getOrigenCamino();
        String destino = alg.getDestinoCamino();

        if (origen.isEmpty() || destino.isEmpty()) {
            JOptionPane.showMessageDialog(ventana, "Ingresa origen y destino para Dijkstra",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!modelo.getGrafo().existeVertice(origen) || !modelo.getGrafo().existeVertice(destino)) {
            JOptionPane.showMessageDialog(ventana, "Los vertices deben existir en el grafo",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ResultadoCamino resultado = modelo.resolverCaminoMinimo(origen, destino);
        if (resultado == null) {
            JOptionPane.showMessageDialog(ventana, "Error al resolver el camino",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        pasosActuales = resultado.getPasos();
        pasoActual = -1;
        esFlujo = false;

        ventana.getPanelResultado().limpiar();
        if (resultado.getCamino().isEmpty()) {
            ventana.getPanelResultado().setResultado("No existe camino de " + origen + " a " + destino);
        } else {
            ventana.getPanelResultado().setResultado("Camino minimo: "
                    + String.join(" → ", resultado.getCamino())
                    + " | Costo: " + resultado.getCostoTotal());
        }

        ventana.getPanelResultado().agregarLinea("=== Dijkstra ===");
        ventana.getPanelResultado().agregarLinea("Origen: " + origen + " | Destino: " + destino);

        for (int i = 0; i < resultado.getPasos().size(); i++) {
            ventana.getPanelResultado().agregarLinea("Paso " + (i + 1) + ": " + resultado.getPasos().get(i).getDescripcion());
        }

        if (!resultado.getCamino().isEmpty()) {
            ventana.getPanelResultado().agregarLinea("Camino: " + String.join(" → ", resultado.getCamino()));
            ventana.getPanelResultado().agregarLinea("Costo total: " + resultado.getCostoTotal());
        }

        ventana.getPanelEntrada().setEnabled(false);
        ventana.getPanelAlgoritmo().setNavegacionHabilitada(true);
        ventana.getPanelAlgoritmo().setEstado("Camino minimo resuelto - Navega los pasos");
    }

    private void avanzarPaso() {
        if (pasosActuales == null) return;
        if (pasoActual < pasosActuales.size() - 1) {
            pasoActual++;
            mostrarPasoActual();
        }
        if (pasoActual == pasosActuales.size() - 1) {
            mostrarResultadoFinal();
            if (esFlujo) {
                simulacion.simularFlujo();
            } else {
                simulacion.simularCamino();
            }
        }
    }

    private void retrocederPaso() {
        if (pasosActuales == null) return;
        if (pasoActual > 0) {
            pasoActual--;
            mostrarPasoActual();
        } else if (pasoActual == 0) {
            pasoActual = -1;
            ventana.getPanelGrafo().limpiarResaltado();
        }
    }

    private void mostrarPasoActual() {
        if (esFlujo) {
            @SuppressWarnings("unchecked")
            List<PasoFlujo> pasos = (List<PasoFlujo>) pasosActuales;
            ventana.getPanelGrafo().resaltarPasoFlujo(pasos.get(pasoActual));
        } else {
            @SuppressWarnings("unchecked")
            List<PasoCamino> pasos = (List<PasoCamino>) pasosActuales;
            ventana.getPanelGrafo().resaltarPasoCamino(pasos.get(pasoActual));
        }
        ventana.getPanelAlgoritmo().setEstado("Paso " + (pasoActual + 1) + " de " + pasosActuales.size());
    }

    private void mostrarResultadoFinal() {
        if (!esFlujo) {
            @SuppressWarnings("unchecked")
            List<PasoCamino> pasos = (List<PasoCamino>) pasosActuales;
            if (!pasos.isEmpty()) {
                ResultadoCamino res = modelo.resolverCaminoMinimo(
                        ventana.getPanelAlgoritmo().getOrigenCamino(),
                        ventana.getPanelAlgoritmo().getDestinoCamino());
                if (res != null && !res.getCamino().isEmpty()) {
                    ventana.getPanelGrafo().resaltarCaminoFinal(res.getCamino());
                }
            }
        }
    }

    private void iniciarAutoPlay() {
        if (pasosActuales == null) return;
        pasoActual = -1;
        ventana.getPanelGrafo().limpiarResaltado();
        ventana.getPanelAlgoritmo().setAutoPlayActivo(true);

        int delay = ventana.getPanelAlgoritmo().getVelocidad();
        timerAutoPlay = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pasoActual < pasosActuales.size() - 1) {
                    avanzarPaso();
                } else {
                    detenerAutoPlay();
                    mostrarResultadoFinal();
                }
            }
        });
        timerAutoPlay.start();
    }

    public void detenerAutoPlay() {
        if (timerAutoPlay != null && timerAutoPlay.isRunning()) {
            timerAutoPlay.stop();
        }
        ventana.getPanelAlgoritmo().setAutoPlayActivo(false);
    }

    private void completarCiudad() {
        if (ciudadCompletada) return;
        ciudadCompletada = true;
        detenerAutoPlay();
        JOptionPane.showMessageDialog(ventana,
                "¡Completaste la Ciudad de Grafos!\nPuntaje: 200",
                "Ciudad Completada", JOptionPane.INFORMATION_MESSAGE);
        partidaGrafos.finalizar();
    }

    public GrafoFlujo getModelo() {
        return modelo;
    }

    public VentanaGrafos getVentana() {
        return ventana;
    }
}
