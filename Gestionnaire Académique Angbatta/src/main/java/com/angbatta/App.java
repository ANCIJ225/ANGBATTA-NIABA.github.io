package com.angbatta;

import java.util.*;
import java.io.*;
import java.io.*;

public class App {
    private static List<Student> students = new ArrayList<>();
    private static List<Course> courses = new ArrayList<>();
    private static List<Grade> grades = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
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

        while (true) {
            System.out.println("\n=== Gestionnaire Académique ===");
            System.out.println("1. Ajouter Étudiant");
            System.out.println("2. Ajouter Cours");
            System.out.println("3. Ajouter Note");
            System.out.println("4. Générer Bulletin");
            System.out.println("5. Promouvoir Étudiants");
            System.out.println("6. Lister Étudiants");
            System.out.println("7. Lister Cours");
            System.out.println("8. Lister Notes d'un Étudiant");
            System.out.println("9. Statistiques");
            System.out.println("10. Modifier Étudiant");
            System.out.println("11. Supprimer Étudiant");
            System.out.println("12. Modifier Cours");
            System.out.println("13. Supprimer Cours");
            System.out.println("14. Modifier Note");
            System.out.println("15. Supprimer Note");
            System.out.println("16. Sauvegarder Données");
            System.out.println("17. Quitter");
            System.out.print("Choisissez une option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            switch (choice) {
                case 1: addStudent(); break;
                case 2: addCourse(); break;
                case 3: addGrade(); break;
                case 4: generateBulletin(); break;
                case 5: promoteStudents(); break;
                case 6: listStudents(); break;
                case 7: listCourses(); break;
                case 8: listStudentGrades(); break;
                case 9: showStatistics(); break;
                case 10: editStudent(); break;
                case 11: deleteStudent(); break;
                case 12: editCourse(); break;
                case 13: deleteCourse(); break;
                case 14: editGrade(); break;
                case 15: deleteGrade(); break;
                case 16: saveData(); break;
                case 17: saveData(); return;
                default: System.out.println("Choix invalide");
            }
        }
    }

    private static void addStudent() {
        System.out.print("ID: ");
        String id = scanner.nextLine();
        if (findStudent(id) != null) {
            System.out.println("Étudiant avec cet ID existe déjà.");
            return;
        }
        System.out.print("Nom: ");
        String name = scanner.nextLine();
        System.out.print("Âge: ");
        int age = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Niveau de classe: ");
        String level = scanner.nextLine();
        students.add(new Student(id, name, age, level));
        System.out.println("Étudiant ajouté.");
    }

    private static void addCourse() {
        System.out.print("ID: ");
        String id = scanner.nextLine();
        if (findCourse(id) != null) {
            System.out.println("Cours avec cet ID existe déjà.");
            return;
        }
        System.out.print("Nom: ");
        String name = scanner.nextLine();
        courses.add(new Course(id, name));
        System.out.println("Cours ajouté.");
    }

    private static void addGrade() {
        System.out.print("ID Étudiant: ");
        String sid = scanner.nextLine();
        Student s = findStudent(sid);
        if (s == null) { System.out.println("Étudiant non trouvé."); return; }
        System.out.print("ID Cours: ");
        String cid = scanner.nextLine();
        Course c = findCourse(cid);
        if (c == null) { System.out.println("Cours non trouvé."); return; }
        System.out.print("Valeur de la note (0-20): ");
        double val = scanner.nextDouble();
        if (val < 0 || val > 20) {
            System.out.println("Note invalide. Doit être entre 0 et 20.");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();
        System.out.print("Période: ");
        String per = scanner.nextLine();
        grades.add(new Grade(s, c, val, per));
        System.out.println("Note ajoutée.");
    }

    private static void generateBulletin() {
        System.out.print("ID Étudiant: ");
        String sid = scanner.nextLine();
        Student s = findStudent(sid);
        if (s == null) { System.out.println("Étudiant non trouvé."); return; }
        System.out.print("Période: ");
        String per = scanner.nextLine();
        List<Grade> studentGrades = new ArrayList<>();
        for (Grade g : grades) {
            if (g.getStudent().getId().equals(sid) && g.getPeriod().equals(per)) {
                studentGrades.add(g);
            }
        }
        if (studentGrades.isEmpty()) {
            System.out.println("Aucune note trouvée pour cette période.");
            return;
        }
        Bulletin b = new Bulletin(s, per, studentGrades);
        System.out.println(b);
    }

    private static void promoteStudents() {
        System.out.print("Période: ");
        String per = scanner.nextLine();
        for (Student s : students) {
            List<Grade> studentGrades = new ArrayList<>();
            for (Grade g : grades) {
                if (g.getStudent().getId().equals(s.getId()) && g.getPeriod().equals(per)) {
                    studentGrades.add(g);
                }
            }
            if (!studentGrades.isEmpty()) {
                Bulletin b = new Bulletin(s, per, studentGrades);
                if (b.getAverage() >= 10) {
                    // Promote
                    String current = s.getClassLevel();
                    if (current.equals("Classe 1")) s.setClassLevel("Classe 2");
                    else if (current.equals("Classe 2")) s.setClassLevel("Classe 3");
                    else if (current.equals("Classe 3")) s.setClassLevel("Classe 4");
                    System.out.println(s.getName() + " promu(e) en " + s.getClassLevel());
                } else {
                    System.out.println(s.getName() + " non promu(e). Moyenne: " + String.format("%.2f", b.getAverage()));
                }
            } else {
                System.out.println(s.getName() + " : aucune note pour cette période.");
            }
        }
    }

    private static void listStudents() {
        if (students.isEmpty()) {
            System.out.println("Aucun étudiant enregistré.");
            return;
        }
        System.out.println("Liste des étudiants:");
        for (Student s : students) {
            System.out.println("- " + s.getId() + ": " + s.getName() + " (" + s.getClassLevel() + ")");
        }
    }

    private static void listCourses() {
        if (courses.isEmpty()) {
            System.out.println("Aucun cours enregistré.");
            return;
        }
        System.out.println("Liste des cours:");
        for (Course c : courses) {
            System.out.println("- " + c.getId() + ": " + c.getName());
        }
    }

    private static void listStudentGrades() {
        System.out.print("ID Étudiant: ");
        String sid = scanner.nextLine();
        Student s = findStudent(sid);
        if (s == null) { System.out.println("Étudiant non trouvé."); return; }
        System.out.println("Notes de " + s.getName() + ":");
        boolean hasGrades = false;
        for (Grade g : grades) {
            if (g.getStudent().getId().equals(sid)) {
                System.out.println("- " + g.getCourse().getName() + " (" + g.getPeriod() + "): " + g.getValue());
                hasGrades = true;
            }
        }
        if (!hasGrades) {
            System.out.println("Aucune note trouvée.");
        }
    }

    private static void showStatistics() {
        System.out.println("=== Statistiques ===");
        System.out.println("Nombre d'étudiants: " + students.size());
        System.out.println("Nombre de cours: " + courses.size());
        System.out.println("Nombre de notes: " + grades.size());
        
        if (!grades.isEmpty()) {
            double total = 0;
            double min = 20;
            double max = 0;
            for (Grade g : grades) {
                total += g.getValue();
                if (g.getValue() < min) min = g.getValue();
                if (g.getValue() > max) max = g.getValue();
            }
            double avg = total / grades.size();
            System.out.println("Moyenne générale: " + String.format("%.2f", avg));
            System.out.println("Note minimale: " + min);
            System.out.println("Note maximale: " + max);
        }
        
        // Students per class
        Map<String, Integer> classCount = new HashMap<>();
        for (Student s : students) {
            classCount.put(s.getClassLevel(), classCount.getOrDefault(s.getClassLevel(), 0) + 1);
        }
        System.out.println("Répartition par classe:");
        for (Map.Entry<String, Integer> entry : classCount.entrySet()) {
            System.out.println("- " + entry.getKey() + ": " + entry.getValue() + " étudiant(s)");
        }
    }

    private static void editStudent() {
        System.out.print("ID Étudiant à modifier: ");
        String id = scanner.nextLine();
        Student s = findStudent(id);
        if (s == null) {
            System.out.println("Étudiant non trouvé.");
            return;
        }
        System.out.print("Nouveau nom (" + s.getName() + "): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) s.setName(name);
        System.out.print("Nouvel âge (" + s.getAge() + "): ");
        String ageStr = scanner.nextLine();
        if (!ageStr.isEmpty()) {
            try {
                int age = Integer.parseInt(ageStr);
                s.setAge(age);
            } catch (NumberFormatException e) {
                System.out.println("Âge invalide.");
            }
        }
        System.out.print("Nouveau niveau de classe (" + s.getClassLevel() + "): ");
        String level = scanner.nextLine();
        if (!level.isEmpty()) s.setClassLevel(level);
        System.out.println("Étudiant modifié.");
    }

    private static void deleteStudent() {
        System.out.print("ID Étudiant à supprimer: ");
        String id = scanner.nextLine();
        Student s = findStudent(id);
        if (s == null) {
            System.out.println("Étudiant non trouvé.");
            return;
        }
        students.remove(s);
        // Remove associated grades
        grades.removeIf(g -> g.getStudent().getId().equals(id));
        System.out.println("Étudiant supprimé.");
    }

    private static void editCourse() {
        System.out.print("ID Cours à modifier: ");
        String id = scanner.nextLine();
        Course c = findCourse(id);
        if (c == null) {
            System.out.println("Cours non trouvé.");
            return;
        }
        System.out.print("Nouveau nom (" + c.getName() + "): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) c.setName(name);
        System.out.println("Cours modifié.");
    }

    private static void deleteCourse() {
        System.out.print("ID Cours à supprimer: ");
        String id = scanner.nextLine();
        Course c = findCourse(id);
        if (c == null) {
            System.out.println("Cours non trouvé.");
            return;
        }
        courses.remove(c);
        // Remove associated grades
        grades.removeIf(g -> g.getCourse().getId().equals(id));
        System.out.println("Cours supprimé.");
    }

    private static void editGrade() {
        System.out.print("ID Étudiant: ");
        String sid = scanner.nextLine();
        Student s = findStudent(sid);
        if (s == null) {
            System.out.println("Étudiant non trouvé.");
            return;
        }
        System.out.print("ID Cours: ");
        String cid = scanner.nextLine();
        Course c = findCourse(cid);
        if (c == null) {
            System.out.println("Cours non trouvé.");
            return;
        }
        System.out.print("Période: ");
        String per = scanner.nextLine();
        Grade g = null;
        for (Grade grade : grades) {
            if (grade.getStudent().getId().equals(sid) && grade.getCourse().getId().equals(cid) && grade.getPeriod().equals(per)) {
                g = grade;
                break;
            }
        }
        if (g == null) {
            System.out.println("Note non trouvée.");
            return;
        }
        System.out.print("Nouvelle valeur (" + g.getValue() + "): ");
        String valStr = scanner.nextLine();
        if (!valStr.isEmpty()) {
            try {
                double val = Double.parseDouble(valStr);
                if (val >= 0 && val <= 20) {
                    g.setValue(val);
                    System.out.println("Note modifiée.");
                } else {
                    System.out.println("Note invalide. Doit être entre 0 et 20.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Valeur invalide.");
            }
        }
    }

    private static void deleteGrade() {
        System.out.print("ID Étudiant: ");
        String sid = scanner.nextLine();
        Student s = findStudent(sid);
        if (s == null) {
            System.out.println("Étudiant non trouvé.");
            return;
        }
        System.out.print("ID Cours: ");
        String cid = scanner.nextLine();
        Course c = findCourse(cid);
        if (c == null) {
            System.out.println("Cours non trouvé.");
            return;
        }
        System.out.print("Période: ");
        String per = scanner.nextLine();
        Grade g = null;
        for (Grade grade : grades) {
            if (grade.getStudent().getId().equals(sid) && grade.getCourse().getId().equals(cid) && grade.getPeriod().equals(per)) {
                g = grade;
                break;
            }
        }
        if (g == null) {
            System.out.println("Note non trouvée.");
            return;
        }
        grades.remove(g);
        System.out.println("Note supprimée.");
    }

    private static void saveData() {
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
            
            System.out.println("Données sauvegardées.");
        } catch (IOException e) {
            System.out.println("Erreur lors de la sauvegarde: " + e.getMessage());
        }
    }

    private static void loadData() {
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
                        Student s = findStudent(parts[0]);
                        Course c = findCourse(parts[1]);
                        if (s != null && c != null) {
                            grades.add(new Grade(s, c, Double.parseDouble(parts[2]), parts[3]));
                        }
                    }
                }
                br.close();
            }
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement: " + e.getMessage());
        }
    }

    private static Student findStudent(String id) {
        for (Student s : students) {
            if (s.getId().equals(id)) return s;
        }
        return null;
    }

    private static Course findCourse(String id) {
        for (Course c : courses) {
            if (c.getId().equals(id)) return c;
        }
        return null;
    }
}