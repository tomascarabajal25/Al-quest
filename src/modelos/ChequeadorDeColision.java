package modelos;

public class ChequeadorDeColision {

	Vista gp;

	public ChequeadorDeColision(Vista gp) {
		this.gp = gp;
	}

	public void chequearConstruccion(EntidadVista entity) {
		int entityLeftWorldX = entity.getWorldX() + entity.getAreaSolida().x;
		int entityRightWorldX = entity.getWorldX() + entity.getAreaSolida().x + entity.getAreaSolida().width;
		int entityTopWorldY = entity.getWorldY() + entity.getAreaSolida().y;
		int entityBottomWorldY = entity.getWorldY() + entity.getAreaSolida().y + entity.getAreaSolida().height;

		int entityLeftCol = entityLeftWorldX / gp.tamaño;
		int entityRightCol = entityRightWorldX / gp.tamaño;
		int entityTopRow = entityTopWorldY / gp.tamaño;
		int entityBottomRow = entityBottomWorldY / gp.tamaño;

		int tileNum1, tileNum2;
		
		Direccion direction = entity.getDireccion();
		
		switch (direction) {
		case Direccion.Arriba:
			entityTopRow = (entityTopWorldY - entity.getVelocidad()) / gp.tamaño;
			tileNum1 = gp.construccionesM.mapaDeConstruccionesNum[entityLeftCol][entityTopRow];
			tileNum2 = gp.construccionesM.mapaDeConstruccionesNum[entityRightCol][entityTopRow];

			if (gp.construccionesM.construcciones[tileNum1].getColision()== true || gp.construccionesM.construcciones[tileNum2].getColision()== true) {
				entity.setColisionOn(true);;
			}
			break;
		case Direccion.Abajo:
			entityBottomRow = (entityBottomWorldY + entity.getVelocidad()) / gp.tamaño;
			tileNum1 = gp.construccionesM.mapaDeConstruccionesNum[entityLeftCol][entityBottomRow];
			tileNum2 = gp.construccionesM.mapaDeConstruccionesNum[entityRightCol][entityBottomRow];

			if (gp.construccionesM.construcciones[tileNum1].getColision()== true || gp.construccionesM.construcciones[tileNum2].getColision()== true) {
				entity.setColisionOn(true);;
			}
			break;
		case Direccion.Izquierda:
			entityLeftCol = (entityLeftWorldX - entity.getVelocidad()) / gp.tamaño;
			tileNum1 = gp.construccionesM.mapaDeConstruccionesNum[entityLeftCol][entityTopRow];
			tileNum2 = gp.construccionesM.mapaDeConstruccionesNum[entityLeftCol][entityBottomRow];

			if (gp.construccionesM.construcciones[tileNum1].getColision()== true || gp.construccionesM.construcciones[tileNum2].getColision()== true) {
				entity.setColisionOn(true);;
			}
			break;
		case Direccion.Derecha:
			entityRightCol = (entityRightWorldX + entity.getVelocidad()) / gp.tamaño;
			tileNum1 = gp.construccionesM.mapaDeConstruccionesNum[entityRightCol][entityTopRow];
			tileNum2 = gp.construccionesM.mapaDeConstruccionesNum[entityRightCol][entityBottomRow];

			if (gp.construccionesM.construcciones[tileNum1].getColision()== true || gp.construccionesM.construcciones[tileNum2].getColision()== true) {
				entity.setColisionOn(true);;
			}
			break;
		}
	}
}