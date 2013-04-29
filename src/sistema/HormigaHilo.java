package sistema;

import salidaDeDatos.SalidaDeDatos;

public class HormigaHilo implements Runnable{

	private int numerodeciudades;

	// TODO falta comentar estas variables
	private int distancias[][];
	private double visibilidad[][];
	private double feromonas[][];
	private int mejorrecorrido[];
	private int mejorlongitudderecorrido = Integer.MAX_VALUE;
	private boolean visitadas[];
	
	private double TAUZERO=1;
	
	public void run(){
	
		run(TAUZERO);
	
	}
	public void run(double nuevo_TAUZERO) {
        
		TAUZERO=nuevo_TAUZERO;
		construirNuevoTour();
		
    }
	
			private void construirNuevoTour() {
				SalidaDeDatos out = new SalidaDeDatos();
				StringBuilder mensaje = new StringBuilder();

			/*	for (int t = 0; t < Constantes.TMAX; t++) {
					if (t % 100 == 0) {
						mensaje.delete(0, mensaje.length());

						mensaje.append(t).append("#iteration");
						out.mostrarPorPantalla(mensaje.toString());
				*/	

					for (int k = 0; k <  Constantes.M; k++) {
						construirTour();
					}

					for (int i = 0; i < numerodeciudades; i++) {
						feromonas[mejorrecorrido[i]][mejorrecorrido[i + 1]] = feromonas[mejorrecorrido[i + 1]][mejorrecorrido[i]] = (1 -  Constantes.GAMMA)
								* feromonas[mejorrecorrido[i]][mejorrecorrido[i + 1]]
								+  Constantes.GAMMA * ( Constantes.Q / mejorlongitudderecorrido);
			
			
				}
		
			}
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

				longituddeltourtemporal = calcularlongitudtour(tourtemporal);

				if (longituddeltourtemporal < mejorlongitudderecorrido) {

					mensaje.delete(0, mensaje.length());

					mejorrecorrido = tourtemporal;
					mejorlongitudderecorrido = longituddeltourtemporal;

					mensaje.append(mejorlongitudderecorrido).append("#Best");

					output.mostrarPorPantalla(mensaje.toString());
				}
			}
			private int calcularlongitudtour(int tour[]) {
				int length = 0;

				for (int i = 0; i < numerodeciudades; i++) {
					length += distancias[tour[i]][tour[i + 1]];
				}

				return length;
			}

}

