package torresDeHanoi;

import materialesUtiles.ValidacionesUtiles;

public class HanoiSolver {
	private boolean stop=false;
	private int paso=0;
	private ObservadorHanoi observador;

	public HanoiSolver(ObservadorHanoi observador) {
	    this.observador = observador;
	}
	/*
	 * @param:pila de origen (de donde se quiere sacar un nodo)
	 * @param1:pila de destino (donde se quiere meter un nodo)
	 * mueve del origen al destino 
	 */
	private void moverPlataforma(Pila origen, Pila destino) {
		ValidacionesUtiles.esDistintoDeNull(destino, "el destino no puede ser null");
		ValidacionesUtiles.esDistintoDeNull(origen, "el origen no puede ser null");
		if (stop==false) {
			Nodo<String> plataforma = new Nodo<>(origen.peek());
			origen.pop();
			destino.push(plataforma);
			this.paso++;

			boolean continuar = observador.onMovimiento(paso);
			if (!continuar) {
			    stop = true;
			}
			
			
			}
			
			
		}
	
	/*@param0:cantidad de discos
	 * @param:pila origen 
	 * @param1:pila auxiliar 
	 * @param2: pila destino
	 * resuelve el de forma recursiva el algoritmo de hanoi
	 */
	protected void resolverHanoi(int n, Pila ori, Pila aux, Pila des) {
		ValidacionesUtiles.esDistintoDeNull(ori, null);
		ValidacionesUtiles.esDistintoDeNull(aux, null);
		ValidacionesUtiles.esDistintoDeNull(des, null);
		ValidacionesUtiles.validarMayorACero(n, "no puede haber menos de un disco");
		if (stop) {
			return;
		}
		try {
			if(n==1) {
			moverPlataforma(ori, des);
			}
		else {
			resolverHanoi(n-1, ori, des, aux);
			moverPlataforma(ori, des);
			
			resolverHanoi(n-1, aux, ori, des);
			}
		} 
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}