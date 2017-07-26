package persistencia;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import persistencia.anotacao.AnotacaoEntidade;
import persistencia.anotacao.Atributo;
import persistencia.anotacao.AutoNumeracao;
import persistencia.excecao.PersistenciaException;

public class Util {
	
	private static Map<Class<?>, Map<Class<?>, Method>> metodosTiposDeDados = new HashMap<Class<?>, Map<Class<?>, Method>>();
	private static Map<Class<?>, Class<?>> tiposDeDadosCompativeis = new HashMap<Class<?>, Class<?>>();
	
	static {
		try {
			Map<Class<?>, Method> metodosTiposDeDadosTemp = new HashMap<Class<?>, Method>();
			metodosTiposDeDadosTemp.put(PreparedStatement.class, PreparedStatement.class.getDeclaredMethod("setString", int.class, String.class));
			metodosTiposDeDados.put(null, metodosTiposDeDadosTemp);
			
			metodosTiposDeDadosTemp = new HashMap<Class<?>, Method>();
			metodosTiposDeDadosTemp.put(PreparedStatement.class, PreparedStatement.class.getDeclaredMethod("setDate", int.class, Date.class));
			metodosTiposDeDadosTemp.put(ResultSet.class, ResultSet.class.getDeclaredMethod("getDate", String.class));
			metodosTiposDeDados.put(java.util.Date.class, metodosTiposDeDadosTemp);
			
			metodosTiposDeDadosTemp = new HashMap<Class<?>, Method>();
			metodosTiposDeDadosTemp.put(PreparedStatement.class, PreparedStatement.class.getDeclaredMethod("setBigDecimal", int.class, BigDecimal.class));
			metodosTiposDeDadosTemp.put(ResultSet.class, ResultSet.class.getDeclaredMethod("getBigDecimal", String.class));
			metodosTiposDeDados.put(BigDecimal.class, metodosTiposDeDadosTemp);
			
			metodosTiposDeDadosTemp = new HashMap<Class<?>, Method>();
			metodosTiposDeDadosTemp.put(PreparedStatement.class, PreparedStatement.class.getDeclaredMethod("setString", int.class, String.class));
			metodosTiposDeDadosTemp.put(ResultSet.class, ResultSet.class.getDeclaredMethod("getString", String.class));
			metodosTiposDeDados.put(String.class, metodosTiposDeDadosTemp);
			metodosTiposDeDados.put(char.class, metodosTiposDeDadosTemp);
			metodosTiposDeDados.put(Character.class, metodosTiposDeDadosTemp);
			
			metodosTiposDeDadosTemp = new HashMap<Class<?>, Method>();
			metodosTiposDeDadosTemp.put(PreparedStatement.class, PreparedStatement.class.getDeclaredMethod("setBoolean", int.class, boolean.class));
			metodosTiposDeDadosTemp.put(ResultSet.class, ResultSet.class.getDeclaredMethod("getBoolean", String.class));
			metodosTiposDeDados.put(boolean.class, metodosTiposDeDadosTemp);
			metodosTiposDeDados.put(Boolean.class, metodosTiposDeDadosTemp);
			
			metodosTiposDeDadosTemp = new HashMap<Class<?>, Method>();
			metodosTiposDeDadosTemp.put(PreparedStatement.class, PreparedStatement.class.getDeclaredMethod("setByte", int.class, byte.class));
			metodosTiposDeDadosTemp.put(ResultSet.class, ResultSet.class.getDeclaredMethod("getByte", String.class));
			metodosTiposDeDados.put(byte.class, metodosTiposDeDadosTemp);
			metodosTiposDeDados.put(Byte.class, metodosTiposDeDadosTemp);
			
			metodosTiposDeDadosTemp = new HashMap<Class<?>, Method>();
			metodosTiposDeDadosTemp.put(PreparedStatement.class, PreparedStatement.class.getDeclaredMethod("setShort", int.class, short.class));
			metodosTiposDeDadosTemp.put(ResultSet.class, ResultSet.class.getDeclaredMethod("getShort", String.class));
			metodosTiposDeDados.put(short.class, metodosTiposDeDadosTemp);
			metodosTiposDeDados.put(Short.class, metodosTiposDeDadosTemp);
			
			metodosTiposDeDadosTemp = new HashMap<Class<?>, Method>();
			metodosTiposDeDadosTemp.put(PreparedStatement.class, PreparedStatement.class.getDeclaredMethod("setInt", int.class, int.class));
			metodosTiposDeDadosTemp.put(ResultSet.class, ResultSet.class.getDeclaredMethod("getInt", String.class));
			metodosTiposDeDados.put(int.class, metodosTiposDeDadosTemp);
			metodosTiposDeDados.put(Integer.class, metodosTiposDeDadosTemp);
			
			metodosTiposDeDadosTemp = new HashMap<Class<?>, Method>();
			metodosTiposDeDadosTemp.put(PreparedStatement.class, PreparedStatement.class.getDeclaredMethod("setLong", int.class, long.class));
			metodosTiposDeDadosTemp.put(ResultSet.class, ResultSet.class.getDeclaredMethod("getLong", String.class));
			metodosTiposDeDados.put(long.class, metodosTiposDeDadosTemp);
			metodosTiposDeDados.put(Long.class, metodosTiposDeDadosTemp);
			
			metodosTiposDeDadosTemp = new HashMap<Class<?>, Method>();
			metodosTiposDeDadosTemp.put(PreparedStatement.class, PreparedStatement.class.getDeclaredMethod("setFloat", int.class, float.class));
			metodosTiposDeDadosTemp.put(ResultSet.class, ResultSet.class.getDeclaredMethod("getFloat", String.class));
			metodosTiposDeDados.put(float.class, metodosTiposDeDadosTemp);
			metodosTiposDeDados.put(Float.class, metodosTiposDeDadosTemp);
			
			metodosTiposDeDadosTemp = new HashMap<Class<?>, Method>();
			metodosTiposDeDadosTemp.put(PreparedStatement.class, PreparedStatement.class.getDeclaredMethod("setDouble", int.class, double.class));
			metodosTiposDeDadosTemp.put(ResultSet.class, ResultSet.class.getDeclaredMethod("getDouble", String.class));
			metodosTiposDeDados.put(double.class, metodosTiposDeDadosTemp);
			metodosTiposDeDados.put(Double.class, metodosTiposDeDadosTemp);
		}
		catch (Exception excecao) {
			excecao.printStackTrace();
		}
		tiposDeDadosCompativeis.put(char.class, Character.class);
		tiposDeDadosCompativeis.put(boolean.class, Boolean.class);
		tiposDeDadosCompativeis.put(byte.class, Byte.class);
		tiposDeDadosCompativeis.put(short.class, Short.class);
		tiposDeDadosCompativeis.put(int.class, Integer.class);
		tiposDeDadosCompativeis.put(long.class, Long.class);
		tiposDeDadosCompativeis.put(float.class, Float.class);
		tiposDeDadosCompativeis.put(double.class, Double.class);
		tiposDeDadosCompativeis.put(Character.class, char.class);
		tiposDeDadosCompativeis.put(Boolean.class, boolean.class);
		tiposDeDadosCompativeis.put(Byte.class, byte.class);
		tiposDeDadosCompativeis.put(Short.class, short.class);
		tiposDeDadosCompativeis.put(Integer.class, int.class);
		tiposDeDadosCompativeis.put(Long.class, long.class);
		tiposDeDadosCompativeis.put(Float.class, float.class);
		tiposDeDadosCompativeis.put(Double.class, double.class);
	}
	
