package ufc.deha.ufc9.dolphin.downloader.gui.view;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CancellationException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ufc.deha.ufc9.dolphin.Dolphin;
import ufc.deha.ufc9.dolphin.referencia.DefaultUFC9;
import ufc.deha.ufc9.dolphin.referencia.Estados;
import ufc.deha.ufc9.dolphin.referencia.TabelaDeReferencia;
import ufc.deha.ufc9.dolphin.utils.MetaData;

public class UpdaterDolphinDataBaseController {

	public static final String TITLE = "UFC9.Dolphin: Updater";
	@FXML
	private ComboBox<TabelaDeReferencia> referenciaCBX;
	@FXML
	private ComboBox<Estados> estadoCBX;
	@FXML
	private Label labelMetaData;

	// Estágio pai.
	private Stage dialogStage;
	private Stage primaryStage;

	public static int MIN_WEIGTH = 400;
	public static int MIN_HEIGHT = 150;
	/**
	 * @return the primaryStage
	 */
	public Stage getDialogStage() {
		return dialogStage;
	}
	/**
	 * @param primaryStage the primaryStage to set
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	/**
	 * Inicializa a classe controller. Este método é chamado automaticamente após o
	 * arquivo fxml ter sido carregado.
	 */
	@FXML
	private void initialize() {
		// Carrega as combo Boxes.
		referenciaCBX.setItems(FXCollections.observableArrayList(TabelaDeReferencia.values()));
		estadoCBX.setItems(FXCollections.observableArrayList(Estados.values()));
		
		referenciaCBX.valueProperty().addListener(new ChangeListener<TabelaDeReferencia>() {
	        @Override public void changed(ObservableValue<? extends TabelaDeReferencia> ov, TabelaDeReferencia old, TabelaDeReferencia novo) {
	        	if(!(novo == old)) {
	        		updateBoxes();
	        		updateLabels();
	        	}
	        }
	    });
		estadoCBX.valueProperty().addListener(new ChangeListener<Estados>() {
			@Override public void changed(ObservableValue<? extends Estados> ov, Estados old, Estados novo) {
	        	if(!(novo == old)) {
	        		updateLabels();
	        	}
	        }   
	    });
		
		alertMessage = "";
		try {
			DefaultUFC9.readDefaultFile();
			referenciaCBX.getSelectionModel().select(DefaultUFC9.REFERENCIA);
			estadoCBX.getSelectionModel().select(DefaultUFC9.ESTADO);
		} catch (IOException e) {
			alertMessage += e.getMessage();
			referenciaCBX.getSelectionModel().select(TabelaDeReferencia.NULL);
			estadoCBX.getSelectionModel().select(Estados.NULL);
		}
		updateBoxes();
		updateLabels();
		handleAlert();
	}

	/**
	 * Define o conjunto de itens.
	 * 
	 * @param entrada - Objeto DolphinReadBlock.
	 */
	public void setParamater(Estados estado) {
		try{
			estadoCBX.getSelectionModel().select(estado);
			updateLabels();
		}catch(Exception e) {}
		}
	public void setParamater(TabelaDeReferencia referencia) {
		referenciaCBX.getSelectionModel().select(referencia);
		updateBoxes();
		updateLabels();
	}
	public void setParamater(TabelaDeReferencia referencia, Estados estado) {
		referenciaCBX.getSelectionModel().select(referencia);
		updateBoxes();
		try{
			estadoCBX.getSelectionModel().select(estado);
		}catch(Exception e) {}
		updateLabels();
		}

	private String alertMessage = "";
	/**
	 * Ainda sujeito a testes.
	 */
	@FXML
	private void handleAlert() {
		if(estadoCBX.getSelectionModel().getSelectedItem() == Estados.NULL ||
				referenciaCBX.getSelectionModel().getSelectedItem() == TabelaDeReferencia.NULL) {
			alertMessage += "\nValores Nulos não podem ser setados para O Estado ou Refencial de Custo.";
		}
		
		if(!alertMessage.equals("")) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);

			//alert.initModality(Modality.WINDOW_MODAL);
			//alert.initOwner(dialogStage);
			
			alert.setTitle("UFC9.Dolphin: Alerta");
			alert.setHeaderText("Mensagem de erro!");
			alert.setContentText(alertMessage == null ? "" : alertMessage);
			alert.showAndWait();
			
