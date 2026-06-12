package juego.ciudades.grafos.view;

import juego.ciudades.grafos.model.GrafoFlujo;
import juego.ciudades.grafos.model.PasoCamino;
import juego.ciudades.grafos.model.PasoFlujo;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

public class PanelGrafo extends JPanel {
    private static final int RADIO_VERTICE = 25;
    private static final int TAMANO_FLECHA = 12;

    private GrafoFlujo modelo;
    private Map<String, Point> posiciones;
    private Set<String> verticesResaltados;
    private Set<String> aristasResaltadas;
    private int[][] flujoActual;
    private Color colorResalte;

    public PanelGrafo() {
        this.posiciones = new HashMap<>();
        this.verticesResaltados = new HashSet<>();
        this.aristasResaltadas = new HashSet<>();
        this.colorResalte = Color.RED;
        setBackground(new Color(30, 30, 30));
        setPreferredSize(new Dimension(600, 500));
    }

    public void setModelo(GrafoFlujo modelo) {
        this.modelo = modelo;
        calcularPosicionesCirculares();
        repaint();
    }

    public void calcularPosicionesCirculares() {
        posiciones.clear();
        if (modelo == null) return;

        List<String> verts = modelo.getVertices();
        int n = verts.size();
        if (n == 0) return;

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        int radio = Math.min(cx, cy) - 60;
        if (radio < 80) radio = 80;

        for (int i = 0; i < n; i++) {
            double angulo = 2 * Math.PI * i / n - Math.PI / 2;
            int x = (int) (cx + radio * Math.cos(angulo));
            int y = (int) (cy + radio * Math.sin(angulo));
            posiciones.put(verts.get(i), new Point(x, y));
        }
    }

    public void resaltarPasoFlujo(PasoFlujo paso) {
        verticesResaltados.clear();
        aristasResaltadas.clear();
        colorResalte = new Color(255, 80, 80);

        if (paso != null) {
            List<String> camino = paso.getCamino();
            verticesResaltados.addAll(camino);
            for (int i = 0; i < camino.size() - 1; i++) {
                aristasResaltadas.add(camino.get(i) + "→" + camino.get(i + 1));
            }
            flujoActual = paso.getFlujoDespues();
        }
        repaint();
    }

    public void resaltarPasoCamino(PasoCamino paso) {
        verticesResaltados.clear();
        aristasResaltadas.clear();
        colorResalte = new Color(80, 150, 255);

        if (paso != null) {
            verticesResaltados.add(paso.getVerticeActual());
            verticesResaltados.addAll(paso.getVisitados());
            if (paso.getAristaRelajada() != null && paso.getAristaRelajada().size() == 2) {
                aristasResaltadas.add(paso.getAristaRelajada().get(0) + "→" + paso.getAristaRelajada().get(1));
            }
        }
        repaint();
    }

    public void resaltarCaminoFinal(List<String> camino) {
        verticesResaltados.clear();
        aristasResaltadas.clear();
        colorResalte = new Color(80, 255, 80);

        if (camino != null) {
            verticesResaltados.addAll(camino);
            for (int i = 0; i < camino.size() - 1; i++) {
                aristasResaltadas.add(camino.get(i) + "→" + camino.get(i + 1));
            }
        }
        repaint();
    }

    public void limpiarResaltado() {
        verticesResaltados.clear();
        aristasResaltadas.clear();
        flujoActual = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (modelo == null) {
            g.setColor(Color.GRAY);
            g.setFont(new Font("SansSerif", Font.PLAIN, 14));
            g.drawString("Agrega vertices y aristas para construir el grafo", 150, getHeight() / 2);
            return;
        }

        calcularPosicionesCirculares();
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        dibujarAristas(g2);
        dibujarVertices(g2);
    }

