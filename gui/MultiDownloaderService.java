package ufc.deha.ufc9.dolphin.downloader.gui;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;

public class MultiDownloaderService extends Service<Void>{
	//Link baixado atualmente
	protected IntegerProperty current = new SimpleIntegerProperty(1);
	// Endereço de saída.
	private StringProperty saveDirectory = new SimpleStringProperty();
	// Lista de links para download.
	private ListProperty<String> downloadURLs = new SimpleListProperty<>();
	// Lista de arquivos gerados pelo download.
	private MapProperty<String, String> fileNames = new SimpleMapProperty<>();

	@Override
	protected MultiDownloaderInBackgroundJavaFx createTask() {
		MultiDownloaderInBackgroundJavaFx task = new MultiDownloaderInBackgroundJavaFx(
				getDownloadURLs(),
				getSaveDirectory());
		//task.set
		 return task;
	}

	/**
	 * @param current the current to set
	 */
	public void setCurrent(Integer current) {
		this.current.set(current);
	}
	/**
	 * @param saveDirectory the saveDirectory to set
	 */
	public void setSaveDirectory(String saveDirectory) {
		this.saveDirectory.set(saveDirectory);
	}
	/**
	 * @param downloadURLs the downloadURLs to set
	 */
	public void setDownloadURLs(Collection<String> downloadURLs) {
		this.downloadURLs.set(FXCollections.observableArrayList(downloadURLs));
	}
	/**
	 * @param fileNames the fileNames to set
	 */
	public void setFileNames(Map<String, String> fileNames) {
		this.fileNames.set(FXCollections.observableMap(fileNames));
	}

	/**
	 * @return the current
	 */
	public IntegerProperty getCurrentProperty() {
		return current;
	}
	/**
	 * @return the saveDirectory
	 */
	public StringProperty getSaveDirectoryProperty() {
		return saveDirectory;
	}
	/**
	 * @return the downloadURLs
	 */
	public ListProperty<String> getDownloadURLsProperty() {
		return downloadURLs;
	}
	/**
	 * @return the fileNames
	 */
	public MapProperty<String, String> getFileNamesProperty() {
		return fileNames;
	}
	
	/**
	 * @return the current
	 */
	public Integer getCurrent() {
		return current.get();
	}
	/**
	 * @return the saveDirectory
	 */
	public String getSaveDirectory() {
		return saveDirectory.get();
	}
	/**
	 * @return the downloadURLs
	 */
	public List<String> getDownloadURLs() {
		return downloadURLs.get();
	}
	/**
	 * @return the fileNames
	 */
	public Map<String, String> getFileNames() {
		return fileNames.get();
	}

}
