package ufc.deha.ufc9.dolphin.downloader;

import java.util.Map;


public class Sabesp implements PaginaWebTabelaDeReferencia {

	public Sabesp() {
		throw new IllegalStateException("Op��o n�o dispon�vel para download!");
	}
	/**
	 * Constroi o link mais recente com base na data do sistema.
	 * <p>Para ter certeza que � o mais recente, o m�todo retorna apenas o link mais recente a retornar uma conex�o v�lida.
	 * @return
	 * 		a endere�o da web do download.
	 */
	@Override
	public String getMostRecentLink() {
		throw new IllegalStateException("Op��o n�o dispon�vel para download!");
	}
	/**
	 * Retorna um array de endere�os de download do mais recente para o mais antigo.
	 * @param maxQuantity
	 * 		Tamanho do array de endere�os.
	 * @return
	 * 		Array de endere�os de download com estado definido no objeto.
	 */
	@Override
	public String[] getLinks(int maxQuantity) {
		throw new IllegalStateException("Op��o n�o dispon�vel para download!");
	}
	/**
	 * Retorna o endere�o de download mais recente para os campos definidos no objeto com o t�tulo.
	 * <p> O resultado � um array de strings. O primeiro elemento � o link e o segundo o t�tulo.
	 * @return
	 * 		Array de strings do tipo: {endere�o, t�tulo}.
	 */
	@Override
	public String[] getMostRecentLinkWithTitle() {
		throw new IllegalStateException("Op��o n�o dispon�vel para download!");
	}	
	/**
	 * Retorna os endere�os de download para os campos definidos no objeto com o t�tulo.
	 * <p> O resultado � um mapa com entradas do tipo <Endere�o, T�tulo> array de strings. O primeiro elemento � o link e o segundo o t�tulo.
	 * @param
	 * 		maxQuantity - Quantidade de links a preencherem o mapa.
	 * @param
	 * 		stat - Desonerado ou n�o desonerado.
	 * @return
	 * 		Mapa com entradas do tipo : <endere�o, t�tulo>.
	 */
	@Override
	public Map<String, String> getLinksWithTitle(int maxQuantity) {
		throw new IllegalStateException("Op��o n�o dispon�vel para download!");
	}
	/**
	 * Retorna um array de endere�os de download do mais recente para o mais antigo.
	 * @param maxQuantity
	 * 		Quantidade de endere�os de um mesmo estado a preencherem o array.
	 * @return
	 * 		Array de endere�os de download com estado definido no objeto e tanto Desonerado quanto N�o Desonerado..
	 */
	@Override
	public Map<String, String> getLinksWithTitleIndependentFromStatus(int maxQuantity) {
		throw new IllegalStateException("Op��o n�o dispon�vel para download!");
	}
}
