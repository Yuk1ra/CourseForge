package org.example;

import java.util.ArrayList;
import java.util.Random;

import static org.example.Main.seed;

public class Generator {
    private final Random random;
    private final String[] courseNames = {
            "Programming",
            "Data Struct",
            "Discrete Math",
            "Computer Org",
            "Algorithms",
            "Operating Systems",
            "Computer Networks",
            "Database",
            "Software Eng",
            "Computer Graphics",
            "Artificial Intelligence",
            "Cybersecurity",
            "Digital Logic",
            "Calculus",
            "Linear Algebra",
            "Statistics",
            "Compiler Design"
    };

    private final String[] firstNames = {
            "Lina", "Omar", "Ahmad", "Sara", "Yousef",
            "Adam", "Maya", "Rami", "Nour", "Khaled",
            "Dana", "Sami", "Layla", "Tariq", "Hana"
    };

    private final String[] lastNames = {
            "Haddad", "Nasser", "Khalil", "Mansour", "Saleh",
            "Odeh", "Hamdan", "Saad", "Awad", "Yasin",
            "Darwish", "Abbas", "Qasem", "Najjar", "Shawqi"
    };
    public Generator() {
        random = new Random(seed);
    }

    public ArrayList<Course> generateCourses(int n) {
        ArrayList<Course> courses = new ArrayList<>();

        for (int i = 0; i < n; i++) {

            String code = "CS" + (200 + i);

            String name = courseNames[i % courseNames.length];

            int credits = 1 + random.nextInt(4);

            int value = 1 + random.nextInt(10);

            String prerequisite = "—";

            if (i > 0 && random.nextBoolean()) {
                int prerequisiteIndex = random.nextInt(i);
                prerequisite = courses.get(prerequisiteIndex).code;
            }

            courses.add(
                    new Course(code, name, credits, value, prerequisite)
            );
        }

        return courses;
    }

    public ArrayList<Record> generateRecords(int m) {
        ArrayList<Record> records = new ArrayList<>();

        for (int i = 0; i < m; i++) {

            int id = 2020000 + random.nextInt(100000);

            String firstName =
                    firstNames[random.nextInt(firstNames.length)];

            String lastName =
                    lastNames[random.nextInt(lastNames.length)];

            double gpa =
                    2.00 + random.nextDouble() * 2.00;

            gpa = Math.round(gpa * 100.0) / 100.0;

            records.add(
                    new Record(
                            id,
                            firstName + " " + lastName,
                            gpa
                    )
            );
        }

        return records;
    }
}
