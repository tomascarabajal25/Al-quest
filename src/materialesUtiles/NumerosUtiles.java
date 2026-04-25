package materialesUtiles;

public class NumerosUtiles {

	/**
	 * Devuelve la parte entera del double
	 * @param valor
	 * @return
	 */
	public static int toInt(Double valor) {
		return valor.intValue();
	}
	
    /**
     * Dado un valor entero, lo deja entre 0 y 255
     * @param valor: puede ser cualquier valor
     * @return
     */
    public static int limitarRango(int minimo, int maximo, int valor) {
    	return Math.max(minimo, Math.min(maximo, valor));
    }

}
