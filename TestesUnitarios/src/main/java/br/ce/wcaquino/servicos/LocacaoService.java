package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.DAOs.LocacaoDao;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoService {

	private LocacaoDao dao;
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filme) throws FilmeSemEstoqueException, LocadoraException{
		
		if(usuario == null){
			throw new LocadoraException("usuario não cadastrado");
		}
		if(filme == null|| filme.isEmpty()){
			throw new LocadoraException("filme nao cadastrado");
		}
		for(Filme f : filme){
			if (f.getEstoque() == 0) {
				throw new FilmeSemEstoqueException();
			}
		}

		Locacao locacao = new Locacao();
		locacao.setFilme(filme);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		Double valorTotal = 0d;

		for(int i=0; i<filme.size(); i++){
			Filme f = filme.get(i);
			Double valorFilme = f.getPrecoLocacao();

			switch (i) {
				case 2: valorFilme = valorFilme*0.75; break;
				case 3: valorFilme = valorFilme*0.5; break;
				case 4: valorFilme = valorFilme*0.25; break;
				case 5: valorFilme = 0d; break;
			}

			valorTotal += valorFilme;
		}
		locacao.setValor(valorTotal);
		
		//Entrega no dia seguinte
		Date dataEntrega = new Date();

		if (DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
		dataEntrega = adicionarDias(dataEntrega, 1);
		}
		
		locacao.setDataRetorno(adicionarDias(dataEntrega, 1));
		
		//Salvando a locacao...	
		dao.salvar(locacao);

		return locacao;
	}

	public void setLocacaoDao(LocacaoDao dao) {
		this.dao = dao;
	}
}