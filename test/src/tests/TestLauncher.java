package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import tests.inicio.TestLoader;
import tests.sistema.TestMatrizDijkstra;

@RunWith(Suite.class)
@SuiteClasses({ TestLoader.class, TestMatrizDijkstra.class, })
public class TestLauncher {
}
