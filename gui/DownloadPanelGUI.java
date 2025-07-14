package ufc.deha.ufc9.dolphin.downloader.gui;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.Map;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import ufc.deha.ufc9.dolphin.DolphinUtils;
import ufc.deha.ufc9.dolphin.downloader.Sinapi;
import ufc.deha.ufc9.dolphin.downloader.PaginaWebTabelaDeReferencia;
import ufc.deha.ufc9.dolphin.downloader.utils.DownloaderInBackground;
import ufc.deha.ufc9.dolphin.referencia.TabelaDeReferencia;
import ufc.deha.ufc9.dolphin.referencia.Desoneracao;
import ufc.deha.ufc9.dolphin.referencia.Estados;
 
/**
 * Aplica��o que realiza o download de um arquivo em endere�o da web.
 * 
 * @since 1.0
 * @version 1.0
 * 
 * @author Guilherme Ribeiro  
 */
public class DownloadPanelGUI extends JPanel {//implements  PropertyChangeListener {
private static final long serialVersionUID = 1L;
	
	private JTable table = new JTable();
	private final DefaultTableModel DEFAULT_MODEL = new DefaultTableModel( new Object[][] {}, new String[] {"Item", "Endere�o"});
	
	//private JProgressBar progressBar = new JProgressBar(0, 100);
	
	private JComboBox<String> comboBoxTable = new JComboBox<>();
	private JComboBox<String> comboBoxEstado = new JComboBox<>();
	private JTextField textAddress = new JTextField();
	private JLabel lableEdit = new JLabel("*");
	private JLabel labelProgressData = new JLabel("...");
	
	private JButton butDownload = new JButton("Baixar");
	private JButton butCancel = new JButton("Cancelar");

	private DownloaderInBackground downloader;
	
	public TabelaDeReferencia getFonte() {
		return TabelaDeReferencia.values()[comboBoxTable.getSelectedIndex()];
	}
	public Estados getSigla() {
		return Estados.values()[comboBoxEstado.getSelectedIndex()];
	}
	public void setFonte(TabelaDeReferencia fonte) {
		comboBoxTable.setSelectedItem(fonte);
	}
	public void setSigla(Estados sigla) {
		comboBoxEstado.setSelectedItem(sigla);
	}
     
    /**
     * Constructor
     */
    public DownloadPanelGUI() {
    	
    	createPanel();
    }
    /**
     * Cria o painel
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void createPanel() {
		setLayout(new BorderLayout(5, 5));	
		
		createTable();
		
		//�rea NORTE do painel.
		Box verticalBox = Box.createVerticalBox();
		add(verticalBox, BorderLayout.NORTH);
		
		FlowLayout fl_panelCombo = new FlowLayout();
		fl_panelCombo.setAlignment(FlowLayout.RIGHT);
		JPanel panelCombo = new JPanel(fl_panelCombo);
		
		JLabel labelTable = new JLabel("Tabela");
		labelTable.setLabelFor(comboBoxTable);
		comboBoxTable.setModel(new DefaultComboBoxModel(TabelaDeReferencia.values()));
		comboBoxTable.addItemListener( new ItemListener(){
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					lableEdit.setText("*");
					}
				}
			});
		
		JLabel lableEstado = new JLabel("Estado:");
		comboBoxEstado = new JComboBox<>();
		lableEstado.setLabelFor(comboBoxEstado);
		comboBoxEstado.setModel(new DefaultComboBoxModel(Estados.values()));
		comboBoxEstado.addItemListener( new ItemListener(){
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					lableEdit.setText("*");
					}
				}
			});
		
		JButton butRefresh = new JButton("Obter links...");
		lableEdit = new JLabel("*");
		butRefresh.addActionListener(new RefreshButtonHandler());			
	
		panelCombo.add(labelTable);
		panelCombo.add(comboBoxTable);
		panelCombo.add(lableEstado);
		panelCombo.add(comboBoxEstado);
		panelCombo.add(butRefresh);
		panelCombo.add(lableEdit);
		
		Box horizontalBoxBrowse = Box.createHorizontalBox();
		textAddress = new JTextField("download/");
		
		JLabel browseLabel = new JLabel("Endere�o: ");
		JButton butBrowse = new JButton("Pesquisar...");
		butBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					String retorno = DolphinUtils.browseDirectory();
					textAddress.setText(retorno);
				}catch(IOException e) {}
			}
		});
		
		browseLabel.setLabelFor(textAddress);
		
		horizontalBoxBrowse.add(browseLabel);
		horizontalBoxBrowse.add(textAddress);
		horizontalBoxBrowse.add(butBrowse);		

		verticalBox.add(horizontalBoxBrowse);
		verticalBox.add(panelCombo);
		
		//Painel Sul para barra de progresso e bot�es.
		JPanel panelSouth = new JPanel();
		panelSouth.setBounds(new Rectangle(0, 0, 5, 5));
		add(panelSouth, BorderLayout.SOUTH);
		panelSouth.setLayout(new BorderLayout(0, 0));
		//panelSouth.add(progressBar, BorderLayout.CENTER);
		
		//Painel de bot�es de Baixar e de Cancelar.
		JPanel panelButtons = new JPanel();
		FlowLayout fl_panelButtons = (FlowLayout) panelButtons.getLayout();
		fl_panelButtons.setVgap(0);
		fl_panelButtons.setAlignment(FlowLayout.RIGHT);
		panelButtons.add(butDownload);
		panelButtons.add(butCancel);
		panelSouth.add(panelButtons, BorderLayout.SOUTH);
		
		JPanel panelProgress = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panelProgress.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setAlignment(FlowLayout.LEFT);
		panelSouth.add(panelProgress, BorderLayout.NORTH);
		
		JLabel labelProgress = new JLabel("Progresso:");
		labelProgress.setSize(new Dimension(90, 0));
		panelProgress.add(labelProgress);
		
		labelProgress.setLabelFor(labelProgressData);
		panelProgress.add(labelProgressData);
		
		//Handler para o bot�o de Baixar.
		butDownload.addActionListener(new DownloadButtonHandler());
		
		//Handler para o bot�o Cancelar.
		butCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					downloader.cancel(true);
				}catch(Exception ex) {}
			}
		});
	
	}
    
    /**
     * Handler do bot�o de aualiza��o dos links.
     * @author Guilherme Ribeiro
     *
     */
    private class RefreshButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			TabelaDeReferencia fonte = TabelaDeReferencia.values()[comboBoxTable.getSelectedIndex()];
			Estados estado = Estados.values()[comboBoxEstado.getSelectedIndex()];
			
