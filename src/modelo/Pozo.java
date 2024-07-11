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
	
	public void agregarFicha(Ficha ficha) {
		pozo.add(ficha);
	}
	
	private void crearFichas() {
		Ficha ficha;
		for(int i=0; i<=6; i+=1) {
			for(int j=i; j<=6; j+=1) {
				ficha = new Ficha(i, j);
				agregarFicha(ficha);
			}
		}
		mezclarPozo();
	}
	
	public void eliminarFicha(Ficha ficha) {
		pozo.remove(ficha);
	}
	
	public void mezclarPozo() {
		Collections.shuffle(pozo);
	}
		
	public int getTamaño() {
		return pozo.size();
	}
	
	public Ficha obtenerFicha() {
		Random random = new Random();
		Ficha ficha = pozo.get(random.nextInt(pozo.size()));
		eliminarFicha(ficha);
		return ficha;
	}
	
	public ArrayList<Ficha> getFichas() {
		return pozo;
	}
	
}
