package persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import persistencia.excecao.PersistenciaException;

abstract class InstrucaoSql<T> {
	
	private Connection conexao;
	private String instrucaoSql;
	private Map<Integer, Object> parametrosPreparedStatement = new HashMap<Integer, Object>();
	PreparedStatement preparedStatement;
	
	abstract String getSql() throws PersistenciaException;
	
	void adicionarParametroPreparedStatement(Object parametro) {
		this.parametrosPreparedStatement.put((this.parametrosPreparedStatement.size() + 1), parametro);
	}
	
	void executarPrivado() throws PersistenciaException, SQLException {
		this.instrucaoSql = this.getSql();
		this.preparedStatement = this.conexao.prepareStatement(this.instrucaoSql);
		
		for(Entry<Integer, Object> chaveValor : this.parametrosPreparedStatement.entrySet()) {
			Util.executarMetodoTipoDeDado((chaveValor.getValue() != null ? chaveValor.getValue().getClass() : null), this.preparedStatement, chaveValor.getKey(), chaveValor.getValue());
		}
		this.preparedStatement.execute();
		System.out.println(this.instrucaoSql);
	}
	
	abstract T executar() throws PersistenciaException, SQLException;
	
	void reiniciar() throws SQLException {
		this.instrucaoSql = null;
		this.parametrosPreparedStatement.clear();
		
		if(this.preparedStatement != null) {
			this.preparedStatement.close();
		}
	}
}