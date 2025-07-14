package ufc.deha.ufc9.dolphin.downloader;

import java.util.Map;

/**
 * Interface a ser implementada por classes que geram links de download a tabelas de refer�ncias.
 * @version 1.0
 * @since 1.0
 * 
 * @author Guilherme Ribeiro  
 */
public interface PaginaWebTabelaDeReferencia {

	/**
	 * Constroi o link mais recente com base na data do sistema.
	 * <p>Para ter certeza que � o mais recente, o m�todo retorna apenas o link mais recente a retornar uma conex�o v�lida.
	 * @return
	 * 		a endere�o da web do download.
	 */
	public String getMostRecentLink();
	/**
	 * Retorna um array de endere�os de download do mais recente para o mais antigo.
	 * @param maxQuantity
	 * 		Tamanho do array de endere�os.
	 * @return
	 * 		Array de endere�os de download com estado definido no objeto.
	 */
	public String[] getLinks(int quantity);
	/**
	 * Retorna o endere�o de download mais recente para os campos definidos no objeto com o t�tulo.
	 * <p> O resultado � um array de strings. O primeiro elemento � o link e o segundo o t�tulo.
	 * @return
	 * 		Array de strings do tipo: {endere�o, t�tulo}.
	 */
	public String[] getMostRecentLinkWithTitle();
	/**
	 * Retornade os endere�os de download para os campos definidos no objeto com o t�tulo.
	 * <p> O resultado � um mapa com entradas do tipo <Endere�o, T�tulo> array de strings. O primeiro elemento � o link e o segundo o t�tulo.
	 * @param
	 * 		maxQuantity - Quantidade de links a preencherem o mapa.
	 * @param
	 * 		stat - Desonerado ou n�o desonerado.
	 * @return
	 * 		Mapa com entradas do tipo : <endere�o, t�tulo>.
	 */
	public Map<String, String> getLinksWithTitle(int quantity);
	/**
	 * Retorna um array de endere�os de download do mais recente para o mais antigo.
	 * @param maxQuantity
	 * 		Quantidade de endere�os de um mesmo estado a preencherem o array.
	 * @return
	 * 		Array de endere�os de download com estado definido no objeto e tanto Desonerado quanto N�o Desonerado..
	 */
	public Map<String, String> getLinksWithTitleIndependentFromStatus(int quantity);
}
