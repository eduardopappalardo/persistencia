package persistencia;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import persistencia.Util.PropriedadeAtributo;
import persistencia.excecao.PersistenciaException;

public class Consulta<T extends Entidade> extends InstrucaoSqlComFiltro<List<T>> {
	
	private Class<T> classe;
	private Set<Util.PropriedadeAtributo> atributosSelecao = new HashSet<Util.PropriedadeAtributo>();
	private Map<Util.PropriedadeAtributo, TipoOrdenacao> atributosOrdenacao = new LinkedHashMap<Util.PropriedadeAtributo, TipoOrdenacao>();
	
	public Consulta(Class<T> classe) throws PersistenciaException {
		
		if(classe == null) {
			throw new PersistenciaException("Classe não definida.");
		}
		this.classe = classe;
		super.atributosEntidade = Util.getPropriedadesAtributosEntidade(this.classe, null, Util.TipoCapturaAtributo.TODOS_RELACIONADOS_ENTIDADE);
	}
	
	public void adicionarSelecao(String sequenciaNomesAtributos) throws PersistenciaException {
		Util.PropriedadeAtributo propriedadeAtributo = Util.getAtributoEntidade(super.atributosEntidade, sequenciaNomesAtributos);
		this.atributosSelecao.add(propriedadeAtributo);
	}
	
	public void adicionarOrdenacaoAscendente(String sequenciaNomesAtributos) throws PersistenciaException {
		this.adicionarOrdenacao(sequenciaNomesAtributos, TipoOrdenacao.ASCENDENTE);
	}
	
	public void adicionarOrdenacaoDescendente(String sequenciaNomesAtributos) throws PersistenciaException {
		this.adicionarOrdenacao(sequenciaNomesAtributos, TipoOrdenacao.DESCENDENTE);
	}
	
	private void adicionarOrdenacao(String sequenciaNomesAtributos, TipoOrdenacao tipoOrdenacao) throws PersistenciaException {
		Util.PropriedadeAtributo propriedadeAtributo = Util.getAtributoEntidade(super.atributosEntidade, sequenciaNomesAtributos);
		this.atributosOrdenacao.put(propriedadeAtributo, tipoOrdenacao);
	}
	
	String getSql() throws PersistenciaException {
		StringBuilder sqlAtributosSelecao = new StringBuilder("");
		StringBuilder sqlAtributosOrdenacao = new StringBuilder("");
		StringBuilder sqlEntidades = new StringBuilder(Util.getNomeEntidade(this.classe) + " AS [" + this.classe.getSimpleName() + "]");
		Set<String> relacionamentos = new TreeSet<String>();
		Field atributo = null;
		String apelidoEntidadeAnterior = null;
		String apelidoEntidade = null;
		String relacionamentoAnterior = "?";
		String operadorLogico = null;
		String tipoJuncao = null;
		
		if(this.atributosSelecao.isEmpty()) {
			this.atributosSelecao = Util.getPropriedadesAtributosEntidade(this.classe, null, Util.TipoCapturaAtributo.TODOS_ENTIDADE);
		}
		if(this.atributosSelecao.isEmpty()) {
			throw new PersistenciaException("Nenhum dado para consultar.");
		}
		if(!this.atributosOrdenacao.isEmpty()) {
			
			for(Entry<Util.PropriedadeAtributo, TipoOrdenacao> chaveValor : this.atributosOrdenacao.entrySet()) {
				
				if(!this.atributosSelecao.contains(chaveValor.getKey())) {
					throw new PersistenciaException("O atributo '" + this.classe.getName() + "." + chaveValor.getKey().sequenciaNomesAtributos + "' deve estar presente na seleção para ser ordenado.");
				}
				sqlAtributosOrdenacao.append(", " + this.getNomeQualificadoAtributo(chaveValor.getKey()) + chaveValor.getValue().getValor());
			}
			sqlAtributosOrdenacao.replace(0, 2, " ORDER BY ");
		}
		for(Util.PropriedadeAtributo propriedadeAtributo : this.atributosSelecao) {
			sqlAtributosSelecao.append(", " + this.getNomeQualificadoAtributo(propriedadeAtributo) + " AS [" + propriedadeAtributo.sequenciaNomesAtributos + "]");
			this.adicionarRelacionamentos(relacionamentos, propriedadeAtributo);
		}
		for(InstrucaoSqlComFiltro.PropriedadeFiltro propriedadeFiltro : super.atributosFiltro) {
			this.adicionarRelacionamentos(relacionamentos, propriedadeFiltro.propriedadeAtributo);
		}
		for(String relacionamento : relacionamentos) {
			atributo = Util.getAtributo(this.classe, relacionamento);
			apelidoEntidadeAnterior = this.classe.getSimpleName() + this.voltarSequenciaNomesAtributos(relacionamento, 1, ".");
			apelidoEntidade = this.classe.getSimpleName() + "." + relacionamento;
			operadorLogico = "";
			
			if(!relacionamento.startsWith(relacionamentoAnterior) || " INNER JOIN ".equals(tipoJuncao)) {
				
				if(Util.permiteValorAtributoNulo(atributo)) {
					tipoJuncao = " LEFT OUTER JOIN ";
				}
				else {
					tipoJuncao = " INNER JOIN ";
				}
			}
			relacionamentoAnterior = relacionamento;
			sqlEntidades.append(tipoJuncao);
			sqlEntidades.append(Util.getNomeEntidade((Class<? extends Entidade>) atributo.getType()));
			sqlEntidades.append(" AS [" + apelidoEntidade + "] ON ");
			
			for(Util.PropriedadeAtributo propriedadeAtributo : Util.getPropriedadesAtributosEntidade((Class<? extends Entidade>) atributo.getType(), null, Util.TipoCapturaAtributo.ENTIDADE_CHAVE_PRIMARIA)) {
				sqlEntidades.append(operadorLogico);
				sqlEntidades.append("[" + apelidoEntidadeAnterior + "]." + Util.getNomeAtributo(this.classe, (relacionamento + "." + propriedadeAtributo.sequenciaNomesAtributos)));
				sqlEntidades.append(InstrucaoSqlComFiltro.TipoOperadorComparacao.IGUAL.getValor());
				sqlEntidades.append("[" + apelidoEntidade + "]." + Util.getNomeAtributo(propriedadeAtributo.atributo));
				operadorLogico = InstrucaoSqlComFiltro.TipoOperadorLogico.E.getValor();
			}
		}
		sqlAtributosSelecao.delete(0, 2);
		return "SELECT " + sqlAtributosSelecao.toString() + " FROM " + sqlEntidades.toString() + super.getSql() + sqlAtributosOrdenacao.toString();
	}
	
