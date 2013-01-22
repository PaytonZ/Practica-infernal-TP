package inicio;

import java.util.StringTokenizer;

public class CrearMatrices {
	
	/**
	 * Gerera la matriz a partir del contenido del fichero.
	 * 
	 * @param contenidoFicheroCargado
	 * @return
	 */
	public int[][] generarMatriz(StringTokenizer contenidoFicheroCargado) {
		
		
		int numerodeciudades = obtenerNumeroDeCiudades(contenidoFicheroCargado);

		return construirMatrizDijkstra(contenidoFicheroCargado, numerodeciudades);
	}

	

	/*
	 * The greedy algorithm, where the solution is constructed from the set of
	 * sorted edges • The christofides algorithm with solutions constructed by
	 * an Eulerian cycle5, where nodes are not visited twice.
	 */
	public int[][] construirMatrizDijkstra(StringTokenizer contenidodelficherocontokens, int numerodeciudades) {
		
		String cadenaleida = "";
		String edgeWeightType = "UNKNOWN";
		int resultado[][] = {};

		// while (true) {

		cadenaleida = contenidodelficherocontokens.nextToken();

		if (cadenaleida.equals("EDGE WEIGHT TYPE")) {

			contenidodelficherocontokens.nextToken();
			edgeWeightType = contenidodelficherocontokens.nextToken();

			if (edgeWeightType.equals("EXPLICIT")) {

				resultado = construirMatrizExplicita(
						contenidodelficherocontokens, numerodeciudades);

			} else {

				resultado = construirMatrizDijkstraCalculoDistanciaEuclides(
						contenidodelficherocontokens, numerodeciudades);
			}

		} else if (cadenaleida.equals("EDGE WEIGHT TYPE:")) {

			edgeWeightType = contenidodelficherocontokens.nextToken();

			if (edgeWeightType.equals("EXPLICIT")) {

				resultado = construirMatrizExplicita(
						contenidodelficherocontokens, numerodeciudades);

			} else {

				resultado = construirMatrizDijkstraCalculoDistanciaEuclides(
						contenidodelficherocontokens, numerodeciudades);
			}
		}

		return resultado;
		// }
	}

	/**
	 * Método que mira las posiciones de las ciudades y crea una matriz con las
	 * distancias entre ellas.
	 * 
	 * @param coordenadas Cordenadas de las ciudades.
	 * @param numerodeciudades El numero de ciudades que hay.
	 * @return Devuelve una matriz con las distancias que hay entre las ciudades.
	 */
	public int[][] calculoDistanciaPorEuclides(double[][] coordenadas, int numerodeciudades) {

		final int indiceprimeracoordenada = 0;
		final int indicesegundacoordenada = 1;

		int matrizDistancias[][] = new int[numerodeciudades][numerodeciudades];
		int dist = 0;

		for (int j = 0; j < coordenadas.length; j++) {
			for (int i = j; i < coordenadas.length; i++) {
				dist = (int) Math
						.floor(.5 + Math.sqrt(Math.pow(coordenadas[i][indiceprimeracoordenada]
										- coordenadas[j][indiceprimeracoordenada], 2.0)
								+ Math.pow(coordenadas[i][indicesegundacoordenada]
												- coordenadas[j][indicesegundacoordenada], 2.0)));

				matrizDistancias[i][j] = dist;
				matrizDistancias[j][i] = dist;
			}
		}

		return matrizDistancias;
	}

	private int[][] construirMatrizDijkstraCalculoDistanciaEuclides(StringTokenizer strTok, int numerodeciudades) {

		double coordenadas[][] = new double[numerodeciudades][2];
		final int indiceprimeracoordenada = 0;
		final int indicesegundacoordenada = 1;

		int contador = 0;

		String cadenaleida = "";

		while (!cadenaleida.equals("EOF")) {

			cadenaleida = strTok.nextToken();

			if (cadenaleida.equals("NODE COORD SECTION")) {

				cadenaleida = strTok.nextToken();

				while (!cadenaleida.equals("EOF")) {

					coordenadas[contador][indiceprimeracoordenada] = Double
							.parseDouble(strTok.nextToken());
					coordenadas[contador][indicesegundacoordenada] = Double
							.parseDouble(strTok.nextToken());

					contador++;

					cadenaleida = strTok.nextToken();
				}
			}
		}

		int matrizAuxiliar[][] = new int[numerodeciudades][numerodeciudades];

		matrizAuxiliar = calculoDistanciaPorEuclides(coordenadas,
				numerodeciudades);

		return matrizAuxiliar;
	}
	
	/**
	 * Este método genera una matriz explicita.
	 * 
	 * @param strTok
	 * @param numerodeciudades
	 * @return La matriz construida
	 */
	private int[][] construirMatrizExplicita(StringTokenizer strTok, int numerodeciudades) {

		int matrizAuxiliar[][] = new int[numerodeciudades][numerodeciudades];

		int countI = 0;
		int countJ = 0;

		String cadenaleida = "";

		while (!cadenaleida.equals("EOF")) {

			cadenaleida = strTok.nextToken();

			if (cadenaleida.equals("EDGE WEIGHT SECTION")) {

				boolean finfichero = false;

				while (!finfichero) {

					cadenaleida = strTok.nextToken();

					if (cadenaleida.equals("EOF")) {

						finfichero = true;
					}
					if (cadenaleida.equals("0")) {

						matrizAuxiliar[countI][countJ] = Integer.parseInt(cadenaleida);
						matrizAuxiliar[countJ][countI] = Integer.parseInt(cadenaleida);

						countI++;
						countJ = 0;
					} else {

						matrizAuxiliar[countI][countJ] = Integer.parseInt(cadenaleida);
						matrizAuxiliar[countJ][countI] = Integer.parseInt(cadenaleida);

						countJ++;
					}
				}
			}
		}

		return matrizAuxiliar;
	}

	/**
	 * Método que busca el numero de ciudades en el string y lo devuelve
	 * 
	 * @param strTok Un StringTokenizer con el contenido del fichero
	 * @return El numero de ciudades en forma de entero
	 */
	private int obtenerNumeroDeCiudades(StringTokenizer strTok) {

		String tempStr = "";
		boolean encontradoNumeroCiudades = false;

		while (encontradoNumeroCiudades == false) {
			tempStr = strTok.nextToken();

			if (tempStr.equals("DIMENSION")) {
				strTok.nextToken();
				tempStr = strTok.nextToken();
				encontradoNumeroCiudades = true;
			}

			else if (tempStr.equals("DIMENSION:")) {
				tempStr = strTok.nextToken();
				encontradoNumeroCiudades = true;
			}
		}
		
		return Integer.parseInt(tempStr);
	}
}
