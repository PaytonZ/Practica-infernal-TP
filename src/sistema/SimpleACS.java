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
 * @author Juan Carlos Marco Gonzalez
 * @author Daniel Serrano Torres
 */
public class SimpleACS {

	// Variables de cuyo significado se desconoce.

	// BETA aparece en la linea 133
	// GAMMA aparece en las lineas 197, 199, 269 y 270
	// qZERO aparece en las linea 238
	// Q aparece en la linea 201

	private static final double BETA = 2, GAMMA = 0.1, qZERO = 0.9, Q = 1.0;

	// M aparece en las lineas 181 y 189
	// TMAX aparece en la linea 182
	private static final int M = 2, TMAX = 50000;

	// random aparece en las lineas 226, 238 y 256
	// TAUZERO aparece en las lineas 133,174, 275
	private static final Random random = new Random();
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

	// Lista de ciudades que la hormiga tiene que visitar para realizar un tour.
	private boolean visitadas[];

	// Se usa para indicar de forma rapida la distancia del ciclo
	private int tourbasico[];

	/**
	 * <p>
	 * En este método se realizan las siguientes funcionalidades de la
	 * aplicación.
	 * </p>
	 * <p>
	 * 1- Se abre el fichero y se genera , segun la entrada del mismo , una
	 * matriz para poder trabajar con ella.
	 * </p>
	 * <p>
	 * 2- Se inician variables como el numero de ciudades , la mejor ruta , y
	 * las ciudades visitadas.
	 * </p>
	 * <p>
	 * 3- Se genera un tour inicial basandose en la proximidad de las ciudades.
	 * </p>
	 * <p>
	 * 4- Se inician las feromonas y las visibilidad.
	 * </p>
	 * <p>
	 * 5- El metodo ha dejado todas las variables listas para poder proceder al
	 * metodo ejecuta.
	 * </p>
	 * 
	 * @param ficheroaabrir
	 *            Ruta del fichero que se va a leer
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
		visitadas = new boolean[numerodeciudades];

		generarTour();
		inicioFeromonasYvisibilidad();
	}

	/**
	 * En este método se lanza el algoritmo cuando dispone de todos los datos
	 * preparados.
	 */
	public void ejecutar() {

		construirNuevoTour();
	}

	/**
	 * Método que se ejecuta cuando finaliza todo el proceso del sistema y se va
	 * a mostrar la salida de datos. Realiza lo siguiente
	 * <p>
	 * 1- Muestra en unidades de medida la longitud de la ruta mejor obtenida
	 * por el algoritmo
	 * </p>
	 * <p>
	 * 2- Muestra el recorrido completo , indicando , por identificador de
	 * ciudad , el mismo.
	 * </p>
	 */
	public void finalizar() {

		mostrarMejorRuta();
	}

	/**
	 * Clase que muestra por pantalla los resultados obtenidos del algoritmo
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
				visibilidad[i][j] = Math.pow(distancias[i][j], BETA);
			}
		}
	}

	/**
	 * Se genera una ruta inicial basandose en la proximidad de unas ciudades
	 * con otras ( ruta no óptima ).
	 */
	private void generarTour() {

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

	private void construirNuevoTour() {

		SalidaDeDatos out = new SalidaDeDatos();
		StringBuilder mensaje = new StringBuilder();

		for (int t = 0; t < TMAX; t++) {
			if (t % 100 == 0) {
				mensaje.delete(0, mensaje.length());

				mensaje.append(t).append("#iteration");
				out.mostrarPorPantalla(mensaje.toString());
			}

			for (int k = 0; k < M; k++) {
				construirTour();
			}

			for (int i = 0; i < numerodeciudades; i++) {
				feromonas[mejorrecorrido[i]][mejorrecorrido[i + 1]] = feromonas[mejorrecorrido[i + 1]][mejorrecorrido[i]] = (1 - GAMMA)
						* feromonas[mejorrecorrido[i]][mejorrecorrido[i + 1]]
						+ GAMMA * (Q / mejorlongitudderecorrido);
			}
		}
	}

	/**
	 * Parte Principal de la ejecucion. Ejecucion del algoritmo de calculo del
	 * ciclo hamiltoniano mediante feromonas de hormigas
	 */
	private void construirTour() {

		SalidaDeDatos output = new SalidaDeDatos();
		StringBuilder mensaje = new StringBuilder();

		int tempTour[] = new int[numerodeciudades + 1];
		int tempLength;

		double weights[] = new double[numerodeciudades];
		double sigmaWeights;
		double q, tempWeight, target;

		int ultima, siguiente;

		for (int i = 0; i < numerodeciudades; i++) {
			visitadas[i] = false;
		}

		ultima = tempTour[0] = tempTour[numerodeciudades] = random
				.nextInt(numerodeciudades);
		visitadas[ultima] = true;

		for (int i = 1; i < numerodeciudades; i++) {
			for (int j = 0; j < numerodeciudades; j++) {

				if (visitadas[j] == true) {
					weights[j] = 0;
				} else {
					weights[j] = feromonas[ultima][j] + visibilidad[ultima][j];
				}
			}
			q = random.nextDouble();
			siguiente = 0;

			if (q <= qZERO) {
				tempWeight = 0;

				for (int j = 0; j < numerodeciudades; j++) {
					if (weights[j] > tempWeight) {
						tempWeight = weights[j];
						siguiente = j;
					}
				}
			} else {
				sigmaWeights = 0;

				for (int j = 0; j < numerodeciudades; j++)
					sigmaWeights += weights[j];

				target = random.nextDouble() * sigmaWeights;
				tempWeight = 0;

				for (int j = 0; j < numerodeciudades && tempWeight < target; j++) {
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

			feromonas[ultima][siguiente] = feromonas[siguiente][ultima] = (1 - GAMMA)
					* feromonas[ultima][siguiente] + GAMMA * TAUZERO;
			tempTour[i] = ultima = siguiente;
			visitadas[ultima] = true;
		}

		tempLength = calcularlongitudtour(tempTour);

		if (tempLength < mejorlongitudderecorrido) {

			mensaje.delete(0, mensaje.length());

			mejorrecorrido = tempTour;
			mejorlongitudderecorrido = tempLength;

			mensaje.append(mejorlongitudderecorrido).append("#Best");

			output.mostrarPorPantalla(mensaje.toString());
		}
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
}
