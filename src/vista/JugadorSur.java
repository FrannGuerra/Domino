package vista;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import modelo.IFicha;

public class JugadorSur extends JPanel {
	
	private JPanel panelFichas;
	private JPanel panelEtiquetas;
	private JLabel nombre;
	private JLabel turno;
	
	private ArrayList<IFicha> fichas;
	
	public JugadorSur() {
		super();
		fichas = new ArrayList<IFicha>();
		crear();
	}
	
	private void crear() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setBackground(new Color(160, 80, 80));
		setPreferredSize(new Dimension(250, 70));
		panelFichas = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelFichas.setBackground(new Color(160, 80, 80));
		panelEtiquetas = new JPanel();
		panelEtiquetas.setBackground(new Color(160, 80, 80));
		panelEtiquetas.setLayout(new BoxLayout(panelEtiquetas, BoxLayout.Y_AXIS));
		panelEtiquetas.setPreferredSize(new Dimension(120, panelEtiquetas.getPreferredSize().height));	
		panelEtiquetas.add(Box.createVerticalGlue());	
		nombre = new JLabel();
		nombre.setFont(new Font("Arial Black", Font.PLAIN, 18));
		nombre.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelEtiquetas.add(nombre);	
		turno = new JLabel("Jugando...");
		turno.setFont(new Font("Tahoma", Font.PLAIN, 15));
		turno.setAlignmentX(Component.CENTER_ALIGNMENT);
		turno.setVisible(false);
		panelEtiquetas.add(turno);
		panelEtiquetas.add(Box.createVerticalGlue());
		add(panelEtiquetas);
		add(Box.createHorizontalStrut(30));	
		add(panelFichas);
	}
	
	public void setNombre(String nombre) {
		this.nombre.setText(nombre);
	}
	
	public void agregarFicha(FichaGrafica ficha) {
		panelFichas.add(ficha);
		fichas.add(ficha.getFicha());
	}
	
	public void eliminarFicha(FichaGrafica ficha) {
		panelFichas.remove(ficha);
		fichas.remove(ficha.getFicha());
	}
	
	public void eliminarFichas() {
		panelFichas.removeAll();
		fichas.clear();
	}
	
	public int getCantidadFichas() {
		return fichas.size();
	}
	
	private boolean sonIguales(IFicha f1, IFicha f2) {
		return f1.getNum1() == f2.getNum1() && f1.getNum2() == f2.getNum2();
	}
	
	private ArrayList<FichaGrafica> obtenerFichasGraficas() {
		ArrayList<FichaGrafica> fichasGraficas = new ArrayList<FichaGrafica>();
		for (Component comp : panelFichas.getComponents()) {
	        if (comp instanceof FichaGrafica) {
	            fichasGraficas.add((FichaGrafica) comp);
	        }
		}
		return fichasGraficas;
	}
	
	private FichaGrafica obtenerFichaGrafica(IFicha ficha) {
		FichaGrafica fichaGrafica = null;
		for (FichaGrafica f: obtenerFichasGraficas()) {
			if (sonIguales(f.getFicha(), ficha)) {
				fichaGrafica = f;
				break;
			}
		}
		return fichaGrafica;
	}
	
	private boolean tieneFicha(ArrayList<IFicha> fichas, IFicha ficha) {
		boolean tiene = false;
    	for (IFicha f : fichas) {
    		if (sonIguales(f, ficha)) {
    			tiene = true;
    			break;
    		}
    	}
    	return tiene;
	}
	
	public boolean tieneFicha(IFicha ficha) {
		return tieneFicha(fichas, ficha);
	}
	
	public void mostrarTurno() {
		turno.setVisible(true);
	}
	
	public void sacarTurno() {
		turno.setVisible(false);
	}
		
	public void sacarViejas(ArrayList<IFicha> fichasActuales) {
		for (FichaGrafica fichaGrafica: obtenerFichasGraficas()) {
			if (!tieneFicha(fichasActuales, fichaGrafica.getFicha())) {
				eliminarFicha(fichaGrafica);
			}
		}
	}
	
	public void sacarSeleccionables() {
		for (FichaGrafica fichaGrafica: obtenerFichasGraficas()) {
			fichaGrafica.setSeleccionable(false);
		}
	}
	
	public void setSeleccionable(IFicha ficha) {
		obtenerFichaGrafica(ficha).setSeleccionable(true);
	}
	
	public void setSeleccionable(ArrayList<IFicha> fichas) {
		for (IFicha ficha: fichas) {
			obtenerFichaGrafica(ficha).setSeleccionable(true);
		}
	}
	
}