    private void dibujarAristas(Graphics2D g2) {
        List<String> verts = modelo.getVertices();
        int n = verts.size();

        for (int i = 0; i < n; i++) {
            String u = verts.get(i);
            Point pu = posiciones.get(u);
            if (pu == null) continue;

            for (estructuras.grafos.Arista<String, Integer> arista : modelo.getGrafo().getAdyacentes(u)) {
                String v = arista.getDestino().getValor();
                Point pv = posiciones.get(v);
                if (pv == null) continue;

                String clave = u + "→" + v;
                boolean resaltada = aristasResaltadas.contains(clave);

                g2.setStroke(resaltada ? new BasicStroke(3f) : new BasicStroke(1.5f));
                g2.setColor(resaltada ? colorResalte : new Color(180, 180, 180));

                Point2D pInicio = puntoEnBorde(pu, pv, RADIO_VERTICE);
                Point2D pFin = puntoEnBorde(pv, pu, RADIO_VERTICE);

                g2.draw(new Line2D.Double(pInicio, pFin));
                dibujarFlecha(g2, pInicio, pFin);

                String etiqueta = String.valueOf(arista.getPeso());
                if (flujoActual != null) {
                    int fi = modelo.indiceDe(u);
                    int fj = modelo.indiceDe(v);
                    if (fi >= 0 && fj >= 0) {
                        etiqueta = flujoActual[fi][fj] + "/" + arista.getPeso();
                    }
                }

                Point2D medio = puntoMedio(pInicio, pFin);
                g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                g2.setColor(resaltada ? colorResalte : Color.WHITE);
                g2.drawString(etiqueta, (int) medio.getX() + 5, (int) medio.getY() - 5);
            }
        }
    }

    private void dibujarVertices(Graphics2D g2) {
        for (String v : modelo.getVertices()) {
            Point p = posiciones.get(v);
            if (p == null) continue;

            boolean resaltado = verticesResaltados.contains(v);
            boolean esFuente = v.equals(modelo.getFuente());
            boolean esSumidero = v.equals(modelo.getSumidero());

            if (resaltado) {
                g2.setColor(colorResalte);
            } else if (esFuente) {
                g2.setColor(new Color(80, 200, 80));
            } else if (esSumidero) {
                g2.setColor(new Color(200, 80, 80));
            } else {
                g2.setColor(new Color(60, 60, 60));
            }

            g2.fillOval(p.x - RADIO_VERTICE, p.y - RADIO_VERTICE, RADIO_VERTICE * 2, RADIO_VERTICE * 2);

            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2f));
            g2.drawOval(p.x - RADIO_VERTICE, p.y - RADIO_VERTICE, RADIO_VERTICE * 2, RADIO_VERTICE * 2);

            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            FontMetrics fm = g2.getFontMetrics();
            int tx = p.x - fm.stringWidth(v) / 2;
            int ty = p.y + fm.getAscent() / 2 - 2;
            g2.drawString(v, tx, ty);
        }
    }

    private void dibujarFlecha(Graphics2D g2, Point2D inicio, Point2D fin) {
        double angulo = Math.atan2(fin.getY() - inicio.getY(), fin.getX() - inicio.getX());

        int x1 = (int) (fin.getX() - TAMANO_FLECHA * Math.cos(angulo - Math.PI / 6));
        int y1 = (int) (fin.getY() - TAMANO_FLECHA * Math.sin(angulo - Math.PI / 6));
        int x2 = (int) (fin.getX() - TAMANO_FLECHA * Math.cos(angulo + Math.PI / 6));
        int y2 = (int) (fin.getY() - TAMANO_FLECHA * Math.sin(angulo + Math.PI / 6));

        int[] xPoints = {(int) fin.getX(), x1, x2};
        int[] yPoints = {(int) fin.getY(), y1, y2};
        g2.fillPolygon(xPoints, yPoints, 3);
    }

    private Point2D puntoEnBorde(Point centro, Point hacia, int radio) {
        double dx = hacia.x - centro.x;
        double dy = hacia.y - centro.y;
        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist == 0) return new Point2D.Double(centro.x, centro.y);
        return new Point2D.Double(
                centro.x + radio * dx / dist,
                centro.y + radio * dy / dist
        );
    }

    private Point2D puntoMedio(Point2D a, Point2D b) {
        return new Point2D.Double((a.getX() + b.getX()) / 2, (a.getY() + b.getY()) / 2);
    }
}
