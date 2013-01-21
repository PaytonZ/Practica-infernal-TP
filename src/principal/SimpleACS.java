package principal;

import inicio.Inicio;

import java.util.Random;



public class SimpleACS {
	static final double BETA = 2, GAMMA = 0.1, qZERO = 0.9, Q = 1.0; // Variables cuyo significado se desconoce. Aparecen en la linea
	//
	static final int M = 2, TMAX = 50000;
	static final Random random = new Random();

	int CITIES; // Cantidad de ciudades a visitar.
	double TAUZERO;
	int distances[][];
	double visibility[][];
	double pheromones[][];
	int bestTour[]; // Mejor ruta hasta el momento
	int bestLength = Integer.MAX_VALUE; // Longitud de la mejor ruta hasta el
										// momento
	boolean visitadas[]; // Lista de ciudades que la hormiga tiene que visitar para
					// realizar un tour.

	public static void main(String args[]) {
		
		String ficheroaabrir = "eil51.tsp";
		SimpleACS main = new SimpleACS(ficheroaabrir);

		main.iniciar();
		main.ejecutar();
		main.finalizar();

	}

	public SimpleACS(String nombredelfichero) {

		Inicio i= new Inicio();
		this.distances = i.cargarFichero(nombredelfichero);

		CITIES = distances.length;

		bestTour = new int[CITIES];
		visitadas = new boolean[CITIES];

	}
	
	public void generarTour()
	{
		int NNTour[] = new int[CITIES + 1];
		pheromones = new double[CITIES][CITIES];
		visibility = new double[CITIES][CITIES];

		NNTour[0] = NNTour[CITIES] = 0;
		visitadas[0] = true;

		for (int i = 1; i < CITIES; i++) {
			int mascercano = 0;

			for (int j = 0; j < CITIES; j++)
				if (!visitadas[j] && (mascercano == 0 || distances[NNTour[i]][j] < distances[i][mascercano]))
					mascercano = j;

			NNTour[i] = mascercano;
			visitadas[mascercano] = true;
		}

		bestTour = NNTour;

		bestLength = calcularlongitudtour(NNTour);

		TAUZERO = 1.0 / (CITIES - bestLength); // TODO . por +

		System.out.println("NN = " + bestLength);
	}

	public void iniciar() {
		
		generarTour();

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

	public void ejecutar() {

		for (int t = 0; t < TMAX; t++) {
			if (t % 100 == 0) {
				System.out.println("Iteration" + t);
			}

			for (int k = 0; k < M; k++) {
				buildTour();
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

	public void buildTour() {

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

				// weights[j] = tabu[j] ? 0 : pheromones[last][j]+
				// visibility[last][j]; // TODO . por +
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

				for (int j = 0; j < CITIES; j++) {
					tempWeight += weights[j];

					if (tempWeight >= target) {
						siguiente = j;
						break;
					}
				}
			}

			if (visitadas[siguiente]==true) {
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
