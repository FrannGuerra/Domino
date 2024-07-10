package controlador;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import commons.Eventos;
import commons.IDJugador;
import modelo.IFicha;
import modelo.IJugador;
import modelo.IPartida;
import vista.IVista;

public class Controlador implements IControladorRemoto, Serializable {

	private static final long serialVersionUID = 1L;
	
	private IPartida modelo;
	private IVista vista;
	//private IJugador jugador;
	
	public Controlador() {
	}
	
	public void setVista(IVista vista) {
		this.vista = vista;
	}
	
	public void agregarJugador(String nombre) {
		try {
			IJugador jugador = modelo.agregarJugador(nombre);
			if (jugador != null) {
				vista.setJugador(jugador);
				if ((jugador.getId() == IDJugador.JUGADOR2 && modelo.getCantidadJugadores() == 2) ||
				(jugador.getId() == IDJugador.JUGADOR3 && modelo.getCantidadJugadores() == 3) ||
				(jugador.getId() == IDJugador.JUGADOR4 && modelo.getCantidadJugadores() == 4)) {
					modelo.iniciarPartida();
				}
			} else {
				vista.maxJugadores();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	
	public void ponerFicha() {
		try {
			modelo.ponerFicha();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<IFicha> getFichasPuedePoner(IDJugador jugador) {
		ArrayList<IFicha> fichas = null;
		try {
			fichas = modelo.getFichasPuedePoner(jugador);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return fichas;
	}
	
	public void ponerFicha(IFicha ficha) {
		try {
			modelo.ponerFicha(ficha);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void ponerFichaExtremo(boolean extremo) {
		try {
			modelo.ponerFichaExtremo(extremo);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void juntarPozo() {
		try {
			modelo.juntarPozo();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void ponerFichaJuntada() {
		try {
			modelo.ponerFicha(modelo.getFicha());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void pasar() {
		try {
			modelo.pasar();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void nuevaRonda() {
		try {
			modelo.comenzarNuevaRonda();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public int getNumFichasJugador(IDJugador jugador) {
		int numFichas = 0;
		try {
			numFichas =  modelo.getNumFichasJugador(jugador);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return numFichas;
	}
	
	public ArrayList<IFicha> getFichasJugador(IDJugador jugador) {
		ArrayList<IFicha> fichas = null;
		try {
			fichas = modelo.getFichasJugador(jugador);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return fichas;
		
	}

	@Override
	public void actualizar(IObservableRemoto modelo, Object object) throws RemoteException {
		if (object instanceof Eventos) {
			Eventos evento = (Eventos) object;
			switch	(evento) {
		
				case INICIO_PARTIDA:
					vista.mostrarInicioPartida(this.modelo.getJugadores());
					break;
					
				case NUEVA_RONDA:
					vista.nuevaRonda(this.modelo.getRonda(), this.modelo.getJugadores(), this.modelo.getFichasPozo().size(), this.modelo.getFichasMesa());
					break;
				
				case COMIENZA_FICHA_ALTA:
					vista.comienzaFichaAlta(this.modelo.getJugadorTurno());
					break;
					
				case JUGADOR_TIRAR_DOBLE:
					vista.tirarDoble(this.modelo.getJugadorTurno(), this.modelo.getFicha());
					break;
				
				case MOSTRAR_FICHAS_JUGADOR:
					vista.mostrarFichasJugador(this.modelo.getJugadorTurno());
					break;	
					
				case MOSTRAR_FICHAS_MESA:
					vista.mostrarFichasMesa(this.modelo.getFicha(), this.modelo.getExtremo());
					break;
					
				case MOSTRAR_TURNO:
					vista.mostrarTurno(this.modelo.getJugadorTurno());
					break;	
					
				case JUGADOR_ELEGIR_FICHA:
					vista.menuEleccionFicha(this.modelo.getJugadorTurno());
					break;	
					
				case AMBOS_EXTREMOS:
					vista.menuElegirExtremo(this.modelo.getJugadorTurno());
					break;	
					
				case JUGADOR_JUNTAR_POZO:
					vista.menuJuntarPozo(this.modelo.getJugadorTurno());
					break;	
					
				case MOSTRAR_POZO:
					vista.mostrarPozo(this.modelo.getFichasPozo().size());
					break;							
				
				case NUEVAS_FICHAS_JUGADOR:
					vista.nuevasFichasJugador(this.modelo.getJugadorTurno());
					break;
					
				case JUNTO_PUEDE_TIRAR:
					vista.juntoPuedeTirar(this.modelo.getJugadorTurno(), this.modelo.getFicha());
					break;	

				case JUNTO_NO_PUEDE_TIRAR:
					vista.juntoNoPuedeTirar(this.modelo.getJugadorTurno(), this.modelo.getFicha());
					break;
	
				case DOMINO:
					vista.informarDomino(this.modelo.getJugadorTurno(), this.modelo.getJugadores());
					break;
					
				case PASA:
					vista.notificarPasa(this.modelo.getJugadorTurno());
					break;						

				case CIERRE:
					vista.informarCierre(this.modelo.getJugadorTurno(), this.modelo.getJugadores());
					break;
					
				case CONFIRMAR_NUEVA_RONDA:
					vista.finRonda();
					break;
				
				case FIN_PARTIDA:
					vista.informarTerminoPartida(this.modelo.getJugadorTurno());
					break;
			}
			
		}
		
	}
	
	@Override
	public <T extends IObservableRemoto> void setModeloRemoto(T modeloRemoto) throws RemoteException {
		this.modelo = (IPartida) modeloRemoto;
	}
	
	
}
