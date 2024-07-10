package app;

import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;
import ar.edu.unlu.rmimvc.servidor.Servidor;
import modelo.Partida;

public class AppServidor {

	public static void main(String[] args) {
		ArrayList<String> ips = Util.getIpDisponibles();
		String ip = (String) JOptionPane.showInputDialog(
				null, 
				"Seleccione la IP en la que escuchará peticiones el servidor", "IP del servidor", 
				JOptionPane.QUESTION_MESSAGE, 
				null,
				ips.toArray(),
				null
		);
		
		String port = (String) JOptionPane.showInputDialog(
				null, 
				"Seleccione el puerto en el que escuchará peticiones el servidor", "Puerto del servidor", 
				JOptionPane.QUESTION_MESSAGE,
				null,
				null,
				8888
		);
		
        String[] opciones = {"Nueva Partida", "Cargar Partida"};
        String opcion = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione una opción",
                "Opciones de Partida",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );
        
        
        Partida modelo = null;
        if (opcion.equals("Nueva Partida")) {
            
            String[] jugadores = {"2 jugadores", "3 jugadores", "4 jugadores"};
            String jugador = (String) JOptionPane.showInputDialog(
                    null,
                    "Seleccione el número de jugadores",
                    "Modo de Juego",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    jugadores,
                    jugadores[0]
            );

            int numJugadores = 2;
            switch (jugador) {
                case "2 jugadores":
                    numJugadores = 2;
                    break;
                case "3 jugadores":
                    numJugadores = 3;
                    break;
                case "4 jugadores":
                    numJugadores = 4;
                    break;
            }
            
            int puntosParaGanar = 50;
            boolean seguir = true;
            while (seguir) {
            	String puntos = (String) JOptionPane.showInputDialog(
                        null,
                        "Ingrese los puntos necesarios para ganar (50 a 150)",
                        "Puntos para Ganar",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        null,
                        "50"
                );
                try {
                    puntosParaGanar = Integer.parseInt(puntos);
                    if (puntosParaGanar >= 50 && puntosParaGanar <= 150) {
                        seguir = false;
                    } else {
                        JOptionPane.showMessageDialog(
                                null,
                                "El valor debe estar entre 50 y 150.",
                                "Valor inválido",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Ingrese un número válido.",
                            "Valor inválido",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
            
            modelo = new Partida(numJugadores, puntosParaGanar);
            
        } else if (opcion.equals("Cargar Partida")) {
        	// Cargar partida
        }
		
        
		Servidor servidor = new Servidor(ip, Integer.parseInt(port));
		try {
			servidor.iniciar(modelo);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (RMIMVCException e) {
			e.printStackTrace();
		}
		
	    
	}
    
}