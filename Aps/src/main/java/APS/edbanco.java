package APS; // Declara um pacote chamado APS. Isso é usado para agrupar classes relacionadas.

// Importações: Essas trazem várias bibliotecas Java para uso no código.
import java.sql.*; // Importa todas as classes do pacote java.sql, que são usadas para lidar com o banco de dados SQL.
import javax.swing.*; // Importa todas as classes do pacote javax.swing, que são usadas para criar a interface gráfica do usuário (GUI).
import javax.swing.table.DefaultTableModel; // Importa a classe DefaultTableModel do pacote javax.swing.table, que é usada para trabalhar com tabelas na GUI.
import java.awt.*; // Importa todas as classes do pacote java.awt, que são usadas para criar a interface gráfica do usuário (GUI).
import java.awt.event.*; // Importa todas as classes do pacote java.awt.event, que são usadas para lidar com eventos como cliques de botão.
import com.toedter.calendar.JDateChooser; // Importa a classe JDateChooser do pacote com.toedter.calendar, que é usada para usar um seletor de data na GUI.
import java.beans.PropertyChangeEvent; // Importa a classe PropertyChangeEvent do pacote java.beans, que é usada para lidar com mudanças de propriedades dos beans.
import java.beans.PropertyChangeListener; // Importa a classe PropertyChangeListener do pacote java.beans, que é usada para ouvir mudanças de propriedades dos beans.
import java.text.SimpleDateFormat; // Importa a classe SimpleDateFormat do pacote java.text, que é usada para formatar datas.
import java.time.LocalDate; // Importa a classe LocalDate do pacote java.time, que é usada para trabalhar com datas.
import java.time.format.DateTimeFormatter; // Importa a classe DateTimeFormatter do pacote java.time.format, que é usada para formatar datas.
import java.time.format.DateTimeParseException; // Importa a classe DateTimeParseException do pacote java.time.format, que é usada para lidar com exceções ao analisar datas.
import java.util.*; // Importa todas as classes do pacote java.util, que são usadas para usar estruturas de dados como List e Map.
import java.util.logging.Level; // Importa a classe Level do pacote java.util.logging, que é usada para registrar eventos.
import java.util.logging.Logger; // Importa a classe Logger do pacote java.util.logging, que é usada para registrar eventos.
import org.jfree.chart.*; // Importa todas as classes do pacote org.jfree.chart, que são usadas para criar gráficos.
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator; // Importa a classe StandardCategoryItemLabelGenerator do pacote org.jfree.chart.labels, que é usada para gerar rótulos de itens de categoria padrão.
import org.jfree.chart.plot.CategoryPlot; // Importa a classe CategoryPlot do pacote org.jfree.chart.plot, que é usada para criar gráficos de categoria.
import org.jfree.chart.plot.PlotOrientation; // Importa a classe PlotOrientation do pacote org.jfree.chart.plot, que é usada para definir a orientação do gráfico.
import org.jfree.chart.renderer.category.BarRenderer; // Importa a classe BarRenderer do pacote org.jfree.chart.renderer.category, que é usada para renderizar barras em um gráfico de barras.
import org.jfree.data.category.DefaultCategoryDataset; // Importa a classe DefaultCategoryDataset do pacote org.jfree.data.category, que é usada para criar um conjunto de dados de categoria padrão.


public class edbanco extends JFrame implements ActionListener { // Declaração da classe 'edbanco' que estende JFrame e implementa ActionListener.
    private Connection connection; // Uma variável privada para a conexão com o banco de dados.
    private JButton viewButton, insertButton, updateButton, deleteButton, voltarButton; // Botões da GUI.
    private JTable table; // Uma tabela para exibir dados.
    private JTextArea infoText; // Uma área de texto para exibir informações.
    private ChartPanel chartPanel;// Uma área de grafico 
    private JDateChooser dateChooserD, dateChooserP; // Seletor de data para escolher datas.
    private String[] cidades = {"São Paulo", "Osasco", "Guarulhos", "Mauá", "Suzano"}; // Array de strings contendo nomes de cidades.
    private int c1 = -2,c2 = -2;
    private boolean erro= false;
    
    public void edbanco (int valor) { // Método 'edbanco' que recebe um valor inteiro.
        def_data(); // Chama o método 'def_data'.
        switch (valor) { // Com base no valor recebido, chama diferentes métodos.
            case 0:
                telas_cadastro(); 
                break;
            case 1:
                telas_deletar();
                break;
            case 2:
                telas_update();                
                break;
            case 3:
                telas_relatorio(1);               
                break;
            case 4:
                telas_relatorio(2);                    
                break;
            case 5:
                telas_relatorio(3); 
                break;
            default:
                // Caso o valor não corresponda a nenhum dos casos acima, não faz nada.
        }
    }
    
