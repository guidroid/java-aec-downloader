package ufc.deha.ufc9.dolphin.downloader;

import java.util.Map;


public class Sabesp implements PaginaWebTabelaDeReferencia {

	public Sabesp() {
		throw new IllegalStateException("Opção não disponível para download!");
	}
	/**
	 * Constroi o link mais recente com base na data do sistema.
	 * <p>Para ter certeza que é o mais recente, o método retorna apenas o link mais recente a retornar uma conexão válida.
	 * @return
	 * 		a endereço da web do download.
	 */
	@Override
	public String getMostRecentLink() {
		throw new IllegalStateException("Opção não disponível para download!");
	}
	/**
	 * Retorna um array de endereços de download do mais recente para o mais antigo.
	 * @param maxQuantity
	 * 		Tamanho do array de endereços.
	 * @return
	 * 		Array de endereços de download com estado definido no objeto.
	 */
	@Override
	public String[] getLinks(int maxQuantity) {
		throw new IllegalStateException("Opção não disponível para download!");
	}
	/**
	 * Retorna o endereço de download mais recente para os campos definidos no objeto com o título.
	 * <p> O resultado é um array de strings. O primeiro elemento é o link e o segundo o título.
	 * @return
	 * 		Array de strings do tipo: {endereço, título}.
	 */
	@Override
	public String[] getMostRecentLinkWithTitle() {
		throw new IllegalStateException("Opção não disponível para download!");
	}	
	/**
	 * Retorna os endereços de download para os campos definidos no objeto com o título.
	 * <p> O resultado é um mapa com entradas do tipo <Endereço, Título> array de strings. O primeiro elemento é o link e o segundo o título.
	 * @param
	 * 		maxQuantity - Quantidade de links a preencherem o mapa.
	 * @param
	 * 		stat - Desonerado ou não desonerado.
	 * @return
	 * 		Mapa com entradas do tipo : <endereço, título>.
	 */
	@Override
	public Map<String, String> getLinksWithTitle(int maxQuantity) {
		throw new IllegalStateException("Opção não disponível para download!");
	}
	/**
	 * Retorna um array de endereços de download do mais recente para o mais antigo.
	 * @param maxQuantity
	 * 		Quantidade de endereços de um mesmo estado a preencherem o array.
	 * @return
	 * 		Array de endereços de download com estado definido no objeto e tanto Desonerado quanto Não Desonerado..
	 */
	@Override
	public Map<String, String> getLinksWithTitleIndependentFromStatus(int maxQuantity) {
		throw new IllegalStateException("Opção não disponível para download!");
	}
}
