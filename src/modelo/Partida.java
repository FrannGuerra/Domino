package modelo;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

import ar.edu.unlu.rmimvc.observer.ObservableRemoto;
import commons.Eventos;
import commons.IDJugador;

public class Partida extends ObservableRemoto implements IPartida, Serializable {
	private static final long serialVersionUID = 1L;

	private int cantidadJugadores;
	private int puntosParaGanar;
	
	private ManejadorJugadores manejadorJugadores;
	private int ronda;
	private Jugador jugadorTurno;
	private Jugador mano;
	private Mesa mesa;
	private Pozo pozo;
	private Ficha fichaJugada;
	private boolean extremo;
	private int jugadoresPasan;
	private int jugadoresNuevaRonda;

	public Partida(int cantidadJugadores, int puntosParaGanar) {
		this.cantidadJugadores = cantidadJugadores;
		this.puntosParaGanar = puntosParaGanar;
		
		manejadorJugadores = new ManejadorJugadores();
		ronda = 0;
		jugadorTurno = null;
		mano = null;
		mesa = new Mesa();
		pozo = new Pozo();
		fichaJugada = null;
		extremo = false;
		jugadoresPasan = 0;	
		jugadoresNuevaRonda = 0;
	}
	
	
	@Override
	public IJugador agregarJugador(String nombre) throws RemoteException {
		IDJugador idjug = null;
		if (manejadorJugadores.getNumJugadores() == 0) {
			idjug = IDJugador.JUGADOR1;
		} else if (manejadorJugadores.getNumJugadores() == 1) {
			idjug = IDJugador.JUGADOR2;
		} else if (manejadorJugadores.getNumJugadores() == 2) {
			if (cantidadJugadores > 2) 
				idjug = IDJugador.JUGADOR3;
		} else if (manejadorJugadores.getNumJugadores() == 3) {
			if (cantidadJugadores == 4) 
				idjug = IDJugador.JUGADOR4;
		}
		
		if (idjug != null) {
			Jugador jugador = new Jugador(nombre, idjug);
			manejadorJugadores.agregarJugador(jugador);
			return jugador;
		} else {
			return null;
		}
	}
	
	@Override
	public void iniciarPartida() throws RemoteException {
		notificarObservadores(Eventos.INICIO_PARTIDA);
		ronda = 1;
		ronda();
	}

	private void ronda() throws RemoteException {
		jugadoresPasan = 0;
		manejadorJugadores.repartirFichas(pozo);	
		notificarObservadores(Eventos.NUEVA_RONDA);
		if (ronda == 1) {
			jugadaFichaMasAlta();						
		} else {
			mano = manejadorJugadores.siguienteJugador(jugadorTurno);
			pasarTurno();
		}	
	}
	
	
	private void jugadaFichaMasAlta() throws RemoteException {
		fichaJugada = manejadorJugadores.buscarDobleMasAlto();
		jugadorTurno = manejadorJugadores.jugadorPropietario(fichaJugada);
		
		mano = jugadorTurno;
		notificarObservadores(Eventos.COMIENZA_FICHA_ALTA);
		notificarObservadores(Eventos.JUGADOR_TIRAR_DOBLE); 
	}
	
	
	// Cuando el usuario ponga la ficha (evento JUGADOR_TIRAR_DOBLE):
	@Override
	public void ponerDoble() throws RemoteException {
		this.extremo = mesa.agregarFicha(fichaJugada);
		jugadaFicha();
	}
	
	private void jugadaFicha() throws RemoteException {
		jugadorTurno.eliminarFicha(fichaJugada);
		notificarObservadores(Eventos.MOSTRAR_FICHAS_JUGADOR);	
		notificarObservadores(Eventos.MOSTRAR_FICHAS_MESA);				
		if (jugadorTurno.getCantidadFichas() != 0) {
			pasarTurno();
		} else {
			domino();
		}
	}
	
	private void pasarTurno() throws RemoteException {
		jugadorTurno = manejadorJugadores.siguienteJugador(jugadorTurno);	
		notificarObservadores(Eventos.MOSTRAR_TURNO);
		turno();
	}
	

	
	private void turno() throws RemoteException {
		if (jugadorTurno.fichasPuedePoner(mesa).size() > 0 ) {	// SI PUEDE PONER:
			jugadoresPasan = 0;	
			notificarObservadores(Eventos.JUGADOR_ELEGIR_FICHA);
		}
		else {												// SI NO PUEDE PONER:
			noPuedePoner();
		}
		
	}
	
	
	// Cuando el usuario ponga la ficha (evento JUGADOR_ELEGIR_FICHA):
	@Override
	public void ponerFicha(IFicha ficha) throws RemoteException {
		fichaJugada = jugadorTurno.getFicha(ficha);
		if (mesa.ambosExtremos(fichaJugada)) {	
			notificarObservadores(Eventos.AMBOS_EXTREMOS); 
		}
		else { 			
			this.extremo = mesa.agregarFicha(fichaJugada);
			jugadaFicha();
		}
	}
	
	
	// Cuando el usuario elija el extremo (evento AMBOS_EXTREMOS):
	@Override
	public void ponerFichaExtremo(boolean extremo) throws RemoteException {
		this.extremo = extremo;
		mesa.agregarFicha(fichaJugada, extremo);
		jugadaFicha();
	}
	
