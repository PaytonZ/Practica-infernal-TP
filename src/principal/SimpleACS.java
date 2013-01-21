package principal;

import inicio.Inicio;

import java.util.Random;

public class SimpleACS {
	
	static final double BETA = 2, GAMMA = 0.1, qZERO = 0.9, Q = 1.0; // Variables de cuyo significado se desconoce.
	static final int M = 2, TMAX = 50000;
	static final Random random = new Random();

	int CITIES; // Cantidad de ciudades a visitar.
	double TAUZERO; // Variables cuyo significado se desconoce.
	int distances[][];
	double visibility[][];
	double pheromones[][];
	int bestTour[]; // Mejor ruta hasta el momento
	int bestLength = Integer.MAX_VALUE; // Longitud de la mejor ruta hasta el
										// momento
	boolean visitadas[]; // Lista de ciudades que la hormiga tiene que visitar
							// para

	// realizar un tour.

	public static void main(String args[]) {

		String ficheroaabrir = "eil51.tsp";
		SimpleACS main = new SimpleACS();

		main.iniciar(ficheroaabrir);
		main.ejecutar();
		main.finalizar();

	}

	public SimpleACS() {

	}
/**
 * En este método se realizan las siguientes funcionalidades de la aplicación
 * 1- Se abre el fichero y se genera , segun la entrada del mismo , una matriz para poder trabajar con ella.
 * 2- Se inician variables como el numero de ciudades , la mejor ruta , y las ciudades visitadas.
 * 3- Se genera un tour inicial basandose en la proximidad de las ciudades
 * 4- Se inician las feromonas y las visibilidad
 * 5- El metodo ha dejado todas las variables listas para poder proceder al metodo ejecuta
 * @param ficheroaabrir
 */
	public void iniciar(String ficheroaabrir) {

		Inicio in = new Inicio();
		this.distances = in.cargarFicheroyGenerarMatriz(ficheroaabrir);

		CITIES = distances.length;

		bestTour = new int[CITIES];
		visitadas = new boolean[CITIES];

		generarTour();
		inicioFeromonasYvisibilidad();

	}

	/**
	 * En esta funcion se inicializan las feromonas al valor TAUZERO y la
	 * visibilidad se inicia al valor de las distancias elevado a un cierto
	 * valor BETA
	 */
	public void inicioFeromonasYvisibilidad() {

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
 * Se genera una ruta inicial basandose en la proximidad de unas ciudades con otras ( ruta no óptima )
 */
	public void generarTour() {
		int NNTour[] = new int[CITIES + 1];
		pheromones = new double[CITIES][CITIES];
		visibility = new double[CITIES][CITIES];

		NNTour[0] = NNTour[CITIES] = 0;
		visitadas[0] = true;

		for (int i = 1; i < CITIES; i++) {
			int mascercano = 0;

			for (int j = 0; j < CITIES; j++)
				if (!visitadas[j]
						&& (mascercano == 0 || distances[NNTour[i]][j] < distances[i][mascercano]))
					mascercano = j;

			NNTour[i] = mascercano;
			visitadas[mascercano] = true;
		}

		bestTour = NNTour;

		bestLength = calcularlongitudtour(NNTour);

		TAUZERO = 1.0 / (CITIES - bestLength);

		System.out.println("NN = " + bestLength);
	}

	public void ejecutar() {

		for (int t = 0; t < TMAX; t++) {
			if (t % 100 == 0) {
				System.out.println("Iteration" + t);
			}

			for (int k = 0; k < M; k++) {
				construirNuevoTour();
			}

			for (int i = 0; i < CITIES; i++) {
				pheromones[bestTour[i]][bestTour[i + 1]] = pheromones[bestTour[i + 1]][bestTour[i]] = (1 - GAMMA)
						* pheromones[bestTour[i]][bestTour[i + 1]]
						+ GAMMA
						* (Q / bestLength);
			}
		}
	}

	public void finalizar() {
		System.out.println(bestLength);

		for (int i = 0; i < CITIES + 1; i++) {
			System.out.print(bestTour[i] + " ");
		}

	}

	public void construirNuevoTour() {

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
				System.out.println("TABU\n" + siguiente);
				System.exit(0);
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

	int calcularlongitudtour(int tour[]) {
		int length = 0;

		for (int i = 0; i < CITIES; i++) {
			length += distances[tour[i]][tour[i + 1]];
		}

		return length;
	}
}
