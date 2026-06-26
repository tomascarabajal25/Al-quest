package modelos;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * Controla el volumen de los Clips de audio del juego.
 *
 * NOTA TEMPORAL: el volumen está hardcodeado en VOLUMEN_GENERAL.
 * Cuando se implemente la pantalla de configuración global, este valor
 * debería reemplazarse por uno dinámico (leído de preferencias del jugador).
 */
public class ControladorVolumen {

    /**
     * Volumen general del juego, en escala lineal de 0.0 (silencio) a 1.0 (máximo).
     * TODO: reemplazar por un valor configurable cuando exista la pantalla de opciones.
     */
    private static final float VOLUMEN_GENERAL = 0.5f;

    private ControladorVolumen() {
        // clase de utilidad, no instanciable
    }

    /**
     * Aplica el volumen general configurado a un Clip.
     * Si el Clip no soporta control de ganancia, no hace nada.
     *
     * Pre: clip no es nulo y ya fue abierto (open()).
     * Post: el volumen del clip queda ajustado según VOLUMEN_GENERAL.
     *
     * @param clip clip de audio ya abierto al cual aplicar el volumen
     */
    public static void aplicarVolumen(Clip clip) {
        aplicarVolumen(clip, VOLUMEN_GENERAL);
    }

    /**
     * Aplica un volumen específico (0.0 a 1.0) a un Clip.
     * Convierte la escala lineal a decibeles, que es lo que requiere
     * javax.sound.sampled internamente.
     *
     * @param clip     clip de audio ya abierto
     * @param volumen  volumen deseado, entre 0.0 (silencio) y 1.0 (máximo)
     */
    public static void aplicarVolumen(Clip clip, float volumen) {
        if (clip == null || !clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            return;
        }

        volumen = Math.max(0.0001f, Math.min(1.0f, volumen)); // evitar log(0)

        FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float decibeles = (float) (Math.log10(volumen) * 20.0);

        // Asegurarse de no salirse del rango soportado por el control
        decibeles = Math.max(control.getMinimum(), Math.min(control.getMaximum(), decibeles));

        control.setValue(decibeles);
    }

    /** @return el volumen general actual (escala 0.0 a 1.0) */
    public static float getVolumenGeneral() {
        return VOLUMEN_GENERAL;
    }
}
