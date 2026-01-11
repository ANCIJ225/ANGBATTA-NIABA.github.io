package com.angbatta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
public class AcademicController {

    @Autowired
    private AcademicService service;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("stats", service.getStatistics());
        return "home";
    }

    // Students
    @GetMapping("/students")
    public String listStudents(Model model) {
        model.addAttribute("students", service.getAllStudents());
        return "students";
    }

    @GetMapping("/students/add")
    public String addStudentForm(Model model) {
        model.addAttribute("student", new Student("", "", 0, ""));
        return "student-form";
    }

    @PostMapping("/students")
    public String addStudent(@ModelAttribute Student student, RedirectAttributes redirectAttributes) {
        if (service.getStudentById(student.getId()) != null) {
            redirectAttributes.addFlashAttribute("error", "Étudiant avec cet ID existe déjà.");
            return "redirect:/students/add";
        }
        service.addStudent(student);
        redirectAttributes.addFlashAttribute("success", "Étudiant ajouté avec succès.");
        return "redirect:/students";
    }

    @GetMapping("/students/{id}/edit")
    public String editStudentForm(@PathVariable String id, Model model) {
        Student student = service.getStudentById(id);
        if (student == null) {
            return "redirect:/students";
        }
        model.addAttribute("student", student);
        return "student-form";
    }

    @PostMapping("/students/{id}")
    public String updateStudent(@PathVariable String id, @ModelAttribute Student student, RedirectAttributes redirectAttributes) {
        student.setId(id);
        service.updateStudent(student);
        redirectAttributes.addFlashAttribute("success", "Étudiant modifié avec succès.");
        return "redirect:/students";
    }

    @PostMapping("/students/{id}/delete")
    public String deleteStudent(@PathVariable String id, RedirectAttributes redirectAttributes) {
        service.deleteStudent(id);
        redirectAttributes.addFlashAttribute("success", "Étudiant supprimé avec succès.");
        return "redirect:/students";
    }

    // Courses
    @GetMapping("/courses")
    public String listCourses(Model model) {
        model.addAttribute("courses", service.getAllCourses());
        return "courses";
    }

    @GetMapping("/courses/add")
    public String addCourseForm(Model model) {
        model.addAttribute("course", new Course("", ""));
        return "course-form";
    }

    @PostMapping("/courses")
    public String addCourse(@ModelAttribute Course course, RedirectAttributes redirectAttributes) {
        if (service.getCourseById(course.getId()) != null) {
            redirectAttributes.addFlashAttribute("error", "Cours avec cet ID existe déjà.");
            return "redirect:/courses/add";
        }
        service.addCourse(course);
        redirectAttributes.addFlashAttribute("success", "Cours ajouté avec succès.");
        return "redirect:/courses";
    }

    @GetMapping("/courses/{id}/edit")
    public String editCourseForm(@PathVariable String id, Model model) {
        Course course = service.getCourseById(id);
        if (course == null) {
            return "redirect:/courses";
        }
        model.addAttribute("course", course);
        return "course-form";
    }

    @PostMapping("/courses/{id}")
    public String updateCourse(@PathVariable String id, @ModelAttribute Course course, RedirectAttributes redirectAttributes) {
        course.setId(id);
        service.updateCourse(course);
        redirectAttributes.addFlashAttribute("success", "Cours modifié avec succès.");
        return "redirect:/courses";
    }

    @PostMapping("/courses/{id}/delete")
    public String deleteCourse(@PathVariable String id, RedirectAttributes redirectAttributes) {
        service.deleteCourse(id);
        redirectAttributes.addFlashAttribute("success", "Cours supprimé avec succès.");
        return "redirect:/courses";
    }

    // Grades
    @GetMapping("/grades")
    public String listGrades(Model model) {
        model.addAttribute("grades", service.getAllGrades());
        return "grades";
    }

    @GetMapping("/grades/add")
    public String addGradeForm(Model model) {
        model.addAttribute("grade", new Grade(null, null, 0.0, ""));
        model.addAttribute("students", service.getAllStudents());
        model.addAttribute("courses", service.getAllCourses());
        return "grade-form";
    }

    @PostMapping("/grades")
    public String addGrade(@RequestParam String studentId, @RequestParam String courseId,
                          @RequestParam double value, @RequestParam String period,
                          RedirectAttributes redirectAttributes) {
        Student student = service.getStudentById(studentId);
        Course course = service.getCourseById(courseId);
        if (student == null || course == null) {
            redirectAttributes.addFlashAttribute("error", "Étudiant ou cours non trouvé.");
            return "redirect:/grades/add";
        }
        if (value < 0 || value > 20) {
            redirectAttributes.addFlashAttribute("error", "Note invalide. Doit être entre 0 et 20.");
            return "redirect:/grades/add";
        }
        Grade grade = new Grade(student, course, value, period);
        service.addGrade(grade);
        redirectAttributes.addFlashAttribute("success", "Note ajoutée avec succès.");
        return "redirect:/grades";
    }

    @GetMapping("/grades/{studentId}/{courseId}/{period}/edit")
    public String editGradeForm(@PathVariable String studentId, @PathVariable String courseId,
                               @PathVariable String period, Model model) {
        List<Grade> grades = service.getAllGrades();
        Grade grade = grades.stream()
                .filter(g -> g.getStudent().getId().equals(studentId) &&
                        g.getCourse().getId().equals(courseId) &&
                        g.getPeriod().equals(period))
                .findFirst().orElse(null);
        if (grade == null) {
            return "redirect:/grades";
        }
        model.addAttribute("grade", grade);
        return "grade-form";
    }

    @PostMapping("/grades/{studentId}/{courseId}/{period}")
    public String updateGrade(@PathVariable String studentId, @PathVariable String courseId,
                             @PathVariable String period, @RequestParam double value,
                             RedirectAttributes redirectAttributes) {
        if (value < 0 || value > 20) {
            redirectAttributes.addFlashAttribute("error", "Note invalide. Doit être entre 0 et 20.");
            return "redirect:/grades/" + studentId + "/" + courseId + "/" + period + "/edit";
        }
        service.updateGrade(studentId, courseId, period, value);
        redirectAttributes.addFlashAttribute("success", "Note modifiée avec succès.");
        return "redirect:/grades";
    }

    @PostMapping("/grades/{studentId}/{courseId}/{period}/delete")
    public String deleteGrade(@PathVariable String studentId, @PathVariable String courseId,
                             @PathVariable String period, RedirectAttributes redirectAttributes) {
        service.deleteGrade(studentId, courseId, period);
        redirectAttributes.addFlashAttribute("success", "Note supprimée avec succès.");
        return "redirect:/grades";
    }

    // Bulletin
    @GetMapping("/bulletin")
    public String bulletinForm(Model model) {
        model.addAttribute("students", service.getAllStudents());
        return "bulletin-form";
    }

    @PostMapping("/bulletin")
    public String generateBulletin(@RequestParam String studentId, @RequestParam String period, Model model) {
        Bulletin bulletin = service.generateBulletin(studentId, period);
        if (bulletin == null) {
            model.addAttribute("error", "Aucune note trouvée pour cette période.");
            model.addAttribute("students", service.getAllStudents());
            return "bulletin-form";
        }
        model.addAttribute("bulletin", bulletin);
        return "bulletin";
    }

    // Promotion
    @PostMapping("/promote")
    public String promoteStudents(@RequestParam String period, RedirectAttributes redirectAttributes) {
        service.promoteStudents(period);
        redirectAttributes.addFlashAttribute("success", "Promotion effectuée pour la période " + period);
        return "redirect:/";
    }

    // Statistics
    @GetMapping("/statistics")
    public String showStatistics(Model model) {
        model.addAttribute("stats", service.getStatistics());
        return "statistics";
    }
}