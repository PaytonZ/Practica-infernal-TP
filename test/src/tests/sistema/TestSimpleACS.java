package tests.sistema;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import sistema.SimpleACS;

public class TestSimpleACS {

	private SimpleACS ACS;
	
	private final int NUM_CIUDADES = 5;
	
	@Before
	public void setUp() throws Exception {
		ACS = new SimpleACS();
		
//		ACS.CITIES = NUM_CIUDADES;
	}

	@Test
	public void test() {
		ACS.iniciar("test1.tsp");
		int expected[] = new int[NUM_CIUDADES+1];
		for (int i = 0; i < expected.length-1; i++) {
			expected[i]=i;
		}
		expected[expected.length-1]=0;
//		assertArrayEqucals(expected, ACS.NNTour);
	}

}
