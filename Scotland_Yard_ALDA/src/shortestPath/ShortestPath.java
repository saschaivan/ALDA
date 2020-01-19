// O. Bittel;
// 28.02.2019

package shortestPath;

import directedGraph.DirectedGraph;
import directedGraph.*;
import sim.SYSimulation;
import sim.SYSimulation;

import java.awt.*;
import java.util.*;
import java.util.List;
// ...

/**
 * Kürzeste Wege in Graphen mit A*- und Dijkstra-Verfahren.
 * @author Oliver Bittel
 * @since 27.01.2015
 * @param <V> Knotentyp.
 */
public class ShortestPath<V> {
	
	SYSimulation sim = null;
	
	Map<V,Double> dist; // Distanz für jeden Knoten
	Map<V,V> pred; // Vorgänger für jeden Knoten
	DirectedGraph<V> g;
	Heuristic<V> h;	// Abstand (Kosten) zwischen den Knoten
	V s;
	V z;

	/**
	 * Konstruiert ein Objekt, das im Graph g k&uuml;rzeste Wege 
	 * nach dem A*-Verfahren berechnen kann.
	 * Die Heuristik h schätzt die Kosten zwischen zwei Knoten ab.
	 * Wird h = null gewählt, dann ist das Verfahren identisch 
	 * mit dem Dijkstra-Verfahren.
	 * @param g Gerichteter Graph
	 * @param h Heuristik. Falls h == null, werden kürzeste Wege nach
	 * dem Dijkstra-Verfahren gesucht.
	 */
	public ShortestPath(DirectedGraph<V> g, Heuristic<V> h) {
		// ...
		dist = new TreeMap<>();
		pred = new TreeMap<>();
		this.g = g;
		this.h = h;
	}


	/**
	 * Diese Methode sollte nur verwendet werden, 
	 * wenn kürzeste Wege in Scotland-Yard-Plan gesucht werden.
	 * Es ist dann ein Objekt für die Scotland-Yard-Simulation zu übergeben.
	 * <p>
	 * Ein typische Aufruf für ein SYSimulation-Objekt sim sieht wie folgt aus:
	 * <p><blockquote><pre>
	 *    if (sim != null)
	 *       sim.visitStation((Integer) v, Color.blue);
	 * </pre></blockquote>
	 * @param sim SYSimulation-Objekt.
	 */
	public void setSimulator(SYSimulation sim) {
		this.sim = sim;
	}

	/**
	 * Sucht den kürzesten Weg von Starknoten s zum Zielknoten g.
	 * <p>
	 * Falls die Simulation mit setSimulator(sim) aktiviert wurde, wird der Knoten,
	 * der als nächstes aus der Kandidatenliste besucht wird, animiert.
	 * @param s Startknoten
	 * @param z Zielknoten
	 */
	public void searchShortestPath(V s, V z) {
		// ...
		LinkedList<V> kl = new LinkedList<>();
		this.s = s;
		this.z = z;

		for (V v : g.getVertexSet()) {
			this.dist.put(v, Double.MAX_VALUE);
			this.pred.put(v, null);
		}

		this.dist.put(s, 0.0);
		kl.add(s);
		while (!kl.isEmpty()) {
			V v = null;
			double minDist = Double.MAX_VALUE;
			for (V e : kl) {
				if (h == null) {
					if (dist.get(e) < minDist) {
						minDist = dist.get(e);
						v = e;
					}
				} else {
					if (dist.get(e) + h.estimatedCost(e, z) < minDist) {
						v = e;
						minDist = dist.get(e) + h.estimatedCost(e, z);
					}
					if (v.equals(z)) {
						printCurrentVertex(v);
						return;
					}
				}
			}
			kl.remove(v);

			printCurrentVertex(v);

			for (V w : g.getSuccessorVertexSet(v)) {
				if (dist.get(w) == Double.MAX_VALUE)
					kl.add(w);
				if (dist.get(v) + g.getWeight(v, w) < dist.get(w)) {
					pred.put(w, v);
					dist.put(w, dist.get(v) + g.getWeight(v, w));
				}
			}
		}
	}


	/**
	 * Liefert einen kürzesten Weg von Startknoten s nach Zielknoten g.
	 * Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
	 * @throws IllegalArgumentException falls kein kürzester Weg berechnet wurde.
	 * @return kürzester Weg als Liste von Knoten.
	 */
	public List<V> getShortestPath() {
		LinkedList<V> path = new LinkedList<>();
		path.add(z);

		V current = pred.get(z);
		do {
			path.add(current);
		} while ((current = pred.get(current)) != s);

		path.add(s);

		Collections.reverse(path);
		return path;

	}

	private void printCurrentVertex(V v) {
		if (sim != null)
			sim.visitStation((Integer) v, Color.BLUE);
	}


	/**
	 * Liefert die Länge eines kürzesten Weges von Startknoten s nach Zielknoten g zurück.
	 * Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
	 * @throws IllegalArgumentException falls kein kürzester Weg berechnet wurde.
	 * @return Länge eines kürzesten Weges.
	 */
	public double getDistance() {
		return dist.get(z);
	}

}
