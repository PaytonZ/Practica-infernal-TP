package principal;

import inicio.Inicio;

import java.util.Random;
import java.util.StringTokenizer;

import salidaDeDatos.SalidaDeDatos;

public class SimpleACS {
	
	// Variables de cuyo significado se desconoce.
	private static final double BETA = 2, GAMMA = 0.1, qZERO = 0.9, Q = 1.0;
	private static final int M = 2, TMAX = 50000;
	private static final Random random = new Random();
	
	// Cantidad de ciudades a visitar.
	private int CITIES;
	
	// Variables cuyo significado se desconoce.
	private double TAUZERO;
	private int distances[][];
	private double visibility[][];
	private double pheromones[][];
	
	// Mejor ruta hasta el momento
	private int bestTour[];
	
	// Longitud de la mejor ruta hasta el momento
	private int bestLength = Integer.MAX_VALUE;
	
	// Lista de ciudades que la hormiga tiene que visitar para realizar un tour.
	private boolean visitadas[];
	
	/**
	 * <p>En este método se realizan las siguientes funcionalidades de la aplicación.</p>
	 * <p>1- Se abre el fichero y se genera , segun la entrada del mismo , una matriz para poder trabajar con ella.</p>
	 * <p>2- Se inician variables como el numero de ciudades , la mejor ruta , y las ciudades visitadas.</p>
	 * <p>3- Se genera un tour inicial basandose en la proximidad de las ciudades.</p>
	 * <p>4- Se inician las feromonas y las visibilidad.</p>
	 * <p>5- El metodo ha dejado todas las variables listas para poder proceder al metodo ejecuta.</p>
	 * 
	 * @param ficheroaabrir Ruta del fichero que se va a leer
	 */
	public void iniciar(String ficheroaabrir) {

		Inicio inicio = new Inicio();
		
		StringTokenizer contenidofechero = inicio.cargarFichero(ficheroaabrir);
		
		// Se genera una matriz con 
		distances = inicio.generarMatriz(contenidofechero); 
		
		// Obtención del número de ciudades a partir del tamaño de la matriz
		CITIES = distances.length;
		
		// Se contruyen las matrices necesarias a partir del número de ciudades
		bestTour = new int[CITIES];
		visitadas = new boolean[CITIES];

		generarTour();
		inicioFeromonasYvisibilidad();
	}

	public void ejecutar() {
		
		construirNuevoTour();
	}
	
	/**
	 * Método que se ejecuta cuando finaliza todo el proceso del sistema y se va a mostrar la salida de datos.
	 */
	public void finalizar() {
		
		System.out.println(bestLength);

		for (int i = 0; i < CITIES + 1; i++) {
			System.out.print(bestTour[i] + " ");
		}
	}

	/**
	 * En este método se inicializan las feromonas al valor TAUZERO y la
	 * visibilidad se inicia al valor de las distancias elevado a un cierto
	 * valor BETA.
	 */
	private void inicioFeromonasYvisibilidad() {

		for (int i = 0; i < CITIES; i++) {
			for (int j = 0; j < CITIES; j++) {
				pheromones[i][j] = TAUZERO;
			}
		}

		for (int i = 0; i < CITIES; i++) {
			for (int j = 0; j < CITIES; j++) {
				visibility[i][j] = Math.pow(distances[i][j], BETA);
			}
		}
	}
	
	/**
	 * Se genera una ruta inicial basandose en la proximidad de unas ciudades con otras ( ruta no óptima ).
	 */
	public void generarTour() {
		
		int NNTour[] = new int[CITIES + 1];
		pheromones = new double[CITIES][CITIES];
		visibility = new double[CITIES][CITIES];

		NNTour[0] = NNTour[CITIES] = 0;
		visitadas[0] = true;

		for (int i = 1; i < CITIES; i++) {
			int mascercano = 0;

			for (int j = 0; j < CITIES; j++) {
				if (!visitadas[j] && (mascercano == 0 || distances[NNTour[i]][j] < distances[i][mascercano])) 
				{
					mascercano = j;
				}
			}

			NNTour[i] = mascercano;
			visitadas[mascercano] = true;
		}

		bestTour = NNTour;

		bestLength = calcularlongitudtour(NNTour);

		TAUZERO = 1.0 / (CITIES - bestLength);
		
		System.out.println("NN = " + bestLength);
	}
	
