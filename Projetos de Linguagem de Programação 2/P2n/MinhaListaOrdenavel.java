import java.util.List;
import java.util.Comparator;
import java.util.Collections;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ArrayList;

enum Criterio {NOME, NOME_REVERSO, PESO, PESO_REVERSO, IMC, IMC_REVERSO, ALTURA, ALTURA_REVERSO, GENERO, GENERO_REVERSO, DATA, DATA_REVERSO, IDADE, IDADE_REVERSO, CPF, CPF_REVERSO};

public class MinhaListaOrdenavel {
	private ArrayList<PessoaIMC> lista;

	public MinhaListaOrdenavel() {
		this.lista = new ArrayList<PessoaIMC>();
	}
	
	public MinhaListaOrdenavel(int n){
		this.lista = new ArrayList<PessoaIMC>(n);
	}

	public PessoaIMC get(int index) {
		return this.lista.get(index);
	}
	
	public void add(PessoaIMC p) {
		this.lista.add(p);
	}
	
	/* Comparadores */
	
	// Comparador para pesos
	public Comparator<PessoaIMC> pesoC = new Comparator<PessoaIMC>() {
		public int compare(PessoaIMC p1, PessoaIMC p2) {
			float p1f, p2f;
			p2f = p2.getPeso();
			p1f = p1.getPeso();
			return Float.compare(p1f, p2f);
		}
	};
	
	// Comparador para alturas
	public Comparator<PessoaIMC> alturaC = new Comparator<PessoaIMC>() {
		public int compare(PessoaIMC p1, PessoaIMC p2) {
			float h1f, h2f;
			h2f = p2.getAltura();
			h1f = p1.getAltura();
			return Float.compare(h1f, h2f);
		}
	};
	
	// Comparador para datas de nascimento
	public Comparator<PessoaIMC> dataC = new Comparator<PessoaIMC>() {
		public int compare(PessoaIMC p1, PessoaIMC p2) {
			GregorianCalendar data1, data2;
			data1 = p1.getDataNasc();
			data2 = p2.getDataNasc();
			
			// Pegando cada dia, mes e ano
			int dia1, mes1, ano1, dia2, mes2, ano2, comparacao;
			
			// Comparando anos
			ano1 = data1.get(Calendar.YEAR);
			ano2 = data2.get(Calendar.YEAR);
			comparacao = Integer.compare(ano1, ano2);
			if (comparacao != 0)
				return comparacao;
			
			//Comparando meses
			mes1 = data1.get(Calendar.MONTH);         
			mes2 = data2.get(Calendar.MONTH);
			comparacao = Integer.compare(mes1, mes2);
			if (comparacao != 0)
				return comparacao;
			
			// Comparando dias
			dia1 = data1.get(Calendar.DAY_OF_MONTH);  
			dia2 = data2.get(Calendar.DAY_OF_MONTH);
			comparacao = Integer.compare(dia1, dia2);
			return comparacao;
			
		}
	};
	
	// Comparador para idades
	public Comparator<PessoaIMC> idadeC = new Comparator<PessoaIMC>() {
		public int compare(PessoaIMC p1, PessoaIMC p2) {
			int idade1, idade2;
			GregorianCalendar data1 = p1.getDataNasc();
			GregorianCalendar data2= p2.getDataNasc();
			
			return Integer.compare(idade(data1), idade(data2));
			
		}
	};
	
	// Comparador para nomes
	public Comparator<PessoaIMC> nomeC = new Comparator<PessoaIMC>() {
		public int compare(PessoaIMC p1, PessoaIMC p2) {
			String nome1, nome2;
			nome1 = p1.getNome() + p1.getSobreNome();
			nome2 = p2.getNome() + p2.getSobreNome();
			nome1 = nome1.replaceAll("[ ]", "");
			nome2 = nome2.replaceAll("[ ]", "");
			
			return nome1.compareToIgnoreCase(nome2);
			
		}
	};
	
	// Comparador para CPF's
	public Comparator<PessoaIMC> cpfC = new Comparator<PessoaIMC>() {
		public int compare(PessoaIMC p1, PessoaIMC p2) {
			long cpf1, cpf2;
			cpf1 = p1.getNumCPF();
			cpf2 = p2.getNumCPF();
			return Long.compare(cpf1, cpf2);
			
		}
	};
	
	// Comparador para IMC's 
	public Comparator<PessoaIMC> imcC = new Comparator<PessoaIMC>() {
		public int compare(PessoaIMC p1, PessoaIMC p2) {
			float imc1, imc2;
			imc1 = p1.calculaIMC();
			imc2 = p2.calculaIMC();
			return Float.compare(imc1, imc2);
			
		}
	};
	
	// Comparador para genero (se é Homem ou Mulher)
	public Comparator<PessoaIMC> generoC = new Comparator<PessoaIMC>() {
		public int compare(PessoaIMC p1, PessoaIMC p2) {
			int genero1, genero2;
			if (p1 instanceof Mulher)
				genero1 = 0;
			else
				genero1 = 1;
				
			if (p2 instanceof Mulher)
				genero2 = 0;
			else
				genero2 = 1;
				
			return genero1 - genero2;  
		}
	
	};
	/* Comparadores */
	
	public ArrayList<PessoaIMC> ordena(Criterio c) throws IllegalArgumentException{
		switch (c) {
			case PESO:
				Collections.sort(this.lista, pesoC);				
				break;
			case PESO_REVERSO:
				Collections.sort(this.lista, pesoC.reversed());
				break;
			case ALTURA:
				Collections.sort(this.lista, alturaC);
				break;
			case ALTURA_REVERSO:
				Collections.sort(this.lista, alturaC.reversed());
				break;
			case DATA:
				Collections.sort(this.lista, dataC);
				break;
			case DATA_REVERSO:
				Collections.sort(this.lista, dataC.reversed());
				break;
			case IDADE:
				Collections.sort(this.lista, idadeC);
				break;
			case IDADE_REVERSO:
				Collections.sort(this.lista, idadeC.reversed());
				break;
			case NOME:
				Collections.sort(this.lista, nomeC);
				break;
			case NOME_REVERSO:
				Collections.sort(this.lista, nomeC.reversed());
				break;
			case CPF:
				Collections.sort(this.lista, cpfC);
				break;
			case CPF_REVERSO:
				Collections.sort(this.lista, cpfC.reversed());
				break;
			case IMC:
				Collections.sort(this.lista, imcC);
				break;
			case IMC_REVERSO:
				Collections.sort(this.lista, imcC.reversed());
				break;
			case GENERO:
				Collections.sort(this.lista, generoC);
				break;
			case GENERO_REVERSO:
				Collections.sort(this.lista, generoC.reversed());
				break;
			default:
				throw new IllegalArgumentException("Foi digitado um critério de ordenação inexistente.");
		}
		return this.lista;
	}
	
	
	private int idade(GregorianCalendar data) {
        GregorianCalendar data_nascimento = data;
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
    	int n = this.lista.size();
    	String str = "";
    	PessoaIMC p;
    	for (int i = 0; i < n; i++) {
    		p = this.lista.get(i);
    		str += p.toString() + "\n\n";
    	}
    	return str; 
    }
    
    
    public static String toString(ArrayList<PessoaIMC> saida) {
    	int n = saida.size();
    	String str = "";
    	PessoaIMC p;
    	for (int i = 0; i < n; i++) {
    		p = saida.get(i);
    		str += p.toString() + "\n\n";
    	}
    	return str; 
    }
	
}
