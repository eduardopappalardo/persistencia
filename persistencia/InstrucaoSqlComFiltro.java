package persistencia;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import persistencia.excecao.PersistenciaException;

abstract class InstrucaoSqlComFiltro<T> extends InstrucaoSql<T> {
	
	Set<Util.PropriedadeAtributo> atributosEntidade;
	List<PropriedadeFiltro> atributosFiltro = new ArrayList<PropriedadeFiltro>();
	
	public void adicionarFiltroIgual(String sequenciaNomesAtributos, Object valorAtributo) throws PersistenciaException {
		this.adicionarFiltro(sequenciaNomesAtributos, valorAtributo, TipoOperadorComparacao.IGUAL, TipoOperadorLogico.E);
	}
	
	public void adicionarFiltroDiferente(String sequenciaNomesAtributos, Object valorAtributo) throws PersistenciaException {
		this.adicionarFiltro(sequenciaNomesAtributos, valorAtributo, TipoOperadorComparacao.DIFERENTE, TipoOperadorLogico.E);
	}
	
	public void adicionarFiltroMenor(String sequenciaNomesAtributos, Object valorAtributo) throws PersistenciaException {
		this.adicionarFiltro(sequenciaNomesAtributos, valorAtributo, TipoOperadorComparacao.MENOR, TipoOperadorLogico.E);
	}
	
	public void adicionarFiltroMenorIgual(String sequenciaNomesAtributos, Object valorAtributo) throws PersistenciaException {
		this.adicionarFiltro(sequenciaNomesAtributos, valorAtributo, TipoOperadorComparacao.MENORIGUAL, TipoOperadorLogico.E);
	}
	
	public void adicionarFiltroMaior(String sequenciaNomesAtributos, Object valorAtributo) throws PersistenciaException {
		this.adicionarFiltro(sequenciaNomesAtributos, valorAtributo, TipoOperadorComparacao.MAIOR, TipoOperadorLogico.E);
	}
	
	public void adicionarFiltroMaiorIgual(String sequenciaNomesAtributos, Object valorAtributo) throws PersistenciaException {
		this.adicionarFiltro(sequenciaNomesAtributos, valorAtributo, TipoOperadorComparacao.MAIORIGUAL, TipoOperadorLogico.E);
	}
	
	public void adicionarFiltroNulo(String sequenciaNomesAtributos) throws PersistenciaException {
		this.adicionarFiltro(sequenciaNomesAtributos, null, TipoOperadorComparacao.NULO, TipoOperadorLogico.E);
	}
	
	public void adicionarFiltroNaoNulo(String sequenciaNomesAtributos) throws PersistenciaException {
		this.adicionarFiltro(sequenciaNomesAtributos, null, TipoOperadorComparacao.NAONULO, TipoOperadorLogico.E);
	}
	
	public void adicionarFiltroParecido(String sequenciaNomesAtributos, Object valorAtributo) throws PersistenciaException {
		this.adicionarFiltro(sequenciaNomesAtributos, valorAtributo, TipoOperadorComparacao.PARECIDO, TipoOperadorLogico.E);
	}
	
	public void adicionarFiltroNaoParecido(String sequenciaNomesAtributos, Object valorAtributo) throws PersistenciaException {
		this.adicionarFiltro(sequenciaNomesAtributos, valorAtributo, TipoOperadorComparacao.NAOPARECIDO, TipoOperadorLogico.E);
	}
	
	void adicionarFiltro(String sequenciaNomesAtributos, Object valorAtributo, TipoOperadorComparacao tipoOperadorComparacao, TipoOperadorLogico tipoOperadorLogico) throws PersistenciaException {
		Util.PropriedadeAtributo propriedadeAtributo = Util.getAtributoEntidade(this.atributosEntidade, sequenciaNomesAtributos);
		
		if(!Util.isTipoDeDadoCompativel(propriedadeAtributo.atributo.getType(), valorAtributo)) {
			throw new PersistenciaException("Valor inválido para o atributo '" + propriedadeAtributo.atributo.getDeclaringClass().getName() + "." + propriedadeAtributo.sequenciaNomesAtributos + "'.");
		}
		propriedadeAtributo.valorAtributo = valorAtributo;
		this.adicionarFiltro(propriedadeAtributo, tipoOperadorComparacao, tipoOperadorLogico);
	}
	
