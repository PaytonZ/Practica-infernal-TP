package ficheros;

import java.util.*;
import java.io.*;

import matematicas.matrices.MatrizDijkstra;

public class Loader {

	int numCities = 0;
	String workStr = "";

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
				System.err.println(e + "\nError de hardware durante la lectura."
						+ "Introduzca el nuevo nombre de archivo y por área.");
			}
		}
		
		contenidoficherocontokens = new StringTokenizer(workStr, "\n\t\r\f");
		
		numCities = getNumberOfCities(contenidoficherocontokens);
		
		MatrizDijkstra matriz = new MatrizDijkstra();
		
		return matriz.coustruirMatrizDijkstra(contenidoficherocontokens, numCities);
	}
	
	private int getNumberOfCities(StringTokenizer strTok) {
		String tempStr = "";
		while (true) {
			tempStr = strTok.nextToken();
			if (tempStr.equals("DIMENSION")) {
				strTok.nextToken();
				tempStr = strTok.nextToken();
				return Integer.parseInt(tempStr);
			} else if (tempStr.equals("DIMENSION:")) {
				tempStr = strTok.nextToken();
				return Integer.parseInt(tempStr);
			}
		}
	}
}