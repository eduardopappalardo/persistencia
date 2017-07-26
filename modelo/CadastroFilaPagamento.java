package modelo;

import java.util.Date;
import persistencia.Entidade;
import persistencia.anotacao.Atributo;
import persistencia.anotacao.AnotacaoEntidade;

@AnotacaoEntidade(nome="TbCadastroFilaPagamento")
public class CadastroFilaPagamento implements Entidade {
	
	private static final long serialVersionUID = 241597694808482710L;
	
	@Atributo(chavePrimaria=true)
	private Integer cdCadastroFilaPagamento;
	private Integer cdColaboradorResponsavel;
	private Integer cdColaboradorAlteracao;
	private String nmCadastroFilaPagamento;
	private Character flAtivo;
	private Character flCadastroPadrao;
	private Date dtInclusao;
	private Date dtAlteracao;
	
	public CadastroFilaPagamento() {
	}

	public Integer getCdCadastroFilaPagamento() {
		return cdCadastroFilaPagamento;
	}

	public void setCdCadastroFilaPagamento(Integer cdCadastroFilaPagamento) {
		this.cdCadastroFilaPagamento = cdCadastroFilaPagamento;
	}

	public Integer getCdColaboradorResponsavel() {
		return cdColaboradorResponsavel;
	}

	public void setCdColaboradorResponsavel(Integer cdColaboradorResponsavel) {
		this.cdColaboradorResponsavel = cdColaboradorResponsavel;
	}

	public Integer getCdColaboradorAlteracao() {
		return cdColaboradorAlteracao;
	}

	public void setCdColaboradorAlteracao(Integer cdColaboradorAlteracao) {
		this.cdColaboradorAlteracao = cdColaboradorAlteracao;
	}

	public String getNmCadastroFilaPagamento() {
		return nmCadastroFilaPagamento;
	}

	public void setNmCadastroFilaPagamento(String nmCadastroFilaPagamento) {
		this.nmCadastroFilaPagamento = nmCadastroFilaPagamento;
	}

	public Character getFlAtivo() {
		return flAtivo;
	}

	public void setFlAtivo(Character flAtivo) {
		this.flAtivo = flAtivo;
	}

	public Character getFlCadastroPadrao() {
		return flCadastroPadrao;
	}

	public void setFlCadastroPadrao(Character flCadastroPadrao) {
		this.flCadastroPadrao = flCadastroPadrao;
	}

	public Date getDtInclusao() {
		return dtInclusao;
	}

	public void setDtInclusao(Date dtInclusao) {
		this.dtInclusao = dtInclusao;
	}

	public Date getDtAlteracao() {
		return dtAlteracao;
	}

	public void setDtAlteracao(Date dtAlteracao) {
		this.dtAlteracao = dtAlteracao;
	}
}