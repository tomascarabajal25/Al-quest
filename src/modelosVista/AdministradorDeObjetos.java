package modelosVista;

public class AdministradorDeObjetos {
	Vista vista;

	public AdministradorDeObjetos(Vista vista) {
		setVista(vista);
	}
	public void setObjetos(ObjetoVista ...objetos) {
		for (ObjetoVista objeto:objetos) {
			if (objeto != null) {
				vista.agregarObjeto(objeto);
			}
		}
	}
	
	
	
	public Vista getVista() {
		return vista;
	}

	private void setVista(Vista vista) {
		this.vista = vista;
	}

}
