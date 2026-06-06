package com.aiquest.juego.ciudades.ordenamientos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.aiquest.modelos.Jugador;
import com.aiquest.modelos.Partida;
import com.aiquest.utils.ValidacionesUtiles;

public class PartidaOrdenamientos<T extends Comparable<T>> extends Partida {
	//ATRIBUTOS----------------------------------------------------------------------
    private List<T> elementosIniciales;
    private Ordenador<T> ordenador;
    private AdministradorDePasos<T> administradorPasos;
    //CONSTRUCTORES-----------------------------------------------------------------
    /**
     * Constructor de la partida específica de la Ciudad ordenamientos
     * Pre:
     * - nombre de la ciudad no nulo
     * - jugador no nulo: jugador de la partida
     * - elementos no nulo: son los elementos que seran ordenados
     * -ordenador elegido para el com.aiquest.com.aiquest.juego no nulo
     * Post:
     * crea la partida de ordenamientos con los atributos indicados
     */
    public PartidaOrdenamientos(String nombreCiudad, Jugador jugador, List<T> elementos, Ordenador<T> ordenador) {
        super(nombreCiudad, jugador); 
        
        setElementosIniciales(new ArrayList<>(elementos));
        setOrdenador(ordenador);
        setAdministradorDePasos(new AdministradorDePasos<>());
    }
    //METODOS DE CLASES-------------------------------------------------------------
    //METODOS GENERALES------------------------------------------------------------
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(elementosIniciales, ordenador);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PartidaOrdenamientos<?> other = (PartidaOrdenamientos<?>) obj;
		return Objects.equals(elementosIniciales, other.elementosIniciales)
				&& Objects.equals(ordenador, other.ordenador);
	}
    
    
    @Override
	public String toString() {
		return "PartidaOrdenamientos [elementosIniciales=" + elementosIniciales + ", ordenador=" + ordenador + "]";
	}

	//METODOS DE COMPORTAMIENTO------------------------------------------------------
    @Override
    /**
     * Pre:
     * - la partida no debe estar iniciada
     * Post:
     * -inicia la partida y comienza el ordenamiento de los elementos
     * -cambia el estado a iniciado
     */
    public void iniciar() {
        ValidacionesUtiles.validarFalso(estaIniciada(), "La partida ya ha sido iniciada");
        
        List<T> copiaDeTrabajo = new ArrayList<>(this.elementosIniciales);
        
        setEstado(EstadoDePartida.Iniciado);
        ordenador.ordenar(copiaDeTrabajo, administradorPasos);
    }
    
    
	@Override
    /**
     * Pre:
     * -la partida debe estar iniciada
     * post: 
     * -cambia el estado a creado
     */
    public void finalizar() {
    	ValidacionesUtiles.validarVerdadero(estaIniciada(), getNombreAlgoritmo());
    	setEstado(EstadoDePartida.Creado);
    }
    /**
     * Post:
     * Elige un paso X aleatorio del historial generado por el algoritmo
     * para usarlo como el desafío de memoria.
     * @return El número de paso seleccionado.
     */
    public PasoOrdenamiento<T> seleccionarPasoDesafioAleatorio() {
    	ValidacionesUtiles.validarVerdadero(estaIniciada(), getNombreAlgoritmo());
        List<PasoOrdenamiento<T>> pasosTotales = administradorPasos.getPasos();
        ValidacionesUtiles.validarMayorACero(pasosTotales.size(), "Debe haber almenos un paso");
        
        int pasoDesafio = (int) (Math.random() * (pasosTotales.size() - 1)) + 1;
        
        return administradorPasos.getPasos().get(pasoDesafio);
    }
    
    /**
     * Post:
     * Valida si la lista de tamaños que el jugador recuerda 
     * coincide exactamente con el orden de los tamaños de las cajas en el paso X.
     * * @param elementos Lista de cajas con los tamaños en el orden que cree el usuario.
     * @param nroPaso El número de paso que el Main determinó para el desafío.
     * @return true si memorizó y ordenó correctamente, false si falló.
     */
    public boolean verificarEstadosDePasos(List<T> elementos, int nroPaso) {
        ValidacionesUtiles.esDistintoDeNull(elementos, "La lista de elementos a verificar no puede ser nula");

        List<PasoOrdenamiento<T>> pasos = administradorPasos.getPasos();
        
        ValidacionesUtiles.validarRangoNumerico(nroPaso, 0, pasos.size() - 1, "Numero de paso fuera de rango ");
        
        int cantidadReal = pasos.get(nroPaso).getCopiasEnEstePaso().size();
        ValidacionesUtiles.validarVerdadero(elementos.size() == cantidadReal, 
            "La cantidad de elementos tiene q coincidir con la cantidad de elementos que tenia el paso");
        
        List<T> elementosReales = pasos.get(nroPaso).getCopiasEnEstePaso();
      
        for (int i = 0; i < elementosReales.size(); i++) {
            if (!elementosReales.get(i).equals(elementos.get(i))) {
                return false; 
            }
        }
        return true; 
    }
    
    //GETTER SIMPLES-----------------------------------------------------------------
    /**
     * post:
     * @return del historial de pasos de la partida
     */
    public List<PasoOrdenamiento<T>> getHistorialDePasos() {
        return administradorPasos.getPasos();
    }
    
    /**
     * Post:
     * @return el nombre del algoritmo seleccionado para la partida
     */
    public String getNombreAlgoritmo() {
        return ordenador.getNombre();
    }
    
    // SETTERS SIMPLES---------------------------------------------------------------------------------------
    /**
     * Pre:
     * @param elementos no nulos
     * Post:
     * - modifica los elementos
     */
    private void setElementosIniciales(List<T> elementos) {
    	ValidacionesUtiles.esDistintoDeNull(elementos, "La lista de elementos no puede ser nula");
    	this.elementosIniciales = elementos;
    	}
    
    /**
     * Pre:
     * @param ordenador no nulo
     * Post:
     * - modifica el ordenador
     */
    private void setOrdenador(Ordenador<T> ordenador) {
    	ValidacionesUtiles.esDistintoDeNull(ordenador, "El ordenador seleccionado no puede ser nulo");
    	this.ordenador = ordenador;
    	}
    
    /**
     * Pre:
     * @param administrador de pasos no nulo
     * Post:
     * - modifica el administrador de pasos
     */
    private void setAdministradorDePasos(AdministradorDePasos<T> admin) { 
    	ValidacionesUtiles.esDistintoDeNull(admin, "El Administrador de pasos no puede ser nulo");
    	this.administradorPasos = admin; 
    }
}