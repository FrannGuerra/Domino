package modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Mesa implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private ArrayList<Ficha> fichasMesa;
	private int extremoIzquierda;
	private int extremoDerecha;
	
	public Mesa() {
		fichasMesa = new ArrayList<Ficha>();
	}

	// Agregar una ficha a la mesa cuando no tiene fichas o cuando se puede poner en un solo extremo
	public boolean agregarFicha(Ficha ficha) {
		boolean extremo = false;
		if (fichasMesa.isEmpty()) {
			fichasMesa.add(ficha);
			extremoIzquierda = ficha.getNum1();
			extremoDerecha = ficha.getNum2();
		} else {
			// Si se puede poner en izquierda, da true (izquierda), sino false (derecha)
			extremo = sePuedePonerExtremo(ficha, true);
			agregarFicha(ficha, extremo);
		}
		return extremo;
	}
	
	// Agregar una ficha a la mesa indicando el extremo
	public void agregarFicha(Ficha ficha, boolean extremo) {
		if (extremo) {	// Izquierda
			if (ficha.getNum2() != extremoIzquierda) {
				extremoIzquierda = ficha.getNum2();				
			} else {
				extremoIzquierda = ficha.getNum1();
			}
			fichasMesa.add(0, ficha);
		} else {	// Derecha
			if (ficha.getNum1() != extremoDerecha) {
				
				extremoDerecha = ficha.getNum1();
			} else {
				extremoDerecha = ficha.getNum2();
			}
			fichasMesa.add(ficha);
		}
	}
	
	public void eliminarFicha(Ficha ficha) {
		fichasMesa.remove(ficha);
	}
	
	public boolean sePuedePoner(Ficha ficha) {
		return (fichasMesa.isEmpty() || sePuedePonerExtremo(ficha, false) || sePuedePonerExtremo(ficha, true));
	}

	public boolean ambosExtremos(Ficha ficha) {
		return  (sePuedePonerExtremo(ficha, true) && sePuedePonerExtremo(ficha, false));
	}
	
	public boolean sePuedePonerExtremo(Ficha ficha, boolean extremo) {
		boolean sePuede = false;
		if (extremo) {
			sePuede =  ficha.getNum1()==extremoIzquierda || ficha.getNum2()==extremoIzquierda;
		} else {
			sePuede =  ficha.getNum1()==extremoDerecha || ficha.getNum2()==extremoDerecha;
		}
		return sePuede;
	}
	
	public void devolverFichas(Pozo pozo) {
		ArrayList<Ficha> fichas = new ArrayList<Ficha>(fichasMesa);
		for (Ficha f: fichas) {
			eliminarFicha(f);
			pozo.agregarFicha(f);
		}
	}
}
