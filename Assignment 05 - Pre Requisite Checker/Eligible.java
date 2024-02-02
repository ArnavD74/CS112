package prereqchecker;

import java.util.*;

/**
 * 
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
 * EligibleInputFile name is passed through the command line as args[1]
 * Read from EligibleInputFile with the format:
 * 1. c (int): Number of courses
 * 2. c lines, each with 1 course ID
 * 
 * Step 3:
 * EligibleOutputFile name is passed through the command line as args[2]
 * Output to EligibleOutputFile with the format:
 * 1. Some number of lines, each with one course ID
 */
public class Eligible {
    public static void main(String[] args) {

        if (args.length < 3) {
            StdOut.println("Execute: java -cp bin prereqchecker.Eligible <adjacency list INput file> <eligible INput file> <eligible OUTput file>");
            return;
        }

        StdIn.setFile(args[0]);
        StdOut.setFile(args[2]);
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
        StdIn.setFile(args[1]);
        int amt = StdIn.readInt();
        int c = 0;
        String[] end = new String[amt];
        while (c < amt) {
            end[c] = StdIn.readString();
            c++;
        }
        c = 0;
        HashMap < String, Boolean > taken = new HashMap < String, Boolean > ();

        for (String x: end) {
            dfs(x, courses, taken);
        }
        Set < String > courseTakenNames = taken.keySet();
        Object[] keys = courses.keySet().toArray();

        boolean haveTaken = true;
        for (Object x: keys) {
            haveTaken = true;
            for (String y: courses.get(x)) {
                if (!taken.getOrDefault(y, false)) {
                    haveTaken = false;
                }
            }

            if (haveTaken && !courseTakenNames.contains(x)) {
                StdOut.println(x);
            }
        }
    }
    private static void dfs(String start, HashMap < String, ArrayList < String >> adjList, HashMap < String, Boolean > visited) {
        visited.put(start, true);
        for (String z: adjList.get(start)) {
            if (!visited.getOrDefault(z, false)) {
                dfs(z, adjList, visited);
            }
        }
    }
}