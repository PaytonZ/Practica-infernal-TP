package matematicas.euclides;

public class EuclidesDistancia {

	/**
	 * Metodo que mira las posiciones de las ciudades y crea una matriz con las distancias entre ellas.
	 * 
	 * @param coordenadas Cordenadas de las ciudades.
	 * @param numerodeciudades El numero de ciudades que hay.
	 * @return Devuelve una matriz con las distancias que hay entre las ciudades.
	 */
	public static int[][] calculoDistanciaPorEuclides(double[][] coordenadas,
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
}
