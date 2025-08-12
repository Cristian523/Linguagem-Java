package lp2g06.biblioteca;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class FuncoesDataAuxiliares {	
	// OBSERVAÇÃO: Esses métodos/trechos de métodos eu pego de Pessoa e faco uma aptacao para essa classe, pois vou usar em outras classes e não quis colocar em ValidaData ou Pessoa.
	
	
	/* Metodos auxiliares para data*/
	public static GregorianCalendar converterData(int dia, int mes, int ano) throws DataInvalidaException {
		if (!ValidaData.isDia(dia))
			throw new DataInvalidaException("Foi digitado um dia invalido.");
		if (!ValidaData.isMes(mes))
			throw new DataInvalidaException("Foi digitado um mes invalido.");
		if (!ValidaData.isAno(ano))
			throw new DataInvalidaException("Foi digitado um ano invalido.");
		if (!ValidaData.isData(dia, mes, ano))
			throw new DataInvalidaException("Foi digitado uma data que não existe.");
		
		return (new GregorianCalendar(ano, mes - 1, dia));
	}
    
    public static GregorianCalendar converterData(String dia, String mes, String ano) throws DataInvalidaException {
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
		if (!ValidaData.isData(numero_dia, numero_mes, numero_ano))
			throw new DataInvalidaException("Foi digitado uma data que não existe.");
		
		return (new GregorianCalendar(numero_ano, numero_mes - 1, numero_dia));
	}
    
    
    
    /* OBSERVACAO: os dois metodos abaixo sao iguais, so mudando uma linha no ultimo else if de cada metodo abaixo.*/
    
    /* default */ static boolean dataInferior(int dia, int mes, int ano) {
    	
        GregorianCalendar Calendario = new GregorianCalendar();
        int dia_atual = Calendario.get(Calendar.DAY_OF_MONTH);
        int mes_atual = Calendario.get(Calendar.MONTH) + 1;
        int ano_atual = Calendario.get(Calendar.YEAR);

        // Apenas verificando se foi digitada uma data após a data atual do computador.
        if (ano > ano_atual)
            return false;
        else if (ano == ano_atual) {
            if (mes > mes_atual)
                return false;
            else if (mes == mes_atual && dia > dia_atual)
                return false;
        }
        
        return true;
    }
    
    /* default */ static boolean dataInferiorInferior(int dia, int mes, int ano) {
    	
        GregorianCalendar Calendario = new GregorianCalendar();
        int dia_atual = Calendario.get(Calendar.DAY_OF_MONTH);
        int mes_atual = Calendario.get(Calendar.MONTH) + 1;
        int ano_atual = Calendario.get(Calendar.YEAR);
        

        // Apenas verificando se foi digitada uma data após a data atual do computador.
        if (ano > ano_atual)
            return false;
        else if (ano == ano_atual) {
            if (mes > mes_atual)
                return false;
            else if (mes == mes_atual && dia >= dia_atual)
                return false;
        }
        
        return true;
    }
	/* Metodos auxiliares para data*/
}
