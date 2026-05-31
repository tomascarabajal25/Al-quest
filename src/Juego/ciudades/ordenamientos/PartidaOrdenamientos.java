package Juego.ciudades.ordenamientos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import java.util.function.Function;

import Juego.ciudades.ordenamientos.ui.DibujarElemento;
import Juego.ciudades.ordenamientos.ui.VistaOrdenamiento;
import modelos.Jugador;
import modelos.Partida;
import utils.ValidacionesUtiles;

public class PartidaOrdenamientos<T extends Comparable<T>> extends Partida {

    // ── Atributos ─────────────────────────────────────────────────────────────
    private List<T>                  elementosIniciales;
    private Ordenador<T>             ordenador;
    private AdministradorDePasos<T>  administradorPasos;
    private VistaOrdenamiento<T>     vista;

    private static final int ACIERTOS_PARA_GANAR = 1;
    private int aciertos = 0;
    private int rondas   = 0;

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * Pre:
     * - nombreCiudad no nulo
     * - jugador no nulo
     * - elementos no nulo
     * - ordenador no nulo
     * - dibujador no nulo: lambda que sabe cómo pintar T en el canvas
     * Post:
     * crea la partida y abre la ventana BMP
     */
    public PartidaOrdenamientos(String nombreCiudad,
                                 Jugador jugador,
                                 List<T> elementos,
                                 Ordenador<T> ordenador,
                                 DibujarElemento<T> dibujador,
                                 Function<T, String> etiqueta) {
        super(nombreCiudad, jugador);
        setElementosIniciales(new ArrayList<>(elementos));
        setOrdenador(ordenador);
        setAdministradorDePasos(new AdministradorDePasos<>());
        setVista(new VistaOrdenamiento<>(dibujador, etiqueta));
    }

