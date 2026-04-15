package br.ce.wcaquino.builders;

import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

import java.util.Date;

public class LocacaoBuilder {

  private Locacao locacao;

  private LocacaoBuilder(){}

  public static LocacaoBuilder umaLocacao(){
    LocacaoBuilder builder = new LocacaoBuilder();
    builder.locacao = new Locacao();
    
    builder.locacao.setDataLocacao(new Date());
    builder.locacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(1));
    builder.locacao.setValor(4.0);

    return builder;
  }

  public LocacaoBuilder comDataLocacao(Date dataLocacao){
    locacao.setDataLocacao(dataLocacao);
    return this;
  }

  public LocacaoBuilder comDataRetorno(Date dataRetorno){
    locacao.setDataRetorno(dataRetorno);
    return this;
  }

  public LocacaoBuilder comValor(Double valor){
    locacao.setValor(valor);
    return this;
  }

  public LocacaoBuilder atrasado(){
    
    locacao.setDataLocacao(DataUtils.obterDataComDiferencaDias(-4));
    locacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(-2));

    return this;
  }

  public LocacaoBuilder comUsuario(Usuario usuario){
    locacao.setUsuario(usuario);
    return this;
  }

  public Locacao agora(){
    return locacao;
  }
}
