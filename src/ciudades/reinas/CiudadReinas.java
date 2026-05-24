package ciudades.reinas;

public class CiudadReinas {
    
    private SolverReinas solver = new SolverReinas();
    private Tablero tablero = new Tablero();
    private int[] solucion;     //la respuesta correcta, oculta al jugador
    private int filajugador;
    
    public boolean iniciarCiudad(int tamanio, int fila, int columna){
        
        tablero.setTamanio(tamanio);
        tablero.setReinas(fila, columna);
        this.filajugador = fila;

        solucion = solver.obtenerSolucion(tablero, fila);

        return solucion != null;    //false = no hay solucion posible desde esa posicion

    }

    public boolean intentarColocarReina (int fila, int columna){
        if (solucion == null) return false;
    
        if (solucion [fila] == columna) {   //coincide con la solucion
            tablero.setReinas(fila, columna);
            return true;
        }

        return false; //    movimiento incorrecto -> desde CiudadReinas se puede disparar "perdiste"
    
    }

    public boolean juegoGanado (){
        for (int i = 0; i < tablero.getTamanio(); i++){
            if (tablero.getReinas(i) == -1) return false;   //hay filas vacias
        }
        return true;
    }

    public void mostrarTablero(){   //metodo provisorio, se puede quitar mas adelante
        tablero.imprimirTablero();
    }

}
