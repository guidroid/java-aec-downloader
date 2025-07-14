package ufc.deha.ufc9.dolphin.downloader.gui.view;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import ufc.deha.ufc9.dolphin.downloader.Sinapi;
import ufc.deha.ufc9.dolphin.downloader.gui.Link;
import ufc.deha.ufc9.dolphin.downloader.gui.MultiDownloaderInBackgroundJavaFx;
import ufc.deha.ufc9.dolphin.referencia.Desoneracao;
import ufc.deha.ufc9.dolphin.referencia.Estados;
import ufc.deha.ufc9.dolphin.referencia.TabelaDeReferencia;

/**
 * Classe para controle da interface de downolad de m�ltiplos arquivos.
 * <br>A opera��o de download ocorre em segundo plano pela classe {@link MultiDownloaderInBackgroundJavaFx}
 * 
 * @since 1.5 (Dolphin)
 * @version 1.0
 * 
 * @author Guilherme Ribeiro  
 *
 */
public class LinkDownloaderViewerController {
	public static final String TITLE = "UFC9.Dolphin: Downloader";
	@FXML private ComboBox<TabelaDeReferencia> referenciaCbx;
	@FXML private ComboBox<Estados> estadoCbx;
	@FXML private ComboBox<Desoneracao> desoneracaoCbx;
	
	@FXML private Button atualizarBtn;
	@FXML private ProgressIndicator progressIndicator;
	@FXML private Label labelProgress;

	@FXML private TableView<Link> table;
	@FXML private TableColumn<Link, String> dateColumn;
	@FXML private TableColumn<Link, String> linkColumn;
	@FXML private TableColumn<Link, Desoneracao> desoneracaoColumn;
	@FXML private TableColumn<Link, Estados> estadoColumn;

	@FXML private TextField saidaEnderecoText;
	
	//Lista de objetos do tipo Link
	private ObservableList<Link> links = FXCollections.observableArrayList();
	//Est�gio pai.
	private Stage dialogStage;
	private Stage primaryStage;

	public static int MIN_WEIGTH = 600;
	public static int MIN_HEIGHT = 400;
//	private MultiDownloaderService service = new MultiDownloaderService();
	/**
	 * Inicializa a classe controller. Este m�todo � chamado automaticamente ap�s o
	 * arquivo fxml ter sido carregado.
	 */
	@FXML
	private void initialize() {
		//Carrega as combo Boxes.
		referenciaCbx.setItems(FXCollections.observableArrayList(TabelaDeReferencia.values()));
		estadoCbx.setItems(FXCollections.observableArrayList(Estados.values()));
		desoneracaoCbx.setItems(FXCollections.observableArrayList(Desoneracao.values()));
		//Seleciona os primeiros elementos.
		referenciaCbx.getSelectionModel().select(TabelaDeReferencia.NULL);
		estadoCbx.getSelectionModel().select(Estados.NULL);
		desoneracaoCbx.getSelectionModel().select(Desoneracao.NULL);
		//Adiciona os listeners �s ComboBOxes que quando alteradas alteram o texto do bot�o de atualiza��o chamando o m�todo {@link #updateTags()}
		referenciaCbx.valueProperty().addListener(new ChangeListener<TabelaDeReferencia>() {
	        @Override public void changed(ObservableValue<? extends TabelaDeReferencia> ov, TabelaDeReferencia old, TabelaDeReferencia novo) {
	        	if(!(novo == old)) {
	        		updateLabels();
	        		updateTags(false);
	        	}
	        }
	    });
		estadoCbx.valueProperty().addListener(new ChangeListener<Estados>() {
			@Override public void changed(ObservableValue<? extends Estados> ov, Estados old, Estados novo) {
	        	if(!(novo == old)) {
	        		updateTags(false);
	        	}
	        }   
	    });
		desoneracaoCbx.valueProperty().addListener(new ChangeListener<Desoneracao>() {
			@Override public void changed(ObservableValue<? extends Desoneracao> ov, Desoneracao old, Desoneracao novo) {
	        	if(!(novo == old)) {
	        		updateTags(false);
	        	}
	        }    
	    });
		//Geradores de valores das colunas a partir do obejto Link.
		dateColumn.setCellValueFactory(cellData -> {
			StringBuilder str = new StringBuilder();
			str.append(cellData.getValue().getMonth()).append("/").append(cellData.getValue().getYear()).toString();

			return new SimpleStringProperty(str.toString());
		});		
		linkColumn.setCellValueFactory(cellData -> cellData.getValue().getLinkProperty());
		desoneracaoColumn.setCellValueFactory(cellData -> cellData.getValue().getDesoneracaoProperty());
		estadoColumn.setCellValueFactory(cellData -> cellData.getValue().getEstadoProperty());

		//Preenche a tabela e muda o tipo de sele��o para m�ltiplo.
		table.setItems(links);
		table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		//service

		updateLabels();
		
	}