    public void telas_cadastro(){
        
        // Configurações da janela
        setSize(500, 300); // Define o tamanho da janela.
        setLocationRelativeTo(null); // Centraliza a janela na tela.

        // Componentes
        table = new JTable(); // Cria uma nova tabela.
        JScrollPane scrollPane = new JScrollPane(table); // Cria um painel de rolagem que contém a tabela.
        viewButton = new JButton("Atualizar"); // Cria um botão para atualizar a tabela.
        insertButton = new JButton("Inserir"); // Cria um botão para inserir dados.
        voltarButton = new JButton("voltar"); // Cria um botão para voltar.

        // Layout
        JPanel buttonPanel = new JPanel(); // Cria um novo painel para os botões.
        buttonPanel.add(viewButton); // Adiciona o botão de visualização ao painel de botões.
        buttonPanel.add(insertButton); // Adiciona o botão de inserção ao painel de botões.
        buttonPanel.add(voltarButton); // Adiciona o botão de voltar ao painel de botões.

        Container contentPane = getContentPane(); // Obtém o painel de conteúdo da janela.
        contentPane.add(scrollPane, BorderLayout.CENTER); // Adiciona o painel de rolagem ao centro do painel de conteúdo.
        contentPane.add(buttonPanel, BorderLayout.SOUTH); // Adiciona o painel de botões ao sul (parte inferior) do painel de conteúdo.

        // Adicionando listeners aos botões
        viewButton.addActionListener(this); // Adiciona um ouvinte de ação ao botão de visualização.
        insertButton.addActionListener(this); // Adiciona um ouvinte de ação ao botão de inserção.
        voltarButton.addActionListener(this); // Adiciona um ouvinte de ação ao botão de voltar.

        // Conectar ao banco de dados
        try {
            connect(); // Tenta conectar ao banco de dados.
            viewData(); // Tenta visualizar os dados.
        } catch (SQLException e) {
            e.printStackTrace(); // Imprime a pilha de chamadas da exceção.
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); // Mostra uma caixa de diálogo com a mensagem de erro.
        }
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0); // Fecha a aplicação quando a janela é fechada.
            }
        });
        setVisible(true); // Torna a janela visível.
    }

    public void telas_deletar() {
        
        // Configurações da janela
        setSize(500, 300);// Define o tamanho da janela.
        setLocationRelativeTo(null); // Centraliza a janela na tela.

        // Componentes
        table = new JTable();// Cria uma nova tabela.
        JScrollPane scrollPane = new JScrollPane(table);
        viewButton = new JButton("Atualizar");
        deleteButton = new JButton("Apagar");
        voltarButton = new JButton("voltar");

        // Layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(viewButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(voltarButton);

        Container contentPane = getContentPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        // Adicionando listeners aos botões
        viewButton.addActionListener(this);
        deleteButton.addActionListener(this);
        voltarButton.addActionListener(this);

        // Conectar ao banco de dados
        try {
            connect();
            viewData();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
            addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setVisible(true);
                
    }
    
    private void telas_update() {
        
        // Configurações da janela
        setSize(500, 300);
        setLocationRelativeTo(null);

        // Componentes
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        viewButton = new JButton("Atualizar");
        updateButton = new JButton("modificar");
        voltarButton = new JButton("voltar");

        // Layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(viewButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(voltarButton);

        Container contentPane = getContentPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        // Adicionando listeners aos botões
        viewButton.addActionListener(this);
        updateButton.addActionListener(this);
        voltarButton.addActionListener(this);

        // Conectar ao banco de dados
        try {
            connect();
            viewData();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
            addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setVisible(true);
    }
    
    private void telas_relatorio(int valor) {
        
        //Configurações da janela
        setSize(800, 600);
        setLocationRelativeTo(null); 
        
        if(valor != 1)
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Componentes
        voltarButton = new JButton("voltar");
        infoText = new JTextArea(0, 0); // Área de texto para exibir as informações
        infoText.setEditable(false); // Tornando a área de texto somente leitura
        infoText.setFont(new Font("Serif", Font.PLAIN, 20)); // Aumenta o tamanho da fonte
        
               
        // Cria o dataset e o gráfico vazio
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        JFreeChart chart = ChartFactory.createBarChart(
            " ",
            " ", 
            "Total", 
            dataset, 
            PlotOrientation.VERTICAL, 
            true, 
            true, 
            false
        );
        
        
        
        // Adicionando texto
        JLabel labelD = new JLabel("Data Inicial:");
        JLabel labelP = new JLabel("Data Final:");

        JPanel datePanel = new JPanel();
        datePanel.add(labelD); // Adiciona o texto ao painel
        datePanel.add(dateChooserD);
        datePanel.add(labelP); // Adiciona o texto ao painel
        datePanel.add(dateChooserP);
        
        // Layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(voltarButton);
        chartPanel = new ChartPanel(chart);
        
        Container contentPanel = getContentPane();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // Define o layout para BoxLayout
        contentPanel.add(datePanel); // Adicionando o seletor de data no topo
        contentPanel.add(infoText); // Adicionando área de texto
        contentPanel.add(chartPanel); // Adicionando o gráfico
        contentPanel.add(buttonPanel); // Adicionando os botões na parte inferior

    
        
        // Adicionando listeners aos botões
        voltarButton.addActionListener(this);
        
        try {
            connect();
            escolha_relatorio(valor);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        } 
        
        dateChooserD.getDateEditor().addPropertyChangeListener(
            new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ("date".equals(e.getPropertyName())) {
                    try {
                        //JOptionPane.showMessageDialog(null, "a data um foi alterada");
                        escolha_relatorio(valor);
                    } catch (SQLException ex) {
                        Logger.getLogger(edbanco.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
            dateChooserP.getDateEditor().addPropertyChangeListener(
            new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ("date".equals(e.getPropertyName())) {
                    try {
                        //JOptionPane.showMessageDialog(null, "a data 2 foi alterada");
                        escolha_relatorio(valor);
                    } catch (SQLException ex) {
                        Logger.getLogger(edbanco.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        
        setVisible(true);
        if(erro)
            try {
                voltar();
            } catch (SQLException ex) {
                Logger.getLogger(edbanco.class.getName()).log(Level.SEVERE, null, ex);
            };
    }  
    
    private void escolha_relatorio(int valor)throws SQLException{
        switch (valor) {
            case 1:
                if (c1 == -2) {
                    c1 = JOptionPane.showOptionDialog(this, "Escolha a cidade:", "cidade", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, cidades, cidades[0])+1;           
                }
                if (c1 == 0){
                    JOptionPane.showMessageDialog(this, "Cidade não celecionada", "Erro", JOptionPane.ERROR_MESSAGE);
                    erro = true;
                } else {
                    viewData_uma_cidades(c1);
                }
                break;
            
            case 2:
                if (c1 == -2) {
                    c1 = JOptionPane.showOptionDialog(this, "Escolha a primeira cidade:", "cidade", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, cidades, cidades[0])+1;
                }
                if (c2 == -2 && c1 != 0) {
                    c2 = JOptionPane.showOptionDialog(this, "Escolha a segunda cidade:", "cidade", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, cidades, cidades[0])+1;
                }

                if (c1 != 0 && c2 != 0 && c1 != c2) {
                    //JOptionPane.showMessageDialog(this, c1+1 +" "+ c2+1);
                    viewData_duas_cidades(c1,c2);
                } else {
                    if (c1 == 0 && c2 == 0)
                    {
                        JOptionPane.showMessageDialog(this, "Nenhuma cidade selecionada", "Erro", JOptionPane.ERROR_MESSAGE);
                        erro = true;
                    }else if(c1 == 0){
                        JOptionPane.showMessageDialog(this, "A primeira cidade não foi selecionada", "Erro", JOptionPane.ERROR_MESSAGE);
                        erro = true;
                    }else if(c2 == 0){
                        JOptionPane.showMessageDialog(this, "A segunda cidade não foi selecionada", "Erro", JOptionPane.ERROR_MESSAGE);   
                        erro = true;
                    }else if(c1 == c2){
                        JOptionPane.showMessageDialog(this, "Você selecionou a mesma cidade", "Erro", JOptionPane.ERROR_MESSAGE); 
                        erro = true;
                    }
                } 
                break;
            
            case 3:
                viewData_todas_cidades();
                break;
        }
        
    }
    
    // Conectar ao banco de dados
    private void connect() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/APS";
        String user = "root";
        String password = "root";
        connection = DriverManager.getConnection(url, user, password);
    }

    // Visualizar casos
    private void viewData() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery
        ("SELECT casos.id_casos as 'Numero do caso', DATE_FORMAT(casos.dt_caso, '%d/%m/%Y') AS 'data da ocorencia' , "
                + "cidades.nome_cidade as 'cidade', casos.infectados, casos.obitos, casos.curados "
                + "FROM casos INNER JOIN cidades ON casos.id_cidade = cidades.id_cidade ORDER BY casos.dt_caso;");
        ResultSetMetaData metaData = resultSet.getMetaData();                       
        
        int columnCount = metaData.getColumnCount();

        DefaultTableModel model = new DefaultTableModel();

        // Adicionando colunas à tabela
        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
            model.addColumn(metaData.getColumnLabel(columnIndex));   
        }
        // Adicionando linhas à tabela
        while (resultSet.next()) {
            Object[] row = new Object[columnCount];
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                row[columnIndex - 1] = resultSet.getObject(columnIndex);
            }
            model.addRow(row);
        }
        table.setModel(model);
        resultSet.close();
        statement.close();
    }
       
    // Visualizar dados de uma cidade
    private void viewData_uma_cidades(int valor1) throws SQLException {
                
        Statement statement1 = connection.createStatement();
        
        var selectedDate1 = dateChooserD.getDate();// pega a data
        var selectedDate2 = dateChooserP.getDate();// pega a data

        // Para formatar a data e convertê-la para uma string
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString1 = sdf.format(selectedDate1);
        String dateString2 = sdf.format(selectedDate2);
        
        // Para formatar a data no padrao SQL para seu usado
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        LocalDate localDate = LocalDate.parse(dateString1, formatter);
        java.sql.Date sqlDate1 = java.sql.Date.valueOf(localDate);
        
        localDate = LocalDate.parse(dateString2, formatter);
        java.sql.Date sqlDate2 = java.sql.Date.valueOf(localDate);
        
        //soma todos os infectados
        PreparedStatement SUM_infe = connection.prepareStatement
        ("SELECT SUM(infectados) FROM casos where id_cidade = ? and dt_caso >= ? and dt_caso <= ?");
        SUM_infe.setInt(1,valor1);
        SUM_infe.setDate(2,sqlDate1);
        SUM_infe.setDate(3,sqlDate2);
        ResultSet resultSUM_infe = SUM_infe.executeQuery();
        int SUM_infe_Value = 0; // Valor padrão caso a consulta não retorne resultados
        // Verifica se há linhas no ResultSet
        if (resultSUM_infe.next()) {
         SUM_infe_Value = resultSUM_infe.getInt(1); // Obtém o valor da primeira coluna do resultado da consulta
        }
        
        //soma todos os obitos
        PreparedStatement SUM_obi = connection.prepareStatement
        ("SELECT SUM(obitos) FROM casos where id_cidade = ? and dt_caso >= ? and dt_caso <= ?");
        SUM_obi.setInt(1,valor1);
        SUM_obi.setDate(2,sqlDate1);
        SUM_obi.setDate(3,sqlDate2);
        ResultSet resultSUM_obi = SUM_obi.executeQuery();
        int SUM_obi_Value = 0; // Valor padrão caso a consulta não retorne resultados
        // Verifica se há linhas no ResultSet
        if (resultSUM_obi.next()) {
         SUM_obi_Value = resultSUM_obi.getInt(1); // Obtém o valor da primeira coluna do resultado da consulta
        }
        
        //soma todos os curados
        PreparedStatement SUM_cura = connection.prepareStatement
        ("SELECT SUM(curados) FROM casos where id_cidade = ? and dt_caso >= ? and dt_caso <= ?");
        SUM_cura.setInt(1,valor1);
        SUM_cura.setDate(2,sqlDate1);
        SUM_cura.setDate(3,sqlDate2);
        ResultSet resultSUM_cura = SUM_cura.executeQuery();
        int SUM_cura_Value = 0; // Valor padrão caso a consulta não retorne resultados
        // Verifica se há linhas no ResultSet
        if (resultSUM_cura.next()) {
         SUM_cura_Value = resultSUM_cura.getInt(1); // Obtém o valor da primeira coluna do resultado da consulta
        }
        
        infoText.setText("");// limpa o infoText1
        infoText.append("  Resutados de comparação da cidade de "+ cidades[valor1-1] +
                " no periodo de "+dateString1 +" a "+dateString2);
        

        
        // Obtém o gráfico e o dataset
        JFreeChart chart = chartPanel.getChart();
        DefaultCategoryDataset dataset = (DefaultCategoryDataset) chart.getCategoryPlot().getDataset();

        // Limpa o dataset
        dataset.clear();

        // Adiciona os novos dados ao dataset
        dataset.addValue(SUM_infe_Value, "Infectados", "Total");
        dataset.addValue(SUM_obi_Value, "Óbitos", "Total");
        dataset.addValue(SUM_cura_Value, "Curados", "Total");

        // Configura o renderer para mostrar os valores nas barras
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setDefaultItemLabelsVisible(true);

        
        // Atualiza o gráfico
        chart.fireChartChanged();
        
 
        statement1.close();
    }
    
    // Visualizar dados de duas cidades 
    private void viewData_duas_cidades(int valor1, int valor2) throws SQLException {
                
        Statement statement1 = connection.createStatement(); 
        
        var selectedDate1 = dateChooserD.getDate();// pega a data
        var selectedDate2 = dateChooserP.getDate();// pega a data

        // Para formatar a data e convertê-la para uma string
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString1 = sdf.format(selectedDate1);
        String dateString2 = sdf.format(selectedDate2);
        
        // Para formatar a data no padrao SQL para seu usado
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        LocalDate localDate = LocalDate.parse(dateString1, formatter);
        java.sql.Date sqlDate1 = java.sql.Date.valueOf(localDate);
        
        localDate = LocalDate.parse(dateString2, formatter);
        java.sql.Date sqlDate2 = java.sql.Date.valueOf(localDate);
             
        //soma todos os infectados da cidade 1
        PreparedStatement SUM_infe_c1 = connection.prepareStatement
        ("SELECT SUM(infectados) FROM casos where id_cidade = ? and dt_caso >= ? and dt_caso <= ?");//Esta linha está preparando uma consulta SQL para ser enviada ao banco de dados
        SUM_infe_c1.setInt(1, valor1);// Aqui, o valor real está sendo definido para o espaço reservado ? na consulta SQL.
        SUM_infe_c1.setDate(2,sqlDate1);
        SUM_infe_c1.setDate(3,sqlDate2);
        ResultSet resultSUM_infe_c1 = SUM_infe_c1.executeQuery();
        
        int SUM_infe_Value_c1 = 0; // Valor padrão caso a consulta não retorne resultados
        if (resultSUM_infe_c1.next()) {// Verifica se há linhas no ResultSet
         SUM_infe_Value_c1 = resultSUM_infe_c1.getInt(1); // Obtém o valor da primeira coluna do resultado da consulta
        }
        
        //soma todos os obitos da cidade 1
        PreparedStatement SUM_obi_c1 = connection.prepareStatement
        ("SELECT SUM(obitos) FROM casos where id_cidade = ? and dt_caso >= ? and dt_caso <= ?");
        SUM_obi_c1.setInt(1,valor1);
        SUM_obi_c1.setDate(2,sqlDate1);
        SUM_obi_c1.setDate(3,sqlDate2);
        ResultSet resultSUM_obi_c1 = SUM_obi_c1.executeQuery();
        int SUM_obi_Value_c1 = 0; // Valor padrão caso a consulta não retorne resultados
        // Verifica se há linhas no ResultSet
        if (resultSUM_obi_c1.next()) {
         SUM_obi_Value_c1 = resultSUM_obi_c1.getInt(1); // Obtém o valor da primeira coluna do resultado da consulta
        }
        
        //soma todos os curados cidade 1
        PreparedStatement SUM_cura_c1 = connection.prepareStatement
        ("SELECT SUM(curados) FROM casos where id_cidade = ? and dt_caso >= ? and dt_caso <= ?");
        SUM_cura_c1.setInt(1,valor1);
        SUM_cura_c1.setDate(2,sqlDate1);
        SUM_cura_c1.setDate(3,sqlDate2);
        ResultSet resultSUM_cura_c1 = SUM_cura_c1.executeQuery();
        int SUM_cura_Value_c1 = 0; // Valor padrão caso a consulta não retorne resultados
        // Verifica se há linhas no ResultSet
        if (resultSUM_cura_c1.next()) {
         SUM_cura_Value_c1 = resultSUM_cura_c1.getInt(1); // Obtém o valor da primeira coluna do resultado da consulta
        }
           
        //soma todos os infectados da cidade 2
        PreparedStatement SUM_infe_c2 = connection.prepareStatement
        ("SELECT SUM(infectados) FROM casos where id_cidade = ? and dt_caso >= ? and dt_caso <= ?");//Esta linha está preparando uma consulta SQL para ser enviada ao banco de dados
        SUM_infe_c2.setInt(1, valor2);// Aqui, o valor real está sendo definido para o espaço reservado ? na consulta SQL.
        SUM_infe_c2.setDate(2,sqlDate1);
        SUM_infe_c2.setDate(3,sqlDate2);
        ResultSet resultSUM_infe_c2 = SUM_infe_c2.executeQuery();
        int SUM_infe_Value_c2 = 0; // Valor padrão caso a consulta não retorne resultados
        if (resultSUM_infe_c2.next()) {// Verifica se há linhas no ResultSet
         SUM_infe_Value_c2 = resultSUM_infe_c2.getInt(1); // Obtém o valor da primeira coluna do resultado da consulta
        }
        
        //soma todos os obitos da cidade 2
        PreparedStatement SUM_obi_c2 = connection.prepareStatement
        ("SELECT SUM(obitos) FROM casos where id_cidade = ? and dt_caso >= ? and dt_caso <= ?");
        SUM_obi_c2.setInt(1,valor2);
        SUM_obi_c2.setDate(2,sqlDate1);
        SUM_obi_c2.setDate(3,sqlDate2);
        ResultSet resultSUM_obi_c2 = SUM_obi_c2.executeQuery();
        int SUM_obi_Value_c2 = 0; // Valor padrão caso a consulta não retorne resultados
        // Verifica se há linhas no ResultSet
        if (resultSUM_obi_c2.next()) {
         SUM_obi_Value_c2 = resultSUM_obi_c2.getInt(1); // Obtém o valor da primeira coluna do resultado da consulta
        }
        
        //soma todos os curados cidade 2
        PreparedStatement SUM_cura_c2 = connection.prepareStatement
        ("SELECT SUM(curados) FROM casos where id_cidade = ? and dt_caso >= ? and dt_caso <= ?");
        SUM_cura_c2.setInt(1,valor2);
        SUM_cura_c2.setDate(2,sqlDate1);
        SUM_cura_c2.setDate(3,sqlDate2);
        ResultSet resultSUM_cura_c2 = SUM_cura_c2.executeQuery();
        int SUM_cura_Value_c2 = 0; // Valor padrão caso a consulta não retorne resultados
        // Verifica se há linhas no ResultSet
        if (resultSUM_cura_c2.next()) {
         SUM_cura_Value_c2 = resultSUM_cura_c2.getInt(1); // Obtém o valor da primeira coluna do resultado da consulta
        }
        
        
        
        
        infoText.setText("");// limpa o infoText1
        infoText.append("  Resutados de comparação das cidade de "+ cidades[valor1-1] +" e "
                + cidades[valor2-1] +" no periodo de "+dateString1 +" a "+dateString2);
        
                
        // Obtém o gráfico e o dataset
        JFreeChart chart = chartPanel.getChart();
        DefaultCategoryDataset dataset = (DefaultCategoryDataset) chart.getCategoryPlot().getDataset();

        // Limpa o dataset
        dataset.clear();

        // Adiciona os novos dados ao dataset
        dataset.addValue(SUM_infe_Value_c1, "Infectados de " + cidades[valor1-1], "Infectados");
        dataset.addValue(SUM_infe_Value_c2, "Infectados de " + cidades[valor2-1], "Infectados");
        dataset.addValue(SUM_obi_Value_c1, "Óbitos de " + cidades[valor1-1], "Óbitos");
        dataset.addValue(SUM_obi_Value_c2, "Óbitos de " + cidades[valor2-1], "Óbitos");
        dataset.addValue(SUM_cura_Value_c1, "Curados de " + cidades[valor1-1], "Curados");
        dataset.addValue(SUM_cura_Value_c2, "Curados de " + cidades[valor2-1], "Curados");
        
    

        // Configura o renderer para mostrar os valores nas barras
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setDefaultItemLabelsVisible(true);
        
        /*
        // Define as cores das barras
        renderer.setSeriesPaint(0, Color.decode("#ff0000")); // Cor da barra "Infectados" + cidades[valor1-1]
        renderer.setSeriesPaint(1, Color.decode("#0000ff")); // Cor da barra "Infectados" + cidades[valor2-1]
        renderer.setSeriesPaint(2, Color.decode("#ff0000")); // Cor da barra "Óbitos" + cidades[valor1-1]
        renderer.setSeriesPaint(3, Color.decode("#0000ff")); // Cor da barra "Óbitos" + cidades[valor2-1]
        renderer.setSeriesPaint(4, Color.decode("#ff0000")); // Cor da barra "Curados" + cidades[valor1-1]
        renderer.setSeriesPaint(5, Color.decode("#0000ff")); // Cor da barra "Curados" + cidades[valor2-1]*/

        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setDefaultItemLabelsVisible(true);
        
        statement1.close();
    }
    
    //Visualizar dados de todas cidades
    private void viewData_todas_cidades()throws SQLException{
        
        int i;
              
        Statement statement1 = connection.createStatement();
        
        var selectedDate1 = dateChooserD.getDate();// pega a data
        var selectedDate2 = dateChooserP.getDate();// pega a data

        // Para formatar a data e convertê-la para uma string
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString1 = sdf.format(selectedDate1);
        String dateString2 = sdf.format(selectedDate2);
        
        // Para formatar a data no padrao SQL para seu usado
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        LocalDate localDate = LocalDate.parse(dateString1, formatter);
        java.sql.Date sqlDate1 = java.sql.Date.valueOf(localDate);
        
        localDate = LocalDate.parse(dateString2, formatter);
        java.sql.Date sqlDate2 = java.sql.Date.valueOf(localDate);
        
        //soma todos os infectados
        PreparedStatement SUM_infe = connection.prepareStatement
        ("SELECT SUM(infectados) FROM casos where dt_caso >= ? and dt_caso <= ?");
        SUM_infe.setDate(1,sqlDate1);
        SUM_infe.setDate(2,sqlDate2);
        ResultSet resultSUM_infe = SUM_infe.executeQuery();
        int SUM_infe_Value = 0; // Valor padrão caso a consulta não retorne resultados
        // Verifica se há linhas no ResultSet
        if (resultSUM_infe.next()) {
         SUM_infe_Value = resultSUM_infe.getInt(1); // Obtém o valor da primeira coluna do resultado da consulta
        }
        
        //soma todos os obitos
        PreparedStatement SUM_obi = connection.prepareStatement
        ("SELECT SUM(obitos) FROM casos where dt_caso >= ? and dt_caso <= ?");
        SUM_obi.setDate(1,sqlDate1);
        SUM_obi.setDate(2,sqlDate2);
        ResultSet resultSUM_obi = SUM_obi.executeQuery();
        int SUM_obi_Value = 0; // Valor padrão caso a consulta não retorne resultados
        // Verifica se há linhas no ResultSet
        if (resultSUM_obi.next()) {
         SUM_obi_Value = resultSUM_obi.getInt(1); // Obtém o valor da primeira coluna do resultado da consulta
        }
        
        //soma todos os curados
        PreparedStatement SUM_cura = connection.prepareStatement
        ("SELECT SUM(curados) FROM casos where dt_caso >= ? and dt_caso <= ?");
        SUM_cura.setDate(1,sqlDate1);
        SUM_cura.setDate(2,sqlDate2);
        ResultSet resultSUM_cura = SUM_cura.executeQuery();
        int SUM_cura_Value = 0; // Valor padrão caso a consulta não retorne resultados
        // Verifica se há linhas no ResultSet
        if (resultSUM_cura.next()) {
         SUM_cura_Value = resultSUM_cura.getInt(1); // Obtém o valor da primeira coluna do resultado da consulta
        }

        
        
        infoText.setText("");// limpa o infoText1
        infoText.append("  Resutados de todas as cidades no periodo de "+dateString1 +" a "+dateString2);
        
        // Obtém o gráfico e o dataset
        JFreeChart chart = chartPanel.getChart();
        DefaultCategoryDataset dataset = (DefaultCategoryDataset) chart.getCategoryPlot().getDataset();

        // Limpa o dataset
        dataset.clear();

        // Adiciona os novos dados ao dataset
        dataset.addValue(SUM_infe_Value, "total de Infectados", "Infectados");
        dataset.addValue(SUM_obi_Value, "total de Óbitos", "Óbitos");
        dataset.addValue(SUM_cura_Value, "total de Curados", "Curados");
        
        for(i=1; i<=5; i++){
            PreparedStatement c_infectados = connection.prepareStatement
        ("SELECT SUM(infectados) FROM casos where id_cidade = ? and (dt_caso >= ? and dt_caso <= ?)");
            c_infectados.setInt(1,i);
            c_infectados.setDate(2,sqlDate1);
            c_infectados.setDate(3,sqlDate2);
            ResultSet result_c_infectados = c_infectados.executeQuery();
            int result_c_infectados_v = 0; // Valor padrão caso a consulta não retorne resultados
            // Verifica se há linhas no ResultSet
            if (result_c_infectados.next()) {
             result_c_infectados_v = result_c_infectados.getInt(1); // Obtém o valor da primeira coluna do resultado da consulta
            }  
            dataset.addValue(result_c_infectados_v, "infectados de " + cidades[i-1], "Infectados");
        
            PreparedStatement c_obitos = connection.prepareStatement
        ("SELECT SUM(obitos) FROM casos where id_cidade = ? and (dt_caso >= ? and dt_caso <= ?)");
            c_obitos.setInt(1,i);
            c_obitos.setDate(2,sqlDate1);
            c_obitos.setDate(3,sqlDate2);
            ResultSet result_c_obitos = c_obitos.executeQuery();
            int result_c_obitos_v = 0; // Valor padrão caso a consulta não retorne resultados
            // Verifica se há linhas no ResultSet
            if (result_c_obitos.next()) {
             result_c_obitos_v = result_c_obitos.getInt(1); // Obtém o valor da primeira coluna do resultado da consulta
            }  
            
            dataset.addValue(result_c_obitos_v, "Óbitos de " + cidades[i-1], "Óbitos");
        
            PreparedStatement c_cura = connection.prepareStatement
        ("SELECT SUM(curados) FROM casos where id_cidade = ? and (dt_caso >= ? and dt_caso <= ?)");
            c_cura.setInt(1,i);
            c_cura.setDate(2,sqlDate1);
            c_cura.setDate(3,sqlDate2);
            ResultSet result_c_cura = c_cura.executeQuery();
            int result_c_cura_v = 0; // Valor padrão caso a consulta não retorne resultados
            // Verifica se há linhas no ResultSet
            if (result_c_cura.next()) {
             result_c_cura_v = result_c_cura.getInt(1); // Obtém o valor da primeira coluna do resultado da consulta
            }  
            
            dataset.addValue(result_c_cura_v, "curados de " + cidades[i-1], "Curados");
            
        }
        

        // Configura o renderer para mostrar os valores nas barras
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setDefaultItemLabelsVisible(true);
        
        
        statement1.close();
        
    }

    // Inserir casos
    private void insertData() throws SQLException {
        
        //escolher a cidade 
        int escolha = JOptionPane.showOptionDialog(this, "Escolha a cidade:", "cidade", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, cidades, cidades[0]);
         
        //verificar se a celeção foi valida
        if (escolha != -1 ){
            //pergunta a data
            String dataString = JOptionPane.showInputDialog("Digite a data no formato dd/MM/yyyy");
            
            // Converte a data digitada em String para um objeto LocalDate
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            boolean dataValida = true;
            LocalDate localDate = null;
            
            //verifica se a data digitada e valida
            try {
                localDate = LocalDate.parse(dataString, formatter);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(null, "Data inválida. Por favor, insira a data no formato dd/MM/yyyy.");
                dataValida = false;
            }
            if (dataValida){
                //cria a variavel da tada para ser colocada no banco 
                java.sql.Date data = java.sql.Date.valueOf(localDate);
                
                String info1 = JOptionPane.showInputDialog("entre com o numero de infectados");
                if(info1.equals (""))
                    info1=null;   
                               
                String info2 = JOptionPane.showInputDialog("entre com o numero de obitos");
                if(info2.equals (""))
                    info2=null;
                
                String info3 = JOptionPane.showInputDialog("entre com o numero de curados");  
                if(info3.equals (""))
                    info1=null;

                    PreparedStatement statement = connection.prepareStatement("INSERT INTO casos (id_cidade, dt_caso, infectados, obitos, curados) VALUES (?, ?, ?, ?, ?)");
                    statement.setInt(1, escolha+1);
                    statement.setDate(2, data);
                    statement.setString(3, info1);
                    statement.setString(4, info2);
                    statement.setString(5, info3);
                    statement.executeUpdate();
                    statement.close();

            }
        }else {
            JOptionPane.showMessageDialog(this, "Cidade não selecionada", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Atualizar casos
    private void updateData() throws SQLException {
     
     int escolha;
     String cidade = null;
     String dataString = null;
     String  infectados = null;
     String  obitos = null;
     String  curados = null;
     boolean dataValida = true;
     boolean cidadeValida=true;
     LocalDate localDate = null;
     java.sql.Date sqlDate = null;
     
     String Sid = JOptionPane.showInputDialog("Qual o Numero do caso a ser modificado");
    
    if (Sid != null){
        int id = Integer.parseInt(Sid);

        // Verifica se o ID existe antes de atualizar
        PreparedStatement checkStatement = connection.prepareStatement("SELECT COUNT(*) FROM casos WHERE id_casos = ?");
        checkStatement.setInt(1, id);
        ResultSet resultSet = checkStatement.executeQuery();
        resultSet.next(); // Move para a primeira linha do resultado
        int rowCount = resultSet.getInt(1); // Obtém o número de linhas retornadas pela consulta

        if (rowCount == 0) {
            // Se o número de linhas for zero, significa que o ID não existe
            JOptionPane.showMessageDialog(this, "O Numero do caso especificado não existe.", "Erro", 
                    JOptionPane.ERROR_MESSAGE);
        } else {
            // ID existe, então procede com a atualização
            // Pergunta ao usuário quais campos ele deseja atualizar
            while(cidadeValida){
                escolha = JOptionPane.showConfirmDialog(this, "Deseja atualizar a cidade?", 
                        "Atualizar Cidade", JOptionPane.YES_NO_OPTION);
                if (escolha == JOptionPane.YES_OPTION) {
                    int escolhaCidade = JOptionPane.showOptionDialog(this, "Escolha a cidade:", "Cidade", 
                            JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, cidades, cidades[0]);
                    if (escolhaCidade!=-1){
                    cidade = String.valueOf(escolhaCidade+1);
                    cidadeValida = false;
                    }                    

                }else{
                    cidadeValida = false;
                }
            }
            
            escolha = JOptionPane.showConfirmDialog(this, "Deseja atualizar a data da ocorencia?", 
                    "Atualizar data da ocorencia", JOptionPane.YES_NO_OPTION);
            if (escolha == JOptionPane.YES_OPTION) {               
                while(dataValida){
                    dataString = JOptionPane.showInputDialog("Entre com a nova data");
                    if (dataString == null)
                    break;                   
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                    //verifica se a data digitada e valida
                    try {
                        localDate = LocalDate.parse(dataString, formatter);
                        sqlDate = java.sql.Date.valueOf(localDate);
                        dataValida = false;
                    } catch (DateTimeParseException e) {
                        JOptionPane.showMessageDialog(null, "Data inválida. Por favor, insira a data no formato dd/MM/yyyy.");
                                          
                    }
            }
            }

            escolha = JOptionPane.showConfirmDialog(this, "Deseja atualizar o numero de infectados?", 
                    "Atualizar infectados", JOptionPane.YES_NO_OPTION);
            if (escolha == JOptionPane.YES_OPTION) {
                infectados = JOptionPane.showInputDialog("Entre com o novo numero de infectados");
            }
            
            escolha = JOptionPane.showConfirmDialog(this, "Deseja atualizar o numero de obitos?", 
                    "Atualizar obitos", JOptionPane.YES_NO_OPTION);
            if (escolha == JOptionPane.YES_OPTION) {
                obitos = JOptionPane.showInputDialog("Entre com o novo numero de obitos");
            }

            escolha = JOptionPane.showConfirmDialog(this, "Deseja atualizar o numero de curados?", 
                    "Atualizar curados", JOptionPane.YES_NO_OPTION);
            if (escolha == JOptionPane.YES_OPTION) {
                curados = JOptionPane.showInputDialog("Entre com o novo numero de curados");
            }            
            
            
            // Atualiza apenas os campos que o usuário decidiu mudar
            PreparedStatement statement = connection.prepareStatement
        ("UPDATE casos SET id_cidade = COALESCE(?, id_cidade), dt_caso = COALESCE(?, dt_caso), "
                + "infectados = COALESCE(?, infectados), obitos = COALESCE(?, obitos),"
                + " curados = COALESCE(?, curados) WHERE id_casos = ?");
            statement.setString(1, cidade);                         
            statement.setDate(2, sqlDate);
            statement.setString(3, infectados);
            statement.setString(4, obitos);
            statement.setString(5, curados);
            statement.setInt(6, id);
            statement.executeUpdate();
            statement.close();
        }

        checkStatement.close();
    } else {
       JOptionPane.showMessageDialog(this, "Erro: ID da linha não pode ficar vazio."); 
    } 

}

    // Excluir casos
    private void deleteData() throws SQLException {
        String key = JOptionPane.showInputDialog("Qual o numero de caso numero da linha a ser deletada?");
        if (key != null) {
            // Verifica se o ID existe antes de excluir
            PreparedStatement checkStatement = connection.prepareStatement("SELECT COUNT(*) FROM casos WHERE id_casos = ?");
            checkStatement.setString(1, key);
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next(); // Move para a primeira linha do resultado
            int rowCount = resultSet.getInt(1); // Obtém o número de linhas retornadas pela consulta

            if (rowCount == 0) {
                // Se o número de linhas for zero, significa que o ID não existe
                JOptionPane.showMessageDialog(this, "O ID especificado não existe.", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                // ID existe, então procede com a exclusão
                int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza de que deseja excluir o caso numero " + key + "?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM casos WHERE id_casos = ?");
                    deleteStatement.setString(1, key);
                    deleteStatement.executeUpdate();
                    deleteStatement.close();
                }
            }

            checkStatement.close();
        }
    }
    
    // Voutar para o menu
    private void voltar() throws SQLException{
       connection.close();
       this.dispose();
       Menu cad = new Menu();
       cad.setVisible(true);  
        }
    
    //configuração para celeção de data
    private void def_data(){
    // Adicionando o seletor de data
        dateChooserD = new JDateChooser();
        dateChooserP = new JDateChooser();
    
        // Definindo o limite de seleção
        Calendar calendarI = Calendar.getInstance();
        Calendar calendarF = Calendar.getInstance();
        
        // Definindo a data mínima como 1 de janeiro de 2023
        calendarI.set(Calendar.YEAR, 2023);
        calendarI.set(Calendar.MONTH, Calendar.JANUARY);
        calendarI.set(Calendar.DAY_OF_MONTH, 1);
        dateChooserD.setMinSelectableDate(calendarI.getTime());
        dateChooserP.setMinSelectableDate(calendarI.getTime());

        // Definindo a data máxima como 31 de dezembro de 2024
        calendarF.set(Calendar.YEAR, 2024);
        calendarF.set(Calendar.MONTH, Calendar.DECEMBER);
        calendarF.set(Calendar.DAY_OF_MONTH, 31);
        dateChooserD.setMaxSelectableDate(calendarF.getTime());
        dateChooserP.setMaxSelectableDate(calendarF.getTime());
        
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set(Calendar.YEAR, 2023);
        calendarStart.set(Calendar.MONTH, Calendar.DECEMBER);
        calendarStart.set(Calendar.DAY_OF_MONTH, 31);
        
        dateChooserP.setDate(calendarStart.getTime());
        dateChooserD.setDate(calendarI.getTime());
        
    }

    // funções dos botoes 
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == viewButton) {
                viewData();
            } else if (e.getSource() == insertButton) {
                insertData();
            } else if (e.getSource() == updateButton) {
                updateData();
            } else if (e.getSource() == deleteButton) {
                deleteData();
            }else if (e.getSource() == voltarButton) {
                voltar();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    
    
}