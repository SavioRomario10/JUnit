package br.ce.wcaquino.servicos;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {
  
  private LocacaoService service;
  
  @Parameter
  public List<Filme> filmes;
  @Parameter(value = 1)
  public Double valorLocacao;
  @Parameter(value = 2)
  public String cenario;

  private static Filme filme1 = new Filme("Filme1", 2, 4.0);
  private static Filme filme2 = new Filme("Filme1", 2, 4.0);
  private static Filme filme3 = new Filme("Filme1", 2, 4.0);
  private static Filme filme4 = new Filme("Filme1", 2, 4.0);
  private static Filme filme5 = new Filme("Filme1", 2, 4.0);
  private static Filme filme6 = new Filme("Filme1", 2, 4.0);
  private static Filme filme7 = new Filme("Filme1", 2, 4.0);

  @Before
  public void setup(){
    service = new LocacaoService();
  }

  @Parameters(name = "{2}")
  public static Collection<Object[]> getParametros(){
    return 
    
    Arrays.asList(new Object[][]{
      {Arrays.asList(filme1, filme2), 8d, "sem desconto"},
      {Arrays.asList(filme1, filme2, filme3), 11d, "3 Filmes: 25%"},
      {Arrays.asList(filme1, filme2, filme3, filme4), 13d, "4 Filmes: 50%"},
      {Arrays.asList(filme1, filme2, filme3, filme4, filme5), 14d, "5 Filmes: 75%"},
      {Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6), 14d, "6 Filmes: 100%"},
      {Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6, filme7), 18d, "sem deesconto"},

    });
  }

  @Test
	public void deveCalcularValorDeLocacao() throws FilmeSemEstoqueException, LocadoraException{
		//cenario
		Usuario usuario = new Usuario("Usuario1");

		//acao
		Locacao resultado = service.alugarFilme(usuario, filmes);

		//verificacao
		assertThat(resultado.getValor(), is(valorLocacao));
	}

}