	public void updateLabels() {
		if(referenciaCbx.getSelectionModel().getSelectedItem() == null || getCurrentReferencia().equals(TabelaDeReferencia.NULL)) {
			estadoCbx.setItems(FXCollections.observableArrayList(Estados.NULL));
			estadoCbx.getSelectionModel().select(0);
			desoneracaoCbx.setItems(FXCollections.observableArrayList(Desoneracao.NULL));
			desoneracaoCbx.getSelectionModel().select(0);
			
		}else {
			estadoCbx.setItems(FXCollections.observableArrayList(getCurrentReferencia().getEstados()));
			estadoCbx.getSelectionModel().select(0);
			desoneracaoCbx.setItems(FXCollections.observableArrayList(getCurrentReferencia().getDesoneracoes()));
			desoneracaoCbx.getSelectionModel().select(0);
			}		
	}

	public TabelaDeReferencia getCurrentReferencia() {
		return referenciaCbx.getSelectionModel().getSelectedItem();
	}
	public Estados getCurrentEstado() {
		return estadoCbx.getSelectionModel().getSelectedItem();
	}
	public Desoneracao getCurrentDesoneracao() {
		return desoneracaoCbx.getSelectionModel().getSelectedItem();
	}

	public void setParamater(Estados estado, Desoneracao desoneracao) {
		estadoCbx.getSelectionModel().select(estado);
		desoneracaoCbx.getSelectionModel().select(desoneracao);
	}
	public void setParamater(TabelaDeReferencia referencia) {
		referenciaCbx.getSelectionModel().select(referencia);
		updateLabels();
		}
	public void setParamater(Estados estado) {
		try{
			estadoCbx.getSelectionModel().select(estado);
		}catch(Exception e) {}
		}
	public void setParamater(Desoneracao desoneracao) {
		try{
			desoneracaoCbx.getSelectionModel().select(desoneracao);
		}catch(Exception e) {}
	}

	private String alertMessage;
	/**
	 * Ainda sujeito a testes.
	 */
	@FXML
	private void handleAlert() {
		if(!alertMessage.equals("")) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
	
			alert.setTitle("UFC9.Dolphin: Alerta");
			alert.setHeaderText("Mensagem de erro!");
			alert.setContentText(alertMessage == null ? "" : alertMessage);
			alert.showAndWait();
		}
	}

