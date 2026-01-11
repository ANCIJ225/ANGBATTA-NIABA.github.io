package com.angbatta;

public class Grade {
    private Student student;
    private Course course;
    private double value;
    private String period;

    public Grade(Student student, Course course, double value, String period) {
        this.student = student;
        this.course = course;
        this.value = value;
        this.period = period;
    }

    // Getters and setters
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }

    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }

    @Override
    public String toString() {
        return "Grade{" +
                "student=" + student.getName() +
                ", course=" + course.getName() +
                ", value=" + value +
                ", period='" + period + '\'' +
                '}';
    }
}