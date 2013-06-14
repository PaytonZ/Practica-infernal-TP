package principal;

import java.io.IOException;

import sistema.Optimizador;


/**
 * Clase que inicia la ejecución del sistema
 * 
 * @author Juan Luis Perez Valbuena
 * @author Alvaro Quesada Pimentel
 * @author Juan Carlos Marco Gonzalez
 * @author Daniel Serrano Torres
 */
public class Manager {

	/**
	 * Método principal para iniciar la ejecución del sistema.
	 * 
	 * @param args
	 *            No se utilizan
	 */
	public static void main(String args[]) {

		String ficheroaabrir = "eil51.tsp";
		//Crea el Optimizador, lo inicializa y lo ejecuta
		Optimizador sistema= new Optimizador();
		sistema.iniciar(ficheroaabrir);
		sistema.ejecutar();

	}
}
