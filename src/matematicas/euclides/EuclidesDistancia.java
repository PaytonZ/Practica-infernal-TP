package matematicas.euclides;

public class EuclidesDistancia {

	public static int[][] calculoDistanciaPorEuclides(double[][] coordenadas,
			int numerodeciudades) {

		final int indiceprimeracoordenada = 0;
		final int indicesegundacoordenada = 1;

		int matrizAuxiliar[][] = new int[numerodeciudades][numerodeciudades];
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

				matrizAuxiliar[i][j] = dist;
				matrizAuxiliar[j][i] = dist;
			}
		}

		return matrizAuxiliar;
	}
}
