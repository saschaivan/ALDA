import java.awt.*;
import java.util.*;
import java.util.List;
;

public class TelNet<V> {
    Map<TelKnoten, Integer> telKnotenNum;
    PriorityQueue<TelVerbindung> edges;
    List<TelVerbindung> minSpanTree;
    int lbg;


    public TelNet(int lbg) {
        this.lbg = lbg;
        telKnotenNum = new HashMap<>();
        edges = new PriorityQueue<>(Comparator.comparing(x -> x.c));
        minSpanTree = new LinkedList<>();
    }

    public int cost(TelKnoten x, TelKnoten y) {
        int dist = Math.abs(x.x - y.x) + Math.abs(x.y - y.y);
        if (dist <= lbg) {
            return dist;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    public boolean addTelKnoten(int x, int y) {
        if (x < 0 || y < 0)
            return false;
        TelKnoten t = new TelKnoten(x, y);
        telKnotenNum.put(t, telKnotenNum.size());
        for (Map.Entry<TelKnoten, Integer> e : telKnotenNum.entrySet()) {
            if (!t.equals(e.getKey()) && lbg >= cost(t, e.getKey())) {
                TelVerbindung u = new TelVerbindung(t, e.getKey(), cost(t, e.getKey()));
                edges.offer(u);
            }
        }
        return true;
    }

    public boolean computeOptTelNet() {
        UnionFind forest = new UnionFind(telKnotenNum.size());
        while (forest.size() != 1 && !edges.isEmpty()) {
            TelVerbindung t = edges.poll();
            TelKnoten v = t.u;
            TelKnoten w = t.v;
            int t1 = forest.find(telKnotenNum.get(v));
            int t2 = forest.find(telKnotenNum.get(w));
            if (t1 != t2) {
                forest.union(t1, t2);
                minSpanTree.add(new TelVerbindung(v, w, cost(v, w)));
            }
        }
        if (edges.isEmpty() && forest.size() != 1) { // es exitiert kein aufspannender Baum
            return false;
        }
        return true;
    }

    public List<TelVerbindung> getOptTelNet() throws IllegalStateException {
       return minSpanTree;
    }

    public int getOptTelNetKosten() throws IllegalStateException {
        int cost = 0;
        for (var e : edges) {
            cost += e.c;
        }
        return cost;
    }

    public void drawOptTelNet(int xMax, int yMax) throws IllegalStateException {
        StdDraw.setXscale(0, xMax);
        StdDraw.setYscale(0, yMax);
        for (var e : minSpanTree) {
            StdDraw.line(e.u.x, e.u.y, e.u.x, e.v.y);
            StdDraw.line(e.u.x, e.v.y, e.v.x, e.v.y);
            StdDraw.setPenColor(new Color(0, 0, 250));
            StdDraw.filledSquare(e.u.x, e.u.y, 0.5);
            StdDraw.filledSquare(e.v.x, e.v.y, 0.5);
            StdDraw.setPenColor(new Color(250, 0, 0));
        }
        StdDraw.show();
    }

    public void generateRandomTelNet(int n, int xMax, int yMax) {
        int i = 0;
        while (i < n) {
            int x = (int)( Math.random() * xMax);
            int y = (int)( Math.random() * yMax);
            if (this.addTelKnoten(x, y))
                i++;
        }
    }

    public int size() {
        return telKnotenNum.size();
    }

    public String toString() {
        return telKnotenNum.toString();
    }

    public static void main (final String[] args) {
//        TelNet tnet = new TelNet(7);
//
//        tnet.addTelKnoten(0,0);
//        tnet.addTelKnoten(2,0);
//        tnet.addTelKnoten(3,1);
//        tnet.addTelKnoten(2,3);
//        tnet.addTelKnoten(1,5);
//        tnet.addTelKnoten(3,6);
//        tnet.addTelKnoten(6,4);
//
//        tnet.toString();
//        System.out.println("Optimales Telefonnetz: " + tnet.getOptTelNet());
//        System.out.println("Kosten optimales Telefonnetz: " + tnet.getOptTelNetKosten());
//        System.out.println("Größe Telefonnetz: " + tnet.size());

//        tnet.computeOptTelNet();
//        tnet.drawOptTelNet(7, 7);
//
        TelNet tnet1 = new TelNet(100);
        tnet1.generateRandomTelNet(1000, 1000, 1000);
        tnet1.computeOptTelNet();
        tnet1.drawOptTelNet(1000, 1000);
    }
}
