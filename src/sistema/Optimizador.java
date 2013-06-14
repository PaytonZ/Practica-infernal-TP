package sistema;

import inicio.CargarFichero;
import inicio.CrearMatrices;


import java.util.StringTokenizer;

import salidaDeDatos.SalidaDeDatos;


/**
 * Clase que resuleve el problema del viajante mediante feromonas de hormigas
 * 
 * @author Juan Luis Perez Valbuena
 * @author Alvaro Quesada Pimentel
 * @author Juan Carlos Marco Gonzalez
 * @author Daniel Serrano Torres
 */
public class Optimizador {

	// Variables de cuyo significado se desconoce.
	private double TAUZERO;

	// Cantidad de ciudades a visitar.
	private int numerodeciudades;

	// TODO falta comentar estas variables
	private int distancias[][];
	private double visibilidad[][];
	private double feromonas[][];

	// Mejor ruta hasta el momento
	private int mejorrecorrido[];

	// Longitud de la mejor ruta hasta el momento
	private int mejorlongitudderecorrido = Integer.MAX_VALUE;


	// Se usa para indicar de forma rapida la distancia del ciclo
	//private int tourbasico[];
	
	//Variables de hilos
	public static final int numDeHormigas = 50000; //Número de hormigas que se va na ejecutar
	private static final int tamañoLWP = Runtime.getRuntime().availableProcessors();//Disponibilidad de procesadores

	private int hormigasViaje=0;// Numero de hormigas en ejecucion
	private int hormigasEspera=numDeHormigas; //Numero de hormigas en espera para ejecutarse
	
	
	/** Inicia los datos de la clase para su ejecucion.
	 * @param ficheroaabrir
	 */
	public void iniciar(String ficheroaabrir) { 
		
		CargarFichero cargarfichero = new CargarFichero();
		CrearMatrices creadormatrices = new CrearMatrices();

		StringTokenizer contenidofechero = cargarfichero
				.cargarFichero(ficheroaabrir);

		// Se genera una matriz con la clase creadormatrices que decidira
		// dependiendo de la entrada la matriz a crear adecuada
		distancias = creadormatrices.generarMatriz(contenidofechero);

		// Obtención del número de ciudades a partir del tamaño de la matriz
		numerodeciudades = distancias.length;

		// Se contruyen las matrices necesarias a partir del número de ciudades
		mejorrecorrido = new int[numerodeciudades];
		
		// Cread mediante un algoritmo voraz una solucion a priori no optima con la que 
		// comparar el resto de soluciones
		generarTour();
		
		// Inicia las feromonas a un determinado valor
		inicioFeromonasYvisibilidad();
	}

	/**
	 * Instancia hormigas nuevas siempre que no se haya llegado al limite.
	 */
	public void ejecutar() {
		
		for(int i=hormigasViaje;i<tamañoLWP&&hormigasEspera>0;i++){
			// Crea una hormiga nueva y la ejecuta, aumentando el contador de hormigas en ejecucion
			// y disminuyendo el de hormigas en espera
			Thread hilo = new Thread(new Hormiga(this,numerodeciudades,feromonas,TAUZERO,visibilidad));
			hilo.start();
			hormigasViaje++;
			hormigasEspera--;
			
		}
	}

	/**
	 * Metodo que se ejecuta al finalizar la aplicación.
	 */
	public void finalizar() {

		mostrarMejorRuta();
		
	}

	/**
	 * Recorre , elemento por elemento, un recorrido y devuelve la distancia
	 * 
	 * @param tour
	 *            El recorrido
	 * @return La distania en unidades de medida del recorrido
	 */
	private int calcularlongitudtour(int tour[]) {
		int length = 0;

		for (int i = 0; i < numerodeciudades; i++) {
			length += distancias[tour[i]][tour[i + 1]];
		}

		return length;
	}

	public int[] getMejorrecorrido() {
		
		return mejorrecorrido;
		
	}

	/**
	 * Actualiza el mejor recorrido obtenido hasta ahora si el recorrido por parametro
	 * tiene una longitud menor.
	 * @param recorrido
	 */
	public synchronized void setMejorrecorrido(int[] recorrido) {
		
		int longitudRecorrido=calcularlongitudtour(recorrido);
		
		if(longitudRecorrido<mejorlongitudderecorrido){
			this.mejorrecorrido =recorrido;
			mejorlongitudderecorrido=longitudRecorrido;
		}
		
	}
	private void setFeromonas(double[][] feromonas) {
		this.feromonas=feromonas;
	}
	
	
	
