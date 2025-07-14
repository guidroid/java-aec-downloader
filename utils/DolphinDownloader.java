package ufc.deha.ufc9.dolphin.downloader.utils;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

import ufc.deha.ufc9.dolphin.downloader.*;
import ufc.deha.ufc9.dolphin.referencia.TabelaDeReferencia;
import ufc.deha.ufc9.dolphin.referencia.Estados;


public final class DolphinDownloader {

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
		MultiDownloaderMain downloader;
		String[] links;
		
		switch(fonte) {
		case SINAPI:
			links = Sinapi.getMostRecentLinkIdependentFromDesoneracao(sigla).values().toArray(new String[0]);
			//System.out.println("Endereço: " + sigla.getSinapiEndereco());
			downloader = new MultiDownloaderMain(links, sigla.getSinapiDownloadEndereco());
			try{
				FileUtils.cleanDirectory(new File(sigla.getSinapiDownloadEndereco()));
				return downloader.download();
			}catch(IOException e) {
				//e.printStackTrace();
				JOptionPane.showMessageDialog(null,  "Download não realizado!","UFC9.Dolphin: Menssagem de alerta", JOptionPane.ERROR_MESSAGE);
				return new String[0];
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
