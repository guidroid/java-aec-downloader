package ufc.deha.ufc9.dolphin.downloader.gui.utils;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ufc.deha.ufc9.dolphin.downloader.gui.MultiDownloaderInBackgroundJavaFx;

/**
 * BArra de progresso para Task.
 * 
 * @since 1.5
 * @version 1.0
 * @author Guilherme Ribeiro  
 *
 */
public class ProgressBarWithCancelButtonController {

	private static String TITLE = "UFC9.Dolphin: Carregando...";
	
	@FXML
	Label progress;
	@FXML
	Label overallProgress;
	@FXML
	private Label message;
	@FXML
	ProgressBar progressBar;
	@FXML 
	Button startButton;
	//Task Ativa
	private static Task<Void> activeTask;
	
	private Stage primaryStage;
	private Stage dialogStage;
	
	private static boolean closeOnSuceeded = true;
	private static boolean autoInitializate = false;
	public static boolean IS_LAST_CANCELLED = false;

	private static final SimpleStringProperty messageProperty = new SimpleStringProperty("Clique em Iniciar...");
	
	public static void resetProperties() {
		closeOnSuceeded = true;
		autoInitializate = false;
		messageProperty.set("Clique em Iniciar...");
		TITLE = "UFC9.Dolphin: Carregando...";
		activeTask = null;
		IS_LAST_CANCELLED = false;
	}
	/**
	 * @return the messageProperty
	 */
	public static SimpleStringProperty getMessageProperty() {
		return messageProperty;
	}

	/**
	 * @return the messageProperty
	 */
	public static String getMessage() {
		return messageProperty.get();
	}
	/**
	 * @return the messageProperty
	 */
	public static void updateMessage(String message) {
		messageProperty.set(message);
	}

	/**
	 * @return Task Ativa.
	 */
	public static Task<Void> getActiveTask() {
		return activeTask;
	}

	/**
	 * Altera a task Ativa
	 * 
	 * @param activeTask Task.
	 */
	public static void setActiveTask(Task<Void> activeTask) {
		ProgressBarWithCancelButtonController.activeTask = activeTask;
	}

	/**
	 * @return O est�gio.
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	/**
	 * Altera o est�gio. 
	 * @param primaryStage - Est�gio do Di�logo.
	 */
	public void setPrimaryStage(Stage dialogStage) {
		this.primaryStage = dialogStage;
	}
	/**
	 * @return the dialogStage
	 */
	public Stage getDialogStage() {
		return dialogStage;
	}
	/**
	 * @param dialogStage the dialogStage to set
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	/**
	 * Marca se a ProgressBar deve ser fechado.
	 * @return
	 */
	public static boolean isCloseOnSuceeded() {
		return ProgressBarWithCancelButtonController.closeOnSuceeded;
	}
	public static void setCloseOnSuceeded(boolean closeOnSuceeded) {
		ProgressBarWithCancelButtonController.closeOnSuceeded = closeOnSuceeded;
	}
	/**
	 * @return the autoInitializate
	 */
	public static boolean isAutoInitializate() {
		return autoInitializate;
	}
	/**
	 * @param autoInitializate the autoInitializate to set
	 */
	public static void setAutoInitializate(boolean autoInitializate) {
		ProgressBarWithCancelButtonController.autoInitializate = autoInitializate;
	}
	@FXML
	public  void handleCancel() {
		if (activeTask != null && activeTask.isRunning()) {
			activeTask.cancel();
		}
		this.dialogStage.close();
		IS_LAST_CANCELLED = true;
	}

	@FXML
	private void initialize() {
		progressBar.progressProperty().set(0.0);
		message.textProperty().unbind();
		message.textProperty().bind(messageProperty);
	}

	/**
	 * Cria uma mensagem de alerta.
	 * 
	 * @param alertMessage - Mensagem de alerta.
	 */
	private void handleAlert(String alertMessage) {
		if (alertMessage != null && !alertMessage.equals("")){
			Alert alert = new Alert(Alert.AlertType.INFORMATION);

			//alert.initModality(Modality.WINDOW_MODAL);
			//alert.initOwner(primaryStage);
			
			alert.setTitle("UFC9.Dolphin: Alerta");
			alert.setHeaderText("Mensagem de erro!");
			alert.setContentText(alertMessage == null ? "" : alertMessage);
		
			alert.showAndWait();		
		}
	}

