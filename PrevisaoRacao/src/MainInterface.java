import java.awt.BorderLayout;
import java.sql.*;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;

public class MainInterface extends JFrame{
	
	private static final String DB_NAME="jdbc:sqlite:Banco.db";
	private static final String TABLE_DADOS="TabelaDados";
	private static String MAX_LINHAS_QUERY = "10";
	private static final int QUANTIDADE_AUMENTO_PRODUCAO = 5000;
	private static final int BONUS_PRODUCAO_5_TON = 1;
	private static final int MEDIA_PADRAO_RACAO = 15000;
	private static final int COLUNA_DATA = 0;
	private static final int COLUNA_RACAO = 1;
	private static final int COLUNA_PREMIX = 2;
	private static final int COLUNA_LOTE = 3;
	private static final int COLUNA_QUANTIDADE = 4;
	private static final int COLUNA_INICIO = 5;
	private static final int COLUNA_FIM = 6;
	private static final int COLUNA_MEDIA = 7;
	private static final int COLUNA_FARELO = 8;
	private static final int COLUNA_TEMPERATURA = 9;
	private static final int COLUNA_PRODMED = 10;
	private static final int COLUNA_TERMINO = 11;
	
	JCheckBox checkboxPremix;
	JCheckBox checkboxFarelo;
	JTextField TFlimiteLinhas;
	JTextField TFMediaPadrao;
	JTextField TFbonusProducao;
	
