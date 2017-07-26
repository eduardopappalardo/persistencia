package modelo;

import java.util.Date;
import persistencia.Entidade;
import persistencia.anotacao.Atributo;
import persistencia.anotacao.AnotacaoEntidade;

@AnotacaoEntidade(nome="TbStatusPagamentoAutorizacao")
public class StatusPagamentoAutorizacao implements Entidade {
	
	private static final long serialVersionUID = -8506309008630063865L;
	
	@Atributo(chavePrimaria=true)
	private Integer cdStatusPagamentoAutorizacao;
	private Integer cdColaboradorInclusao;
	private String dsStatusPagamentoAutorizacao;
	private Date dtInclusao;
	private Character flAtivo;
	
	@Atributo(chaveEstrangeira=true)
	private CadastroFilaPagamento cadastroFilaPagamento;
	
	public StatusPagamentoAutorizacao() {
	}

	public Integer getCdStatusPagamentoAutorizacao() {
		return cdStatusPagamentoAutorizacao;
	}

	public void setCdStatusPagamentoAutorizacao(Integer cdStatusPagamentoAutorizacao) {
		this.cdStatusPagamentoAutorizacao = cdStatusPagamentoAutorizacao;
	}

	public Integer getCdColaboradorInclusao() {
		return cdColaboradorInclusao;
	}

	public void setCdColaboradorInclusao(Integer cdColaboradorInclusao) {
		this.cdColaboradorInclusao = cdColaboradorInclusao;
	}

	public String getDsStatusPagamentoAutorizacao() {
		return dsStatusPagamentoAutorizacao;
	}

	public void setDsStatusPagamentoAutorizacao(String dsStatusPagamentoAutorizacao) {
		this.dsStatusPagamentoAutorizacao = dsStatusPagamentoAutorizacao;
	}

	public Date getDtInclusao() {
		return dtInclusao;
	}

	public void setDtInclusao(Date dtInclusao) {
		this.dtInclusao = dtInclusao;
	}

	public Character getFlAtivo() {
		return flAtivo;
	}

	public void setFlAtivo(Character flAtivo) {
		this.flAtivo = flAtivo;
	}
}