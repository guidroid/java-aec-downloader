public class DolphinBDViewer extends Application {

	private static Stage primaryStage;
	private static BorderPane rootLayout;

	@Override
	public void start(Stage primaryStage) {
		DolphinBDViewer.primaryStage = primaryStage;
		DolphinBDViewer.primaryStage.setTitle("XXX9.Dolphin");				
		initRootLayout();	
	}


	public void initRootLayout() {
		try {			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(DolphinBDViewer.class.getResource("gui/view/bd/BDViewer.fxml"));	
			DolphinBDViewer.rootLayout = (BorderPane) loader.load();
			Scene scene = new Scene(DolphinBDViewer.rootLayout);

			DolphinBDViewer.primaryStage.setScene(scene);
			BDViewerController controller = loader.getController();
			controller.setDialogStage(DolphinBDViewer.primaryStage);
						
			DolphinBDViewer.primaryStage.show();			
		} catch (IOException e) {
//			e.printStackTrace();
		}
	}
	public static void run() {
		launch(new String[0]);
	}
	public static void showBDViewer(Stage primaryStage) {
		try {			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(DolphinBDViewer.class.getResource("gui/view/bd/BDViewer.fxml"));	
			loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("XXX9.Dolphin: BDViewer");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			
			BDViewerController controller = loader.getController();
			controller.setDialogStage(dialogStage);
						
			dialogStage.showAndWait();			
		} catch (IOException e) {
//			e.printStackTrace();
		}
	}
}
