package java;

import java.ficheros.TestLoader;
import java.matematicas.euclides.TestEuclidesDistancia;
import java.matematicas.matrices.TestMatrizDijkstra;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({ TestLoader.class, TestEuclidesDistancia.class, TestMatrizDijkstra.class})
public class TestLuancher {

}