//	MultiDownloaderService service;
	private final Executor executor = Executors.newSingleThreadExecutor(r -> {
	    Thread t = new Thread(r, "controller-thread");
	    t.setDaemon(true);
	    return t;
	  });
	/**
	 * A��o que � chamada quando o bot�o de Baixar � pressionado.
	 */
	@FXML
	private void handleBaixar() {
		if (table.getSelectionModel().getSelectedItem() != null &&
				!saidaEnderecoText.getText().equals("")) {
			//Objeto de Task que faz o download in BAckground
			Task<Void> task = new MultiDownloaderInBackgroundJavaFx(
					table.getSelectionModel().getSelectedItems().stream().map(Link::getLink).collect(Collectors.toList()),
					saidaEnderecoText.getText());
			//Relaciona as informa��es de progesso � propriedade de porgresso da Task
			this.progressIndicator.progressProperty().unbind();
			this.progressIndicator.progressProperty().bind(task.progressProperty());
			
			this.labelProgress.textProperty().unbind();
			this.labelProgress.textProperty().bind(task.messageProperty());
			
			executor.execute(task);
		}else {
			alertMessage = "";
			if(getCurrentReferencia() == TabelaDeReferencia.SABESP) {alertMessage += "O referencial escolhido n�o fornece suporte de download.\n";}
			if(table.getSelectionModel().getSelectedItem() != null) alertMessage += "Selecione pelo menos 1 (um) link.\n";
			if(!saidaEnderecoText.getText().equals("")) alertMessage += "Endere�o de sa�da n�o pode estar vazio.\n";
			handleAlert();
		}
		
	}
	/**
	 * Altera o endere�o de sa�da onde os arquivos baixados ser�o salvos.
	 */
	@FXML
	private void handleAlterarSaida() {
		DirectoryChooser chooser = new DirectoryChooser();
		
		chooser.setTitle("UFC9.Dolphin");
		File selectedDirectory = chooser.showDialog(dialogStage);
		
		if (selectedDirectory != null) {
			saidaEnderecoText.setText(selectedDirectory.getAbsolutePath());
		}
	}

	/**
	 * A��o que � chamada quando o bot�o de atualizar � pressionado.
	 * <br>Atualiza a listView de links caso os par�metros de Refer�ncia, Estado e Desonera��o sejam diferentes de NULL.
	 */
	@FXML
	private void handleAtualizar() {

		alertMessage = "";
		if(getCurrentReferencia() == TabelaDeReferencia.SABESP) {alertMessage += "O referencial escolhido n�o fornece suporte de download.\n";}
		handleAlert();
		
		Task<ObservableList<Link>> task = new Task<ObservableList<Link>>()  {
			@Override
			protected void done() {
				super.succeeded();
				updateProgress(0, 1);
				updateMessage("");
				updateTitle("Atualizar");
			}
			@Override
			public ObservableList<Link> call() {
				Estados estado = estadoCbx.getSelectionModel().getSelectedItem();
				Desoneracao desoneracao = desoneracaoCbx.getSelectionModel().getSelectedItem();
				TabelaDeReferencia referencia = referenciaCbx.getSelectionModel().getSelectedItem();

				updateTitle("Atualizar*");
				
				if (estado == Estados.NULL || desoneracao == Desoneracao.NULL || referencia == TabelaDeReferencia.NULL) {
					links.setAll(FXCollections.observableArrayList());
				} else {
					switch (referencia) {
					case SINAPI:
						updateProgress(-1, -1);
						updateMessage("Aguarde...");
						Sinapi sinapi = new Sinapi(estado, desoneracao);
						links.setAll(FXCollections.observableArrayList(sinapi.getLinksObject(10)));
						return links;
					case SABESP:
					default:
						links.setAll(FXCollections.observableArrayList());
						break;
					}
				}
				return links;
			}
		};
		progressIndicator.progressProperty().unbind();
		progressIndicator.progressProperty().bind(task.progressProperty());
		
		labelProgress.textProperty().unbind();
		labelProgress.textProperty().bind(task.messageProperty());
		
		atualizarBtn.textProperty().unbind();
		atualizarBtn.textProperty().bind(task.titleProperty());

		table.itemsProperty().unbind();
		table.itemsProperty().bind(task.valueProperty());
		
		new Thread(task).start();
	}

	/**
	 * Atualiza o progresso na barra de progresso.
	 * @param message - Mensagem mostrada na label de progresso.
	 * @param progress - Valor do progresso entre 0 e 1. Use valores negativos para deixar como progresso indeterminado.
	 */
	protected void updateProgressBar(String message, Double progress) {
		labelProgress.setText(message);
		progressIndicator.setProgress(progress);
	}
	/**
	 * Atualiza as tags do bot�o de atualizar e o endere�o de sa�da.
	 * @param flag - true para indicar que n�o h� altera��es.
	 */
	private void updateTags(boolean flag) {
		atualizarBtn.textProperty().unbind();
		if(flag) {
			atualizarBtn.setText("Atualizar");
		}else {
			atualizarBtn.setText("Atualizar*");			
		}
		if (estadoCbx.getSelectionModel().getSelectedItem() != null && 
				estadoCbx.getSelectionModel().getSelectedItem() != Estados.NULL) {
			switch(referenciaCbx.getSelectionModel().getSelectedItem()) {
				case SABESP:
					saidaEnderecoText.setText(estadoCbx.getSelectionModel().getSelectedItem().getSabespDownloadEndereco());
					break;
				case SINAPI:
					saidaEnderecoText.setText(estadoCbx.getSelectionModel().getSelectedItem().getSinapiDownloadEndereco());
					break;
				case NULL:
				default:
					saidaEnderecoText.setText("");
			}
		}else {
			saidaEnderecoText.setText("");
		}
	}	
	
	public Stage getDialogStage() {
		return this.dialogStage;
	}
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
}
