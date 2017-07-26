package persistencia;

import java.sql.SQLException;
import persistencia.excecao.PersistenciaException;

public class Insercao extends InstrucaoSql<Boolean> {
	
	private Class<? extends Entidade> classe;
	private Entidade objetoDados;
	
	public Insercao(Entidade objetoDados) throws PersistenciaException {
		
		if(objetoDados == null) {
			throw new PersistenciaException("Objeto de dados não definido.");
		}
		this.classe = objetoDados.getClass();
		this.objetoDados = objetoDados;
	}
	
	String getSql() throws PersistenciaException {
		StringBuilder sql1 = new StringBuilder("");
		StringBuilder sql2 = new StringBuilder("");
		
		for(Util.PropriedadeAtributo propriedadeAtributo : Util.getPropriedadesAtributosEntidade(this.classe, this.objetoDados, Util.TipoCapturaAtributo.TODOS_ENTIDADE)) {
			
			if(Util.isAtributoAutoNumeracao(propriedadeAtributo.atributo)) {
				try {
					propriedadeAtributo.valorAtributo = Util.getAutoNumeracao(propriedadeAtributo, super.preparedStatement.getConnection());
				} catch (SQLException excecao) {
					throw new PersistenciaException(excecao.getMessage());
				}
			}
			Util.validarValorNulo(propriedadeAtributo);
			sql1.append(", " + Util.getNomeAtributo(this.classe, propriedadeAtributo.sequenciaNomesAtributos));
			sql2.append(", ?");
			super.adicionarParametroPreparedStatement(propriedadeAtributo.valorAtributo);
		}
		if(sql1.length() == 0) {
			throw new PersistenciaException("Nenhum dado para inserir.");
		}
		sql1.delete(0, 2);
		sql2.delete(0, 2);
		return "INSERT INTO " + Util.getNomeEntidade(this.classe) + "(" + sql1.toString() + ") VALUES(" + sql2.toString() + ")";
	}
	
	public Boolean executar() throws PersistenciaException, SQLException {
		super.executarPrivado();
		boolean registroInserido = super.preparedStatement.getUpdateCount() == 1;
		super.reiniciar();
		return registroInserido;
	}
}