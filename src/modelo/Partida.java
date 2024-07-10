package modelo;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

import ar.edu.unlu.rmimvc.observer.ObservableRemoto;
import commons.Eventos;
import commons.IDJugador;

public class Partida extends ObservableRemoto implements IPartida, Serializable {
	private static final long serialVersionUID = 1L;

	private int cantidadJugadores;
	private int puntosParaGanar;
	
	private ArrayList<Jugador> jugadores;
	private int ronda;
	private Jugador jugadorTurno;
	private Jugador mano;
	private Mesa mesa;
	private Pozo pozo;
	private Ficha fichaJugada;
	private boolean extremo;
	private int jugadoresPasan;
	private int jugadoresNuevaRonda;

	

	public Partida(int cantidadJugadores, int puntosParaGanar) {
		this.cantidadJugadores = cantidadJugadores;
		this.puntosParaGanar = puntosParaGanar;
		
		jugadores = new ArrayList<Jugador>();
		ronda = 0;
		jugadorTurno = null;
		mano = null;
		mesa = new Mesa();
		pozo = new Pozo();
		fichaJugada = null;
		extremo = false;
		jugadoresPasan = 0;	
		jugadoresNuevaRonda = 0;
	}
	
	
	@Override
	public IJugador agregarJugador(String nombre) throws RemoteException {
		IDJugador idjug = null;
		if (jugadores.size() == 0) {
			idjug = IDJugador.JUGADOR1;
		} else if (jugadores.size() == 1) {
			idjug = IDJugador.JUGADOR2;
		} else if (jugadores.size() == 2) {
			if (cantidadJugadores > 2) 
				idjug = IDJugador.JUGADOR3;
		} else if (jugadores.size() == 3) {
			if (cantidadJugadores == 4) 
				idjug = IDJugador.JUGADOR4;
		}
		
		if (idjug != null) {
			Jugador jugador = new Jugador(nombre, idjug);
			jugadores.add(jugador);
			return jugador;
		} else {
			return null;
		}
	}
	
	
	
	
	
	
	
	//// GETTERS QUE UTILIZA EL CONTROLADOR ////
	@Override
	public int getRonda() throws RemoteException {
		return ronda;
	}
	
	@Override
	public int getCantidadJugadores() throws RemoteException {
		return cantidadJugadores;
	}
	
	@Override
	public ArrayList<IFicha> getFichasMesa() throws RemoteException {
		ArrayList<IFicha> fichasMesa = new ArrayList<>();
		fichasMesa.addAll(mesa.getFichasMesa());
		return fichasMesa;
	}

	@Override
	public ArrayList<IFicha> getFichasJugador(IDJugador idjug) throws RemoteException {
		ArrayList<IFicha> fichas = new ArrayList<>();
		fichas.addAll(buscarJugador(idjug).getFichas());
		return fichas;
	}
	
	@Override
	public ArrayList<IFicha> getFichasPozo() throws RemoteException {
		ArrayList<IFicha> fichas = new ArrayList<>();
		fichas.addAll(pozo.getFichas());
		return fichas;
	}

	@Override
	public IFicha getFicha() throws RemoteException {
		return fichaJugada;
	}
	
	@Override
	public boolean getExtremo() throws RemoteException {
		return extremo;
	}
	
	/*
	 * OBTENER EL JUGADOR QUE TIENE EL TURNO:
	 */
	@Override
	public IJugador getJugadorTurno() throws RemoteException {
		return jugadorTurno;
	}
	
	/*
	 * OBTENER LA LISTA DE JUGADORES:
	 */
	@Override
	public ArrayList<IJugador> getJugadores() throws RemoteException {
		ArrayList<IJugador> ijugadores = new ArrayList<>();
		ijugadores.addAll(jugadores);
		return ijugadores;
	}

	/*
	 * OBTENER LAS FICHAS QUE PUEDE PONER UN JUGADOR:
	 */
	@Override
	public ArrayList<IFicha> getFichasPuedePoner(IDJugador idjug) throws RemoteException {
		ArrayList<IFicha> fichas = new ArrayList<>();
		fichas.addAll(fichasPuedePoner(buscarJugador(idjug)));
		return fichas;
	}
	
