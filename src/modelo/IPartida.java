package modelo;

import java.rmi.RemoteException;
import java.util.ArrayList;

import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import commons.IDJugador;

public interface IPartida extends IObservableRemoto {

	int getRonda() throws RemoteException;

	ArrayList<IFicha> getFichasJugador(IDJugador idjug) throws RemoteException;

	int getNumFichasPozo() throws RemoteException;

	IFicha getFicha() throws RemoteException;

	boolean getExtremo() throws RemoteException;

	/*
	 * OBTENER EL JUGADOR QUE TIENE EL TURNO:
	 */
	IJugador getJugadorTurno() throws RemoteException;

	/*
	 * OBTENER LA LISTA DE JUGADORES:
	 */
	ArrayList<IJugador> getJugadores() throws RemoteException;

	/*
	 * OBTENER LAS FICHAS QUE PUEDE PONER UN JUGADOR:
	 */
	ArrayList<IFicha> getFichasPuedePoner(IDJugador idjug) throws RemoteException;

	int getNumFichasJugador(IDJugador jugador) throws RemoteException;

	/*
	 * AGREGAR UN JUGADOR:
	 */
	IJugador agregarJugador(String nombre) throws RemoteException;
	
	

	/*
	 * INICIAR LA PARTIDA:
	 */
	void iniciarPartida() throws RemoteException;


	void ponerDoble() throws RemoteException;

	void ponerFicha(IFicha ficha) throws RemoteException;

	/*
	 * PONER UNA FICHA CONOCIENDO SU EXTREMO
	 * SE USA CUANDO LA FICHA SE PUEDE PONER A AMBOS EXTREMOS:
	 */
	void ponerFichaExtremo(boolean extremo) throws RemoteException;

	/*
	 * JUNTAR DEL POZO
	 */
	void juntarPozo() throws RemoteException;

	int getCantidadJugadores() throws RemoteException;

	void pasar() throws RemoteException;
	
	void comenzarNuevaRonda() throws RemoteException;


}