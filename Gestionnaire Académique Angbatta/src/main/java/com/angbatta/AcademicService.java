package com.angbatta;

import org.springframework.stereotype.Service;
import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

@Service
public class AcademicService {
    private List<Student> students = new ArrayList<>();
    private List<Course> courses = new ArrayList<>();
    private List<Grade> grades = new ArrayList<>();

    public AcademicService() {
        loadData();
        // Add some sample data if empty
        if (courses.isEmpty()) {
            courses.add(new Course("MATH", "Mathématiques"));
            courses.add(new Course("FR", "Français"));
        }
        if (students.isEmpty()) {
            students.add(new Student("1", "Alice", 10, "Classe 1"));
            students.add(new Student("2", "Bob", 11, "Classe 1"));
        }
    }

    // Student methods
    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    public Student getStudentById(String id) {
        return students.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
    }

    public void addStudent(Student student) {
        if (getStudentById(student.getId()) == null) {
            students.add(student);
            saveData();
        }
    }

    public void updateStudent(Student student) {
        Student existing = getStudentById(student.getId());
        if (existing != null) {
            existing.setName(student.getName());
            existing.setAge(student.getAge());
            existing.setClassLevel(student.getClassLevel());
            saveData();
        }
    }

    public void deleteStudent(String id) {
        students.removeIf(s -> s.getId().equals(id));
        grades.removeIf(g -> g.getStudent().getId().equals(id));
        saveData();
    }

    // Course methods
    public List<Course> getAllCourses() {
        return new ArrayList<>(courses);
    }

    public Course getCourseById(String id) {
        return courses.stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);
    }

    public void addCourse(Course course) {
        if (getCourseById(course.getId()) == null) {
            courses.add(course);
            saveData();
        }
    }

    public void updateCourse(Course course) {
        Course existing = getCourseById(course.getId());
        if (existing != null) {
            existing.setName(course.getName());
            saveData();
        }
    }

    public void deleteCourse(String id) {
        courses.removeIf(c -> c.getId().equals(id));
        grades.removeIf(g -> g.getCourse().getId().equals(id));
        saveData();
    }

    // Grade methods
    public List<Grade> getAllGrades() {
        return new ArrayList<>(grades);
    }

    public List<Grade> getGradesByStudent(String studentId) {
        return grades.stream().filter(g -> g.getStudent().getId().equals(studentId)).collect(Collectors.toList());
    }

    public void addGrade(Grade grade) {
        grades.add(grade);
        saveData();
    }

    public void updateGrade(String studentId, String courseId, String period, double value) {
        Grade grade = grades.stream()
                .filter(g -> g.getStudent().getId().equals(studentId) &&
                        g.getCourse().getId().equals(courseId) &&
                        g.getPeriod().equals(period))
                .findFirst().orElse(null);
        if (grade != null) {
            grade.setValue(value);
            saveData();
        }
    }

    public void deleteGrade(String studentId, String courseId, String period) {
        grades.removeIf(g -> g.getStudent().getId().equals(studentId) &&
                g.getCourse().getId().equals(courseId) &&
                g.getPeriod().equals(period));
        saveData();
    }

    // Bulletin methods
    public Bulletin generateBulletin(String studentId, String period) {
        Student student = getStudentById(studentId);
        if (student == null) return null;

        List<Grade> studentGrades = grades.stream()
                .filter(g -> g.getStudent().getId().equals(studentId) && g.getPeriod().equals(period))
                .collect(Collectors.toList());

        if (studentGrades.isEmpty()) return null;

        return new Bulletin(student, period, studentGrades);
    }

    // Promotion
    public void promoteStudents(String period) {
        for (Student student : students) {
            Bulletin bulletin = generateBulletin(student.getId(), period);
            if (bulletin != null && bulletin.getAverage() >= 10) {
                String current = student.getClassLevel();
                if (current.equals("Classe 1")) student.setClassLevel("Classe 2");
                else if (current.equals("Classe 2")) student.setClassLevel("Classe 3");
                else if (current.equals("Classe 3")) student.setClassLevel("Classe 4");
            }
        }
        saveData();
    }

    // Statistics
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("studentCount", students.size());
        stats.put("courseCount", courses.size());
        stats.put("gradeCount", grades.size());

        if (!grades.isEmpty()) {
            double total = grades.stream().mapToDouble(Grade::getValue).sum();
            double avg = total / grades.size();
            double min = grades.stream().mapToDouble(Grade::getValue).min().orElse(0);
            double max = grades.stream().mapToDouble(Grade::getValue).max().orElse(0);
            stats.put("average", avg);
            stats.put("min", min);
            stats.put("max", max);
        }

        Map<String, Integer> classCount = new HashMap<>();
        for (Student s : students) {
            classCount.put(s.getClassLevel(), classCount.getOrDefault(s.getClassLevel(), 0) + 1);
        }
        stats.put("classDistribution", classCount);

        return stats;
    }

    private void saveData() {
        try {
            // Save students
            PrintWriter pw = new PrintWriter(new FileWriter("students.txt"));
            for (Student s : students) {
                pw.println(s.getId() + "," + s.getName() + "," + s.getAge() + "," + s.getClassLevel());
            }
            pw.close();

            // Save courses
            pw = new PrintWriter(new FileWriter("courses.txt"));
            for (Course c : courses) {
                pw.println(c.getId() + "," + c.getName());
            }
            pw.close();

            // Save grades
            pw = new PrintWriter(new FileWriter("grades.txt"));
            for (Grade g : grades) {
                pw.println(g.getStudent().getId() + "," + g.getCourse().getId() + "," + g.getValue() + "," + g.getPeriod());
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        try {
            // Load students
            File file = new File("students.txt");
            if (file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        students.add(new Student(parts[0], parts[1], Integer.parseInt(parts[2]), parts[3]));
                    }
                }
                br.close();
            }

            // Load courses
            file = new File("courses.txt");
            if (file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 2) {
                        courses.add(new Course(parts[0], parts[1]));
                    }
                }
                br.close();
            }

            // Load grades
            file = new File("grades.txt");
            if (file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        Student s = getStudentById(parts[0]);
                        Course c = getCourseById(parts[1]);
                        if (s != null && c != null) {
                            grades.add(new Grade(s, c, Double.parseDouble(parts[2]), parts[3]));
                        }
                    }
                }
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}