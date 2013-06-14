package sistema;

import salidaDeDatos.SalidaDeDatos;

public class Hormiga implements Runnable{

	private Optimizador sistema;
	private boolean visitadas[];
	private int numerodeciudades;
	private double feromonas[][];
	private double visibilidad[][];
	private double TAUZERO;
	
	public Hormiga(Optimizador sistema, int numerodeciudades,double feromonas[][],double TAUZERO,double visibilidad[][]){
		visitadas = new boolean[numerodeciudades];
		this.numerodeciudades=numerodeciudades;
		this.sistema=sistema;
		this.feromonas=feromonas;
		this.TAUZERO=TAUZERO;
		this.visibilidad=visibilidad;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		construirTour();
	}

	



	/**
	 * Parte Principal de la ejecucion. Ejecucion del algoritmo de calculo del
	 * ciclo hamiltoniano mediante feromonas de hormigas
	 */
	private void construirTour() {

		SalidaDeDatos output = new SalidaDeDatos();
		StringBuilder mensaje = new StringBuilder();

		int tourtemporal[] = new int[numerodeciudades + 1];
		int longituddeltourtemporal;

		// Variables Cuyo significado se desconoce
		double weights[] = new double[numerodeciudades];
		double sigmaWeights;
		double q, tempWeight, target;

		int ultima, siguiente;

		for (int i = 0; i < numerodeciudades; i++) {
			visitadas[i] = false;
		}

		ultima = tourtemporal[0] = tourtemporal[numerodeciudades] =  Constantes.random
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
			q =  Constantes.random.nextDouble();
			siguiente = 0;

			if (q <=  Constantes.qZERO) {
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

				target =  Constantes.random.nextDouble() * sigmaWeights;
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

			feromonas[ultima][siguiente] = feromonas[siguiente][ultima] = (1 -  Constantes.GAMMA)
					* feromonas[ultima][siguiente] +  Constantes.GAMMA * TAUZERO;
			tourtemporal[i] = ultima = siguiente;
			visitadas[ultima] = true;
		}
/*
		longituddeltourtemporal = calcularlongitudtour(tourtemporal);

		if (longituddeltourtemporal < mejorlongitudderecorrido) {

			mensaje.delete(0, mensaje.length());

			mejorrecorrido = tourtemporal;
			mejorlongitudderecorrido = longituddeltourtemporal;

			mensaje.append(mejorlongitudderecorrido).append("#Best");

			output.mostrarPorPantalla(mensaje.toString());
		}*/
		
		
		sistema.finalizacionHormiga(tourtemporal);
	}
	
	// TODO falta comentar estas variables
	
	
	/*
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
	
			*/
		

}