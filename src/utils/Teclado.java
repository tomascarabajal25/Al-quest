package utils;

import java.util.Scanner;

public class Teclado {

	public static Scanner teclado;
	
	public static void inicializar() {
		teclado = new Scanner(System.in);
	}
	
	public static void finalizar() {
		teclado.close();
	}
	
	public static String leerTexto() {
		return teclado.nextLine();
	}

    public static char leerCaracter() {
        return teclado.next().charAt(0);
    }
	public static int leerEntero() {
		return teclado.nextInt();
	}
}