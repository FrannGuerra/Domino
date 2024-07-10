package vista;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

import modelo.IFicha;


public class FichaGrafica extends JPanel {
	
	private JLabel label1;
	private JLabel label2;
	private IFicha ficha;
	private boolean horizontal;
	private boolean seleccionable;
	private boolean colocadaComoDoble;
	private boolean invertida;

	public FichaGrafica(IFicha ficha, boolean horizontal) {
		this.invertida = false;
		this.horizontal = horizontal;
		this.ficha = ficha;
		this.seleccionable = false;
		this.colocadaComoDoble = this.esDoble();
		
		setBorder(new LineBorder(new Color(0, 0, 0)));
		if (horizontal) {
			setSize(60, 30);
			setPreferredSize(new Dimension(60, 30));
			setLayout(new GridLayout(1, 2)); // 1 fila, 2 columnas
		} else {
			setSize(30, 60);
			setPreferredSize(new Dimension(30, 60));
			setLayout(new GridLayout(2, 1)); // 2 filas, 1 columna
		}
		
		
		label1 = new JLabel(Integer.toString(getNum1()));
		label1.setFont(new Font("Tahoma", Font.BOLD, 16));
		label1.setHorizontalAlignment(SwingConstants.CENTER);
        label1.setBorder(new LineBorder(new Color(0, 0, 0)));
		
        label2 = new JLabel(Integer.toString(getNum2()));
        label2.setFont(new Font("Tahoma", Font.BOLD, 16));
        label2.setHorizontalAlignment(SwingConstants.CENTER);
        label2.setBorder(new LineBorder(new Color(0, 0, 0)));

		
		add(label1);
		add(label2);
	}
	
	public boolean esHorizontal() {
		return this.horizontal;
	}
	
	public int getNum1() {
		if (invertida) {
			return this.ficha.getNum2();
		} else {
			return this.ficha.getNum1();
		}
	}
	
	public int getNum2() {
		if (invertida) {
			return this.ficha.getNum1();
		} else {			
			return this.ficha.getNum2();
		}
	}
	
	public boolean esDoble() {
		return this.ficha.esDoble();
	}
	
	public void setSeleccionable(boolean seleccionable) {
		if (this.seleccionable != seleccionable) {
			this.seleccionable = seleccionable;
			if (this.seleccionable) {
				setBorder(new LineBorder(new Color(80, 215, 50)));
			} else {
				setBorder(new LineBorder(new Color(0, 0, 0)));
			}
		}
	}
	
	public void onClick(MouseAdapter listener) {
		this.addMouseListener(listener);
	}

	public IFicha getFicha() {
		return this.ficha;
	}
	
	public void setColocadaComoDoble(boolean colocadaComoDoble) {
		this.colocadaComoDoble = colocadaComoDoble;
	}

	public boolean esColocadaComoDoble() {
		return colocadaComoDoble;
	}
	
	public void setInvertida(boolean invertida) {
		this.invertida = invertida;
		if (invertida) {
			label1.setText(Integer.toString(ficha.getNum2()));
			label2.setText(Integer.toString(ficha.getNum1()));
		} else {
			label1.setText(Integer.toString(ficha.getNum1()));
			label2.setText(Integer.toString(ficha.getNum2()));
		}
	}
}