			alertMessage = "";
		}
	}
	@FXML
	private void handleSelectFiles() {
		DirectoryChooser chooser = new DirectoryChooser();
		
		chooser.setTitle("UFC9.Dolphin");
		try{
			File file = new File(estadoCBX.getSelectionModel().getSelectedItem().getDownloadPath(
					referenciaCBX.getSelectionModel().getSelectedItem()));
			file.mkdirs();
			
			chooser.setInitialDirectory(file);
		}catch(Exception e){}
		
		File selectedDirectory = null;
		try {
			selectedDirectory = chooser.showDialog(dialogStage);
		}catch(Exception e) {}
		
		alertMessage = "";
		
		if (selectedDirectory != null) {
			try {
				Dolphin.updateBancoBySelectingFiles(selectedDirectory.getAbsolutePath(),
						getCurrentReferencia(),
						getCurrentEstado(),
						this.dialogStage);
				
			}catch(CancellationException e) {
			} catch (Exception e) {
				alertMessage += "\n" + e.getMessage();
			}
		}
		
		handleAlert();
	}

	//Atualizar
	@FXML
	private void handleUpdateButton() {
		alertMessage = "";
		try {
			Dolphin.updateBancoByDownloadingFiles(
					getCurrentReferencia(),
					getCurrentEstado(),
					dialogStage);
			updateLabels();
		} catch (Exception e) {
//			e.printStackTrace();
			alertMessage += "\n" + e.getMessage();
		}

		handleAlert();
	}
	private void updateBoxes() {
		if(referenciaCBX.getSelectionModel().getSelectedItem() == null || getCurrentReferencia().equals(TabelaDeReferencia.NULL)) {
			estadoCBX.setItems(FXCollections.observableArrayList(Estados.NULL));
			estadoCBX.getSelectionModel().select(0);			
		}else {
			estadoCBX.setItems(FXCollections.observableArrayList(getCurrentReferencia().getEstados()));
			estadoCBX.getSelectionModel().select(0);
			}
	}
	@FXML
	private void updateLabels() {
		if(getCurrentEstado() != null &&
				getCurrentReferencia() != null &&
				getCurrentEstado() != Estados.NULL &&
				getCurrentReferencia() != TabelaDeReferencia.NULL) {
			labelMetaData.setText(
				MetaData.getFormattedData(estadoCBX.getSelectionModel().getSelectedItem()
						.getParentPath(referenciaCBX.getSelectionModel().getSelectedItem())));	
		}else {
			labelMetaData.setText("*");
		}
		
	}
	
	public TabelaDeReferencia getCurrentReferencia() {
		return referenciaCBX.getSelectionModel().getSelectedItem();
	}
	public Estados getCurrentEstado() {
		return estadoCBX.getSelectionModel().getSelectedItem();
	}
	
	public static UpdaterDolphinDataBaseController showUpdater(Stage primaryStage) throws IOException {
		//Carrega o RootLayout dentro do arquivo
		FXMLLoader loader = new FXMLLoader();
		//loader.setLocation(Teste.class.getResource("view/LinkDownloaderViewer.fxml"));	
		loader.setLocation(UpdaterDolphinDataBaseController.class.getResource("UpdaterDolphinDataBase.fxml"));
		BorderPane guiView = (BorderPane) loader.load();

		Stage dialogStage = new Stage();
		dialogStage.setScene(new Scene(guiView));
		dialogStage.setTitle("UFC9.Dolphin");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(primaryStage);
		dialogStage.setResizable(false);
		
		UpdaterDolphinDataBaseController controller = loader.getController();
		controller.setDialogStage(dialogStage);
		
		dialogStage.showAndWait();
		return controller;
	}
	public static UpdaterDolphinDataBaseController showUpdater(Stage primaryStage, TabelaDeReferencia referencia, Estados estado) throws IOException {
		//Carrega o RootLayout dentro do arquivo
		FXMLLoader loader = new FXMLLoader();
		//loader.setLocation(Teste.class.getResource("view/LinkDownloaderViewer.fxml"));	
		loader.setLocation(UpdaterDolphinDataBaseController.class.getResource("UpdaterDolphinDataBase.fxml"));
		BorderPane guiView = (BorderPane) loader.load();

		Stage dialogStage = new Stage();
		dialogStage.setScene(new Scene(guiView));
		dialogStage.setTitle("UFC9.Dolphin");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(primaryStage);
		dialogStage.setResizable(false);
		
		UpdaterDolphinDataBaseController controller = loader.getController();
		controller.setDialogStage(dialogStage);
		controller.setParamater(referencia, estado);
		
		dialogStage.showAndWait();
		return controller;
	}

}
