package prereqchecker;

import java.util.*;

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
 * NeedToTakeInputFile name is passed through the command line as args[1]
 * Read from NeedToTakeInputFile with the format:
 * 1. One line, containing a course ID
 * 2. c (int): Number of courses
 * 3. c lines, each with one course ID
 * 
 * Step 3:
 * NeedToTakeOutputFile name is passed through the command line as args[2]
 * Output to NeedToTakeOutputFile with the format:
 * 1. Some number of lines, each with one course ID
 */
public class NeedToTake {
    public static void main(String[] args) {

        if (args.length < 3) {
            StdOut.println("Execute: java NeedToTake <adjacency list INput file> <need to take INput file> <need to take OUTput file>");
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
        String target = StdIn.readString();
        int amt = StdIn.readInt();
        int c = 0;
        String[] end = new String[amt];
        while (c < amt) {
            end[c] = StdIn.readString();
            c++;
        }
        HashMap < String, Boolean > taken = new HashMap < String, Boolean > ();
        HashMap < String, Boolean > needToTake = new HashMap < String, Boolean > ();

        for (String y: end) {
            dfs(y, courses, taken);
        }

        for (String y: courses.get(target)) {
            dfs(y, courses, needToTake);
        }
        Set < String > keySet = taken.keySet();
        Set < String > nSet = needToTake.keySet();
        ArrayList < String > listofKeys = new ArrayList < String > (keySet);
        ArrayList < String > listofnSet = new ArrayList < String > (nSet);

        for (String x: listofKeys) {
            if (listofnSet.contains(x)) {
                listofnSet.remove(x);
            }
        }

        for (String x: listofnSet) {
            StdOut.println(x);
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