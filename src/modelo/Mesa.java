package modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Mesa implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Ficha> fichasMesa;
	private int extremo1;
	private int extremo2;
	
	public Mesa() {
		fichasMesa = new ArrayList<Ficha>();
	}

	// pongo la ficha cuando no hay fichas, o cuando solo va en un extremo:
	public boolean agregarFicha(Ficha ficha) {
		boolean extremo = false;
		if (fichasMesa.size() == 0) {
			fichasMesa.add(ficha);
			extremo1 = ficha.getNum1();
			extremo2 = ficha.getNum2();
		} else {
			extremo = sePuedePonerExtremo(ficha, true);
			agregarFicha(ficha, extremo);
			// le paso el extremo izquierdo, si se puede poner, da true, 
			// si no se puede da false, que es el extremo derecho.
		}
		return extremo;
	}
	
	// pongo la ficha cuando puede ir en los dos extremos, le paso el extremo
	public void agregarFicha(Ficha ficha, boolean extremo) {
		if (extremo) {  // Extremo izquierdo = true
			if (ficha.getNum2() != extremo1) {
				//ficha.intercambiarNumeros();
				extremo1 = ficha.getNum2();				// En vez de intercambiar los dejo como están pero cambio el extremo
			} else {
				extremo1 = ficha.getNum1();
			}
				
			fichasMesa.add(0, ficha);
			//extremo1 = ficha.getNum1();
		}
		else {	// Extremo derecho = false
			if (ficha.getNum1() != extremo2) {
				//ficha.intercambiarNumeros();
				extremo2 = ficha.getNum1();
			} else {
				extremo2 = ficha.getNum2();
			}
			fichasMesa.add(ficha);
			//extremo2 = ficha.getNum2();
		}
	}
	
	public void eliminarFicha(Ficha ficha) {
		fichasMesa.remove(ficha);
	}
	
	public ArrayList<Ficha> getFichasMesa() {
		return fichasMesa;
	}
	
	public boolean sePuedePoner(Ficha ficha) {
		return (fichasMesa.size() == 0 || sePuedePonerExtremo(ficha, false) || sePuedePonerExtremo(ficha, true));
	}

	public boolean ambosExtremos(Ficha ficha) {
		return ( (ficha.getNum1()==extremo1 || ficha.getNum2()==extremo1) && (ficha.getNum1()==extremo2 || ficha.getNum2()==extremo2) );
	}
	
	
	public boolean sePuedePonerExtremo(Ficha ficha, boolean extremo) {
		boolean sePuede = false;
		if (extremo) {
			sePuede =  ficha.getNum1()==extremo1 || ficha.getNum2()==extremo1;
		} else {
			sePuede =  ficha.getNum1()==extremo2 || ficha.getNum2()==extremo2;
		}
		return sePuede;
	}
	
	
	
	
	
	
	
}
