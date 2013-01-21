package inicio;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class Inicio {

	int numerodeciudades = 0; // Numero de ciudades que hay.
	String workStr = ""; // String con el contenido del fichero.

	/**
	 * Carga la información sobre el problema desde un fichero.
	 * 
	 * @param path
	 *            Ruta del fichero a leer
	 * @return Devuelve una matriz de Dijstra con el contenido del fichero y el
	 *         numero de ciudads
	 */
	public int[][] cargarFichero(String path) {
		StringTokenizer contenidoficherocontokens;

		if (path != null) {
			BufferedReader bufferdelectura;

			try {
				bufferdelectura = new BufferedReader(new FileReader(path));
				while (bufferdelectura.ready())
					workStr += bufferdelectura.readLine() + "\n";

			} catch (FileNotFoundException e) {
				System.err.println(e + "\nArchivo no encontrado."
						+ "Escriba el nuevo nombre de archivo y por área.");

			} catch (IOException e) {
				System.err.println(e
						+ "\nError de hardware durante la lectura."
						+ "Introduzca el nuevo nombre de archivo y por área.");
			}
		}

		contenidoficherocontokens = new StringTokenizer(workStr, "\n\t\r\f");

		numerodeciudades = obtenerNumeroDeCiudades(contenidoficherocontokens);

		return construirMatrizDijkstra(contenidoficherocontokens,
				numerodeciudades);
	}

	/**
	 * Metodo que busca el numero de ciudades en el string y lo devuelve
	 * 
	 * @param strTok
	 *            Un StringTokenizer con el contenido del fichero
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

	/**
	 * Metodo que mira las posiciones de las ciudades y crea una matriz con las
	 * distancias entre ellas.
	 * 
	 * @param coordenadas
	 *            Cordenadas de las ciudades.
	 * @param numerodeciudades
	 *            El numero de ciudades que hay.
	 * @return Devuelve una matriz con las distancias que hay entre las
	 *         ciudades.
	 */
	public int[][] calculoDistanciaPorEuclides(double[][] coordenadas,
			int numerodeciudades) {

		final int indiceprimeracoordenada = 0;
		final int indicesegundacoordenada = 1;

		int matrizDistancias[][] = new int[numerodeciudades][numerodeciudades];
		int dist = 0;

		for (int j = 0; j < coordenadas.length; j++) {
			for (int i = j; i < coordenadas.length; i++) {
				dist = (int) Math
						.floor(.5 + Math.sqrt(Math
								.pow(coordenadas[i][indiceprimeracoordenada]
										- coordenadas[j][indiceprimeracoordenada],
										2.0)
								+ Math.pow(
										coordenadas[i][indicesegundacoordenada]
												- coordenadas[j][indicesegundacoordenada],
										2.0)));

				matrizDistancias[i][j] = dist;
				matrizDistancias[j][i] = dist;
			}
		}

		return matrizDistancias;
	}

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

		matrizAuxiliar = calculoDistanciaPorEuclides(coordenadas,
				numerodeciudades);

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
