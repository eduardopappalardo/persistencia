package persistencia;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import persistencia.excecao.PersistenciaException;

public class ParametrosNomeados extends InstrucaoSql<Integer> {
	
	private String instrucaoSql;
	private Object objetoParametros;
	private Map<String, Object> mapaParametros = new HashMap<String, Object>();
	
	public ParametrosNomeados(String instrucaoSql) throws PersistenciaException {
		
		if(instrucaoSql == null) {
			throw new PersistenciaException("Instrução SQL não definida.");
		}
		this.instrucaoSql = instrucaoSql;
	}
	
	public ParametrosNomeados(String instrucaoSql, Object objetoParametros) throws PersistenciaException {
		this(instrucaoSql);
		
		if(objetoParametros == null) {
			throw new PersistenciaException("Objeto de parâmetros não definido.");
		}
	}
	
	public ParametrosNomeados(String instrucaoSql, Map<String, Object> mapaParametros) throws PersistenciaException {
		this(instrucaoSql);
		
		if(mapaParametros == null) {
			throw new PersistenciaException("Mapa de parâmetros não definido.");
		}
	}
	
	String getSql() throws PersistenciaException {
		Pattern pattern = Pattern.compile("\\?[a-zA-Z_0-9.]+");
		Matcher matcher = pattern.matcher(this.instrucaoSql);
		StringBuffer instrucaoSqlTemp = new StringBuffer("");
		String parametro = null;
		
		while(matcher.find()) {
			parametro = matcher.group().substring(1);
			this.adicionarParametroPreparedStatement(this.getParametro(parametro));
			matcher.appendReplacement(instrucaoSqlTemp, "?");
		}
		matcher.appendTail(instrucaoSqlTemp);
		return instrucaoSqlTemp.toString();
	}
	
	private Object getParametro(String parametro) throws PersistenciaException {
		
		if(this.objetoParametros != null) {
			return Util.getValorAtributo(parametro, this.objetoParametros);
		}
		else if(this.mapaParametros.containsKey(parametro)) {
			return this.mapaParametros.get(parametro);
		}
		else {
			throw new PersistenciaException("Parâmetro '" + parametro + "' não definido.");
		}
	}
	
	public Integer executar() throws PersistenciaException, SQLException {
		super.executarPrivado();
		int quantidadeRegistrosAfetados = this.preparedStatement.getUpdateCount();
		this.reiniciar();
		return quantidadeRegistrosAfetados;
	}
	
	public <T> List<T> executarConsulta(Class<T> tipoObjetoRetorno) throws PersistenciaException, SQLException {
		super.executarPrivado();
		List<T> objetos = ConsultaUtil.consultar(this.preparedStatement.getResultSet(), tipoObjetoRetorno);
		this.reiniciar();
		return objetos;
	}
}