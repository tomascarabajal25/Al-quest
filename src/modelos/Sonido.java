package modelos;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sonido {
    // ATRIBUTOS
    private final Map<String, URL> enlacesDeSonido = new HashMap<>();
    private Clip clipMusica;
    private Clip clipEfecto;

    // CONSTRUCTORES ---------------------------------------------------------------------------------------------
    public Sonido() {
        //  los sonidos se agregan mediante agregarSonido
    }

    // METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------
    /**
     * Agrega un sonido al registro local buscándolo como recurso en el classpath.
     *
     * Pre: ruta no es nula y corresponde a un recurso accesible mediante getResource.
     * Post: si se encontró el recurso, queda mapeado bajo el nombre indicado.
     *
     * @param nombre nombre lógico para referenciar el sonido
     * @param ruta ruta del recurso (por ejemplo: "/sonidos/musica.wav")
     */
    public void agregarSonido(
            String nombre,
            String ruta) {
        URL url = getClass().getResource(ruta);
        if (url != null) {
            enlacesDeSonido.put(nombre, url);
        } else {
            System.err.println("No se encontró el archivo: " + ruta);
        }
    }

    /**
     * Reproduce en bucle la música asociada al nombre.
     *
     * Si ya hay música reproduciéndose, la detiene antes de iniciar la nueva.
     * Si no existe el nombre en el registro, imprime un mensaje de error.
     *
     * @param nombre nombre del sonido previamente registrado
     */
    public void playMusica(
            String nombre) {
        try {
            URL recurso = enlacesDeSonido.get(nombre);
            if (recurso == null) {
                System.err.println("Música no registrada: " + nombre);
                return;
            }

            if (clipMusica != null && clipMusica.isRunning()) {
                clipMusica.stop();
            }

            AudioInputStream ais = AudioSystem.getAudioInputStream(recurso);
            ais = convertirA16Bit(ais);
            clipMusica = AudioSystem.getClip();
            clipMusica.open(ais);
            clipMusica.loop(Clip.LOOP_CONTINUOUSLY);
            clipMusica.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Detiene la música en reproducción y libera el recurso del clip.
     */
    public void stopMusica() {
        if (clipMusica != null) {
            clipMusica.stop();
            clipMusica.close();
        }
    }

    /**
     * Reproduce un efecto de sonido una sola vez.
     *
     * @param nombre nombre del sonido previamente registrado
     */
    public void playEfecto(
            String nombre) {
        try {
            URL recurso = enlacesDeSonido.get(nombre);
            if (recurso == null) {
                System.err.println("Efecto no registrado: " + nombre);
                return;
            }

            AudioInputStream ais = AudioSystem.getAudioInputStream(recurso);
            ais = convertirA16Bit(ais);
            clipEfecto = AudioSystem.getClip();
            clipEfecto.open(ais);
            clipEfecto.setFramePosition(0);
            clipEfecto.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Detiene el efecto de sonido en reproducción y libera el clip.
     */
    public void stopEfecto() {
        if (clipEfecto != null) {
            clipEfecto.stop();
            clipEfecto.close();
        }
    }

	
    
    /**
     * Convierte un AudioInputStream a PCM 16-bit si su formato original no es
     * soportado directamente por javax.sound (ej: PCM 32-bit, 24-bit, MP3, etc.).
     * Si ya es PCM 16-bit, lo devuelve sin cambios.
     *
     * Pre:  ais no es nulo.
     * Post: el stream devuelto siempre tiene sampleSize de 16 bits.
     */
    private AudioInputStream convertirA16Bit(AudioInputStream ais) {
        AudioFormat formatoOriginal = ais.getFormat();
        if (formatoOriginal.getSampleSizeInBits() != 16
                || formatoOriginal.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
            AudioFormat formatoDestino = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    formatoOriginal.getSampleRate(),
                    16,
                    formatoOriginal.getChannels(),
                    formatoOriginal.getChannels() * 2,
                    formatoOriginal.getSampleRate(),
                    false // little-endian
            );
            ais = AudioSystem.getAudioInputStream(formatoDestino, ais);
        }
        return ais;
    }

    //Getters -----------------------------------------------------------------------------------------------
    public Clip getClipMusica() {
		return clipMusica;
	}

	public Clip getClipEfecto() {
		return clipEfecto;
	}

	public Map<String, URL> getEnlacesDeSonido() {
		return enlacesDeSonido;
	}
	
	//Setters -----------------------------------------------------------------------------------------------
	public void setClipEfecto(Clip clipEfecto) {
		this.clipEfecto = clipEfecto;
	}
	public void setClipMusica(Clip clipMusica) {
		this.clipMusica = clipMusica;
	}
}