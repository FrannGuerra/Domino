package vista;

import javax.swing.*;

import commons.IDJugador;
import controlador.Controlador;
import modelo.IFicha;
import modelo.IJugador;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class VistaConsola extends JFrame implements IVista {
	
	private Controlador controlador;
	
	private JTextArea textoConsola;
    private JTextField inputConsola;
    private VentanaIngresoJugador ventanaIngreso;
    private Estados estado;
    private ArrayList<IJugador> jugadores;
    private IJugador jugador;
    IFicha fichaATirar;
    
    //private ArrayList<IFicha> fichasMesa;
    
    private ArrayList<String> fichasMesa;
    
    public VistaConsola(Controlador controlador) {
    	this.controlador = controlador;
    	this.controlador.setVista(this);
    	ventanaIngreso();
    }
    
    public void ventanaIngreso() {
    	estado = Estados.VENTANA_INGRESO;
    	ventanaIngreso = new VentanaIngresoJugador();
        ventanaIngreso.setVisible(true);
        ventanaIngreso.onClickIniciar(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = ventanaIngreso.getNombreJugador().trim();
                if ( (!nombre.isEmpty()) && (nombre.length() <= 8) ) {
                	ventanaIngreso.dispose();
                	controlador.agregarJugador(nombre);
                } else {
                    JOptionPane.showMessageDialog(VistaConsola.this,"Debe ingresar un nombre válido, de no más de 8 caracteres","Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        ventanaIngreso.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
    
    @Override
    public void setJugador(IJugador jugador) {
    	this.jugador = jugador;
    	mostrarVistaConsola();
    }
    
	@Override
	public void maxJugadores() {
	    JOptionPane optionPane = new JOptionPane(
	        "Ya se alcanzó el máximo de jugadores, no podés entrar a la partida",
	        JOptionPane.ERROR_MESSAGE,
	        JOptionPane.DEFAULT_OPTION
	    );
	    JDialog dialog = optionPane.createDialog(this, "Error");
	    dialog.addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosed(WindowEvent e) {
	            System.exit(0);
	        }

	        @Override
	        public void windowClosing(WindowEvent e) {
	            System.exit(0);
	        }
	    });
	    dialog.setVisible(true);
	}
    
	public void mostrarVistaConsola() {
    	estado = Estados.VISTA_INICIADA;
    	setVisible(true);
        setTitle("Vista de Consola");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
      
        textoConsola = new JTextArea();
        textoConsola.setEditable(false);
        textoConsola.setBackground(Color.BLACK);
        textoConsola.setForeground(Color.WHITE);
        textoConsola.setFont(new Font("Monospaced", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(textoConsola);
        add(scrollPane, BorderLayout.CENTER);
        println("Bienvenido al juego DOMINÓ");
        println();

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(Color.BLACK);
        
        inputConsola = new JTextField();
        inputConsola.setBackground(Color.BLACK);
        inputConsola.setForeground(Color.WHITE);
        inputConsola.setFont(new Font("Monospaced", Font.PLAIN, 16));
        inputConsola.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String input = inputConsola.getText().trim();
                println(input);
                println();
                
                if (esEntero(input)) {
                	int opcion = Integer.parseInt(input);
                	
                	if (estado == Estados.ESPERANDO_ELECCION_PONER_DOBLE) {
                		if (opcion == 1) {
                			estado = Estados.VISTA_INICIADA;
                			controlador.ponerDoble();
                		}
                		else {
                			println("No ha ingresado una opción válida");
                	    	print("Ingresá 1 para colocar la ficha: ");
                		}
                	}
                	else if (estado == Estados.ESPERANDO_ELECCION_FICHA) {
	            		if ( (opcion >= 1) && (opcion <= controlador.getFichasPuedePoner(jugador.getId()).size())) {
	            			estado = Estados.VISTA_INICIADA;
	            			controlador.ponerFicha(controlador.getFichasPuedePoner(jugador.getId()).get(opcion-1));
	            		}
	            		else {
	            			println("No ha ingresado una opción válida");
	            			print("Seleccione la ficha que desea colocar: ");
	            		}	
                	}
                	else if (estado == Estados.ESPERANDO_ELECCION_EXTREMO) {
                 		if (opcion == 1) {
                 			estado = Estados.VISTA_INICIADA;
                 			controlador.ponerFichaExtremo(true);
                 		} 
                 		else if (opcion == 2) {
                 			estado = Estados.VISTA_INICIADA;
                 			controlador.ponerFichaExtremo(false);
                 		} 
                 		else {
                 			println("No ha ingresado una opción válida");
                 			print("Ingrese el extremo en el que desea colocar la ficha: ");
                 		}
                    }
                	else if (estado == Estados.ESPERANDO_ELECCION_JUNTAR_POZO) {
                		if (opcion == 1) {
                			estado = Estados.VISTA_INICIADA;
                			controlador.juntarPozo();
                		}
                		else {
                			println("No ha ingresado una opción válida");
                	    	print("Ingresá 1 para juntar una ficha del pozo: ");
                		}
                	}
                	else if(estado == Estados.ESPERANDO_ELECCION_PONER_FICHA_JUNTADA) {
                		if (opcion == 1) {
                			estado = Estados.VISTA_INICIADA;
                			controlador.ponerFicha(fichaATirar);
                		}
                		else {
                			println("No ha ingresado una opción válida");
                			print("Ingresá 1 para colocar la ficha ");
                		}
                	}
                	else if(estado == Estados.ESPERANDO_ELECCION_PASAR) {
                		if (opcion == 1) {
                			estado = Estados.VISTA_INICIADA;
                			controlador.pasar();
                		} else {
                			println("No ha ingresado una opción válida");
                			print("Ingresá 1 para pasar ");
                		}
                	}
                	else if(estado == Estados.ESPERANDO_ELECCION_NUEVA_RONDA) {
                		if (opcion == 1) {
                			estado = Estados.VISTA_INICIADA;
                			controlador.nuevaRonda();
                		} else {
                			println("No ha ingresado una opción válida");
                			print("Ingresá 1 si querés comenzar una nueva ronda: ");
                		}
                	}
                	inputConsola.setText("");
                	
                } else {
                	println("No ha ingresado una opción válida");
                	println();
                	inputConsola.setText("");
                }
            }
            
        });       
        inputPanel.add(inputConsola, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
    }
	
    public boolean esEntero(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private void println(String texto) {
    	textoConsola.append(texto + "\n");
    	textoConsola.setCaretPosition(textoConsola.getDocument().getLength());
    }
    
    private void println() {
    	println("");
    }
    
    private void print(String texto) {
    	textoConsola.append(texto);
    	textoConsola.setCaretPosition(textoConsola.getDocument().getLength());
    }
    
    private boolean sonIguales(IFicha f1, IFicha f2) {
    	return (f1.getNum1() == f2.getNum1()) && (f1.getNum2() == f2.getNum2());
    }
    
    private boolean sonIguales(IJugador j1, IJugador j2) {
    	return j1.getId() == j2.getId();
    }
    
    @Override
    public void mostrarInicioPartida(ArrayList<IJugador> jugadores) {
    	this.jugadores = jugadores;
    	fichasMesa = new ArrayList<String>();
    	
    	println("Jugadores de esta partida: ");
    	for (IJugador jugador : jugadores) {
	        String nombre = jugador.getNombre();
	        if (sonIguales(jugador, this.jugador)) {
	            nombre += " (Tú)";
	        }
	        println(" - " + nombre);
	    }
    	println();
    	println("La partida comenzó");
    	println();
    }
    
    @Override
    public void nuevaRonda(int ronda, int fichasPozo) {
    	println("Comienza la ronda " + ronda);
    	println();
    	
    	for (IJugador jugador : this.jugadores) {
    		if (!sonIguales(jugador, this.jugador)) {
    			println("Cantidad de fichas de " + jugador.getNombre() + ": " + controlador.getNumFichasJugador(jugador.getId()));
    		}
    	}
    	println();
    	
    	println("Fichas en el pozo: " + fichasPozo);
    	println();
    	
    	
    	this.fichasMesa.clear();
    	println("Fichas en la mesa:");
    	println();
    	
    	println("Tus fichas: ");
    	for (IFicha ficha : controlador.getFichasJugador(this.jugador.getId())) {
    		print(" [" + ficha.getNum1() + "|" + ficha.getNum2() + "] ");
    	}
    	println();
    	println();
    }
    
    @Override
    public void comienzaFichaAlta(IJugador jugador) {
    	if (sonIguales(jugador, this.jugador)) {
    		println("Comenzás porque sos el jugador que tiene el doble mas alto o la ficha más alta");
    	} else {
    		println("Comienza " + jugador.getNombre() + ", porque es el jugador que tiene el doble mas alto o la ficha más alta");
    		println();
    	}
    }
    
    @Override
    public void tirarDoble(IJugador jugadorTurno, IFicha ficha) {
    	if (sonIguales(jugadorTurno, this.jugador)) {
        	print("Ingresá 1 para colocar la ficha [" + ficha.getNum1() + "|" + ficha.getNum2() + "] : ");
        	estado = Estados.ESPERANDO_ELECCION_PONER_DOBLE;		
    	}
    }
    
    @Override
    public void mostrarFichasJugador(IJugador jugadorTurno) {
    	if (sonIguales(jugadorTurno, this.jugador)) {
        	println("Tus fichas: ");
        	for (IFicha ficha: controlador.getFichasJugador(jugador.getId())) {
        		print(" [" + ficha.getNum1() + "|" + ficha.getNum2() + "] ");
        	}
        	println();
        	println();
    	} else {	// Si no es este jugador:
    		print("Cantidad de fichas de " + jugadorTurno.getNombre() + ": ");
    		println(Integer.toString(controlador.getNumFichasJugador(jugadorTurno.getId())));
    		println();
    	}
    }
    
    @Override
    public void mostrarFichasMesa(IFicha ificha, boolean extremo) {
    	String fichaStr = "[" + ificha.getNum1() + "|" + ificha.getNum2() + "]";
        String fichaInvertidaStr = "[" + ificha.getNum2() + "|" + ificha.getNum1() + "]";
    	
        if (fichasMesa.isEmpty()) {
            fichasMesa.add(fichaStr);
        } else if (extremo) {
            // Agregar a la izquierda
            if (fichasMesa.get(0).startsWith("[" + ificha.getNum2())) {
            	fichasMesa.add(0, fichaStr);
            } else {
            	fichasMesa.add(0, fichaInvertidaStr);
            }
        } else {
            // Agregar a la derecha
            if (fichasMesa.get(fichasMesa.size() - 1).endsWith(ificha.getNum1() + "]")) {
            	fichasMesa.add(fichaStr);
            } else {
            	fichasMesa.add(fichaInvertidaStr);
            }
        }
        
        println("Fichas en la mesa: ");
        println(" " + String.join(" ", fichasMesa));
        println();
    	
    }
    
    @Override
    public void mostrarTurno(IJugador jugadorTurno) {
    	if (sonIguales(jugadorTurno, this.jugador)) {
    		println("Es tu turno");
    	} else {
    		println("Es el turno de " + jugadorTurno.getNombre());
    	}
    	println();
    }
    
    @Override
    public void menuEleccionFicha(IJugador jugadorTurno) {
    	if (sonIguales(jugadorTurno, this.jugador)) {
        	println("Fichas que puede colocar: ");
        	int opcion = 1;
        	for (IFicha f: controlador.getFichasPuedePoner(this.jugador.getId())) {
        		println(opcion +  "- [" + f.getNum1() + "|" + f.getNum2() + "]");
        		opcion += 1;
        	}
        	println();
        	print("Seleccione la ficha que desea colocar: ");
        	estado = Estados.ESPERANDO_ELECCION_FICHA;
    	}
    }
    
    @Override
    public void menuElegirExtremo(IJugador jugadorTurno) {
    	if (sonIguales(jugadorTurno, this.jugador)) {
        	println("La ficha seleccionada se puede colocar en ambos extremos");
        	println("1- Izquierda");
        	println("2- Derecha");
        	println();
        	print("Ingrese el extremo en el que desea colocar la ficha: ");
        	estado = Estados.ESPERANDO_ELECCION_EXTREMO;
    	}
    }
    
    @Override
    public void menuJuntarPozo(IJugador jugadorTurno) {
    	if (sonIguales(jugadorTurno, jugador)) {
	    	println("No podes poner ninguna ficha");
	    	print("Ingresá 1 para juntar una ficha del pozo: ");
	    	estado = Estados.ESPERANDO_ELECCION_JUNTAR_POZO;    		
    	} else {
    		println(jugadorTurno.getNombre() + " debe juntar del pozo porque no puede tirar");
    		println();
    	}
    }
    
    @Override
    public void mostrarPozo(int fichasPozo) {
    	println("Fichas en el pozo: " + fichasPozo);
    	println();
    }
    
    @Override
    public void nuevasFichasJugador(IJugador jugadorTurno) {
    	mostrarFichasJugador(jugadorTurno);
    }
    
    @Override
    public void juntoPuedeTirar(IJugador jugadorTurno, IFicha ficha) {
    	if (sonIguales(jugadorTurno, this.jugador)) {
    		fichaATirar = ficha;
        	println("La ficha que juntaste es [" + ficha.getNum1() + "|" + ficha.getNum2() + "]");
        	println("Se puede colocar");
        	print("Ingresá 1 para colocar la ficha: ");
        	estado = Estados.ESPERANDO_ELECCION_PONER_FICHA_JUNTADA;
    	}
    }
    
    @Override
    public void juntoNoPuedeTirar(IJugador jugadorTurno, IFicha ficha) {
    	if (sonIguales(jugadorTurno, this.jugador)) {
        	println("La ficha que juntaste es [" + ficha.getNum1() + "|" + ficha.getNum2() + "]");
    		println("No se puede colocar");
    		println();
    	}
    }    
    
    @Override
    public void informarDomino(IJugador jugadorTurno, ArrayList<IJugador> jugadores) {
    	println(jugadorTurno.getNombre() + " gana la ronda por DOMINÓ");
    	println();
    	println("Puntos: ");
    	for (IJugador jugador : jugadores) {
    		println(jugador.getNombre() + ": " + jugador.getPuntos() + " puntos");
    	}
    	println();
    }
    
    @Override
    public void notificarPasa(IJugador jugadorTurno) {
    	if (sonIguales(jugadorTurno, this.jugador)) {
    		println("Tenes que pasar porque no podes tirar y no hay fichas en el pozo");
    		print("Ingresá 1 para pasar: ");
    		estado = Estados.ESPERANDO_ELECCION_PASAR;
    	} else {
    		println(jugadorTurno.getNombre() + " pasa porque no puede tirar y no hay fichas en el pozo");
    		println();
    	}
    }
    
    @Override
    public void informarCierre(IJugador jugadorTurno, ArrayList<IJugador> jugadores) {
    	println(jugadorTurno.getNombre() + " gana la ronda por CIERRE, todos los jugadores pasaron y fue el jugador que menos puntos tenía en las fichas");
    	println();
    	println("Puntos: ");
    	for (IJugador jugador : jugadores) {
    		println(jugador.getNombre() + ": " + jugador.getPuntos() + " puntos");
    	}
    	println();
    }
    
    @Override
    public void finRonda() {
    	println("Esperando que todos los jugadores acepten comenzar una nueva ronda...");
    	println();
       	print("Ingresá 1 si querés comenzar una nueva ronda: ");
    	estado = Estados.ESPERANDO_ELECCION_NUEVA_RONDA;
    }
    
    @Override
    public void informarTerminoPartida(IJugador jugadorTurno) {
    	println(jugadorTurno.getNombre() + " ganó la partida, obtuvo " + jugadorTurno.getPuntos() + " puntos");
    	println();
    }


}
