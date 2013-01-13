package ficheros;

import java.util.*;
import java.io.*;

public class Loader {

	int numCities = 0;
	String workStr = "";

	public int[][] loadData(String path) {
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
		numCities = getNumberOfCities(new StringTokenizer(workStr, "\n\t\r\f"));
		return buildDistMatrix(new StringTokenizer(workStr, "\n\t\r\f"));
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

	private int[][] buildDistMatrix(StringTokenizer strTok) {
		String tempStr = "";
		String edgeWeightType = "UNKNOWN";

		while (true) {
			tempStr = strTok.nextToken();
			if (tempStr.equals("EDGE WEIGHT TYPE")) {
				strTok.nextToken();
				tempStr = strTok.nextToken();
				edgeWeightType = tempStr;
				if (edgeWeightType.equals("EXPLICIT")) {
					return buildDistMatrixEXPLICIT(new StringTokenizer(workStr,
							"\n\t\r\f"));
				} else {
					return buildDistMatrixEUC2D(new StringTokenizer(workStr,
							"\n\t\r\f"));
				}
			} else if (tempStr.equals("EDGE WEIGHT TYPE:")) {
				tempStr = strTok.nextToken();
				edgeWeightType = tempStr;
				if (edgeWeightType.equals("EXPLICIT")) {
					return buildDistMatrixEXPLICIT(new StringTokenizer(workStr,
							"\n\t\r\f"));
				} else {
					return buildDistMatrixEUC2D(new StringTokenizer(workStr,
							"\n\t\r\f"));
				}
			}
		}
	}

	private int[][] buildDistMatrixEUC2D(StringTokenizer strTok) {
		final int X = 0;
		final int Y = 1;
		int tempMatrix[][] = new int[numCities][numCities];
		String tempStr = "";
//		double x, y = 0.0;
		int counter = 0;
		double coords[][] = new double[numCities][2];
		int dist = 0;
		while (true) {
			tempStr = strTok.nextToken();
			if (tempStr.equals("NODE COORD SECTION")) {

				while (!strTok.nextToken().equals("EOF")) {
					coords[counter][X] = Double.parseDouble(strTok.nextToken());
					coords[counter][Y] = Double.parseDouble(strTok.nextToken());
					counter++;
				}
				break;
			}
		}
		for (int j = 0; j < coords.length; j++) {
			for (int i = j; i < coords.length; i++) {
				dist = (int) Math.floor(.5 + Math.sqrt(

				Math.pow(coords[i][X] - coords[j][X], 2.0)
						+ Math.pow(coords[i][Y] - coords[j][Y], 2.0))); // TODO . por +

				tempMatrix[i][j] = dist;
				tempMatrix[j][i] = dist;
			}
		}
		return tempMatrix;
	}

	private int[][] buildDistMatrixEXPLICIT(StringTokenizer strTok) {
		int tempMatrix[][] = new int[numCities][numCities];
		int countI = 0;
		int countJ = 0;
		String tempStr = "";

		while (true) {
			tempStr = strTok.nextToken();
			if (tempStr.equals("EDGE WEIGHT SECTION")) {
				while (true) {
					tempStr = strTok.nextToken();
					if (tempStr.equals("EOF")) {
						return tempMatrix;
					}
					if (tempStr.equals("0")) {
						tempMatrix[countI][countJ] = Integer.parseInt(tempStr);
						tempMatrix[countJ][countI] = Integer.parseInt(tempStr);
						countI++;
						countJ = 0;
					} else {
						tempMatrix[countI][countJ] = Integer.parseInt(tempStr);
						tempMatrix[countJ][countI] = Integer.parseInt(tempStr);
						countJ++;
					}
				}
			}
		}
	}
}