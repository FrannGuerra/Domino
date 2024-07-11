package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.jgoodies.forms.layout.FormLayout;

import commons.IDJugador;
import controlador.Controlador;
import modelo.IFicha;
import modelo.IJugador;

import com.jgoodies.forms.layout.CellConstraints;

import javax.swing.border.LineBorder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.awt.Font;
import javax.swing.border.MatteBorder;

public class VistaGrafica extends JFrame implements IVista {
	
	private Controlador controlador;
	private VentanaIngresoJugador ventanaIngreso;
	private Estados estado;
	private ArrayList<IJugador> jugadores;
	private IJugador jugador;
	private Map<IDJugador, String> ubicaciones;
	
	private JPanel mainPanel;
	private JPanel leftPanel;
	private JPanel rightPanel;
	
	private JPanel panelNorte;
	private JugadorSur panelSur;
	private JPanel panelEste;
	private JPanel panelOeste;
	
	private JPanel panelPozo;
	
	private JPanel panelFichasJugadorNorte;
	private JPanel panelFichasJugadorEste;
	private JPanel panelFichasJugadorOeste;
	
	private JLabel labelNombreJugadorNorte;
	private JLabel labelNombreJugadorEste;
	private JLabel labelNombreJugadorOeste;
	
	private JLabel labelTurnoJugadorNorte;
	private JLabel labelTurnoJugadorEste;
	private JLabel labelTurnoJugadorOeste;
	
	// Array de fichas de cada jugador;
	private ArrayList<FichaGraficaReves> fichasJugadorNorte;
	//private ArrayList<FichaGrafica> fichasJugadorSur;
	private ArrayList<FichaGraficaReves> fichasJugadorEste;
	private ArrayList<FichaGraficaReves> fichasJugadorOeste;
	// Array fichas pozo:
	private ArrayList<FichaGraficaReves> fichasPozo;
	// Array de fichas de la mesa:
	private ArrayList<FichaGrafica> fichasMesa;
	
	IFicha fichaATirar;	// Para comprobar si apretó la ficha correcta
	
	private IJugador jugadorTurno;	// Para manejar los label

	private MesaDomino mesa;
	
	private JTextArea infoPartida;
	
	private JLabel labelRonda;
	
	private JLabel labelPuntosJugadorNorte;
	private JLabel labelPuntosJugadorSur;
	private JLabel labelPuntosJugadorEste;
	private JLabel labelPuntosJugadorOeste;
	
	
	private JPanel panelEleccionExtremos;
	private JPanel panelPresionarPasar;
	private JPanel panelNuevaRonda;
	
	
	
	public VistaGrafica(Controlador controlador) {
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
                    JOptionPane.showMessageDialog(VistaGrafica.this,"Debe ingresar un nombre válido, de no más de 8 caracteres","Error",JOptionPane.ERROR_MESSAGE);
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
    	iniciar();
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
	
	private void iniciar() {
		setTitle("Dominó");
		setBackground(new Color(160, 80, 80));
		
        // Configurar el FormLayout para la pantalla dividida
        String columnSpecs = "pref:grow, 4dlu, 280px"; // 280px para el panel derecho
        String rowSpecs = "fill:pref:grow";
        FormLayout layout = new FormLayout(columnSpecs, rowSpecs);
        mainPanel = new JPanel(layout);
        CellConstraints cc = new CellConstraints();


        // Panel IZQUIERDO con borderlayout donde va el tablero y los jugadores
        leftPanel = new JPanel(new BorderLayout());
        
        
        // Panel para fichas del DOMINÓ armado
		mesa = new MesaDomino();
		leftPanel.add(mesa, BorderLayout.CENTER);

		
		// Panel para el jugador Sur (principal)
		
		panelSur = new JugadorSur();
		leftPanel.add(panelSur, BorderLayout.SOUTH);
		
		// Panel para el jugador Norte 
		panelNorte = new JPanel();
		panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.X_AXIS));
		panelNorte.setBackground(new Color(160, 80, 80));
		panelNorte.setPreferredSize(new Dimension(250, 70));
		panelFichasJugadorNorte = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panelFichasJugadorNorte.setBackground(new Color(160, 80, 80));
		panelNorte.add(panelFichasJugadorNorte);
		panelNorte.add(Box.createHorizontalStrut(30));
		JPanel panelEtiquetasNorte = new JPanel();
		panelEtiquetasNorte.setBackground(new Color(160, 80, 80));
		panelEtiquetasNorte.setLayout(new BoxLayout(panelEtiquetasNorte, BoxLayout.Y_AXIS));
		panelEtiquetasNorte.setPreferredSize(new Dimension(120, panelEtiquetasNorte.getPreferredSize().height));	// Ancho fijo		
		panelEtiquetasNorte.add(Box.createVerticalGlue());	
		labelNombreJugadorNorte = new JLabel();
		labelNombreJugadorNorte.setFont(new Font("Arial Black", Font.PLAIN, 18));
		labelNombreJugadorNorte.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelEtiquetasNorte.add(labelNombreJugadorNorte);	
		labelTurnoJugadorNorte = new JLabel("Jugando...");
		labelTurnoJugadorNorte.setFont(new Font("Tahoma", Font.PLAIN, 15));
		labelTurnoJugadorNorte.setAlignmentX(Component.CENTER_ALIGNMENT);
		labelTurnoJugadorNorte.setVisible(false);
		panelEtiquetasNorte.add(labelTurnoJugadorNorte);
		panelEtiquetasNorte.add(Box.createVerticalGlue());
		panelNorte.add(panelEtiquetasNorte);	
		leftPanel.add(panelNorte, BorderLayout.NORTH);
		
