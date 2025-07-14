package ufc.deha.ufc9.dolphin.downloader.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
//import javax.swing.border.EmptyBorder;

import ufc.deha.ufc9.dolphin.DolphinUtils;
import ufc.deha.ufc9.dolphin.referencia.TabelaDeReferencia;
import ufc.deha.ufc9.dolphin.referencia.Estados;

/**
 * Abre uma caixa de di�logo para sele��o de arquivo de tipo de refer�ncia (SINAPI ou SABESP) para baixar.
 * @since 1.0
 * @verions 1.0
 * 
 * @author Guilherme Ribeiro  
 *
 */
public class LinkSelectorGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	
	//private DownloadPanelGUI downloadPanel;

	/**
	 * Lan�a a aplica��o.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LinkSelectorGUI frame = new LinkSelectorGUI(TabelaDeReferencia.SINAPI, Estados.CE);
					frame.setVisible(true);
				} catch (Exception e) {
					//e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LinkSelectorGUI(TabelaDeReferencia fonte, Estados estado) {
		DolphinUtils.setNimbusLookAndFeel();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 450);

		this.setTitle("Estado: " + estado.getEstado() + " (" + estado.getSigla() + ")");
		createFrame(estado);//createFrame(fonte, sigla);
		
	}
	private void createFrame(Estados estado) {
		setContentPane(new DownloaderSelectorPanel1(estado));
	}
	/*
	private void createFrame(TabelaDeReferencia fonte, Estados sigla) {
		downloadPanel = new DownloadPanelGUI();
		downloadPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(downloadPanel);
		
		downloadPanel.setFonte(fonte);
		downloadPanel.setSigla(sigla);
	}*/

}