	@Override
	public int getNumFichasJugador(IDJugador jugador) throws RemoteException {
		return buscarJugador(jugador).getFichas().size();
	}
	///////////////////////////////////////////////////////////////////////////////////
	
	

	
	@Override
	public void iniciarPartida() throws RemoteException {
		notificarObservadores(Eventos.INICIO_PARTIDA);

		ronda = 1;
		ronda();
	}

	/*
	 * INICIAR UNA NUEVA RONDA:
	 */
	@Override
	public void ronda() throws RemoteException {
		jugadoresPasan = 0;
		repartirFichas();
		notificarObservadores(Eventos.NUEVA_RONDA);
		
		if (ronda == 1) {		// SI LA RONDA ES LA PRIMERA:
			jugadaFichaMasAlta();						
		} else {
			pasarTurno();
		}	
	}
	
	private void pasarTurno() throws RemoteException {
		jugadorTurno = siguienteJugador(jugadorTurno);		
		notificarObservadores(Eventos.MOSTRAR_TURNO);
		turno();
	}
	
	
	private void turno() throws RemoteException {
		if (fichasPuedePoner(jugadorTurno).size() > 0 ) {	// SI PUEDE PONER:
			jugadoresPasan = 0;								// reinicio jugadoresPasan
			notificarObservadores(Eventos.JUGADOR_ELEGIR_FICHA);		// notifico que debe elegir una ficha
		}
		else {												// SI NO PUEDE PONER:
			noPuedePoner();
		}
		
	}
	
	
	
	private Jugador siguienteJugador(Jugador jugador) { 
		int indice = jugadores.indexOf(jugador) + 1;
		if (indice == jugadores.size())
			indice = 0;
		return jugadores.get(indice);
	}
	

	private void jugadaFichaMasAlta() throws RemoteException {
		fichaJugada = buscarDobleMasAlto();
		jugadorTurno = jugadorPropietario(fichaJugada);
		
		notificarObservadores(Eventos.COMIENZA_FICHA_ALTA);
		
		mano = jugadorTurno;
		notificarObservadores(Eventos.JUGADOR_TIRAR_DOBLE); 
	}
	

	// Este método se usa cuando se pone la primer ficha de la ronda 1
	// Porque no hace falta obtener la ficha del modelo, ya se sabe que es esa.
	@Override
	public void ponerFicha() throws RemoteException {
		this.extremo = mesa.agregarFicha(fichaJugada);
		jugadaFicha();
	}

	@Override
	public void ponerFicha(IFicha ficha) throws RemoteException {
		fichaJugada = buscarFicha(ficha);
		
		// SI SE PUEDE PONER EN AMBOS EXTREMOS:
		if (mesa.ambosExtremos(fichaJugada)) {	
			notificarObservadores(Eventos.AMBOS_EXTREMOS); // notifico que debe se puede poner a ambos extremos
		}
		// SI NO SE PUEDE PONER EN AMBOS EXTREMOS (SOLO SE PUEDE EN UNO):
		else { 			
			this.extremo = mesa.agregarFicha(fichaJugada);
			jugadaFicha();
		}
	}
	
	/*
	 * PONER UNA FICHA CONOCIENDO SU EXTREMO
	 * SE USA CUANDO LA FICHA SE PUEDE PONER A AMBOS EXTREMOS:
	 */
	@Override
	public void ponerFichaExtremo(boolean extremo) throws RemoteException {
		this.extremo = extremo;
		mesa.agregarFicha(fichaJugada, extremo);
		jugadaFicha();
	}
	
	private void jugadaFicha() throws RemoteException {
		jugadorTurno.eliminarFicha(fichaJugada);	// elimino la ficha del jugador
		notificarObservadores(Eventos.MOSTRAR_FICHAS_JUGADOR);	// actualizo las fichas del jugador
		notificarObservadores(Eventos.MOSTRAR_FICHAS_MESA);				
		verificarDomino();
	}
	
