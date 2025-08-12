import java.util.Calendar;
import java.util.GregorianCalendar;


public class Pessoa {
    private String nome;
    private String sobreNome;
    private GregorianCalendar dataNasc; 
    private long numCPF;
	// private static int numeroPessoas = 0;


    /* Construtores */
	public Pessoa() {     // A contagem agora será feita em PessoaIMC
		//numeroPessoas++;
	}
	
	public Pessoa(String novoNome, String novoSobreNome, int dia, int mes, int ano) throws NomeInvalidoException, SobrenomeInvalidoException, DataInvalidaException {
		setNome(novoNome);
		setSobreNome(novoSobreNome);
		setDataNasc(dia, mes, ano);
		//numeroPessoas++;
	}
	
	public Pessoa(String novoNome, String novoSobreNome, String dia, String mes, String ano) throws NomeInvalidoException, SobrenomeInvalidoException, DataInvalidaException {
		setNome(novoNome);
		setSobreNome(novoSobreNome);
		setDataNasc(dia, mes, ano);
		//numeroPessoas++;
	}
	
	public Pessoa(String novoNome, String novoSobreNome, int dia, int mes, int ano, String novoCPF) throws NomeInvalidoException, SobrenomeInvalidoException, DataInvalidaException, CPFInvalidoException {
		setNome(novoNome);
		setSobreNome(novoSobreNome);
		setDataNasc(dia, mes, ano);
		setNumCPF(novoCPF);
		//numeroPessoas++;
	}
	
	public Pessoa(String novoNome, String novoSobreNome, String dia, String mes, String ano, String novoCPF) throws NomeInvalidoException, SobrenomeInvalidoException, DataInvalidaException, CPFInvalidoException{
		setNome(novoNome);
		setSobreNome(novoSobreNome);
		setDataNasc(dia, mes, ano);
		setNumCPF(novoCPF);
		//numeroPessoas++;
	}
    /* Construtores */


	/* Getters */
    public String getNome() {
        return this.nome;
    }
    public String getSobreNome() {
        return this.sobreNome;
    }
    public GregorianCalendar getDataNasc() {
    	return this.dataNasc;
    }
    public long getNumCPF() {
        return this.numCPF;
    }
   
	/* Getters */

	
	/* Setters */
    public void setNome(String novoNome) throws NomeInvalidoException {
        int n = novoNome.length();
        char c;
        for (int i = 0; i < n; i++) {
        	c = novoNome.charAt(i);
        	if (c == ' ')
        		throw new NomeInvalidoException("Seu primeiro nome não pode ter espaços.");
        	if (!Character.isAlphabetic(c))
        		throw new NomeInvalidoException("Foi encontrado em seu nome um caractere não pertencente ao intervalo a-z ou A-Z.");
        }
        this.nome = novoNome;
    }
    
    public void setSobreNome(String novoSobreNome) throws SobrenomeInvalidoException {
        int n = novoSobreNome.length();
        char c;
        for (int i = 0; i < n; i++) {
        	c = novoSobreNome.charAt(i);
        	if (c == ' ')
        		continue;
        	else if (!Character.isAlphabetic(c))
        		throw new SobrenomeInvalidoException("Foi encontrado em seu sobrenome um caractere não pertencente ao intervalo a-z ou A-Z.");
        }
        this.sobreNome = novoSobreNome;
    }
    
	public void setDataNasc(int dia, int mes, int ano) throws DataInvalidaException {
		if (!ValidaData.isDia(dia))
			throw new DataInvalidaException("Foi digitado um dia invalido.");
		if (!ValidaData.isMes(mes))
			throw new DataInvalidaException("Foi digitado um mes invalido.");
		if (!ValidaData.isAno(ano))
			throw new DataInvalidaException("Foi digitado um ano invalido.");
		if (!ValidaData.isDataValida(dia, mes, ano))
			throw new DataInvalidaException("Foi digitado uma data que não existe.");
		
		this.dataNasc = new GregorianCalendar(ano, mes - 1, dia);
	}
    
    public void setDataNasc(String dia, String mes, String ano) throws DataInvalidaException {
		if (!ValidaData.isDia(dia))
			throw new DataInvalidaException("Foi digitado um dia invalido.");
		int numero_dia = Integer.parseInt(dia);
		
		
		if (!ValidaData.isMes(mes))
			throw new DataInvalidaException("Foi digitado um mes invalido.");
		
		int numero_mes;
		try {
			numero_mes = Integer.parseInt(mes);	
		}
		catch (NumberFormatException e) {
			Meses aux = Meses.converter(mes);
			numero_mes = aux.numeroMes();
		}
		
		if (!ValidaData.isAno(ano))
			throw new DataInvalidaException("Foi digitado um ano invalido.");
		int numero_ano = Integer.parseInt(ano);
		
		if (!ValidaData.isDataValida(numero_dia, numero_mes, numero_ano))
			throw new DataInvalidaException("Foi digitado uma data que não existe.");
		
		this.dataNasc = new GregorianCalendar(numero_ano, numero_mes - 1, numero_dia);
	}

    public void setNumCPF(String novoCPF) throws CPFInvalidoException {
    	if (!ValidaCPF.isCPF(novoCPF))
    		throw new CPFInvalidoException("Foi digitado um CPF no formato errado ou o CPF nao pode existir.");
    	this.numCPF = ValidaCPF.toLong(novoCPF);
    }
    /* Setters */
    
    
  
    /*
    public static int numPessoas() {
    	return numeroPessoas;
    }
    */
    
    @Override
    public String toString() {
    	String dia = Integer.toString(dataNasc.get(Calendar.DAY_OF_MONTH));
    	if (dia.length() < 2) dia = "0" + dia; // Apenas formatacao mais bonitinha
    	 
    	String mes = Integer.toString(dataNasc.get(Calendar.MONTH) + 1);
    	if (mes.length() < 2) mes = "0" + mes; // Apenas formatacao mais bonitinha
    	
    	String ano = Integer.toString(dataNasc.get(Calendar.YEAR));
    	String cpf = Long.toString(numCPF);
    	int n = cpf.length();
    	while (n < 11) {
    		cpf = "0" + cpf;
    		n++;
    	}
    	return ("Nome: " + getNome() + "\nSobrenome: " + getSobreNome() + "\nData de Nascimento: " + dia + "/" + mes + "/" + ano + "\nCPF: " + cpf);  
    	
    }
   
}



