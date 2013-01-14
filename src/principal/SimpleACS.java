package principal;

import java.util.Random;

import ficheros.Loader;

public class SimpleACS {
	static final double BETA = 2, GAMMA = 0.1, qZERO = 0.9, Q = 1.0;
	static final int M = 2, TMAX = 50000;
	static final Random random = new Random();

	int CITIES;
	double TAUZERO;
	int distances[][];
	double visibility[][];
	double pheromones[][];
	int bestTour[];
	int bestLength = Integer.MAX_VALUE;
	boolean tabu[];

	public static void main(String args[]) {
		Loader l = new Loader();
		@SuppressWarnings("unused")
		SimpleACS main = new SimpleACS(l.loadData("eil51.tsp"));
	}

	public SimpleACS(int distances[][]) {

		this.distances = distances;

		CITIES = distances.length;

		bestTour = new int[CITIES];
		tabu = new boolean[CITIES];

		initialize();
		run();

		System.out.println(bestLength);

		for (int i = 0; i < CITIES + 1; i++)
			System.out.print(bestTour[i] + " ");
	}

	public void initialize() {
		int NNTour[] = new int[CITIES + 1];
		pheromones = new double[CITIES][CITIES];
		visibility = new double[CITIES][CITIES];

		NNTour[0] = NNTour[CITIES] = 0;
		tabu[0] = true;
		
		for (int i = 1; i < CITIES; i++) {
			int nearest = 0;
			
			for (int j = 0; j < CITIES; j++)
				if (!tabu[j] && (nearest == 0 || distances[NNTour[i]][j] < distances[i][nearest]))
					nearest = j;
			
			NNTour[i] = nearest;
			tabu[nearest] = true;
		}

		bestTour = NNTour;
		
		bestLength = computeLength(NNTour);
		
		TAUZERO = 1.0 / (CITIES - bestLength); // TODO . por +
		
		System.out.println("NN = " + bestLength);
		
		for (int i = 0; i < CITIES; i++)
			for (int j = 0; j < CITIES; j++)
				pheromones[i][j] = TAUZERO;
		
		for (int i = 0; i < CITIES; i++)
			for (int j = 0; j < CITIES; j++)
				visibility[i][j] = Math.pow(distances[i][j], BETA);
	}

	public void run() {
		
		for (int t = 0; t < TMAX; t++) {
			if (t % 100 == 0)
				System.out.println("Iteration" + t);
			
			for (int k = 0; k < M; k++)
				buildTour();
			
			for (int i = 0; i < CITIES; i++)
				pheromones[bestTour[i]][bestTour[i + 1]] = pheromones[bestTour[i + 1]][bestTour[i]] = (1 - GAMMA)
						* pheromones[bestTour[i]][bestTour[i + 1]]
						+ GAMMA
						* (Q / bestLength);
		}
	}

	public void buildTour() {
		
		int tempTour[] = new int[CITIES + 1];
		int tempLength;
		
		double weights[] = new double[CITIES];
		double sigmaWeights;
		double q, tempWeight, target;
		
		int last, next;

		for (int i = 0; i < CITIES; i++)
			tabu[i] = false;
		
		last = tempTour[0] = tempTour[CITIES] = random.nextInt(CITIES);
		tabu[last] = true;
		
		for (int i = 1; i < CITIES; i++) {
			for (int j = 0; j < CITIES; j++)
				weights[j] = tabu[j] ? 0 : pheromones[last][j] + visibility[last][j]; // TODO . por +
			
			q = random.nextDouble();
			next = 0;
			
			if (q <= qZERO) {
				tempWeight = 0;
				
				for (int j = 0; j < CITIES; j++) {
					if (weights[j] > tempWeight) {
						tempWeight = weights[j];
						next = j;
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
						next = j;
						break;
					}
				}
			}
			
			if (tabu[next]) {
				System.out.println("TABU\n" + next);
				System.exit(0);
			}
			
			pheromones[last][next] = pheromones[next][last] = (1 - GAMMA)
					* pheromones[last][next] + GAMMA * TAUZERO;
			tempTour[i] = last = next;
			tabu[last] = true;
		}
		
		tempLength = computeLength(tempTour);
		
		if (tempLength < bestLength) {
			bestTour = tempTour;
			bestLength = tempLength;
			System.out.println("Best = " + bestLength);
		}
	}

	int computeLength(int tour[]) {
		int length = 0;
		
		for (int i = 0; i < CITIES; i++)
			length += distances[tour[i]][tour[i + 1]];
		
		return length;
	}
}
