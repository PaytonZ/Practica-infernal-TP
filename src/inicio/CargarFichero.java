package inicio;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Clase que obtiene el contenido del fichero del problema del viajante.
 * Lo prepara para ser utilizado por clases de tratamiento de datos.
 * 
 * @author Juan Luis Perez Valbuena
 * @author Alvaro Quesada Pimentel
 * @author Juan Carlos Marco Gonzalez
 * @author Daniel Serrano Torres
 */
public class CargarFichero {

	/**
	 * Carga la información sobre el problema desde un fichero.
	 * 
	 * @param path Ruta del fichero a leer
	 * @return Devuelve una matriz de Dijstra con el contenido del fichero y el
	 *         numero de ciudads
	 */
	public StringTokenizer cargarFichero(String path) {
		
		String contenidoficherocontokens = new String();

		if (path != null) {
			BufferedReader bufferdelectura;

			try {
				bufferdelectura = new BufferedReader(new FileReader(path));
				while (bufferdelectura.ready())
					contenidoficherocontokens += bufferdelectura.readLine() + "\n";

			} catch (FileNotFoundException e) {
				System.err.println(e + "\nArchivo no encontrado."
						+ "Escriba el nuevo nombre de archivo y por área.");

			} catch (IOException e) {
				System.err.println(e
						+ "\nError de hardware durante la lectura."
						+ "Introduzca el nuevo nombre de archivo y por área.");
			}
		}

		return new StringTokenizer(contenidoficherocontokens, "\n\t\r\f");
	}
}

