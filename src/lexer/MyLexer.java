package lexer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class MyLexer {

    public static ArrayList<String[]> data = new ArrayList<>();
    public static String column[]={"Lexema","Significado","Token"};

    public static JLabel textErrors;

    public static JTable table;

    public static void main(String[] args) throws IOException {
        JFrame f = new JFrame();

        // TEXTO CÓDIGO
        JTextArea area = new JTextArea("Seu código...");
        JScrollPane areaScroll = new JScrollPane(area);
        areaScroll.setBounds(10,10, 560,100);

        // BOTÃO CARREGAR ARQUIVO
        JButton buttonAddFile = new JButton("Carregar arquivo");
        buttonAddFile.setBounds(10,120,560, 40);
        buttonAddFile.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JFileChooser chooser = new JFileChooser();
                int returnVal = chooser.showOpenDialog(null);
                File file = null;
                if(returnVal == JFileChooser.APPROVE_OPTION)
                    file = chooser.getSelectedFile();

                if(file != null) {
                    BufferedReader in = null;
                    try {
                        area.setText("");
                        in = new BufferedReader(new FileReader(file));
                        String line = in.readLine();
                        while(line != null){
                            area.append(line + "\n");
                            line = in.readLine();
                        }
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        });

        // BOTÃO ANALISAR
        JButton buttonAnalisar = new JButton("Analisar!");
        buttonAnalisar.setBounds(10,170,560, 40);
        buttonAnalisar.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                data.clear();
                String codigo = area.getText();
                codigo = codigo.replace("\n", "").replace("\r", "");
                Reader targetReader = new StringReader(codigo);

                Lex lex = new Lex(targetReader);
                Token token = null;

                DefaultTableModel model = new DefaultTableModel();
                model.setColumnIdentifiers(column);
                String erros = "";
                do {
                    try {
                        token = lex.yylex();
                    } catch (IOException | Error ex) {
                        erros = erros + "\n" + ex.getMessage();
                        ex.printStackTrace();
                    }

                    if(token != null) {
                        String[] str = { token.getLexema(), token.getType().name(), "<" + token.getLexema() + ", " + token.getType() + ">" };
                        data.add(str);
                        model.addRow(str);
                    }
                } while (token != null);

                try {
                    targetReader.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                if(!erros.isEmpty()) {
                    textErrors.setText("Erros encontrados: \n" + erros);
                } else {
                    textErrors.setText("Erros encontrados: Nenhum erro encontrado");
                }
                table.setModel(model);
            }
        });

        // TEXTO ERROS
        textErrors =new JLabel();
        textErrors.setBounds(10,220, 560,20);
        textErrors.setText("Erros encontrados: Nenhum erro encontrado");

        // TABELA LEXEMAS / TOKENS
        String[][] newData = new String[data.size()][3];
        for(int i = 0; i < data.size(); i++) {
            newData[i] = data.get(i);
        }
        table = new JTable(newData, column);
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBounds(10,340,560,300);
        tableScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        f.add(areaScroll);
        f.add(buttonAddFile);
        f.add(buttonAnalisar);
        f.add(textErrors);
        f.add(tableScroll);

        f.setSize(600,700);
        f.setLayout(null);
        f.setVisible(true);
        f.setTitle("Analisador Léxico");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}