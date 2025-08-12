

public abstract class PessoaIMC extends Pessoa {
	protected float peso;
    protected float altura;
    private static int numeroPessoas = 0;

    
    /* Construtores */
    public PessoaIMC() {
    	super();
    	numeroPessoas++;;
    }
    
    public PessoaIMC(String novoNome, String novoSobreNome, int dia, int mes, int ano) throws NomeInvalidoException, SobrenomeInvalidoException, DataInvalidaException {
		super(novoNome, novoSobreNome, dia, mes, ano);
		numeroPessoas++;
	}
	
	public PessoaIMC(String novoNome, String novoSobreNome, String dia, String mes, String ano) throws NomeInvalidoException, SobrenomeInvalidoException, DataInvalidaException {
		super(novoNome, novoSobreNome, dia, mes, ano);
		numeroPessoas++;
	}
    
   	public PessoaIMC(String novoNome, String novoSobreNome, int dia, int mes, int ano, String novoCPF, float novoPeso, float novaAltura) throws NomeInvalidoException, SobrenomeInvalidoException, DataInvalidaException, CPFInvalidoException, PesoInvalidoException, AlturaInvalidaException {
		super(novoNome, novoSobreNome, dia, mes, ano, novoCPF);
		setPeso(novoPeso);
		setAltura(novaAltura);
		numeroPessoas++;
	}
	
	public PessoaIMC(String novoNome, String novoSobreNome, String dia, String mes, String ano, String novoCPF, float novoPeso, float novaAltura) throws NomeInvalidoException, SobrenomeInvalidoException, DataInvalidaException, CPFInvalidoException, PesoInvalidoException, AlturaInvalidaException {
		super(novoNome, novoSobreNome, dia, mes, ano, novoCPF);
		setPeso(novoPeso);
		setAltura(novaAltura);
		numeroPessoas++;
    }
	
    
	/* Construtores */
	
	/* Getters */
	public float getPeso() {
        return this.peso;
    }
    public float getAltura() {
        return this.altura;
    }
	/* Getters */
	
	
	/* Setters */
	public void setPeso(float novoPeso) throws PesoInvalidoException{
        if (novoPeso >= 2 && novoPeso <= 200)
        	this.peso = novoPeso;
        else
        	throw new PesoInvalidoException("O peso fornecido nao esta no intervalo entre 2 a 200 kg.");
    }
    public void setAltura(float novaAltura) throws AlturaInvalidaException{
        if (novaAltura >= 0.4 && novaAltura <= 2.5) 
        	this.altura = novaAltura;
        else
        	throw new AlturaInvalidaException("A altura fornecida nao esta no intervalo entre 0.4 a 2.5 metros.");
    }
	/* Setters */
	
	
	
	/* Outos metodos */
	
	public float calculaIMC() {
		return peso / (altura * altura);
	}
	public abstract String resultIMC();
	
	public static int numPessoas() {
    	return numeroPessoas;
    }
	
	
	/* Outros metodos */
	
	
	@Override
	public String toString() {
		/* Foi o que eu entendi do trabalho */
    	String tipo = "";            
    	if (this instanceof Homem)
    		tipo = "Homem";
    	else if (this instanceof Mulher)
    		tipo = "Mulher";
    	/* Foi o que eu entendi do trabalho */
	
		return (super.toString() + "\nPeso: " + getPeso() + "\nAltura: " + getAltura() + "\nTipo: " + tipo);
	}
}
