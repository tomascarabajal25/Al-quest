package ciudadReinas;

public class CiudadReinas {
    
    SolverReinas solver = new SolverReinas();
    Tablero tablero = new Tablero();
    
    public void iniciarCiudad(int tamanio, int fila, int columna){
        
        tablero.setTamanio(tamanio);
        tablero.setReinas(fila, columna);

        //falta iniciar el bmp al iniciar la ciudad
    }

    public void ciudadIniciada(int filaJugador){
        if (solver.resolver(tablero, 0, filaJugador)){ // el fila:0 es para que inicie la iteracion en resolver
            //Solucion encontrada, mostrar BMP
        } else{
            System.out.println("No existe solucion");
        }


        //falta mostrar el gráfico BMP con la ciudad ya iniciada
    }

    public void mostrarTablero(){
        tablero.imprimirTablero();
    }

}
