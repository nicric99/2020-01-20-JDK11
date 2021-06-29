package it.polito.tdp.artsmia;

import java.awt.List;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.ArtistiConnessi;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {
	
	private Model model ;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnArtistiConnessi;

    @FXML
    private Button btnCalcolaPercorso;

    @FXML
    private ComboBox<String> boxRuolo;

    @FXML
    private TextField txtArtista;

    @FXML
    private TextArea txtResult;

    @FXML
    void doArtistiConnessi(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Calcola artisti connessi \n");
    	for(ArtistiConnessi as:model.getConnessi()) {
       	 txtResult.appendText(as.getA1().toString()+ "   "+as.getA2().toString()+ "   "+as.getPeso()+"\n");
       	}
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Calcola percorso\n");
    	String id= txtArtista.getText();
    	Integer num=null;
    	try {
    		num=Integer.parseInt(id);
    	}catch(NumberFormatException nfe) {
    		txtResult.appendText("Attenzione devi selezionare un id numerico\n");
    		return;
    	}
    	Artist trovato=model.trovato(num);
    	if(trovato==null) {
    		txtResult.appendText("L'id non corrisponde ad alcun artista nel grafo\n");
    		return;
    	}
    	if(model.ricorsione(trovato).size()==1) {
    		txtResult.appendText("Non è stato possibile trovare alcun cammino");
    		return;
    	}
    	for(Artist a:model.ricorsione(trovato)) {
    		txtResult.appendText(a.getId()+" "+a.getNome()+" \n");
    	}
    	txtResult.appendText("Il peso del percorso migliore"+model.getPesoMigliore()+" Lunghezza "+model.getMigliore().size());
    	
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Crea grafo");
    	String ruolo= boxRuolo.getValue();
    	if(ruolo==null) {
    		txtResult.appendText("Seleziona un ruolo prima\n");
    		return;
    	}
    	model.creaGrafo(ruolo);
    	txtResult.appendText("Numero di vertici "+model.getNVertici()+"\n");
    	txtResult.appendText("Numero di Archi "+model.getNArchi()+"\n");

    }

    public void setModel(Model model) {
    	this.model = model;
    	boxRuolo.getItems().addAll(model.getRuoli());
    	}

    
    @FXML
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnArtistiConnessi != null : "fx:id=\"btnArtistiConnessi\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert boxRuolo != null : "fx:id=\"boxRuolo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtArtista != null : "fx:id=\"txtArtista\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

    }
}
