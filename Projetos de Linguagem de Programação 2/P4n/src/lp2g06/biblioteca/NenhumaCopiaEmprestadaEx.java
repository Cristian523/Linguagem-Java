package lp2g06.biblioteca;

public class NenhumaCopiaEmprestadaEx extends Exception {
	private static final long serialVersionUID = 1L;

	public NenhumaCopiaEmprestadaEx() {
		super("Um erro desconhecido ocoreu");
	}
	public NenhumaCopiaEmprestadaEx(String message) {
		super(message);
	}

	@Override
	public String toString() {
		return ("NenhumaCopiaEmprestadaEx: " + super.getMessage());
	}
}