package ufc.deha.ufc9.dolphin.downloader.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import ufc.deha.ufc9.dolphin.DolphinUtils;

/**
 * Realiza o download de mais de um link de download.
 * 
 * @since 1.1 (Dolphin)
 * @version 1.0
 * 
 * @author Guilherme Ribeiro
 */
public class MultiDownloaderMain {

	/**
	 * Conjunto de links por onde os arquivos ser�o baixados.
	 */
	private String[] downloadURLs;    
	/**
	 * Endere�o dos arquivos gerados ap�s a opera��o de download.
	 */
    private String[] fileNames = new String[0];
    /**
     * Pasta de salvamento padr�o dos arquivos baixados.
     */
    private String saveDirectory = "C://UFC//orcamento";
     

	/**
	 * Inicializa o objeto de multi-download.
	 * @param downloadURLs
	 * 		Array de links por onde ser�o baixados os arquivos.
	 * @param saveDirectory
	 * 		Pasta onde os arquivos ser�o salvos.
	 */
	public MultiDownloaderMain(String[] downloadURLs, String saveDirectory) {
        this.downloadURLs = downloadURLs;
        this.saveDirectory = saveDirectory;
        //System.out.println("Existe: " + new File(saveDirectory).exists());
        if (!new File(saveDirectory).exists()) {
        	new File(saveDirectory).mkdirs();
        }
    } 
    /**
     * Executa o download.
     * Arquivos comprimidos s�o descomprimidos no mesmo endere�o do download.
     * @throws IOException 
     * 		Caso n�o seja poss�vle criar o arquivo que seria baixado.
     */
    public String[] download() throws IOException {
    	CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
		
		List<String> fileNames = new ArrayList<>();
		//System.out.println("Baixando...");
		for(String downloadURL : getDownloadURLs()) {
			DownloadUtil util = new DownloadUtil(downloadURL);
            
            String saveFilePath = saveDirectory + File.separator + util.getFileName();
            //System.out.println(saveFilePath);
            fileNames.add(saveFilePath);
		    try (ReadableByteChannel rbc = Channels.newChannel( util.getUrl().openStream());
	            FileOutputStream fos = new FileOutputStream( saveFilePath )) {
	            fos.getChannel().transferFrom( rbc, 0, Long.MAX_VALUE );           
	 
	        } catch (IOException e) {	        	
	            throw(new IOException("Error ao baixar o arquivo "+ downloadURL, e));
	            
	        }
	    }
		List<String> retorno = new ArrayList<>();
		//System.out.println("Descomprimindo...");
		for(String fileName : fileNames) {
            //System.out.println(fileName);
            if (DolphinUtils.isUnzippable(fileName)){
            	retorno.addAll(DolphinUtils.unzipFile(fileName));
            	
            }else {
            	retorno.add(fileName);
            }
		}
		
		this.fileNames = retorno.toArray(new String[0]);
		System.out.println("Donwload conclu�do e arquivo(s) descomprimido(s) " + retorno);
		//JOptionPane.showMessageDialog(null,  "Donwload conclu�do e arquivo(s) descomprimido(s)","UFC9.Dolphin: Menssagem de alerta", JOptionPane.WARNING_MESSAGE);
		return this.fileNames;
    }
    
    /**
     * @return
     * 		O endere�os dos arquivos baixados.
     */
    public String[] getFileNames() {
		return fileNames;
	}
	/**
	 * @param fileNames
	 * 		Seta os endere�os dos arquivos baixados.
	 */
	protected void setFileNames(String[] fileNames) {
		this.fileNames = fileNames;
	}
	/**
	 * @return
	 * 		Links por onde os arquivos ser�o baixados.
	 */
	public String[] getDownloadURLs() {
		return downloadURLs;
	}
	/**
	 * @param downloadURLs
	 * 		Seta os links por onde os arquivos ser�o baixados.
	 */
	public void setDownloadURLs(String[] downloadURLs) {
		this.downloadURLs = downloadURLs;
	}
}