	private void noPuedePoner() throws RemoteException {
		if (pozo.getTamaño() > 0) {
			notificarObservadores(Eventos.JUGADOR_JUNTAR_POZO);
		}
		else {
			jugadoresPasan++;
			notificarObservadores(Eventos.PASA);
		}
	}


	// Cuando el usuario junte del pozo (evento JUGADOR_JUNTAR_POZO):
	@Override
	public void juntarPozo() throws RemoteException {
		fichaJugada = pozo.obtenerFicha();		
		jugadorTurno.agregarFicha(fichaJugada);			
		
		notificarObservadores(Eventos.MOSTRAR_POZO);
		notificarObservadores(Eventos.NUEVAS_FICHAS_JUGADOR);
			
		if (mesa.sePuedePoner(fichaJugada)) {
			notificarObservadores(Eventos.JUNTO_PUEDE_TIRAR);
		} else {
			notificarObservadores(Eventos.JUNTO_NO_PUEDE_TIRAR);
			noPuedePoner();
		}
	}

	@Override
	public void pasar() throws RemoteException {
		if (jugadoresPasan < manejadorJugadores.getNumJugadores()) { 
			pasarTurno();
		} else {
			cierre();
		}
	}
	
	
	
	
	private void domino() throws RemoteException {
		manejadorJugadores.sumarPuntosGanador(jugadorTurno);	
		notificarObservadores(Eventos.DOMINO);
		verificarTerminoPartida();
	}
	
	private void cierre() throws RemoteException {
		jugadorTurno = manejadorJugadores.buscarJugadorMenosPuntos(mano);
		manejadorJugadores.sumarPuntosGanador(jugadorTurno);
		notificarObservadores(Eventos.CIERRE);
		verificarTerminoPartida();
	}
	

	private void verificarTerminoPartida() throws RemoteException {
		if (jugadorTurno.getPuntos() >= puntosParaGanar) {
			notificarObservadores(Eventos.FIN_PARTIDA);
		}
		else {
			devolverFichasPozo();	
			ronda++;
			notificarObservadores(Eventos.CONFIRMAR_NUEVA_RONDA);
		}
	}
	
	
	private void devolverFichasPozo() {
		manejadorJugadores.devolverFichas(pozo);
		mesa.devolverFichas(pozo);
		pozo.mezclarPozo();
	}
	
	
	@Override
	public void comenzarNuevaRonda() throws RemoteException {
		jugadoresNuevaRonda++;
		if (jugadoresNuevaRonda == manejadorJugadores.getNumJugadores()) {
			jugadoresNuevaRonda = 0;
			ronda();
		}
	}
	
	

	

	

	
	
	////////////////////////////////
	@Override
	public int getCantidadJugadores() throws RemoteException {
		return cantidadJugadores;
	}
	
	@Override
	public int getRonda() throws RemoteException {
		return ronda;
	}
	
	@Override
	public ArrayList<IFicha> getFichasJugador(IDJugador idjug) throws RemoteException {
		ArrayList<IFicha> fichas = new ArrayList<>();
		fichas.addAll(manejadorJugadores.buscarJugador(idjug).getFichas());
		
		return fichas;
	}
	
	@Override
	public int getNumFichasPozo() throws RemoteException {
		return pozo.getTamaño();
	}

	@Override
	public IFicha getFicha() throws RemoteException {
		return fichaJugada;
	}
	
	@Override
	public boolean getExtremo() throws RemoteException {
		return extremo;
	}
	
	/*
	 * OBTENER EL JUGADOR QUE TIENE EL TURNO:
	 */
	@Override
	public IJugador getJugadorTurno() throws RemoteException {
		return jugadorTurno;
	}
	
	/*
	 * OBTENER LA LISTA DE JUGADORES:
	 */
	@Override
	public ArrayList<IJugador> getJugadores() throws RemoteException {
		ArrayList<IJugador> ijugadores = new ArrayList<>();
		ijugadores.addAll(manejadorJugadores.getJugadores());
		
		return ijugadores;
	}

	/*
	 * OBTENER LAS FICHAS QUE PUEDE PONER UN JUGADOR:
	 */
	@Override
	public ArrayList<IFicha> getFichasPuedePoner(IDJugador idjug) throws RemoteException {
		ArrayList<IFicha> fichas = new ArrayList<>();
		fichas.addAll(manejadorJugadores.buscarJugador(idjug).fichasPuedePoner(mesa));
		return fichas;
	}
	
	@Override
	public int getNumFichasJugador(IDJugador jugador) throws RemoteException {
		return manejadorJugadores.buscarJugador(jugador).getFichas().size();
	}
	///////////////////////////////////////////////////////////////////////////////////
	
}
