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

	private boolean esPartidaAnterior;
	private	boolean sePuedeGuardar;
	private String nombre;
	private int jugadoresReanudan;
	
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
		this.esPartidaAnterior = false;
		this.sePuedeGuardar = false;
		this.nombre = "";
		jugadoresReanudan = 0;
		
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
		IJugador jugador = null;
		if (esPartidaAnterior) {
			jugador = manejadorJugadores.cargarJugador(nombre, cantidadJugadores);
		} else {
			jugador = manejadorJugadores.agregarJugador(nombre, cantidadJugadores);
		}
		return jugador;
	}
	
	private void setNombre() {
		for (Jugador j: manejadorJugadores.getJugadores()) {
			this.nombre += j.getNombre() + " ";
		}
		this.nombre.trim();
	}
	
	
	@Override
	public void iniciarPartida() throws RemoteException {
		setNombre();
		notificarObservadores(Eventos.INICIO_PARTIDA);
		ronda = 1;
		ronda();
	}
	
	@Override
	public void reanudarPartida() throws RemoteException {
		jugadoresReanudan++;
		if (jugadoresReanudan == cantidadJugadores) {
			jugadoresNuevaRonda = 0;
			notificarObservadores(Eventos.REANUDACION_PARTIDA);
			sePuedeGuardar = false;
			ronda();
		}
	}

	// Cuando el usuario ponga la ficha (evento JUGADOR_TIRAR_DOBLE):
	@Override
	public void ponerDoble() throws RemoteException {
		this.extremo = mesa.agregarFicha(fichaJugada);
		jugadaFicha();
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
	
	// Cuando el usuario junte del pozo (evento JUGADOR_JUNTAR_POZO):
	@Override
	public void juntarPozo() throws RemoteException {
		fichaJugada = pozo.obtenerFicha();		
		jugadorTurno.agregarFicha(fichaJugada);			
		notificarObservadores(Eventos.MOSTRAR_POZO);
		notificarObservadores(Eventos.MOSTRAR_FICHAS_JUGADOR);
		if (mesa.sePuedePoner(fichaJugada)) {
			notificarObservadores(Eventos.JUNTO_PUEDE_TIRAR);
		} else {
			notificarObservadores(Eventos.JUNTO_NO_PUEDE_TIRAR);
			noPuedePoner();
		}
	}
	
	// Cuando el usuario pase (evento PASA):
	@Override
	public void pasar() throws RemoteException {
		if (jugadoresPasan < manejadorJugadores.getNumJugadores()) { 
			pasarTurno();
		} else {
			cierre();
		}
	}
	
	// Cuando el jugador decide jugar una nueva ronda (evento CONFIRMAR_NUEVA_RONDA):
	@Override
	public void comenzarNuevaRonda() throws RemoteException {
		jugadoresNuevaRonda++;
		if (jugadoresNuevaRonda == manejadorJugadores.getNumJugadores()) {
			sePuedeGuardar = false;
			ronda();
		}
	}
	
	////////////////////////////////////////
	//// MÉTODOS PRIVADOS
	
	private void ronda() throws RemoteException {
		jugadoresPasan = 0;
		jugadoresNuevaRonda = 0;
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
		if (jugadorTurno.fichasPuedePoner(mesa).size() > 0 ) {	
			jugadoresPasan = 0;	
			notificarObservadores(Eventos.JUGADOR_ELEGIR_FICHA);
		}
		else {												
			noPuedePoner();
		}
		
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
			sePuedeGuardar = true;
		}
	}
	
	private void devolverFichasPozo() {
		manejadorJugadores.devolverFichas(pozo);
		mesa.devolverFichas(pozo);
		pozo.mezclarPozo();
	}
	
	////////////////////////////////////////
	//// GETTERS PARA EL CONTROLADOR:
	
	@Override
	public int getCantidadJugadores() throws RemoteException {
		return cantidadJugadores;
	}
	
	@Override
	public ArrayList<IJugador> getJugadores() throws RemoteException {
		ArrayList<IJugador> ijugadores = new ArrayList<>();
		ijugadores.addAll(manejadorJugadores.getJugadores());
		return ijugadores;
	}
	
	@Override
	public ArrayList<IFicha> getFichasJugador(IDJugador idjug) throws RemoteException {
		ArrayList<IFicha> fichas = new ArrayList<>();
		fichas.addAll(manejadorJugadores.buscarJugador(idjug).getFichas());
		return fichas;
	}
	
	@Override
	public ArrayList<IFicha> getFichasPuedePoner(IDJugador idjug) throws RemoteException {
		ArrayList<IFicha> fichas = new ArrayList<>();
		fichas.addAll(manejadorJugadores.buscarJugador(idjug).fichasPuedePoner(mesa));
		return fichas;
	}
	
	@Override
	public int getRonda() throws RemoteException {
		return ronda;
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
	
	@Override
	public IJugador getJugadorTurno() throws RemoteException {
		return jugadorTurno;
	}
	
	@Override
	public boolean esPartidaAnterior() throws RemoteException {
		return this.esPartidaAnterior;
	}
	
	////////////////////////////////////////
	//// MÉTODOS PARA EL SERVIDOR:
	
	public boolean sePuedeGuardar() {
		return this.sePuedeGuardar;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public void guardarPartida() throws RemoteException {
		notificarObservadores(Eventos.PARTIDA_GUARDADA);
		System.exit(0);
	}
	
	public void configurarPartidaComoAnterior() {
		this.esPartidaAnterior = true;
		manejadorJugadores.reiniciarJugadoresReconectados();
	}
	
}
