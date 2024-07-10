package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaIngresoJugador extends JFrame {
	
	private JTextField textoJugador;
    private JButton botonIngresar;
	private JPanel panel;
    
	public VentanaIngresoJugador() {
		setTitle("Ingreso de Nombre");
		setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setSize(300, 150);
        setLocationRelativeTo(null);

        panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel label = new JLabel("Ingrese su nombre:");
        textoJugador = new JTextField(10);
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        inputPanel.add(label);
        inputPanel.add(textoJugador);
        
        botonIngresar = new JButton("Ingresar");
        JPanel botonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botonPanel.add(botonIngresar);
        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(botonPanel, BorderLayout.SOUTH);

        getContentPane().add(panel);
	}
	
	public void onClickIniciar(ActionListener listener) {
		this.botonIngresar.addActionListener(listener);
	}
	
	public String getNombreJugador() {
		return this.textoJugador.getText();
	}
}
