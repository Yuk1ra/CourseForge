package org.example;

public class Course {
    String code;
    String name;
    int credits;
    int value;
    String prerequisite;

    public Course(String code, String name, int credits, int value, String prerequisite) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.value = value;
        this.prerequisite = prerequisite;
    }

    @Override
    public String toString() {
        return code + " " + name + " " + credits + "cr val " + value
                + " pre:" + prerequisite;
    }
}