	private Util() {
	}
	
	static boolean isAtributoAutoNumeracao(Field atributo) {
		//Atributo anotacaoAtributo = atributo.getAnnotation(Atributo.class);
		//return anotacaoAtributo != null && anotacaoAtributo.autoNumeracao();
		return atributo.isAnnotationPresent(AutoNumeracao.class);
	}
	
	static boolean isAtributoChavePrimaria(Field atributo) {
		Atributo anotacaoAtributo = atributo.getAnnotation(Atributo.class);
		return anotacaoAtributo != null && anotacaoAtributo.chavePrimaria();
	}
	
	static boolean isAtributoChaveEstrangeira(Field atributo) {
		Atributo anotacaoAtributo = atributo.getAnnotation(Atributo.class);
		return anotacaoAtributo != null && anotacaoAtributo.chaveEstrangeira();
	}
	
	static boolean isAtributoValido(Field atributo) {
		Atributo anotacaoAtributo = atributo.getAnnotation(Atributo.class);
		return anotacaoAtributo == null || !anotacaoAtributo.ignoraAtributo();
	}
	
	static boolean permiteValorAtributoNulo(Field atributo) {
		Atributo anotacaoAtributo = atributo.getAnnotation(Atributo.class);
		return anotacaoAtributo != null && anotacaoAtributo.permiteValorNulo();
	}
	
