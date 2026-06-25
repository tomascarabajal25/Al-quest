package juego.ciudades.ordenamientos;

import java.util.List;
import java.util.Objects;
import utils.ValidacionesUtiles;

/**
 * Clase abstracta que representa un algoritmo de ordenamiento genérico.
 * * Define la estructura base y el comportamiento que deben heredar todos
 * los ordenadores de la ciudad de ordenamientos.
 * * @param <T> tipo de dato elemental que extiende de Comparable
 */
public abstract class Ordenador<T extends Comparable<T>> {

    // CONSTANTES

    // ATRIBUTOS DE CLASE

    // ATRIBUTOS
    private String nombre;

    // CONSTRUCTORES
    /**
     * Inicializa un ordenador con su nombre correspondiente.
     * * Pre:
     * - El nombre no debe ser nulo.
     * * Post:
     * - Se crea el objeto con el nombre asignado.
     * * @param nombre cadena de caracteres que identifica al ordenador
     */
    public Ordenador(String nombre) {
        setNombre(nombre);
    }

    // METODOS DE CLASE

    // METODOS GENERALES
    /**
     * Calcula el código hash basado en el nombre del ordenador.
     * * Post:
     * - Devuelve un entero que representa el hash.
     * * @return código hash del objeto
     */
    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }

    /**
     * Compara si este ordenador es idéntico a otro objeto.
     * * Post:
     * - Devuelve verdadero si ambos comparten el mismo nombre y clase.
     * * @param obj objeto a comparar
     * @return verdadero si son iguales, falso en caso contrario
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Ordenador<?> other = (Ordenador<?>) obj;
        return Objects.equals(nombre, other.nombre);
    }

    /**
     * Genera una cadena de texto representativa con el estado del ordenador.
     * * Post:
     * - Devuelve el texto legible con los atributos internos.
     * * @return cadena descriptiva del objeto
     */
    @Override
    public String toString() {
        return "Ordenador [nombre=" + nombre + "]";
    }

    // METODOS DE COMPORTAMIENTO
    /**
     * Ejecuta el algoritmo de ordenamiento tradicional sobre la colección.
     * * Pre:
     * - La colección de elementos no debe ser nula.
     * * Post:
     * - Modifica la colección mutándola a un estado ordenado de menor a mayor.
     * * @param elementos colección de tipo Vector a ordenar
     */
    protected abstract void ordenar(List<T> elementos);

    /**
     * Ejecuta el algoritmo de ordenamiento registrando el paso a paso.
     * * Pre:
     * - La colección de elementos no debe ser nula.
     * - El administrador de pasos no debe ser nulo.
     * * Post:
     * - Modifica la colección mutándola a un estado ordenado.
     * - El historial de pasos contiene el registro detallado de las operaciones.
     * * @param elementos colección de tipo Vector a ordenar
     * @param historialDePasos administrador encargado de auditar las mutaciones
     */
    public abstract void ordenar(
    		List<T> elementos,
            AdministradorDePasos<T> historialDePasos);

    // GETTERS
    /**
     * Obtiene el nombre del algoritmo de ordenamiento.
     * * Post:
     * - Retorna la cadena de caracteres correspondiente al nombre.
     * * @return nombre del ordenador
     */
    public String getNombre() {
        return nombre;
    }

    // SETTERS
    /**
     * Establece el nombre del ordenador validando su estado.
     * * Pre:
     * - El parámetro nombre no debe ser nulo.
     * * Post:
     * - El atributo interno nombre es actualizado.
     * * @param nombre nuevo nombre del ordenador
     */
    private void setNombre(String nombre) {
        ValidacionesUtiles.esDistintoDeNull(nombre, "nombre");
        this.nombre = nombre;
    }
}