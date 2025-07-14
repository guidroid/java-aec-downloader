package ufc.deha.ufc9.dolphin.downloader;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ufc.deha.ufc9.dolphin.referencia.Desoneracao;
import ufc.deha.ufc9.dolphin.referencia.Estados;
/**
 * Classe geradora de links para download das tabelas de refer�ncia da SINAPI.
 * 
 * v1.1: 
 * 	- Adicionado limite de buscas para 12 buscas no link mais recente.
 * 	- Consertado o erro na constru��o do link adicionando mais um indicador referente ai estado. 
 * @version 1.1
 * @since 1.0
 * 
 * @author Guilherme Ribeiro  
 */
public class Sinapi implements PaginaWebTabelaDeReferencia{
	/**
	 * Link usado como base para construir os links de download dos arquivos zipados.
	 */
	public static final String linkBase = "http://www.caixa.gov.br/Downloads/sinapi-a-partir-jul-2009-%r/SINAPI_ref_Insumos_Composicoes_%r_%r%r_%r.zip";
	//http://www.caixa.gov.br/Downloads/sinapi-a-partir-jul-2009-sp/SINAPI_ref_Insumos_Composicoes_SP_052020_Desonerado.zip
	/**
	 * Elemento que ser� substitu�do por um valor para a constru��o do link. 
	 */
	public static final String INDICATOR = "%r";
	/**
	 * Estado de refer�ncia.
	 */
	private Estados estadoSigla;
	/**
	 * Estado de desonera��o.
	 */
	private Desoneracao desoneracao;
	
	/**
	 * Inicializa os campos de estado e status.
	 * @param estado
	 * 		Estado de refer�ncia
	 * @param desoneracao
	 * 		Desonera��o
	 */
	public Sinapi(String estado, Desoneracao desoneracao) {
		this.estadoSigla = Estados.valueOf(estado.toUpperCase());
		this.desoneracao = desoneracao;
	}
	/**
	 * Inicializa os campos de estado e status.
	 * @param estadoSigla
	 * @param status
	 */
	public Sinapi(Estados estadoSigla, Desoneracao desoneracao) {
		this.estadoSigla = estadoSigla;
		this.desoneracao = desoneracao;
		getMostRecentLink();
	}
	/**
	 * @return
	 * 		o estado.
	 */
	public Estados getEstadoSigla() {
		return estadoSigla;
	}
	/**
	 * @return
	 * 		a desonera��o.
	 */
	public Desoneracao getDesoneracao() {
		return desoneracao;
	}
	/**
	 * @param estadoSigla
	 * 		o estado que se deseja setar.
	 */
	public void setEstadoSigla(Estados estadoSigla) {
		this.estadoSigla = estadoSigla;
	}
	/**
	 * @param desoneracao
	 * 		o estado da desonera��o que se deseja setar.
	 */
	public void setDesoneracao(Desoneracao desoneracao) {
		this.desoneracao = desoneracao;
	}

	/**
	 * Constroi o link mais recente com base na data do sistema.
	 * <p>Para ter certeza que � o mais recente, o m�todo retorna apenas o link mais recente a retornar uma conex�o v�lida.
	 * @return
	 * 		a endere�o da web do download.
	 */
	public String getMostRecentLink() {
		boolean found = false;
		String link = "";
		int increment = 0;
		
		while(!found) {
			Integer[] date = FontPage.getOlderDateMonthsInIntegerArray((long) increment);
			String linkTmp = generateLink(estadoSigla, date[0], date[1], desoneracao);
			if (FontPage.checkURL(linkTmp)) {
				link = linkTmp;
				found = true;
			}
			increment++;
		}	
		return link;
	}
	