	static String getNomeAtributo(Field atributo) {
		Atributo anotacaoAtributo = atributo.getAnnotation(Atributo.class);
		
		if(anotacaoAtributo != null && !anotacaoAtributo.nome().isEmpty()) {
			return anotacaoAtributo.nome();
		}
		else {
			return atributo.getName();
		}
	}
	
	static String getNomeAtributo(Class<?> classe, String sequenciaNomesAtributos) throws PersistenciaException {
		String[] nomesAtributos = sequenciaNomesAtributos.split("\\.");
		StringBuilder sequenciaNomesAtributosTemp = new StringBuilder("");
		String[] chaveValor = null;
		Atributo anotacaoAtributo = null;
		Field atributo = null;
		Class<?> classeAtual = classe;
		
		for(int cont = 0; cont <= (nomesAtributos.length - 2); cont++) {
			atributo = getAtributo(classeAtual, nomesAtributos[cont]);
			classeAtual = atributo.getType();
			anotacaoAtributo = atributo.getAnnotation(Atributo.class);
			sequenciaNomesAtributosTemp.append(nomesAtributos[cont] + ".");
			
			if(anotacaoAtributo != null && !anotacaoAtributo.nome().isEmpty()) {
				
				for(String nomesAtributosTemp : anotacaoAtributo.nome().split(";")) {
					chaveValor = nomesAtributosTemp.split("=");
					
					if(chaveValor.length != 2) {
						throw new PersistenciaException("Sintaxe de definição de nome(s) do(s) atributo(s) incorreta.");
					}
					if((sequenciaNomesAtributosTemp.toString() + chaveValor[0]).equals(sequenciaNomesAtributos)) {
						return chaveValor[1];
					}
				}
			}
		}
		atributo = getAtributo(classeAtual, nomesAtributos[(nomesAtributos.length - 1)]);
		return getNomeAtributo(atributo);
	}
	
	static String getNomeEntidade(Class<? extends Entidade> classe) {
		AnotacaoEntidade anotacaoClasse = classe.getAnnotation(AnotacaoEntidade.class);
		String nomeEntidade = classe.getSimpleName();
		String nomeEsquema = "";
		String nomeBanco = "";
		
		if(anotacaoClasse != null) {
			
			if(!anotacaoClasse.nome().isEmpty()) {
				nomeEntidade = anotacaoClasse.nome();
			}
			if(!anotacaoClasse.nomeEsquema().isEmpty()) {
				nomeEsquema = anotacaoClasse.nomeEsquema() + ".";
			}
			if(!anotacaoClasse.nomeBanco().isEmpty()) {
				nomeBanco = anotacaoClasse.nomeBanco() + ".";
			}
		}
		return (nomeBanco + nomeEsquema + nomeEntidade);
	}
	
