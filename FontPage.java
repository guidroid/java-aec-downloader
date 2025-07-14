package ufc.deha.ufc9.dolphin.downloader;

import java.time.LocalDate;

import org.jsoup.Jsoup;

/**
 * Classe final de m�todos abstratos que servem para o download de arquivos.
 * @author Guilherme Ribeiro
 *
 * @since 1.0
 * @version 1.0
 */
public final class FontPage{	
	/**
	 * @return
	 * 		M�s e ano atuais em uma array de inteiros da forma {M�s, Ano}.
	 */
	public static Integer[] getCurrentDateInIntegerArray() {
		LocalDate localDate = LocalDate.now();
		
		int month = localDate.getMonthValue();
		int year = localDate.getYear();
		 getOlderDateMonthsInIntegerArray((long) 2);
		return new Integer[] {month, year};
	}
	/**
	 * @return
	 * 		M�s e ano em meses atr�s em uma array de inteiros da forma {M�s, Ano}.
	 */
	public static Integer[] getOlderDateMonthsInIntegerArray(Long old) {
		LocalDate localDate = LocalDate.now().minusMonths(old);
		
		int month = localDate.getMonthValue();
		int year = localDate.getYear();
		
		return new Integer[] {month, year};
	}
	/**
	 * @return
	 * 		M�s e ano em meses a frente em uma array de inteiros da forma {M�s, Ano}.
	 */
	public static Integer[] getOLaterDateMonthsInIntegerArray(Long late) {
		LocalDate localDate = LocalDate.now().minusMonths(late);
		
		int month = localDate.getMonthValue();
		int year = localDate.getYear();
		
		return new Integer[] {month, year};
	}
	/**
	 * Avalia se endere�o da web existe gerando uma conex�o v�lida.
	 * @param 
	 * 		targetUrl - endere�o da web. 
	 * @return
	 * 		true se a conex�o for bem sucedida, false caso contr�rio.
	 */
	public static boolean checkURL(String targetUrl) {
		try {
			Jsoup.connect(targetUrl).ignoreContentType(true).execute();
	        return true;
		} catch (Exception e) {
			return false;
		}
	}

}
