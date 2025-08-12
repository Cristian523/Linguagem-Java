import java.util.Calendar;
import java.util.GregorianCalendar;

public class ValidaData {
	public static boolean isDataValida(int dia, int mes, int ano) {
	    if (isDia(dia) && isMes(mes) && isAno(ano)) {
            boolean ano_bissexto = false;
            if ((ano % 4 == 0 && ano % 100 != 0) || (ano % 400 == 0))
                ano_bissexto = true;

            switch(mes) {
                case 2:
                    if (ano_bissexto) {
                        if (dia > 29)
                            return false;
                    }
                    else {
                        if (dia > 28)
                            return false;
                    }
                    break;
                case 4: case 6: case 9: case 11:
                    if (dia > 30)
                        return false;
                    break;
                default:
                    break;
            }

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
        else
            return false;
	}
	
	public static boolean isDataValida(String dia, String mes, String ano) {
	    if (isDia(dia) && isMes(mes) && isAno(ano)) {
            int dia_numero, mes_numero, ano_numero;
            dia_numero = Integer.parseInt(dia);
            ano_numero = Integer.parseInt(ano);
            try {
                mes_numero = Integer.parseInt(mes);
            }
            catch(NumberFormatException e) {
                Meses aux = Meses.converter(mes);
                mes_numero = aux.numeroMes();
            }
    
            return isDataValida(dia_numero, mes_numero, ano_numero);
        }
        else
            return false;
	}
	
	
	public static boolean isDia(int dia) {
		if (dia >= 1 && dia <= 31)
			return true;
		else
			return false;
	}
	public static boolean isDia(String dia) {
		try {
			int dia_inteiro = Integer.parseInt(dia);
			if (dia_inteiro >= 1 && dia_inteiro <= 31)
				return true;
			else
				return false;
		}
		catch (NumberFormatException e) {
			return false;
		}
	}

    
	public static boolean isMes(int mes) {
		if (mes >= 1 && mes <= 12)
			return true;
		else
			return false;
	}
    
	public static boolean isMes(String mes) {
		int n = mes.length();
		boolean eh_Convertivel = true;
		if (n == 0)
            return false;
        
        for (int i = 0; i < n; i++) {
			if (!Character.isDigit(mes.charAt(i))) {
				eh_Convertivel = false;
				break;
			}		
		}
		
		int numero_mes;
		if (eh_Convertivel)
			numero_mes = Integer.parseInt(mes);
		else {
			Meses aux = Meses.converter(mes);
			if (aux == null)
				return false;
			numero_mes = aux.numeroMes();
		}
		
		if (numero_mes >= 1 && numero_mes <= 12)
			return true;
		else
			return false;
		
	}

    
	public static boolean isAno(int ano) {
	    GregorianCalendar Calendario = new GregorianCalendar();
        int ano_atual = Calendario.get(Calendar.YEAR);
        int diferenca = ano_atual - ano;
        if (diferenca >= 0 && diferenca <= 120)
            return true;
        else
            return false;
	}
	public static boolean isAno(String ano)  {
	    int ano_numero;
        try {
            ano_numero = Integer.parseInt(ano);
        }
        catch(NumberFormatException e) {
            return false;
        }

        GregorianCalendar Calendario = new GregorianCalendar();
        int ano_atual = Calendario.get(Calendar.YEAR);
        int diferenca = ano_atual - ano_numero;
        if (diferenca >= 0 && diferenca <= 120)
            return true;
        else
            return false;
	}

    // Teste apenas!
    /*
    public static void main(String[] args) {
        System.out.println(ValidaData.isDataValida(13, 04, 2006));              // true
        System.out.println(ValidaData.isDataValida(30, 02, 1992));              // false
        System.out.println(ValidaData.isDataValida("07", "Maio", "1977"));      // true
        System.out.println(ValidaData.isDataValida("31", "11", "2008"));        // false
        System.out.println(ValidaData.isDataValida("25", "04", "2025"));        // true
        System.out.println(ValidaData.isDataValida("29", "dezembro", "2030"));  // false
        System.out.println(ValidaData.isDataValida(31, 01, 2012));               // true
        System.out.println(ValidaData.isDataValida("26", "Abril", "2025"));     // true
    }
    */
}