	/**
	 * Retorna um array de endere�os de download do mais recente para o mais antigo.
	 * @param maxQuantity
	 * 		Tamanho do array de endere�os.
	 * @return
	 * 		Array de endere�os de download com estado e status definidos no objeto.
	 */
	public String[] getLinks(int maxQuantity) {
		List<String> links = new ArrayList<>();
		
		for (int i = 0 ; i < maxQuantity; i++) {
			Integer[] date = FontPage.getOlderDateMonthsInIntegerArray((long) i);
		
			String link = generateLink(estadoSigla, date[0], date[1], desoneracao);
			if (FontPage.checkURL(link)) {
				links.add(link);
			}
			
		}
		
		return links.toArray(new String[0]);		
	}
	/**
	 * Retorna um array de endere�os de download do mais recente para o mais antigo.
	 * @param maxQuantity
	 * 		Tamanho do array de endere�os.
	 * @return
	 * 		Array de endere�os de download com estado definido no objeto.
	 */
	public String[] getLinks(int maxQuantity, Desoneracao desoneracao) {
		List<String> links = new ArrayList<>();
		
		for (int i = 0 ; i < maxQuantity; i++) {
			Integer[] date = FontPage.getOlderDateMonthsInIntegerArray((long) i);
			String link = generateLink(estadoSigla, date[0], date[1], desoneracao);
			if (FontPage.checkURL(link)) {
				links.add(link);
			}
		}
		
		return links.toArray(new String[0]);
		
	}
	/**
	 * Retorna um array de endere�os de download do mais recente para o mais antigo.
	 * @param maxQuantity
	 * 		Quantidade de endere�os de um mesmo estado a preencherem o array.
	 * @return
	 * 		Array de endere�os de download com estado definido no objeto e tanto Desonerado quanto N�o Desonerado..
	 */
	public String[] getLinksIndependentFromStatus(int maxQuantity) {
		List<String> links = new ArrayList<>();
		
		for (int i = 0 ; i < maxQuantity; i++) {
			Integer[] date = FontPage.getOlderDateMonthsInIntegerArray((long) i);
			for(Desoneracao desoneracao : Desoneracao.values()) {
				String link = generateLink(estadoSigla, date[0], date[1], desoneracao);
				if (FontPage.checkURL(link)) {
					links.add(link);
				}
			}
		}		
		return links.toArray(new String[0]);
	}
	
	/**
	 * Retorna o endere�o de download mais recente para os campos definidos no objeto com o t�tulo.
	 * <p> O resultado � um array de strings. O primeiro elemento � o link e o segundo o t�tulo.
	 * @return
	 * 		Array de strings do tipo: {endere�o, t�tulo}.
	 */
	@Override
	public String[] getMostRecentLinkWithTitle() {
		String link = getMostRecentLink();

		try {
			String urlPath = new URL(link).getPath();
			String title = urlPath.substring(urlPath.lastIndexOf('/') + 1);
	        
	        return new String[] {link, title};
		} catch (IOException e) {
	        
	        return new String[2];
		}  
    }
	/**
	 * Retornade os endere�os de download para os campos definidos no objeto com o t�tulo.
	 * <p> O resultado � um mapa com entradas do tipo <Endere�o, T�tulo> array de strings. O primeiro elemento � o link e o segundo o t�tulo.
	 * @param
	 * 		maxQuantity - Quantidade de links a preencherem o mapa.
	 * @return
	 * 		Mapa com entradas do tipo : <endere�o, t�tulo>.
	 */
	@Override
	public Map<String, String> getLinksWithTitle(int maxQuantity) {
		String[] links = getLinks(maxQuantity);
		Map<String, String> retorno = new LinkedHashMap<>();
		for (String link : links) {
			try {			
				String urlPath = new URL(link).getPath();
				String title = urlPath.substring(urlPath.lastIndexOf('/') + 1);
				
		        retorno.put(link,  title);
			} catch (IOException e) {}
		}
		return retorno;		
	}

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
	public Map<String, String> getLinksWithTitle(int maxQuantity, Desoneracao desoneracao) {
		String[] links = getLinks(maxQuantity, desoneracao);
		Map<String, String> retorno = new LinkedHashMap<>();
		for (String link : links) {
			try {
				String urlPath = new URL(link).getPath();
				String title = urlPath.substring(urlPath.lastIndexOf('/') + 1);
				
		        retorno.put(link,  title);
			} catch (IOException e) {}
		}
		return retorno;		
	}
	/**
	 * Retornade os endere�os de download para os campos definidos no objeto com o t�tulo.
	 * <p> O resultado � um mapa com entradas do tipo <Endere�o, T�tulo> array de strings. O primeiro elemento � o link e o segundo o t�tulo.
	 * @param maxQuantity
	 * 		Quantidade de links a preencherem o mapa de cada estado: Desonerado e n�o desonerado.
	 * @return
	 * 		Mapa com entradas do tipo : <endere�o, t�tulo>.
	 */
	@Override
	public Map<String, String> getLinksWithTitleIndependentFromStatus(int maxQuantity) {
		String[] links = getLinksIndependentFromStatus(maxQuantity);
		Map<String, String> retorno = new LinkedHashMap<>();
		for (String link : links) {
			try {
				String urlPath = new URL(link).getPath();
				String title = urlPath.substring(urlPath.lastIndexOf('/') + 1);
				
		        retorno.put(link,  title);
			} catch (IOException e) {}
		}
		return retorno;		
	}
	/**
	 * Gera o link com base nas informa��es de estado, m�s, ano e status.
	 * <p>N�o h� checagem da conex�o. Apenas gera o endere�o. Para verificar a exist�ncia, use {@link FontPage#checkURL(String)}.
	 * @param estado
	 * 		Estado de refer�ncia
	 * @param month
	 * 		M�s do arquivo de refer�ncia.
	 * @param year
	 * 		Ano do arquivo de refer�ncia.
	 * @param desoneracao
	 * 		Estado de desonera��o.
	 * @return
	 * 		Endere�o do download.
	 */
	private static String generateLink(Estados estado, Integer month, Integer year, Desoneracao desoneracao) {
		String retorno = linkBase;
		
		retorno = retorno.replaceFirst(INDICATOR, estado.getSigla().toLowerCase());
		retorno = retorno.replaceFirst(INDICATOR, estado.getSigla());
		retorno = retorno.replaceFirst(INDICATOR, String.format("%02d", month));
		retorno = retorno.replaceFirst(INDICATOR, Integer.toString(year));
		retorno = retorno.replaceFirst(INDICATOR, desoneracao.getDesoneracao());

		return retorno;
	}
	
