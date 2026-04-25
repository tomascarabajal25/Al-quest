package materialesUtiles;

public class SistemaUtiles {

	/**
	 * Detiene la aplicacion por un lapso de milisegundos
	 * @param milisegundos
	 */
	public static void esperar(long milisegundos) {
		ValidacionesUtiles.validarMayorOIgualACero(milisegundos, "milisegundos");
		try {
			Thread.sleep(milisegundos);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Devuele la ruta completa de un archivo del source.
	 * @param rutaRelativa: arranca con src....
	 * @return
	 */
	public static String generarRutaAbsoluta(String rutaRelativa) {
		try {
			String rutaBase = SistemaUtiles.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			if (rutaBase.endsWith("bin/")) {
				rutaBase = rutaBase.substring(0, rutaBase.length() - 4);
			}
			if (rutaBase.startsWith("/")) {
				rutaBase = rutaBase.substring(1);
			}
			return rutaBase + rutaRelativa;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
