package Juego.ciudades.recoleccionEnMatriz;

import Juego.Constantes;
import estructuras.listas.ListaSimplementeEnlazada;
import estructuras.vector.Vector;
import modelos.*;
import utils.Teclado;
import utils.ValidacionesUtiles;

public class VistaCiudadRecoleccion {
    //INTERFACES ----------------------------------------------------------------------------------------------
    //ENUMERADOS ----------------------------------------------------------------------------------------------
    //CONSTANTES ----------------------------------------------------------------------------------------------
    //ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
    //ATRIBUTOS -----------------------------------------------------------------------------------------------
    //ATRIBUTOS TRANSITORIOS ----------------------------------------------------------------------------------
    //CONSTRUCTORES -------------------------------------------------------------------------------------------
    //METODOS ABSTRACTOS --------------------------------------------------------------------------------------
    //METODOS HEREDADOS (CLASE)--------------------------------------------------------------------------------
    //METODOS HEREDADOS (INTERFACE)----------------------------------------------------------------------------
    //METODOS DE CLASE ----------------------------------------------------------------------------------------
    //METODOS GENERALES ---------------------------------------------------------------------------------------
    /**
     * Imprimir inteerfaz de la ciudad
     */
    public static void imprimirInterfaz(Mapa3D mapa, Mochila mochila, Vector<Elemento> elementos, Jugador jugador, int puntos, int visibilidad) {
        ValidacionesUtiles.esDistintoDeNull(mapa, "mapa");
        ValidacionesUtiles.esDistintoDeNull(mochila, "mochila");
        ValidacionesUtiles.esDistintoDeNull(elementos, "elementos");
        ValidacionesUtiles.esDistintoDeNull(mapa, "jugador");
        ValidacionesUtiles.validarMayorOIgualACero(puntos, "puntos");
        ValidacionesUtiles.validarMayorOIgualACero(visibilidad, "visibilidad");

        Teclado.inicializar();

        System.out.println("Puntaje: " + puntos);
        System.out.println("Elementos en mochila: " + mochila.getCantidadElementos());
        System.out.println("/n");

        int[] posicionJugador = mapa.getPosicionCeldaConContenido(jugador);
        imprimirMapa(mapa.getNivel(posicionJugador[2]), elementos, jugador, posicionJugador, visibilidad);
    }

    public static void imprimirMapa(Mapa mapa, Vector<Elemento> elementos, Jugador jugador, int[] posicionJugador, int visibilidad){
        Vector<Vector<Celda<?>>> celdasVecinas = mapa.getCeldasVecinasRespectoPosicion(posicionJugador[0], posicionJugador[1], visibilidad);

        for (Vector<Celda<?>> celdas : celdasVecinas) {
            for (Celda<?> celda : celdas) {
                if (celda.equals(jugador)) {
                    System.out.println(Constantes.CARACTER_JUGADOR);
                } else if (celda.equals(elementos.obtener(0))){
                    System.out.println(Constantes.CARACTER_CARTA_VISIBILIDAD);
                } else if (celda.equals(elementos.obtener(1))) {
                    System.out.println(Constantes.CARACTER_CARTA_DESPLAZAMIENTO);
                } else if (celda.equals(elementos.obtener(2))) {
                    System.out.println(Constantes.CARACTER_CARTA_DESPLAZAMIENTO);
                } else {
                    System.out.println(Constantes.CARACTER_VACIO);
                }
            }
        }
    }

    public static void imprimirMochila(Mochila mochila, Vector<Elemento> elementos){
        ValidacionesUtiles.esDistintoDeNull(mochila, "mochila");
        ValidacionesUtiles.esDistintoDeNull(elementos, "elementos");

        ListaSimplementeEnlazada<Elemento> elementosGuardados = mochila.getElementos();

        int i = 1;
        for (Elemento elemento : elementosGuardados) {
            System.out.println(i + " - " + elemento.getNombre());
            i++;
        }
        System.out.println("Q para salir");
    }

    public static char ingresarCaracter(){
        Teclado.inicializar();
        return Teclado.leerCaracter();
    }

    public static void cartaEncontrada(String nombre, String descripcion){

    }
    //METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------
    //METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------
    //GETTERS REDEFINIDOS -------------------------------------------------------------------------------------
    //GETTERS INICIALIZADOS -----------------------------------------------------------------------------------
    //GETTERS COMPLEJOS ---------------------------------------------------------------------------------------
    //GETTERS SIMPLES -----------------------------------------------------------------------------------------
    //SETTERS COMPLEJOS----------------------------------------------------------------------------------------
    //SETTERS SIMPLES -----------------------------------------------------------------------------------------
}
