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
 * SchedulePlanInputFile name is passed through the command line as args[1]
 * Read from SchedulePlanInputFile with the format:
 * 1. One line containing a course ID
 * 2. c (int): number of courses
 * 3. c lines, each with one course ID
 * 
 * Step 3:
 * SchedulePlanOutputFile name is passed through the command line as args[2]
 * Output to SchedulePlanOutputFile with the format:
 * 1. One line containing an int c, the number of semesters required to take the
 * course
 * 2. c lines, each with space separated course ID's
 */
public class SchedulePlan {

    public static Boolean checkPreReq(HashMap < String, ArrayList < String >> x, ArrayList < String > z, String y) {
        System.out.println(z + " " + y);
        for (String t: z) {
            if (t.equals(y)) {
                return true;
            } else {
                if (checkPreReq(x, x.get(t), y))
                    return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {

        StdIn.setFile(args[0]);

        int i = Integer.parseInt(StdIn.readLine());
        HashSet < String > course = new HashSet < > ();
        for (int j = 0; j < i; j++) {
            course.add(StdIn.readLine());
        }

        HashMap < String, ArrayList < String >> courses = new HashMap < String, ArrayList < String >> ();
        for (String x: course) {
            courses.put(x, new ArrayList < String > ());
        }
        int k = Integer.parseInt(StdIn.readLine());
        for (int l = 0; l < k; l++) {
            String start = StdIn.readLine();
            if (course.contains(start.substring(0, start.indexOf(" ")))) {
                courses.get(start.substring(0, start.indexOf(" "))).add(start.substring(start.indexOf(" ") + 1));
            }

        }

        StdIn.setFile(args[1]);
        StdOut.setFile(args[2]);
        String targetCourse = StdIn.readLine();
        int m = Integer.parseInt(StdIn.readLine());
        ArrayList < String > courseChecks = new ArrayList < String > ();
        ArrayList < String > coursesToTake = new ArrayList < String > ();
        for (int p = 0; p < m; p++) {
            String temp = StdIn.readLine();
            courseChecks.add(temp);
        }

        for (String x: courses.keySet()) {
            if (checkPreReq(courses, courseChecks, x)) {
                if (!courseChecks.contains(x)) {
                    courseChecks.add(x);
                    m++;
                }
            }
        }
        for (String x: courses.keySet()) {
            for (String name: courses.get(x)) {
                if (checkPreReq(courses, courses.get(targetCourse), name) && !courseChecks.contains(name)) {
                    coursesToTake.add(name);
                }
            }
        }

        for (String x: courses.keySet()) {
            if (courseChecks.contains(x)) {
                coursesToTake.remove(x);
            }
        }

        HashSet < String > coursesToTakeHash = new HashSet < String > ();
        for (String x: coursesToTake)
            coursesToTakeHash.add(x);

        ArrayList < String > [] schedule = new ArrayList[23];
        for (int u = 0; u < 23; u++) {
            schedule[u] = new ArrayList < String > ();
        }
        coursesToTake = new ArrayList < String > (coursesToTakeHash);

        boolean courseCheck = false;
        int scheduleCounter = 0;
        ArrayList < String > temp = new ArrayList < String > ();
        courseChecks.clear();
        while (coursesToTake.size() > 0) {
            for (int s = 0; s < coursesToTake.size(); s++) {
                courseCheck = true;
                for (int r = 0; r < coursesToTake.size(); r++) {
                    if (checkPreReq(courses, courses.get(coursesToTake.get(s)), coursesToTake.get(r)) &&
                        !coursesToTake.get(r).equals(coursesToTake.get(s))) {
                        courseCheck = false;
                    }
                }

                if (courseCheck) {
                    temp.add(coursesToTake.get(s));
                    courseChecks.add(coursesToTake.get(s));
                }
            }
            for (String x: courseChecks) {
                if (coursesToTake.contains(x)) {
                    coursesToTake.remove(x);
                }
            }

            for (String x: temp)
                schedule[scheduleCounter].add(x);
            scheduleCounter++;
            temp.clear();
        }

        StdOut.println(scheduleCounter);

        for (int d = 0; d < schedule.length; d++) {
            for (String x: schedule[d]) {
                StdOut.print(x + " ");
            }
            StdOut.println();
        }

    }

}