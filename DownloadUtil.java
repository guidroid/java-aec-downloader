package ufc.deha.ufc9.dolphin.downloader;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Utilidade para download de arquivo na web.
 * <p>
 * Armazena a url e verifica se ela � coerente.
 * @since 1.0
 * @version 1.0
 * 
 * @author Guilherme Ribeiro  
 */
public class DownloadUtil{
	/**
	 * Link do download.
	 */
	private URL url;
	/**
	 * Nome do arquivo baixado.
	 */
	private String fileName;
	
	private String addressURL;
	
	/**
	 * Inicializa o objeto com o link do download.
	 * @param targetURL
	 * 		link do download.
	 * @throws MalformedURLException
	 * 		Caso o link n�o possa ser encontrado.
	 */
	public DownloadUtil(String targetURL) throws MalformedURLException{
		this.addressURL = targetURL;
		this.fileName = targetURL.substring(targetURL.lastIndexOf("/") + 1,	targetURL.length());		
		this.url = new URL(targetURL);
	}
	/**
	 * @return
	 * 		link de download.
	 */
	public URL getUrl() {
		return url;
	}
	
	/**
	 * @return
	 * 		Url com protocolo https em vez de http:
	 * @throws MalformedURLException
	 */
	public URL getHttpsURL() throws MalformedURLException {
		String targetURL = addressURL;
		
		if (targetURL.startsWith("http:")) {
			targetURL = targetURL.replaceFirst("http:", "https:");
		}
		
		return new URL(targetURL);
	}
	/**
	 * Seta a url de download.
	 * @param url
	 * 		link do download. 		
	 */
	public void setUrl(URL url) {
		this.url = url;
	}
	/** 
	 * @return
	 * 		o nome do arquivo baixado.
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName
	 * 		Seta o nome do arquivo.
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * Retorna o tamanho do arquivo em url ou -1 se esse tamanho estiver indefinido.
	 * @param url - URL do arquivo.
	 * @return o tamanho do arquivo.
	 */
	public int contentLength() {
       HttpURLConnection connection;
       int contentLength = -1;

       try {
           HttpURLConnection.setFollowRedirects( false );

           connection = (HttpURLConnection) url.openConnection();
           connection.setRequestMethod( "HEAD" );

           contentLength = connection.getContentLength();
       } catch ( Exception e ) { }

       return contentLength;
   }
}