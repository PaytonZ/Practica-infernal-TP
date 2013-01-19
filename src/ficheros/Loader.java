package ficheros;

import java.util.*;
import java.io.*;

import matematicas.matrices.MatrizDijkstra;

public class Loader {

	int numCities = 0; //Numero de ciudades que hay.
	String workStr = ""; //String con el contenido del fichero.

	/**
	 * @param path
	 * @return Devuelve una matriz de Dijstra con el contenido del fichero y el numero de ciudads
	 */
	public int[][] loadData(String path) {
		StringTokenizer contenidoficherocontokens;
		
		if (path != null) {
			BufferedReader reader;

			try {
				reader = new BufferedReader(new FileReader(path));
				while (reader.ready())
					workStr += reader.readLine() + "\n";

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

		numCities = getNumberOfCities(contenidoficherocontokens);

		MatrizDijkstra matriz = new MatrizDijkstra();

		return matriz.construirMatrizDijkstra(contenidoficherocontokens,
				numCities);
	}

	/**
	 * Metodo que busca el numero de ciudades en el string y lo devuelve
	 * 
	 * @param Un StringTokenizer con el contenido del fichero
	 * @return El numero de ciudades en forma de entero
	 */
	private int getNumberOfCities(StringTokenizer strTok) {
		
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
	
//	private int getNumberOfCities(StringTokenizer strTok) {
//		String tempStr = "";
//
//		while (true) {
//			tempStr = strTok.nextToken();
//			if (tempStr.equals("DIMENSION")) {
//				strTok.nextToken();
//				tempStr = strTok.nextToken();
//				return Integer.parseInt(tempStr);
//			} else if (tempStr.equals("DIMENSION:")) {
//				tempStr = strTok.nextToken();
//				return Integer.parseInt(tempStr);
//			}
//		}
//	}
}
