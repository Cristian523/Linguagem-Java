package lp2g06.biblioteca;

public class LivroNaoCadastradoEx extends Exception {
	private static final long serialVersionUID = 1L;
	
	public LivroNaoCadastradoEx() {
		super("Um erro desconhecido ocorreu.");
	}
	
	public LivroNaoCadastradoEx(String message) {
		super(message);
	}

	@Override
	public String toString() {
		return "LivroNaoCadastradoEx: " + super.getMessage();
	}	
}
