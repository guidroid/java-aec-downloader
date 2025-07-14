package ufc.deha.ufc9.dolphin.downloader.utils;

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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import ufc.deha.ufc9.dolphin.DolphinUtils;

public class MultiDownloaderInBackground  extends SwingWorker<Void, Long> implements RBCWrapperDelegateMulti{

	private String[] downloadURLs;
    private String saveDirectory;
    
    private String[] fileNames = new String[0];
    private JLabel labelProgressData;
     
    public String[] getFileNames() {
		return fileNames;
	}
	public void setFileNames(String[] fileNames) {
		this.fileNames = fileNames;
	}
	public String[] getDownloadURLs() {
		return downloadURLs;
	}
	public void setDownloadURLs(String[] downloadURLs) {
		this.downloadURLs = downloadURLs;
	}

	public MultiDownloaderInBackground(String[] downloadURLs, String saveDirectory) {
        this.downloadURLs = downloadURLs;
        this.saveDirectory = saveDirectory;
        this.labelProgressData = null;
    }
	public MultiDownloaderInBackground(String[] downloadURLs, String saveDirectory, JLabel labelProgressData) {
        this.downloadURLs = downloadURLs;
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
		
		List<String> fileNames = new ArrayList<>();
		for(String downloadURL : getDownloadURLs()) {
		    try {
		        DownloadUtil util = new DownloadUtil(downloadURL);
		             
	            String saveFilePath = saveDirectory + File.separator + util.getFileName();
	            fileNames.add(saveFilePath);
	            
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
	    }
		
		this.fileNames = fileNames.toArray(new String[0]);
		return null;
    }
    
    /**
     * Altera os resultados intermediários e a label fornecida para isso.
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
	            JOptionPane.showMessageDialog(null, "Arquivo foi baixado com sucesso!\nEndereço de saída: " + saveDirectory , "UFC9.Dolphin: Mensagem de alerta", JOptionPane.INFORMATION_MESSAGE);
	            
	            for(String fileName : getFileNames()) {
		            if (DolphinUtils.isUnzippable(fileName)){
		            	 int dialogButton = JOptionPane.YES_NO_OPTION;    
		            	 dialogButton = JOptionPane.showConfirmDialog (null, "O arquivo baixado está comprimido.\nDescomprimir para uma pasta no mesmo endereço do arquivo?","UFC9.Dolphin: Mensagem de alerta", dialogButton);
		            	 if (dialogButton == JOptionPane.YES_OPTION) {
		            		 DolphinUtils.UnzipFileWithProgressBar unzipper = new DolphinUtils.UnzipFileWithProgressBar(fileName);
		            		 unzipper.unzip();	            		 
		            	 }
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
	    private RBCWrapperDelegateMulti delegate;
	    private long expectedSize;
	    private ReadableByteChannel rbc;
	    private long readSoFar;
	
	    RBCWrapper( ReadableByteChannel rbc, long expectedSize, RBCWrapperDelegateMulti delegate ) {
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

interface RBCWrapperDelegateMulti {
    public void rbcProgressCallback( MultiDownloaderInBackground.RBCWrapper rbc );
}