	/**
	 * Retorna o link mais recente.
	 * @param estado
	 * 		sigla do estado.
	 * @param desoneracao
	 * 		estado Desonerado ou N�o Desonerado.
	 * @return
	 * 		Link do arquivo mais recente com as informa��es fornecidas.
	 */
	public static String getMostRecentLink(Estados estado, Desoneracao desoneracao) {
		boolean found = false;
		String link = "";
		int increment = 0;
		
		while(!found && increment < 12) {
			Integer[] date = FontPage.getOlderDateMonthsInIntegerArray((long) increment);
			String linkTmp = generateLink(estado, date[0], date[1], desoneracao);
			//System.out.println(linkTmp);
			if (FontPage.checkURL(linkTmp)) {
				link = linkTmp;
				found = true;
			}
			increment++;
		}	
		return link;
	}
	/**
	 * Retorna o link mais recente Desonerado e N�o desonerado para o estado fornecido.
	 * @param estado
	 * 		Estado de refer�ncia.
	 * @return
	 * 		Link do arquivo mais recente para o estado independente da desonera��o. 		
	 */
	public static Map<Desoneracao, String> getMostRecentLinkIdependentFromDesoneracao(Estados estado) {
		Map<Desoneracao, String> retorno = new HashMap<>();
		retorno.put(Desoneracao.DESONERADO, getMostRecentLink(estado, Desoneracao.DESONERADO));
		retorno.put(Desoneracao.NAO_DESONERADO, getMostRecentLink(estado, Desoneracao.NAO_DESONERADO));
		
		return retorno;
	}
	/**
	 * Retorna o link mais recente para cada estado.
	 * @param desoneracao
	 * 		Estado da desonera��o.
	 * @return
	 * 		Link mais recente para cada estado.
	 */
	public static Map<Estados, String> getMostRecentLinkFromAllStates(Desoneracao desoneracao){
		Map<Estados, String> retorno = new HashMap<>();
		for(Estados sigla : Estados.values()) {
			retorno.put(sigla, getMostRecentLink(sigla, desoneracao));
		}
		return retorno;		
	}
}
