package ufc.deha.ufc9.dolphin.downloader.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipException;

import javax.swing.JOptionPane;

import ufc.deha.ufc9.dolphin.DolphinUtils;
import ufc.deha.ufc9.dolphin.downloader.*;
import ufc.deha.ufc9.dolphin.referencia.TabelaDeReferencia;
import ufc.deha.ufc9.dolphin.referencia.Estados;
//Trabalhar nesse agora.
public final class DolphinDownloaderInBackground {

	/**
	 * Baixa os arquivos de referência (especialmente para SINAPI) a partir do estado.
	 * <p>Os arquivos baixados são tanto Desonerado quanrto não desonerado.
	 * @param sigla
	 * 		Sigla do estado.
	 * @param fonte
	 * 		Tabela de Referência a ser usada para download
	 * @param unzip
	 * 		True se deseja que arquivos descomprimidos sejam baixados.
	 * @return
	 * 		O endereço com o endereço dos arquivos baixados na pasta final.
	 */
	public static String[] downloadFile(Estados sigla, TabelaDeReferencia fonte, boolean unzip) {
		MultiDownloaderInBackground downloader;
		String[] links;
		
		switch(fonte) {
		case SINAPI:
			//Pega os links de desonerado e não desonerado.
			links = Sinapi.getMostRecentLinkIdependentFromDesoneracao(sigla).values().toArray(new String[0]);
			//Executa o download.
			downloader = new MultiDownloaderInBackground(links, sigla.getSinapiEndereco());
			downloader.execute();
			
			List<String> retorno = new ArrayList<>();
			if (unzip) {
				for (String fileNameZipped : downloader.getFileNames()) {
					if (DolphinUtils.isUnzippable(fileNameZipped)){
						try {
							retorno.addAll(DolphinUtils.unzipFile(fileNameZipped));
						}catch(ZipException e) {//Caso o arquivo não possa sere descompactado.
							retorno.add(fileNameZipped);
						}catch(IOException e) {//Caso o arquivo não possa ser aberto.
						}
					}else {
						retorno.add(fileNameZipped);
					}
				}
				return retorno.toArray(new String[0]);
			}else {
				return downloader.getFileNames();				
			}
		case SABESP:
			new Sabesp(); 
			JOptionPane.showMessageDialog(null, "Opção de download não disponível!\nErro: {Tabela de Referência = SABESP}", "UFC9.Dolphin: Alerta", JOptionPane.ERROR_MESSAGE);
			return new String[0];
		default:
			JOptionPane.showMessageDialog(null, "Opção de download não disponível!\nErro: {Tabela de Referência = desconhecida}", "UFC9.Dolphin: Alerta", JOptionPane.ERROR_MESSAGE);
			return new String[0];
		}
	}
	/**
	 * Baixa os arquivos de referência (especialmente para SINAPI) a partir do estado.
	 * <p>Os arquivos baixados são tanto Desonerado quanrto não desonerado.
	 * @param sigla
	 * 		Sigla do estado.
	 * @param fonte
	 * 		Tabela de Referência a ser usada para download
	 * @param unzip
	 * 		True se deseja que arquivos descomprimidos sejam baixados.
	 * @return
	 * 		O endereço com o endereço dos arquivos baixados na pasta final.
	 */
	public static String[] downloadFile(String siglaEstado, TabelaDeReferencia fonte, boolean unzip) {
		return downloadFile(Estados.valueOf(siglaEstado.toUpperCase()), fonte, unzip);
	}
	/**
	 * Baixa os arquivos de referência (especialmente para SINAPI) a partir do estado.
	 * <p>Os arquivos baixados são tanto Desonerado quanrto não desonerado.
	 * @param sigla
	 * 		Sigla do estado.
	 * @param fonte
	 * 		Tabela de Referência a ser usada para download
	 * @param unzip
	 * 		True se deseja que arquivos descomprimidos sejam baixados.
	 * @return
	 * 		O endereço com o endereço dos arquivos baixados na pasta final.
	 */
	public static String[] downloadFile(String siglaEstado, String fonte, boolean unzip) {
		return downloadFile(Estados.valueOf(siglaEstado.toUpperCase()), TabelaDeReferencia.valueOf(fonte.toUpperCase()), unzip);
	}
	
	/**
	 * Baixa os arquivos de referência (especialmente para SINAPI) a partir do estado.
	 * <p>Os arquivos baixados são tanto Desonerado quanto não desonerado. Os arquivos zipados são sempre descompatados.
	 * @param sigla
	 * 		Sigla do estado.
	 * @param fonte
	 * 		Tabela de Referência a ser usada para download
	 * @param unzip
	 * 		True se deseja que arquivos descomprimidos sejam baixados.
	 * @return
	 * 		O endereço com o endereço dos arquivos baixados na pasta final.
	 */
	public static String[] downloadFile(Estados sigla, TabelaDeReferencia fonte) {
		return downloadFile(sigla, fonte, true);
		}
	/**
	 * Baixa os arquivos de referência (especialmente para SINAPI) a partir do estado.
	 * <p>Os arquivos baixados são tanto Desonerado quanto não desonerado. Os arquivos zipados são sempre descompatados.
	 * @param sigla
	 * 		Sigla do estado.
	 * @param fonte
	 * 		Tabela de Referência a ser usada para download
	 * @param unzip
	 * 		True se deseja que arquivos descomprimidos sejam baixados.
	 * @return
	 * 		O endereço com o endereço dos arquivos baixados na pasta final.
	 */
	public static String[] downloadFile(String siglaEstado, TabelaDeReferencia fonte) {
		return downloadFile(Estados.valueOf(siglaEstado.toUpperCase()), fonte, true);
	}
	/**
	 * Baixa os arquivos de referência (especialmente para SINAPI) a partir do estado.
	 * <p>Os arquivos baixados são tanto Desonerado quanto não desonerado. Os arquivos zipados são sempre descompatados.
	 * @param sigla
	 * 		Sigla do estado.
	 * @param fonte
	 * 		Tabela de Referência a ser usada para download
	 * @param unzip
	 * 		True se deseja que arquivos descomprimidos sejam baixados.
	 * @return
	 * 		O endereço com o endereço dos arquivos baixados na pasta final.
	 */
	public static String[] downloadFile(String siglaEstado, String fonte) {
		return downloadFile(Estados.valueOf(siglaEstado.toUpperCase()), TabelaDeReferencia.valueOf(fonte.toUpperCase()), true);
	}
}
