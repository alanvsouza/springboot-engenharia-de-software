package br.unesp.rc.junit5tutorial.suite;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

import br.unesp.rc.junit5tutorial.VectorEqualTest;
import br.unesp.rc.junit5tutorial.VectorSizeTest;

@Suite
@SuiteDisplayName("Suíte de teste")
@SelectClasses({VectorEqualTest.class, VectorSizeTest.class})
public class JUnitTestSuite {

}
