package Juego.ciudades.complejidad;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserEcuacion {

    // patrón: T(n) = 2T(n/2) + n^2
    private static final Pattern PATRON = Pattern.compile(
        "T\\(n\\)\\s*=\\s*(\\d+)T\\(n/(\\d+)\\)\\s*\\+\\s*(.+)"
    );

    /**
     * Parsea un string con la forma "T(n) = aT(n/b) + f(n)".
     *
     * @param entrada string ingresado por el usuario
     * @return EcuacionRecurrencia con los valores extraídos, o null si el formato es inválido
     */
    public EcuacionRecurrencia parsear(String entrada) {
        if (entrada == null || entrada.isBlank()) {
            return null;
        }

        Matcher m = PATRON.matcher(entrada.trim());

        if (!m.matches()) return null;

        int a = Integer.parseInt(m.group(1));
        int b = Integer.parseInt(m.group(2));
        String fn = m.group(3).trim();

        return new EcuacionRecurrencia(a, b, fn);
    }

    /**
     * Verifica si el string tiene el formato correcto.
     *
     * @param entrada string a verificar
     * @return true si el formato es válido
     */
    public boolean esValido(String entrada) {
        if (entrada == null || entrada.isBlank()) return false;
        return PATRON.matcher(entrada.trim()).matches();
    }
}
