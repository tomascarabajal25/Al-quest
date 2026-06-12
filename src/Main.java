import modelos.PartidaGeneral;
import persistencia.GestorDeInicio;

public class Main {
    public static void main(String[] args) {
        PartidaGeneral partidaGeneral = GestorDeInicio.iniciarSesion();

        if (partidaGeneral == null) {
            // El usuario canceló el login: cerrar la aplicación.
            return;
        }

        partidaGeneral.iniciar();
    }
}