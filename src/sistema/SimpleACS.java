package sistema;

import inicio.CargarFichero;
import inicio.CrearMatrices;

import java.util.Random;
import java.util.StringTokenizer;

import salidaDeDatos.SalidaDeDatos;

/**
 * Clase que resuleve el problema del viajante mediante feromonas de hormigas
 * 
 * @author Juan Luis Perez Valbuena
 * @author Alvaro Quesada Pimentel
 * @author Juan Carlos Marco
 * @author Daniel Serrano Torres
 */
public class SimpleACS {
	
	// Variables de cuyo significado se desconoce.
	
		//BETA aparece en la linea 133
		//GAMMA aparece en las lineas 197, 199, 269 y 270
		//qZERO aparece en las linea 238
		//Q aparece en la linea 201
	
	private static final double BETA = 2, GAMMA = 0.1, qZERO = 0.9,Q  = 1.0;
	
		//M aparece en las lineas  181 y 189
		//TMAX aparece en la linea 182
	private static final int  M = 2, TMAX = 50000;
	
		//random aparece en las lineas 226, 238 y 256
		//TAUZERO aparece en las lineas 133,174, 275
	private static final Random random = new Random();
	private double TAUZERO;
	
	// Cantidad de ciudades a visitar.
	private int CITIES;
	
	
	//TODO falta comentar estas variables
	private int distances[][]; 
	private double visibility[][];
	private double pheromones[][];
	
	// Mejor ruta hasta el momento
	private int bestTour[];
	
	// Longitud de la mejor ruta hasta el momento
	private int bestLength = Integer.MAX_VALUE;
	
	// Lista de ciudades que la hormiga tiene que visitar para realizar un tour.
	private boolean visitadas[];
	
	private int NNTour[];
	
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

		CargarFichero cargarfichero = new CargarFichero();
		CrearMatrices creadormatrices = new CrearMatrices();
		
		StringTokenizer contenidofechero = cargarfichero.cargarFichero(ficheroaabrir);
		
		// Se genera una matriz con la clase creadormatrices que decidira 
		// dependiendo de la entrada la matriz a crear adecuada
		distances = creadormatrices.generarMatriz(contenidofechero); 
		
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
	 * Realiza lo siguiente
	 * 1- Muestra en unidades de medida la longitud de la ruta mejor obtenida por el algoritmo
	 * 2- Muestra el ciclo completo, indicando las ciudades a vistadas //TODO mejorar
	 */
	public void finalizar() {
		
		mostrarMejorRuta();
	}

	private void mostrarMejorRuta() 
	{
		SalidaDeDatos output= new SalidaDeDatos();
		StringBuilder mensaje= new StringBuilder();
		
		mensaje.append(bestLength);
		
		output.mostrarPorPantalla(mensaje.toString(),"#defecto");

		// Limpia el buffer completo para crear un nuevo mensaje
		// reusando el objeto salida de datos.
		mensaje.delete(0, mensaje.length());
		
		for (int i = 0; i < CITIES + 1; i++) {
			
			mensaje.append(bestTour[i]).append(" ");
		}
		output.mostrarPorPantalla(mensaje.toString(),"#defecto");
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
	private void generarTour() {
		
		NNTour = new int[CITIES + 1];
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
		
		SalidaDeDatos output= new SalidaDeDatos();
		StringBuilder mensaje= new StringBuilder();
		
		mensaje.append(bestLength).append("#NN");
		
		output.mostrarPorPantalla(mensaje.toString());
	}
	
	private void construirNuevoTour() {
		
		SalidaDeDatos out= new SalidaDeDatos();
		StringBuilder mensaje  = new StringBuilder();
		
		for (int t = 0; t < TMAX; t++) {
			if (t % 100 == 0) {
				mensaje.delete(0, mensaje.length());
				
				mensaje.append(t).append("#iteration");
				out.mostrarPorPantalla(mensaje.toString());
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

		SalidaDeDatos output= new SalidaDeDatos();
		StringBuilder mensaje = new StringBuilder();

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
					}
				}
			}
			
			if (visitadas[siguiente] == true) {
				mensaje.append(siguiente).append("#tabu");
				output.mostrarPorPantalla(mensaje.toString());
			}
			
			pheromones[ultima][siguiente] = pheromones[siguiente][ultima] = (1 - GAMMA)
					* pheromones[ultima][siguiente] + GAMMA * TAUZERO;
			tempTour[i] = ultima = siguiente;
			visitadas[ultima] = true;
		}

		tempLength = calcularlongitudtour(tempTour);

		if (tempLength < bestLength) {
			
			mensaje.delete(0, mensaje.length());
			
			bestTour = tempTour;
			bestLength = tempLength;
			
			mensaje.append(bestLength).append("#Best");
			
			output.mostrarPorPantalla(mensaje.toString());
		}
	}

	private int calcularlongitudtour(int tour[]) {
		int length = 0;

		for (int i = 0; i < CITIES; i++) {
			length += distances[tour[i]][tour[i + 1]];
		}

		return length;
	}
}
