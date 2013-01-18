package matematicas.matrices;

import java.util.StringTokenizer;

import matematicas.euclides.EuclidesDistancia;

public class MatrizDijkstra {

	/*
	 * The greedy algorithm, where the solution is constructed from the set of
	 * sorted edges • The christofides algorithm with solutions constructed by
	 * an Eulerian cycle5, where nodes are not visited twice.
	 */
	public int[][] construirMatrizDijkstra(
			StringTokenizer contenidodelficherocontokens, int numerodeciudades) {
		String cadenaleida = "";
		String edgeWeightType = "UNKNOWN";
		int resultado[][] = {};

		// while (true) {

		cadenaleida = contenidodelficherocontokens.nextToken();

		if (cadenaleida.equals("EDGE WEIGHT TYPE")) {

			contenidodelficherocontokens.nextToken();
			edgeWeightType = contenidodelficherocontokens.nextToken();

			if (edgeWeightType.equals("EXPLICIT")) {

				resultado = buildDistMatrixEXPLICIT(
						contenidodelficherocontokens, numerodeciudades);

			} else {

				resultado = construirMatrizDijkstraCalculoDistanciaEuclides(
						contenidodelficherocontokens, numerodeciudades);
			}

		} else if (cadenaleida.equals("EDGE WEIGHT TYPE:")) {

			edgeWeightType = contenidodelficherocontokens.nextToken();

			if (edgeWeightType.equals("EXPLICIT")) {

				resultado = buildDistMatrixEXPLICIT(
						contenidodelficherocontokens, numerodeciudades);

			} else {

				resultado = construirMatrizDijkstraCalculoDistanciaEuclides(
						contenidodelficherocontokens, numerodeciudades);
			}
		}

		return resultado;
		// }
	}

	private int[][] construirMatrizDijkstraCalculoDistanciaEuclides(
			StringTokenizer strTok, int numerodeciudades) {

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

		matrizAuxiliar = EuclidesDistancia.calculoDistanciaPorEuclides(
				coordenadas, numerodeciudades);

		return matrizAuxiliar;
	}

	private int[][] buildDistMatrixEXPLICIT(StringTokenizer strTok,
			int numerodeciudades) {

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

						matrizAuxiliar[countI][countJ] = Integer
								.parseInt(cadenaleida);
						matrizAuxiliar[countJ][countI] = Integer
								.parseInt(cadenaleida);

						countI++;
						countJ = 0;
					} else {

						matrizAuxiliar[countI][countJ] = Integer
								.parseInt(cadenaleida);
						matrizAuxiliar[countJ][countI] = Integer
								.parseInt(cadenaleida);

						countJ++;
					}
				}
			}
		}

		return matrizAuxiliar;
	}
}