	/** Metodo que invocan las hormigas al acabar su ejecucion para comunicarselo al Optimizador
	 * @param recorrido
	 */
	public synchronized void finalizacionHormiga(int[] recorrido,double feromonas[][]){
		
		//Muestra la distancia del recorrido de la hormiga
		System.out.println("Ha vuelto una hormiga con un recorrido de "+calcularlongitudtour(recorrido));
		//Intenta actualizar el mejor recorrido con el que ha hecho la hormiga
		setMejorrecorrido(recorrido);
		//Actualiza las feromonas con las que ha puesto la hormiga
		setFeromonas(feromonas);
		//Actualiza las feromonas repecto del mejor recorrido
		for (int i = 0; i < numerodeciudades; i++) {
			feromonas[mejorrecorrido[i]][mejorrecorrido[i + 1]] = feromonas[mejorrecorrido[i + 1]][mejorrecorrido[i]] = (1 -  Constantes.GAMMA)
					* feromonas[mejorrecorrido[i]][mejorrecorrido[i + 1]]
					+  Constantes.GAMMA * ( Constantes.Q / mejorlongitudderecorrido);
		}
		// Se desapunta del contador de hormigas en ejecucion
		hormigasViaje--;
		// Si quedan hormigas por ejecutarse llama al metodo ejecutar() de Optimizador
		if(hormigasEspera>0)
			this.ejecutar();
		// Si ya no quedan hormigas por ejecutar y ya han vuelto todas, llama al metodo
		// finalizar() de Optimizador
		else if(hormigasEspera==0&&hormigasViaje==0)
			this.finalizar();
	}
	
	

	/**
	 * En este método se inicializan las feromonas al valor TAUZERO y la
	 * visibilidad se inicia al valor de las distancias elevado a un cierto
	 * valor BETA.
	 */
	private void inicioFeromonasYvisibilidad() {

		for (int i = 0; i < numerodeciudades; i++) {
			for (int j = 0; j < numerodeciudades; j++) {
				feromonas[i][j] = TAUZERO;
			}
		}

		for (int i = 0; i < numerodeciudades; i++) {
			for (int j = 0; j < numerodeciudades; j++) {
				visibilidad[i][j] = Math.pow(distancias[i][j], Constantes.BETA);
			}
		}
	}
	
	
	/** Genera mediante un algoritmo voraz una solucion primitiva.
	 * 
	 */
	private void generarTour() {
		int tourbasico[];
		boolean visitadas[]= new boolean[numerodeciudades];
		tourbasico = new int[numerodeciudades + 1];
		feromonas = new double[numerodeciudades][numerodeciudades];
		visibilidad = new double[numerodeciudades][numerodeciudades];

		tourbasico[0] = tourbasico[numerodeciudades] = 0;
		visitadas[0] = true;

		for (int i = 1; i < numerodeciudades; i++) {
			int mascercano = 0;

			for (int j = 0; j < numerodeciudades; j++) {
				if (!visitadas[j]
						&& (mascercano == 0 || distancias[tourbasico[i]][j] < distancias[i][mascercano])) {
					mascercano = j;
				}
			}

			tourbasico[i] = mascercano;
			visitadas[mascercano] = true;
		}

		mejorrecorrido = tourbasico;

		mejorlongitudderecorrido = calcularlongitudtour(tourbasico);

		TAUZERO = 1.0 / (numerodeciudades - mejorlongitudderecorrido);

		SalidaDeDatos output = new SalidaDeDatos();
		StringBuilder mensaje = new StringBuilder();

		mensaje.append(mejorlongitudderecorrido).append("#NN");

		output.mostrarPorPantalla(mensaje.toString());
	}
	
	/** Metodo que muestra el mejor recorrido
	 * 
	 */
	private void mostrarMejorRuta() {
		SalidaDeDatos output = new SalidaDeDatos();
		StringBuilder mensaje = new StringBuilder();

		mensaje.append(mejorlongitudderecorrido);

		output.mostrarPorPantalla(mensaje.toString(), "#defecto");

		// Limpia el buffer completo para crear un nuevo mensaje
		// reusando el objeto salida de datos.
		mensaje.delete(0, mensaje.length());

		for (int i = 0; i < numerodeciudades + 1; i++) {

			mensaje.append(mejorrecorrido[i]).append(" ");
		}
		output.mostrarPorPantalla(mensaje.toString(), "#defecto");
	}
}
