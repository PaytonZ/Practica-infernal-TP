package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import tests.inicio.TestLoader;

@RunWith(Suite.class)
@SuiteClasses({ TestLoader.class})
public class TestLauncher {}
