import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UnionFind {
    List<Integer> p;
    public UnionFind(int n) {
        p = new ArrayList();
        for (int i = 0; i < n; i++) {
            p.add(-1);
        }
    }

    public int find(int e) {
        if (e >= p.size())
            throw new IllegalArgumentException();
        while (p.get(e) >= 0) // e ist keine Wurzel
            e = p.get(e);
        return e;
    }

    public void union(int s1, int s2) {
        s1 = find(s1);
        s2 = find(s2);
        if (p.get(s1) >= 0 || p.get(s2) >= 0)
            return;
        if (s1 == s2)
            return;
        if (-p.get(s1) < -p.get(s2)) {
            p.set(s1, s2); // Höhe von s1 < Höhe von s2
        } else {
            if (-p.get(s1) == -p.get(s2)) {
                int tmp = p.get(s1); // Höhe von s1 erhöht sich um 1
                p.set(s1, tmp - 1);
            }
            p.set(s2, s1);
        }
    }

    public int size() {
        int c = 0;
        for (int i : p)
            if (i < 0)
                c++;
        return c;
    }

    public static void main(final String[] args) {
        UnionFind union = new UnionFind(10);
        union.union(0, 2);
        union.union(3, 6);
        union.union(2, 5);
        System.out.println(union.p.toString());
    }
}
