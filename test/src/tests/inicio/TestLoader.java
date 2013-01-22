package tests.inicio;

import static org.junit.Assert.*;

import inicio.CargarFichero;

import java.util.StringTokenizer;

import org.junit.Before;
import org.junit.Test;

public class TestLoader {

	private CargarFichero inicio;
	
	@Before
	public void setUp() throws Exception {
		 inicio = new CargarFichero();
	}

	@Test
	public void testLoadData() {
		String path = "test.tsp";
		
		StringTokenizer contenidoFichero = inicio.cargarFichero(path);
		assertNotNull("Error: El fichero no se a cargado correctamente", contenidoFichero);
		
		String cadenarecostrudiacontenidofichero = reconstruirCadena(contenidoFichero);
		
		assertNotNull("Error: El fichero no se a cargado correctamente", cadenarecostrudiacontenidofichero);
		assertTrue("Error: El fichero no contiene ningún dato o se ha cargado incorrectamente", cadenarecostrudiacontenidofichero.length() > 0);
	}
	
	private String reconstruirCadena(StringTokenizer contenidoFichero) {
		
		StringBuilder cadena = new StringBuilder();
		
		
		while (contenidoFichero.hasMoreTokens()) {
			cadena.append(contenidoFichero.nextToken());
		}
		
		return cadena.toString();
	}
}