			fillTable(fonte, estado);
			lableEdit.setText("");
		}
	}
    
    /**
     * Bot�o de download.
     */
    private class DownloadButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			String selectedLink =  table.getValueAt(table.getSelectedRow(), 1).toString();
			String saveFolder = textAddress.getText();
			
			try {
				downloader = new DownloaderInBackground(selectedLink, saveFolder, labelProgressData);
				labelProgressData.setText("");
				
				downloader.execute();	
			}catch(Exception e) { }
		}
    }    

    /**
     * Cria a tipo e a adiciona ao painel
     * <p>
     * Seta as principais propriedades da tipo e seu modelo padr�o.
     */
	private void createTable() {		
		table.setModel(DEFAULT_MODEL);
		
		table.getColumnModel().getColumn(0).setPreferredWidth(400);
		table.getColumnModel().getColumn(1).setPreferredWidth(400);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollTable = new JScrollPane(table);
		add(scrollTable, BorderLayout.CENTER);
		//table.getSelectionModel().addListSelectionListener( new TableRowSelectionHandler());
		}
 
	/**
	 * Preenche a tipo a partir dos valores na ComboBoxes de fonte e de estado.
	 * @param fonte - tipo de refer�ncia a ser usada.
	 * @param estado - estado a ser usado.
	 */
    private void fillTable(TabelaDeReferencia fonte, Estados estado) {
		PaginaWebTabelaDeReferencia page = null;
		try {
			if (fonte == TabelaDeReferencia.SINAPI) {
				page = new Sinapi(estado.getSigla(), Desoneracao.NAO_DESONERADO);
			} else if(fonte == TabelaDeReferencia.SABESP) {
				page = null;
			}

			DefaultTableModel model = DEFAULT_MODEL;
			
			Map<String, String> links = page.getLinksWithTitleIndependentFromStatus(12);

			for(Map.Entry<String, String> link : links.entrySet()) {
				model.addRow(new String[]{link.getValue(), link.getKey()});
			}
			
			table.setModel(model);
		}catch(Exception e) {
			//e.printStackTrace();
		}
	}
	
     
    /**
     * Update the progress Data state whenever the progress of download changes.
     */
    /*@Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("progress")) {
        	int progress = (int) evt.getNewValue();
        	String result = new DecimalFormat("#.##").format( progress / 1000000.0).toString() + "Mb";
            labelProgressData.setText(result);//Value(progress);
        }
    }*/
     
    /**
     * Abre uma janela para escolha de diret�rio.
     * @return - String com o endere�o da pasta selecionada
     * @throws IOException - Caso o bot�o cancelar seja pressionado.
     */
    /*public static String openBrowseDirectory() throws IOException{
		JPanel panel = new JPanel();
		JFileChooser chooser;
		   
	    chooser = new JFileChooser(); 
	    chooser.setCurrentDirectory(new java.io.File("."));
	    chooser.setDialogTitle("UFC9.Dolphin: Selecione um diret�rio");
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	   
	    chooser.setAcceptAllFileFilterUsed(false);
	     
	    if (chooser.showOpenDialog(panel) == JFileChooser.APPROVE_OPTION) {
	    	return chooser.getSelectedFile().getAbsolutePath();
	    } else {
	      throw new IOException();
	    }
	}*/
    
}
