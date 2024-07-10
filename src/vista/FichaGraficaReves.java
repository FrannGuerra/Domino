package vista;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class FichaGraficaReves extends JPanel {
	private boolean horizontal;
	
	public FichaGraficaReves(boolean horizontal) {
		this.horizontal = horizontal;
		setOpaque(true);
		setBackground(Color.GRAY);
		setBorder(new LineBorder(new Color(0, 0, 0), 2));
		if (horizontal) {
			setSize(60, 30);
			setPreferredSize(new Dimension(60, 30));
		} else {
			setSize(30, 60);
			setPreferredSize(new Dimension(30, 60));
		}
		
	}
	
	public void onClick(MouseAdapter listener) {
		this.addMouseListener(listener);
	}
}
