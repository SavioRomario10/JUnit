package br.ce.wcaquino.matchers;

import java.sql.Date;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import br.ce.wcaquino.utils.DataUtils;

public class DiaSemanaMatchers extends TypeSafeMatcher<Date>{

  private Integer diaSemana;

  public DiaSemanaMatchers(Integer diaSemana){
    this.diaSemana = diaSemana;
  }

  public void describeTo(Description arg0){

  }

  @Override
  protected boolean matchesSafely(Date data){
    return DataUtils.verificarDiaSemana(data, diaSemana);
  }
}
