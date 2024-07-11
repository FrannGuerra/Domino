package modelo;

import java.rmi.RemoteException;
import java.util.ArrayList;

import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import commons.IDJugador;

public interface IPartida extends IObservableRemoto {
	
	// Acciones que ejecuta el usuario o controlador:

	IJugador agregarJugador(String nombre) throws RemoteException;
	
	void iniciarPartida() throws RemoteException;

	void ponerDoble() throws RemoteException;

	void ponerFicha(IFicha ficha) throws RemoteException;

	void ponerFichaExtremo(boolean extremo) throws RemoteException;

	void juntarPozo() throws RemoteException;

	void pasar() throws RemoteException;
	
	void comenzarNuevaRonda() throws RemoteException;
	
	
	// Getters para el controlador:
	
	int getCantidadJugadores() throws RemoteException;

	ArrayList<IJugador> getJugadores() throws RemoteException;
	
	ArrayList<IFicha> getFichasJugador(IDJugador idjug) throws RemoteException;
	
	ArrayList<IFicha> getFichasPuedePoner(IDJugador idjug) throws RemoteException;

	int getRonda() throws RemoteException;

	int getNumFichasPozo() throws RemoteException;

	IFicha getFicha() throws RemoteException;

	boolean getExtremo() throws RemoteException;

	IJugador getJugadorTurno() throws RemoteException;
}