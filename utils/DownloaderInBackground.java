package ufc.deha.ufc9.dolphin.downloader.utils;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import ufc.deha.ufc9.dolphin.DolphinUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.DecimalFormat;
import java.util.List;
 
/**
 * Baixa arquivos da internet em outra thread.
 * <p>Se fornecida, uma barra de progresso, o progresso � atualizado para arquivos com tamanho definido.
 * Ainda como informa��o intermedi�rio, � fornecido quanto foi baixado.
 * @since 1.0
 * @version 1.0
 * 
 * @author Guilherme Ribeiro  
 *
 */
public class DownloaderInBackground extends SwingWorker<Void, Long> implements RBCWrapperDelegate{

	private String downloadURL;
    private String saveDirectory;
    
    private String fileName = "";
    private JLabel labelProgressData;
     
    public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public DownloaderInBackground(String downloadURL, String saveDirectory) {
        this.downloadURL = downloadURL;
        this.saveDirectory = saveDirectory;
        this.labelProgressData = null;
    }
	public DownloaderInBackground(String downloadURL, String saveDirectory, JLabel labelProgressData) {
        this.downloadURL = downloadURL;
        this.saveDirectory = saveDirectory;
        this.labelProgressData = labelProgressData;
    }
	
	public void rbcProgressCallback( RBCWrapper rbc ) {
		 publish(rbc.getReadSoFar());
	    }
	 
	 /**
	  * Retorna o tamanho do arquivo em url ou -1 se esse tamanho estiver indefinido.
	  * @param url - URL do arquivo.
	  * @return o tamanho do arquivo.
	  */
	 private int contentLength( URL url ) {
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
	 
    /**
     * Executa a thread em Background.
     */
    @Override
    protected Void doInBackground() {
		CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
		FileOutputStream fos = null;
		ReadableByteChannel rbc = null;
	    try {
	        DownloadUtil util = new DownloadUtil(downloadURL);
	             
            String saveFilePath = saveDirectory + File.separator + util.getFileName(); 
            setFileName(saveFilePath);
            
            rbc = new RBCWrapper( Channels.newChannel( util.getUrl().openStream() ), contentLength( util.getUrl() ), this );
            fos = new FileOutputStream( saveFilePath );
            fos.getChannel().transferFrom( rbc, 0, Long.MAX_VALUE );
            
 
        } catch (IOException ex) {	        	
            JOptionPane.showMessageDialog(null, "Error downloading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);           
		    //ex.printStackTrace();
		    cancel(true);          
		}finally {
			if (fos != null) {
				try{
					fos.close();
				}catch(IOException ex){}
			}
		}
	return null;
    }
    
    /**
     * Altera os resultados intermedi�rios e a label fornecida para isso.
     */
    @Override
    protected void process(List<Long> publishedVals) {
    	if(labelProgressData != null) {
    		String result = new DecimalFormat("#.##").format( publishedVals.get(publishedVals.size() - 1) / 1000000.0).toString() + " Mb";
    		labelProgressData.setText(result);
    	}
    }
 
    /**
     * Executa em Swing's event dispatching thread
     */
    @Override
    protected void done() {
    	try {
    		get();
    	
	        if (!isCancelled()) {
	            JOptionPane.showMessageDialog(null, "Arquivo foi baixado com sucesso!\nEndere�o de sa�da: " + getFileName() , "UFC9.Dolphin: Mensagem de alerta", JOptionPane.INFORMATION_MESSAGE);
	            if (DolphinUtils.isUnzippable(getFileName())){
	            	 int dialogButton = JOptionPane.YES_NO_OPTION;    
	            	 dialogButton = JOptionPane.showConfirmDialog (null, "O arquivo baixado est� comprimido.\nDescomprimir para uma pasta no mesmo endere�o do arquivo?","UFC9.Dolphin: Mensagem de alerta",dialogButton);
	            	 if (dialogButton == JOptionPane.YES_OPTION) {
	            		 DolphinUtils.UnzipFileWithProgressBar unzipper = new DolphinUtils.UnzipFileWithProgressBar(getFileName());
	            		 unzipper.unzip();	            		 
	            	 }
	            }
	        }
	        if(labelProgressData != null) {
	    		labelProgressData.setText(labelProgressData.getText() + " Finalizado");
	    	}

    	}catch(Exception e) {
    		if (isCancelled() && labelProgressData != null) {
    			labelProgressData.setText("Cancelado!");
	        }
    	}
    }  

    @SuppressWarnings("unused")
	public static final class RBCWrapper implements ReadableByteChannel {
	    private RBCWrapperDelegate delegate;
	    private long expectedSize;
	    private ReadableByteChannel rbc;
	    private long readSoFar;
	
	    RBCWrapper( ReadableByteChannel rbc, long expectedSize, RBCWrapperDelegate delegate ) {
	        this.delegate = delegate;
	        this.expectedSize = expectedSize;
	        this.rbc = rbc;
	    }
	
	    public void close() throws IOException { rbc.close(); }
	    public long getReadSoFar() { return readSoFar; }
	    public boolean isOpen() { return rbc.isOpen(); }
	
	    public int read( ByteBuffer bb ) throws IOException {
	        int n;
	        double progress;
	
	        if ( ( n = rbc.read( bb ) ) > 0 ) {
	            readSoFar += n;
	            progress = expectedSize > 0 ? (double) readSoFar / (double) expectedSize * 100.0 : -1.0;
	            delegate.rbcProgressCallback( this);
	        }
	
	        return n;
	    }
	}
}
interface RBCWrapperDelegate {
    public void rbcProgressCallback( DownloaderInBackground.RBCWrapper rbc );
}
