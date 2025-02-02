package com.arvind.restapitest.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arvind.restapitest.model.Student;
import com.arvind.restapitest.model.Students;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private HttpServletRequest request;

    private Students studentsData;

    @PostConstruct
    public void init() {
        try {
            // Load initial data from XML file
            ClassPathResource resource = new ClassPathResource("dummy-students.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Students.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            studentsData = (Students) unmarshaller.unmarshal(resource.getInputStream());
            if (studentsData.getStudents() == null) {
                studentsData.setStudents(new ArrayList<>());
            }
        } catch (Exception e) {
            studentsData = new Students();
            studentsData.setStudents(new ArrayList<>());
        }
    }

    // GET all students
    @GetMapping(value = "/students", 
                produces = {
                    "application/xml", 
                    "application/xml;charset=UTF-8",
                    "application/json"
                })
    public ResponseEntity<Students> getAllStudents() {
        return ResponseEntity.ok(studentsData);
    }

    // GET student by ID
    @GetMapping(value = "/students/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable String id) {
        Student foundStudent = studentsData.getStudents().stream()
            .filter(student -> student.getId().equals(id))
            .findFirst()
            .orElse(null);

        if (foundStudent == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(foundStudent);
    }

    // POST new student
    @PostMapping(value = "/students", 
                consumes = {
                    "application/xml", 
                    "application/xml;charset=UTF-8",
                    "application/json"
                })
    public ResponseEntity<String> addStudent(@RequestBody Student newStudent) {
        try {
            studentsData.getStudents().add(newStudent);
            return ResponseEntity.ok("Student added successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                .body("Failed to add student: " + e.getMessage());
        }
    }

    // DELETE student
    @DeleteMapping("/students/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable String id) {
        boolean removed = studentsData.getStudents().removeIf(student -> student.getId().equals(id));

        if (!removed) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok("Student deleted successfully");
    }

    // PUT update student
    @PutMapping("/students/{id}")
    public ResponseEntity<String> updateStudent(@PathVariable String id, @RequestBody Student updatedStudent) {
        try {
            boolean found = false;
            for (Student student : studentsData.getStudents()) {
                if (student.getId().equals(id)) {
                    student.setName(updatedStudent.getName());
                    student.setLocation(updatedStudent.getLocation());
                    student.setPhone(updatedStudent.getPhone());
                    student.setCourse(updatedStudent.getCourse());
                    found = true;
                    break;
                }
            }

            if (!found) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok("Student updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                .body("Failed to update student: " + e.getMessage());
        }
    }

    // PATCH partial update student
    @PatchMapping("/students/{id}")
    public ResponseEntity<String> patchStudent(@PathVariable String id, @RequestBody Student partialStudent) {
        try {
            boolean found = false;
            for (Student student : studentsData.getStudents()) {
                if (student.getId().equals(id)) {
                    if (partialStudent.getName() != null) student.setName(partialStudent.getName());
                    if (partialStudent.getLocation() != null) student.setLocation(partialStudent.getLocation());
                    if (partialStudent.getPhone() != null) student.setPhone(partialStudent.getPhone());
                    if (partialStudent.getCourse() != null) student.setCourse(partialStudent.getCourse());
                    found = true;
                    break;
                }
            }

            if (!found) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok("Student updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                .body("Failed to update student: " + e.getMessage());
        }
    }
}
