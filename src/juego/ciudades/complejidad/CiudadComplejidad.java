package juego.ciudades.complejidad;

import java.util.List;

public class CiudadComplejidad {

    private final ParserEcuacion parser;
    private final SolverTeoremaMaestro solver;
    private EcuacionRecurrencia ecuacion;
    private List<PasoTeoremaMaestro> pasos;
    private String resultado;

    /**
     * Crea una nueva ciudad de complejidad algorítmica.
     */
    public CiudadComplejidad() {
        this.parser = new ParserEcuacion();
        this.solver = new SolverTeoremaMaestro();
    }

    /**
     * Procesa la ecuación ingresada por el usuario.
     * Si el formato es inválido devuelve false.
     *
     * @param entrada string con la ecuación, ej: "T(n) = 2T(n/2) + n"
     * @return true si la ecuación es válida y fue procesada, false si no
     */
    public boolean procesarEcuacion(String entrada) {
        ecuacion = parser.parsear(entrada);
        if (ecuacion == null) return false;

        pasos = solver.resolver(ecuacion);
        resultado = solver.getResultado(ecuacion);
        return true;
    }

    /** @return complejidad resultante como string, ej: "O(n log n)" */
    public String getResultado() {
        return resultado;
    }

    /**
     * @return lista de pasos del teorema maestro, o null si no se procesó ninguna ecuación
     */
    public List<PasoTeoremaMaestro> getPasos() {
        return pasos;
    }

    /**
     * @return la ecuación procesada, o null si no se procesó ninguna
     */
    public EcuacionRecurrencia getEcuacion() {
        return ecuacion;
    }

    /**
     * Verifica si el string tiene el formato correcto.
     *
     * @param entrada string a verificar
     * @return true si el formato es válido
     */
    public boolean esEntradaValida(String entrada) {
        return parser.esValido(entrada);
    }
}
