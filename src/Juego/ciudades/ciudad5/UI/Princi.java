package Juego.ciudades.ciudad5.UI;

import javax.swing.JFrame;

import modelos.Vista;
//prueba no va servir despues
public class Princi {
	
	public static void main(String[] args) {
		JFrame ventana= new JFrame();
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventana.setResizable(false);
		ventana.setTitle("Ciudad De Busqueda");
		
		Vista vista=new Vista("/maps/world01.txt");
		ventana.add(vista);
		ventana.pack();
		vista.startGameThread();
		//vista.setUpJuego();
		
		ventana.setLocationRelativeTo(null);
		ventana.setVisible(true);
		
	}
}
