package persistencia;

import java.sql.SQLException;
import persistencia.excecao.PersistenciaException;

public class Exclusao extends InstrucaoSqlComFiltro<Integer> {
	
	private Class<? extends Entidade> classe;
	private Entidade objetoDados;
	
	public Exclusao(Class<? extends Entidade> classe) throws PersistenciaException {
		
		if(classe == null) {
			throw new PersistenciaException("Classe não definida.");
		}
		this.classe = classe;
		super.atributosEntidade = Util.getPropriedadesAtributosEntidade(this.classe, null, Util.TipoCapturaAtributo.TODOS_ENTIDADE);
	}
	
	public Exclusao(Entidade objetoDados) throws PersistenciaException {
		
		if(objetoDados == null) {
			throw new PersistenciaException("Objeto de dados não definido.");
		}
		this.classe = objetoDados.getClass();
		this.objetoDados = objetoDados;
	}
	
	String getSql() throws PersistenciaException {
		
		if(super.atributosFiltro.isEmpty()) {
			super.capturarFiltroChavePrimaria(this.objetoDados);
		}
		else if(super.atributosFiltro.isEmpty()) {
			throw new PersistenciaException("Nenhum dado para filtrar a exclusão.");
		}
		return "DELETE FROM " + Util.getNomeEntidade(this.classe) + super.getSql();
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