package br.ce.wcaquino.servicos;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import static br.ce.wcaquino.utils.DataUtils.*;

import br.ce.wcaquino.entidades.*;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocacaoServicesTest {

	private LocacaoService service;

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setup() {
		service = new LocacaoService();
	}

  @Test
	public void deveAlugarFilme() throws Exception {
		
		assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		//cenario de teste
		Usuario usuario = new Usuario("Ususario 1");
		List<Filme> filme = Arrays.asList(new Filme("Filme 1", 8, 4.0));

		//acao
		Locacao locacao = service.alugarFilme(usuario, filme);

		//verificacao
		error.checkThat(locacao.getValor(), is(equalTo(4.0)));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias( 1)), is(true));
	}
	
	@Test(expected = FilmeSemEstoqueException.class)
	public void naoDeveAlugarFilmeSemEstoque() throws Exception{
		//cenario de teste
		Usuario usuario = new Usuario("Ususario 1");
		List<Filme> filme = Arrays.asList(new Filme("Filme 1", 0, 4.0));

		//acao
		service.alugarFilme(usuario, filme);
	}

	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException{
		//cenario
		List<Filme> filme = Arrays.asList(new Filme("Filme 1", 8, 4.0));

		//acao
		try{
			service.alugarFilme(null, filme);
			Assert.fail();
		}
		catch(LocadoraException e){
			assertThat(e.getMessage(), is("usuario não cadastrado"));
		}
	}
	@Test
	public void naoDeveAlugarFilmeVazio() throws FilmeSemEstoqueException, LocadoraException{
		//cenario
		Usuario usuario = new Usuario("Ususario 1");

		exception.expect(LocadoraException.class);
		exception.expectMessage("filme nao cadastrado");

		//acao
		service.alugarFilme(usuario, null);
	}

	@Test
	public void devePagar75PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException{
		//cenario
		Usuario usuario = new Usuario("Usuario1");
		List<Filme> filme = Arrays.asList(
			new Filme("Filme1", 2, 4.0),
			new Filme("Filme2", 6, 4.0),
			new Filme("Filme3", 7, 4.0)
		);

		//acao
		Locacao resultado = service.alugarFilme(usuario, filme);

		//verificacao
		assertThat(resultado.getValor(), is(11d));
	}

	@Test
	public void devePagar50PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException{
		//cenario
		Usuario usuario = new Usuario("Usuario1");
		List<Filme> filme = Arrays.asList(
			new Filme("Filme1", 2, 4.0),
			new Filme("Filme2", 6, 4.0),
			new Filme("Filme3", 7, 4.0),
			new Filme("Filme4", 3, 4.0)

		);

		//acao
		Locacao resultado = service.alugarFilme(usuario, filme);

		//verificacao
		assertThat(resultado.getValor(), is(13d));
	}

	@Test
	public void devePagar25PctNoFilme5() throws FilmeSemEstoqueException, LocadoraException{
		//cenario
		Usuario usuario = new Usuario("Usuario1");
		List<Filme> filme = Arrays.asList(
			new Filme("Filme1", 2, 4.0),
			new Filme("Filme2", 6, 4.0),
			new Filme("Filme3", 7, 4.0),
			new Filme("Filme4", 3, 4.0),
			new Filme("Filme5", 7, 4.0)
		);

		//acao
		Locacao resultado = service.alugarFilme(usuario, filme);

		//verificacao
		assertThat(resultado.getValor(), is(14d));
	}

	@Test
	public void devePagar0PctNoFilme6() throws FilmeSemEstoqueException, LocadoraException{
		//cenario
		Usuario usuario = new Usuario("Usuario1");
		List<Filme> filme = Arrays.asList(
			new Filme("Filme1", 2, 4.0),
			new Filme("Filme2", 6, 4.0),
			new Filme("Filme3", 7, 4.0),
			new Filme("Filme4", 3, 4.0),
			new Filme("Filme5", 7, 4.0),
			new Filme("Filme5", 7, 4.0)

		);

		//acao
		Locacao resultado = service.alugarFilme(usuario, filme);

		//verificacao
		assertThat(resultado.getValor(), is(14d));
	}

	@Test
	@Ignore
	public void naoDeveDevolverFilmeNoDomingo() throws FilmeSemEstoqueException, LocadoraException{

		assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		//cenario
		Usuario usuario = new Usuario("usuario");
		List<Filme> filme = Arrays.asList(new Filme("Filme 1", 8, 4.0));

		//acao
		Locacao retorno = service.alugarFilme(usuario, filme);

		//verificacao
		boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
		assertTrue(ehSegunda);
	}
}
