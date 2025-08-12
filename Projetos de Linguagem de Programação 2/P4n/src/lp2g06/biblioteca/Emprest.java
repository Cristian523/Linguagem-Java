package lp2g06.biblioteca;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.io.Serializable;

/* default */ class Emprest implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private GregorianCalendar dataEmprestimo;
	private GregorianCalendar dataDevolucao;
	private GregorianCalendar dataLimite; 
	private int codigo;
	
	/* Construtores */
	/* default */ Emprest(GregorianCalendar dataEmprestimo, GregorianCalendar dataDevolucao, GregorianCalendar dataLimite, boolean verificarData,
			int codigo) throws MinhasExcecoes{
		this.dataEmprestimo = dataEmprestimo;
		this.dataDevolucao = dataDevolucao;
		setDataLimite(dataLimite, verificarData);      // DataInvalidaException herda de MinhasExcecoes.
		setCodigo(codigo);
	}
	/* default */ Emprest(GregorianCalendar dataEmprestimo, GregorianCalendar dataDevolucao, int dia, int mes, int ano, boolean verificarData,
			int codigo) throws MinhasExcecoes{
		this.dataEmprestimo = dataEmprestimo;
		this.dataDevolucao = dataDevolucao;
		setDataLimite(dia, mes, ano, verificarData);  // DataInvalidaException herda de MinhasExcecoes.
		setCodigo(codigo);
	}
	/* default */ Emprest(GregorianCalendar dataEmprestimo, GregorianCalendar dataDevolucao, String dia, String mes, String ano, boolean verificarData,
			int codigo) throws MinhasExcecoes{
		this.dataEmprestimo = dataEmprestimo;
		this.dataDevolucao = dataDevolucao;
		setDataLimite(dia, mes, ano, verificarData);  // DataInvalidaException herda de MinhasExcecoes.
		setCodigo(codigo);
	}
	/* Construtores */
	
	/* Getters */
	public GregorianCalendar getDataEmprestimo() {
		return dataEmprestimo;
	}
	public GregorianCalendar getDataDevolucao() {
		return dataDevolucao;
	}
	public GregorianCalendar getDataLimite() {
		return dataLimite; 	
	}
	public int getCodigo() {
		return codigo;
	}
	/* Getters */
	
	/* Setters */
	public void setDataEmprestimo(GregorianCalendar dataEmprestimo) {
		this.dataEmprestimo = dataEmprestimo;
	}
	
	public void setDataDevolucao(GregorianCalendar dataDevolucao) {
		this.dataDevolucao = dataDevolucao;
	}
	
	public void setDataLimite(GregorianCalendar data, boolean verificarData) throws DataInvalidaException {
		int numero_dia = data.get(Calendar.DAY_OF_MONTH);
		int numero_mes = data.get(Calendar.MONTH) + 1;
		int numero_ano = data.get(Calendar.YEAR);
		if (verificarData && FuncoesDataAuxiliares.dataInferior(numero_dia, numero_mes, numero_ano))
			throw new DataInvalidaException("A data de devolucao deve ser posterior a data do emprestimo.");
		
		this.dataLimite = data;
	}
	
	public void setDataLimite(int dia, int mes, int ano, boolean verificarData) throws DataInvalidaException {
		GregorianCalendar data = FuncoesDataAuxiliares.converterData(dia, mes, ano);
		if (verificarData && FuncoesDataAuxiliares.dataInferior(dia, mes, ano))
			throw new DataInvalidaException("A data de devolucao deve ser posterior a data do emprestimo.");
		this.dataLimite = data;
	}
	
	public void setDataLimite(String dia, String mes, String ano, boolean verificarData) throws DataInvalidaException {
		GregorianCalendar data = FuncoesDataAuxiliares.converterData(dia, mes, ano);
		int numero_dia = data.get(Calendar.DAY_OF_MONTH);
		int numero_mes = data.get(Calendar.MONTH) + 1;
		int numero_ano = data.get(Calendar.YEAR);
		if (verificarData && FuncoesDataAuxiliares.dataInferior(numero_dia, numero_mes, numero_ano))
			throw new DataInvalidaException("A data de devolucao deve ser posterior a data do emprestimo.");
		
		this.dataLimite = data;
	}
	
	public void setCodigo(int codigo) throws MinhasExcecoes {
		if (codigo >= 1 && codigo <= 999)
			this.codigo = codigo;
		else
			throw new MinhasExcecoes("Codigo do livro e invalido, pois precisa pertencer ao intervalo entre 1 a 999.");
	}
	/* Setters */
	
	
	
    
    @Override
    public String toString() {
    	String devolucao;
    	if (dataDevolucao == null)
    		devolucao = "null";
    	else
    		devolucao = ValidaData.toString(dataDevolucao);
    	
    	return "codigoLivro: " + getCodigo() + "\n" +
    			"dataEmprestimo: " + ValidaData.toString(dataEmprestimo) + "\n" + 
    	       "dataDevolucao: " + devolucao + "\n" + 
    	       "dataLimite: " + ValidaData.toString(dataLimite) + "\n"
    	       ;
    }
    
 
}
