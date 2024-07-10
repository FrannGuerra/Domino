package vista;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import modelo.IFicha;

public class MesaDomino extends JPanel {
	
	private final int ANCHO_HORIZONTAL = 60;
    private final int ALTO_HORIZONTAL = 30;
    private final int ANCHO_VERTICAL = 30;
    private final int ALTO_VERTICAL = 60;
	private int x_der;
	private int y_der;
	private int x_izq;
	private int y_izq;
	private FichaGrafica ultimaFichaDerecha;
	private FichaGrafica ultimaFichaIzquierda;
	private boolean sentido_der;
	private boolean sentido_izq;
	
	public MesaDomino() {
		super(null);
		setBackground(new Color(0, 128, 0));
		setBorder(new LineBorder(Color.BLACK, 2));
		setPreferredSize(new Dimension(777, 541));
		x_der = 0;
		y_der = 0;
		x_izq = 0; 
		y_izq = 0;		
		ultimaFichaDerecha = null;
		ultimaFichaIzquierda = null;
		sentido_der = false;
		sentido_izq = true;
	}
	
	public void sacarFichas() {
		removeAll();
	}
	
	public void agregarFicha(IFicha ficha, boolean extremo) {
		if (getComponentCount() == 0) {
			agregarPrimeraFicha(ficha);
		} else {
			agregarOtraFicha(ficha, extremo);
		}
	}
	
	private void agregarPrimeraFicha(IFicha ificha) {
		FichaGrafica ficha = new FichaGrafica(ificha, !ificha.esDoble());
		int x = getWidth()/2 - ficha.getWidth()/2;	
		int y = getHeight()/2 - ficha.getHeight()/2;
		ficha.setLocation(x, y);
		x_der = x;
		y_der = y;
		x_izq = x;
		y_izq = y;
		ultimaFichaDerecha = ficha;
		ultimaFichaIzquierda = ficha;
		sentido_der = false;
		sentido_izq = true;
		add(ficha);
	}
	
