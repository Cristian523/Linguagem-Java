public class NomeInvalidoException extends Exception {
	public NomeInvalidoException(String mensagem) {
		super(mensagem);
	}
	
	@Override
	public String toString() {
		return ("Nome invalido: " + super.getMessage());
	}
	
}
