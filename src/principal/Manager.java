package principal;

import sistema.SimpleACS;

public class Manager {

	/**
	 * Método principal para iniciar la ejecución del sistema.
	 * 
	 * @param args No se utilizan
	 */
	public static void main(String args[]) {

		String ficheroaabrir = "eil51.tsp";
		SimpleACS main = new SimpleACS();

		main.iniciar(ficheroaabrir);
		main.ejecutar();
		main.finalizar();
	}
}
