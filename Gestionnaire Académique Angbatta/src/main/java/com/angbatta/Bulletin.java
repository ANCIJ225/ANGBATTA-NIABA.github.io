package com.angbatta;

import java.util.List;

public class Bulletin {
    private Student student;
    private String period;
    private List<Grade> grades;
    private double average;

    public Bulletin(Student student, String period, List<Grade> grades) {
        this.student = student;
        this.period = period;
        this.grades = grades;
        this.average = calculateAverage();
    }

    private double calculateAverage() {
        if (grades.isEmpty()) return 0;
        double sum = 0;
        for (Grade g : grades) {
            sum += g.getValue();
        }
        return sum / grades.size();
    }

    // Getters and setters
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }

    public List<Grade> getGrades() { return grades; }
    public void setGrades(List<Grade> grades) { this.grades = grades; this.average = calculateAverage(); }

    public double getAverage() { return average; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Bulletin for ").append(student.getName()).append(" - ").append(period).append("\n");
        sb.append("Grades:\n");
        for (Grade g : grades) {
            sb.append("- ").append(g.getCourse().getName()).append(": ").append(g.getValue()).append("\n");
        }
        sb.append("Average: ").append(String.format("%.2f", average)).append("\n");
        return sb.toString();
    }
}