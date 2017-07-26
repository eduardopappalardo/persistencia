package persistencia;

import java.sql.SQLException;

import modelo.CadastroAutorizacaoPagamento;
import persistencia.excecao.PersistenciaException;

public class Teste {
	
	public static void main(String[] args) throws PersistenciaException, SQLException {
		long millis = 0;
		long totalMillis = 0;
		
		Consulta<CadastroAutorizacaoPagamento> consulta = new Consulta<CadastroAutorizacaoPagamento>(CadastroAutorizacaoPagamento.class);
		
		for(int cont = 1; cont <= 1; cont++) {
			millis = System.currentTimeMillis();
			System.out.println(consulta.getSql());
			totalMillis += System.currentTimeMillis() - millis;
		}
		System.out.println(totalMillis / 1);
	}
	
	/*private static Connection getConnection() throws SQLException {
		try {
			Class.forName("com.sybase.jdbc3.jdbc.SybDataSource");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return DriverManager.getConnection("jdbc:sybase:Tds:192.168.50.64:5100/DBAUTORIZACAOVEICULO", "desenvolvedor", "provider");
	}*/	
}