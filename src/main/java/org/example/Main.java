package org.example;

import java.util.*;

public class Main {
    //Constants
    public static final int seed = 202417427 % 10000;
    public static final int n = 12 + seed % 6; // Courses
    public static final int m = 40 + seed % 20; // Records
    public static final int C = 15 + seed % 4; // Credit cap
    //Generator and Data
    private static final Generator generator = new Generator();
    private static final ArrayList<Course> courses = generateCourses();
    private static final ArrayList<Record> records = generateRecords();
    //Module 1
    private static int selectionComparisons = 0;
    private static int insertionComparisons = 0;
    private static int swaps = 0;
    private static int shifts = 0;
    //Module 4
    private static int comparisons = 0;

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
                    bestCourseLoad();
                    break;

                case 4:
                    comparisons = 0;
                    ArrayList<Record> sortedRecords = insertionSortRecords();
                    Scanner scanner = new Scanner(System.in);
                    System.out.print("Enter target GPA: ");
                    double targetGPA = scanner.nextDouble();

                    int index = binarySearchGPA(sortedRecords, targetGPA);
                    if (index != -1) {
                        System.out.println("Record found at index: " + index);
                    } else {
                        System.out.println("Record not found.");
                    }
                    System.out.println("Comparisons: " + comparisons);

                    Scanner scanner2 = new Scanner(System.in);
                    System.out.println("\nGCD and LCM");
                    System.out.print("Enter first number: ");
                    int a = scanner2.nextInt();
                    System.out.print("Enter second number: ");
                    int b = scanner2.nextInt();

                    int g = gcd(a, b);
                    int l = (a * b) / g;

                    System.out.println("GCD(" + a + ", " + b + ") = " + g);
                    System.out.println("LCM = " + l);

                    break;

                case 5:
                    runExperiments();
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

    //Module 1
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
    //Module 2
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
    //Module 3
    private static void bestCourseLoad() {
        int k = 10;
        List<Course> eligible = courses.subList(0, k);

        int totalSubsets = 1 << k;

        int bestValue = -1;
        int bestCredits = 0;
        int bestMask = 0;
        int legal = 0;

        for (int i = 0; i < totalSubsets; i++) {

            int credits = 0;
            int value = 0;

            for (int j = 0; j < k; j++) {

                if ((i & (1 << j)) != 0) {
                    credits += eligible.get(j).credits;
                    value += eligible.get(j).value;
                }
            }

            if (credits <= C) {
                legal++;

                if (value > bestValue) {
                    bestValue = value;
                    bestCredits = credits;
                    bestMask = i;
                }
            }
        }

        System.out.println("Best course load:");

        for (int j = 0; j < k; j++) {
            if ((bestMask & (1 << j)) != 0) {
                System.out.println(eligible.get(j));
            }
        }

        System.out.println("Credits: " + bestCredits);
        System.out.println("Value: " + bestValue);
        System.out.println("Subsets checked: " + totalSubsets);
        System.out.println("Legal subsets: " + legal);
    }
    //Module 4
    private static int binarySearchGPA(ArrayList<Record> sortedRecords, double target) {
        int low = 0;
        int high = sortedRecords.size() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            comparisons++;

            if (sortedRecords.get(mid).gpa == target) {
                return mid;
            }

            if (sortedRecords.get(mid).gpa < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return -1;
    }

    private static int gcd(int a, int b) {
        int moduloOperations = 0;

        while (b != 0) {
            int remainder = a % b;
            moduloOperations++;

            a = b;
            b = remainder;
        }

        System.out.println("Modulo operations: " + moduloOperations);

        return a;
    }

    //Module 5
    private static void runExperiments() {

        int[] sizes = {100, 200, 400, 800, 1600};
        int runs = 3;

        System.out.println("\n===== MACHINE EXPERIMENTS =====");

        System.out.println("\nSORTING EXPERIMENT");
        System.out.printf("%-8s %-15s %-15s%n",
                "n", "Selection Avg", "Insertion Avg");

        for (int size : sizes) {

            long totalSelection = 0;
            long totalInsertion = 0;

            for (int run = 0; run < runs; run++) {

                ArrayList<Record> testRecords =
                        generator.generateRecords(size);

                totalSelection += selectionSortExperiment(testRecords);
                totalInsertion += insertionSortExperiment(testRecords);
            }

            double selectionAverage =
                    (double) totalSelection / runs;

            double insertionAverage =
                    (double) totalInsertion / runs;

            System.out.printf(
                    "%-8d %-15.2f %-15.2f%n",
                    size,
                    selectionAverage,
                    insertionAverage
            );
        }

        System.out.println("\nBINARY SEARCH EXPERIMENT");
        System.out.printf("%-8s %-20s%n",
                "n", "Binary Search Avg");

        for (int size : sizes) {

            long totalComparisons = 0;

            for (int run = 0; run < runs; run++) {

                ArrayList<Record> testRecords =
                        generator.generateRecords(size);

                // Sort the records
                ArrayList<Record> sorted =
                        new ArrayList<>(testRecords);

                sortWithoutCounting(sorted);

                double target =
                        sorted.get(sorted.size() - 1).gpa;

                totalComparisons +=
                        binarySearchExperiment(sorted, target);
            }

            double average =
                    (double) totalComparisons / runs;

            System.out.printf(
                    "%-8d %-20.2f%n",
                    size,
                    average
            );
        }
    }

    private static long selectionSortExperiment(
            ArrayList<Record> records
    ) {

        ArrayList<Record> rec =
                new ArrayList<>(records);

        long comparisons = 0;

        for (int i = 0; i < rec.size() - 1; i++) {

            int minIndex = i;

            for (int j = i + 1; j < rec.size(); j++) {

                comparisons++;

                if (rec.get(j).gpa <
                        rec.get(minIndex).gpa) {

                    minIndex = j;
                }
            }

            if (minIndex != i) {

                Record temp = rec.get(i);

                rec.set(
                        i,
                        rec.get(minIndex)
                );

                rec.set(
                        minIndex,
                        temp
                );
            }
        }

        return comparisons;
    }

    private static long insertionSortExperiment(
            ArrayList<Record> records
    ) {

        ArrayList<Record> rec =
                new ArrayList<>(records);

        long comparisons = 0;

        for (int i = 1; i < rec.size(); i++) {

            Record key = rec.get(i);

            int j = i - 1;

            while (j >= 0) {

                comparisons++;

                if (rec.get(j).gpa <= key.gpa) {
                    break;
                }

                rec.set(
                        j + 1,
                        rec.get(j)
                );

                j--;
            }

            rec.set(j + 1, key);
        }

        return comparisons;
    }

    private static void sortWithoutCounting(
            ArrayList<Record> rec
    ) {

        for (int i = 0; i < rec.size() - 1; i++) {

            int minIndex = i;

            for (int j = i + 1; j < rec.size(); j++) {

                if (rec.get(j).gpa <
                        rec.get(minIndex).gpa) {

                    minIndex = j;
                }
            }

            if (minIndex != i) {

                Record temp = rec.get(i);

                rec.set(
                        i,
                        rec.get(minIndex)
                );

                rec.set(
                        minIndex,
                        temp
                );
            }
        }
    }

    private static long binarySearchExperiment(
            ArrayList<Record> sortedRecords,
            double target
    ) {

        int low = 0;
        int high = sortedRecords.size() - 1;

        long comparisons = 0;

        while (low <= high) {

            int mid =
                    low + (high - low) / 2;

            comparisons++;

            if (sortedRecords.get(mid).gpa == target) {
                return comparisons;
            }

            if (sortedRecords.get(mid).gpa < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return comparisons;
    }
}
