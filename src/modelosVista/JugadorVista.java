package modelosVista;



import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import modelos.Jugador;



public class JugadorVista extends EntidadVista {
	private Vista vistaDelJuego;
	private KeyHandler keyHa;
	
	private final int screenX;
	private final int screenY;
	private Jugador jugador;
	
	
	
	//deberia heredar de entidad
	
	
	public JugadorVista(Jugador jugador, KeyHandler key,
            int spawnCol, int spawnFila,
            String rutaSprites,Vista vista
            ) {
		super(jugador.getNombre());
		setJugador(jugador) ;
		setVista(vista);
		setKey(key);
		screenX=vistaDelJuego.getAnchoDePantalla()/2 -(vistaDelJuego.tamaño/2);
		screenY=vistaDelJuego.getLargoDePantalla()/2 -(vistaDelJuego.tamaño/2);
		
		setWorldX(spawnCol * vistaDelJuego.tamaño);
		setWorldY(spawnFila * vistaDelJuego.tamaño); 
		
		setAreaSolida(new Rectangle(8, 16, 32, 32)); 
		setDireccion(Direccion.Abajo);;
		
		getImagenesDelJugador(rutaSprites);
		}
	
	

	private void setJugador(Jugador jugador2) {
		jugador=jugador2;
	}
	public Jugador getJugador() {
		return jugador;
	}

	public void actualizar() {
		if(keyHa.upPressed==true 
				|| keyHa.downPressed==true 
				|| keyHa.leftPressed==true
				|| keyHa.rightPressed==true ) {
			if(keyHa.upPressed) {setDireccion(Direccion.Arriba);}
			if(keyHa.downPressed) {setDireccion(Direccion.Abajo);}
			if(keyHa.leftPressed) {setDireccion(Direccion.Izquierda);}
			if(keyHa.rightPressed) {setDireccion(Direccion.Derecha);}
			setColisionOn(false);
			vistaDelJuego.chequeadorDeColision.chequearConstruccion(this);
			
			if(isColisionOn()==false) {
				switch (getDireccion()) {
					case Arriba: {
						setWorldY(getWorldY()-getVelocidad());
						break;
					}
					case Abajo: {
						setWorldY(getWorldY()+getVelocidad());
						break;
					}
					case Izquierda: {
						setWorldX(getWorldX()-getVelocidad());
						break;
					}
					case Derecha: {
						setWorldX(getWorldX()+getVelocidad());
						break;
					}
				}
			}
			
			
			setSpriteCounter(getSpriteCounter()+1);
			if(getSpriteCounter() >12) {
				if(getSpriteNum() == 1) {
					setSpriteNum(2);;
				}
				else {
					setSpriteNum(1);
				}
				setSpriteCounter(0);
			}
		}
	}
	public void getImagenesDelJugador(String ruta) {
		try {
			setUp1(ImageIO.read(getClass().getResourceAsStream(ruta + "_up_1.bmp")));
			setUp2(ImageIO.read(getClass().getResourceAsStream(ruta + "_up_2.bmp")));
			setDown1(ImageIO.read(getClass().getResourceAsStream(ruta + "_down_1.bmp")));
			setDown2(ImageIO.read(getClass().getResourceAsStream(ruta + "_down_2.bmp")));
			setRight1(ImageIO.read(getClass().getResourceAsStream(ruta + "_right_1.bmp")));
			setRight2(ImageIO.read(getClass().getResourceAsStream(ruta + "_right_2.bmp")));
			setLeft1(ImageIO.read(getClass().getResourceAsStream(ruta + "_left_1.bmp")));
			setLeft2(ImageIO.read(getClass().getResourceAsStream(ruta + "_left_2.bmp")));
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g2) {
		BufferedImage image=null;
		switch (getDireccion()) {
		case Direccion.Arriba: 
			if(getSpriteNum()==1) {
				image=getUp1();
			}
			if(getSpriteNum() == 2) {
				image=getUp2();
			}
			break;
		case Direccion.Abajo: 
			if(getSpriteNum()==1) {
				image=getDown1();
			}
			if(getSpriteNum() == 2) {
				image=getDown2();
			}
			break;
		case Direccion.Izquierda: 
			if(getSpriteNum()==1) {
				image=getLeft1();
			}
			if(getSpriteNum() == 2) {
				image=getLeft2();
			}
			break;
		case Direccion.Derecha: 
			if(getSpriteNum()==1) {
				image=getRight1();
			}
			if(getSpriteNum() == 2) {
				image=getRight2();
			}
			break;
			}
		g2.drawImage(image, screenX, screenY, vistaDelJuego.tamaño, vistaDelJuego.tamaño,null);
	}
	
	
	
	public Vista getVistaDelJuego() {
		return vistaDelJuego;
	}

	

	public KeyHandler getKeyHa() {
		return keyHa;
	}

	
	
	public int getScreenX() {
		return screenX;
	}
	public int getScreenY() {
		return screenY;
	}
	
	
	

	private void setVista(Vista vista) {
		this.vistaDelJuego=vista;
	}
	
	private void setKey(KeyHandler key){
		this.keyHa=key;
	}
	
}
