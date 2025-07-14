package ufc.deha.ufc9.dolphin.downloader.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FilenameUtils;

import javafx.concurrent.Task;
import ufc.deha.ufc9.dolphin.DolphinUtils;

public class UnzipInBackGroundJavaFx extends Task<Void> {
	// Link baixado atualmente
	protected int current = 1;
	// Endereço de saída.
	private String saveDirectory;
	// Lista de links para download.
	private List<String> parentFileNames = new ArrayList<>();
	// Lista de arquivos gerados pelo download.
	private List<String> fileNames = new ArrayList<>();

	private boolean override = true;

	/**
	 * Construtor.
	 * 
	 * @param saveDirectory
	 */
	public UnzipInBackGroundJavaFx() {
		super();
		this.saveDirectory = "";
		updateTitle("Unzip");
	}

	/**
	 * Construtor.
	 * 
	 * @param saveDirectory
	 */
	public UnzipInBackGroundJavaFx(String saveDirectory) {
		super();
		this.saveDirectory = saveDirectory;
		updateTitle("Unzip");
	}

	/**
	 * Construtor.
	 * 
	 * @param downloadURL
	 * @param saveDirectory
	 */
	public UnzipInBackGroundJavaFx(String parentFileName, String saveDirectory) {
		super();
		setParentFileNames(parentFileName);
		this.saveDirectory = saveDirectory;
		updateTitle("Unzip");
	}

	/**
	 * Construtor.
	 * 
	 * @param parentFileNames
	 * @param saveDirectory
	 */
	public UnzipInBackGroundJavaFx(Collection<String> parentFileNames, String saveDirectory) {
		super();
		setParentFileNames(parentFileNames);
		this.saveDirectory = saveDirectory;
		updateTitle("Unzip");
	}

	@Override
	protected Void call() throws IOException {
		// Mensagem de alerta
		String errorMessage = "";
		// Lista de saída.
		List<String> names = new ArrayList<>();

		current = 1;
		for (String pathFile : getParentFileNames()) {
			updateTitle(current + "/" + getParentFileNames().size());
			// Arquivo que se deseja descomprimir.
			File dir = new File(pathFile);
			// Arquivo de saída removendo caracteres incompatíveis.
			File outdir = new File(dir.getParent());// + File.separator + dir.getName().replaceFirst("[.][^.]+$", ""));
			// Garante que endereço existe.
			if (!outdir.exists())
				outdir.mkdirs();
			// Buffer de salvamento.
			byte[] buffer = new byte[1024];

			if (DolphinUtils.isUnzippable(pathFile)) {
				try (FileInputStream fis = new FileInputStream(dir);
						ZipInputStream zis = new ZipInputStream(fis, Charset.forName("UTF-8"));) {

					ZipEntry ze = zis.getNextEntry();

					while (ze != null && !isCancelled()) {
						updateProgress(0, 1);
						// Cria arquivo de saída.
						String filename = FilenameUtils.getName(ze.getName().replaceFirst("^[^a-zA-Z0-9]", "D"));
						File newFile = new File(outdir + File.separator + filename);
						// Atualiza a messagem (propriedade) com o endereço do arquivo atual.
						updateMessage(newFile.getAbsolutePath());

						if (ze.isDirectory() && !isCancelled()) {
							if (!newFile.exists())
								newFile.mkdirs();
						} else {
							// Garante que o diretório de criação exista.
							if (!new File(newFile.getParent()).exists())
								new File(newFile.getParent()).mkdirs();
							// Operação avalia o parâmetro de override para decidir se sobreescreve ou não.
							// Caso este seja falso, lança uma exceção.
							/*
							 * if (override) { if (newFile.exists()) newFile.delete(); } else { if
							 * (newFile.exists()) throw (new
							 * IOException("Não há autorização para sobresecreverw!")); }
							 */
							int i = 1;
							while (newFile.exists()) {// Evita sobrescrever arquivos enquanto o objeto fis está aberto.
								StringBuilder newName = new StringBuilder();
								newName.append(outdir).append(File.separator);
								newName.append(FilenameUtils.getBaseName(filename));
								newName.append("_").append(i).append(".");
								newName.append(FilenameUtils.getExtension(filename));

								String tmpFilename = newName.toString();
								newFile = new File(tmpFilename);
								i++;
							}
						}
						// Cria arquivo de saída.
						try (FileOutputStream fos = new FileOutputStream(newFile);) {

							long totalBytesRead = 0;
							long fileSize = ze.getSize();// newFile.length();

							int len;
							while ((len = zis.read(buffer)) > 0) {
								fos.write(buffer, 0, len);
								totalBytesRead += len;

								if (fileSize > 0) {
									this.updateProgress(totalBytesRead, fileSize);
								} else {
									this.updateMessage("Descomprimindo: " + (totalBytesRead / 1000.0) + "kB");
								}
							}

							names.add(newFile.getAbsolutePath());
						} catch (Exception e) {
//							e.printStackTrace();
							errorMessage += "\n" + filename + ": " + e.getMessage();
						}

						zis.closeEntry();
						ze = zis.getNextEntry();
					} // while
				} catch (IOException e) {
//					e.printStackTrace();
					errorMessage += "\n" + e.getMessage();
				} catch (IllegalArgumentException e) {
					// e.printStackTrace();
					// errorMessage += "\n" + e.getMessage();
				}
			} else {
				names.add(pathFile);
			}
		}
		setFileNames(names);
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


	public String getSaveDirectory() {
		return saveDirectory;
	}
	public void setSaveDirectory(String saveDirectory) {
		this.saveDirectory = saveDirectory;
	}
	public List<String> getFileNames() {
		return fileNames;
	}
	public void setFileNames(List<String> fileNames) {
		this.fileNames.clear();
		this.fileNames.addAll(fileNames);
	}
	public List<String> getParentFileNames() {
		return parentFileNames;
	}
	public void setParentFileNames(String parentFileNames) {
		this.parentFileNames.clear();
		this.parentFileNames.add(parentFileNames);
	}
	public void setParentFileNames(Collection<String> parentFileNames) {
		this.parentFileNames.clear();
		this.parentFileNames.addAll(parentFileNames);
	}
	public boolean isOverride() {
		return override;
	}
	public void setOverride(boolean override) {
		this.override = override;
	}
}
