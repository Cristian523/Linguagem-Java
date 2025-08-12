package lp2g06.biblioteca;

public enum Meses {
	JANEIRO(1),
	FEVEREIRO(2),
	MARCO(3),
	ABRIL(4),
	MAIO(5),
	JUNHO(6),
	JULHO(7),
	AGOSTO(8),
	SETEMBRO(9),
	OUTUBRO(10),
	NOVEMBRO(11),
	DEZEMBRO(12);
		
	private final int numero;
		
	Meses(int n) {
		this.numero = n;
	}
		
	public int numeroMes() {
		return this.numero;
	}
		
	public static Meses converter(String str) {
		try {
			return Meses.valueOf(str.toUpperCase());
		}
		catch (IllegalArgumentException e) {
			return null;
		}
	}
}
