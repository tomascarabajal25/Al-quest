package juego.ciudades.complejidad;

import java.util.Objects;

public class EcuacionRecurrencia {

    private int a;      // cantidad de subproblemas
    private int b;      // factor de división
    private String fn;  // función de costo, ej: "n", "n^2", "1"

    /**
     * @param a cantidad de subproblemas
     * @param b factor de división
     * @param fn función de costo como string
     */
    public EcuacionRecurrencia(int a, int b, String fn) {
        this.a = a;
        this.b = b;
        this.fn = fn;
    }

    public int getA() {
        return a;
    }
    public int getB() {
        return b;
    }
    public String getFn() {
        return fn;
    }

    @Override
    public String toString() {
        return "T(n) = " + a + "T(n/" + b + ") + " + fn;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EcuacionRecurrencia ec = (EcuacionRecurrencia) o;
        return a == ec.a && b == ec.b && Objects.equals(fn, ec.fn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, fn);
    }
}
