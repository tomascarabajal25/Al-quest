package juego.ciudades.complejidad;

import java.util.ArrayList;
import java.util.List;

public class SolverTeoremaMaestro {

    private static final double EPSILON = 1e-9;

    /**
     * Resuelve la ecuación de recurrencia aplicando el teorema maestro.
     * Graba cada paso del proceso.
     *
     * @param ec ecuación de recurrencia a resolver
     * @return lista de pasos con el proceso y resultado
     */
    public List<PasoTeoremaMaestro> resolver(EcuacionRecurrencia ec) {
        List<PasoTeoremaMaestro> pasos = new ArrayList<>();

        int a = ec.getA();
        int b = ec.getB();
        String fn = ec.getFn();

        // paso 1: mostrar la ecuación
        pasos.add(new PasoTeoremaMaestro("Ecuación: " + ec.toString()));

        // paso 2: calcular log_b(a)
        double logBA = Math.log(a) / Math.log(b);
        pasos.add(new PasoTeoremaMaestro("Calcular log_" + b + "(" + a + ") = " + String.format("%.2f", logBA)));

        // paso 3: identificar exponente de f(n)
        double expFn = obtenerExponente(fn);
        pasos.add(new PasoTeoremaMaestro("Exponente de f(n) = " + fn + " es: " + expFn));

        // paso 4: comparar y determinar caso
        if (expFn < logBA - EPSILON) {
            pasos.add(new PasoTeoremaMaestro("f(n) crece más lento que n^" + String.format("%.2f", logBA)));
            pasos.add(new PasoTeoremaMaestro("→ Caso 1: T(n) = Θ(n^" + String.format("%.2f", logBA) + ")"));

        } else if (Math.abs(expFn - logBA) <= EPSILON) {
            pasos.add(new PasoTeoremaMaestro("f(n) crece igual que n^" + String.format("%.2f", logBA)));

            if (Math.abs(logBA - 1) < EPSILON) {
                pasos.add(new PasoTeoremaMaestro("→ Caso 2: T(n) = Θ(n log n)"));
            
            } else if (Math.abs(logBA - 2) < EPSILON) {
                pasos.add(new PasoTeoremaMaestro("→ Caso 2: T(n) = Θ(n^2 log n)"));
            
            } else {
                pasos.add(new PasoTeoremaMaestro("→ Caso 2: T(n) = Θ(n^" + String.format("%.0f", logBA) + " log n)"));
            }
        }

        return pasos;
    }

    /**
     * Extrae el exponente de f(n) para compararlo con log_b(a).
     * Soporta: "1" → 0, "n" → 1, "n^k" → k, "n*log(n)" → 1 (caso especial)
     *
     * @param fn función de costo como string
     * @return exponente como double
     */
    private double obtenerExponente(String fn) {
        fn = fn.trim();

        if (fn.equals("1")) {
            return 0;
        }
        if (fn.equals("n")) {
            return 1;
        }
        if (fn.startsWith("n^")) {
            return Double.parseDouble(fn.substring(2));
        }
        
        if (fn.contains("log")) {   // n*log(n) tratado como caso especial
            return 1;
        } 

        return 0; // default
    }

    /**
     * Devuelve la complejidad resultante sin grabar pasos.
     *
     * @param ec ecuación de recurrencia
     * @return complejidad como string
     */
    public String getResultado(EcuacionRecurrencia ec) {
        double logBA = Math.log(ec.getA()) / Math.log(ec.getB());
        double expFn = obtenerExponente(ec.getFn());

        if (expFn < logBA - EPSILON) {          // Caso 1
            return "O(n^" + String.format("%.0f", logBA) + ")";

        } else if (Math.abs(expFn - logBA) <= EPSILON) {  // Caso 2
            if (Math.abs(logBA - 1) < EPSILON) {
                return "O(n log n)";
            }
            
            if (Math.abs(logBA - 2) < EPSILON) {
                return "O(n^2 log n)";
            }

            return "O(n^" + String.format("%.0f", logBA) + " log n)";

        } else {   // Caso 3
            if (expFn == 1) {
                return "O(n)";
            }
            if (expFn == 2) {
                return "O(n^2)";
            }
            return "O(n^" + String.format("%.0f", expFn) + ")";
        }
    }
}
