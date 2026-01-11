package com.angbatta;

public class Student {
    private String id;
    private String name;
    private int age;
    private String classLevel;

    public Student(String id, String name, int age, String classLevel) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.classLevel = classLevel;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getClassLevel() { return classLevel; }
    public void setClassLevel(String classLevel) { this.classLevel = classLevel; }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", classLevel='" + classLevel + '\'' +
                '}';
    }
}