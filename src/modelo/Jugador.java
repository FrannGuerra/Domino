package modelo;

import java.io.Serializable;
import java.util.ArrayList;

import commons.IDJugador;

public class Jugador implements IJugador, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private IDJugador id;
	private String nombre;
	private ArrayList<Ficha> fichas;
	private int puntos;
	
	public Jugador(String nombre, IDJugador id) {
		this.id = id;
		this.nombre = nombre;
		this.fichas = new ArrayList<Ficha>();
		this.puntos = 0;
	}
	
	@Override
	public String getNombre() {
		return nombre;
	}
	
	@Override
	public int getPuntos() {
		return puntos;
	}
	
	public void agregarPuntos(int puntos) {
		this.puntos += puntos;
	}
	
	public void agregarFicha(Ficha ficha) {
		fichas.add(ficha);
	}
	
	public void eliminarFicha(Ficha ficha) {
		fichas.remove(ficha);
	}
	
	public ArrayList<Ficha> getFichas() {
		return fichas;
	}

	public int getCantidadFichas() {
		return fichas.size();
	}
	
	public int getPuntosFichas() {
		int suma = 0;
		for (Ficha f: fichas) {
			suma += f.getValor();
		}
		return suma;
	}

	@Override
	public IDJugador getId() {
		return id;
	}
}
