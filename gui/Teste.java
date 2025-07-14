package ufc.deha.ufc9.dolphin.downloader.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ufc.deha.ufc9.dolphin.downloader.gui.view.UpdaterDolphinDataBaseController;

public class Teste extends Application {

	Stage primaryStage;
	BorderPane rootLayout;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;
			primaryStage.setTitle("UFC9.Dolphin");				
			initRootLayout();	
		}catch(Exception e) {
//			e.printStackTrace();
		}		
	}
	public void initRootLayout() {
		try {			
			//Carrega o RootLayout dentro do arquivo
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Teste.class.getResource("view/UpdaterDolphinDataBase.fxml"));
	    	rootLayout = (BorderPane) loader.load();

	    	((UpdaterDolphinDataBaseController) loader.getController()).setDialogStage(primaryStage);
			//Mostra a cena (Scene) contendo a RootLayout
	    	Scene scene = new Scene(rootLayout);
			
			primaryStage.setScene(scene);
			primaryStage.show();			
		} catch (IOException e) {
//			e.printStackTrace();
		}
	}
	

	public static void main(String[] args) {
		launch(args);
	}
}
