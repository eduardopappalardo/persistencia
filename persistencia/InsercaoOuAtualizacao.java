package persistencia;

import java.sql.SQLException;
import persistencia.excecao.PersistenciaException;

public class InsercaoOuAtualizacao {
	
	private Entidade objetoDados;
	
	public InsercaoOuAtualizacao(Entidade objetoDados) throws PersistenciaException {
		
		if(objetoDados == null) {
			throw new PersistenciaException("Objeto de dados não definido.");
		}
		this.objetoDados = objetoDados;
	}
	
	public boolean executar() throws PersistenciaException, SQLException {
		
		if(new Atualizacao(this.objetoDados).executar() == 0) {
			return new Insercao(this.objetoDados).executar();
		}
		else {
			return true;
		}
	}
}