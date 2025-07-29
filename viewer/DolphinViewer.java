package ufc.deha.ufc9.dolphin.viewer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ufc.deha.ufc9.dolphin.Descriptions;
import ufc.deha.ufc9.dolphin.Dolphin;
import ufc.deha.ufc9.dolphin.composicao.ItemDolphin;
import ufc.deha.ufc9.dolphin.referencia.DefaultUFC9;
import ufc.deha.ufc9.dolphin.viewer.gui.model.reader.dolphin.DolphinReadBlock;
import ufc.deha.ufc9.dolphin.viewer.gui.view.ConsultasViewController;
import ufc.deha.ufc9.dolphin.viewer.gui.view.OverViewController;
import ufc.deha.ufc9.dolphin.viewer.gui.view.SingleViewController;

public class DolphinViewer extends Application{
	
	private static Stage primaryStage;
	private static BorderPane rootLayout;

	/**
	 * DolphinReadBlck selecionado.
	 */
	public static int selected = 0;
	
	public static ObservableMap<String, ItemDolphin> GENERATED_ITENS = FXCollections.observableHashMap();
	
	public static ObservableMap<String, ItemDolphin> getGeneratedItens() {
		return GENERATED_ITENS;
	}

	public static ObservableList<DolphinReadBlock> list = FXCollections.observableArrayList();
	
	/*private static void setGeneratedItens(ObservableMap<String, ItemDolphin> itens) {
		DolphinViewer.GENERATED_ITENS = itens;
	}*/
	public static void generateItens() {
		Map<String, ItemDolphin> backupMap = new HashMap<>();
		for(DolphinReadBlock block : DolphinViewer.list) {
			if (block.getDescricao() != "" && block.getSelected() != null) {
				backupMap.put(block.getDescricao(), block.getSelected());
			}
		}
		DolphinViewer.GENERATED_ITENS.clear();
		DolphinViewer.GENERATED_ITENS.putAll(backupMap);
	}
	
	public static ObservableList<DolphinReadBlock> getList() {
		return list;
	}
	/**
	 * Método adidionado na versão 1.1.
	 * @param descricao
	 * @param itens
	 */
	public static void addToList(String descricao, List<ItemDolphin> itens) {
		DolphinViewer.list.add(new DolphinReadBlock(descricao, itens));
	}
	/**
	 * Método adidionado na versão 1.1.
	 * @param descricao
	 * @param itens
	 */
	public static void addToList(Map<String, List<ItemDolphin>> map) {
		for(Map.Entry<String, List<ItemDolphin>> entry : map.entrySet()) {
			DolphinViewer.list.add(new DolphinReadBlock(entry));
		}
	}
	/**
	 * @param list
	 */
	public static void setList(ObservableList<DolphinReadBlock> list) {
		DolphinViewer.list.clear();
		DolphinViewer.list = list;
	}
	public static void setList(Map<String, List<ItemDolphin>> map) {
		DolphinViewer.list.clear();
		
		for(Map.Entry<String, List<ItemDolphin>> entry : map.entrySet()) {
			DolphinViewer.list.add(new DolphinReadBlock(entry));			
		}
	}
	
	public static int getSelected() {
		return selected;
	}
	public static void setSelected(int selected) {
		DolphinViewer.selected = selected;
	}
	@Override
	public void start(Stage primaryStage) {
		try {
			DolphinViewer.primaryStage = primaryStage;
			DolphinViewer.primaryStage.setTitle("UFC9.Dolphin");				
			initRootLayout();	
			//DataLoader mainApp = new DataLoader();
			
			showSingleView();
			
		}catch(Exception e) {
//			e.printStackTrace();
		}		
	}
	