	private void verificarDomino() throws RemoteException {
		if (jugadorTurno.getCantidadFichas() == 0) {
			sumarPuntosGanador(jugadorTurno);
			notificarObservadores(Eventos.DOMINO);
			verificarTerminoPartida();
		}
		else {
			pasarTurno();
		}
	}

	
	
	
	
	
	/*
	 * JUNTAR DEL POZO
	 */
	@Override
	public void juntarPozo() throws RemoteException {
		fichaJugada = pozo.obtenerFichaRandom();		// obtengo una ficha del pozo
		jugadorTurno.agregarFicha(fichaJugada);			// agrego la ficha al jugador
		
		notificarObservadores(Eventos.MOSTRAR_POZO);
		notificarObservadores(Eventos.NUEVAS_FICHAS_JUGADOR);
			
		if (mesa.sePuedePoner(fichaJugada)) {
			notificarObservadores(Eventos.JUNTO_PUEDE_TIRAR);
		} else {
			notificarObservadores(Eventos.JUNTO_NO_PUEDE_TIRAR);
			noPuedePoner();
		}
	}

	private void noPuedePoner() throws RemoteException {
		if (pozo.getTamaño() > 0) {	// SI HAY FICHAS EN EL POZO:
			notificarObservadores(Eventos.JUGADOR_JUNTAR_POZO);	// notifico que debe juntar del pozo
		}
		else {
			jugadoresPasan += 1;
			notificarObservadores(Eventos.PASA);
		}
	}
	
	@Override
	public void pasar() throws RemoteException {
		verificarCierre();
	}
	
	
	private void verificarCierre() throws RemoteException {
		// SI PASAN TODOS LOS JUGADORES (CIERRE):
		if (jugadoresPasan == jugadores.size()) { 
			buscarJugadorMenosPuntos();			// Busco el jugador que tiene menos puntos de fichas
			sumarPuntosGanador(jugadorTurno);	// sumo puntos al ganador
			notificarObservadores(Eventos.CIERRE);
			verificarTerminoPartida();
		}
		// SI NO PASARON TODOS:
		else {
			pasarTurno();	
		}
	}
	
	
	

	private void verificarTerminoPartida() throws RemoteException {
		if (jugadorTurno.getPuntos() >= puntosParaGanar) {
			notificarObservadores(Eventos.FIN_PARTIDA);
		}
		else {
			devolverFichasPozo();	
			ronda += 1;
			notificarObservadores(Eventos.CONFIRMAR_NUEVA_RONDA);
		}
	}
	
	@Override
	public void comenzarNuevaRonda() throws RemoteException {
		jugadoresNuevaRonda++;
		if (jugadoresNuevaRonda == jugadores.size()) {
			jugadoresNuevaRonda = 0;
			ronda();
		}
	}
	
	
	/*
	 * REPARTIR 7 FICHAS A CADA JUGADOR:
	 */
	private void repartirFichas() {
		for (Jugador j: jugadores) {
			for (int i=1; i<=7; i+=1) {
				Ficha f = pozo.obtenerFichaRandom();
				j.agregarFicha(f);
			}
		}
	}
	
	/*
	 * OBTENER EL OBJETO JUGADOR A PARTIR DE SU ID
	 */
	private Jugador buscarJugador(IDJugador idjug) {
		Jugador jugador = null;
		for (Jugador j : jugadores) {
			if (j.getId() == idjug) {
				jugador = j;
			}
		}
		return jugador;
	}
	
	
	
	
	
	
	
	
	
	
	

	
	
	/*
	 * OBTENER LAS FICHAS QUE PUEDE PONER UN JUGADOR 
	 */
	private ArrayList<Ficha> fichasPuedePoner(Jugador jugador) {
		ArrayList<Ficha> fichas = new ArrayList<Ficha>();
		for (Ficha f: jugador.getFichas()) {
			if (mesa.sePuedePoner(f)) {
				fichas.add(f);
			}
		}
		return fichas;
	}
	

	
	/*
	 * BUSCAR EL DOBLE MAS ALTO ENTRE LOS JUGADORES:
	 */
	private Ficha buscarDobleMasAlto() {
		Ficha masAlto = null;
		boolean primerDoble = true;
		for (Jugador j: jugadores) {
			for (Ficha f: j.getFichas()) {
				if (f.esDoble()) {
					if (primerDoble) {
						primerDoble = false;
						masAlto = f;
					} else {
						if (f.getNum1() > masAlto.getNum1()) {
							masAlto = f;
						}
					}
				}
			}
		}
		if (masAlto == null) 
			masAlto = buscarFichaMasAlta();
		return masAlto;
	}
	
