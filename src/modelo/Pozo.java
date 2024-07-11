package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Pozo implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<Ficha> pozo;
	
	public Pozo() {
		pozo = new ArrayList<Ficha>();
		crearFichas();
	}
	
	private void crearFichas() {
		for(int i=0; i<=6; i++) {
			for(int j=i; j<=6; j++) {
				agregarFicha(new Ficha(i, j));
			}
		}
		mezclarPozo();
	}
	
	public void mezclarPozo() {
		Collections.shuffle(pozo);
	}
	
	public int getTamaño() {
		return pozo.size();
	}
	
	public void agregarFicha(Ficha ficha) {
		pozo.add(ficha);
	}
		
	public void eliminarFicha(Ficha ficha) {
		pozo.remove(ficha);
	}
	
	public Ficha obtenerFicha() {
		Random random = new Random();
		Ficha ficha = pozo.get(random.nextInt(pozo.size()));
		eliminarFicha(ficha);
		return ficha;
	}
}
