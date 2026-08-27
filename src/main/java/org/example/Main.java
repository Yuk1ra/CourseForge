package org.example;

import java.util.*;

public class Main {

    public static int seed = 202417427 % 10000;
    public static int n = 12 + seed % 6; // Courses
    public static int m = 40 + seed % 20; // Records
    public static int C = 15 + seed % 4; // Credit cap

    private static final Generator generator = new Generator();
    private static final ArrayList<Course> courses = generateCourses();
    private static final ArrayList<Record> records = generateRecords();
    private static int selectionComparisons = 0;
    private static int insertionComparisons = 0;
    private static int swaps = 0;
    private static int shifts = 0;


    public static void main(String[] args) {

        System.out.println("Seed: " + seed + "\nCourses: " + n + "\nRecords: " + m + "\nCredit cap: " + C);

        Scanner input = new Scanner(System.in);
        String menu = """
                Enter Menu option:\s
                1: Sort The Records, Count The Work
                2: A Legal Study Order, By Source Removal
                3: Best Course Load, By Checking Every Subset
                4: Two Small Algorithms, Two Counters
                5: Analyse Four Algorithms - Only Four
                10: Exit.""";

        System.out.println(menu);
        int in = input.nextInt();

        while(in != 10){
            switch (in){
                case 1:
                    swaps = 0;
                    shifts = 0;
                    selectionComparisons = 0;
                    insertionComparisons = 0;

                    ArrayList<Record> selectionSorted = selectionSortRecords();
                    ArrayList<Record> insertionSorted = insertionSortRecords();

                    System.out.println("Sorted Records:\nSelection\tInsertion");

                    for (int i = 0; i < m; i++) {
                        System.out.println(
                                selectionSorted.get(i) +
                                        "\t" +
                                        insertionSorted.get(i)
                        );
                    }

                    boolean match = true;

                    for (int i = 0; i < m; i++) {
                        if (selectionSorted.get(i).gpa != insertionSorted.get(i).gpa) {
                            match = false;
                            break;
                        }
                    }

                    System.out.println("Lists have matching GPA order: " + match);
                    System.out.println("Selection Sort Comparisons: " + selectionComparisons);
                    System.out.println("Selection Sort Swaps: " + swaps);
                    System.out.println("Insertion Sort Comparisons: " + insertionComparisons);
                    System.out.println("Insertion Sort Shifts: " + shifts);

                    insertionSortTrace();

                    break;

                case 2:
                    studySort();
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
            }

            System.out.println("\n" + menu);
            in = input.nextInt();
        }
    }

    private static ArrayList<Course> generateCourses(){
        ArrayList<Course> courses = new ArrayList<>();

        System.out.println("Generating " + n + " courses...");
        for (Course course : generator.generateCourses(n)) {
            courses.add(course);
            System.out.println(course);
        }
        return courses;
    }

    private static ArrayList<Record> generateRecords(){
        ArrayList<Record> records = new ArrayList<>();

        System.out.println("Generating " + m + " records...");
        for (Record record : generator.generateRecords(m)) {
            records.add(record);
            System.out.println(record);
        }
        return records;
    }

    private static ArrayList<Record> selectionSortRecords() {
        ArrayList<Record> rec = new ArrayList<>(records);

        for (int i = 0; i < m; i++) { // m = number of records
            int minIndex = i;

            for (int j = i + 1; j < m; j++) {
                selectionComparisons++; // count comparisons

                if (rec.get(j).gpa < rec.get(minIndex).gpa) {
                    minIndex = j;
                }
            }

            if (minIndex != i) {
                Record temp = rec.get(i);
                rec.set(i, rec.get(minIndex));
                rec.set(minIndex, temp);
                swaps++; // count swaps
            }
        }

        return rec;
    }

    private static ArrayList<Record> insertionSortRecords() {
        ArrayList<Record> rec = new ArrayList<>(records);

        for (int i = 1; i < m; i++) {
            Record key = rec.get(i);
            int j = i - 1;

            while (j >= 0) {
                insertionComparisons++;

                if (rec.get(j).gpa <= key.gpa) {
                    break;
                }
                rec.set(j + 1, rec.get(j));
                j--;
                shifts++;
            }

            rec.set(j + 1, key);
        }

        return rec;
    }

    private static void insertionSortTrace() {
        int[] values = {89, 45, 68, 90, 29, 73, 12, 55, 41, 66};

        System.out.print("in: ");
        for (int value : values) {
            System.out.print(value + " ");
        }
        System.out.println();

        for (int i = 1; i < values.length; i++) {
            int key = values[i];
            int j = i - 1;

            while (j >= 0 && values[j] > key) {
                values[j + 1] = values[j];
                j--;
            }

            values[j + 1] = key;

            for (int k = 0; k < values.length; k++) {
                System.out.print(values[k] + " ");

                if (k == i) {
                    System.out.print("| ");
                }
            }

            System.out.println();
        }
    }

    private static void studySort() {
        Map<String, ArrayList<String>> graph = new HashMap<>();
        Map<String, Integer> prerequisiteCount = new HashMap<>();
        Queue<String> queue = new LinkedList<>();
        int prints = 0; // count printed/removed courses
        ArrayList<String> order = new ArrayList<>();

        for (Course course : courses) {
            graph.put(course.code, new ArrayList<>());
            prerequisiteCount.put(course.code, 0);
        }

        for (Course course : courses) {
            if (!course.prerequisite.equals("—")) {
                graph.get(course.prerequisite).add(course.code);
                prerequisiteCount.put(course.code, prerequisiteCount.get(course.code) + 1);
            }
        }

        for (Course course : courses) {
            if (prerequisiteCount.get(course.code) == 0) {
                queue.add(course.code);
            }
        }

        while (!queue.isEmpty()) {
            String current = queue.remove();

            order.add(current);
            prints++;

            for (String dependent : graph.get(current)) {
                int remaining = prerequisiteCount.get(dependent) - 1;
                prerequisiteCount.put(dependent, remaining);

                if (remaining == 0) {
                    queue.add(dependent);
                }
            }
        }
        if (prints != courses.size()) {
            System.out.println("Cycle detected!");
        } else {
            System.out.println("Study order: " + order);
            System.out.println("Order valid: " + verifyStudyOrder(order));
        }
    }

    private static boolean verifyStudyOrder(ArrayList<String> order) {

        for (Course course : courses) {

            if (!course.prerequisite.equals("—")) {

                int prerequisiteIndex = order.indexOf(course.prerequisite);
                int courseIndex = order.indexOf(course.code);

                if (prerequisiteIndex >= courseIndex) {
                    return false;
                }
            }
        }

        return true;
    }
}
