import modelos.PartidaGeneral;
import persistencia.GestorDeInicio;

public class Main {
    public static void main(String[] args) {
    	System.setProperty("sun.java2d.d3d", "false");
        PartidaGeneral partidaGeneral = GestorDeInicio.iniciarSesion();

        if (partidaGeneral == null) {
            return;
        }

        partidaGeneral.iniciar();
    }
}