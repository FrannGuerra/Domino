package app;

import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;
import ar.edu.unlu.rmimvc.cliente.Cliente;
import controlador.Controlador;
import vista.IVista;
import vista.VistaGrafica;
import vista.VistaConsola;

public class AppCliente {

	public static void main(String[] args) {
		ArrayList<String> ips = Util.getIpDisponibles();
		String ip = (String) JOptionPane.showInputDialog(
				null, 
				"Seleccione la IP en la que escuchará peticiones el cliente", "IP del cliente", 
				JOptionPane.QUESTION_MESSAGE, 
				null,
				ips.toArray(),
				null
		);
		String port = (String) JOptionPane.showInputDialog(
				null, 
				"Seleccione el puerto en el que escuchará peticiones el cliente", "Puerto del cliente", 
				JOptionPane.QUESTION_MESSAGE,
				null,
				null,
				9999
		);
		String ipServidor = (String) JOptionPane.showInputDialog(
				null, 
				"Seleccione la IP en la corre el servidor", "IP del servidor", 
				JOptionPane.QUESTION_MESSAGE, 
				null,
				null,
				null
		);
		String portServidor = (String) JOptionPane.showInputDialog(
				null, 
				"Seleccione el puerto en el que corre el servidor", "Puerto del servidor", 
				JOptionPane.QUESTION_MESSAGE,
				null,
				null,
				8888
		);
		
		// Elección tipo de vista
        String[] opciones = {"Vista Gráfica", "Vista Consola"};
        int seleccion = JOptionPane.showOptionDialog(
                null,
                "Seleccione el tipo de vista",
                "Tipo de Vista",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );
		
		Controlador controlador = new Controlador();
		IVista vista;
		if (seleccion == 0) {
            vista = new VistaGrafica(controlador);
		} else {
            vista = new VistaConsola(controlador);
		}
		
		Cliente c = new Cliente(ip, Integer.parseInt(port), ipServidor, Integer.parseInt(portServidor));
		try {
			c.iniciar(controlador);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (RMIMVCException e) {
			e.printStackTrace();
		}
	}

}