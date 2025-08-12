package lp2g06.biblioteca;

public class ValidaCPF {
    
    public static boolean isCPF(String str) {
        if (!eh_Convertivel(str)) {
            return false;
        }

        String CPF = str.replaceAll("[./-]", "");

        if (CPF.equals("00000000000") || CPF.equals("11111111111") ||
            CPF.equals("22222222222") || CPF.equals("33333333333") ||
            CPF.equals("44444444444") || CPF.equals("55555555555") ||
            CPF.equals("66666666666") || CPF.equals("77777777777") ||
            CPF.equals("88888888888") || CPF.equals("99999999999") )
            return false;

        char dig10, dig11;
        int soma, peso, num, r;
        // Calculo do 1° digito
        soma = 0;
        peso = 10;
        
        for (int i = 0; i < 9; i++) {
            num = (int)(CPF.charAt(i) - 48);
            soma += num * peso;
            peso--;
        }
        r = 11 - (soma % 11);
        if ((r == 10) || (r == 11))
            dig10 = '0';
        else
            dig10 = (char)(r + 48);

        // Calculo do 2° digito

        soma = 0; peso = 11;
        for (int i = 0; i < 10; i++) {
            num = (int)(CPF.charAt(i) - 48);
            soma += num * peso;
            peso--;
        }

        r = 11 - (soma % 11);
        if ((r == 10) || (r == 11))
            dig11 = '0';
        else
            dig11 = (char)(r + 48);

        // Verificar os digitos

        if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
            return true;
        else 
            return false;
        
    }

    public static long toLong(String str) {
        str = str.replaceAll("[./-]", "");
        long CPF;
        try {
            CPF = Long.parseLong(str);
        }
        catch (NumberFormatException e) {
            throw new NumberFormatException("Erro: a string digitada não é convertível em Long.");
        }
        return CPF;
    }

    

    private static boolean eh_Convertivel(String str) {
        int n = str.length();
        char c;
        if (n == 14) {
            for (int i = 0; i < n; i++) {
                c = str.charAt(i);
                switch (i) {
                    case 3: case 7:
                        if (c != '.')
                            return false;
                        break;    
                    case 11:
                        if (c != '/' && c != '-')
                            return false;
                        break;    
                    default:
                        if (!Character.isDigit(c))
                            return false;
                        break;  
                }
            }
        }
        else if (n == 11) {
            for (int i = 0; i < n; i++) {
                if (!Character.isDigit(str.charAt(i)))
                    return false;
            }
        }
        else
            return false;
    
        return true;
    }
    
    public static String toString(long numCPF) {
    	String cpf = Long.toString(numCPF);
    	int n = cpf.length();
    	while (n < 11) {
    		cpf = "0" + cpf;
    		n++;
    	}
    	return (cpf);
    }
    
}
