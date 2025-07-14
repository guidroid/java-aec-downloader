package ufc.deha.ufc9.dolphin.downloader.gui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import javafx.concurrent.Task;
import ufc.deha.ufc9.dolphin.banco.BancoDolphin;
import ufc.deha.ufc9.dolphin.utils.SourceFileMap;

public class UpdateDataBaseInBackgroundJavaFx extends Task<Void> {
	private BancoDolphin banco;
	private SourceFileMap sourceFiles; 
	//String[] sourceFiles = new String[0];

	/**
	 * Construtor.
	 */
	public UpdateDataBaseInBackgroundJavaFx() {
		super();
		this.banco = null;
		updateTitle("Updater");
	}

	/**
	 * Construtor.
	 * @param banco - Banco de dados a ser alimentado.
	 */
	public UpdateDataBaseInBackgroundJavaFx(BancoDolphin banco) {
		super();
		this.banco = banco;
		updateTitle("Updater");
	}

	/**
	 * Construtor.
	 * @param banco - Banco de dados
	 * @param sourceFiles - Arquivos fonte
	 */
	public UpdateDataBaseInBackgroundJavaFx(BancoDolphin banco, SourceFileMap sourceFiles) {//String[] sourceFiles) {
		super();
		this.banco = banco;
		this.sourceFiles = sourceFiles;
		try{ updateTitle("Updater");} catch(Exception e) {}
	}
	
	@Override
	protected Void call() throws IOException {
		String errorMessage = "";

		try {
			banco.getProgressProperty().addListener((observable, oldvalue, newvalue) -> {
				this.updateProgress(newvalue.doubleValue(), 1);
				if (newvalue.doubleValue() >= 0) {
					this.updateMessage(((int) (newvalue.doubleValue() * 100)) + "%");
				} else {
					this.updateMessage("...");
				}
			});
		} catch (Exception e) {}
		try {
			banco.setSourceFiles(sourceFiles);
			banco.loadSourceFiles();
			banco.fillTables();
		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
			errorMessage += "\n" + e.getMessage();
		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
			errorMessage += "\n" + e.getMessage();
		} catch (FileNotFoundException e) {
//			e.printStackTrace();
			errorMessage += "\n" + e.getMessage();
		} catch (IOException e) {
//			e.printStackTrace();
			errorMessage += "\n" + e.getMessage();
		} catch (SQLException e) {
//			e.printStackTrace();
			errorMessage += "\n" + e.getMessage();
		}
		
		updateTitle("");
		updateMessage("");

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

	/**
	 * @return the banco
	 */
	public BancoDolphin getBanco() {
		return banco;
	}
	/**
	 * @param banco the banco to set
	 */
	public void setBanco(BancoDolphin banco) {
		this.banco = banco;
	}
	/**
	 * @return the sourceFiles
	 */
	public SourceFileMap getSourceFiles() {//String[] getSourceFiles() {
		return sourceFiles;
	}
	/**
	 * @param sourceFiles the sourceFiles to set
	 */
	public void setSourceFiles(SourceFileMap sourceFiles){//String[] sourceFiles) {
		this.sourceFiles = sourceFiles;
	}
}