	private void agregarOtraFicha(IFicha ificha, boolean extremo) {
		boolean horizontal;
		boolean invertida = false;
		boolean comoDoble = true; 
		FichaGrafica ficha = null;
		
		if (extremo) { // SI VA EN EXTREMO IZQUIERDO
			
			if (ultimaFichaIzquierda.esHorizontal()) {	// LA ANTERIOR ES HORIZONTAL
				if (ificha.esDoble()) {	// SI ES DOBLE
					if (sentido_izq) { 	// HAY QUE IR A LA IZQUIERDA
						if (x_izq - ANCHO_VERTICAL - ANCHO_HORIZONTAL < 10) {	// SI AL AGREGARLA VERTICAL COMO DOBLE LUEGO NO ENTRA UNA HORIZONTAL
							// Va arriba vertical, Cambiar sentido a derecha
							horizontal = false;
							y_izq = y_izq - ALTO_VERTICAL;
							sentido_izq = false;
							comoDoble = false;
						} else {												// HAY ESPACIO LUEGO PARA UNA HORIZONTAL:
							// Va vertical a la izq como doble
							horizontal = false;
							x_izq = x_izq - ANCHO_VERTICAL;
							y_izq = y_izq - ALTO_VERTICAL/4;
						}			
					} else {			// HAY QUE IR A LA DERECHA
						if (x_izq + ANCHO_VERTICAL + ANCHO_HORIZONTAL*2 > getWidth()-10) {	// SI AL AGREGARLA VERTICAL COMO DOBLE LUEGO NO ENTRA UNA HORIZONTAL
							// Va arriba vertical, Cambiar sentido a izquierda
							horizontal = false;
							x_izq = x_izq + ANCHO_VERTICAL;
							y_izq = y_izq - ALTO_VERTICAL;
							sentido_izq = true;
							comoDoble = false;
						} else {												// HAY ESPACIO LUEGO PARA UNA HORIZONTAL:
							// Va vertical a la izq como doble
							horizontal = false;
							x_izq = x_izq + ANCHO_VERTICAL*2;
							y_izq = y_izq - ALTO_VERTICAL/4;
						}				
					}
				} else {				// NO ES DOBLE
					if (sentido_izq) {	// HAY QUE IR A LA IZQUIERDA
						if (x_izq - ANCHO_HORIZONTAL < 10) {		// SI AL AGREGARLA HORIZONTAL SE PASA:
							// Va arriba vertical, Cambiar sentido a derecha
							horizontal = false;
							y_izq = y_izq - ALTO_VERTICAL;
							sentido_izq = false;
						} else {									// SI AL AGREGARLA HORIZONTAL NO SE PASA:
							// Va horizontal a la izquierda
							horizontal = true;
							x_izq = x_izq - ANCHO_HORIZONTAL;	
						}
						//Invertir si es necesario
						invertida = ultimaFichaIzquierda.getNum1() != ificha.getNum2();
					} else {			// HAY QUE IR A LA DERECHA
						if (x_izq + ANCHO_HORIZONTAL*2 > getWidth()-10) {		// SI AL AGREGARLA HORIZONTAL SE PASA:
							// Va arriba vertical, Cambiar sentido a izquierda
							horizontal = false;
							x_izq = x_izq + ANCHO_VERTICAL;
							y_izq = y_izq - ALTO_VERTICAL;
							sentido_izq = true;
							invertida = ultimaFichaIzquierda.getNum2() != ificha.getNum2();
						} else {									// SI AL AGREGARLA HORIZONTAL NO SE PASA:
							// Va horizontal a la derecha
							horizontal = true;
							x_izq = x_izq + ANCHO_HORIZONTAL;
							invertida = ultimaFichaIzquierda.getNum2() != ificha.getNum1();
						}
					}
				}
			} else {									// LA ANTERIOR ES VERTICAL
				if (ultimaFichaIzquierda.esDoble()) {	// LA ANTERIOR ES DOBLE
					if (ultimaFichaIzquierda.esColocadaComoDoble()) {	// ESTÁ FUNCIONANDO COMO DOBLE VERTICAL NORMAL
						if (sentido_izq) { 	// HAY QUE IR A LA IZQUIERDA
							// Va horizontal a la izquierda
							horizontal = true;
							x_izq = x_izq - ANCHO_HORIZONTAL;
							y_izq = y_izq + ALTO_HORIZONTAL/2;
							invertida = ultimaFichaIzquierda.getNum1() != ificha.getNum2();
						} else {			// HAY QUE IR A LA DERECHA
							horizontal = true;
							x_izq = x_izq + ANCHO_HORIZONTAL/2;
							y_izq = y_izq + ALTO_HORIZONTAL/2;
							invertida = ultimaFichaIzquierda.getNum2() != ificha.getNum1();
						}
					} else {	// NO ESTÁ FUNCIONANDO COMO DOBLE VERTICAL NORMAL	
						if (sentido_izq) { 	// HAY QUE IR A LA IZQUIERDA
							// Va arriba horizontal a la izquierda
							horizontal = true;
							x_izq = x_izq - ANCHO_HORIZONTAL/2;
							y_izq = y_izq - ALTO_HORIZONTAL;
							invertida = ultimaFichaIzquierda.getNum1() != ificha.getNum2();
						} else {			// HAY QUE IR A LA DERECHA
							horizontal = true;
							y_izq = y_izq - ALTO_HORIZONTAL;
							invertida = ultimaFichaIzquierda.getNum1() != ificha.getNum1();
						}
					}
				} else {								// LA ANTERIOR NO ES DOBLE
					if (sentido_izq) {	// HAY QUE IR A LA IZQUIERDA
						// Va abajo, horizontal
						horizontal = true;
						x_izq = x_izq - ANCHO_HORIZONTAL/2;
						y_izq = y_izq - ALTO_HORIZONTAL;
						invertida = ultimaFichaIzquierda.getNum1() != ificha.getNum2();
					} else {			// HAY QUE IR A LA DERECHA
						horizontal = true;
						y_izq = y_izq - ALTO_HORIZONTAL;
						invertida = ultimaFichaIzquierda.getNum1() != ificha.getNum1();
					}
				}
			}
			
			ficha = new FichaGrafica(ificha, horizontal);
			if (ificha.esDoble()) {
				ficha.setColocadaComoDoble(comoDoble);
			}
			ficha.setInvertida(invertida);
			ficha.setLocation(x_izq, y_izq);
			ultimaFichaIzquierda = ficha;
			
		} else {
			
			if (ultimaFichaDerecha.esHorizontal()) {	// LA ANTERIOR ES HORIZONTAL
				if (ificha.esDoble()) {	// SI ES DOBLE
					if (sentido_der) { 	// HAY QUE IR A LA IZQUIERDA
						if (x_der - ANCHO_VERTICAL - ANCHO_HORIZONTAL < 10) {	// SI AL AGREGARLA VERTICAL COMO DOBLE LUEGO NO ENTRA UNA HORIZONTAL
							// Va abajo vertical, Cambiar sentido a derecha
							horizontal = false;
							y_der = y_der + ALTO_VERTICAL/2;
							sentido_der = false;
							comoDoble = false;
						} else {												// HAY ESPACIO LUEGO PARA UNA HORIZONTAL:
							// Va vertical a la izq como doble
							horizontal = false;
							x_der = x_der - ANCHO_VERTICAL;
							y_der = y_der - ALTO_VERTICAL/4;
						}			
					} else {			// HAY QUE IR A LA DERECHA
						if (x_der + ANCHO_VERTICAL + ANCHO_HORIZONTAL*2 > getWidth()-10) {	// SI AL AGREGARLA VERTICAL COMO DOBLE LUEGO NO ENTRA UNA HORIZONTAL
							// Va abajo vertical, Cambiar sentido a izquierda
							horizontal = false;
							x_der = x_der + ANCHO_VERTICAL;
							y_der = y_der + ALTO_VERTICAL/2;
							sentido_der = true;
							comoDoble = false;
						} else {												// HAY ESPACIO LUEGO PARA UNA HORIZONTAL:
							// Va vertical a la izq como doble
							horizontal = false;
							x_der = x_der + ANCHO_VERTICAL*2;
							y_der = y_der - ALTO_VERTICAL/4;
						}				
					}
				} else {				// NO ES DOBLE
					if (sentido_der) {	// HAY QUE IR A LA IZQUIERDA
						if (x_der - ANCHO_HORIZONTAL < 10) {		// SI AL AGREGARLA HORIZONTAL SE PASA:
							// Va abajo vertical, Cambiar sentido a derecha
							horizontal = false;
							y_der = y_der + ALTO_VERTICAL/2;
							sentido_der = false;
							invertida = ultimaFichaDerecha.getNum1() != ificha.getNum1();
						} else {									// SI AL AGREGARLA HORIZONTAL NO SE PASA:
							// Va horizontal a la izquierda
							horizontal = true;
							x_der = x_der - ANCHO_HORIZONTAL;
							invertida = ultimaFichaDerecha.getNum1() != ificha.getNum2();
						}
					} else {			// HAY QUE IR A LA DERECHA
						if (x_der + ANCHO_HORIZONTAL*2 > getWidth()-10) {		// SI AL AGREGARLA HORIZONTAL SE PASA:
							// Va abajo vertical, Cambiar sentido a izquierda
							horizontal = false;
							x_der = x_der + ANCHO_VERTICAL;
							y_der = y_der + ALTO_VERTICAL/2;
							sentido_der = true;
							
						} else {									// SI AL AGREGARLA HORIZONTAL NO SE PASA:
							// Va horizontal a la derecha
							horizontal = true;
							x_der = x_der + ANCHO_HORIZONTAL;
						}
						invertida = ultimaFichaDerecha.getNum2() != ificha.getNum1();
					}
				}
			} else {									// LA ANTERIOR ES VERTICAL
				if (ultimaFichaDerecha.esDoble()) {	// LA ANTERIOR ES DOBLE
					if (ultimaFichaDerecha.esColocadaComoDoble()) {	// ESTÁ FUNCIONANDO COMO DOBLE VERTICAL NORMAL
						if (sentido_der) { 	// HAY QUE IR A LA IZQUIERDA
							// Va horizontal a la izquierda
							horizontal = true;
							x_der = x_der - ANCHO_HORIZONTAL;
							y_der = y_der + ALTO_HORIZONTAL/2;
							invertida = ultimaFichaDerecha.getNum1() != ificha.getNum2();
						} else {			// HAY QUE IR A LA DERECHA
							horizontal = true;
							x_der = x_der + ANCHO_HORIZONTAL/2;
							y_der = y_der + ALTO_HORIZONTAL/2;
							invertida = ultimaFichaDerecha.getNum2() != ificha.getNum1();
						}
					} else {	// NO ESTÁ FUNCIONANDO COMO DOBLE VERTICAL NORMAL	
						if (sentido_der) { 	// HAY QUE IR A LA IZQUIERDA
							// Va abajo horizontal a la izquierda
							horizontal = true;
							x_der = x_der - ANCHO_HORIZONTAL/2;
							y_der = y_der + ALTO_HORIZONTAL*2;
							invertida = ultimaFichaDerecha.getNum2() != ificha.getNum2();
						} else {			// HAY QUE IR A LA DERECHA
							horizontal = true;
							y_der = y_der + ALTO_HORIZONTAL*2;
							invertida = ultimaFichaDerecha.getNum2() != ificha.getNum1();
						}
					}
				} else {								// LA ANTERIOR NO ES DOBLE
					if (sentido_der) {	// HAY QUE IR A LA IZQUIERDA
						// Va arriba, horizontal
						horizontal = true;
						x_der = x_der - ANCHO_HORIZONTAL/2;
						y_der = y_der + ALTO_HORIZONTAL*2;
						invertida = ultimaFichaDerecha.getNum2() != ificha.getNum2();
					} else {			// HAY QUE IR A LA DERECHA
						horizontal = true;
						y_der = y_der + ALTO_HORIZONTAL*2;
						invertida = ultimaFichaDerecha.getNum2() != ificha.getNum1();
					}
				}
			}
			
			ficha = new FichaGrafica(ificha, horizontal);
			if (ificha.esDoble()) {
				ficha.setColocadaComoDoble(comoDoble);
			}
			ficha.setInvertida(invertida);
			ficha.setLocation(x_der, y_der);
			ultimaFichaDerecha = ficha;
			
		}
		
		add(ficha);

	}

	
	
	

}