	@FXML
	private void startTask() {		
		if (dialogStage == null) {
			handleAlert("N�o foi poss�vel carregar o di�logo!");
			return;
		}
		// Inicia uma nova task.
		if (activeTask != null && !activeTask.isRunning()) {
			progressBar.progressProperty().unbind();
			progressBar.progressProperty().bind(activeTask.progressProperty());
			
			progress.textProperty().unbind();
			progress.textProperty().bind(activeTask.titleProperty());
			overallProgress.textProperty().unbind();
			overallProgress.textProperty().bind(activeTask.messageProperty());

			// Garante que o di�logo seja fechado ap�s concluir a Task.
			if (closeOnSuceeded) {
				activeTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

					@Override
					public void handle(WorkerStateEvent event) {
						dialogStage.close();
					}
				});
			}else {
				activeTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

					@Override
					public void handle(WorkerStateEvent event) {
						startButton.setVisible(false);
					}
				});
			}
			// Garante que o di�logo seja fechado ap�s concluir a Task.
			activeTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					dialogStage.close();
					startButton.setVisible(false);
					handleAlert("Erro: " + event.getSource().getMessage());
				}
			});
			// Garante que o di�logo seja cancelado e fechado ap�s cancelar.
			activeTask.setOnCancelled(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					dialogStage.close();
				}
			});
			// Carrega a Thread
			Thread t = new Thread(activeTask);
			t.start();
			startButton.setVisible(false);
		}
	}
	/**
	 * @return the tITLE
	 */
	public static String getTitle() {
		return TITLE;
	}
	/**
	 * @param tITLE the tITLE to set
	 */
	public static void setTitle(String title) {
		TITLE = "UFC9.Dolphin: " + title;
	}
	/**
	 * Abre uma barra de progresso para baixar uma Task.
	 * 
	 * @param task         - Task
	 * @param primaryStage - Est�gio prim�rio.
	 * @throws Exception 
	 */
	public static ProgressBarWithCancelButtonController showInProgressBar(Task<Void> task, Stage primaryStage)
			throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ProgressBarWithCancelButtonController.class.getResource("ProgressBarWithCancelButton.fxml"));
		AnchorPane guiView = (AnchorPane) loader.load();

		Stage dialogStage = new Stage();

		dialogStage.setScene(new Scene(guiView));
		dialogStage.setTitle(getTitle());
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(primaryStage);
		dialogStage.setResizable(false);

		// D� ao controlador acesso � the main app.
		ProgressBarWithCancelButtonController gui = loader.getController();
		ProgressBarWithCancelButtonController.setActiveTask(task);
		gui.setPrimaryStage(primaryStage);
		gui.setDialogStage(dialogStage);

		if (isAutoInitializate()) {
			dialogStage.setOnShowing(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					gui.startButton.setVisible(false);
					gui.startTask();
				}
			});
		}
		dialogStage.showAndWait();

		return gui;
	}

	/**
	 * Mostra uma caixa de di�logo independente.
	 * 
	 * @param task
	 */
	public static ProgressBarWithCancelButtonController showInProgressBar(Task<Void> task) throws Exception {
		Runner.setActiveTask(task);
		Runner.run();
		return Runner.gui;
	}

	public static void main(String[] args) throws Exception {
		List<String> list = new ArrayList<>();
		list.add("C:/UFC/orcamento/Cear�/SINAPI/download/SINAPI_Custo_Ref_Composicoes_Analitico_CE_202010_NaoDesonerado.xls");
		list.add("C:/UFC/orcamento/Cear�/SINAPI/download/SINAPI_Custo_Ref_Composicoes_Sintetico_CE_202010_NaoDesonerado.xls");
	
		MultiDownloaderInBackgroundJavaFx task = new MultiDownloaderInBackgroundJavaFx(list, "C:\\UFC\\");

		showInProgressBar(task);
	}
	protected static class Runner extends Application {

		static Task<Void> activeTask;
		
		public Runner() {}
		public static ProgressBarWithCancelButtonController gui;
		/**
		 * Reproduz o di�logo.
		 */
		public static void run() {
			launch(new String[0]);
		}
		
		public static Task<Void> getActiveTask() {
			return activeTask;
		}
		public static void setActiveTask(Task<Void> activeTask) {
			Runner.activeTask = activeTask;
		}


		@Override
		public void start(Stage primaryStage) throws Exception {
			try {
				AnchorPane rootLayout = new AnchorPane();
				Scene scene = new Scene(rootLayout, 300, 120);

				primaryStage.setTitle(ProgressBarWithCancelButtonController.getTitle());
				primaryStage.setScene(scene);
				primaryStage.show();
				
			} catch (Exception e) {
//				e.printStackTrace();
				throw(e);
			}
		}

	}
}
