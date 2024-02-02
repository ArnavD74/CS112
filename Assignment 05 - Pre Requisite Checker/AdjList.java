package prereqchecker;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Steps to implement this class main method:
 * 
 * Step 1:
 * AdjListInputFile name is passed through the command line as args[0]
 * Read from AdjListInputFile with the format:
 * 1. a (int): number of courses in the graph
 * 2. a lines, each with 1 course ID
 * 3. b (int): number of edges in the graph
 * 4. b lines, each with a source ID
 * 
 * Step 2:
 * AdjListOutputFile name is passed through the command line as args[1]
 * Output to AdjListOutputFile with the format:
 * 1. c lines, each starting with a different course ID, then 
 *    listing all of that course's prerequisites (space separated)
 */
public class AdjList {
    public static void main(String[] args) {
        if (args.length < 2) {
            StdOut.println("Execute: java -cp bin prereqchecker.AdjList <adjacency list INput file> <adjacency list OUTput file>");
            return;
        }
        StdIn.setFile(args[0]);
        StdOut.setFile(args[1]);

        int lines = StdIn.readInt();
        HashMap < String, ArrayList < String >> courses = new HashMap < > ();
        int a = 0;

        while (a < lines) {
            courses.put(StdIn.readString(), new ArrayList < > ());
            a++;
        }

        lines = StdIn.readInt();
        a = 0;
        while (a < lines) {
            courses.get(StdIn.readString()).add(StdIn.readString());
            a++;
        }

        Object[] keys = courses.keySet().toArray();

        for (Object x: keys) {
            StdOut.print(x + " ");
            for (String y: courses.get(x)) {
                StdOut.print(y + " ");
            }
            StdOut.println();
        }
    }
}