	/*
	 * EN CASO DE NO HABER DOBLES, BUSCO LA MAS ALTA
	 * SI HAY MAS DE UNA ELIJO RANDOM:
	 */
	private Ficha buscarFichaMasAlta() { 
		ArrayList<Ficha> mayores = new ArrayList<Ficha>();
		Ficha masAlta = new Ficha(0, 0);
		for (Jugador j: jugadores) {
			for (Ficha f: j.getFichas()) {
				if (f.getValor() > masAlta.getValor()) {
					masAlta = f;
					mayores.clear();
					mayores.add(f);
				} else if (f.getValor() == masAlta.getValor()) {
					mayores.add(f);
				}
			}
		}
		Random random = new Random();
		masAlta = mayores.get(random.nextInt(mayores.size()));
		return masAlta;
	}
	
	/*
	 * OBTENER EL JUGADOR QUE TIENE UNA DETERMINADA FICHA:
	 */
	private Jugador jugadorPropietario(Ficha ficha) { 
		Jugador jugador = null;
		for (Jugador j: jugadores) {
			for (Ficha f: j.getFichas()) {
				if (f == ficha) {
					jugador = j;
				}
			}
		}
		return jugador;
	}
	

	
	/*
	 * OBTENER EL OBJETO FICHA A PARTIR DE SU INTERFAZ:
	 */
	private Ficha buscarFicha(IFicha ificha) {
		Ficha ficha = null;
		for (Ficha f: jugadorTurno.getFichas()) {
			if ((f.getNum1() == ificha.getNum1()) && (f.getNum2() == ificha.getNum2())) {
				ficha = f;
			}
		}
		return ficha;
	}
	
	/*
	 * SUMAR PUNTOS DE LAS FICHAS DE LOS OTROS JUGADORES AL JUGADOR GANADOR
	 */
	private void sumarPuntosGanador(Jugador jugadorGanador) { 
		for (Jugador j: jugadores) {
			if (j != jugadorGanador) 
				jugadorGanador.agregarPuntos(j.getPuntosFichas());
		}
	}

	/*
	 * DEVOLVER LAS FICHAS DE LOS JUGADORES Y DE LA MESA AL POZO:
	 * necesito de una lista auxiliar para que no haya errores de concurrencia
	 */
	private void devolverFichasPozo() {
		ArrayList<Ficha> auxiliar;
		for (Jugador j: jugadores) {
			auxiliar = new ArrayList<Ficha>(j.getFichas());
			for (Ficha f: auxiliar) {
				j.eliminarFicha(f);
				pozo.agregarFicha(f);
			}
		}
		auxiliar = new ArrayList<Ficha>(mesa.getFichasMesa());
		for (Ficha f: auxiliar) {
			mesa.eliminarFicha(f);
			pozo.agregarFicha(f);
		}
		pozo.mezclarPozo();
	}
	
	private void buscarJugadorMenosPuntos() {
		int min = 500;
		for (Jugador j: jugadores) {
			if (j.getPuntosFichas() < min) {
				min = j.getPuntosFichas();
				jugadorTurno = j;
			} else if (j.getPuntosFichas() == min) {
				jugadorTurno = masCercanoAMano(j, jugadorTurno);
			}
		}
	}
	
	private Jugador masCercanoAMano(Jugador j1, Jugador j2) { // retorna el jugador mas cercano a la mano
		int distanciaJ1 = 0;
		int distanciaJ2 = 0;
		Jugador aux = mano;
		while (aux != j1) {
			aux = siguienteJugador(mano);
			distanciaJ1 += 1;
		}
		aux = mano;
		while (aux != j2) {
			aux = siguienteJugador(mano);
			distanciaJ2 += 1;
		}
		if (distanciaJ1 < distanciaJ2)
			aux = j1;
		else
			aux = j2;
		return aux;
	}
	
}
