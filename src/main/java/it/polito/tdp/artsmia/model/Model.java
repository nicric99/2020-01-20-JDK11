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
	
	
}
