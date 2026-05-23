package material.teclado;

import java.util.Scanner;

public class Teclado {

	public static Scanner teclado;
	
	public static void inicializar() {
		teclado = new Scanner(System.in);
	}
	
	public static void finalizar() {
		teclado.close();
	}
	
	public static int leerEntero() {
		return teclado.nextInt();
	}
}