package tests.inicio;

import static org.junit.Assert.*;

import inicio.Inicio;

import java.util.StringTokenizer;

import org.junit.Before;
import org.junit.Test;

public class TestLoader {

	private Inicio inicio;
	
	@Before
	public void setUp() throws Exception {
		 inicio = new Inicio();
	}

	@Test
	public void testLoadData() {
		String path = "test.tsp";
		
		StringTokenizer contenidoFichero = inicio.cargarFichero(path);
		
		assertNotNull("Error: El fichero no se a cargado correctamente", contenidoFichero);
	}
}