	/**
	 * 
	 * CREATE TABLE AutoNumeracao (
	 *   sequenciaNomesAtributos VARCHAR(100),
	 *   ultimoValorUsado BIGINT NOT NULL,
	 *   CONSTRAINT PK_AutoNumeracao PRIMARY KEY sequenciaNomesAtributos
	 * )
	 * 
	 */
	static long getAutoNumeracao(PropriedadeAtributo propriedadeAtributo, Connection conexao) throws SQLException {
		AutoNumeracao anotacaoAutoNumeracao = propriedadeAtributo.atributo.getAnnotation(AutoNumeracao.class);
		long autoNumeracao = 0L;
		conexao.setAutoCommit(false);
		PreparedStatement consultaAutoNumeracao = null;
		PreparedStatement insereAutoNumeracao = null;
		PreparedStatement atualizaAutoNumeracao = conexao.prepareStatement("UPDATE AutoNumeracao SET ultimoValorUsado = ultimoValorUsado + ? WHERE sequenciaNomesAtributos = ?");
		atualizaAutoNumeracao.setLong(1, anotacaoAutoNumeracao.incremento());
		atualizaAutoNumeracao.setString(2, propriedadeAtributo.sequenciaNomesAtributos);
		
		if(atualizaAutoNumeracao.executeUpdate() > 0) {
			consultaAutoNumeracao = conexao.prepareStatement("SELECT ultimoValorUsado FROM AutoNumeracao WHERE sequenciaNomesAtributos = ?");
			consultaAutoNumeracao.setString(1, propriedadeAtributo.sequenciaNomesAtributos);
			consultaAutoNumeracao.executeQuery().next();
			autoNumeracao = consultaAutoNumeracao.getResultSet().getLong("ultimoValorUsado");
			consultaAutoNumeracao.close();
		}
		else {
			insereAutoNumeracao = conexao.prepareStatement("INSERT INTO AutoNumeracao(sequenciaNomesAtributos, ultimoValorUsado) VALUES(?, ?)");
			insereAutoNumeracao.setString(1, propriedadeAtributo.sequenciaNomesAtributos);
			insereAutoNumeracao.setLong(2, anotacaoAutoNumeracao.valorInicial());
			insereAutoNumeracao.execute();
			insereAutoNumeracao.close();
			autoNumeracao = anotacaoAutoNumeracao.valorInicial();
		}
		atualizaAutoNumeracao.close();
		conexao.commit();
		return autoNumeracao;
	}
	
	static Set<PropriedadeAtributo> getPropriedadesAtributosEntidade(Class<? extends Entidade> classe, Object objetoDados, TipoCapturaAtributo tipoCapturaAtributo) throws PersistenciaException {
		Set<PropriedadeAtributo> propriedadesAtributosEntidade = new HashSet<PropriedadeAtributo>();
		getPropriedadesAtributosEntidade(classe, objetoDados, tipoCapturaAtributo, true, "", propriedadesAtributosEntidade);
		return propriedadesAtributosEntidade;
	}
	
	private static void getPropriedadesAtributosEntidade(Class<?> classe, Object objetoDados, TipoCapturaAtributo tipoCapturaAtributo, boolean entidadePrincipal, String sequenciaNomesAtributos, Set<PropriedadeAtributo> propriedadesAtributosEntidade) throws PersistenciaException {
		Object valorAtributo = null;
		boolean chavePrimaria = false;
		
		for(Field atributo : classe.getDeclaredFields()) {
			
			if(!Modifier.isStatic(atributo.getModifiers()) && isAtributoValido(atributo) && !Collection.class.isAssignableFrom(atributo.getType())) {
				chavePrimaria = isAtributoChavePrimaria(atributo);
				
				if(tipoCapturaAtributo.equals(TipoCapturaAtributo.TODOS_RELACIONADOS_ENTIDADE)
					|| (!entidadePrincipal && chavePrimaria)
					|| (entidadePrincipal && (tipoCapturaAtributo.equals(TipoCapturaAtributo.TODOS_ENTIDADE)
							|| (chavePrimaria && tipoCapturaAtributo.equals(TipoCapturaAtributo.ENTIDADE_CHAVE_PRIMARIA)))
							|| (!chavePrimaria && !isAtributoAutoNumeracao(atributo) && tipoCapturaAtributo.equals(TipoCapturaAtributo.ENTIDADE_EXCETO_CHAVES_IMUTAVEIS)))) {
					
					valorAtributo = (objetoDados != null ? getValorAtributo(atributo, objetoDados) : null);
					
					if(isAtributoChaveEstrangeira(atributo)) {
						
						if(!Entidade.class.isAssignableFrom(atributo.getType())) {
							throw new PersistenciaException("Atributo '" + atributo.getDeclaringClass().getName() + "." + atributo.getName() + "' não é do tipo '" + Entidade.class.getName() + "'.");
						}
						getPropriedadesAtributosEntidade(atributo.getType(), valorAtributo, tipoCapturaAtributo, false, (sequenciaNomesAtributos + atributo.getName() + "."), propriedadesAtributosEntidade);
					}
					else {
						PropriedadeAtributo propriedadeAtributo = new PropriedadeAtributo();
						propriedadeAtributo.atributo = atributo;
						propriedadeAtributo.valorAtributo = valorAtributo;
						propriedadeAtributo.sequenciaNomesAtributos = (sequenciaNomesAtributos + atributo.getName());
						propriedadesAtributosEntidade.add(propriedadeAtributo);
					}
				}
			}
		}
	}
	