    // ── Métodos generales ─────────────────────────────────────────────────────

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(elementosIniciales, ordenador);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj == null) { return false; }
        if (!super.equals(obj)) { return false; }
        if (getClass() != obj.getClass()) { return false; }
        PartidaOrdenamientos<?> other = (PartidaOrdenamientos<?>) obj;
        return Objects.equals(elementosIniciales, other.elementosIniciales)
                && Objects.equals(ordenador, other.ordenador);
    }

    @Override
    public String toString() {
        return "PartidaOrdenamientos [elementosIniciales=" + elementosIniciales
                + ", ordenador=" + ordenador + "]";
    }

    // ── Comportamiento ────────────────────────────────────────────────────────

    /**
     * Pre:  la partida no debe estar iniciada
     * Post: ejecuta el ordenamiento, anima la vista, corre las rondas de desafío
     *       y finaliza la partida
     */
    @Override
    public void iniciar() {
        ValidacionesUtiles.validarFalso(estaIniciada(), "La partida ya ha sido iniciada");

        // 1. Ordenar y registrar todos los pasos en el historial
        List<T> copiaDeTrabajo = new ArrayList<>(this.elementosIniciales);
        setEstado(EstadoDePartida.Iniciado);
        ordenador.ordenar(copiaDeTrabajo, administradorPasos);

        // 2. Mostrar la animación completa para que el jugador memorice
        vista.animarOrdenamiento(administradorPasos.getPasos(), getNombreAlgoritmo());

        // 3. Bucle de rondas hasta ganar
        while (estaIniciada() && aciertos < ACIERTOS_PARA_GANAR) {
            rondas++;

            PasoOrdenamiento<T> pasoDesafio = seleccionarPasoDesafioAleatorio();
            int nroPaso = administradorPasos.getPasos().indexOf(pasoDesafio);

            // El jugador ingresa el orden mediante JOptionPane
            List<T> respuesta = vista.mostrarDesafioYEsperar(
                    pasoDesafio, nroPaso, administradorPasos.getPasos().size());

            // Input inválido o null cuenta como error
            boolean acerto = (respuesta != null)
                    && verificarEstadosDePasos(respuesta, nroPaso);

            if (acerto) { aciertos++; }

            vista.mostrarFeedback(acerto, pasoDesafio,
                    respuesta != null ? respuesta : new ArrayList<>());
        }

        // 4. Victoria
        if (aciertos >= ACIERTOS_PARA_GANAR) {
            vista.mostrarVictoria(aciertos, rondas);
            setPuntaje(100);
        }

        finalizar();
    }

    /**
     * Pre:  la partida debe estar iniciada
     * Post: cambia el estado a Creado
     */
    @Override
    public void finalizar() {
        ValidacionesUtiles.validarVerdadero(estaIniciada(), getNombreAlgoritmo());
        setEstado(EstadoDePartida.Creado);
    }

    /**
     * Post: elige un paso aleatorio del historial (excluye inicio y fin)
     *       para usarlo como desafío de memoria
     */
    public PasoOrdenamiento<T> seleccionarPasoDesafioAleatorio() {
        ValidacionesUtiles.validarVerdadero(estaIniciada(), getNombreAlgoritmo());
        List<PasoOrdenamiento<T>> pasosTotales = administradorPasos.getPasos();
        ValidacionesUtiles.validarMayorACero(pasosTotales.size(), "Debe haber al menos un paso");
        int indice = (int) (Math.random() * (pasosTotales.size() - 1)) + 1;
        return administradorPasos.getPasos().get(indice);
    }

    /**
     * Pre:
     * - elementos no nulo
     * - nroPaso dentro del rango del historial
     * - elementos.size() == tamaño del paso real
     * Post: devuelve true si el orden del jugador coincide exactamente
     *       con el estado real del paso nroPaso
     */
    public boolean verificarEstadosDePasos(List<T> elementos, int nroPaso) {
        ValidacionesUtiles.esDistintoDeNull(elementos,
                "La lista de elementos a verificar no puede ser nula");
        List<PasoOrdenamiento<T>> pasos = administradorPasos.getPasos();
        ValidacionesUtiles.validarRangoNumerico(nroPaso, 0, pasos.size() - 1,
                "Numero de paso fuera de rango");
        int cantidadReal = pasos.get(nroPaso).getCopiasEnEstePaso().size();
        ValidacionesUtiles.validarVerdadero(elementos.size() == cantidadReal,
                "La cantidad de elementos tiene que coincidir con la del paso");
        List<T> elementosReales = pasos.get(nroPaso).getCopiasEnEstePaso();
        for (int i = 0; i < elementosReales.size(); i++) {
            if (!elementosReales.get(i).equals(elementos.get(i))) {
                return false;
            }
        }
        return true;
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    /**
     * Post: devuelve el historial completo de pasos
     */
    public List<PasoOrdenamiento<T>> getHistorialDePasos() {
        return administradorPasos.getPasos();
    }

    /**
     * Post: devuelve el nombre del algoritmo seleccionado
     */
    public String getNombreAlgoritmo() {
        return ordenador.getNombre();
    }

    // ── Setters ───────────────────────────────────────────────────────────────

    /**
     * Pre:  elementos != null
     * Post: modifica los elementos iniciales
     */
    private void setElementosIniciales(List<T> elementos) {
        ValidacionesUtiles.esDistintoDeNull(elementos,
                "La lista de elementos no puede ser nula");
        this.elementosIniciales = elementos;
    }

    /**
     * Pre:  ordenador != null
     * Post: modifica el ordenador
     */
    private void setOrdenador(Ordenador<T> ordenador) {
        ValidacionesUtiles.esDistintoDeNull(ordenador,
                "El ordenador seleccionado no puede ser nulo");
        this.ordenador = ordenador;
    }

    /**
     * Pre:  admin != null
     * Post: modifica el administrador de pasos
     */
    private void setAdministradorDePasos(AdministradorDePasos<T> admin) {
        ValidacionesUtiles.esDistintoDeNull(admin,
                "El Administrador de pasos no puede ser nulo");
        this.administradorPasos = admin;
    }

    /**
     * Pre:  vista != null
     * Post: modifica la vista
     */
    private void setVista(VistaOrdenamiento<T> vista) {
        ValidacionesUtiles.esDistintoDeNull(vista, "La vista no puede ser nula");
        this.vista = vista;
    }
}