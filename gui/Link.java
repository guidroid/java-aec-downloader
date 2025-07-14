package ufc.deha.ufc9.dolphin.downloader.gui;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import ufc.deha.ufc9.dolphin.referencia.Desoneracao;
import ufc.deha.ufc9.dolphin.referencia.Estados;

public class Link {

	private final SimpleIntegerProperty monthProperty = new SimpleIntegerProperty(0);
	private final SimpleIntegerProperty yearProperty = new SimpleIntegerProperty(0);
	private final SimpleStringProperty titleProperty = new SimpleStringProperty("<Sem título>");
	private final SimpleStringProperty linkProperty = new SimpleStringProperty("Link não encontrado!");
	

	private final SimpleObjectProperty<Estados> estadoProperty = new SimpleObjectProperty<>(Estados.NULL);;
	private final SimpleObjectProperty<Desoneracao> desoneracaoProperty = new SimpleObjectProperty<>(Desoneracao.NULL);
	
	
	public Link() {		
	}

	public Link(Integer month, Integer year, String title, String link) {		
		monthProperty.set(month);
		yearProperty.set(year);
		titleProperty.set(title);
		linkProperty.set(link);
	}
	
	public void setMonth(Integer month) {
		this.monthProperty.set(month);
	}
	public void setYear(Integer year) {
		this.yearProperty.set(year);
	}
	public void setTitle(String title) {
		this.titleProperty.set(title);
	}
	public void setLink(String link) {
		this.linkProperty.set(link);
	}
	public void setEstado(Estados estado) {
		this.estadoProperty.set(estado);
	}
	public void setDesoneracao(Desoneracao desoneracao) {
		this.desoneracaoProperty.set(desoneracao);
	}
	
	public Integer getMonth() {
		return monthProperty.get();
	}
	public Integer getYear() {
		return yearProperty.get();
	}
	public String getTitle() {
		return titleProperty.get();
	}
	public String getLink() {
		return linkProperty.get();
	}
	public Estados getEstado() {
		return estadoProperty.get();
	}
	public Desoneracao getDesoneracao() {
		return desoneracaoProperty.get();
	}
	
	public SimpleIntegerProperty getMonthProperty() {
		return monthProperty;
	}
	public SimpleIntegerProperty getYearProperty() {
		return yearProperty;
	}
	public SimpleStringProperty getTitleProperty() {
		return titleProperty;
	}
	public SimpleStringProperty getLinkProperty() {
		return linkProperty;
	}
	public SimpleObjectProperty<Estados> getEstadoProperty() {
		return estadoProperty;
	}
	public SimpleObjectProperty<Desoneracao> getDesoneracaoProperty() {
		return desoneracaoProperty;
	}
	
}