	static boolean isTipoDeDadoCompativel(Class<?> tipoDeDado, Object objeto) {
		
		if(objeto != null) {
			return tipoDeDado.equals(objeto.getClass()) || objeto.getClass().equals(tiposDeDadosCompativeis.get(tipoDeDado));
		}
		else {
			return true;
		}
	}
	
	private static Object converterObjeto(Class<?> tipoDeDadoDestino, Object objetoOrigem) throws PersistenciaException {
		
		if(!isTipoDeDadoCompativel(tipoDeDadoDestino, objetoOrigem)) {
			
			if(tipoDeDadoDestino.equals(java.util.Date.class) && objetoOrigem.getClass().equals(Date.class)) {
				return new java.util.Date(((Date) objetoOrigem).getTime());
			}
			else if(tipoDeDadoDestino.equals(Date.class) && objetoOrigem.getClass().equals(java.util.Date.class)) {
				return new Date(((java.util.Date) objetoOrigem).getTime());
			}
			else if((tipoDeDadoDestino.equals(char.class) || tipoDeDadoDestino.equals(Character.class)) && objetoOrigem.getClass().equals(String.class)) {
				return ((String) objetoOrigem).trim().charAt(0);
			}
			else if(tipoDeDadoDestino.equals(String.class) && objetoOrigem.getClass().equals(Character.class)) {
				return ((Character) objetoOrigem).toString();
			}
			else {
				throw new PersistenciaException("Não foi possível converter '" + objetoOrigem.getClass().getName() + "' para '" + tipoDeDadoDestino.getName() + "'.");
			}
		}
		else {
			return objetoOrigem;
		}
	}
	
	static Object executarMetodoTipoDeDado(Class<?> tipoDeDado, Object objeto, Object... parametros) throws PersistenciaException {
		Method metodoTipoDeDado = getMetodoTipoDeDado(tipoDeDado, objeto.getClass());
		
		if(metodoTipoDeDado.getDeclaringClass().equals(PreparedStatement.class)) {
			return executarMetodo(metodoTipoDeDado, objeto, parametros[0], converterObjeto(metodoTipoDeDado.getParameterTypes()[1], parametros[1]));
		}
		else if(metodoTipoDeDado.getDeclaringClass().equals(ResultSet.class)) {
			return converterObjeto(tipoDeDado, executarMetodo(metodoTipoDeDado, objeto, parametros));
		}
		else {
			return executarMetodo(metodoTipoDeDado, objeto, parametros);
		}
	}
	
	private static Method getMetodoTipoDeDado(Class<?> tipoDeDado, Class<?> tipoObjeto) throws PersistenciaException {
		Map<Class<?>, Method> metodosTiposDeDadoTemp = metodosTiposDeDados.get(tipoDeDado);
		
		if(metodosTiposDeDadoTemp == null || !metodosTiposDeDadoTemp.containsKey(tipoObjeto)) {
			throw new PersistenciaException("Método inexistente para o tipo de dado '" + (tipoDeDado != null ? tipoDeDado.getName() : "null") + "' e objeto '" + tipoObjeto.getName() + "'.");
		}
		else {
			return metodosTiposDeDadoTemp.get(tipoObjeto);
		}
	}
	
	static PropriedadeAtributo getAtributoEntidade(Set<PropriedadeAtributo> propriedadesAtributosEntidade, String sequenciaNomesAtributos) throws PersistenciaException {
		
		for(PropriedadeAtributo propriedadeAtributo : propriedadesAtributosEntidade) {
			
			if(propriedadeAtributo.sequenciaNomesAtributos.equals(sequenciaNomesAtributos)) {
				return propriedadeAtributo;
			}
		}
		throw new PersistenciaException("Atributo '" + sequenciaNomesAtributos + "' inexistente ou inacessível.");
	}
	
	static Field getAtributo(Class<?> classe, String sequenciaNomesAtributos) throws PersistenciaException {
		Field atributo = null;
		Class<?> classeAtual = classe;
		
		for(String nomeAtributo : sequenciaNomesAtributos.split("\\.")) {
			try {
				atributo = classeAtual.getDeclaredField(nomeAtributo);
			}
			catch (Exception excecao) {
				throw new PersistenciaException("Atributo '" + classe.getName() + "." + sequenciaNomesAtributos + "' inexistente ou inacessível.");
			}
			classeAtual = atributo.getType();
		}
		return atributo;
	}
	
