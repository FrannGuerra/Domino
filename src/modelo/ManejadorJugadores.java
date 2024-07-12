package modelo;

import java.io.Serializable;
import java.util.ArrayList;

import commons.IDJugador;

public class ManejadorJugadores implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Jugador> jugadoresReconectados;
	private ArrayList<Jugador> jugadores;
	
	public ManejadorJugadores() {
		this.jugadores = new ArrayList<Jugador>();
		this.jugadoresReconectados = new ArrayList<Jugador>();
	}
	
	public int getNumJugadores() {
		return jugadores.size();
	}
	
	public ArrayList<Jugador> getJugadores() {
		return jugadores;
	}
	
	public void reiniciarJugadoresReconectados() {
		jugadoresReconectados.clear();
	}
	
	public Jugador cargarJugador(String nombre, int numJugadores) {
		Jugador jugador = null;
		for (Jugador j: jugadores) {
			if (j.getNombre().equals(nombre) && !jugadoresReconectados.contains(j)) {
				jugador = j;
				jugadoresReconectados.add(j);
				System.out.println("ManejadorJugadores: " + j.getNombre() + " reconectado");
				break;
			}
		}
		return jugador;
	}
	
	public Jugador agregarJugador(String nombre, int numJugadores) {
		IDJugador idjug = null;
		Jugador jugador = null;
		if (jugadores.size() == 0) {
			idjug = IDJugador.JUGADOR1;
		} else if (jugadores.size() == 1) {
			idjug = IDJugador.JUGADOR2;
		} else if (jugadores.size() == 2) {
			if (numJugadores > 2) 
				idjug = IDJugador.JUGADOR3;
		} else if (jugadores.size() == 3) {
			if (numJugadores == 4) 
				idjug = IDJugador.JUGADOR4;
		}
		if (idjug != null) {
			jugador = new Jugador(nombre, idjug);
			jugadores.add(jugador);
		}
		return jugador;
	}
	
	public Jugador siguienteJugador(Jugador jugador) {
		int indice = jugadores.indexOf(jugador) + 1;
		if (indice == jugadores.size())
			indice = 0;
		return jugadores.get(indice);
	}
	
	public void repartirFichas(Pozo pozo) {
		for (Jugador j: jugadores) {
			j.juntarFichas(pozo);
		}
	}
	
	public Ficha buscarDobleMasAlto() {
		Ficha doble = null;
		for (Jugador j : jugadores) {
			Ficha fichaJugador = j.obtenerMayorDoble();
			if (fichaJugador != null) {
				if (doble == null) {
					doble = fichaJugador;
				} else if (fichaJugador.getNum1() > doble.getNum1()) {
					doble = fichaJugador;
				}
			}
		}
		if (doble == null) {
			doble = buscarFichaMasAlta();
		}
		return doble;
	}
	
	private Ficha buscarFichaMasAlta() {
		Ficha masAlta = jugadores.get(0).obtenerFichaMasAlta();
		for (int i=1; i<jugadores.size(); i++) {
			Ficha fichaJugador = jugadores.get(i).obtenerFichaMasAlta();
			if (fichaJugador.getValor() > masAlta.getValor()) {
				masAlta = fichaJugador;
			}
		}
		return masAlta;
	}
	
	public Jugador jugadorPropietario(Ficha ficha) {
		Jugador jugador = null;
		for (Jugador j: jugadores) {
			if (j.tieneFicha(ficha)) {
				jugador = j;
				break;
			}
		}
		return jugador;
	}
	
	public Jugador buscarJugadorMenosPuntos(Jugador mano) {
		Jugador jugador = jugadores.get(0);
		for (int i=1; i<jugadores.size(); i++) {
			Jugador j = jugadores.get(i);
			if (j.getPuntosFichas() < jugador.getPuntosFichas()) {
				jugador = j;
			} else if (j.getPuntosFichas() == jugador.getPuntosFichas()) {
				if (distanciaDeMano(j, mano) < distanciaDeMano(jugador, mano)) {
					jugador = j;
				}
			}
		}
		return jugador;
	}
	
	private int distanciaDeMano(Jugador j, Jugador mano) {
		int distancia = 0;
		while(j != mano) {
			distancia++;
			j = siguienteJugador(j);
		}
		return distancia;
	}
	
	public void sumarPuntosGanador(Jugador jugador) {
		for (Jugador j: jugadores) {
			if (j != jugador) {
				jugador.agregarPuntos(j.getPuntosFichas());
			}
		}
	}
	
	public void devolverFichas(Pozo pozo) {
		for (Jugador j: jugadores) {
			j.devolverFichas(pozo);
		}
	}
	
	public Jugador buscarJugador(IDJugador idjug) {
		Jugador jugador = null;
		for (Jugador j : jugadores) {
			if (j.getId() == idjug) {
				jugador = j;
			}
		}
		return jugador;
	}
}