		// Panel Oeste (2 jugadores)
		panelOeste = new JPanel();
		panelOeste.setLayout(new BoxLayout(panelOeste, BoxLayout.Y_AXIS));
		panelOeste.setBorder(new MatteBorder(2, 0, 2, 0, (Color) new Color(0, 0, 0)));	// Borde arriba y abajo
		panelOeste.setBackground(new Color(160, 80, 80));
		panelOeste.setPreferredSize(new Dimension(100, 168));
		leftPanel.add(panelOeste, BorderLayout.WEST);

		// Panel Este
		panelEste = new JPanel();
		panelEste.setPreferredSize(new Dimension(100, 168));
		panelEste.setBorder(new MatteBorder(2, 0, 2, 0, (Color) new Color(0, 0, 0)));
		panelEste.setBackground(new Color(160, 80, 80));
		leftPanel.add(panelEste, BorderLayout.EAST);
		

		rightPanel = new JPanel();	// Panel DERECHO con BoxLayout Y donde va infor de la partida
		rightPanel.setBackground(new Color(160, 80, 80));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(Box.createVerticalStrut(5));  
        
        // Título "datos de la partida"
        JLabel datosPartida = new JLabel("<html><div style='text-align: center;'>Datos de la partida</div></html>");
        datosPartida.setAlignmentX(Component.CENTER_ALIGNMENT);
        datosPartida.setFont(new Font("Arial Black", Font.PLAIN, 32));
        rightPanel.add(datosPartida);
        rightPanel.add(Box.createVerticalStrut(10));
       
        // TextArea para informar lo que ocurre en la partida
        infoPartida = new JTextArea();
        infoPartida.setBackground(new Color(211, 165, 165));
        infoPartida.setLineWrap(true); 
        infoPartida.setWrapStyleWord(true);
        infoPartida.setFont(new Font("Arial", Font.PLAIN, 16));
        infoPartida.setEditable(false);
        JScrollPane scrollPane1 = new JScrollPane(infoPartida);
        scrollPane1.setPreferredSize(new Dimension(250, 250));
        scrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JPanel panelAuxiliar = new JPanel();
        panelAuxiliar.setLayout(new BoxLayout(panelAuxiliar, BoxLayout.X_AXIS));
        panelAuxiliar.setBackground(new Color(160, 80, 80));
        panelAuxiliar.add(Box.createHorizontalGlue());
        panelAuxiliar.add(scrollPane1);
        panelAuxiliar.add(Box.createHorizontalGlue());
        rightPanel.add(panelAuxiliar);
        println("Bienvenido al juego DOMINÓ");
        println();
        rightPanel.add(Box.createVerticalStrut(10));
        
