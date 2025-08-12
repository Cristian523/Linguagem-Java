package lp2g06.biblioteca;

public class UsuarioNaoCadastradoEx extends Exception {
	private static final long serialVersionUID = 1L;

	public UsuarioNaoCadastradoEx() {
		super("Um erro desconhecido ocorreu.");
	}

	public UsuarioNaoCadastradoEx(String message) {
		super(message);
	}
	
	@Override
	public String toString() {
		return "UsuarioNaoCadastradoEx: " + super.getMessage() + " ";
	}
}
