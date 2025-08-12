// @author cristian

import lp2g06.biblioteca.*;
import java.io.*;
import java.util.Comparator;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.table.*;


public class P4nX extends javax.swing.JFrame {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(P4nX.class.getName());
    
    // Campos criados diretamente por mim
    private File fileHistoricoUsuario = null;
    private File fileHistoricoLivro = null;
    private String nomeHistoricoUsuario = null;
    private String nomeHistoricoLivro = null;
    private Biblioteca minhaBiblioteca;
    private final JFileChooser selecionaArquivos = new JFileChooser(new File(System.getProperty("user.dir")));
    
    /* Meus modelos para JTable*/
    private MeuModelo<DefaultTableModel, TableRowSorter<DefaultTableModel>> modeloUsuario = new MeuModelo<>() {
        private DefaultTableModel modelo = new DefaultTableModel(new String[] {"Nome", "Data de Nascimento", "CPF", "Endereco"}, 0) {
            @Override
            public boolean isCellEditable(int row, int count) {   // Não permitindo que o usuário mexa nas posições
                return false;
            }
        };

        @Override
        public DefaultTableModel getModelo() {
            return modelo;       
        }
        
        @Override
        public TableRowSorter<DefaultTableModel> getRowSorter(int numeroColunas) {
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
            
            sorter.setComparator(1, this.comparadorData());   // Aqui compararei datas
            for (int i = 0; i < numeroColunas; i++) {
                if (i != 1) 
                    sorter.setComparator(i, this.comparadorString());
            }
            
            return sorter;
        }
    
    };
    
    private MeuModelo<DefaultTableModel, TableRowSorter<DefaultTableModel>> modeloLivro = new MeuModelo<>() {
        private DefaultTableModel modelo = new DefaultTableModel(new String[] {"Codigo", "Titulo", "Categoria", "Copias", "Emprestimos"}, 0) {
            @Override
            public boolean isCellEditable(int row, int count) {   // Não permitindo que o usuário mexa nas posições
                return false;
            }
        };
        
        @Override
        public DefaultTableModel getModelo() {
            return modelo;
        }
        
        @Override
        public TableRowSorter<DefaultTableModel> getRowSorter(int numeroColunas) {
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
            sorter.setComparator(0, this.comparadorNumero());
            sorter.setComparator(1, this.comparadorString());
            sorter.setComparator(2, this.comparadorString());
            sorter.setComparator(3, this.comparadorNumero());
            sorter.setComparator(4, this.comparadorNumero());
            return sorter;
        } 
        
        
    };
    /* Meus modelos para JTable*/
    
    
    /* Meus metodos auxiliares */
    private void configurarTabela(JTable tabela, MeuModelo<DefaultTableModel, TableRowSorter<DefaultTableModel>> modelo) {
        tabela.setRowSelectionAllowed(false);
        tabela.setColumnSelectionAllowed(false);
        tabela.setCellSelectionEnabled(false);
        tabela.getTableHeader().setReorderingAllowed(false);
        tabela.setRowSorter(modelo.getRowSorter(tabela.getColumnCount()));
        
    }
    
    private void adicionarUsuarioATabela(Usuario user) {
        modeloUsuario.adicionarLinha(new Object[] {user.getNome() + " " + user.getSobreNome(), ValidaData.toString(user.getDataNasc()), ValidaCPF.toString(user.getNumCPF()), user.getEndereco()} );
        
    }
    
    private void adicionarLivroATabela(Livro book) {
        modeloLivro.adicionarLinha(new Object[] {Integer.toString(book.getCodigoLivro()), book.getTituloLivro(), book.getCategoria().toString(), 
                                    Integer.toString(book.getCopias()), Integer.toString(book.getEmprestados())} );  // Eu estava tendo problemas aqui, por isso esses toStrings()
    }
    
    private int obterLinhaCorrespondente(JTable tabela, int coluna, String elemento) {
        int n = tabela.getRowCount();
        for (int i = 0; i < n; i++) {
            if (elemento.equals((String) tabela.getValueAt(i, coluna)))
                return i;
        }
        
        return -1;
    }
    
    private void carregarTabelaUsuario() {
        if (tabelaUsuarios != null) {   // então antes de carregar a nova tabela vou remover as antigas linhas
            int n = tabelaUsuarios.getRowCount();
            for (int i = n - 1; i >= 0; i--)
                modeloUsuario.getModelo().removeRow(i);
            configurarTabela(tabelaUsuarios, modeloUsuario);
        }
        
        ArrayList<Long> CPFs = minhaBiblioteca.getCPFs();
        if (CPFs != null) {
            for (Long i: CPFs) {
                try {
                    adicionarUsuarioATabela(minhaBiblioteca.getUsuario(i));
                }
                catch(UsuarioNaoCadastradoEx e){} // Nunca entrara dentro desse catch nesse caso
            }
        }
    }
    
    private void carregarTabelaLivro() {
        if (tabelaLivros != null) {    // então antes de carregar a nova tabela vou remover as antigas linhas
            int n = tabelaLivros.getRowCount();
            for (int i = n - 1; i >= 0; i--)
                modeloLivro.getModelo().removeRow(i);
            configurarTabela(tabelaLivros, modeloLivro);       
        }
        
        ArrayList<Integer> codigos = minhaBiblioteca.getCodigos();
        if (codigos != null) {
            for (Integer i: codigos) {
                try {
                    adicionarLivroATabela(minhaBiblioteca.getLivro(i));
                }
                catch(LivroNaoCadastradoEx e){}  // Nunca entrara dentro desse catch nesse caso
            }
        }
    }
    
