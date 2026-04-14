package br.ce.wcaquino.matchers;

public class MatchersProprios {

  public static DiaSemanaMatchers caiEm(Integer diaSemana){
    return new DiaSemanaMatchers(diaSemana);
  }

}
