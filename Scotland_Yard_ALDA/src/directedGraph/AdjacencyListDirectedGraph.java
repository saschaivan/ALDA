// O. Bittel;
// 19.03.2018

package directedGraph;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.Set;

/**
 * Implementierung von DirectedGraph mit einer doppelten TreeMap 
 * für die Nachfolgerknoten und einer einer doppelten TreeMap 
 * für die Vorgängerknoten. 
 * <p>
 * Beachte: V muss vom Typ Comparable&lt;V&gt; sein.
 * <p>
 * Entspicht einer Adjazenzlisten-Implementierung 
 * mit schnellem Zugriff auf die Knoten.
 * @author Oliver Bittel
 * @since 19.03.2018
 * @param <V> Knotentyp.
 */
public class AdjacencyListDirectedGraph<V> implements DirectedGraph<V> {
    // doppelte Map für die Nachfolgerknoten:
    private final Map<V, Map<V, Double>> succ = new TreeMap<>(); 
    
    // doppelte Map für die Vorgängerknoten:
    private final Map<V, Map<V, Double>> pred = new TreeMap<>(); 

    private int numberEdge = 0;

	@Override
	public boolean addVertex(V v) {
		if (v != null) {
			succ.put(v, new TreeMap<V, Double>());
			pred.put(v, new TreeMap<V, Double>());
		}
		return true;
    }

    @Override
    public boolean addEdge(V v, V w, double weight) {
		if (v == null && w == null) {
			return false;
		}
		if (!(succ.containsKey(v) && pred.containsKey(v))) {
			addVertex(v);
		}
		if (!(succ.containsKey(w) && pred.containsKey(w))) {
			addVertex(w);
		}
		succ.get(v).put(w, weight);
		pred.get(w).put(v, weight);
		numberEdge++;
		return true;
    }

    @Override
    public boolean addEdge(V v, V w) {
		addEdge(v, w, 1);
		return true;
    }

    @Override
    public boolean containsVertex(V v) {
		if (succ.containsKey(v) || pred.containsKey(v)) {
			return true;
		}
		return false;
    }

    @Override
    public boolean containsEdge(V v, V w) {
		if (succ.containsKey(v) && succ.containsKey(w)) {
			return succ.get(v).containsKey(w);
		}
		return false;
    }

    @Override
    public double getWeight(V v, V w) {
        if (containsEdge(v, w)) {
			return succ.get(v).get(w);
		}
		throw new NullPointerException();
    }

	
    @Override
    public int getInDegree(V v) {
		if (!containsVertex(v)) {
			throw new NullPointerException();
		}
		return pred.get(v).size();
    }

    @Override
    public int getOutDegree(V v) {
		if (!containsVertex(v)) {
			throw new NullPointerException();
		}
		return succ.get(v).size();
    }
	
	@Override
    public Set<V> getVertexSet() {
		return Collections.unmodifiableSet(succ.keySet()); // nicht modifizierbare Sicht
    }

    @Override
    public Set<V> getPredecessorVertexSet(V v) {
		return Collections.unmodifiableSet(pred.get(v).keySet());
    }

    @Override
    public Set<V> getSuccessorVertexSet(V v) {
		return Collections.unmodifiableSet(succ.get(v).keySet());
    }

    @Override
    public int getNumberOfVertexes() {
		return succ.size();
    }

    @Override
    public int getNumberOfEdges() {
		return numberEdge;
    }
	
	@Override
    public DirectedGraph<V> invert() {
		DirectedGraph<V> tmp = new AdjacencyListDirectedGraph();
		for (Map.Entry<V, Map<V, Double>> entry : pred.entrySet()) {
			for (Map.Entry<V, Double> e : entry.getValue().entrySet()) {
				tmp.addEdge(entry.getKey(), e.getKey(), e.getValue());
			}

		}
		return tmp;
	}

	@Override
	public String toString() {
		String s = "";
		for (Map.Entry<V, Map<V, Double>> e : succ.entrySet()) {
			for (Map.Entry<V, Double> entry : succ.get(e.getKey()).entrySet()) {
				s += e.getKey() + " --> " + entry.getKey() + " weight = " + entry.getValue() + "\n";
			}
		}
		return s;
	}
	
	
	public static void main(String[] args) {
		DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
		g.addEdge(1,2);
		g.addEdge(2,5);
		g.addEdge(5,1);
		g.addEdge(2,6);
		g.addEdge(3,7);
		g.addEdge(4,3);
		g.addEdge(4,6);
		g.addEdge(7,4);
		
		
		System.out.println(g.getNumberOfVertexes());	// 7
		System.out.println(g.getNumberOfEdges());		// 8
		System.out.println(g.getVertexSet());	// 1, 2, ..., 7
		System.out.println(g);
			// 1 --> 2 weight = 1.0 
			// 2 --> 5 weight = 1.0
			// 2 --> 6 weight = 1.0
			// 3 --> 7 weight = 1.0
			// ...
		
		System.out.println("");
		System.out.println(g.getOutDegree(2));				// 2
		System.out.println(g.getSuccessorVertexSet(2));	// 5, 6
		System.out.println(g.getInDegree(6));				// 2
		System.out.println(g.getPredecessorVertexSet(6));	// 2, 4
		
		System.out.println("");
		System.out.println(g.containsEdge(1,2));	// true
		System.out.println(g.containsEdge(2,1));	// false
		System.out.println(g.getWeight(1,2));	// 1.0	
		g.addEdge(1, 2, 5.0);
		System.out.println(g.getWeight(1,2));	// 5.0	
		
		System.out.println("");
		System.out.println(g.invert());
			// 1 --> 5 weight = 1.0
			// 2 --> 1 weight = 5.0
			// 3 --> 4 weight = 1.0 
			// 4 --> 7 weight = 1.0
			// ...
			
		Set<Integer> s = g.getSuccessorVertexSet(2);
		System.out.println(s);
		// s.remove(5);	// Laufzeitfehler! Warum?
		// da s unmodifiable ist
	}
}
