package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Test;

public class CalculadoraTest {

  @Test
  public void deveSomarDoisValores(){
    //cenario
    int a = 5;
    int b = 3;

    Calculadora calc = new Calculadora();

    //acao
    int result = calc.somar(a, b);

    //vereficacao
    Assert.assertEquals(8, result);
  }

  @Test 
  public void deveSubtrairDoisValores(){
    //cenario
    int a = 5;
    int b = 3;

    Calculadora calc = new Calculadora();

    //acao
    int result = calc.subtrair(a, b);

    //vereficacao
    Assert.assertEquals(2, result);
  }
}
