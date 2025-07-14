package ufc.deha.ufc9.dolphin.downloader.gui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.concurrent.Task;
import ufc.deha.ufc9.dolphin.downloader.DownloadUtil;

/**
 * Classe de download in BackGround para JavaFX.
 * 
 * @since 1.5 (Dolphin)
 * @version 1.0
 * 
 * @author Guilherme Ribeiro  
 *
 */
public class MultiDownloaderInBackgroundJavaFx extends Task<Void> implements RBCWrapperDelegateMulti {
	//Link baixado atualmente
	protected int current = 1;
	//Endere�o de sa�da.
	private String saveDirectory;
	//Lista de links para download.
	private List<String> downloadURLs = new ArrayList<>();
	//Lista de arquivos gerados pelo download.
	private Map<String, String> fileNames = new HashMap<>();

	/**
	 * Construtor.
	 * @param saveDirectory
	 */
	public MultiDownloaderInBackgroundJavaFx() {
		super();
		this.saveDirectory = "";
		updateTitle("Download");
	}
	/**
	 * Construtor.
	 * @param saveDirectory
	 */
	public MultiDownloaderInBackgroundJavaFx(String saveDirectory) {
		super();
		this.saveDirectory = saveDirectory;
		updateTitle("Download");	
	}
	/**
	 * Construtor.
	 * @param downloadURL
	 * @param saveDirectory
	 */
	public MultiDownloaderInBackgroundJavaFx(String downloadURL, String saveDirectory) {
		super();
		setDownloadURLs(downloadURL);
		this.saveDirectory = saveDirectory;
		updateTitle("Download");	
	}
	/**
	 * Construtor.
	 * @param downloadURLs
	 * @param saveDirectory
	 */
	public MultiDownloaderInBackgroundJavaFx(Collection<String> downloadURLs, String saveDirectory) {
		super();
		setDownloadURLs(downloadURLs);
		this.saveDirectory = saveDirectory;
		updateTitle("Download");	
	}
	
	@Override
	public void rbcProgressCallback(RBCWrapper rbc) {
		this.updateProgress(rbc.getReadSoFar(), rbc.getExpectedSize());
		if (rbc.getProgress() >= 0.0) {
			this.updateMessage("Baixando: " + Integer.toString((int) rbc.getProgress()) + "%");
		} else {
			this.updateMessage("Baixando: " + Double.toString((int) (rbc.getReadSoFar() / 1000.0)) + "kB");
		}
	}
	@Override
	protected Void call() throws IOException {
		//Linha de c�digo necess�ria para o download.
		CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
		

		Map<String, String> listOfFileNames = new HashMap<>();
		String errorMessage = "";

		current = 1;
		for (String downloadURL : getDownloadURLs()) {
			this.updateTitle("(" + current + "/" + getDownloadURLs().size()  + ")");
			try {
				DownloadUtil util = new DownloadUtil(downloadURL);
				//Cria o nome do arquivo.
				String saveFilePath = saveDirectory + File.separator + util.getFileName();
				listOfFileNames.put(downloadURL, saveFilePath);
				//GArante que a pasta onde o arquivo ser� salvo j� exista.
				new File(saveDirectory).mkdirs();
				//Checa se o arquivo j� existe. Caso sim, ele � deletado.
				if(new File(saveFilePath).exists()) {
					new File(saveFilePath).delete();
				}
				try(ReadableByteChannel rbc = new RBCWrapper(Channels.newChannel(util.getUrl().openStream()), util.contentLength(), this);
						FileOutputStream fos = new FileOutputStream(saveFilePath);){
					fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				}catch(Exception ex) {
					ex.printStackTrace();
				}

			} catch (IOException ex) {
				ex.printStackTrace();
				errorMessage += "\n" + ex.getMessage();
				listOfFileNames.remove(downloadURL);
				//cancel(true);
			}
			
			current++;
		}
		setFileNames(listOfFileNames);

		if(!errorMessage.equals("")) {
			this.updateMessage(errorMessage);
			throw(new IOException(errorMessage));
		}
		return null;
	}
	
	@Override
	protected void failed() {
		updateProgress(0, 1);
		this.updateMessage("Erro!");
	}

	@Override
	protected void done() {
		updateProgress(1, 1);
		this.updateMessage("Baixado!");
	}

	public Map<String, String> getFileNames() {
		return fileNames;
	}

	public void setFileNames(Map<String, String> fileNames) {
		this.fileNames.clear();
		this.fileNames.putAll(fileNames);
	}

	public List<String> getDownloadURLs() {
		return downloadURLs;
	}

	public void setDownloadURLs(String downloadURL) {
		this.downloadURLs.clear();
		this.downloadURLs.add(downloadURL);
	}

	public void setDownloadURLs(Collection<String> downloadURLs) {
		this.downloadURLs.clear();
		this.downloadURLs.addAll(downloadURLs);
	}


	/**
	 * Classe final que realiza o download como ReadableByteChannel e atualiza o progresso.
	 */
	public static final class RBCWrapper implements ReadableByteChannel {
		//Objeto que implementa a interface que delega a opera��o de download e tem um m�todo de atualiza��o de progresso.
		private RBCWrapperDelegateMulti delegate;
		//ReadableByteChannel que ser� usado no download
		private ReadableByteChannel rbc;
		
		//Tamanho esperado do arquivo como long.
		private long expectedSize;
		//Quantidade de bytes lida at� agora.
		private long readSoFar;
		//Progresso como double que corresponde � raz�o entre readSoFar e expectedSize.
		private double progress;
				
		/**
		 * Construtor.
		 * @param rbc - ReadableByteChannel que ser� usado no download
		 * @param expectedSize - Tamanho esperado do arquivo. Use -1 para torn�-lo indeterminado.
		 * @param delegate - Classe que implenta o m�todo de atualiza��o do progresso.
		 */
		RBCWrapper(ReadableByteChannel rbc, long expectedSize, RBCWrapperDelegateMulti delegate) {
			this.delegate = delegate;
			this.expectedSize = expectedSize;
			this.rbc = rbc;
		}
		
		@Override
		public void close() throws IOException {
			rbc.close();
		}
		@Override
		public boolean isOpen() {
			return rbc.isOpen();
		}
		
		@Override
		public int read(ByteBuffer bb) throws IOException {
			int n;
			progress = 0.0;

			//L� o conjunto de bytes definido e atualiza os par�metros da classe.
			if ((n = rbc.read(bb)) > 0) {
				readSoFar += n;
				progress = expectedSize > 0 ? (double) readSoFar / (double) expectedSize * 100.0 : -1.0;
				delegate.rbcProgressCallback(this);
			}

			return n;
		}

		public long getExpectedSize() {
			return expectedSize;
		}
		public long getReadSoFar() {
			return readSoFar;
		}
		public double getProgress() {
			return progress;
		}

	}
}

interface RBCWrapperDelegateMulti {
	/**
	 * Atualiza o progresso.
	 * @param rbc
	 */
	public void rbcProgressCallback(MultiDownloaderInBackgroundJavaFx.RBCWrapper rbc);
}