	public List<T> executar() throws PersistenciaException, SQLException {
		super.executarPrivado();
		List<T> objetos = ConsultaUtil.consultar(super.preparedStatement.getResultSet(), this.classe);
		super.reiniciar();
		return objetos;
	}
	
	private void adicionarRelacionamentos(Set<String> relacionamentos, Util.PropriedadeAtributo propriedadeAtributo) {
		String sequenciaNomesAtributos = this.voltarSequenciaNomesAtributos(propriedadeAtributo.sequenciaNomesAtributos, (Util.isAtributoChavePrimaria(propriedadeAtributo.atributo) ? 2 : 1), "");
		
		while(!sequenciaNomesAtributos.isEmpty()) {
			
			if(relacionamentos.contains(sequenciaNomesAtributos)) {
				break;
			}
			relacionamentos.add(sequenciaNomesAtributos);
			sequenciaNomesAtributos = this.voltarSequenciaNomesAtributos(sequenciaNomesAtributos, 1, "");
		}
	}
	
	private String voltarSequenciaNomesAtributos(String sequenciaNomesAtributos, int quantidadeNiveis, String prefixoSeNaoVazio) {
		StringBuilder sequenciaNomesAtributoTemp = new StringBuilder(sequenciaNomesAtributos);
		
		for(int cont = 1, posicao = -1; cont <= quantidadeNiveis; cont++) {
			posicao = sequenciaNomesAtributoTemp.lastIndexOf(".");
			
			if(posicao != -1) {
				sequenciaNomesAtributoTemp.delete(posicao, sequenciaNomesAtributoTemp.length());
			}
			else {
				return "";
			}
		}
		return prefixoSeNaoVazio + sequenciaNomesAtributoTemp.toString();
	}
	
	String getNomeQualificadoAtributo(PropriedadeAtributo propriedadeAtributo) throws PersistenciaException {
		return "[" + this.classe.getSimpleName() + this.voltarSequenciaNomesAtributos(propriedadeAtributo.sequenciaNomesAtributos, (Util.isAtributoChavePrimaria(propriedadeAtributo.atributo) ? 2 : 1), ".") + "]." + Util.getNomeAtributo(this.classe, propriedadeAtributo.sequenciaNomesAtributos);
	}
	
	private enum TipoOrdenacao {
		
		ASCENDENTE(" ASC"),
		DESCENDENTE(" DESC");
		
		private String valor;
		
		private TipoOrdenacao(String valor) {
			this.valor = valor;
		}
		
		private String getValor() {
			return this.valor;
		}
	}
}