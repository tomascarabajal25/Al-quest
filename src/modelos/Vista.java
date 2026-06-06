package modelos;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import javax.swing.JPanel;

import Juego.ciudades.ciudad5.UI.MinijuegoDesafio;

public class Vista extends JPanel implements Runnable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//configuracion screen
	final int tamañoOriginal=16; //16x16 pixeles
	final int escala=3; 
	
	public final int tamaño = tamañoOriginal *escala;
	final int columnas = 16;
	final int filas = 12;
	final int anchoDePantalla= tamaño * columnas; //768pix	
	final int largoDePantalla= tamaño*filas; //576pix
	public JugadorVista jugadorVista;
	
	//configuracion del mundo
	public final int columnasDelMundo=50;
	public final int filasDelMundo=50;
	public final int anchoDeMundo=tamaño*columnasDelMundo;
	public final int largoDeMundo=tamaño*filasDelMundo;
	
	//fps
	int FPS=60;

	protected ManejadorDeConstruccion construccionesM=new ManejadorDeConstruccion(this);
	KeyHandler keyhandler=new KeyHandler();
	Thread hiloDelJuego;
	public AdministradorDeObjetos adminObjt= new AdministradorDeObjetos(this);
	ChequeadorDeColision chequeadorDeColision= new ChequeadorDeColision(this);
	private Vector<ObjetoVista> objetos=new Vector<ObjetoVista>();
	
	public MinijuegoDesafio miniJuego;
	
	public Vista(String rutaMundo) {
		this.setPreferredSize(new Dimension(anchoDePantalla,largoDePantalla));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyhandler);
		this.setFocusable(true);
		jugadorVista=new JugadorVista(new Jugador("hardcode"), keyhandler,25,23,"/assets/jugador/boy",this);
		construccionesM.loadMap(rutaMundo);
		setUpJuego();
		

		
	}
	public void setMinijuego(MinijuegoDesafio minijuego) {
		miniJuego=minijuego;
	}
	
	public void setUpJuego() {
		adminObjt.setObjetos();
	}
	public void agregarObjeto(ObjetoVista objeto) {
		objetos.add(objeto);
	}

	
	public void startGameThread() {
		hiloDelJuego= new Thread(this);
		hiloDelJuego.start();	
	}
	
	
	@Override
	public void run() {
		
		double drawInterval = 1000000000/FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		
		while(hiloDelJuego != null) {
			
			currentTime = System.nanoTime();
			
			delta += (currentTime - lastTime) / drawInterval;
			
			lastTime = currentTime;
			
			if(delta >= 1) {
				actualizar();
				repaint();
				delta--;
			}
			
		}
		
	}
		
	
	public void actualizar() {
		if (miniJuego != null) {
	        miniJuego.actualizar(jugadorVista);
	    }
		jugadorVista.actualizar();
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		//Construcciones
		construccionesM.draw(g2);
		//Objetos
		for(ObjetoVista objeto:objetos) {
				objeto.draw(g2, this);
			}
		//}
		//Jugador
		
		jugadorVista.draw(g2);
		if (miniJuego != null) {
	        miniJuego.draw(g2, jugadorVista);
	    }
		
		g2.dispose();
	}
	public void detenerHilo() {
        hiloDelJuego = null; // el while(hiloDelJuego != null) termina solo
    }
}
