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
	public IDJugador getId() {
		return id;
	}
	
	@Override
	public String getNombre() {
		return nombre;
	}
	
	@Override
	public int getPuntos() {
		return puntos;
	}
	
	public ArrayList<Ficha> getFichas() {
		return fichas;
	}
	
	public void agregarFicha(Ficha ficha) {
		fichas.add(ficha);
	}
	
	public void eliminarFicha(Ficha ficha) {
		fichas.remove(ficha);
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
	
	public void juntarFichas(Pozo pozo) {
		for (int i=1; i<=7; i++) {
			agregarFicha(pozo.obtenerFicha());
		}
	}
	
	public void devolverFichas(Pozo pozo) {
		ArrayList<Ficha> fichas = new ArrayList<Ficha>(this.fichas);
		for (Ficha f: fichas) {
			eliminarFicha(f);
			pozo.agregarFicha(f);
		}
	}
	
	public Ficha obtenerMayorDoble() {
		Ficha doble = null;
		for (Ficha f : fichas) {
			if (f.esDoble()) {
				if (doble == null) {
					doble = f;
				} else if (f.getNum1() > doble.getNum1()) {
					doble = f;
				}
			}
		}
		return doble;
	}
	
	public Ficha obtenerFichaMasAlta() {
		Ficha alta = fichas.get(0);
		for (int i=1; i<fichas.size(); i++) {
			Ficha ficha = fichas.get(i);
			if (ficha.getValor() > alta.getValor()) {
				alta = ficha;
			}
		}
		return alta;
	}
	
	public ArrayList<Ficha> fichasPuedePoner(Mesa mesa) {
		ArrayList<Ficha> fichasPuedePoner = new ArrayList<Ficha>();
		for (Ficha f : fichas) {
			if (mesa.sePuedePoner(f)) {
				fichasPuedePoner.add(f);
			}
		}
		return fichasPuedePoner;
	}
	
	// Devuelve la ficha en base a una ificha
	public Ficha getFicha(IFicha ificha) {
		Ficha ficha = null;
		for (Ficha f: fichas) {
			if ((f.getNum1() == ificha.getNum1()) && (f.getNum2() == ificha.getNum2())) {
				ficha = f;
				break;
			}
		}
		return ficha;
	}
	
	public boolean tieneFicha(Ficha ficha) {
		return fichas.contains(ficha);
	}

	public void agregarPuntos(int puntos) {
		this.puntos += puntos;
	}
}