	public MainInterface() {
	setSize (1200,600);
	setResizable(true);
	setTitle("Calculadora Previsao");
	
	JPanel panel = new JPanel();
	panel.setLayout(new BorderLayout());	
	JToolBar panelNorte = new JToolBar();	
	panelNorte.setFloatable(false);
	
	JPanel panelData = new JPanel();
	JLabel labelData = new JLabel("Data");
	panelData.add(labelData);
	String format = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
	JFormattedTextField textData = new JFormattedTextField(format);
	textData.setPreferredSize(new Dimension(80,20));
	textData.setMinimumSize(textData.getPreferredSize());	
	panelData.setPreferredSize(new Dimension(150,25));
	panelData.add(textData);
	panelNorte.add(panelData);
	panelNorte.addSeparator();  
	
	JPanel panelLote = new JPanel();
	JLabel labelLote = new JLabel("Lote");
	panelLote.add(labelLote);
	JTextField textLote = new JTextField(7);
	textLote.setText("01/"+textData.getText().replace("/",""));
	textLote.setMinimumSize(textLote.getPreferredSize());
	panelLote.add(textLote);	
	panelLote.setPreferredSize(new Dimension(150,25));
	panelNorte.add(panelLote);
	panelNorte.addSeparator();  
	
	JPanel panelQuantidade = new JPanel();
	JLabel labelQuantidade = new JLabel("Quantidade");
	panelQuantidade.add(labelQuantidade);
	JTextField textQuantidade = new JTextField(7);
	textQuantidade.setMinimumSize(textQuantidade.getPreferredSize());
	panelQuantidade.add(textQuantidade);
	panelQuantidade.setPreferredSize(new Dimension(160,25));
	panelNorte.add(panelQuantidade);
	panelNorte.addSeparator();  
	
	JPanel panelRacao = new JPanel();
	JLabel labelRacao = new JLabel("Tipo Ração");
	panelRacao.add(labelRacao);
	JComboBox<String> comboTipoRacao = new JComboBox<String>();
	comboTipoRacao.setEditable(true);
	fillComboBox(comboTipoRacao, "RACAO");
	panelRacao.add(comboTipoRacao);
	panelRacao.setPreferredSize(new Dimension(210,30));
	panelRacao.setMinimumSize(panelRacao.getPreferredSize());
	panelNorte.add(panelRacao);
	panelNorte.addSeparator();  
	
	JPanel panelPremix = new JPanel();	
	JLabel labelPremix = new JLabel("Premix");
	panelPremix.add(labelPremix);
	JComboBox<String> comboTipoPremix = new JComboBox<String>();
	comboTipoPremix.setEditable(true);
	fillComboBox(comboTipoPremix, "PREMIX");
	panelPremix.add(comboTipoPremix,BorderLayout.NORTH);
	panelPremix.setPreferredSize(new Dimension(200,30));
	panelPremix.setMinimumSize(panelRacao.getPreferredSize());
	panelNorte.add(panelPremix);
	panelNorte.addSeparator();  
	
	JPanel panelFarelo = new JPanel();
	JLabel labelFarelo = new JLabel("Farelo");
	panelFarelo.add(labelFarelo);
	JComboBox<String> comboTipoFarelo = new JComboBox<String>();
	comboTipoFarelo.setEditable(true);
	fillComboBox(comboTipoFarelo, "FARELO");
	panelFarelo.add(comboTipoFarelo);
	panelFarelo.setPreferredSize(new Dimension(200,30));
	panelFarelo.setMinimumSize(panelRacao.getPreferredSize());
	panelNorte.add(panelFarelo);
	panelNorte.addSeparator();  
	
	JTable tabela = new JTable();
	DefaultTableModel modeloTabela = new DefaultTableModel(
			new Object [][] {},
			new String [] { "Data", "Ração", "Premix", "Lote", "Quantidade","Inicio","Fim","Kg/h","Farelo","Temperatura", "Produção Média", "Término"} 
			);
	tabela.setModel(modeloTabela);

	JScrollPane tabelaPane = new JScrollPane(tabela);
	panel.add(tabelaPane,BorderLayout.CENTER);
	
	JButton botaoAdicionar = new JButton("Adicionar");
	panelNorte.add(botaoAdicionar);
	panel.add(panelNorte,BorderLayout.NORTH);
	botaoAdicionar.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (textData.getText().isEmpty()||textData.getText().trim().equals("")) {
				JOptionPane.showMessageDialog(panel, "Verifique a data.","Erro",JOptionPane.INFORMATION_MESSAGE);
			}else if (comboTipoRacao.getSelectedItem().toString().trim().equals("")) {
				JOptionPane.showMessageDialog(panel, "Verifique o tipo de ração.","Erro",JOptionPane.INFORMATION_MESSAGE);
			}else if(comboTipoPremix.getSelectedItem().toString().trim().equals("")) {
				JOptionPane.showMessageDialog(panel, "Verifique o tipo de Premix.","Erro",JOptionPane.INFORMATION_MESSAGE);
			}else if(textLote.getText().isEmpty()||textLote.getText().trim().equals("")||textLote.getText().toString().length()!=11) {
				JOptionPane.showMessageDialog(panel, "Verifique o Lote.","Erro",JOptionPane.INFORMATION_MESSAGE);
			}else if (textQuantidade.getText().isEmpty()||textQuantidade.getText().trim().equals("")) {
				JOptionPane.showMessageDialog(panel, "Verifique a quantidade.","Erro",JOptionPane.INFORMATION_MESSAGE);
			}else if (comboTipoFarelo.getSelectedItem().toString().trim().equals("")) {
				JOptionPane.showMessageDialog(panel, "Verifique o tipo de farelo.","Erro",JOptionPane.INFORMATION_MESSAGE);
			}else {
				modeloTabela.addRow(new Object[] {
						textData.getText().trim(),
						comboTipoRacao.getSelectedItem().toString().trim(),
						comboTipoPremix.getSelectedItem().toString().trim(),
						textLote.getText().trim(),
						textQuantidade.getText().trim(),
						"",
						"",
						"",
						comboTipoFarelo.getSelectedItem(),
						"",
						"",
						"",
				});
			comboTipoRacao.setSelectedIndex(0);
			comboTipoPremix.setSelectedIndex(0);
			comboTipoFarelo.setSelectedIndex(0);
			textQuantidade.setText("");
			textLote.requestFocus();
			}				
	}
	});
	
	JButton botaoExcluir = new JButton ("Excluir");
	JPanel panelBotaoSouth = new JPanel();
	botaoExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int escolha = JOptionPane.showOptionDialog(panel, "Tem certeza?", "Excluir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (escolha==JOptionPane.YES_OPTION) {
					int linhas[] = tabela.getSelectedRows();
					DefaultTableModel dtm = (DefaultTableModel)tabela.getModel();
					for (int i = (linhas.length-1);i>=0;i--) {
						dtm.removeRow(linhas[i]);
					}
				}				
			}});
	
	JButton botaoAtualizar = new JButton ("Atualizar");
	panelBotaoSouth.add(botaoAtualizar);
	botaoAtualizar.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			for (int i=0;i<modeloTabela.getRowCount();i++) {
				modeloTabela.setValueAt(CalcularMedia(modeloTabela.getValueAt(i, COLUNA_INICIO).toString(),
						modeloTabela.getValueAt(i,COLUNA_FIM).toString(),modeloTabela.getValueAt(i,COLUNA_QUANTIDADE).toString()), i, COLUNA_MEDIA);
				modeloTabela.setValueAt(CalcularProdMed(modeloTabela,i, checkboxPremix.isSelected(),
						checkboxFarelo.isSelected(),Integer.valueOf(TFlimiteLinhas.getText().toString())), i, COLUNA_PRODMED);
				modeloTabela.setValueAt(CalcularTermino(modeloTabela.getValueAt(i, COLUNA_PRODMED).toString(),tabela, i, TFbonusProducao), i, COLUNA_TERMINO);
				fillComboBox(comboTipoRacao, "RACAO");
				fillComboBox(comboTipoFarelo, "FARELO");
				fillComboBox(comboTipoPremix, "PREMIX");
			}
		}
	});	
	
	JButton botaoSalvar = new JButton ("Salvar");
	botaoSalvar.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			Connection c = estabeleceConexao(DB_NAME);
			criaTabela(c, TABLE_DADOS);
			try {				
				for (int i=0;i<tabela.getRowCount();i++) {
					boolean valoresOK = true;
					for (int j=0;j<tabela.getColumnCount()-1;j++) {
						if ((tabela.getValueAt(i, j).toString().trim()).isEmpty()) {
							System.out.println("Linha: "+i+"Coluna: "+j+" null");
							valoresOK = false;
						}
					}
					if(valoresOK) {
						Statement stmt = c.createStatement();
						String sql = "INSERT INTO "+TABLE_DADOS+" (DATA, RACAO, PREMIX, LOTE, QUANTIDADE, INICIO, FIM, KgH, FARELO, TEMPERATURA, PRODMED)"+
										 "VALUES ('"+tabela.getValueAt(i,COLUNA_DATA)+
												"','"+tabela.getValueAt(i,COLUNA_RACAO)+
										        "','"+tabela.getValueAt(i,COLUNA_PREMIX)+
										        "','"+tabela.getValueAt(i,COLUNA_LOTE)+
										        "',"+tabela.getValueAt(i,COLUNA_QUANTIDADE)+
										        ",'"+tabela.getValueAt(i,COLUNA_INICIO)+
										        "','"+tabela.getValueAt(i,COLUNA_FIM)+
										        "','"+tabela.getValueAt(i,COLUNA_MEDIA)+
										        "','"+tabela.getValueAt(i,COLUNA_FARELO)+
										        "','"+tabela.getValueAt(i,COLUNA_TEMPERATURA)+
										        "','"+tabela.getValueAt(i,COLUNA_PRODMED)+"');";
						stmt.executeUpdate(sql);
						stmt.close();
						System.out.println("Dados salvos com sucesso. linha: "+i);
						JOptionPane.showMessageDialog(panel, "Dados salvos com sucesso.","Sucesso",JOptionPane.INFORMATION_MESSAGE);

						DefaultTableModel dtm = (DefaultTableModel)tabela.getModel();				
						dtm.removeRow(i);
						i=i-1;
						valoresOK = true;
					}else {
						JOptionPane.showMessageDialog(panel, "Verifique os dados. linha: "+(i+1),"Erro",JOptionPane.INFORMATION_MESSAGE);
					}
				}			
			}catch (Exception exception) {
				System.out.println(exception.getClass().getName()+" "+exception.getMessage());
			}
			
			encerraConexao(c);
			fillComboBox(comboTipoRacao, "RACAO");
			fillComboBox(comboTipoFarelo, "FARELO");
			fillComboBox(comboTipoPremix, "PREMIX");
		}
	});
	panelBotaoSouth.add(botaoExcluir);
	panelBotaoSouth.add(botaoSalvar);
	panel.add(panelBotaoSouth,BorderLayout.SOUTH);

	checkboxPremix = new JCheckBox("Premix");
	checkboxFarelo = new JCheckBox("Farelo");
	TFlimiteLinhas = new JTextField(5);
	TFlimiteLinhas.setText(MAX_LINHAS_QUERY);
	JLabel JLConsiderar = new JLabel("Considerar: ");
	JLabel JLMax = new JLabel("Max de dados: ");
	JLabel JLMediaPadrao = new JLabel("Média Padrão: ");
	TFMediaPadrao = new JTextField(5);
	TFMediaPadrao.setText(String.valueOf(MEDIA_PADRAO_RACAO));


	JPanel panelBotaoEast = new JPanel();
	panelBotaoEast.setPreferredSize(new Dimension(95,200));
	JButton botaoSubir = new JButton(" Subir ");
	botaoSubir.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			int linhas[] = tabela.getSelectedRows();
			if (linhas.length==1&&linhas[0]>0) {
				
				for (int i=0;i<12;i++) {
					String item1 = tabela.getValueAt(linhas[0], i).toString();
					String item2 = tabela.getValueAt(linhas[0]-1, i).toString();	
					tabela.setValueAt(item1, linhas[0]-1, i);		
					tabela.setValueAt(item2, linhas[0], i);
				}
				tabela.changeSelection(linhas[0]-1, 0, false, false);
			}
		}
	});
	JButton botaoDescer = new JButton("Descer");
	botaoDescer.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			int linhas[] = tabela.getSelectedRows();
			if (linhas.length==1&&linhas[0]<tabela.getRowCount()-1) {
				
				for (int i=0;i<12;i++) {
					String item1 = tabela.getValueAt(linhas[0], i).toString();	
					String item2 = tabela.getValueAt(linhas[0]+1, i).toString();	
					tabela.setValueAt(item1, linhas[0]+1, i);	
					tabela.setValueAt(item2, linhas[0], i);
				}
				tabela.changeSelection(linhas[0]+1, 0, false, false);
			}
		}
	});
	JButton botaoExcluirBD = new JButton("Excluir BD");
	botaoExcluirBD.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			try {
				String nomeColuna = JOptionPane.showInputDialog(panel,"Digite o tipo do dado (COLUNA)");
				if(nomeColuna.trim().length()>0) {
					String nomeDado = JOptionPane.showInputDialog(panel,"Digite o nome do dado (ITEM)");
					if (nomeDado.trim().length()>0) {
						if(excluirDado(nomeDado,nomeColuna)) {
							JOptionPane.showMessageDialog(panel, "Dados excluidos com sucesso","Sucesso!",JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}else {
					JOptionPane.showMessageDialog(panel, "Ocorreu um erro","Erro",JOptionPane.INFORMATION_MESSAGE);
				}
				fillComboBox(comboTipoRacao, "RACAO");
				fillComboBox(comboTipoFarelo, "FARELO");
				fillComboBox(comboTipoPremix, "PREMIX");				
			}catch (Exception ex) {
				System.out.println(e.getClass().getName()+""+ex.getMessage());
			}			
		}
	});
	
	JLabel JLbonusProducao = new JLabel("Bonus %/5k");
	TFbonusProducao = new JTextField(5);
	TFbonusProducao.setText(String.valueOf(BONUS_PRODUCAO_5_TON));
	
	
	panelBotaoEast.add(botaoSubir);
	panelBotaoEast.add(botaoDescer);
	panelBotaoEast.add(JLConsiderar);
	panelBotaoEast.add(checkboxPremix);
	panelBotaoEast.add(checkboxFarelo);
	panelBotaoEast.add(JLMax);
	panelBotaoEast.add(TFlimiteLinhas);
	panelBotaoEast.add(JLMediaPadrao);
	panelBotaoEast.add(TFMediaPadrao);
	panelBotaoEast.add(JLbonusProducao);
	panelBotaoEast.add(TFbonusProducao);
	panelBotaoEast.add(botaoExcluirBD);
	
	
	panel.add(panelBotaoEast, BorderLayout.EAST);
	add(panel);	
	
	
	
	}
	private String CalcularMedia(String horarioInicial, String horarioFinal, String valorQuantidade) {
		double valorTempo=0;
		double valorQuantidadeD = Double.valueOf(valorQuantidade);
		double mediaProducao=0;
		try {
			double valorInicial = Double.valueOf(horarioInicial.replace(":","").trim());
			double valorFinal = Double.valueOf(horarioFinal.replace(":", "").trim());
			int minInicial = (int) (valorInicial%100);
			int horInicial = (int) (valorInicial-minInicial);
			int minFinal = (int) (valorFinal%100);
			int horFinal = (int) (valorFinal-minFinal);
			int minsDiferenca;
			if(minFinal>=minInicial) {
				minsDiferenca = minFinal-minInicial;
			}else {
				minsDiferenca = minFinal-minInicial+60;
				horFinal = horFinal-100;
			}
			int horDiferenca = horFinal-horInicial;
			
			valorTempo = horDiferenca/100*60+minsDiferenca;
			mediaProducao = valorQuantidadeD/valorTempo*60;
		}catch(Exception e) {
			
		}
		DecimalFormat df = new DecimalFormat("#####");
		df.setRoundingMode(RoundingMode.CEILING);
		String horarioTotal = df.format(mediaProducao);
	return horarioTotal;
	}
	
	public static Connection estabeleceConexao(String BD) {
		Connection c=null;
				try {
					Class.forName("org.sqlite.JDBC");
					c = DriverManager.getConnection(BD);
					System.out.println("Conexão criada");
					c.setAutoCommit(true);
					return c;
				}catch (Exception e){
					System.out.println("Falha ao criar BD");
					System.out.println(e.getClass().getName()+" "+e.getMessage());
					return null;
				}
	}
	public static boolean encerraConexao(Connection c) {
		try {
			c.close();
			System.out.println("Conexão encerrada");
			return true;
		}catch (Exception e) {
			System.out.println("Conexão não encerrada");
			System.out.println(e.getClass().getName()+" "+e.getMessage());
			return false;
		}
	}
	public static boolean criaTabela(Connection c, String nomeTabela) {
		Statement stmt = null;
		try {
			stmt = c.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS "+nomeTabela+
					"(ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
					"DATA TEXT NOT NULL, "+
					"RACAO TEXT NOT NULL, "+
					"PREMIX TEXT NOT NULL, "+
					"LOTE TEXT NOT NULL, "+
					"QUANTIDADE INT NOT NULL, "+
					"INICIO TEXT NOT NULL, "+
					"FIM TEXT NOT NULL, "+
					"KgH TEXT NOT NULL, "+
					"FARELO TEXT NOT NULL, "+
					"TEMPERATURA TEXT NOT NULL, "+
					"PRODMED TEXT NOT NULL)";
			stmt.execute(sql);
			stmt.close();
			System.out.println("Tabela criada ou existe");
			return true;
					
		}catch (Exception e) {
			System.out.println(e.getClass().getName()+" "+e.getMessage());
			return false;
		}
	}
	
	
	private String CalcularTermino(String media, JTable tabela, int linha, JTextField bonusProd) {
		double bonusProducao = Double.valueOf(bonusProd.getText());
		double quantidade = Double.valueOf(tabela.getValueAt(linha, 4).toString().trim());
		int quant5k = (int) (quantidade%QUANTIDADE_AUMENTO_PRODUCAO);
		quant5k = (int) (quantidade-quant5k)/QUANTIDADE_AUMENTO_PRODUCAO;
		quantidade = quantidade/100*(100-(bonusProducao*quant5k));
		System.out.println("Quantidadeaaaaaaaaaa"+quantidade+bonusProducao+quant5k);
		String inicio = tabela.getValueAt(linha, 5).toString();
		String fim = tabela.getValueAt(linha, 6).toString();
		if(linha>0&&tabela.getValueAt(linha-1, COLUNA_TERMINO).toString().trim().length()>0) {
			inicio = tabela.getValueAt(linha-1, COLUNA_TERMINO).toString();
		}		
		String valorEstimadoFinal = "";
		if (media.length()>0&&quantidade>0&&inicio.length()>0&&fim.length()==0) {
			inicio = inicio.replace(":","");
			double valorProdMed = Integer.valueOf(media.trim());
			double valorInicioMin = Integer.valueOf(inicio.trim())%100;
			double valorInicioHora = (Integer.valueOf(inicio.trim())-valorInicioMin)/100;
			double valorPrevisao = quantidade/valorProdMed*100;
			double valorMinPrevisao = (valorPrevisao%100);
			double valorHoraPrevisao = (valorPrevisao - valorMinPrevisao);
			valorMinPrevisao = valorMinPrevisao/100*60;
			valorMinPrevisao=valorMinPrevisao+valorInicioMin;
			if(valorMinPrevisao>60) {
				valorHoraPrevisao=valorHoraPrevisao+100;
				valorMinPrevisao=valorMinPrevisao-60;
			}
			valorHoraPrevisao = valorHoraPrevisao/100;
			valorHoraPrevisao=valorHoraPrevisao+valorInicioHora;
			if(valorHoraPrevisao>=24) {
				valorHoraPrevisao=valorHoraPrevisao-24;
			}
			DecimalFormat df = new DecimalFormat("#####");
			df.setRoundingMode(RoundingMode.FLOOR);
			String valorHoraString = df.format(valorHoraPrevisao);
			String valorMinString = df.format(valorMinPrevisao);
			if (valorMinString.length()==1) {
				valorMinString = "0"+valorMinString;
			}		
			
			valorEstimadoFinal=valorHoraString+":"+valorMinString;
		}
		return valorEstimadoFinal;
	}
	

	private String CalcularProdMed(DefaultTableModel tabela, int linha, boolean premix, boolean farelo, int MaxLinhas) {
		String nomeRacao = tabela.getValueAt(linha,COLUNA_RACAO).toString();
		String nomePremix = tabela.getValueAt(linha,COLUNA_PREMIX).toString();
		String nomeFarelo = tabela.getValueAt(linha, COLUNA_FARELO).toString();
		float resultadoProdMed;
		int quantidadeResultados = 0;
		int totalValorKgH = 0;
		String returnValue = "";
		String query = "SELECT * FROM "+TABLE_DADOS+" WHERE RACAO = '"+nomeRacao+"'  ORDER BY ID DESC LIMIT "+MaxLinhas+";" ;
		if (premix^farelo) {
			query = "SELECT * FROM " + TABLE_DADOS+" WHERE RACAO = '"+nomeRacao+"' AND PREMIX = '"+nomePremix+"' ORDER BY ID DESC LIMIT "+MaxLinhas+";";
		}else if (farelo^premix) {
			query = "SELECT * FROM " + TABLE_DADOS+" WHERE RACAO = '"+nomeRacao+"' AND FARELO = '"+nomeFarelo+"' ORDER BY ID DESC LIMIT "+MaxLinhas+";";
		}else if (premix&&farelo) {
			query = "SELECT * FROM " + TABLE_DADOS+" WHERE RACAO = '"+nomeRacao+"' AND FARELO = '"+nomeFarelo+
					"' AND PREMIX = '"+nomePremix+"' ORDER BY ID DESC LIMIT "+MaxLinhas+";";
		}
		try {
			Connection c = estabeleceConexao(DB_NAME);
			criaTabela(c, TABLE_DADOS);
			Statement stmt = c.createStatement();
				ResultSet rs;
				rs = stmt.executeQuery(query);			
			while (rs.next()) {
				quantidadeResultados++;
				totalValorKgH = totalValorKgH+(Integer.valueOf(rs.getString("KgH").trim()));
			}

			encerraConexao(c);
		} catch (SQLException e) {
			System.out.println(e.getClass().getName()+" "+e.getMessage());
		}
		if(quantidadeResultados==0) {	
			resultadoProdMed = Float.valueOf(TFMediaPadrao.getText().toString());

			DecimalFormat df = new DecimalFormat("#####");
			df.setRoundingMode(RoundingMode.CEILING);
			returnValue = df.format(resultadoProdMed);
			JOptionPane.showMessageDialog(null, "Dados insuficientes para esta configuração. \nTipo: "+ tabela.getValueAt(linha, COLUNA_RACAO),
					"Erro",JOptionPane.INFORMATION_MESSAGE);
		}else {
			resultadoProdMed = (totalValorKgH/quantidadeResultados);
			DecimalFormat df = new DecimalFormat("#####");
			df.setRoundingMode(RoundingMode.CEILING);
			returnValue = df.format(resultadoProdMed);
		}
		return returnValue;		
	}
	
	private void fillComboBox(JComboBox<String> comboBox, String filtro) {
		comboBox.removeAllItems();
		comboBox.addItem("");
		String queryTipoFarelo = "SELECT DISTINCT "+filtro+" FROM "+TABLE_DADOS+"  ";
		try {
			Connection c = estabeleceConexao(DB_NAME);
			criaTabela(c, TABLE_DADOS);
			Statement stmt = c.createStatement();
				ResultSet rs;
				rs = stmt.executeQuery(queryTipoFarelo);			
			while (rs.next()) {
				comboBox.addItem(rs.getString(1));
			}
			encerraConexao(c);
		} catch (SQLException e) {
			System.out.println(e.getClass().getName()+" "+e.getMessage());
		}
	}
	
	private boolean excluirDado(String dado, String coluna) {
		try {
			Connection c = estabeleceConexao(DB_NAME);
			String sql = "DELETE from "+TABLE_DADOS+" where "+coluna+"='"+dado+"';";
			Statement stmt = c.createStatement();
			stmt.execute(sql);
			stmt.close();
			encerraConexao(c);
			System.out.println("ItemExcluido");
			return true;
		}catch (SQLException e) {
			System.out.println("SQL: "+e.getClass().getName()+""+e.getMessage());
			return false;
		}
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				MainInterface frame = new MainInterface();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});			
	}	
	
}
