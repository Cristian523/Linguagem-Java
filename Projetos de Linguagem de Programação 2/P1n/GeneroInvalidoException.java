public class GeneroInvalidoException extends Exception {
	public GeneroInvalidoException(String mensagem) { 
		super(mensagem);
	}

	@Override
	public String toString() {
		return ("Genero invalido: " + super.getMessage());
	}

}
