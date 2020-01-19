/*
 * Test der verscheidenen Dictionary-Implementierungen
 *
 * O. Bittel
 * 6.7.2017
 */
package dictionary;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Static test methods for different Dictionary implementations.
 * @author oliverbittel
 */
public class DictionaryTest {

	/**
	 * @param args not used.
	 */
	public static void main(String[] args) throws IOException {
		//testSortedArrayDictionary();
		//testHashDictionary();
		testBinaryTreeDictionary();
		//PerformanceTest();
		//tui();
	}

	private static void tui() {
		Scanner in = new Scanner(System.in);
		Dictionary<String, String> dict = null;
		JFileChooser chooser = new JFileChooser();
		String line;
		while((line = in.nextLine()) != null) {
			try {
				String[] s = line.split("[^0-9^a-z^A-Z^ÃŸ^Ã¤^Ã¶^Ã¼^Ã„^Ã–^Ãœ]+");
				String a = s[0];
				if (a.equals("create")) {
					if ((s.length == 2)) {
						if (s[1].equals("SortedArrayDictionary")) {
							dict = create(0);
							System.out.println("SortedArrayDictionary wurde angelegt");
						} else if (s[1].equals("HashDictionary")) {
							dict = create(1);
							System.out.println("HashDictionary angelegt");
							s[1] = null;
						} else if (s[1].equals(("BinaryTreeDictionary"))) {
							dict = create(2);
							System.out.println("BinaryTreeDictionary angelegt");
							s[1] = null;
						} else {
							System.out.println("Es konnte kein Dictionary angelegt werden");
							System.exit(1);
						}
					} else {
						dict = create(0);
						System.out.println("SortedArrayDictionary angelegt");
					}
				}
				if (s[0].equals("read")) {
					int rueckgabe = chooser.showOpenDialog(null);
					if(dict == null) {
						System.out.println("zuerst ein Dictionary anlegen!");
						continue;
					}
					if(s.length == 2) {
						if(rueckgabe == JFileChooser.APPROVE_OPTION) {
							read(dict, Integer.parseInt(s[1]), chooser.getSelectedFile().getPath());
						}
					} else {
						read(dict, 0, chooser.getSelectedFile().getPath());
					}

				}
				if (s[0].equals("p")) {
					if (dict == null) {
						System.out.println("zuerst ein Dictionary anlegen!");
						continue;
					}
					for(Dictionary.Entry<String, String> e : dict) {
						System.out.println("Key: " + e.getKey() + " Value: " + e.getValue());
					}
				}
				if (s[0].equals("s")) {
					if (dict == null) {
						System.out.println("zuerst ein Dictionary anlegen!");
						continue;
					}
					if (s.length == 2) {
						if(dict.search(s[1]) == null) {
							System.out.println("Eintrag konnte nicht gefunden werden");
						} else {
							System.out.println("Deutsch: " + s[1] + " Englisch: " + dict.search(s[1]));
						}
					}
				}
				if (s[0].equals("i")) {
					if (dict == null) {
						System.out.println("zuerst ein Dictionary anlegen!");
						continue;
					}
					if (s.length == 3) {
						dict.insert(s[1], s[2]);
						System.out.println("Eintrag wurde hinzugefügt");
					} else {
						System.out.println("Eintrag konnte nicht hinzugefügt werden");
					}
				}
				if (s[0].equals("r")) {
					if (dict == null) {
						System.out.println("zuerst ein Dictionary anlegen!");
						continue;
					}
					if (s.length == 2) {
						dict.remove(s[1]);
						System.out.println("Eintrag wurde gelöscht");
					} else {
						System.out.println("Eintrage konnte nicht gelöscht werden");
					}
				}
				if (s[0].equals("exit")) {
					System.out.println("Programm wird beendet...");
					System.exit(0);
				}
			} catch (NullPointerException x) {
				System.err.printf("Eingabefehler: %s%n", x.getMessage());
				continue;
			} catch (IllegalArgumentException x) {
				System.err.printf("Eingabefehler: %s%n", x.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static Dictionary<String,String> create(int d) {
		if(d == 0) {
			return new SortedArrayDictionary<>();
		}
		if(d == 1) {
			return new HashDictionary<>(3);
		}
		if(d == 2) {
			return new BinaryTreeDictionary<>();
		}
		return null;
	}

	private static void PerformanceTest() throws IOException {

		// SortedArrayDictionary Performance mit 16000 Einträgen
		Dictionary<String, String> dict = new SortedArrayDictionary<>();
		long startArray = System.nanoTime();
		read(dict, 0, "dtengl.txt");
		long endArray = System.nanoTime();
		double timeArray = (double) (endArray - startArray) / 1.0e09;
		System.out.println("SortedArrayDictionary Performance mit (16000): " + timeArray + " sec");

		// SortedArrayDictionary Performance mit 8000 Einträgen
		Dictionary<String, String> dict1 = new SortedArrayDictionary<>();
		long startArray1 = System.nanoTime();
		read(dict1, 8000, "dtengl.txt");
		long endArray1 = System.nanoTime();
		double timeArray1 = (double) (endArray1 - startArray1) / 1.0e09;
		System.out.println("SortedArrayDictionary Performance (8000): " + timeArray1 + " sec");

		//SortedArray Erfolgreiche Suche für 16000 Einträge
		LineNumberReader deutsch;
		List<String> deutscheWoerter = new ArrayList<>();
		deutsch = new LineNumberReader(new FileReader("dtengl.txt"));
		String line;
		while((line = deutsch.readLine()) != null) {
			String[] wf = line.split("[^a-z^A-Z^ÃŸ^Ã¤^Ã¶^Ã¼^Ã„^Ã–^Ãœ]+");
			deutscheWoerter.add(wf[0]);
		}
		long startSuche = System.nanoTime();
		for (String s : deutscheWoerter) {
			dict.search(s);
		}
		long endSuche = System.nanoTime();
		double timeSuche = (double) (endSuche - startSuche) / 1.0e09;
		System.out.println("Erfolgreiche Suche SortedArray (16000): " + timeSuche + " sec");

		// SortedArrayDictionary Erfolgreiche Suche für 8000 Einträge
		long startSuche1 = System.nanoTime();
		for (String s1 : deutscheWoerter) {
			dict1.search(s1);
		}
		long endSuche1 = System.nanoTime();
		double timeSuche1 = (endSuche1 - startSuche1) / 1.0e09;
		System.out.println("Erfolgreiche Suche SortedArray (8000): " + timeSuche1 + " sec");

		// HashDictionary Performance mit 16000 Einträgen
		Dictionary<String, String> dictHash = new HashDictionary<>(3);
		long startHash = System.nanoTime();
		read(dictHash, 0, "dtengl.txt");
		long endHash = System.nanoTime();
		double timeHash = (double) (endHash - startHash) / 1.0e09;
		System.out.println("HashDictionary Performance mit (16000): " + timeHash + " sec");

		// HashDictionary Performance mit 80000 Einträgen
		Dictionary<String, String> dictHash1 = new HashDictionary<>(3);
		long startHash1 = System.nanoTime();
		read(dictHash1, 8000, "dtengl.txt");
		long endHash1 = System.nanoTime();
		double timeHash1 = (double) (endHash1 - startHash1) / 1.0e09;
		System.out.println("HashDictionary Performance mit (8000): " + timeHash1 + " sec");

		// erolgreiche Suche HashDictionary mit 16000 Einträgen
		long startSuche2 = System.nanoTime();
		for (String e : deutscheWoerter) {
			dictHash.search(e);
		}
		long endSuche2 = System.nanoTime();
		double timeSuche2 = (double) (endSuche2 - startSuche2) / 1.0e09;
		System.out.println("Erfolgreiche Suche HashDictionary (16000): " + timeSuche2 + " sec");

		// erfolgreiche Suche HashDictionary mit 8000 Einträgen
		long startSuche3 = System.nanoTime();
		for (String a : deutscheWoerter) {
			dictHash1.search(a);
		}
		long endSuche3 = System.nanoTime();
		double timeSuche3 = (double) (endSuche3 - startSuche3) / 1.0e09;
		System.out.println("Erfolgreiche Suche HashDictionary (8000): " + timeSuche3 + " sec");

		// Nicht erfolgreiche Suche HashDictionary mit 16000
		LineNumberReader englisch;
		List<String> englischeWoerter = new ArrayList<>();
		englisch = new LineNumberReader(new FileReader("dtengl.txt"));
		String line1;
		while((line1 = englisch.readLine()) != null) {
			String[] wf1 = line1.split("[^a-z^A-Z^ÃŸ^Ã¤^Ã¶^Ã¼^Ã„^Ã–^Ãœ]+");
			englischeWoerter.add(wf1[1]);
		}
		long startNSuche = System.nanoTime();
		for (String d : englischeWoerter) {
			dictHash.search(d);
		}
		long endNSuche = System.nanoTime();
		double timeNSuche = (double) (endNSuche - startNSuche) / 1.0e09;
		System.out.println("Nicht erfolgreiche Suche HashDictionary (16000): " + timeNSuche + " sec");

		// Nicht erfolgreiche Suche HashDictionary mit 8000 Einträgen
		long startN1Suche = System.nanoTime();
		for (String f : englischeWoerter) {
			dictHash1.search(f);
		}
		long endN1Suche = System.nanoTime();
		double timeN1Suche = (double) (endN1Suche - startN1Suche) / 1.0e09;
		System.out.println("Nicht erfolgreiche Suche HashDictionary (8000): " + timeN1Suche + " sec");

		// nicht erfolgreiche Suche SortedArray mit 16000 Einträgen
		long startNSuche2 = System.nanoTime();
		for (String d1 : englischeWoerter) {
			dict.search(d1);
		}
		long endNSuche2 = System.nanoTime();
		double timeNSuche2 = (double) (endNSuche2 - startNSuche2) / 1.0e09;
		System.out.println("Nicht erfolgreiche Suche SortedArray (16000): " + timeNSuche2 + " sec");

		// nicht erfolgreiche Suche SortedArray mit 8000 Einträgen
		long startNSuche3 = System.nanoTime();
		for (String d2 : englischeWoerter) {
			dict1.search(d2);
		}
		long endNSuche3 = System.nanoTime();
		double timeNSuche3 = (double) (endNSuche3 - startNSuche3) / 1.0e09;
		System.out.println("Nicht erfolgreiche Suche SortedArray (8000): " + timeNSuche3 + " sec");

		// BinaryTreeDictionary Perfomance mit 16000 Einträgen
		Dictionary<String, String> dictBin = new BinaryTreeDictionary<>();
		long startBin = System.nanoTime();
		read(dictBin, 0, "dtengl.txt");
		long endBin = System.nanoTime();
		double timeBin = (double) (endBin - startBin) / 1.0e09;
		System.out.println("BinaryTreeDictionary Performance mit (16000): " + timeBin + " sec");

		// BinaryTreeDictionary Perfomance mit 8000 Einträgen
		Dictionary<String, String> dictBin1 = new BinaryTreeDictionary<>();
		long startBin1 = System.nanoTime();
		read(dictBin1, 8000, "dtengl.txt");
		long endBin1 = System.nanoTime();
		double timeBin1 = (double) (endBin1 - startBin1) / 1.0e09;
		System.out.println("BinaryTreeDictionary Performance mit (8000): " + timeBin1 + " sec");

		// BinaryTreeDictionary Erfolgreiche Suche für 16000 Einträge
		long startSucheBin = System.nanoTime();
		for (String a : deutscheWoerter) {
			dictBin.search(a);
		}
		long endSucheBin = System.nanoTime();
		double timeSucheBin = (double) (endSucheBin - startSucheBin) / 1.0e09;
		System.out.println("Erfolgreiche Suche BinaryTreeDictionary (16000): " + timeSucheBin + " sec");

		// BinaryTreeDictionary Erfolgreiche Suche für 8000 Einträge
		long startSucheBin1 = System.nanoTime();
		for (String a : deutscheWoerter) {
			dictBin1.search(a);
		}
		long endSucheBin1 = System.nanoTime();
		double timeSucheBin1 = (double) (endSucheBin1 - startSucheBin1) / 1.0e09;
		System.out.println("Erfolgreiche Suche BinaryTreeDictionary (8000): " + timeSucheBin1 + " sec");

		// nicht erfolgreiche Suche BinaryTreeDictionary mit 16000 Einträgen
		long startNSucheBin = System.nanoTime();
		for (String d1 : englischeWoerter) {
			dictBin.search(d1);
		}
		long endNSucheBin = System.nanoTime();
		double timeNSucheBin = (double) (endNSucheBin - startNSucheBin) / 1.0e09;
		System.out.println("Nicht erfolgreiche Suche BinaryTreeDictionary (16000): " + timeNSucheBin + " sec");

		// nicht erfolgreiche Suche BinaryTreeDictionary mit 8000 Einträgen
		long startNSucheBin1 = System.nanoTime();
		for (String d1 : englischeWoerter) {
			dictBin1.search(d1);
		}
		long endNSucheBin1 = System.nanoTime();
		double timeNSucheBin1 = (double) (endNSucheBin1 - startNSucheBin1) / 1.0e09;
		System.out.println("Nicht erfolgreiche Suche BinaryTreeDictionary (8000): " + timeNSucheBin1 + " sec");


	}

	private static void read(Dictionary<String, String> dict, int n, String data) throws IOException {
		LineNumberReader in1;
		in1 = new LineNumberReader(new FileReader(data));
		String line;
		int i = 0;
		if (n == 0) {
			while ((line = in1.readLine()) != null) {
				String[] wf = line.split("[^a-z^A-Z^ÃŸ^Ã¤^Ã¶^Ã¼^Ã„^Ã–^Ãœ]+");
				String c = wf[0];
				String d = wf[1];
				dict.insert(c, d);
			}
		} else if (n > 0) {
			while ((line = in1.readLine()) != null && i < n) {
				String[] wf = line.split("[^a-z^A-Z^ÃŸ^Ã¤^Ã¶^Ã¼^Ã„^Ã–^Ãœ]+");
				String c = wf[0];
				String d = wf[1];
				dict.insert(c, d);
				i++;
			}
		}
	}

	private static void testSortedArrayDictionary() {
		Dictionary<String, String> dict = new SortedArrayDictionary<>();
		testDict(dict);
	}
	
	private static void testHashDictionary() {
		Dictionary<String, String> dict = new HashDictionary<>(3);
		testDict(dict);

	}
	
	private static void testBinaryTreeDictionary() {
		Dictionary<String, String> dict = new BinaryTreeDictionary<>();
		testDict(dict);
        
        // Test für BinaryTreeDictionary mit prettyPrint 
        // (siehe Aufgabe 10; Programmiertechnik 2).
        // Pruefen Sie die Ausgabe von prettyPrint auf Papier nach.
        BinaryTreeDictionary<Integer, Integer> btd = new BinaryTreeDictionary<>();
        btd.insert(10, 0);
        btd.insert(20, 0);
        btd.insert(30, 0);
        System.out.println("insert:");
        btd.prettyPrint();

        btd.insert(40, 0);
        btd.insert(50, 0);
        System.out.println("insert:");
        btd.prettyPrint();

        btd.insert(21, 0);
        System.out.println("insert:");
        btd.prettyPrint();

        btd.insert(35, 0);
        btd.insert(60, 0);
        System.out.println("insert:");
        btd.prettyPrint();

        System.out.println("For Each Loop:");
        for (Dictionary.Entry<Integer, Integer> e : btd) {
            System.out.println(e.getKey() + ": " + e.getValue());
        }

        btd.remove(30);
        System.out.println("remove:");
        btd.prettyPrint();

        btd.remove(35);
        btd.remove(40);
        btd.remove(50);
        System.out.println("remove:");
        btd.prettyPrint();
    }
	
	private static void testDict(Dictionary<String, String> dict) {
		System.out.println("===== New Test Case ========================");
		System.out.println("test " + dict.getClass());
		System.out.println(dict.insert("gehen", "go") == null);		// true
		String s = new String("gehen");
		System.out.println(dict.search(s) != null);					// true
		System.out.println(dict.search(s).equals("go"));			// true
		System.out.println(dict.insert(s, "walk").equals("go"));	// true
		System.out.println(dict.search("gehen").equals("walk"));	// true
		System.out.println(dict.remove("gehen").equals("walk"));	// true
		System.out.println(dict.remove("gehen") == null);			// true
		dict.insert("starten", "start");
		dict.insert("gehen", "go");
		dict.insert("schreiben", "write");
		dict.insert("reden", "say");
		dict.insert("arbeiten", "work");
		dict.insert("lesen", "read");
		dict.insert("singen", "sing");
		dict.insert("schwimmen", "swim");
		dict.insert("rennen", "run");
		dict.insert("beten", "pray");
		dict.insert("tanzen", "dance");
		dict.insert("schreien", "cry");
		dict.insert("tauchen", "dive");
		dict.insert("fahren", "drive");
		dict.insert("spielen", "play");
		dict.insert("planen", "plan");
		for (Dictionary.Entry<String, String> e : dict) {
			System.out.println(e.getKey() + ": " + e.getValue() + " search: " + dict.search(e.getKey()));
		}
	}
	
}