	public void initRootLayout() {
		try {			
			//Carrega o RootLayout dentro do arquivo
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(DolphinViewer.class.getResource("gui/view/RootLayout.fxml"));		
			rootLayout = (BorderPane) loader.load();
			//Mostra a cena (Scene) contendo a RootLayout
			Scene scene = new Scene(rootLayout);
			//DolphinViewer.addCSSToScene(scene);
			
			primaryStage.setScene(scene);
			primaryStage.show();			
		} catch (IOException e) {
//			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param mainApp
	 * @return
	 */
	public static boolean showOverview(Stage primaryStage) {//DolphinViewer mainApp) {//ItemComumFX itenmx				
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(DolphinViewer.class.getResource("gui/view/Overview.fxml"));

			AnchorPane overView = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			
			dialogStage.setScene(new Scene(overView));
			dialogStage.setTitle(OverViewController.TITLE);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);

			// Define o person overview dentro do root layout.
			//rootLayout.setCenter(overView);

			// Dá ao controlador acesso à the main app.
			OverViewController controller = loader.getController();
			//controller.setPrimaryStage(primaryStage);
			controller.setDialogStage(dialogStage);
			controller.setPrimaryStage(primaryStage);
			//controller.setDolphinViewer(primaryStage);//mainApp);
			// controller.setItem(list.get(selected));

			dialogStage.showAndWait();

			return controller.isOkClicked();

		} catch (IOException e) {
//			e.printStackTrace();
			return false;
		}
	}
	
	public static void main(String [] args) {
		launch(args);
	}
	public static void showSingleView() {//DolphinViewer mainApp) {
		
		try {
			//Carrega a PersonOverview
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(DolphinViewer.class.getResource("gui/view/SingleView.fxml"));			
			AnchorPane singleView = (AnchorPane) loader.load();		
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle(SingleViewController.TITLE);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			
			 // Define o person overview dentro do root layout.
			rootLayout.setCenter(singleView);

	        // Dá ao controlador acesso à the main app.
	        SingleViewController controller = loader.getController();	
	        controller.setDialogStage(primaryStage);  
	        controller.setPrimaryStage(primaryStage);      
	       // controller.setMainApp(mainApp);
	        //controller.setItem(list.get(selected));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	}
	public static void showSingleView(Stage primaryStage) {//DolphinViewer mainApp) {
		
		try {
			//Carrega a PersonOverview
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(DolphinViewer.class.getResource("gui/view/SingleView.fxml"));			
			AnchorPane singleView = (AnchorPane) loader.load();		
			
			Stage dialogStage = new Stage();
			dialogStage.setScene(new Scene(singleView));
			dialogStage.setTitle(SingleViewController.TITLE);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			
			 // Define o person overview dentro do root layout.
			//rootLayout.setCenter(singleView);

	        // Dá ao controlador acesso à the main app.
	        SingleViewController controller = loader.getController();	
	        controller.setDialogStage(dialogStage);
	        controller.setPrimaryStage(primaryStage);
	       // controller.setMainApp(mainApp);
	        //controller.setItem(list.get(selected));
			dialogStage.showAndWait();
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	}
	public static void showConsultas(Stage primaryStage) {
		
		try {
			//Carrega a PersonOverview
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(DolphinViewer.class.getResource("gui/view/ConsultasView.fxml"));			
			AnchorPane consultasView = (AnchorPane) loader.load();		
			
			Stage dialogStage = new Stage();
			dialogStage.setScene(new Scene(consultasView));
			dialogStage.setTitle(ConsultasViewController.TITLE);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			
	        ConsultasViewController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setPrimaryStage(primaryStage);

			dialogStage.showAndWait();
			if(controller.isOKClicked()) {
				DefaultUFC9.readDefaultFile();
				Descriptions descrObj;
				try {
					descrObj = new Descriptions( Dolphin.openBanco(), controller.getList().toArray(new String[0]));
					DolphinViewer.addToList(descrObj.itemBrowse());
					DolphinViewer.showSingleView(primaryStage);
					
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				} catch (Exception e){
//					e.printStackTrace();
				}
				
			}
		} catch (IOException e) {
//			e.printStackTrace();
		}
	}
	/**
	 * Retorna o palco principal.
	 * @return
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	/**
	 * Método de lançamento do objeto.
	 * <br>Antes de usar o método garantir que {@link DolphinViewer#list} não esteja vazia.
	 */
	public static void run() {
		launch();
	}
}