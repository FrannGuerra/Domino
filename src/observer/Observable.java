package observer;

public interface Observable {
	
	public void agregarObservador(Observador observador); // Agrego observadores
	
	public void notificar(Object evento); // Le notifica al observador que sucedió un evento
	
}
