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

	// BETA aparece en la linea 133
	// GAMMA aparece en las lineas 197, 199, 269 y 270
	// qZERO aparece en las linea 238
	// Q aparece en la linea 201

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
	private int tourbasico[];
	
	// use power of 2
	public static final int numDeHormigas = 2048 * 20;
	private static final int tamañoLWP = Runtime.getRuntime().availableProcessors();

	private int hormigasViaje=0;
	private int hormigasEspera=numDeHormigas;
	
	
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
		generarTour();
		
		// La clase hormiga debe implementar el interfaz Runnable que obliga a la sobreescritura del método run
		//hormigas = new Runnable();
		
		
		inicioFeromonasYvisibilidad();
	}

	/**
	 * En este método se lanza el algoritmo cuando dispone de todos los datos
	 * preparados.
	 */
	public void ejecutar() {
		for(int i=hormigasViaje;i<tamañoLWP&&hormigasEspera>0;i++){
			Thread hilo = new Thread(new Hormiga(this,numerodeciudades,feromonas,TAUZERO,visibilidad));
			hilo.start();
			hormigasViaje++;
			hormigasEspera--;
		}
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

	public synchronized void setMejorrecorrido(int[] recorrido) {
		int longitudRecorrido=calcularlongitudtour(recorrido);
		if(longitudRecorrido<mejorlongitudderecorrido){
			this.mejorrecorrido =recorrido;
			mejorlongitudderecorrido=longitudRecorrido;
		}
		
	}
	
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
	
	public synchronized void finalizacionHormiga(int[] recorrido){
		setMejorrecorrido(recorrido);
		for (int i = 0; i < numerodeciudades; i++) {
			feromonas[mejorrecorrido[i]][mejorrecorrido[i + 1]] = feromonas[mejorrecorrido[i + 1]][mejorrecorrido[i]] = (1 -  Constantes.GAMMA)
					* feromonas[mejorrecorrido[i]][mejorrecorrido[i + 1]]
					+  Constantes.GAMMA * ( Constantes.Q / mejorlongitudderecorrido);
		}
		hormigasViaje--;
		if(hormigasEspera>0)
			this.ejecutar();
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
	
	private void generarTour() {
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
}
