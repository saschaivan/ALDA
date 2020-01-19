// O. Bittel;
// 05-09-2018

package directedGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Klasse für Bestimmung aller strengen Komponenten.
 * Kosaraju-Sharir Algorithmus.
 * @author Oliver Bittel
 * @since 22.02.2017
 * @param <V> Knotentyp.
 */
public class StrongComponents<V> {
	// comp speichert fuer jede Komponente die zughörigen Knoten.
    // Die Komponenten sind numeriert: 0, 1, 2, ...
    // Fuer Beispielgraph in Aufgabenblatt 2, Abb3:
    // Component 0: 5, 6, 7,
    // Component 1: 8,
    // Component 2: 1, 2, 3,
    // Component 3: 4,

	private final Map<Integer,Set<V>> comp = new TreeMap<>();
	private final List<V> alreadyVisited = new LinkedList<>();

	/**
	 * Ermittelt alle strengen Komponenten mit
	 * dem Kosaraju-Sharir Algorithmus.
	 * @param g gerichteter Graph.
	 */
	public StrongComponents(DirectedGraph<V> g) {
		// ...
		// a)
        DepthFirstOrder<V> dfs = new DepthFirstOrder<>(g);
        List<V> p = new LinkedList<>(dfs.postOrder());
        Collections.reverse(p);

        // b)
        DirectedGraph<V> m = g.invert();
        // DepthFirstOrder<V> dfsGI = new DepthFirstOrder<>(m);


		// c)
        int c = 0;
        for (var v : p) {
            if (alreadyVisited.contains(v)) {
                continue;
            }
            comp.put(c, new TreeSet<>());
            comp.get(c).add(v);
            alreadyVisited.add(v);
            reachR(m, v, c);
            c++;
        }
	}

	private void reachR(DirectedGraph<V> g, V v, int c) {
	    for (var e : g.getSuccessorVertexSet(v)) {
	        if(alreadyVisited.contains(e))
	            continue;
	        comp.get(c).add(e);
	        alreadyVisited.add(e);
	        reachR(g, e, c);
        }
    }


	/**
	 *
	 * @return Anzahl der strengen Komponeneten.
	 */
	public int numberOfComp() {
		return comp.size();
	}

	@Override
	public String toString() {
        String s = "";
        for (Map.Entry<Integer, Set<V>> e : comp.entrySet()) {
            s += "Component " + e.getKey() + ": " + e.getValue() + "\n";
        }
        return s;
	}

	/**
	 * Liest einen gerichteten Graphen von einer Datei ein.
	 * @param fn Dateiname.
	 * @return gerichteter Graph.
	 * @throws FileNotFoundException
	 */
	public static DirectedGraph<Integer> readDirectedGraph(File fn) throws FileNotFoundException {
		DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
		Scanner sc = new Scanner(fn);
		sc.nextInt();
		sc.nextInt();
		while (sc.hasNextInt()) {
			int v = sc.nextInt();
			int w = sc.nextInt();
			g.addEdge(v, w);
		}
		return g;
	}

	private static void test1() {
		DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
		g.addEdge(1,2);
		g.addEdge(1,3);
		g.addEdge(2,1);
		g.addEdge(2,3);
		g.addEdge(3,1);

		g.addEdge(1,4);
		g.addEdge(5,4);

		g.addEdge(5,7);
		g.addEdge(6,5);
		g.addEdge(7,6);

		g.addEdge(7,8);
		g.addEdge(8,2);

		StrongComponents<Integer> sc = new StrongComponents<>(g);

		System.out.println(sc.numberOfComp());  // 4

		System.out.println(sc);
			// Component 0: 5, 6, 7,
        	// Component 1: 8,
            // Component 2: 1, 2, 3,
            // Component 3: 4,
	}

	private static void test2() throws FileNotFoundException {
		DirectedGraph<Integer> g = readDirectedGraph(new File("mediumDG.txt"));
		System.out.println(g.getNumberOfVertexes());
		System.out.println(g.getNumberOfEdges());
		System.out.println(g);

		System.out.println("");

		StrongComponents<Integer> sc = new StrongComponents<>(g);
		System.out.println(sc.numberOfComp());  // 10
		System.out.println(sc);

	}

	public static void main(String[] args) throws FileNotFoundException {
		test1();
		test2();
	}
}
