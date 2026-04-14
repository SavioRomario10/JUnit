package br.ce.wcaquino.servicos;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
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
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.FilmeBuilder.umFilmeSemEstoque;

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
		Usuario usuario = umUsuario().agora();
		List<Filme> filme = Arrays.asList(umFilme().comValor(5.0).agora());

		//acao
		Locacao locacao = service.alugarFilme(usuario, filme);

		//verificacao
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias( 1)), is(true));
	}
	
	@Test(expected = FilmeSemEstoqueException.class)
	public void naoDeveAlugarFilmeSemEstoque() throws Exception{
		//cenario de teste
		Usuario usuario = umUsuario().agora();
		List<Filme> filme = Arrays.asList(umFilmeSemEstoque().agora());

		//acao
		service.alugarFilme(usuario, filme);
	}

	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException{
		//cenario
		List<Filme> filme = Arrays.asList(umFilme().agora());

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
		Usuario usuario = umUsuario().agora();

		exception.expect(LocadoraException.class);
		exception.expectMessage("filme nao cadastrado");

		//acao
		service.alugarFilme(usuario, null);
	}

	@Test
	public void naoDeveDevolverFilmeNoDomingo() throws FilmeSemEstoqueException, LocadoraException{

		assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filme = Arrays.asList(umFilme().agora());

		//acao
		Locacao retorno = service.alugarFilme(usuario, filme);

		//verificacao
		boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
		assertTrue(ehSegunda);
	}
}
