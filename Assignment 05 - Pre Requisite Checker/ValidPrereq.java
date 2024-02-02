package prereqchecker;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

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
 * ValidPreReqInputFile name is passed through the command line as args[1]
 * Read from ValidPreReqInputFile with the format:
 * 1. 1 line containing the proposed advanced course
 * 2. 1 line containing the proposed prereq to the advanced course
 * 
 * Step 3:
 * ValidPreReqOutputFile name is passed through the command line as args[2]
 * Output to ValidPreReqOutputFile with the format:
 * 1. 1 line, containing either the word "YES" or "NO"
 */
public class ValidPrereq {
    public static void main(String[] args) {

        /*if ( args.length < 3 ) {
            StdOut.println("Execute: java -cp bin prereqchecker.ValidPrereq <adjacency list INput file> <valid prereq INput file> <valid prereq OUTput file>");
            return;
        }*/

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
        String start = StdIn.readString();
        String end = StdIn.readString();

        HashMap < String, Boolean > visited = new HashMap < String, Boolean > ();

        if (pathExists(end, start, courses, visited)) {
            StdOut.print("NO");
        } else {
            StdOut.print("YES");
        }
    }

    private static boolean pathExists(String start, String end, HashMap < String, ArrayList < String >> adjList, HashMap < String, Boolean > visited) {
        if (start.equals(end))
            return true;
        visited.put(start, true);
        for (String z: adjList.get(start)) {
            if (!visited.getOrDefault(z, false)) {
                if (pathExists(z, end, adjList, visited)) return true;
            }
        }
        return false;
    }
}