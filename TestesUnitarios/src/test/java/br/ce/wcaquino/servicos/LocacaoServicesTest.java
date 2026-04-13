package br.ce.wcaquino.servicos;

import java.util.Date;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static br.ce.wcaquino.utils.DataUtils.*;

import br.ce.wcaquino.entidades.*;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;

public class LocacaoServicesTest {

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

  @Test
	public void testeLocacao() throws Exception {
		//cenario de teste
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Ususario 1");
		Filme filme = new Filme("Filme 1", 8, 4.0);

		//acao
		Locacao locacao = service.alugarFilme(usuario, filme);

		//verificacao
		error.checkThat(locacao.getValor(), is(equalTo(4.0)));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias( 1)), is(true));
	}
	
	@Test(expected = FilmeSemEstoqueException.class)
	public void testFilmeSemEstoque() throws Exception{
		//cenario de teste
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Ususario 1");
		Filme filme = new Filme("Filme 1", 0, 4.0);

		//acao
		service.alugarFilme(usuario, filme);
	}

	@Test
	public void testeUsuarioVazio() throws FilmeSemEstoqueException{
		//cenario
		LocacaoService service = new LocacaoService();
		Filme filme = new Filme("Filme 1", 2, 4.0);

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
	public void testeFilmeVazio() throws FilmeSemEstoqueException, LocadoraException{
		//cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Ususario 1");

		exception.expect(LocadoraException.class);
		exception.expectMessage("filme nao cadastrado");

		//acao
		service.alugarFilme(usuario, null);
	}
}
