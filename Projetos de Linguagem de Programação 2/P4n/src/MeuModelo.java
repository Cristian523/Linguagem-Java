// @author cristian

import java.util.Comparator;
import javax.swing.table.*;

// Fiz essa interface a fim de praticar mesmo e de deixar as coisas um pouco mais encapsuladas.
public interface MeuModelo<M extends TableModel, R extends TableRowSorter<M>> {
    
    public M getModelo();
    public R getRowSorter(int numeroColunas);
    
    default Comparator<String> comparadorString() {
        Comparator<String> comp = new Comparator<>() {
            @Override
            public int compare(String str1, String str2) {
                str1 = str1.replaceAll(" ", "");
                str2 = str2.replaceAll(" ", "");
                return str1.compareToIgnoreCase(str2);
            }  
        };
        return comp;            
    }
    
   
    default Comparator<String> comparadorData() {
        Comparator<String> dataC = new Comparator<String>() {
            @Override
            public int compare(String data1, String data2) {
                // Pegando cada dia, mes e ano
                String dia1, mes1, ano1, dia2, mes2, ano2;
                int comparacao;
                
                // A comparação que eu faço abaixo é gambiarra? Bem, sim, mas pelo menos funciona...

                // Comparando anos
                ano1 = new String(new char[] {data1.charAt(6), data1.charAt(7), data1.charAt(8), data1.charAt(9)});
                ano2 = new String(new char[] {data2.charAt(6), data2.charAt(7), data2.charAt(8), data2.charAt(9)});
                comparacao = ano1.compareTo(ano2);
                if (comparacao != 0)
                    return comparacao;

                //Comparando meses
                mes1 = new String(new char[] {data1.charAt(3), data1.charAt(4)});         
                mes2 = new String(new char[] {data2.charAt(3), data2.charAt(4)});
                comparacao = mes1.compareTo(mes2);
                if (comparacao != 0)
                    return comparacao;

                // Comparando dias
                dia1 = new String(new char[] {data1.charAt(0), data1.charAt(1)});   
                dia2 = new String(new char[] {data2.charAt(0), data2.charAt(1)});
                comparacao = dia1.compareTo(dia2);
                return comparacao;
            }
        };
        return dataC;
    }
    
    default Comparator<String> comparadorNumero() {
        Comparator<String> comp = new Comparator<String>() {
            @Override
            public int compare(String numero1, String numero2) {
                int n1 = Integer.parseInt(numero1);
                int n2 = Integer.parseInt(numero2);
                return Integer.compare(n1, n2);
            }
        };
        return comp;
    }
    
    default void adicionarLinha(Object[] linha) {
        if (getModelo() instanceof DefaultTableModel) {
            DefaultTableModel modelo = (DefaultTableModel) getModelo();
            modelo.addRow(linha);
        }
        else
            throw new UnsupportedOperationException("Você precisa implementar este método já que não usou um DefaultTableModel");
    }
    
    
}
