package modelo;

import java.io.Serializable;

public class Ficha implements IFicha, Serializable {

	private static final long serialVersionUID = 1L;
	
	private int num1;
	private int num2;
	
	public Ficha(int num1, int num2) {
		this.num1 = num1;
		this.num2 = num2;
	}
	
	@Override
	public int getNum1() {
		return num1;
	}

	@Override
	public int getNum2() {
		return num2;
	}

	@Override
	public boolean esDoble() {
		return (num1 == num2);
	}
	
	public int getValor() {
		return (num1 + num2);
	}
	
}