	void adicionarFiltro(Util.PropriedadeAtributo propriedadeAtributo, TipoOperadorComparacao tipoOperadorComparacao, TipoOperadorLogico tipoOperadorLogico) throws PersistenciaException {
		
		if(propriedadeAtributo.valorAtributo == null && !(tipoOperadorComparacao.equals(TipoOperadorComparacao.NULO) || tipoOperadorComparacao.equals(TipoOperadorComparacao.NAONULO))) {
			throw new PersistenciaException("Filtro com valor nulo não permitido para o atributo '" + propriedadeAtributo.atributo.getDeclaringClass().getName() + "." + propriedadeAtributo.sequenciaNomesAtributos + "'.");
		}
		PropriedadeFiltro propriedadeFiltro = new PropriedadeFiltro();
		propriedadeFiltro.propriedadeAtributo = propriedadeAtributo;
		propriedadeFiltro.tipoOperadorComparacao = tipoOperadorComparacao;
		propriedadeFiltro.tipoOperadorLogico = tipoOperadorLogico;
		this.atributosFiltro.add(propriedadeFiltro);
	}
	
	void capturarFiltroChavePrimaria(Entidade objetoDados) throws PersistenciaException {
		
		if(objetoDados != null) {
			
			for(Util.PropriedadeAtributo propriedadeAtributo : Util.getPropriedadesAtributosEntidade(objetoDados.getClass(), objetoDados, Util.TipoCapturaAtributo.ENTIDADE_CHAVE_PRIMARIA)) {
				this.adicionarFiltro(propriedadeAtributo, TipoOperadorComparacao.IGUAL, TipoOperadorLogico.E);
			}
		}
	}
	
	String getSql() throws PersistenciaException {
		StringBuilder sql = new StringBuilder("");
		
		if(!this.atributosFiltro.isEmpty()) {
			
			for(PropriedadeFiltro propriedadeFiltro : this.atributosFiltro) {
				sql.append(propriedadeFiltro.tipoOperadorLogico.getValor());
				sql.append(this.getNomeQualificadoAtributo(propriedadeFiltro.propriedadeAtributo));
				sql.append(propriedadeFiltro.tipoOperadorComparacao.getValor());
				
				if(propriedadeFiltro.propriedadeAtributo.valorAtributo != null) {
					sql.append("?");
					super.adicionarParametroPreparedStatement(propriedadeFiltro.propriedadeAtributo.valorAtributo);
				}
			}
			sql.replace(0, (sql.indexOf(" ", 1) + 1), " WHERE ");
		}
		return sql.toString();
	}
	
	abstract String getNomeQualificadoAtributo(Util.PropriedadeAtributo propriedadeAtributo) throws PersistenciaException;
	
	enum TipoOperadorComparacao {
		
		IGUAL(" = "),
		DIFERENTE(" <> "),
		MENOR(" < "),
		MENORIGUAL(" <= "),
		MAIOR(" > "),
		MAIORIGUAL(" >= "),
		NULO(" IS NULL"),
		NAONULO(" IS NOT NULL"),
		PARECIDO(" LIKE "),
		NAOPARECIDO(" NOT LIKE ");
		
		private String valor;
		
		private TipoOperadorComparacao(String valor) {
			this.valor = valor;
		}
		
		String getValor() {
			return this.valor;
		}
	}
	
	enum TipoOperadorLogico {
		
		E(" AND "),
		OU(" OR ");
		
		private String valor;
		
		private TipoOperadorLogico(String valor) {
			this.valor = valor;
		}
		
		String getValor() {
			return this.valor;
		}
	}
	
	static class PropriedadeFiltro {
		
		Util.PropriedadeAtributo propriedadeAtributo;
		TipoOperadorComparacao tipoOperadorComparacao;
		TipoOperadorLogico tipoOperadorLogico;
		
		private PropriedadeFiltro() {
		}
	}
}