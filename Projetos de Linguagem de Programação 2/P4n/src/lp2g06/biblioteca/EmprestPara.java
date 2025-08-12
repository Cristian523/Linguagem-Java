package lp2g06.biblioteca;

import java.util.GregorianCalendar;
import java.io.Serializable;

/* default */ class EmprestPara implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private GregorianCalendar dataEmprestimo;
	private GregorianCalendar dataDevolucao;
	private long numCPF;
	
	/* Construtor */
	/* default */ EmprestPara(GregorianCalendar dataEmprestimo, GregorianCalendar dataDevolucao, long numCPF) {
		this.dataEmprestimo = dataEmprestimo;
		this.dataDevolucao = dataDevolucao;
		this.numCPF = numCPF;
	}
	/* Construtor */
	
	
	/* Getters */
	public GregorianCalendar getDataEmprestimo() {
		return dataEmprestimo;
	}
	public GregorianCalendar getDataDevolucao() {
		return dataDevolucao;
	}
	public long getNumCPF() {
		return numCPF;
	}
	/* Getters */
	
	
	/* Setters */
	public void setDataEmprestimo(GregorianCalendar dataEmprestimo) {
		this.dataEmprestimo = dataEmprestimo;
	}
	public void setDataDevolucao(GregorianCalendar dataDevolucao) {
		this.dataDevolucao = dataDevolucao;
	}
	public void setNumCPF(long numCPF) {
		this.numCPF = numCPF;
	}
	/* Setters */
	
	@Override
	public String toString() {
		return "CPF: " + ValidaCPF.toString(numCPF) + "\n" +
				"dataEmprestimo: " + ValidaData.toString(dataEmprestimo) + "\n" +
			    "dataDevolucao: " + ValidaData.toString(dataDevolucao)
				;
	}
	
}
