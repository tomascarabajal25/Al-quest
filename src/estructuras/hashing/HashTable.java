package estructuras.hashing;

public class HashTable {
    // El array (vector) donde almacenaremos los datos.
    // Cada elemento del array puede contener un par Clave-Valor.
    // Para simplificar, almacenaremos la clave y el valor juntos como un String.
    private String[] table;
    private int capacity;

    /**
     * Constructor para inicializar la tabla hash con una capacidad dada.
     * @param capacity El tamaño del array de almacenamiento.
     */
    public HashTable(int capacity) {
        this.capacity = capacity;
        this.table = new String[capacity];
    }

    /**
     * 🔑 Función de Hashing: Convierte la clave en un índice del array.
     * Es la parte central para entender cómo se distribuyen los datos.
     * @param key La clave a hashear.
     * @return El índice calculado dentro del rango [0, capacity - 1].
     */
    private int hash(String key) {
        // Obtenemos el valor hash por defecto de Java para la cadena.
        // Lo convertimos a positivo (por si acaso) y aplicamos el módulo (%)
        // sobre la capacidad del array. Esto asegura que el resultado
        // sea un índice válido dentro de nuestro array.
        return Math.abs(key.hashCode() % capacity);
    }

    /**
     * 📥 Inserta un par Clave-Valor en la tabla hash.
     * @param key La clave para la búsqueda.
     * @param value El valor asociado a la clave.
     */
    public void put(String key, String value) {
        // 1. Calcular el índice usando la función hash
        int index = hash(key);

        // 2. Almacenar el par Clave-Valor en la posición calculada.
        // Nota: Para esta implementación simple, NO manejamos COLISIONES,
        // simplemente sobrescribimos si ya hay algo en ese índice.
        this.table[index] = key + ":" + value;

        System.out.println("-> Poniendo: [" + key + ", " + value + "] en el índice: " + index);
    }

    /**
     * 📤 Recupera el valor asociado a una clave.
     * @param key La clave para buscar.
     * @return El valor asociado, o null si la clave no se encuentra (o hay colisión no resuelta).
     */
    public String get(String key) {
        // 1. Calcular el índice usando la función hash (¡Debe ser el mismo que se usó para guardar!)
        int index = hash(key);

        // 2. Recuperar el elemento en esa posición
        String entry = this.table[index];

        if (entry != null) {
            // Separar la clave y el valor (formato "clave:valor")
            String[] parts = entry.split(":", 2);
            String storedKey = parts[0];
            String storedValue = parts.length > 1 ? parts[1] : null;

            // 3. Verificación de clave (para evitar devolver un valor de otra clave que colisionó)
            if (storedKey.equals(key)) {
                return storedValue;
            }
        }
        // Si no hay nada, o la clave almacenada no coincide (colisión simple), retorna null
        return null;
    }

    /**
     * Muestra el estado actual de la tabla.
     */
    public void displayTable() {
        System.out.println("\n--- Estado Actual de la Tabla Hash ---");
        for (int i = 0; i < capacity; i++) {
            System.out.printf("Índice [%d]: %s%n", i, (table[i] != null ? table[i] : "VACÍO"));
        }
        System.out.println("-------------------------------------\n");
    }
}
