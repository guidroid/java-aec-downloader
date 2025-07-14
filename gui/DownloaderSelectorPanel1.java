package ufc.deha.ufc9.dolphin.downloader.gui;

import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Box;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.SwingConstants;

import java.io.File;

import ufc.deha.ufc9.dolphin.DolphinUtils;
import ufc.deha.ufc9.dolphin.Main;
import ufc.deha.ufc9.dolphin.banco.MetaData;
import ufc.deha.ufc9.dolphin.referencia.Estados;
import ufc.deha.ufc9.dolphin.referencia.TabelaDeReferencia;

/**
 * 
 * v1.0: Download apenas (SINAPI)
 * v1.1: Atualizadas as pastas para download e adicionada a op��o de atualiza��o do banco por uma pasta selecionada.
 * v1.2: 
 * 	- Os paineis de SINAPI e SABESP s� s�o mostrados quando � fornecido o estado adequado.
 * 	- Al�m disso, removeu-se o campo de Tabela de Refer�ncia j� que este n�o era utilizado pela classe.
 * @since 1.2 (Dolphin)
 * @version 1.1
 * @author Guilherme Ribeiro
 *
 */
public class DownloaderSelectorPanel1 extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Label da data do banco de dados SINAPI
	 */
	private JLabel sinapiDataBanco = null;
	/**
	 * Label da data do banco de dados SABESP
	 */
	private JLabel sabespDataBanco = null;
	/**
	 * Tabela de refer�ncia.
	 */
	//TabelaDeReferencia referencia = TabelaDeReferencia.SINAPI;
	/**
	 * Estado de refer�ncia.
	 */
	Estados estado = Estados.SP; 
	
	
	public static void main(String[]  args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrame frame = new JFrame();
					frame.add(new DownloaderSelectorPanel1(Estados.CE));
					frame.setVisible(true);
				} catch (Exception e) {
					//e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Inicializa o painel.
	 */
	public DownloaderSelectorPanel1(String estado) {
		//this.referencia = TabelaDeReferencia.valueOf(referencia.toUpperCase());
		this.estado = Estados.getFromSiglaOrName(estado);
		createPanel();
	}
	
	/**
	 * Inicializa o painel.
	 */
	public DownloaderSelectorPanel1(Estados estado) {
		//this.referencia = referencia;
		this.estado = estado;
		createPanel();
	}
	/**
	 * Cria o painel.
	 */
	private void createPanel() {
		setLayout(new GridLayout(2, 1, 0, 5));
		
		/**
		 * Sinapi panel: apenas se o estado for diferente de NULL.
		 */
		if (estado != Estados.NULL) {
			Box sinapiPanel = Box.createVerticalBox();
			add(sinapiPanel);
	
			JPanel sinapiTituloPanel = new JPanel();
			FlowLayout fl_sinapiTituloPanel = (FlowLayout) sinapiTituloPanel.getLayout();
			fl_sinapiTituloPanel.setAlignment(FlowLayout.LEFT);
			sinapiPanel.add(sinapiTituloPanel);
			
			JPanel sinapiData = new JPanel();
			FlowLayout fl_sinapiData = (FlowLayout) sinapiData.getLayout();
			fl_sinapiData.setAlignment(FlowLayout.LEFT);
			sinapiPanel.add(sinapiData);
			
			JLabel sinapiDataLabel = new JLabel("�ltima atualiza��o: ");
			sinapiDataLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
			sinapiData.add(sinapiDataLabel);
			
			sinapiDataBanco = new JLabel("");
			sinapiData.add(sinapiDataBanco);
			
			JPanel sinapiButtons = new JPanel();
			sinapiPanel.add(sinapiButtons);
			sinapiButtons.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
					
			/**
			 * Bot�o de sele��o para arquivos da Tabela SINAPI.
			 */
			JButton sinapiSelectButton = new JButton("Selecionar arquivos...");
			sinapiButtons.add(sinapiSelectButton);
			sinapiSelectButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg){
					try{
						String folder = DolphinUtils.browseDirectory(new File(estado.getSinapiDownloadEndereco()));
						Main.updateBanco(folder, TabelaDeReferencia.SABESP, estado);
						updateLabels();
					}catch(IOException e) { }
				}
			});
			/**
			 * Bot�o de download (dispon�vel apenas para SINAPI).
			 */
			JButton sinapiDownloadButton = new JButton("Baixar atualiza��o");
			sinapiButtons.add(sinapiDownloadButton);
			sinapiDownloadButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg){
					Main.updateBanco(TabelaDeReferencia.SINAPI, estado);
					updateLabels();
					}
			});
			
			JLabel sinapiTituloLabel = new JLabel("Banco SINAPI");
			sinapiTituloLabel.setHorizontalAlignment(SwingConstants.LEFT);
			sinapiTituloLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
			sinapiTituloPanel.add(sinapiTituloLabel);
		}
		
		/**
		 * Sabep panel carregado apenas se o estado for S�o Paulo.
		 */
		if (estado == Estados.SP) {
			Box sabespPanel = Box.createVerticalBox();
			add(sabespPanel);
			
			JPanel sabespTituloPanel = new JPanel();
			FlowLayout fl_sabespTituloPanel = (FlowLayout) sabespTituloPanel.getLayout();
			fl_sabespTituloPanel.setAlignment(FlowLayout.LEFT);
			sabespPanel.add(sabespTituloPanel);
			
			JPanel sabespData = new JPanel();
			FlowLayout fl_sabespData = (FlowLayout) sabespData.getLayout();
			fl_sabespData.setAlignment(FlowLayout.LEFT);
			sabespPanel.add(sabespData);
			
			JLabel sabespTituloLabel = new JLabel("Banco SABESP");
			sabespTituloLabel.setHorizontalAlignment(SwingConstants.LEFT);
			sabespTituloLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
			sabespTituloPanel.add(sabespTituloLabel);
			
			JLabel sabespDataLabel = new JLabel("�ltima atualiza��o: ");
			sabespDataLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
			sabespData.add(sabespDataLabel);
			
			sabespDataBanco = new JLabel("");
			sabespData.add(sabespDataBanco);
			JPanel sabespButtons = new JPanel();
			sabespPanel.add(sabespButtons);
			sabespButtons.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
	
			/**
			 * Bot�o de sele��o de arquivos da Tabela Sabesp.
			 */
			JButton sabespSelectButton = new JButton("Selecionar arquivos...");
			sabespButtons.add(sabespSelectButton);
			sabespSelectButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg){
					try{
						String folder = DolphinUtils.browseDirectory(new File(estado.getSabespDownloadEndereco()));
						Main.updateBanco(folder, TabelaDeReferencia.SABESP, estado);
						updateLabels();
					}catch(IOException e) { }
				}
			});
		}
		updateLabels();
	}
	
	private void updateLabels() {
		if (sinapiDataBanco != null) sinapiDataBanco.setText(MetaData.getFormattedData(estado.getSinapiParentPath()));
		if (sabespDataBanco != null) sabespDataBanco.setText(MetaData.getFormattedData(estado.getSabespParentPath()));	
	}
}
