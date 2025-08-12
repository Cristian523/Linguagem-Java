import java.util.Calendar;
import java.util.GregorianCalendar;

public class Mulher extends Pessoa{
		/* Construtores */
		public Mulher() {
			super();
		}
		public Mulher(String novoNome, String novoSobreNome, int dia, int mes, int ano) throws NomeInvalidoException, SobrenomeInvalidoException, DataInvalidaException {
			super(novoNome, novoSobreNome, dia, mes, ano);
		}
		
		public Mulher(String novoNome, String novoSobreNome, String dia, String mes, String ano) throws NomeInvalidoException, SobrenomeInvalidoException, DataInvalidaException {
			super(novoNome, novoSobreNome, dia, mes, ano);
		}
		
		public Mulher(String novoNome, String novoSobreNome, int dia, int mes, int ano, String novoCPF, float novoPeso, float novaAltura) throws NomeInvalidoException, SobrenomeInvalidoException, DataInvalidaException, CPFInvalidoException, PesoInvalidoException, AlturaInvalidaException {
			super(novoNome, novoSobreNome, dia, mes, ano, novoCPF, novoPeso, novaAltura);
		}
		
		public Mulher(String novoNome, String novoSobreNome, String dia, String mes, String ano, String novoCPF, float novoPeso, float novaAltura) throws NomeInvalidoException, SobrenomeInvalidoException, DataInvalidaException, CPFInvalidoException, PesoInvalidoException, AlturaInvalidaException {
			super(novoNome, novoSobreNome, dia, mes, ano, novoCPF, novoPeso, novaAltura);
		}
		/* Construtores */


	private int idade() {
        GregorianCalendar data_nascimento = getDataNasc();
        int dia = data_nascimento.get(Calendar.DAY_OF_MONTH);
        int mes = data_nascimento.get(Calendar.MONTH);
        int ano = data_nascimento.get(Calendar.YEAR);

        GregorianCalendar atual = new GregorianCalendar();
        int dia_atual = atual.get(Calendar.DAY_OF_MONTH);
        int mes_atual = atual.get(Calendar.MONTH);
        int ano_atual = atual.get(Calendar.YEAR);

        int idade_atual = ano_atual - ano;
        if (mes_atual < mes || (mes_atual == mes && dia_atual < dia))
            idade_atual--;

        atual = null;
        return idade_atual;
        
    }
    
    @Override
    public String toString() {
    	String cpf = Long.toString(getNumCPF());
    	int n = cpf.length();
    	while (n < 11) {
    		cpf = "0" + cpf;
    		n++;
    	}
    	
    	return ("Nome: " + getNome() + "\nSobrenome: " + getSobreNome() + "\nIdade: " + idade() + "\nGenero: Feminino" + "\nCPF: " + cpf + "\nPeso: " + getPeso() + "\nAltura: " + getAltura());
    }
       
}
