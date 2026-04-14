package br.ce.suites;

import br.ce.wcaquino.servicos.*;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  LocacaoServicesTest.class,
  CalculoValorLocacaoTest.class,
  CalculadoraTest.class
})
public class SuitesExecucao {
  
}