	static void validarValorNulo(PropriedadeAtributo propriedadeAtributo) throws PersistenciaException {
		
		if((!Util.permiteValorAtributoNulo(propriedadeAtributo.atributo) || Util.isAtributoChavePrimaria(propriedadeAtributo.atributo)) && propriedadeAtributo.valorAtributo == null) {
			throw new PersistenciaException("Valor nulo não permitido para o atributo '" + propriedadeAtributo.atributo.getDeclaringClass().getName() + "." + propriedadeAtributo.sequenciaNomesAtributos + "'.");
		}
	}

	static Method getMetodo(Class<?> classe, String nomeMetodo, Class<?>... argumentos) throws PersistenciaException {
		try {
			return classe.getDeclaredMethod(nomeMetodo, argumentos);
		}
		catch (Exception excecao) {
			throw new PersistenciaException("Método '" + classe.getName() + "." + nomeMetodo + "' inexistente ou inacessível.");
		}
	}
	
	static <T> T instanciarObjeto(Class<T> classe) throws PersistenciaException {
		try {
			return classe.newInstance();
		}
		catch (Exception excecao) {
			throw new PersistenciaException("Falha ao instanciar classe '" + classe.getName() + "'.");
		}
	}
	
	static Object executarMetodo(Method metodo, Object objeto, Object... parametros) throws PersistenciaException {
		try {
			return metodo.invoke(objeto, parametros);
		}
		catch (Exception excecao) {
			throw new PersistenciaException("Falha ao executar método '" + metodo.getDeclaringClass().getName() + "." + metodo.getName() + "'.");
		}
	}
	
	static Object getValorAtributo(Field atributo, Object objeto) throws PersistenciaException {
		String nomeMetodo = "get" + capitalizarPalavra(atributo.getName());
		Method metodo = getMetodo(atributo.getDeclaringClass(), nomeMetodo);
		return executarMetodo(metodo, objeto);
	}
	
	static void setValorAtributo(Field atributo, Object valorAtributo, Object objeto) throws PersistenciaException {
		String nomeMetodo = "set" + capitalizarPalavra(atributo.getName());
		Method metodo = getMetodo(atributo.getDeclaringClass(), nomeMetodo, atributo.getType());
		executarMetodo(metodo, objeto, valorAtributo);
	}
	
	static String capitalizarPalavra(String palavra) {
		return Character.toUpperCase(palavra.charAt(0)) + palavra.substring(1);
	}
	
	static Object getValorAtributo(String sequenciaNomesAtributos, Object objeto) throws PersistenciaException {
		String[] nomesAtributos = sequenciaNomesAtributos.split("\\.");
		Field atributo = null;
		Object objetoAtual = null;
		Object objetoAnterior = objeto;
		
		for(int cont = 0; cont <= (nomesAtributos.length - 2); cont++) {
			atributo = getAtributo(objetoAnterior.getClass(), nomesAtributos[cont]);
			objetoAtual = getValorAtributo(atributo, objetoAnterior);
			
			if(objetoAtual == null) {
				return null;
			}
			objetoAnterior = objetoAtual;
		}
		atributo = getAtributo(objetoAnterior.getClass(), nomesAtributos[(nomesAtributos.length - 1)]);
		return getValorAtributo(atributo, objetoAnterior);
	}
	
	enum TipoCapturaAtributo {
		TODOS_RELACIONADOS_ENTIDADE,
		TODOS_ENTIDADE,
		ENTIDADE_CHAVE_PRIMARIA,
		ENTIDADE_EXCETO_CHAVES_IMUTAVEIS;
	}
	
	static class PropriedadeAtributo {
		
		Field atributo;
		Object valorAtributo;
		String sequenciaNomesAtributos;
		
		private PropriedadeAtributo() {
		}
		
		public int hashCode() {
			return this.sequenciaNomesAtributos.hashCode();
		}
		
		public boolean equals(Object objeto) {
			
			if(objeto instanceof PropriedadeAtributo) {
				return this.sequenciaNomesAtributos.equals(((PropriedadeAtributo) objeto).sequenciaNomesAtributos);
			}
			else {
				return false;
			}
		}
	}
}