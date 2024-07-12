package vista;

import java.util.ArrayList;

import modelo.IFicha;
import modelo.IJugador;

public interface IVista {
	
	void maxJugadores();
	
	void nombreNoCoincide();
	
	void mostrarInicioPartida(ArrayList<IJugador> jugadores);
	
	void mostrarReanudacionPartida(ArrayList<IJugador> jugadores);

	void nuevaRonda(int ronda, int fichasPozo);

	void comienzaFichaAlta(IJugador jugadorTurno);
	
	void tirarDoble(IJugador jugadorTurno, IFicha ficha);
	
	void mostrarFichasJugador(IJugador jugadorTurno);
	
	void mostrarFichasMesa(IFicha ficha, boolean extremo);

	void mostrarTurno(IJugador jugadorTurno);

	void menuEleccionFicha(IJugador jugadorTurno);
	
	void menuElegirExtremo(IJugador jugadorTurno);
	
	void menuJuntarPozo(IJugador jugadorTurno);
	
	void mostrarPozo(int fichasPozo);
	
	void juntoPuedeTirar(IJugador jugadorTurno, IFicha ficha);
	
	void juntoNoPuedeTirar(IJugador jugadorTurno, IFicha ficha);

	void informarDomino(IJugador jugadorTurno, ArrayList<IJugador> jugadores);

	void notificarPasa(IJugador jugadorTurno);
	
	void informarCierre(IJugador jugadorTurno, ArrayList<IJugador> jugadores);
	
	void finRonda();
	
	void informarTerminoPartida(IJugador jugadorTurno);

	void setJugador(IJugador jugador);

	void partidaGuardada();
	
	void saleJugador(IJugador jugadorSalio);





	


}