	private void construirNuevoTour() {
		
		SalidaDeDatos out= new SalidaDeDatos();
		String mensaje;
		
		for (int t = 0; t < TMAX; t++) {
			if (t % 100 == 0) {
				mensaje=String.valueOf(t)+ "#iteration";
				out.mostrarPorPantalla(mensaje);
			}

			for (int k = 0; k < M; k++) {
				construirTour();
			}

			for (int i = 0; i < CITIES; i++) {
				pheromones[bestTour[i]][bestTour[i + 1]] = pheromones[bestTour[i + 1]][bestTour[i]] = (1 - GAMMA)
						* pheromones[bestTour[i]][bestTour[i + 1]]
						+ GAMMA * (Q / bestLength);
			}
		}
	}

	private void construirTour() {
		
		SalidaDeDatos out= new SalidaDeDatos();
		String mensaje;

		int tempTour[] = new int[CITIES + 1];
		int tempLength;

		double weights[] = new double[CITIES];
		double sigmaWeights;
		double q, tempWeight, target;

		int ultima, siguiente;

		for (int i = 0; i < CITIES; i++) {
			visitadas[i] = false;
		}

		ultima = tempTour[0] = tempTour[CITIES] = random.nextInt(CITIES);
		visitadas[ultima] = true;

		for (int i = 1; i < CITIES; i++) {
			for (int j = 0; j < CITIES; j++) {
				
				if (visitadas[j] == true) {
					weights[j] = 0;
				} else {
					weights[j] = pheromones[ultima][j] + visibility[ultima][j];
				}
			}
			q = random.nextDouble();
			siguiente = 0;

			if (q <= qZERO) {
				tempWeight = 0;

				for (int j = 0; j < CITIES; j++) {
					if (weights[j] > tempWeight) {
						tempWeight = weights[j];
						siguiente = j;
					}
				}
			} else {
				sigmaWeights = 0;

				for (int j = 0; j < CITIES; j++)
					sigmaWeights += weights[j];

				target = random.nextDouble() * sigmaWeights;
				tempWeight = 0;

				for (int j = 0; j < CITIES && tempWeight < target; j++) {
					tempWeight += weights[j];

				if (tempWeight >= target) {
						siguiente = j;
						//break;
					}
				}
			}

			if (visitadas[siguiente] == true) {
				mensaje= String.valueOf(siguiente) + "#tabu";
				out.mostrarPorPantalla(mensaje);
				//System.exit(0);
			}

			pheromones[ultima][siguiente] = pheromones[siguiente][ultima] = (1 - GAMMA)
					* pheromones[ultima][siguiente] + GAMMA * TAUZERO;
			tempTour[i] = ultima = siguiente;
			visitadas[ultima] = true;
		}

		tempLength = calcularlongitudtour(tempTour);

		if (tempLength < bestLength) {
			bestTour = tempTour;
			bestLength = tempLength;
			System.out.println("Best = " + bestLength);
		}
	}

	private int calcularlongitudtour(int tour[]) {
		int length = 0;

		for (int i = 0; i < CITIES; i++) {
			length += distances[tour[i]][tour[i + 1]];
		}

		return length;
	}

	/**
	 * Método principal para iniciar la ejecución del sistema.
	 * 
	 * @param args No se utilizan
	 */
	public static void main(String args[]) {

		String ficheroaabrir = "eil51.tsp";
		SimpleACS main = new SimpleACS();

		main.iniciar(ficheroaabrir);
		main.ejecutar();
		main.finalizar();
	}
}