        // Etiqueta para informar el número de ronda
        labelRonda = new JLabel("Ronda:");
        labelRonda.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelRonda.setFont(new Font("Tahoma", Font.BOLD, 24));
        rightPanel.add(labelRonda);
        rightPanel.add(Box.createVerticalStrut(10));
        
        
        labelPuntosJugadorSur = new JLabel();
        labelPuntosJugadorSur.setFont(new Font("Tahoma", Font.PLAIN, 20));
        labelPuntosJugadorSur.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelPuntosJugadorSur.setVisible(false);
        rightPanel.add(labelPuntosJugadorSur);
        labelPuntosJugadorNorte = new JLabel();
        labelPuntosJugadorNorte.setFont(new Font("Tahoma", Font.PLAIN, 20));
        labelPuntosJugadorNorte.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelPuntosJugadorNorte.setVisible(false);
        rightPanel.add(labelPuntosJugadorNorte);
        labelPuntosJugadorEste = new JLabel();
        labelPuntosJugadorEste.setFont(new Font("Tahoma", Font.PLAIN, 20));
        labelPuntosJugadorEste.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelPuntosJugadorEste.setVisible(false);
        rightPanel.add(labelPuntosJugadorEste);
        labelPuntosJugadorOeste = new JLabel();
        labelPuntosJugadorOeste.setFont(new Font("Tahoma", Font.PLAIN, 20));
        labelPuntosJugadorOeste.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelPuntosJugadorOeste.setVisible(false);
        rightPanel.add(labelPuntosJugadorOeste);
        rightPanel.add(Box.createVerticalStrut(10));
 		
       
        panelEleccionExtremos = new JPanel();
        panelEleccionExtremos.setBackground(new Color(160, 80, 80));
        JLabel label = new JLabel("¿En qué extremo desea colocar la ficha?");
        label.setFont(new Font("Tahoma", Font.BOLD, 14));
        panelEleccionExtremos.add(label);
        JButton leftButton = new JButton("Izquierda");
        leftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	panelEleccionExtremos.setVisible(false);
            	estado = Estados.VISTA_INICIADA;
                controlador.ponerFichaExtremo(true);
            }
        });
        JButton rightButton = new JButton("Derecha");
        rightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	panelEleccionExtremos.setVisible(false);
            	estado = Estados.VISTA_INICIADA;
            	controlador.ponerFichaExtremo(false);
            }
        });
        panelEleccionExtremos.add(leftButton);
        panelEleccionExtremos.add(rightButton);
        panelEleccionExtremos.setVisible(false);
        rightPanel.add(panelEleccionExtremos);
        
        panelPresionarPasar = new JPanel();
        panelPresionarPasar.setBackground(new Color(160, 80, 80));
        JLabel labelPasar = new JLabel("Presione el botón para pasar turno");
        labelPasar.setFont(new Font("Tahoma", Font.BOLD, 14));
        panelPresionarPasar.add(labelPasar);
        JButton botonPasar = new JButton("Pasar");
        botonPasar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelPresionarPasar.setVisible(false);
                estado = Estados.VISTA_INICIADA;
                controlador.pasar();
            }
        });
        panelPresionarPasar.add(botonPasar);
        panelPresionarPasar.setVisible(false);
        rightPanel.add(panelPresionarPasar);
        
    	panelNuevaRonda = new JPanel();
    	panelNuevaRonda.setBackground(new Color(160, 80, 80));
    	JLabel labelNuevaRonda = new JLabel("Presioná el botón si querés");
    	JLabel labelNuevaRonda2 = new JLabel("comenzar una nueva ronda");
    	labelNuevaRonda.setFont(new Font("Tahoma", Font.BOLD, 14));
    	labelNuevaRonda2.setFont(new Font("Tahoma", Font.BOLD, 14));
    	panelNuevaRonda.add(labelNuevaRonda);
    	panelNuevaRonda.add(labelNuevaRonda2);
    	JButton botonNuevaRonda = new JButton("Nueva Ronda");
    	botonNuevaRonda.addActionListener(new ActionListener() {
    	    @Override
    	    public void actionPerformed(ActionEvent e) {
    	        panelNuevaRonda.setVisible(false);
    	        estado = Estados.VISTA_INICIADA;
    	        controlador.nuevaRonda();
    	    }
    	});
    	panelNuevaRonda.add(botonNuevaRonda);
    	panelNuevaRonda.setVisible(false);
    	rightPanel.add(panelNuevaRonda);

        
        
        rightPanel.add(Box.createVerticalGlue());
        
     
        // Añadir los paneles al mainPanel
        mainPanel.add(leftPanel, cc.xy(1, 1));
        mainPanel.add(rightPanel, cc.xy(3, 1));

        // Pantallas menores a 1280x720:
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if ((screenSize.width < 1280) || (screenSize.height < 720)) {
	        JScrollPane scrollPane = new JScrollPane(mainPanel);
	        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	        getContentPane().add(scrollPane, BorderLayout.CENTER);
        } else {
        	getContentPane().add(mainPanel, BorderLayout.CENTER);
        }

		setResizable(false);
        setSize(1280, 720);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setVisible(true);
	}	
	
	private void agregarPozo() {
		panelOeste.add(Box.createVerticalStrut(5));
		JLabel labelPozo = new JLabel("POZO");
        labelPozo.setFont(new Font("Arial", Font.BOLD, 18));
        labelPozo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelOeste.add(labelPozo);
        panelOeste.add(Box.createVerticalGlue());    
        JPanel panelFlow = new JPanel(new FlowLayout());
		panelFlow.setBackground(new Color(160, 80, 80));	
		panelPozo = new JPanel(new GridLayout(0, 2, 3, 3));	// Filas dinámicas, dos columnas, espacios de 3 píxeles
		panelPozo.setBackground(new Color(160, 80, 80));
		panelFlow.add(panelPozo);
		panelOeste.add(panelFlow);
		panelOeste.add(Box.createVerticalGlue());
	}
	
	private void agregarJugadorEste() {
		panelEste.setLayout(new BoxLayout(panelEste, BoxLayout.Y_AXIS));
		panelEste.add(Box.createVerticalGlue());
		JPanel panelFlowLayout = new JPanel(new FlowLayout());
		panelFlowLayout.setBackground(new Color(160, 80, 80));	
		panelFichasJugadorEste = new JPanel();
		panelFichasJugadorEste.setLayout(new GridLayout(0, 1, 5, 5));
		panelFichasJugadorEste.setBackground(new Color(160, 80, 80));
		panelFichasJugadorEste.add(new FichaGraficaReves(true)); 	
		panelFichasJugadorEste.add(new FichaGraficaReves(true)); 	
		panelFlowLayout.add(panelFichasJugadorEste);
		panelEste.add(panelFlowLayout);
		JPanel panelEtiquetasEste = new JPanel();
		panelEtiquetasEste.setBackground(new Color(160, 80, 80));
		panelEtiquetasEste.setLayout(new BoxLayout(panelEtiquetasEste, BoxLayout.Y_AXIS));
		panelEtiquetasEste.setPreferredSize(new Dimension(panelEtiquetasEste.getPreferredSize().width, 42));
		labelNombreJugadorEste = new JLabel();
		labelNombreJugadorEste.setFont(new Font("Arial Black", Font.PLAIN, 16));
		labelNombreJugadorEste.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelEtiquetasEste.add(labelNombreJugadorEste);	
		labelTurnoJugadorEste = new JLabel("Jugando...");
		labelTurnoJugadorEste.setFont(new Font("Tahoma", Font.PLAIN, 15));
		labelTurnoJugadorEste.setAlignmentX(Component.CENTER_ALIGNMENT);
		labelTurnoJugadorEste.setVisible(false);
		panelEtiquetasEste.add(labelTurnoJugadorEste);
		panelEste.add(panelEtiquetasEste);
	}
	
	private void agregarJugadorOeste() {
		JPanel panelEtiquetasOeste = new JPanel();
		panelEtiquetasOeste.setBackground(new Color(160, 80, 80));
		panelEtiquetasOeste.setLayout(new BoxLayout(panelEtiquetasOeste, BoxLayout.Y_AXIS));
		panelEtiquetasOeste.setPreferredSize(new Dimension(panelEtiquetasOeste.getPreferredSize().width, 42));
		labelNombreJugadorOeste = new JLabel();
		labelNombreJugadorOeste.setFont(new Font("Arial Black", Font.PLAIN, 16));
		labelNombreJugadorOeste.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelEtiquetasOeste.add(labelNombreJugadorOeste);	
		labelTurnoJugadorOeste = new JLabel("Jugando...");
		labelTurnoJugadorOeste.setFont(new Font("Tahoma", Font.PLAIN, 15));
		labelTurnoJugadorOeste.setAlignmentX(Component.CENTER_ALIGNMENT);
		labelTurnoJugadorOeste.setVisible(false);
		panelEtiquetasOeste.add(labelTurnoJugadorOeste);
		panelOeste.add(panelEtiquetasOeste);
		panelOeste.add(Box.createVerticalGlue());
		JPanel panelFlow = new JPanel(new FlowLayout());
		panelFlow.setBackground(new Color(160, 80, 80));	
		panelFichasJugadorOeste = new JPanel();
		panelFichasJugadorOeste.setLayout(new GridLayout(0, 1, 5, 5));
		panelFichasJugadorOeste.setBackground(new Color(160, 80, 80));	
		panelFlow.add(panelFichasJugadorOeste);
		panelOeste.add(panelFlow);
	}
	
    private void println(String texto) {
    	infoPartida.append(texto + "\n");
    	infoPartida.setCaretPosition(infoPartida.getDocument().getLength());
    }
    
    private void println() {
    	println("");
    }
    
    private void print(String texto) {
    	infoPartida.append(texto);
    	infoPartida.setCaretPosition(infoPartida.getDocument().getLength());
    }
    
    private boolean sonIguales(IFicha f1, IFicha f2) {
    	return (f1.getNum1() == f2.getNum1()) && (f1.getNum2() == f2.getNum2());
    }
    
    private boolean sonIguales(IJugador j1, IJugador j2) {
    	return j1.getId() == j2.getId();
    }
    
    private boolean tieneFicha(ArrayList<IFicha> fichas, IFicha ficha) {
    	boolean tiene = false;
    	for (IFicha f : fichas) {
    		if (sonIguales(f, ficha))
    			tiene = true;
    	}
    	return tiene;
    }
    
    private FichaGrafica crearFichaGrafica(IFicha ficha, boolean horizontal) {
    	FichaGrafica fichaGrafica = new FichaGrafica(ficha, horizontal);
    	fichaGrafica.onClick(new MouseAdapter() {
             @Override
             public void mouseClicked(MouseEvent e) {
            	 // Se clickeo una ficha:
            	 if (estado == Estados.ESPERANDO_ELECCION_PONER_DOBLE) {
            		 // Compruebo que la ficha sea del jugador (puede ser del pozo u otro lugar)
            		
            		 //if (fichaGrafica.getParent() == panelFichasJugadorSur) {
                		 if (sonIguales(fichaGrafica.getFicha(), fichaATirar)) {
                			 estado = Estados.VISTA_INICIADA;
                			 controlador.ponerDoble();
                		 } else {
                			 // Se apretó una ficha que no es la que tiene que tirar
                			 println("No podés colocar esa ficha, tenes que colocar el doble más grande o la ficha más alta");
                			 println();
                		 } 
            		 //}
            	 } 
            	 else if (estado == Estados.ESPERANDO_ELECCION_FICHA) {
            		 //if (fichaGrafica.getParent() == panelFichasJugadorSur) {
            			 if (tieneFicha(controlador.getFichasPuedePoner(jugador.getId()), fichaGrafica.getFicha())) {
            				 estado = Estados.VISTA_INICIADA;
            				 /*for (FichaGrafica f : fichasJugadorSur) {
            					 f.setSeleccionable(false);
            				 }
            				 */
            				 panelSur.sacarSeleccionables();
                			 controlador.ponerFicha(fichaGrafica.getFicha());
            			 } else {
            				 println("No podés colocar esa ficha");
                			 println();
            			 }
            		 //}
            	 } 
            	 else if (estado == Estados.ESPERANDO_ELECCION_PONER_FICHA_JUNTADA) {
            		 //if (fichaGrafica.getParent() == panelFichasJugadorSur) {
                		 if (sonIguales(fichaGrafica.getFicha(), fichaATirar)) {
                			 estado = Estados.VISTA_INICIADA;
                			 controlador.ponerFicha(fichaATirar);
                		 } else {
                			 println("No podés colocar esa ficha, tenes que colocar la que juntaste");
                			 println();
                		 }
            		// }
            	 }
            	 
            	 
             }
    	});
    	return fichaGrafica;
    }
    
    private FichaGrafica buscarFichaGrafica(IFicha datosFicha, ArrayList<FichaGrafica> fichas) {
    	FichaGrafica fichaGrafica = null;
    	for (FichaGrafica ficha : fichas) {
    		if (sonIguales(ficha.getFicha(), datosFicha)) {
    			fichaGrafica = ficha;
    		}
    	}
    	return fichaGrafica;
    }
    
    private FichaGraficaReves crearFichaGraficaReves (boolean horizontal, boolean pozo) {
    	FichaGraficaReves fichaGraficaReves = new FichaGraficaReves(horizontal);
    	if (pozo) {
    		fichaGraficaReves.onClick(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                	if (estado == Estados.ESPERANDO_ELECCION_JUNTAR_POZO) {
                		if (fichaGraficaReves.getParent() == panelPozo) {
                			estado = Estados.VISTA_INICIADA;
		           			controlador.juntarPozo();
                		}
                	}
                }
    		});
    	}
    	return fichaGraficaReves;
    }
    
    
    
	@Override
	public void mostrarInicioPartida(ArrayList<IJugador> jugadores) {
		this.jugadores = jugadores;
		ubicaciones = new HashMap<>();
		fichasJugadorNorte = new ArrayList<FichaGraficaReves>();
		
		
		int numJugadores = jugadores.size();
		labelPuntosJugadorSur.setVisible(true);
		labelPuntosJugadorNorte.setVisible(true);
	    if (numJugadores == 2) {
	        fichasPozo = new ArrayList<FichaGraficaReves>();
	    	agregarPozo();
	    } else if (numJugadores >= 3) {
	    	fichasJugadorEste = new ArrayList<FichaGraficaReves>();
	    	agregarJugadorEste();
	        labelPuntosJugadorEste.setVisible(true);
	        if (numJugadores == 4) {
	        	fichasJugadorOeste = new ArrayList<FichaGraficaReves>();
	        	agregarJugadorOeste();
	            labelPuntosJugadorOeste.setVisible(true);
	        } else {
	        	fichasPozo = new ArrayList<FichaGraficaReves>();
	            agregarPozo();
	            
	        }
	    }
	    
	    
		ArrayList<IJugador> jugadoresOrdenados = new ArrayList<IJugador>();
		int indiceInicial = -1;
		for (int i = 0; i < jugadores.size(); i++) {
			if (jugadores.get(i).getId() == jugador.getId()) {
	            indiceInicial = i;
	            break;
	        }
	    }
        for (int i = indiceInicial; i < jugadores.size(); i++) {
            jugadoresOrdenados.add(jugadores.get(i));
        }
        for (int i = 0; i < indiceInicial; i++) {
            jugadoresOrdenados.add(jugadores.get(i));
        }
	        
		
		
		println("Jugadores de esta partida: ");
    	for (int i=0; i<jugadoresOrdenados.size(); i++) {
    		IJugador jugador = jugadoresOrdenados.get(i);
	        if (i==0) {
	        	ubicaciones.put(jugador.getId(), "sur");
	            panelSur.setNombre(jugador.getNombre());
	            labelPuntosJugadorSur.setText("Puntos de " + jugador.getNombre() + ": " + jugador.getPuntos());
	        } else if (i == 1) { 
        		if (jugadores.size() != 4) {
        			ubicaciones.put(jugador.getId(), "norte");
	        		labelNombreJugadorNorte.setText(jugador.getNombre());
		        	labelPuntosJugadorNorte.setText("Puntos de " + jugador.getNombre() + ": " + jugador.getPuntos());
        		} else {
	        		ubicaciones.put(jugador.getId(), "oeste");
	        		labelNombreJugadorOeste.setText(jugador.getNombre());
	        		labelPuntosJugadorOeste.setText("Puntos de " + jugador.getNombre() + ": " + jugador.getPuntos());
        		}
	        } else if (i == 2) {
        		if (jugadores.size() == 3) {
        			ubicaciones.put(jugador.getId(), "este");
	        		labelNombreJugadorEste.setText(jugador.getNombre());
	        		labelPuntosJugadorEste.setText("Puntos de " + jugador.getNombre() + ": " + jugador.getPuntos());
        		} else {
        			ubicaciones.put(jugador.getId(), "norte");
	        		labelNombreJugadorNorte.setText(jugador.getNombre());
		        	labelPuntosJugadorNorte.setText("Puntos de " + jugador.getNombre() + ": " + jugador.getPuntos());
        		}
	        } else {
	        		ubicaciones.put(jugador.getId(), "este");
	        		labelNombreJugadorEste.setText(jugador.getNombre());
	        		labelPuntosJugadorEste.setText("Puntos de " + jugador.getNombre() + ": " + jugador.getPuntos());
	        }
	        println(" - " + jugador.getNombre());
	    }
		
		
    	println();
    	println("La partida comenzó");
    	println();
    	
    	revalidate();
    	repaint();
    }
    
	
    @Override
	public void nuevaRonda(int ronda, int fichasPozo) {
    	println("Comienza la ronda " + ronda);
    	println();
    	labelRonda.setText("Ronda: " + ronda);
    	
    	// Fichas de los jugadores:
    	for (IJugador jugador : this.jugadores) {
    		if (!sonIguales(jugador, this.jugador)) {
    			String ubicacion = ubicaciones.get(jugador.getId());
    			ArrayList<FichaGraficaReves> fichas;
    			JPanel panelFichas;
    			if (ubicacion == "norte") {
    				fichas = fichasJugadorNorte;
    				panelFichas = panelFichasJugadorNorte;
    			} else if (ubicacion == "este") {
    				fichas = fichasJugadorEste;
    				panelFichas = panelFichasJugadorEste;
    			} else {
    				fichas = fichasJugadorOeste;
    				panelFichas = panelFichasJugadorOeste;
    			}
    			
    			fichas.clear();
    			panelFichas.removeAll();
    			for (int i = 1; i <= controlador.getNumFichasJugador(jugador.getId()); i++) {
    				FichaGraficaReves fichaOponente = crearFichaGraficaReves((ubicacion == "este" || ubicacion == "oeste"), false); // vertical (false), no-pozo (false)
    				panelFichas.add(fichaOponente);
    				fichas.add(fichaOponente);
    			}
    		}
    	}
    	
    	// Pozo
    	if (this.jugadores.size() != 4) {
	    	for (int i=this.fichasPozo.size()+1; i<=fichasPozo; i++) {
				FichaGraficaReves fichaGraficaReves = crearFichaGraficaReves(false, true);	// vertical (false), pozo (true)
				this.fichasPozo.add(fichaGraficaReves);
				panelPozo.add(fichaGraficaReves);
			}
    	}
    	
    	// Fichas Sur
    	panelSur.eliminarFichas();
    	for (IFicha ficha: controlador.getFichasJugador(this.jugador.getId())) {
    		FichaGrafica fichaGrafica = crearFichaGrafica(ficha, false);
    		panelSur.agregarFicha(fichaGrafica);
    	}
 
    	
    	
    	mesa.sacarFichas();
    	
    	revalidate();
    	repaint();
	}
    
    @Override
    public void comienzaFichaAlta(IJugador jugador) {
    	this.jugadorTurno = jugador;
    	if (sonIguales(jugadorTurno, this.jugador)) {
    		println("Comenzás porque sos el jugador que tiene el doble mas alto o la ficha más alta");
    		panelSur.mostrarTurno();
    	} else {
    		println("Comienza " + jugador.getNombre() + ", porque es el jugador que tiene el doble mas alto o la ficha más alta");
    		println();
			String ubicacion = ubicaciones.get(jugador.getId());
			if (ubicacion == "norte") {
				labelTurnoJugadorNorte.setVisible(true);	
			} else if (ubicacion == "este") {
				labelTurnoJugadorEste.setVisible(true);	
			} else {
				labelTurnoJugadorOeste.setVisible(true);	
			} 
    	}
    	
    	revalidate();
    	repaint();
    }
    
	@Override
	public void tirarDoble(IJugador jugadorTurno, IFicha ficha) {
		if (sonIguales(jugadorTurno, this.jugador)) {
    		println("Debes colocarla para empezar");
	    	println();
			fichaATirar = ficha;
			panelSur.setSeleccionable(ficha);
	    	estado = Estados.ESPERANDO_ELECCION_PONER_DOBLE;    	
	    	revalidate();
	    	repaint();
		}
	}
    
    @Override
	public void mostrarFichasJugador(IJugador jugadorTurno) {
    	if (sonIguales(jugadorTurno, this.jugador)) {
    		ArrayList<IFicha> fichasActuales = controlador.getFichasJugador(jugador.getId());
    		if (panelSur.getCantidadFichas() > fichasActuales.size()) {
                panelSur.sacarViejas(fichasActuales);
            } else {
        	    for (IFicha ficha : fichasActuales) {
        	        if (!panelSur.tieneFicha(ficha)) {
        	            FichaGrafica fichaGrafica = crearFichaGrafica(ficha, false);
        	            panelSur.agregarFicha(fichaGrafica);
        	        }
        	    }
            }
     
    	} else {
    		String ubicacion = ubicaciones.get(jugadorTurno.getId());
			ArrayList<FichaGraficaReves> fichas;
			JPanel panelFichas;
			if (ubicacion == "norte") {
				fichas = fichasJugadorNorte;
				panelFichas = panelFichasJugadorNorte;
			} else if (ubicacion == "este") {
				fichas = fichasJugadorEste;
				panelFichas = panelFichasJugadorEste;
			} else {
				fichas = fichasJugadorOeste;
				panelFichas = panelFichasJugadorOeste;
			}
			int numFichas = controlador.getNumFichasJugador(jugadorTurno.getId());
			int diferencia = numFichas-fichas.size();
			if (diferencia > 0) {
				// Agregar nuevas:
				for (int i=1; i<=diferencia; i++) {
					FichaGraficaReves fichaOponente = crearFichaGraficaReves((ubicacion == "este" || ubicacion == "oeste"), false);
					panelFichas.add(fichaOponente);
					fichas.add(fichaOponente);
				}
			} else {
				diferencia = Math.abs(diferencia);
				// Sacar viejas:
				for (int i=1; i<=diferencia; i++) {
					panelFichas.remove(0);
					fichas.remove(0);
				}
			}		    
    	}
    	
    	revalidate();
    	repaint();
	}	
    
    @Override
	public void mostrarFichasMesa(IFicha ificha, boolean extremo) {
    	mesa.agregarFicha(ificha, extremo);
		revalidate();
    	repaint();
	}
  
    @Override
	public void mostrarTurno(IJugador jugadorTurno) {
    	// Saco el turno anterior
	    if (sonIguales(this.jugadorTurno, jugador)) {
	    	panelSur.sacarTurno();
	    } else {
	    	String ubicacion = ubicaciones.get(this.jugadorTurno.getId());
			if (ubicacion == "norte") {
				labelTurnoJugadorNorte.setVisible(false);
			} else if (ubicacion == "este") {
				labelTurnoJugadorEste.setVisible(false);
			} else {
				labelTurnoJugadorOeste.setVisible(false);
			}
	    }
	    
	    // Pongo el nuevo turno
    	this.jugadorTurno = jugadorTurno;
    	
    	// Actualizo en pantalla
    	if (sonIguales(jugadorTurno, jugador)) {
    		println("Es tu turno");
    		panelSur.mostrarTurno();
    	} else {
    		println("Es el turno de " + jugadorTurno.getNombre());
    		String ubicacion = ubicaciones.get(jugadorTurno.getId());
			if (ubicacion == "norte") {
				labelTurnoJugadorNorte.setVisible(true);
			} else if (ubicacion == "este") {
				labelTurnoJugadorEste.setVisible(true);
			} else {
				labelTurnoJugadorOeste.setVisible(true);
			}
    	}
    	println();
    	
    	revalidate();
    	repaint();
	}
    
    @Override
	public void menuEleccionFicha(IJugador jugadorTurno) {
    	if (sonIguales(jugadorTurno, this.jugador)) {
        	ArrayList<IFicha> fichasPuedePoner = controlador.getFichasPuedePoner(jugador.getId());
    		panelSur.setSeleccionable(fichasPuedePoner);
    		println("Seleccioná la ficha que desees colocar");
    		println();
    		estado = Estados.ESPERANDO_ELECCION_FICHA;
    		
    		revalidate();
        	repaint();
    	}
	}
    
    @Override
	public void menuElegirExtremo(IJugador jugadorTurno) {
    	if (sonIguales(jugadorTurno, this.jugador)) {
        	println("La ficha seleccionada se puede colocar en ambos extremos");
        	println("Elegí el extremo donde queres colocarla");
        	println();
        	estado = Estados.ESPERANDO_ELECCION_EXTREMO;
        	panelEleccionExtremos.setVisible(true);
        	revalidate();
        	repaint();
    	}
	}
    
    
	@Override
	public void menuJuntarPozo(IJugador jugadorTurno) {
		if (sonIguales(jugadorTurno, jugador)) {
			println("No podes poner ninguna ficha");
	    	println("Tenés que juntar una ficha del pozo");
	    	println();
	    	estado = Estados.ESPERANDO_ELECCION_JUNTAR_POZO;	
		} else {
    		println(jugadorTurno.getNombre() + " debe juntar del pozo porque no puede tirar");
    		println();
		}
		
		revalidate();
    	repaint();
	}
    
    @Override
    public void mostrarPozo(int fichasPozo) {
    	int sobrantes = this.fichasPozo.size() - fichasPozo;
    	for (int i=1; i<=sobrantes; i++) {
    		this.fichasPozo.remove(0);
    		panelPozo.remove(0);
    	}
    	
    	revalidate();
    	repaint();
    }
    

    
	@Override
	public void juntoPuedeTirar(IJugador jugadorTurno, IFicha ficha) {
		if (sonIguales(jugadorTurno, this.jugador)) {
			println("La ficha que juntaste se puede colocar");
			println();
			fichaATirar = ficha;
			panelSur.setSeleccionable(ficha);
			estado = Estados.ESPERANDO_ELECCION_PONER_FICHA_JUNTADA;
		}
	}   
    
	@Override
	public void juntoNoPuedeTirar(IJugador jugadorTurno, IFicha ficha) {
		if (sonIguales(jugadorTurno, this.jugador)) {
			println("La ficha que juntaste no se puede colocar");
			println();
		}
	}    

	@Override
	public void informarDomino(IJugador jugadorTurno, ArrayList<IJugador> jugadores) {
		println(jugadorTurno.getNombre() + " gana la ronda por DOMINÓ");
    	println();
    	for (IJugador jugador : jugadores) {
    		if (sonIguales(jugador, this.jugador)) {
    			labelPuntosJugadorSur.setText("Puntos de " + jugador.getNombre() + ": " + jugador.getPuntos());
    		} else {
    			String ubicacion = ubicaciones.get(jugador.getId());
    			if (ubicacion == "norte") {
    				labelPuntosJugadorNorte.setText("Puntos de " + jugador.getNombre() + ": " + jugador.getPuntos());
    			} else if (ubicacion == "este") {
    				labelPuntosJugadorEste.setText("Puntos de " + jugador.getNombre() + ": " + jugador.getPuntos());
    			} else {
    				labelPuntosJugadorOeste.setText("Puntos de " + jugador.getNombre() + ": " + jugador.getPuntos());
    			} 
    		}
    	}
	}

	@Override
	public void notificarPasa(IJugador jugadorTurno) {
		if (sonIguales(jugadorTurno, this.jugador)) {
			println("Tenes que pasar porque no podes tirar y no hay fichas en el pozo");
    		println();
    		estado = Estados.ESPERANDO_ELECCION_PASAR;
    		panelPresionarPasar.setVisible(true);
    		revalidate();
    		repaint();
    	} else {
    		println(jugadorTurno.getNombre() + " pasa porque no puede tirar y no hay fichas en el pozo");
    		println();
    	}
	}	
	
	@Override
	public void informarCierre(IJugador jugadorTurno, ArrayList<IJugador> jugadores) {
		println(jugadorTurno.getNombre() + " gana la ronda por CIERRE, todos los jugadores pasaron y fue el jugador que menos puntos tenía en las fichas");
    	println();
    	for (IJugador jugador : jugadores) {
    		if (sonIguales(jugador, this.jugador)) {
    			labelPuntosJugadorSur.setText("Puntos de " + jugador.getNombre() + ": " + jugador.getPuntos());
    		} else {
    			String ubicacion = ubicaciones.get(jugador.getId());
    			if (ubicacion == "norte") {
    				labelPuntosJugadorNorte.setText("Puntos de " + jugador.getNombre() + ": " + jugador.getPuntos());
    			} else if (ubicacion == "este") {
    				labelPuntosJugadorEste.setText("Puntos de " + jugador.getNombre() + ": " + jugador.getPuntos());
    			} else {
    				labelPuntosJugadorOeste.setText("Puntos de " + jugador.getNombre() + ": " + jugador.getPuntos());
    			} 
    		}
    	}
	}
	
    @Override
    public void finRonda() {
    	println("Esperando que todos los jugadores acepten comenzar una nueva ronda...");
    	println();
    	estado = Estados.ESPERANDO_ELECCION_NUEVA_RONDA;
    	panelNuevaRonda.setVisible(true);
    }
	
	@Override
	public void informarTerminoPartida(IJugador jugadorTurno) {
	  	println(jugadorTurno.getNombre() + " ganó la partida, obtuvo " + jugadorTurno.getPuntos() + " puntos");
    	println();
	}
	
}
