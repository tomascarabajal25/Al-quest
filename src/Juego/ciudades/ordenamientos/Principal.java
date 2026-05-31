package Juego.ciudades.ordenamientos;

import java.util.List;

import Juego.ciudades.ordenamientos.ui.RecursosOrdenamiento;
import modelos.Jugador;

public class Principal {

    public static void main(String[] args) {

        Jugador jugador = new Jugador("Tester");

        List<Caja> cajas = List.of(
                new Caja("A", 40),
                new Caja("B", 10),
                new Caja("C", 30),
                new Caja("D", 25),
                new Caja("E", 35)
        );

        Ordenador<Caja> ordenador = new OrdenadorBubble<>("Bubble Sort");

        RecursosOrdenamiento rec = new RecursosOrdenamiento();

        PartidaOrdenamientos<Caja> partida = new PartidaOrdenamientos<>(
                "Ciudad Ordenamientos",
                jugador,
                cajas,
                ordenador,

                // Lambda 1 — CÓMO DIBUJAR: calcula alto proporcional al tamaño
                (g, caja, lista, x, yBase, ancho, altoMaximo, destacado) -> {
                    // Encontramos el tamaño máximo de la lista para escalar
                    int maxTam = lista.stream()
                            .mapToInt(c -> ((Caja) c).getTamaño())
                            .max().orElse(1);
                    int altoReal = (caja.getTamaño() * altoMaximo) / maxTam;
                    altoReal = Math.max(altoReal, 20); // mínimo visible

                    int yDibujo = yBase - altoReal;

                    g.drawImage(
                        destacado ? rec.getCajaRoja().getImage()
                                  : rec.getCajaNormal().getImage(),
                        x, yDibujo, ancho, altoReal, null
                    );
                    return altoReal;
                },

                // Lambda 2 — ETIQUETA: texto legible debajo de cada caja
                caja -> caja.getNombre() + " (" + caja.getTamaño() + ")"
        );

        partida.iniciar();
    }
}