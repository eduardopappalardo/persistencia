package modelo;

import java.util.Date;
import persistencia.Entidade;
import persistencia.anotacao.Atributo;
import persistencia.anotacao.AnotacaoEntidade;

@AnotacaoEntidade(nome="TbCadastroAutorizacaoPagamento", nomeBanco="DBAUTORIZACAOVEICULO", nomeEsquema="dbo")
public class CadastroAutorizacaoPagamento implements Entidade {
	
	private static final long serialVersionUID = 8070856572402578413L;
	
	@Atributo(chavePrimaria=true, chaveEstrangeira=true)
	private CadastroFilaPagamento cadastroFilaPagamento;
	@Atributo(chavePrimaria=true)
	private Integer nuProposta;
	private Integer cdOperador;
	@Atributo(chaveEstrangeira=true, permiteValorNulo=true, nome="cadastroFilaPagamento.cdCadastroFilaPagamento=codigo")
	private StatusPagamentoAutorizacao statusPagamentoAutorizacao;
	private Date dtInclusao;
	private Date dtAutomatizacao;
	private Character flPriorizado;
	private Character flPagamentoDirecionado;
	private Character flPagamentoBloqueado;
	private Character flExportacaoPriorizado;
	
	public CadastroAutorizacaoPagamento() {
	}

	public CadastroFilaPagamento getCadastroFilaPagamento() {
		return cadastroFilaPagamento;
	}

	public void setCadastroFilaPagamento(CadastroFilaPagamento cadastroFilaPagamento) {
		this.cadastroFilaPagamento = cadastroFilaPagamento;
	}

	public Integer getNuProposta() {
		return nuProposta;
	}

	public void setNuProposta(Integer nuProposta) {
		this.nuProposta = nuProposta;
	}

	public Integer getCdOperador() {
		return cdOperador;
	}

	public void setCdOperador(Integer cdOperador) {
		this.cdOperador = cdOperador;
	}

	public StatusPagamentoAutorizacao getStatusPagamentoAutorizacao() {
		return statusPagamentoAutorizacao;
	}

	public void setStatusPagamentoAutorizacao(
			StatusPagamentoAutorizacao statusPagamentoAutorizacao) {
		this.statusPagamentoAutorizacao = statusPagamentoAutorizacao;
	}

	public Date getDtInclusao() {
		return dtInclusao;
	}

	public void setDtInclusao(Date dtInclusao) {
		this.dtInclusao = dtInclusao;
	}

	public Date getDtAutomatizacao() {
		return dtAutomatizacao;
	}

	public void setDtAutomatizacao(Date dtAutomatizacao) {
		this.dtAutomatizacao = dtAutomatizacao;
	}

	public Character getFlPriorizado() {
		return flPriorizado;
	}

	public void setFlPriorizado(Character flPriorizado) {
		this.flPriorizado = flPriorizado;
	}

	public Character getFlPagamentoDirecionado() {
		return flPagamentoDirecionado;
	}

	public void setFlPagamentoDirecionado(Character flPagamentoDirecionado) {
		this.flPagamentoDirecionado = flPagamentoDirecionado;
	}

	public Character getFlPagamentoBloqueado() {
		return flPagamentoBloqueado;
	}

	public void setFlPagamentoBloqueado(Character flPagamentoBloqueado) {
		this.flPagamentoBloqueado = flPagamentoBloqueado;
	}

	public Character getFlExportacaoPriorizado() {
		return flExportacaoPriorizado;
	}

	public void setFlExportacaoPriorizado(Character flExportacaoPriorizado) {
		this.flExportacaoPriorizado = flExportacaoPriorizado;
	}
}