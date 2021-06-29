package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;



public class Model {
	private ArtsmiaDAO dao;
	private Map<Integer, Artist> idMap ;
	private Graph<Artist, DefaultWeightedEdge> grafo ;
	private List<Artist> migliore;
	
	
	// crezione di un grafo e costruttore del model
	public Model() {
		
		this.grafo= new SimpleWeightedGraph<Artist,DefaultWeightedEdge>(DefaultWeightedEdge.class) ;
		dao= new  ArtsmiaDAO();
		idMap= new HashMap<>();
	}
	public void creaGrafo(String ruolo) {
		List<Artist> result= new ArrayList<>(dao.getVertici(ruolo, idMap));
		Graphs.addAllVertices(this.grafo, result);
		for(Adiacenza a: dao.getArchi(ruolo, idMap)) {
			DefaultWeightedEdge e= this.grafo.getEdge(a.getA1(), a.getA2());
			if(this.grafo.vertexSet().contains(a.getA1()) && this.grafo.vertexSet().contains(a.getA2()) && e==null) {
				Graphs.addEdge(this.grafo, a.getA1(), a.getA2(), a.getPeso());
			}
		}
	}
	
	public Integer getNVertici() {
		return this.grafo.vertexSet().size();
	}
	public Integer getNArchi() {
		return this.grafo.edgeSet().size();
	}
	public List<String> getRuoli() {
		return dao.getRuoli();
	}
	public List<ArtistiConnessi> getConnessi(){
		List<ArtistiConnessi> result= new ArrayList<>();
		for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
			Artist a1= this.grafo.getEdgeSource(e);
			Artist a2=this.grafo.getEdgeTarget(e);
			Integer peso= (int) this.grafo.getEdgeWeight(e);
			result.add(new ArtistiConnessi(a1,a2,peso));
		}
		Collections.sort(result);
		Collections.reverse(result);
		return result;
	}
	public Artist trovato(Integer num) {
		for(Artist a:this.grafo.vertexSet()) {
			if(a.getId().equals(num)) {
				return a;
			}
		}
		return null;
	}
	public List<Artist> ricorsione(Artist partenza) {
		migliore=null;
		List<Artist> parziale= new ArrayList<>();
		parziale.add(partenza);
		cerca(parziale,0,0);
		return this.migliore;		
	}
	private void cerca(List<Artist> parziale,Integer livello, Integer peso) {
		//condizione di terminazione
		if(migliore==null) {
			migliore= new ArrayList<>(parziale);
		}else {
			if(accettabile(parziale,peso)==false) {
				if(parziale.size()>migliore.size()) {
					this.migliore= new ArrayList<>(parziale);	
				}
				return;
			}
		}
		for(Artist a:Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))) {
			if(livello==0) {
				DefaultWeightedEdge e= this.grafo.getEdge(parziale.get(parziale.size()-1), a);
				int valore= (int) this.grafo.getEdgeWeight(e);
				parziale.add(a);
				cerca(parziale,livello+1,valore);
				parziale.remove(a);
			}else {
				DefaultWeightedEdge e= this.grafo.getEdge(parziale.get(parziale.size()-1), a);
				int valore= (int) this.grafo.getEdgeWeight(e);
				if(valore==peso && !parziale.contains(a)) {
					parziale.add(a);
					cerca(parziale,livello+1,valore);
					parziale.remove(a);				
				}
			}
		}
		
	}
	private boolean accettabile(List<Artist> parziale,Integer peso) {
		for(Artist a: Graphs.neighborListOf(this.grafo,parziale.get(parziale.size()-1) )) {
			DefaultWeightedEdge e= this.grafo.getEdge(parziale.get(parziale.size()-1), a);
			int valore= (int) this.grafo.getEdgeWeight(e);
			if(peso==valore && !parziale.contains(a)) {
				return true;
			}
		}
		return false;
	}
	public Integer getPesoMigliore() {
		DefaultWeightedEdge e= this.grafo.getEdge(migliore.get(migliore.size()-1),migliore.get(migliore.size()-2));
		return (int) this.grafo.getEdgeWeight(e);
	}
	public List<Artist> getMigliore(){
		return this.migliore;
	}
	
}
