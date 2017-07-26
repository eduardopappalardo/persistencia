package persistencia;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import persistencia.excecao.PersistenciaException;

public class ConsultaUtil {
	
	private ConsultaUtil() {
	}
	
	static <T> List<T> consultar(ResultSet resultSet, Class<T> tipoObjetoRetorno) throws PersistenciaException, SQLException {
		ResultSetMetaData resultSetMetaData = null;
		List<T> objetos = new ArrayList<T>();
		T objeto = null;
		
		if(resultSet != null) {
			resultSetMetaData = resultSet.getMetaData();
			
			while(resultSet.next()) {
				objeto = Util.instanciarObjeto(tipoObjetoRetorno);
				
				for(int cont = 1; cont <= resultSetMetaData.getColumnCount(); cont++) {
					setValorAtributo(resultSetMetaData.getColumnLabel(cont), resultSet, objeto);
				}
				objetos.add(objeto);
			}
		}
		return objetos;
	}
	
	private static void setValorAtributo(String sequenciaNomesAtributos, ResultSet resultSet, Object objeto) throws PersistenciaException {
		String[] nomesAtributos = sequenciaNomesAtributos.split("\\.");
		Field atributo = null;
		Object objetoAtual = null;
		Object objetoAnterior = objeto;
		Object valorAtributo = null;
		
		for(int cont = 0; cont <= (nomesAtributos.length - 2); cont++) {
			atributo = Util.getAtributo(objetoAnterior.getClass(), nomesAtributos[cont]);
			objetoAtual = Util.getValorAtributo(atributo, objetoAnterior);
			
			if(objetoAtual == null) {
				objetoAtual = Util.instanciarObjeto(atributo.getType());
				Util.setValorAtributo(atributo, objetoAtual, objetoAnterior);
			}
			objetoAnterior = objetoAtual;
		}
		atributo = Util.getAtributo(objetoAnterior.getClass(), nomesAtributos[(nomesAtributos.length - 1)]);
		valorAtributo = Util.executarMetodoTipoDeDado(atributo.getType(), resultSet, sequenciaNomesAtributos);
		Util.setValorAtributo(atributo, valorAtributo, objetoAnterior);
	}
}