    private void salvarAutomaticamente() {
        String str = "\nO salvamento automático aqui falhou. Tente salvar os seus históricos no menu manutenção.";
        if (fileHistoricoUsuario == null || fileHistoricoLivro == null) {
            
            JOptionPane.showMessageDialog(this, "Os dois arquivos ainda não foram carregados." + str , "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
        else {
            try {
                minhaBiblioteca.salvaArqUsu(fileHistoricoUsuario);
                minhaBiblioteca.salvaArqLiv(fileHistoricoLivro);
                JOptionPane.showMessageDialog(this, "Os arquivo  " + nomeHistoricoUsuario + "  e  " + nomeHistoricoLivro + "  foram salvos com sucesso!");
            }
            catch (IOException e) {
                JOptionPane.showMessageDialog(this, e + str , "Erro", JOptionPane.ERROR_MESSAGE);
            }  
        }
    }
    
    private boolean exibirDialogoMulta(Usuario user, String mensagem) {
        int opcao = JOptionPane.showConfirmDialog(this, mensagem + "\nEle pagou a multa?", "Observação", JOptionPane.YES_NO_OPTION);
        if (opcao == JOptionPane.YES_OPTION) {
            user.setSituacaoMultaUsuario("Regular");
            JOptionPane.showMessageDialog(this, "Multa paga com sucesso!");
            return true;
        }
        else {
            user.setSituacaoMultaUsuario("Multa pendente!");
            return false;
        }
    }
    
    private void controlarVisibilidadeCadastro(boolean opcao1, boolean opcao2) {
        // Para o RadioBotton1
        cadastroLabel2.setVisible(opcao1);     cadastroTextField1.setVisible(opcao1);
        cadastroLabel3.setVisible(opcao1);     cadastroTextField2.setVisible(opcao1);
        cadastroLabel4.setVisible(opcao1);     cadastroTextField3.setVisible(opcao1);
        cadastroLabel5.setVisible(opcao1);     cadastroTextField4.setVisible(opcao1);
        cadastroLabel6.setVisible(opcao1);     cadastroTextField5.setVisible(opcao1);     
        cadastroLabel7.setVisible(opcao1);     cadastroTextField6.setVisible(opcao1);
        cadastroLabel8.setVisible(opcao1);     cadastroTextField11.setVisible(opcao1);
        cadastroLabel13.setVisible(opcao1);
        
        
        // Para o RadioBotton2
        cadastroLabel9.setVisible(opcao2);     cadastroTextField7.setVisible(opcao2);
        cadastroLabel10.setVisible(opcao2);    cadastroTextField8.setVisible(opcao2);
        cadastroLabel11.setVisible(opcao2);    cadastroTextField9.setVisible(opcao2);
        cadastroLabel12.setVisible(opcao2);    cadastroTextField10.setVisible(opcao2);    
    }
    /* Meus metodos auxiliares */
    
    
    
    
    
    
    
    
    public P4nX() {
        initComponents();
        areaExibicao2.setEditable(false);   // Esqueci de alterar inicialmente
    }
    
    // OBSERVAÇÂO: não estranhe o método abaixo, já que foi o NetBeans que gerou isso tudo (digo mais pelo tamanho dele e por usar as classes de pacotes sem usar import.
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        inicio = new javax.swing.JPanel();
        inicioLabel2 = new javax.swing.JLabel();
        inicioLabel1 = new javax.swing.JLabel();
        inicioBotao1 = new javax.swing.JRadioButton();
        inicioBotao2 = new javax.swing.JRadioButton();
        inicioBotao3 = new javax.swing.JButton();
        inicioLabel3 = new javax.swing.JLabel();
        inicioLabel4 = new javax.swing.JLabel();
        inicioBotao4 = new javax.swing.JButton();
        inicioBotao5 = new javax.swing.JButton();
        inicioLabel5 = new javax.swing.JLabel();
        inicioLabel6 = new javax.swing.JLabel();
        menuPrincipal = new javax.swing.JPanel();
        menuBotao1 = new javax.swing.JButton();
        menuBotao2 = new javax.swing.JButton();
        menuBotao3 = new javax.swing.JButton();
        menuBotao4 = new javax.swing.JButton();
        menuLabel1 = new javax.swing.JLabel();
        menuLabel2 = new javax.swing.JLabel();
        menuManutencao = new javax.swing.JPanel();
        manutencaoLabel1 = new javax.swing.JLabel();
        manutencaoBotao1 = new javax.swing.JRadioButton();
        manutencaoBotao2 = new javax.swing.JRadioButton();
        manutencaoBotao3 = new javax.swing.JRadioButton();
        manutencaoBotao4 = new javax.swing.JRadioButton();
        manutencaoLabel2 = new javax.swing.JLabel();
        manutencaoBotao5 = new javax.swing.JButton();
        manutencaoBotao6 = new javax.swing.JButton();
        manutencaoBotao7 = new javax.swing.JButton();
        menuCadastro = new javax.swing.JPanel();
        cadastroBotao1 = new javax.swing.JButton();
        cadastroBotao2 = new javax.swing.JRadioButton();
        cadastroLabel1 = new javax.swing.JLabel();
        cadastroBotao3 = new javax.swing.JRadioButton();
        cadastroBotao4 = new javax.swing.JRadioButton();
        cadastroBotao5 = new javax.swing.JButton();
        cadastroLabel2 = new javax.swing.JLabel();
        cadastroLabel3 = new javax.swing.JLabel();
        cadastroLabel4 = new javax.swing.JLabel();
        cadastroLabel5 = new javax.swing.JLabel();
        cadastroLabel6 = new javax.swing.JLabel();
        cadastroLabel7 = new javax.swing.JLabel();
        cadastroLabel8 = new javax.swing.JLabel();
        cadastroLabel9 = new javax.swing.JLabel();
        cadastroLabel10 = new javax.swing.JLabel();
        cadastroLabel11 = new javax.swing.JLabel();
        cadastroLabel12 = new javax.swing.JLabel();
        cadastroTextField1 = new javax.swing.JTextField();
        cadastroTextField2 = new javax.swing.JTextField();
        cadastroTextField3 = new javax.swing.JTextField();
        cadastroTextField4 = new javax.swing.JTextField();
        cadastroTextField5 = new javax.swing.JTextField();
        cadastroTextField6 = new javax.swing.JTextField();
        cadastroTextField7 = new javax.swing.JTextField();
        cadastroTextField8 = new javax.swing.JTextField();
        cadastroTextField9 = new javax.swing.JTextField();
        cadastroTextField10 = new javax.swing.JTextField();
        cadastroLabel13 = new javax.swing.JLabel();
        cadastroTextField11 = new javax.swing.JTextField();
        menuEmprestimo = new javax.swing.JPanel();
        emprestimoBotao1 = new javax.swing.JButton();
        emprestimoBotao2 = new javax.swing.JRadioButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        areaExibicao2 = new javax.swing.JTextArea();
        emprestimoLabel1 = new javax.swing.JLabel();
        emprestimoBotao3 = new javax.swing.JRadioButton();
        emprestimoBotao4 = new javax.swing.JRadioButton();
        emprestimoBotao5 = new javax.swing.JButton();
        emprestimoLabel2 = new javax.swing.JLabel();
        emprestimoLabel3 = new javax.swing.JLabel();
        emprestimoLabel4 = new javax.swing.JLabel();
        emprestimoLabel5 = new javax.swing.JLabel();
        emprestimoLabel6 = new javax.swing.JLabel();
        emprestimoLabel7 = new javax.swing.JLabel();
        emprestimoTextField1 = new javax.swing.JTextField();
        emprestimoTextField2 = new javax.swing.JTextField();
        emprestimoTextField3 = new javax.swing.JTextField();
        emprestimoTextField4 = new javax.swing.JTextField();
        emprestimoTextField5 = new javax.swing.JTextField();
        emprestimoBotao6 = new javax.swing.JRadioButton();
        menuRelatorio = new javax.swing.JPanel();
        painelTabelaUsuarios = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaUsuarios = new javax.swing.JTable();
        painelTabelaLivros = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelaLivros = new javax.swing.JTable();
        relatorioBotao1 = new javax.swing.JButton();
        relatorioBotao2 = new javax.swing.JButton();
        relatorioBotao3 = new javax.swing.JButton();
        relatorioLabel1 = new javax.swing.JLabel();
        relatorioBotao4 = new javax.swing.JButton();
        painelDetalhes = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        areaExibicao = new javax.swing.JTextArea();
        detalhesBotao1 = new javax.swing.JButton();
        detalhesBotao2 = new javax.swing.JRadioButton();
        detalhesBotao3 = new javax.swing.JRadioButton();
        detalhesLabel = new javax.swing.JLabel();
        detalhesTextField = new javax.swing.JTextField();
        detalhesBotao4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("biblioteca");

        inicioLabel2.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        inicioLabel2.setText("Para o correto inicializamento da biblioteca, tem-se as seguintes opções: ");

        inicioLabel1.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        inicioLabel1.setText("Ola Bibliotecário!");

        inicioBotao1.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        inicioBotao1.setText("Carregar históricos existentes");
        inicioBotao1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inicioBotao1ActionPerformed(evt);
            }
        });

        inicioBotao2.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        inicioBotao2.setText("Deixar biblioteca zerada");
        inicioBotao2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inicioBotao2ActionPerformed(evt);
            }
        });

        inicioBotao3.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        inicioBotao3.setText("prosseguir");
        inicioBotao3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inicioBotao3ActionPerformed(evt);
            }
        });

        inicioLabel3.setFont(new java.awt.Font("Inter", 0, 17)); // NOI18N
        inicioLabel3.setText("Histórico usuários:");
        inicioLabel3.setVisible(false);

        inicioLabel4.setFont(new java.awt.Font("Inter", 0, 17)); // NOI18N
        inicioLabel4.setText("Histórico livros: ");
        inicioLabel4.setVisible(false);

        inicioBotao4.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        inicioBotao4.setText("abrir arquivo");
        inicioBotao4.setVisible(false);
        inicioBotao4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inicioBotao4ActionPerformed(evt);
            }
        });

        inicioBotao5.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        inicioBotao5.setText("abrir arquivo");
        inicioBotao5.setVisible(false);
        inicioBotao5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inicioBotao5ActionPerformed(evt);
            }
        });

        inicioLabel5.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        inicioLabel6.setVisible(false);

        inicioLabel6.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        inicioLabel6.setVisible(false);

        javax.swing.GroupLayout inicioLayout = new javax.swing.GroupLayout(inicio);
        inicio.setLayout(inicioLayout);
        inicioLayout.setHorizontalGroup(
            inicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inicioLayout.createSequentialGroup()
                .addGroup(inicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(inicioLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(inicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(inicioLabel1)
                            .addComponent(inicioLabel2)))
                    .addGroup(inicioLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(inicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(inicioBotao1)
                            .addGroup(inicioLayout.createSequentialGroup()
                                .addComponent(inicioLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(inicioBotao4))
                            .addComponent(inicioBotao2)
                            .addGroup(inicioLayout.createSequentialGroup()
                                .addComponent(inicioLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(inicioBotao5))
                            .addComponent(inicioLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)
                            .addComponent(inicioLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(32, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, inicioLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(inicioBotao3)
                .addGap(32, 32, 32))
        );
        inicioLayout.setVerticalGroup(
            inicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inicioLayout.createSequentialGroup()
                .addComponent(inicioLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inicioLabel2)
                .addGap(18, 18, 18)
                .addComponent(inicioBotao1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(inicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inicioLabel3)
                    .addComponent(inicioBotao4))
                .addGap(1, 1, 1)
                .addComponent(inicioLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(inicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inicioLabel4)
                    .addComponent(inicioBotao5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inicioLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(inicioBotao2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                .addComponent(inicioBotao3)
                .addGap(24, 24, 24))
        );

        menuPrincipal.setVisible(false);

        menuBotao1.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        menuBotao1.setText("Manutenção");
        menuBotao1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBotao1ActionPerformed(evt);
            }
        });

        menuBotao2.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        menuBotao2.setText("Cadastro");
        menuBotao2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBotao2ActionPerformed(evt);
            }
        });

        menuBotao3.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        menuBotao3.setText("Empréstimo");
        menuBotao3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBotao3ActionPerformed(evt);
            }
        });

        menuBotao4.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        menuBotao4.setText("Relatório");
        menuBotao4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBotao4ActionPerformed(evt);
            }
        });

        menuLabel1.setFont(new java.awt.Font("Inter SemiBold", 2, 20)); // NOI18N
        menuLabel1.setText("           MENU PRINCIPAL");

        menuLabel2.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        menuLabel2.setText("  Aqui voce tem as seguintes opções");

        javax.swing.GroupLayout menuPrincipalLayout = new javax.swing.GroupLayout(menuPrincipal);
        menuPrincipal.setLayout(menuPrincipalLayout);
        menuPrincipalLayout.setHorizontalGroup(
            menuPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuPrincipalLayout.createSequentialGroup()
                .addGroup(menuPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(menuPrincipalLayout.createSequentialGroup()
                        .addGap(151, 151, 151)
                        .addGroup(menuPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(menuBotao2, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(menuBotao1, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(menuBotao3, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(menuBotao4, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(menuPrincipalLayout.createSequentialGroup()
                        .addGap(152, 152, 152)
                        .addComponent(menuLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(menuPrincipalLayout.createSequentialGroup()
                        .addGap(122, 122, 122)
                        .addComponent(menuLabel2)))
                .addContainerGap(129, Short.MAX_VALUE))
        );
        menuPrincipalLayout.setVerticalGroup(
            menuPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuPrincipalLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(menuLabel1)
                .addGap(26, 26, 26)
                .addComponent(menuLabel2)
                .addGap(18, 18, 18)
                .addComponent(menuBotao1)
                .addGap(34, 34, 34)
                .addComponent(menuBotao2)
                .addGap(35, 35, 35)
                .addComponent(menuBotao3)
                .addGap(34, 34, 34)
                .addComponent(menuBotao4)
                .addContainerGap(47, Short.MAX_VALUE))
        );

        menuManutencao.setVisible(false);

        manutencaoLabel1.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        manutencaoLabel1.setText("Aqui em manutenção voce tem as seguintes opções:");

        manutencaoBotao1.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        manutencaoBotao1.setText("Salvar histórico de usuários");
        manutencaoBotao1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manutencaoBotao1ActionPerformed(evt);
            }
        });

        manutencaoBotao2.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        manutencaoBotao2.setText("Salvar histórico de livros");
        manutencaoBotao2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manutencaoBotao2ActionPerformed(evt);
            }
        });

        manutencaoBotao3.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        manutencaoBotao3.setText("Ler histórico de usuários");
        manutencaoBotao3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manutencaoBotao3ActionPerformed(evt);
            }
        });

        manutencaoBotao4.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        manutencaoBotao4.setText("Ler histórico de livros");
        manutencaoBotao4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manutencaoBotao4ActionPerformed(evt);
            }
        });

        manutencaoLabel2.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N

        manutencaoBotao5.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        manutencaoBotao5.setText("salvar/carregar");
        manutencaoBotao5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manutencaoBotao5ActionPerformed(evt);
            }
        });

        manutencaoBotao6.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        manutencaoBotao6.setText("voltar");
        manutencaoBotao6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manutencaoBotao6ActionPerformed(evt);
            }
        });

        manutencaoBotao7.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        manutencaoBotao7.setText("abrir arquivo");
        manutencaoBotao7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manutencaoBotao7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout menuManutencaoLayout = new javax.swing.GroupLayout(menuManutencao);
        menuManutencao.setLayout(menuManutencaoLayout);
        menuManutencaoLayout.setHorizontalGroup(
            menuManutencaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuManutencaoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(menuManutencaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(manutencaoLabel1)
                    .addComponent(manutencaoBotao1)
                    .addComponent(manutencaoBotao4)
                    .addComponent(manutencaoBotao2)
                    .addComponent(manutencaoBotao3))
                .addContainerGap(118, Short.MAX_VALUE))
            .addGroup(menuManutencaoLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(menuManutencaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(menuManutencaoLayout.createSequentialGroup()
                        .addComponent(manutencaoBotao7)
                        .addGap(6, 421, Short.MAX_VALUE))
                    .addGroup(menuManutencaoLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(manutencaoBotao6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(manutencaoBotao5))
                    .addComponent(manutencaoLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );
        menuManutencaoLayout.setVerticalGroup(
            menuManutencaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuManutencaoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(manutencaoLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(manutencaoBotao1)
                .addGap(12, 12, 12)
                .addComponent(manutencaoBotao2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(manutencaoBotao3)
                .addGap(12, 12, 12)
                .addComponent(manutencaoBotao4)
                .addGap(18, 18, 18)
                .addComponent(manutencaoBotao7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(manutencaoLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
                .addGroup(menuManutencaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(manutencaoBotao6)
                    .addComponent(manutencaoBotao5))
                .addGap(25, 25, 25))
        );

        menuCadastro.setVisible(false);

        cadastroBotao1.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        cadastroBotao1.setText("voltar");
        cadastroBotao1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cadastroBotao1ActionPerformed(evt);
            }
        });

        cadastroBotao2.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        cadastroBotao2.setText("Salvar automaticamente ao sair");
        cadastroBotao2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cadastroBotao2ActionPerformed(evt);
            }
        });
        cadastroBotao2.setEnabled(false);

        cadastroLabel1.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        cadastroLabel1.setText("Aqui em cadastro voce tem as seguintes opções:");

        cadastroBotao3.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        cadastroBotao3.setText("Cadastrar um usuário");
        cadastroBotao3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cadastroBotao3ActionPerformed(evt);
            }
        });

        cadastroBotao4.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        cadastroBotao4.setText("Cadastrar um livro");
        cadastroBotao4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cadastroBotao4ActionPerformed(evt);
            }
        });

        cadastroBotao5.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        cadastroBotao5.setText("cadastrar");
        cadastroBotao5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cadastroBotao5ActionPerformed(evt);
            }
        });

        cadastroLabel2.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        cadastroLabel2.setText("Nome:");
        cadastroLabel2.setVisible(false);

        cadastroLabel3.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        cadastroLabel3.setText("Sobrenome:");
        cadastroLabel3.setVisible(false);

        cadastroLabel4.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        cadastroLabel4.setText("Data de Nascimento:");
        cadastroLabel4.setVisible(false);

        cadastroLabel5.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        cadastroLabel5.setText("dia:");
        cadastroLabel5.setVisible(false);

        cadastroLabel6.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        cadastroLabel6.setText("mês:");
        cadastroLabel6.setVisible(false);

        cadastroLabel7.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        cadastroLabel7.setText("ano:");
        cadastroLabel7.setVisible(false);

        cadastroLabel8.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        cadastroLabel8.setText("CPF:");
        cadastroLabel8.setVisible(false);

        cadastroLabel9.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        cadastroLabel9.setText("Código: ");
        cadastroLabel9.setVisible(false);

        cadastroLabel10.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        cadastroLabel10.setText("Título: ");
        cadastroLabel10.setVisible(false);

        cadastroLabel11.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        cadastroLabel11.setText("Categoria: ");
        cadastroLabel11.setVisible(false);

        cadastroLabel12.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        cadastroLabel12.setText("Cópias:");
        cadastroLabel12.setVisible(false);

        cadastroTextField1.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        cadastroTextField1.setVisible(false);

        cadastroTextField2.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        cadastroTextField2.setVisible(false);

        cadastroTextField3.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        cadastroTextField3.setVisible(false);

        cadastroTextField4.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        cadastroTextField4.setVisible(false);

        cadastroTextField5.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        cadastroTextField5.setVisible(false);

        cadastroTextField6.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        cadastroTextField6.setVisible(false);

        cadastroTextField7.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        cadastroTextField7.setVisible(false);

        cadastroTextField8.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        cadastroTextField8.setVisible(false);

        cadastroTextField9.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        cadastroTextField9.setVisible(false);

        cadastroTextField10.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        cadastroTextField10.setVisible(false);

        cadastroLabel13.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        cadastroLabel13.setText("Endereço:");
        cadastroLabel13.setVisible(false);

        cadastroTextField11.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        cadastroTextField11.setVisible(false);
        cadastroTextField11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cadastroTextField11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout menuCadastroLayout = new javax.swing.GroupLayout(menuCadastro);
        menuCadastro.setLayout(menuCadastroLayout);
        menuCadastroLayout.setHorizontalGroup(
            menuCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuCadastroLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(menuCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(menuCadastroLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(cadastroBotao1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cadastroBotao5)
                        .addGap(19, 19, 19))
                    .addGroup(menuCadastroLayout.createSequentialGroup()
                        .addGroup(menuCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(menuCadastroLayout.createSequentialGroup()
                                .addComponent(cadastroBotao3)
                                .addGap(159, 159, 159)
                                .addComponent(cadastroTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cadastroLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cadastroTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cadastroLabel1)
                            .addGroup(menuCadastroLayout.createSequentialGroup()
                                .addComponent(cadastroLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cadastroTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(menuCadastroLayout.createSequentialGroup()
                                .addComponent(cadastroLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cadastroTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(24, 24, 24)
                                .addComponent(cadastroLabel3)
                                .addGap(6, 6, 6)
                                .addComponent(cadastroTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(menuCadastroLayout.createSequentialGroup()
                                .addComponent(cadastroLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cadastroTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(166, 166, 166)
                                .addComponent(cadastroLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cadastroTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(menuCadastroLayout.createSequentialGroup()
                                .addComponent(cadastroLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cadastroLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cadastroTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cadastroLabel6)))
                        .addContainerGap(54, Short.MAX_VALUE))
                    .addGroup(menuCadastroLayout.createSequentialGroup()
                        .addComponent(cadastroLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cadastroTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cadastroLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cadastroTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44))
                    .addGroup(menuCadastroLayout.createSequentialGroup()
                        .addGroup(menuCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cadastroBotao2)
                            .addComponent(cadastroBotao4)
                            .addGroup(menuCadastroLayout.createSequentialGroup()
                                .addComponent(cadastroLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cadastroTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 548, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        menuCadastroLayout.setVerticalGroup(
            menuCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuCadastroLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cadastroLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cadastroBotao3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(menuCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cadastroLabel2)
                    .addComponent(cadastroLabel3)
                    .addComponent(cadastroTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cadastroTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(menuCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cadastroLabel4)
                    .addComponent(cadastroLabel5)
                    .addComponent(cadastroLabel6)
                    .addComponent(cadastroLabel7)
                    .addComponent(cadastroTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cadastroTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cadastroTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(menuCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cadastroLabel8)
                    .addComponent(cadastroTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addGroup(menuCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cadastroTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cadastroLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cadastroBotao4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(menuCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(menuCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cadastroTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cadastroLabel9))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cadastroLabel12)
                        .addComponent(cadastroTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(menuCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(menuCadastroLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(menuCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cadastroLabel11)
                            .addComponent(cadastroTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(menuCadastroLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(menuCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cadastroLabel10)
                            .addComponent(cadastroTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(cadastroBotao2)
                .addGap(35, 35, 35)
                .addGroup(menuCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cadastroBotao5)
                    .addComponent(cadastroBotao1))
                .addGap(13, 13, 13))
        );

        menuEmprestimo.setVisible(false);

        emprestimoBotao1.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        emprestimoBotao1.setText("voltar");
        emprestimoBotao1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emprestimoBotao1ActionPerformed(evt);
            }
        });

        emprestimoBotao2.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        emprestimoBotao2.setText("Salvar automaticamente ao sair");
        emprestimoBotao2.setEnabled(false);

        jScrollPane4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        areaExibicao2.setBackground(new java.awt.Color(204, 204, 255));
        areaExibicao2.setColumns(20);
        areaExibicao2.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        areaExibicao2.setRows(5);
        jScrollPane4.setViewportView(areaExibicao2);
        areaExibicao.setEditable(false);

        emprestimoLabel1.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        emprestimoLabel1.setText("Aqui em empréstimo suas opções são:");

        emprestimoBotao3.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        emprestimoBotao3.setText("Fazer um empréstimo");
        emprestimoBotao3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emprestimoBotao3ActionPerformed(evt);
            }
        });

        emprestimoBotao4.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        emprestimoBotao4.setText("Fazer uma devolução");
        emprestimoBotao4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emprestimoBotao4ActionPerformed(evt);
            }
        });

        emprestimoBotao5.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        emprestimoBotao5.setText("emprestar/devolver");
        emprestimoBotao5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emprestimoBotao5ActionPerformed(evt);
            }
        });

        emprestimoLabel2.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        emprestimoLabel2.setText("CPF: ");
        emprestimoLabel2.setVisible(false);

        emprestimoLabel3.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        emprestimoLabel3.setText("    Código: ");
        emprestimoLabel3.setVisible(false);

        emprestimoLabel4.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        emprestimoLabel4.setText("Data Limite: ");
        emprestimoLabel4.setVisible(false);

        emprestimoLabel5.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        emprestimoLabel5.setText("dia: ");
        emprestimoLabel5.setVisible(false);

        emprestimoLabel6.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        emprestimoLabel6.setText("mes: ");
        emprestimoLabel6.setVisible(false);

        emprestimoLabel7.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        emprestimoLabel7.setText("ano: ");
        emprestimoLabel7.setVisible(false);

        emprestimoTextField1.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        emprestimoTextField1.setVisible(false);

        emprestimoTextField2.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        emprestimoTextField2.setVisible(false);

        emprestimoTextField3.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        emprestimoTextField3.setVisible(false);
        emprestimoTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emprestimoTextField3ActionPerformed(evt);
            }
        });

        emprestimoTextField4.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        emprestimoTextField4.setVisible(false);

        emprestimoTextField5.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        emprestimoTextField5.setVisible(false);

        emprestimoBotao6.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        emprestimoBotao6.setText("Desabilitar verificação de Data Limite ser posterior à data atual");
        emprestimoBotao6.setVisible(false);

        javax.swing.GroupLayout menuEmprestimoLayout = new javax.swing.GroupLayout(menuEmprestimo);
        menuEmprestimo.setLayout(menuEmprestimoLayout);
        menuEmprestimoLayout.setHorizontalGroup(
            menuEmprestimoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuEmprestimoLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(menuEmprestimoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(menuEmprestimoLayout.createSequentialGroup()
                        .addGroup(menuEmprestimoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(menuEmprestimoLayout.createSequentialGroup()
                                .addComponent(emprestimoBotao1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(emprestimoBotao5))
                            .addGroup(menuEmprestimoLayout.createSequentialGroup()
                                .addGroup(menuEmprestimoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(emprestimoBotao4)
                                    .addComponent(emprestimoBotao2)
                                    .addComponent(emprestimoLabel1)
                                    .addComponent(emprestimoBotao3))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(18, 18, 18))
                    .addGroup(menuEmprestimoLayout.createSequentialGroup()
                        .addGroup(menuEmprestimoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(menuEmprestimoLayout.createSequentialGroup()
                                .addComponent(emprestimoLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(emprestimoTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(emprestimoLabel3))
                            .addGroup(menuEmprestimoLayout.createSequentialGroup()
                                .addComponent(emprestimoLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(emprestimoLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(emprestimoTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(emprestimoLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(emprestimoTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(emprestimoLabel7)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(menuEmprestimoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(emprestimoTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(emprestimoTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(menuEmprestimoLayout.createSequentialGroup()
                        .addComponent(emprestimoBotao6)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(menuEmprestimoLayout.createSequentialGroup()
                .addGap(166, 166, 166)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 198, Short.MAX_VALUE))
        );
        menuEmprestimoLayout.setVerticalGroup(
            menuEmprestimoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuEmprestimoLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(emprestimoLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addComponent(emprestimoBotao3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(emprestimoBotao6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(emprestimoBotao4)
                .addGap(18, 18, 18)
                .addGroup(menuEmprestimoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emprestimoLabel2)
                    .addComponent(emprestimoTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(emprestimoLabel3)
                    .addComponent(emprestimoTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(menuEmprestimoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emprestimoLabel4)
                    .addComponent(emprestimoLabel5)
                    .addComponent(emprestimoTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(emprestimoLabel6)
                    .addComponent(emprestimoTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(emprestimoLabel7)
                    .addComponent(emprestimoTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(emprestimoBotao2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(menuEmprestimoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(emprestimoBotao1)
                    .addComponent(emprestimoBotao5))
                .addGap(17, 17, 17))
        );

        menuRelatorio.setVisible(false);

        tabelaUsuarios.setModel(modeloUsuario.getModelo());
        jScrollPane1.setViewportView(tabelaUsuarios);
        configurarTabela(tabelaUsuarios, modeloUsuario);

        javax.swing.GroupLayout painelTabelaUsuariosLayout = new javax.swing.GroupLayout(painelTabelaUsuarios);
        painelTabelaUsuarios.setLayout(painelTabelaUsuariosLayout);
        painelTabelaUsuariosLayout.setHorizontalGroup(
            painelTabelaUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelTabelaUsuariosLayout.createSequentialGroup()
                .addGap(0, 53, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        painelTabelaUsuariosLayout.setVerticalGroup(
            painelTabelaUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelTabelaUsuariosLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        painelTabelaLivros.setVisible(false);

        tabelaLivros.setModel(modeloLivro.getModelo());
        jScrollPane2.setViewportView(tabelaLivros);
        configurarTabela(tabelaLivros, modeloLivro);

        javax.swing.GroupLayout painelTabelaLivrosLayout = new javax.swing.GroupLayout(painelTabelaLivros);
        painelTabelaLivros.setLayout(painelTabelaLivrosLayout);
        painelTabelaLivrosLayout.setHorizontalGroup(
            painelTabelaLivrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelTabelaLivrosLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(57, Short.MAX_VALUE))
        );
        painelTabelaLivrosLayout.setVerticalGroup(
            painelTabelaLivrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelTabelaLivrosLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 60, Short.MAX_VALUE))
        );

        relatorioBotao1.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        relatorioBotao1.setText("voltar");
        relatorioBotao1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                relatorioBotao1ActionPerformed(evt);
            }
        });

        relatorioBotao2.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        relatorioBotao2.setText("Exibir usuários");
        relatorioBotao2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                relatorioBotao2ActionPerformed(evt);
            }
        });

        relatorioBotao3.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        relatorioBotao3.setText("Exibir livros");
        relatorioBotao3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                relatorioBotao3ActionPerformed(evt);
            }
        });

        relatorioLabel1.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        relatorioLabel1.setText("Aqui em Relatório você tem as seguintes opções:");

        relatorioBotao4.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        relatorioBotao4.setText("ver detalhes");
        relatorioBotao4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                relatorioBotao4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout menuRelatorioLayout = new javax.swing.GroupLayout(menuRelatorio);
        menuRelatorio.setLayout(menuRelatorioLayout);
        menuRelatorioLayout.setHorizontalGroup(
            menuRelatorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuRelatorioLayout.createSequentialGroup()
                .addGap(26, 131, Short.MAX_VALUE)
                .addComponent(painelTabelaLivros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(96, 96, 96))
            .addGroup(menuRelatorioLayout.createSequentialGroup()
                .addGroup(menuRelatorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(menuRelatorioLayout.createSequentialGroup()
                        .addGap(166, 166, 166)
                        .addComponent(relatorioBotao2, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(relatorioBotao3, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(menuRelatorioLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(menuRelatorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(relatorioLabel1)
                            .addGroup(menuRelatorioLayout.createSequentialGroup()
                                .addComponent(relatorioBotao1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(relatorioBotao4)))))
                .addGap(17, 17, 17))
            .addGroup(menuRelatorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuRelatorioLayout.createSequentialGroup()
                    .addContainerGap(121, Short.MAX_VALUE)
                    .addComponent(painelTabelaUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(138, Short.MAX_VALUE)))
        );
        menuRelatorioLayout.setVerticalGroup(
            menuRelatorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuRelatorioLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(relatorioLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(menuRelatorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(relatorioBotao3)
                    .addComponent(relatorioBotao2))
                .addGroup(menuRelatorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(menuRelatorioLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(painelTabelaLivros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(94, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuRelatorioLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(menuRelatorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(relatorioBotao4)
                            .addComponent(relatorioBotao1))
                        .addGap(36, 36, 36))))
            .addGroup(menuRelatorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuRelatorioLayout.createSequentialGroup()
                    .addContainerGap(75, Short.MAX_VALUE)
                    .addComponent(painelTabelaUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(114, Short.MAX_VALUE)))
        );

        painelDetalhes.setVisible(false);

        jScrollPane3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        areaExibicao.setBackground(new java.awt.Color(204, 204, 255));
        areaExibicao.setColumns(20);
        areaExibicao.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        areaExibicao.setRows(5);
        jScrollPane3.setViewportView(areaExibicao);
        areaExibicao.setEditable(false);

        detalhesBotao1.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        detalhesBotao1.setText("voltar");
        detalhesBotao1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalhesBotao1ActionPerformed(evt);
            }
        });

        detalhesBotao2.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        detalhesBotao2.setText("Exibir informações de um usuário em específico");
        detalhesBotao2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalhesBotao2ActionPerformed(evt);
            }
        });

        detalhesBotao3.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        detalhesBotao3.setText("Exibir informações de um livro em específico");
        detalhesBotao3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalhesBotao3ActionPerformed(evt);
            }
        });

        detalhesLabel.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        detalhesLabel.setText("Digite o CPF do usuário/código do livro:");

        detalhesTextField.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N

        detalhesBotao4.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        detalhesBotao4.setText("exibir detalhes");
        detalhesBotao4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalhesBotao4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout painelDetalhesLayout = new javax.swing.GroupLayout(painelDetalhes);
        painelDetalhes.setLayout(painelDetalhesLayout);
        painelDetalhesLayout.setHorizontalGroup(
            painelDetalhesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelDetalhesLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(painelDetalhesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(detalhesBotao2)
                    .addComponent(detalhesBotao3)
                    .addGroup(painelDetalhesLayout.createSequentialGroup()
                        .addComponent(detalhesLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(detalhesTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(painelDetalhesLayout.createSequentialGroup()
                        .addComponent(detalhesBotao1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 487, Short.MAX_VALUE)
                        .addComponent(detalhesBotao4)))
                .addGap(17, 17, 17))
            .addGroup(painelDetalhesLayout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 525, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        painelDetalhesLayout.setVerticalGroup(
            painelDetalhesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelDetalhesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(detalhesBotao2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(detalhesBotao3)
                .addGap(31, 31, 31)
                .addGroup(painelDetalhesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(detalhesLabel)
                    .addComponent(detalhesTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addGroup(painelDetalhesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(detalhesBotao1)
                    .addComponent(detalhesBotao4))
                .addGap(17, 17, 17))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(278, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 187, Short.MAX_VALUE)
                    .addComponent(menuManutencao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 188, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 118, Short.MAX_VALUE)
                    .addComponent(menuCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 119, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(menuRelatorio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 107, Short.MAX_VALUE)
                    .addComponent(menuEmprestimo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 108, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(painelDetalhes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(menuPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(402, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 236, Short.MAX_VALUE)
                    .addComponent(menuManutencao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 237, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 198, Short.MAX_VALUE)
                    .addComponent(menuCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 198, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 120, Short.MAX_VALUE)
                    .addComponent(menuRelatorio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 121, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 153, Short.MAX_VALUE)
                    .addComponent(menuEmprestimo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 153, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(painelDetalhes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(menuPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void inicioBotao1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inicioBotao1ActionPerformed
        
        boolean foiSelecionado = inicioBotao1.isSelected();
        
        if (foiSelecionado) {
            inicioBotao2.setEnabled(false);
            inicioLabel3.setVisible(true);
            inicioLabel4.setVisible(true);
            inicioBotao4.setVisible(true);
            inicioBotao5.setVisible(true);
            inicioLabel5.setVisible(true);
            inicioLabel6.setVisible(true);
            
        }
        else {
            inicioBotao2.setEnabled(true);
            inicioLabel3.setVisible(false);
            inicioLabel4.setVisible(false);
            inicioBotao4.setVisible(false);
            inicioBotao5.setVisible(false);
            inicioLabel5.setVisible(false);
            inicioLabel6.setVisible(false);
        }
        
        
        
        
    }//GEN-LAST:event_inicioBotao1ActionPerformed

    private void inicioBotao2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inicioBotao2ActionPerformed
        
        
        if (inicioBotao2.isSelected())
            inicioBotao1.setEnabled(false);
        else
            inicioBotao1.setEnabled(true);
        
    }//GEN-LAST:event_inicioBotao2ActionPerformed

    private void inicioBotao3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inicioBotao3ActionPerformed
        
        
        if (!inicioBotao1.isSelected() && !inicioBotao2.isSelected())
            JOptionPane.showMessageDialog(this, "Selecione uma das opções acima antes de prosseguir", "Um erro ocorreu", JOptionPane.ERROR_MESSAGE);
        
       
        else if (inicioBotao1.isSelected()) {
            if(fileHistoricoUsuario == null || fileHistoricoLivro == null) {
                JOptionPane.showMessageDialog(this, "Os dois arquivos precisam ser selecionados!", "Erro", JOptionPane.ERROR_MESSAGE);
 
            }
            else {
            
                String historicoUsuarios = fileHistoricoUsuario.getName();
                String historicoLivros = fileHistoricoLivro.getName();

                if (historicoUsuarios.charAt(0) != 'u') {
                    JOptionPane.showMessageDialog(this, "O nome de histórico de usuários precisa começar com 'u'. Tente novamente", "Atenção", JOptionPane.WARNING_MESSAGE);
                }
                else if (historicoLivros.charAt(0) != 'l') {
                    JOptionPane.showMessageDialog(this, "O nome de histórico de livros precisa começar com 'l'. Tente novamente", "Atenção", JOptionPane.WARNING_MESSAGE);
                } 
                else {
                    try {
                        minhaBiblioteca = new Biblioteca(fileHistoricoUsuario, fileHistoricoLivro);
                        nomeHistoricoUsuario = historicoUsuarios;
                        nomeHistoricoLivro = historicoLivros;
                        carregarTabelaUsuario();
                        carregarTabelaLivro();

                        JOptionPane.showMessageDialog(this, "Os históricos foram carregados com sucesso!");

                        inicio.setVisible(false);
                        menuPrincipal.setVisible(true);

                    }
                    catch (ClassNotFoundException | IOException e) {
                        JOptionPane.showMessageDialog(this, e, "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }


            }
        }
        else {
            inicio.setVisible(false);
            menuPrincipal.setVisible(true);
            fileHistoricoUsuario = null;     nomeHistoricoUsuario = null;	
   			fileHistoricoLivro = null;      nomeHistoricoLivro = null;	
            
            minhaBiblioteca = new Biblioteca();
        }    
        
        
    }//GEN-LAST:event_inicioBotao3ActionPerformed

    private void manutencaoBotao1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manutencaoBotao1ActionPerformed
        
        if (manutencaoBotao1.isSelected()) {
            manutencaoBotao2.setEnabled(false);
            manutencaoBotao3.setEnabled(false);
            manutencaoBotao4.setEnabled(false);
        }
        else {
            manutencaoBotao2.setEnabled(true);
            manutencaoBotao3.setEnabled(true);
            manutencaoBotao4.setEnabled(true);
        } 
            
            
            
    }//GEN-LAST:event_manutencaoBotao1ActionPerformed

    private void manutencaoBotao2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manutencaoBotao2ActionPerformed
        
        if (manutencaoBotao2.isSelected()) {
            manutencaoBotao1.setEnabled(false);
            manutencaoBotao3.setEnabled(false);
            manutencaoBotao4.setEnabled(false);
        }
        else {
            manutencaoBotao1.setEnabled(true);
            manutencaoBotao3.setEnabled(true);
            manutencaoBotao4.setEnabled(true);
        } 
    }//GEN-LAST:event_manutencaoBotao2ActionPerformed

    private void manutencaoBotao3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manutencaoBotao3ActionPerformed
        
        if (manutencaoBotao3.isSelected()) {
            manutencaoBotao1.setEnabled(false);
            manutencaoBotao2.setEnabled(false);
            manutencaoBotao4.setEnabled(false);
        }
        else {
            manutencaoBotao1.setEnabled(true);
            manutencaoBotao2.setEnabled(true);
            manutencaoBotao4.setEnabled(true);
        } 
    }//GEN-LAST:event_manutencaoBotao3ActionPerformed

    private void manutencaoBotao4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manutencaoBotao4ActionPerformed
        
        if (manutencaoBotao4.isSelected()) {
            manutencaoBotao1.setEnabled(false);
            manutencaoBotao2.setEnabled(false);
            manutencaoBotao3.setEnabled(false);
        }
        else {
            manutencaoBotao1.setEnabled(true);
            manutencaoBotao2.setEnabled(true);
            manutencaoBotao3.setEnabled(true);
        } 
    }//GEN-LAST:event_manutencaoBotao4ActionPerformed

    private void manutencaoBotao6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manutencaoBotao6ActionPerformed
       
        
        menuManutencao.setVisible(false);
        menuPrincipal.setVisible(true);
    }//GEN-LAST:event_manutencaoBotao6ActionPerformed

    private void menuBotao1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBotao1ActionPerformed
        
        menuPrincipal.setVisible(false);
        menuManutencao.setVisible(true);
    }//GEN-LAST:event_menuBotao1ActionPerformed

    private void manutencaoBotao5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manutencaoBotao5ActionPerformed
        
        String nomeArquivo;
        boolean histUsuario = fileHistoricoUsuario != null && (manutencaoBotao1.isSelected() || manutencaoBotao3.isSelected());
        boolean histLivro = fileHistoricoLivro != null && (manutencaoBotao2.isSelected() || manutencaoBotao4.isSelected());
        
        if (!histUsuario && !histLivro) {
            JOptionPane.showMessageDialog(this, "Um arquivo precisa ser selecionado", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        else {
            if (manutencaoBotao1.isSelected()) {     // salvar historico de usuario
                nomeArquivo = fileHistoricoUsuario.getName();
                try {
                    if (nomeArquivo.charAt(0) == 'u') {
                        minhaBiblioteca.salvaArqUsu(fileHistoricoUsuario);
                        nomeHistoricoUsuario = nomeArquivo;
                        JOptionPane.showMessageDialog(this, "Salvo com sucesso o histórico de nome  " + nomeArquivo + "  !");
                    }
                    else {
                        JOptionPane.showMessageDialog(this, "O primeiro caractere precisa ser 'u'. Tente novamente", "Atenção", JOptionPane.WARNING_MESSAGE);
                        
                    }
                }
                catch(IOException e) {
                    JOptionPane.showMessageDialog(this, e, "Erro", JOptionPane.ERROR_MESSAGE);
                    nomeHistoricoUsuario = null;
                }
            }
            
            
            else if (manutencaoBotao2.isSelected()) {   // salvar historico de livro
                nomeArquivo = fileHistoricoLivro.getName();
                try {
                    if (nomeArquivo.charAt(0) == 'l') {
                        minhaBiblioteca.salvaArqLiv(fileHistoricoLivro);
                        nomeHistoricoLivro = nomeArquivo;
                        JOptionPane.showMessageDialog(this, "Salvo com sucesso o histórico de nome  " + nomeArquivo + "  !");
                    }
                    else {
                        JOptionPane.showMessageDialog(this, "O primeiro caractere precisa ser 'l'. Tente novamente", "Atenção", JOptionPane.WARNING_MESSAGE);
                        
                    }
                }
                catch(IOException e) {
                    JOptionPane.showMessageDialog(this, e, "Erro", JOptionPane.ERROR_MESSAGE);
                    nomeHistoricoLivro = null;
                }
            }
            
            
            
            else if (manutencaoBotao3.isSelected()) {   // carregar historico de usuario
                nomeArquivo = fileHistoricoUsuario.getName(); 
                try {
                     if (nomeArquivo.charAt(0) == 'u') {
                         minhaBiblioteca.leArqUsu(fileHistoricoUsuario);
                         nomeHistoricoUsuario = nomeArquivo;
                         carregarTabelaUsuario();
                         JOptionPane.showMessageDialog(this, "Histórico de nome  " + nomeArquivo + "  foi carregado com sucesso!");
                     }
                     else {
                         JOptionPane.showMessageDialog(this, "O primeiro caractere precisa ser 'u'. Tente novamente", "Atenção", JOptionPane.WARNING_MESSAGE);
                         
                     }
                     
                 }
                 catch (ClassNotFoundException | IOException e) {
                      JOptionPane.showMessageDialog(this, e, "Erro", JOptionPane.ERROR_MESSAGE);
                      nomeHistoricoUsuario = null;
                 }
            } 
            
            
            else if (manutencaoBotao4.isSelected()) {   // carregar historico de livro
                nomeArquivo = fileHistoricoLivro.getName();
                try {
                     if (nomeArquivo.charAt(0) == 'l') {
                         minhaBiblioteca.leArqLiv(fileHistoricoLivro);
                         nomeHistoricoLivro = nomeArquivo;
                         carregarTabelaLivro();
                         JOptionPane.showMessageDialog(this, "Histórico de nome  " + nomeArquivo + "  foi carregado com sucesso!");
                     }
                     else {
                         JOptionPane.showMessageDialog(this, "O primeiro caractere precisa ser 'l'. Tente novamente", "Atenção", JOptionPane.WARNING_MESSAGE);
                         
                     }
                     
                 }
                 catch (ClassNotFoundException | IOException e) {
                      JOptionPane.showMessageDialog(this, e, "Erro", JOptionPane.ERROR_MESSAGE);
                      nomeHistoricoLivro = null;
                 }
            }
            
            
            else {   // Nenhum dos botões foram selecionados
                JOptionPane.showMessageDialog(this, "Selecione uma opção acima e um arquivo primeiro", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_manutencaoBotao5ActionPerformed

    private void inicioBotao4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inicioBotao4ActionPerformed
        
        
        int valor = selecionaArquivos.showOpenDialog(this);
        if (valor == JFileChooser.APPROVE_OPTION) {
            inicioLabel5.setText("");
            fileHistoricoUsuario = selecionaArquivos.getSelectedFile();
            inicioLabel5.setText(fileHistoricoUsuario.getAbsolutePath());
        }
        else if (valor == JFileChooser.CANCEL_OPTION) {
            JOptionPane.showMessageDialog(this, "Um arquivo deve ser selecionado para continuar", "Erro", JOptionPane.WARNING_MESSAGE);
        
        }
        else {
            JOptionPane.showMessageDialog(this, "Um erro ocorreu na hora de abrir o arquivo", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
        
        
    }//GEN-LAST:event_inicioBotao4ActionPerformed

    private void inicioBotao5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inicioBotao5ActionPerformed
        
        
        int valor = selecionaArquivos.showOpenDialog(this);
        if (valor == JFileChooser.APPROVE_OPTION) {
            inicioLabel6.setText("");
            fileHistoricoLivro = selecionaArquivos.getSelectedFile();
            inicioLabel6.setText(fileHistoricoLivro.getAbsolutePath());
        }
        else if (valor == JFileChooser.CANCEL_OPTION) {
            JOptionPane.showMessageDialog(this, "Um arquivo deve ser selecionado para salvar/carregar o histórico", "Atenção", JOptionPane.WARNING_MESSAGE);
        
        }
        else {
            JOptionPane.showMessageDialog(this, "Um erro ocorreu na hora de abrir o arquivo", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_inicioBotao5ActionPerformed

    private void manutencaoBotao7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manutencaoBotao7ActionPerformed
        
        int valor;
        if (manutencaoBotao1.isSelected()) {
            valor = selecionaArquivos.showSaveDialog(this);
            if (valor == JFileChooser.APPROVE_OPTION) {
                manutencaoLabel2.setText("");
                fileHistoricoUsuario = selecionaArquivos.getSelectedFile();
                manutencaoLabel2.setText(fileHistoricoUsuario.getAbsolutePath());
            }
            else if(valor == JFileChooser.CANCEL_OPTION) {
               JOptionPane.showMessageDialog(this, "Um arquivo deve ser selecionado para continuar", "Atenção", JOptionPane.WARNING_MESSAGE);                                        
            
            }
            else {
                JOptionPane.showMessageDialog(this, "Um erro ocorreu na hora de abrir o arquivo", "Erro", JOptionPane.ERROR_MESSAGE);
            }
            
        }
        
        else if(manutencaoBotao2.isSelected()) {
            valor = selecionaArquivos.showSaveDialog(this);
            if (valor == JFileChooser.APPROVE_OPTION) {
                manutencaoLabel2.setText("");
                fileHistoricoLivro = selecionaArquivos.getSelectedFile();
                manutencaoLabel2.setText(fileHistoricoLivro.getAbsolutePath());
            }
            else if(valor == JFileChooser.CANCEL_OPTION) {
               JOptionPane.showMessageDialog(this, "Um arquivo deve ser selecionado para continuar", "Atenção", JOptionPane.WARNING_MESSAGE);                                        
            
            }
            else {
                JOptionPane.showMessageDialog(this, "Um erro ocorreu na hora de abrir o arquivo", "Erro", JOptionPane.ERROR_MESSAGE);
            }
            
        }
        
        else if(manutencaoBotao3.isSelected()) {
            valor = selecionaArquivos.showOpenDialog(this);
            if (valor == JFileChooser.APPROVE_OPTION) {
                manutencaoLabel2.setText("");
                fileHistoricoUsuario = selecionaArquivos.getSelectedFile();
                manutencaoLabel2.setText(fileHistoricoUsuario.getAbsolutePath());
            }
            else if(valor == JFileChooser.CANCEL_OPTION) {
               JOptionPane.showMessageDialog(this, "Um arquivo deve ser selecionado para continuar", "Atenção", JOptionPane.WARNING_MESSAGE);                                        
            
            }
            else {
                JOptionPane.showMessageDialog(this, "Um erro ocorreu na hora de abrir o arquivo", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        else if(manutencaoBotao4.isSelected()) {
            valor = selecionaArquivos.showOpenDialog(this);
            if (valor == JFileChooser.APPROVE_OPTION) {
                manutencaoLabel2.setText("");
                fileHistoricoLivro = selecionaArquivos.getSelectedFile();
                manutencaoLabel2.setText(fileHistoricoLivro.getAbsolutePath());
            }
            else if(valor == JFileChooser.CANCEL_OPTION) {
               JOptionPane.showMessageDialog(this, "Um arquivo deve ser selecionado para continuar", "Atenção", JOptionPane.WARNING_MESSAGE);                                        
            
            }
            else {
                JOptionPane.showMessageDialog(this, "Um erro ocorreu na hora de abrir o arquivo", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        else {
            JOptionPane.showMessageDialog(this, "Você deve selecionar uma opção antes de continuar", "Erro", JOptionPane.ERROR_MESSAGE);
            
        }
        
    }//GEN-LAST:event_manutencaoBotao7ActionPerformed

    private void cadastroBotao1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cadastroBotao1ActionPerformed
        
        
        if (cadastroBotao2.isSelected())
            salvarAutomaticamente();
        
        menuCadastro.setVisible(false);
        menuPrincipal.setVisible(true);
        
    }//GEN-LAST:event_cadastroBotao1ActionPerformed

    private void menuBotao2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBotao2ActionPerformed
        
        if (nomeHistoricoUsuario != null && nomeHistoricoLivro != null) {
            String arquivos = nomeHistoricoUsuario + "  e  " + nomeHistoricoLivro;
            cadastroBotao2.setText("Salvar automaticamente os arquivos  " + arquivos + "  ao voltar para o menu anterior.");
            cadastroBotao2.setEnabled(true);
        }
        else {
        	cadastroBotao2.setEnabled(false);
        	cadastroBotao2.setSelected(false);
        	cadastroBotao2.setText("Salvar automaticamente ao sair");
        }
        menuPrincipal.setVisible(false);
        menuCadastro.setVisible(true);
        
    }//GEN-LAST:event_menuBotao2ActionPerformed

    private void cadastroBotao2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cadastroBotao2ActionPerformed
        
    }//GEN-LAST:event_cadastroBotao2ActionPerformed

    private void cadastroBotao3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cadastroBotao3ActionPerformed
        
        if (cadastroBotao3.isSelected()) {
            cadastroBotao4.setEnabled(false);
            controlarVisibilidadeCadastro(true, false);
        }
        else {
            cadastroBotao4.setEnabled(true);
            controlarVisibilidadeCadastro(false, false);
        }
            
        
    }//GEN-LAST:event_cadastroBotao3ActionPerformed

    private void cadastroBotao4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cadastroBotao4ActionPerformed
        
        if (cadastroBotao4.isSelected()) {
            cadastroBotao3.setEnabled(false);
            controlarVisibilidadeCadastro(false, true);
        }
        else {
            cadastroBotao3.setEnabled(true);
            controlarVisibilidadeCadastro(false, false);
        }
        
        
    }//GEN-LAST:event_cadastroBotao4ActionPerformed

    private void cadastroBotao5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cadastroBotao5ActionPerformed
        
        int codigo, copias;
        
        if (cadastroBotao3.isSelected()) {
            try {
                Usuario user = new Usuario(cadastroTextField1.getText(), cadastroTextField2.getText(),
                                           cadastroTextField3.getText(), cadastroTextField4.getText(),
                                           cadastroTextField5.getText(), cadastroTextField6.getText(),
                                           cadastroTextField11.getText());
                
                minhaBiblioteca.cadastraUsuario(user);
                adicionarUsuarioATabela(user);
                
                JOptionPane.showMessageDialog(this, "O cadastro foi realizado com sucesso!  Para ver as informações, vá para o menu relatório.");
               
                
                // Limpando os campos
                cadastroTextField1.setText(""); cadastroTextField2.setText("");
                cadastroTextField3.setText(""); cadastroTextField4.setText("");
                cadastroTextField5.setText(""); cadastroTextField6.setText("");
                cadastroTextField11.setText("");
                
                                
            }
            catch (MinhasExcecoes e) {
                JOptionPane.showMessageDialog(this, e, "Erro", JOptionPane.ERROR_MESSAGE);
            }
            
        }
        
        else if (cadastroBotao4.isSelected()) {
            try {
                codigo = Integer.parseInt(cadastroTextField7.getText());
                copias = Integer.parseInt(cadastroTextField9.getText());
                
                Livro book = new Livro(codigo, cadastroTextField8.getText(),
                                       cadastroTextField10.getText(), copias, 0);
                
                minhaBiblioteca.cadastraLivro(book);
                adicionarLivroATabela(book);
                
                JOptionPane.showMessageDialog(this, "O cadastro foi realizado com sucesso!  Para ver as informações, vá para o menu relatório.");
                
                // Limpando os campos
                cadastroTextField7.setText(""); cadastroTextField8.setText("");
                cadastroTextField9.setText(""); cadastroTextField10.setText("");
                  
            }
            catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Não foi digitado um inteiro no campo  código/cópia.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
            
            catch (MinhasExcecoes e) {
                JOptionPane.showMessageDialog(this, e, "Erro", JOptionPane.ERROR_MESSAGE);
            }
            
        }
        else {
            JOptionPane.showMessageDialog(this, "Nenhuma opção de cadastro acima foi selecionada.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_cadastroBotao5ActionPerformed

    private void cadastroTextField11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cadastroTextField11ActionPerformed
        
    }//GEN-LAST:event_cadastroTextField11ActionPerformed

    private void relatorioBotao1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_relatorioBotao1ActionPerformed
        
        
        menuRelatorio.setVisible(false);
        menuPrincipal.setVisible(true);
    }//GEN-LAST:event_relatorioBotao1ActionPerformed

    private void emprestimoBotao1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emprestimoBotao1ActionPerformed
        
       
        if (emprestimoBotao2.isSelected())
            salvarAutomaticamente();
        areaExibicao2.setText("");  
        menuEmprestimo.setVisible(false);
        menuPrincipal.setVisible(true);
    }//GEN-LAST:event_emprestimoBotao1ActionPerformed

    private void menuBotao3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBotao3ActionPerformed
        
        
        if (nomeHistoricoUsuario != null && nomeHistoricoLivro != null) {
            String arquivos = nomeHistoricoUsuario + "  e  " + nomeHistoricoLivro;
            emprestimoBotao2.setText("Salvar automaticamente os arquivos  " + arquivos + "  ao voltar para o menu anterior.");
            emprestimoBotao2.setEnabled(true);
        }
         else {
        	emprestimoBotao2.setEnabled(false);
        	emprestimoBotao2.setSelected(false);
        	emprestimoBotao2.setText("Salvar automaticamente ao sair");
        }
        
        menuPrincipal.setVisible(false);
        menuEmprestimo.setVisible(true);
    }//GEN-LAST:event_menuBotao3ActionPerformed

    private void menuBotao4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBotao4ActionPerformed
       
        
        menuPrincipal.setVisible(false);
        menuRelatorio.setVisible(true);
    }//GEN-LAST:event_menuBotao4ActionPerformed

    private void relatorioBotao2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_relatorioBotao2ActionPerformed
        
        painelTabelaLivros.setVisible(false);
        painelTabelaUsuarios.setVisible(true);
    }//GEN-LAST:event_relatorioBotao2ActionPerformed

    private void relatorioBotao3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_relatorioBotao3ActionPerformed
        
        painelTabelaUsuarios.setVisible(false);
        painelTabelaLivros.setVisible(true);
    }//GEN-LAST:event_relatorioBotao3ActionPerformed

    private void relatorioBotao4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_relatorioBotao4ActionPerformed
        
        menuRelatorio.setVisible(false);
        painelDetalhes.setVisible(true);
        
    }//GEN-LAST:event_relatorioBotao4ActionPerformed

    private void detalhesBotao1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalhesBotao1ActionPerformed
        
        
        areaExibicao.setText("");
        painelDetalhes.setVisible(false);
        menuRelatorio.setVisible(true);
    }//GEN-LAST:event_detalhesBotao1ActionPerformed

    private void detalhesBotao2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalhesBotao2ActionPerformed
        
        if (detalhesBotao2.isSelected()) 
            detalhesBotao3.setEnabled(false);
        else
            detalhesBotao3.setEnabled(true);
    }//GEN-LAST:event_detalhesBotao2ActionPerformed

    private void detalhesBotao3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalhesBotao3ActionPerformed
        
        if (detalhesBotao3.isSelected())
            detalhesBotao2.setEnabled(false);
        else
            detalhesBotao2.setEnabled(true);
    }//GEN-LAST:event_detalhesBotao3ActionPerformed

    private void detalhesBotao4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalhesBotao4ActionPerformed
        
        String campoTexto = detalhesTextField.getText();
        
        if (detalhesBotao2.isSelected()) {
            Usuario user;
            if (ValidaCPF.isCPF(campoTexto)) {
                try {
                    user = minhaBiblioteca.getUsuario(ValidaCPF.toLong(campoTexto));
                    areaExibicao.setText("As informações são:\n\n" + user.toString());
                    detalhesTextField.setText("");
                }
                catch(UsuarioNaoCadastradoEx e) {
                    JOptionPane.showMessageDialog(this, e, "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
            else
                JOptionPane.showMessageDialog(this, "O CPF digitado é inválido", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
        else if (detalhesBotao3.isSelected()) {
            Livro book;
            int numero;
            try {
                numero = Integer.parseInt(campoTexto);
                book = minhaBiblioteca.getLivro(numero);
                areaExibicao.setText("As informações são:\n\n" + book.toString());
                detalhesTextField.setText("");
            }
            catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Você não digitou um inteiro para o código.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
            catch(LivroNaoCadastradoEx e) {
                JOptionPane.showMessageDialog(this, e, "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        else
            JOptionPane.showMessageDialog(this, "Nenhuma opção acima foi selecionada.", "Erro", JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_detalhesBotao4ActionPerformed

    private void emprestimoBotao3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emprestimoBotao3ActionPerformed
        
        if (emprestimoBotao3.isSelected()) {
            emprestimoBotao4.setEnabled(false);
            emprestimoBotao6.setVisible(true);
            
            // Exibindo na tela
            emprestimoLabel2.setVisible(true);     emprestimoLabel5.setVisible(true);
            emprestimoLabel3.setVisible(true);     emprestimoLabel6.setVisible(true);
            emprestimoLabel4.setVisible(true);     emprestimoLabel7.setVisible(true);
            
            emprestimoTextField1.setVisible(true);     emprestimoTextField4.setVisible(true);
            emprestimoTextField2.setVisible(true);     emprestimoTextField5.setVisible(true);
            emprestimoTextField3.setVisible(true);
        }
        else {
            emprestimoBotao4.setEnabled(true);
            emprestimoBotao6.setVisible(false);
            emprestimoBotao6.setSelected(false);
            
            // Removendo a exibição da tela
            emprestimoLabel2.setVisible(false);     emprestimoLabel5.setVisible(false);
            emprestimoLabel3.setVisible(false);     emprestimoLabel6.setVisible(false);
            emprestimoLabel4.setVisible(false);     emprestimoLabel7.setVisible(false);
            
            emprestimoTextField1.setVisible(false);     emprestimoTextField4.setVisible(false);
            emprestimoTextField2.setVisible(false);     emprestimoTextField5.setVisible(false);
            emprestimoTextField3.setVisible(false);
        }
            
    }//GEN-LAST:event_emprestimoBotao3ActionPerformed

    private void emprestimoBotao4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emprestimoBotao4ActionPerformed
        
        if (emprestimoBotao4.isSelected()) {
            emprestimoBotao3.setEnabled(false);
            
            // Exibindo na tela
            emprestimoLabel2.setVisible(true);     emprestimoTextField1.setVisible(true);
            emprestimoLabel3.setVisible(true);     emprestimoTextField2.setVisible(true);     
            
        }
        else {
            emprestimoBotao3.setEnabled(true);
        
            // Removendo a exibição da tela
            emprestimoLabel2.setVisible(false);     emprestimoTextField1.setVisible(false);
            emprestimoLabel3.setVisible(false);     emprestimoTextField2.setVisible(false);
        }
    }//GEN-LAST:event_emprestimoBotao4ActionPerformed

    private void emprestimoTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emprestimoTextField3ActionPerformed
        
    }//GEN-LAST:event_emprestimoTextField3ActionPerformed

    private void emprestimoBotao5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emprestimoBotao5ActionPerformed
        
        if (emprestimoBotao3.isSelected()) {
            boolean verificarOpcao = true;
            if (emprestimoBotao6.isSelected())
                verificarOpcao = false;
            
            Usuario user;
            Livro book;
            String str = emprestimoTextField1.getText();
            if (ValidaCPF.isCPF(str)) {
                try {
                    user = minhaBiblioteca.getUsuario(ValidaCPF.toLong(str));
                    book = minhaBiblioteca.getLivro(Integer.parseInt(emprestimoTextField2.getText()));
                    
                    boolean continuar = true;
                    if (!user.getSituacaoMultaUsuario().equals("Regular")) {
                        continuar = exibirDialogoMulta(user, "O usuário possui uma multa pendente não paga.");
                        if (continuar)
                            areaExibicao2.setText("Informações do usuário:\n\n\n" + user + "\n\n\nInformações do livro:\n\n\n" + book);
                    }
                    if (continuar) {
                        String dia = emprestimoTextField3.getText();
                        String mes = emprestimoTextField4.getText();
                        String ano = emprestimoTextField5.getText();
                        minhaBiblioteca.emprestaLivro(dia, mes, ano, verificarOpcao, user, book);
                     
                        
                        JOptionPane.showMessageDialog(this, "O empréstimo do livro de código  " + book.getCodigoLivro() 
                                                     + "  para o usuário de CPF  " + ValidaCPF.toString(user.getNumCPF()) + "  foi realizado com sucesso!");
                        areaExibicao2.setText("Informações do usuário:\n\n\n" + user + "\n\n\nInformações do livro:\n\n\n" + book);
                        
                        // Apagando os campos de texto
                        emprestimoTextField1.setText("");     emprestimoTextField4.setText("");
                        emprestimoTextField2.setText("");     emprestimoTextField5.setText("");
                        emprestimoTextField3.setText("");
                        
                        // Atualizando a tabela
                        int linha = obterLinhaCorrespondente(tabelaLivros, 0, Integer.toString(book.getCodigoLivro()));
                        tabelaLivros.setValueAt(Integer.toString(book.getEmprestados()), linha, 4);
                    }
                    else
                        JOptionPane.showMessageDialog(this, "O empréstimo não pode continuar pois este usuário não pagou a multa.", "Erro", JOptionPane.ERROR_MESSAGE);
                    
                }
                catch(NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Não foi digitado um número inteiro para o código do livro.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
                catch(MinhasExcecoes | CopiaNaoDisponivelEx | LivroNaoCadastradoEx | UsuarioNaoCadastradoEx e) {
                    JOptionPane.showMessageDialog(this, e, "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
            else
                JOptionPane.showMessageDialog(this, "Não foi digitado um CPF válido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
        
        
        else if (emprestimoBotao4.isSelected()) {
            Usuario user;
            Livro book;
            String str = emprestimoTextField1.getText();
            if (ValidaCPF.isCPF(str)) {
                try {
                    user = minhaBiblioteca.getUsuario(ValidaCPF.toLong(str));
                    book = minhaBiblioteca.getLivro(Integer.parseInt(emprestimoTextField2.getText()));
                    minhaBiblioteca.devolveLivro(user, book);
                    
                    if (!user.getSituacaoMultaUsuario().equals("Regular"))
                        exibirDialogoMulta(user, "O usuário está devolvendo este livro atrasado e, com isso, ele precisa pagar uma multa para posteriormente pedir outro empréstimo.");
                    
                    JOptionPane.showMessageDialog(this, "A devolução do livro de código  " + book.getCodigoLivro()
                                                 + "  para o usuário de CPF  " + ValidaCPF.toString(user.getNumCPF()) + "  foi realizada com sucesso!");
                    
                    areaExibicao2.setText("Informações do usuário:\n\n\n" + user + "\n\n\nInformações do livro:\n\n\n" + book);
                    
                    // Apagando os campos de texto
                    emprestimoTextField1.setText("");     emprestimoTextField4.setText("");
                    emprestimoTextField2.setText("");     emprestimoTextField5.setText("");
                    emprestimoTextField3.setText("");
                    
                    // Atualizando a tabela
                    int linha = obterLinhaCorrespondente(tabelaLivros, 0, Integer.toString(book.getCodigoLivro()));
                    tabelaLivros.setValueAt(Integer.toString(book.getEmprestados()), linha, 4);
                }
                
                catch(NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Não foi digitado um número inteiro para o código do livro.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
                catch (MinhasExcecoes | NenhumaCopiaEmprestadaEx | LivroNaoCadastradoEx | UsuarioNaoCadastradoEx e) {
                    JOptionPane.showMessageDialog(this, e, "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
            else
                JOptionPane.showMessageDialog(this, "Não foi digitado um CPF válido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
        
        else 
            JOptionPane.showMessageDialog(this, "Nenhuma opção acima foi selecionada!", "Erro", JOptionPane.ERROR_MESSAGE);
        
    }//GEN-LAST:event_emprestimoBotao5ActionPerformed

    
    
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new P4nX().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea areaExibicao;
    private javax.swing.JTextArea areaExibicao2;
    private javax.swing.JButton cadastroBotao1;
    private javax.swing.JRadioButton cadastroBotao2;
    private javax.swing.JRadioButton cadastroBotao3;
    private javax.swing.JRadioButton cadastroBotao4;
    private javax.swing.JButton cadastroBotao5;
    private javax.swing.JLabel cadastroLabel1;
    private javax.swing.JLabel cadastroLabel10;
    private javax.swing.JLabel cadastroLabel11;
    private javax.swing.JLabel cadastroLabel12;
    private javax.swing.JLabel cadastroLabel13;
    private javax.swing.JLabel cadastroLabel2;
    private javax.swing.JLabel cadastroLabel3;
    private javax.swing.JLabel cadastroLabel4;
    private javax.swing.JLabel cadastroLabel5;
    private javax.swing.JLabel cadastroLabel6;
    private javax.swing.JLabel cadastroLabel7;
    private javax.swing.JLabel cadastroLabel8;
    private javax.swing.JLabel cadastroLabel9;
    private javax.swing.JTextField cadastroTextField1;
    private javax.swing.JTextField cadastroTextField10;
    private javax.swing.JTextField cadastroTextField11;
    private javax.swing.JTextField cadastroTextField2;
    private javax.swing.JTextField cadastroTextField3;
    private javax.swing.JTextField cadastroTextField4;
    private javax.swing.JTextField cadastroTextField5;
    private javax.swing.JTextField cadastroTextField6;
    private javax.swing.JTextField cadastroTextField7;
    private javax.swing.JTextField cadastroTextField8;
    private javax.swing.JTextField cadastroTextField9;
    private javax.swing.JButton detalhesBotao1;
    private javax.swing.JRadioButton detalhesBotao2;
    private javax.swing.JRadioButton detalhesBotao3;
    private javax.swing.JButton detalhesBotao4;
    private javax.swing.JLabel detalhesLabel;
    private javax.swing.JTextField detalhesTextField;
    private javax.swing.JButton emprestimoBotao1;
    private javax.swing.JRadioButton emprestimoBotao2;
    private javax.swing.JRadioButton emprestimoBotao3;
    private javax.swing.JRadioButton emprestimoBotao4;
    private javax.swing.JButton emprestimoBotao5;
    private javax.swing.JRadioButton emprestimoBotao6;
    private javax.swing.JLabel emprestimoLabel1;
    private javax.swing.JLabel emprestimoLabel2;
    private javax.swing.JLabel emprestimoLabel3;
    private javax.swing.JLabel emprestimoLabel4;
    private javax.swing.JLabel emprestimoLabel5;
    private javax.swing.JLabel emprestimoLabel6;
    private javax.swing.JLabel emprestimoLabel7;
    private javax.swing.JTextField emprestimoTextField1;
    private javax.swing.JTextField emprestimoTextField2;
    private javax.swing.JTextField emprestimoTextField3;
    private javax.swing.JTextField emprestimoTextField4;
    private javax.swing.JTextField emprestimoTextField5;
    private javax.swing.JPanel inicio;
    private javax.swing.JRadioButton inicioBotao1;
    private javax.swing.JRadioButton inicioBotao2;
    private javax.swing.JButton inicioBotao3;
    private javax.swing.JButton inicioBotao4;
    private javax.swing.JButton inicioBotao5;
    private javax.swing.JLabel inicioLabel1;
    private javax.swing.JLabel inicioLabel2;
    private javax.swing.JLabel inicioLabel3;
    private javax.swing.JLabel inicioLabel4;
    private javax.swing.JLabel inicioLabel5;
    private javax.swing.JLabel inicioLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JRadioButton manutencaoBotao1;
    private javax.swing.JRadioButton manutencaoBotao2;
    private javax.swing.JRadioButton manutencaoBotao3;
    private javax.swing.JRadioButton manutencaoBotao4;
    private javax.swing.JButton manutencaoBotao5;
    private javax.swing.JButton manutencaoBotao6;
    private javax.swing.JButton manutencaoBotao7;
    private javax.swing.JLabel manutencaoLabel1;
    private javax.swing.JLabel manutencaoLabel2;
    private javax.swing.JButton menuBotao1;
    private javax.swing.JButton menuBotao2;
    private javax.swing.JButton menuBotao3;
    private javax.swing.JButton menuBotao4;
    private javax.swing.JPanel menuCadastro;
    private javax.swing.JPanel menuEmprestimo;
    private javax.swing.JLabel menuLabel1;
    private javax.swing.JLabel menuLabel2;
    private javax.swing.JPanel menuManutencao;
    private javax.swing.JPanel menuPrincipal;
    private javax.swing.JPanel menuRelatorio;
    private javax.swing.JPanel painelDetalhes;
    private javax.swing.JPanel painelTabelaLivros;
    private javax.swing.JPanel painelTabelaUsuarios;
    private javax.swing.JButton relatorioBotao1;
    private javax.swing.JButton relatorioBotao2;
    private javax.swing.JButton relatorioBotao3;
    private javax.swing.JButton relatorioBotao4;
    private javax.swing.JLabel relatorioLabel1;
    private javax.swing.JTable tabelaLivros;
    private javax.swing.JTable tabelaUsuarios;
    // End of variables declaration//GEN-END:variables
}
