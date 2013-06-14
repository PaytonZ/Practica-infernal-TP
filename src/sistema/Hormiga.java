package sistema;

import salidaDeDatos.SalidaDeDatos;

public class Hormiga implements Runnable{

	//Referencia al sistema
	private Optimizador sistema;
	
	//Array de recorrido de la hormiga
	private boolean visitadas[];
	//Numero de ciudades del problema
	private int numerodeciudades;
	//Array con las feromonas
	private double feromonas[][];
	//Array con las distancias entre ciudades
	private double visibilidad[][];
	//Variable de significado desconocido
	//Probablemente un cociente para la actualizacion de feromonas
	private double TAUZERO;
	
	
	/** Constructor que recibe los datos del problema por parametros
	 * @param sistema
	 * @param numerodeciudades
	 * @param feromonas
	 * @param TAUZERO
	 * @param visibilidad
	 */
	public Hormiga(Optimizador sistema, int numerodeciudades,double feromonas[][],double TAUZERO,double visibilidad[][]){
		visitadas = new boolean[numerodeciudades];
		this.numerodeciudades=numerodeciudades;
		this.sistema=sistema;
		this.feromonas=feromonas;
		this.TAUZERO=TAUZERO;
		this.visibilidad=visibilidad;
	}
	
	//Override del método run() del interfaz Runnable
	@Override
	public void run() {
		construirTour();
	}

	/**
	 * Parte Principal de la ejecucion. Ejecucion del algoritmo de calculo del
	 * ciclo hamiltoniano mediante feromonas de hormigas
	 */
	private void construirTour() {

		SalidaDeDatos output = new SalidaDeDatos();
		StringBuilder mensaje = new StringBuilder();
		
		//Tour de la hormiga
		int tourtemporal[] = new int[numerodeciudades + 1];
		//Toma en cuenta la distancia y la atraccion de las feromonas
		double pesos[] = new double[numerodeciudades];
		// Variables Cuyo significado se desconoce
		double sigmaWeights;
		double q, tempWeight, target;
		
		int ultima, siguiente;

		//Inicializa el array de visitadas a falso
		for (int i = 0; i < numerodeciudades; i++) {
			visitadas[i] = false;
		}
		
		//Pone la ciudad de partida como primera y ultima del recorrido 
		//y pone la ultima ciudad como visitada
		ultima = tourtemporal[0] = tourtemporal[numerodeciudades] =  Constantes.random
				.nextInt(numerodeciudades);
		visitadas[ultima] = true;

		//Algoritmo que selecciona las ciudades a visitar
		for (int i = 1; i < numerodeciudades; i++) {
			for (int j = 0; j < numerodeciudades; j++) {

				if (visitadas[j] == true) {
					pesos[j] = 0;
				} else {
					pesos[j] = feromonas[ultima][j] + visibilidad[ultima][j];
				}
			}
			q =  Constantes.random.nextDouble();
			siguiente = 0;

			if (q <=  Constantes.qZERO) {
				tempWeight = 0;

				for (int j = 0; j < numerodeciudades; j++) {
					if (pesos[j] > tempWeight) {
						tempWeight = pesos[j];
						siguiente = j;
					}
				}
			} else {
				sigmaWeights = 0;

				for (int j = 0; j < numerodeciudades; j++)
					sigmaWeights += pesos[j];

				target =  Constantes.random.nextDouble() * sigmaWeights;
				tempWeight = 0;

				for (int j = 0; j < numerodeciudades && tempWeight < target; j++) {
					tempWeight += pesos[j];

					if (tempWeight >= target) {
						siguiente = j;
					}
				}
			}

			if (visitadas[siguiente] == true) {
				mensaje.append(siguiente).append("#tabu");
				output.mostrarPorPantalla(mensaje.toString());
			}
			
			//Actualiza las feromonas
			feromonas[ultima][siguiente] = feromonas[siguiente][ultima] = (1 -  Constantes.GAMMA)
					* feromonas[ultima][siguiente] +  Constantes.GAMMA * TAUZERO;
			tourtemporal[i] = ultima = siguiente;
			visitadas[ultima] = true;
		}
		
		// Le comunica al sistema que ya ha terminado su ejecucion
		// y le pasa los resultados
		sistema.finalizacionHormiga(tourtemporal,feromonas);
	}
	

		

}