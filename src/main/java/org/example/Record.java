package org.example;

public class Record {
    int id;
    String name;
    double gpa;

    public Record(int id, String name, double gpa) {
        this.id = id;
        this.name = name;
        this.gpa = gpa;
    }

    @Override
    public String toString() {
        return id + " " + name + " GPA " + String.format("%.2f", gpa);
    }
}
