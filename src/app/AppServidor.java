package app;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;
import ar.edu.unlu.rmimvc.servidor.Servidor;
import modelo.Partida;
import serializacion.Serializador;

public class AppServidor {

	private static Serializador serializador = new Serializador("partidas.dat");
	
	public static void main(String[] args) {
		// Creo el archivo
		File archivoPartidas = new File("partidas.dat");
		if (!archivoPartidas.exists()) {
		    try {
		    	archivoPartidas.createNewFile();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		
			
		
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
		
		String[] opciones;
		Object[] objetosRecuperados = serializador.readObjects();
		if (objetosRecuperados != null) {
		    opciones = new String[]{"Nueva Partida", "Cargar Partida"};
		} else {
		    opciones = new String[]{"Nueva Partida"};
		}
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
        	String[] nombresPartidas = new String[objetosRecuperados.length];
			ArrayList<Partida> partidas = new ArrayList<Partida>();
			for (int i=0; i<objetosRecuperados.length; i++) {
				Partida partida = (Partida) objetosRecuperados[i];
				partidas.add(partida);
				nombresPartidas[i] = partida.getNombre();
			}
			

			
				
			String partidaSeleccionada = (String) JOptionPane.showInputDialog(
		        null,
		        "Seleccione una partida",
		        "Partidas Guardadas",
		        JOptionPane.QUESTION_MESSAGE,
		        null,
		        nombresPartidas,
		        nombresPartidas[0]
			);
				
			if (partidaSeleccionada != null) {
			    Partida partida = null;
			    for (Partida p : partidas) {
			        if (p.getNombre() == partidaSeleccionada) {
			            partida = p;
			            break;
			        }
			    }
			    
				if (partida != null) {
			        modelo = partida;
			        modelo.configurarPartidaComoAnterior();	// porque no todos los cambios se guardan


			        
			        /*
			        if (partidas.size() == 1) {	// Borro el archivo si solo estaba esa partida
						serializador.limpiarArchivo();
						System.out.println("Contenido del archivo borrado");
			        } else {	// Reescribo el archivo sin esa partida
			        	
			        	if (partidas.get(0) != partida) {
				        	serializador.writeOneObject(partidas.get(0));
				        	for (int i=1; i<partidas.size(); i++) {
				        		Partida p = partidas.get(i);
				        		if (p != partida) {
				        			serializador.addOneObject(p);
				        		}
				        	}
				        	System.out.println("Archivo sobreescrito");
				        } else {
				        	serializador.writeOneObject(partidas.get(1));
				        	for (int i=2; i<partidas.size(); i++) {
				        		serializador.addOneObject(partidas.get(i));
				        	}
				        	System.out.println("Archivo sobreescrito, era la primera");
				        }
			        	
			        	
			        }
			        */
			        

				}
				
				
				
			}

        }
		
        
		Servidor servidor = new Servidor(ip, Integer.parseInt(port));
		try {
			servidor.iniciar(modelo);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (RMIMVCException e) {
			e.printStackTrace();
		}
		
		
		
		// Cartel para preguntar si quiere guardar la partida:
		while (true) {
			Object[] options = {"Sí", "No"};
		    int respuesta = JOptionPane.showOptionDialog(
		            null,
		            "¿Salir y guardar la partida?",
		            "Guardar partida",
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE,
		            null,
		            options,
		            options[0]
		    );
		    if (respuesta == JOptionPane.YES_OPTION) {
		        try {
		        	// Compruebo si se puede guardar (ronda finalizada) 
		        	if (modelo.sePuedeGuardar()) {
			        	boolean resultadoGuardar;
			        	if (serializador.readObjects() == null) {
			        		System.out.println("Guardando primera partida");
			        		resultadoGuardar = serializador.writeOneObject(modelo);
			        	} else {
			        		System.out.println("Guardando una partida que no es la primera");
			        		resultadoGuardar = serializador.addOneObject(modelo);
			        	}
			        	
			        	if (resultadoGuardar) {
			        		JOptionPane.showMessageDialog(
			        		            null,
			        		            "Partida guardada correctamente.",
			        		            "Éxito",
			        		            JOptionPane.INFORMATION_MESSAGE
			        		);
			        		System.out.println("Se guardó la partida");
			        		modelo.guardarPartida(); 
			        		break;
			        	} else {
			        		JOptionPane.showMessageDialog(
			                        null,
			                        "La partida no se pudó guardar, intente nuevamente.",
			                        "Error",
			                        JOptionPane.ERROR_MESSAGE
			                );
			        	}
		        	} else {
		        		JOptionPane.showMessageDialog(
		                        null,
		                        "La partida no se puede guardar en este momento, debe esperar a que termine la ronda.",
		                        "Error",
		                        JOptionPane.ERROR_MESSAGE
		                );
		        	}	    	
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    } else if (respuesta == JOptionPane.NO_OPTION || respuesta == JOptionPane.CLOSED_OPTION) {
		        System.out.println("Partida no guardada.");
		        break;
		    }
		}
		
		
	    
	}
    
}