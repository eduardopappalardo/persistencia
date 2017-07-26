package persistencia;

import java.sql.SQLException;
import persistencia.excecao.PersistenciaException;

public class Atualizacao extends InstrucaoSqlComFiltro<Integer> {
	
	private Class<? extends Entidade> classe;
	private Entidade objetoDados;
	
	public Atualizacao(Entidade objetoDados) throws PersistenciaException {
		
		if(objetoDados == null) {
			throw new PersistenciaException("Objeto de dados não definido.");
		}
		this.classe = objetoDados.getClass();
		this.objetoDados = objetoDados;
		super.atributosEntidade = Util.getPropriedadesAtributosEntidade(this.classe, null, Util.TipoCapturaAtributo.TODOS_ENTIDADE);
	}
	
	String getSql() throws PersistenciaException {
		StringBuilder sql = new StringBuilder("");
		
		for(Util.PropriedadeAtributo propriedadeAtributo : Util.getPropriedadesAtributosEntidade(this.classe, this.objetoDados, Util.TipoCapturaAtributo.ENTIDADE_EXCETO_CHAVES_IMUTAVEIS)) {
			Util.validarValorNulo(propriedadeAtributo);
			sql.append(", " + Util.getNomeAtributo(this.classe, propriedadeAtributo.sequenciaNomesAtributos) + InstrucaoSqlComFiltro.TipoOperadorComparacao.IGUAL.getValor() + "?");
			super.adicionarParametroPreparedStatement(propriedadeAtributo.valorAtributo);
		}
		if(super.atributosFiltro.isEmpty()) {
			super.capturarFiltroChavePrimaria(this.objetoDados);
		}
		if(sql.length() == 0) {
			throw new PersistenciaException("Nenhum dado para atualizar.");
		}
		else if(super.atributosFiltro.isEmpty()) {
			throw new PersistenciaException("Nenhum dado para filtrar a atualização.");
		}
		sql.delete(0, 2);
		return "UPDATE " + Util.getNomeEntidade(this.classe) + " SET " + sql.toString() + super.getSql();
	}
	
	public Integer executar() throws PersistenciaException, SQLException {
		super.executarPrivado();
		int quantidadeRegistrosAfetados = super.preparedStatement.getUpdateCount();
		super.reiniciar();
		return quantidadeRegistrosAfetados;
	}
	
	String getNomeQualificadoAtributo(Util.PropriedadeAtributo propriedadeAtributo) throws PersistenciaException {
		return Util.getNomeAtributo(this.classe, propriedadeAtributo.sequenciaNomesAtributos);
	}
}