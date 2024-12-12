package com.arvind.restapitest.controller;

import java.io.FileWriter;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
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
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private HttpServletRequest request;

    // GET all students
    @GetMapping(value = "/students", 
                produces = {
                    "application/xml", 
                    "application/xml;charset=UTF-8",
                    "application/json"
                })
    public ResponseEntity<Students> getAllStudents() {
        try {
            ClassPathResource resource = new ClassPathResource("dummy-students.xml");
            InputStream inputStream = resource.getInputStream();
            
            JAXBContext jaxbContext = JAXBContext.newInstance(Students.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Students students = (Students) unmarshaller.unmarshal(inputStream);
            
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // GET student by ID
    @GetMapping(value = "/students/{id}", 
                produces = {
                    "application/xml", 
                    "application/xml;charset=UTF-8",
                    "application/json"
                })
    public ResponseEntity<?> getStudentById(@PathVariable String id) {
        try {
            ClassPathResource resource = new ClassPathResource("dummy-students.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Students.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Students students = (Students) unmarshaller.unmarshal(resource.getInputStream());

            Student foundStudent = students.getStudents().stream()
                .filter(student -> student.getId().equals(id))
                .findFirst()
                .orElse(null);

            if (foundStudent == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(foundStudent);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
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
            // Read existing students
            ClassPathResource resource = new ClassPathResource("dummy-students.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Students.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Students students = (Students) unmarshaller.unmarshal(resource.getInputStream());

            // Add new student
            students.getStudents().add(newStudent);

            // Save updated list
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            
            Path resourceDirectory = Paths.get("src", "main", "resources", "dummy-students.xml");
            try (FileWriter writer = new FileWriter(resourceDirectory.toFile())) {
                marshaller.marshal(students, writer);
            }

            return ResponseEntity.ok()
                .body("Student added successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                .body("Failed to add student: " + e.getMessage());
        }
    }

    // DELETE student
    @DeleteMapping("/students/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable String id) {
        try {
            ClassPathResource resource = new ClassPathResource("dummy-students.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Students.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Students students = (Students) unmarshaller.unmarshal(resource.getInputStream());

            boolean removed = students.getStudents().removeIf(student -> student.getId().equals(id));

            if (!removed) {
                return ResponseEntity.notFound().build();
            }

            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            
            Path resourceDirectory = Paths.get("src", "main", "resources", "dummy-students.xml");
            try (FileWriter writer = new FileWriter(resourceDirectory.toFile())) {
                marshaller.marshal(students, writer);
            }

            return ResponseEntity.ok("Student deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                .body("Failed to delete student: " + e.getMessage());
        }
    }

    // PUT update student
    @PutMapping("/students/{id}")
    public ResponseEntity<String> updateStudent(@PathVariable String id, @RequestBody Student updatedStudent) {
        try {
            ClassPathResource resource = new ClassPathResource("dummy-students.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Students.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Students students = (Students) unmarshaller.unmarshal(resource.getInputStream());

            boolean found = false;
            for (Student student : students.getStudents()) {
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

            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            
            Path resourceDirectory = Paths.get("src", "main", "resources", "dummy-students.xml");
            try (FileWriter writer = new FileWriter(resourceDirectory.toFile())) {
                marshaller.marshal(students, writer);
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
            ClassPathResource resource = new ClassPathResource("dummy-students.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Students.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Students students = (Students) unmarshaller.unmarshal(resource.getInputStream());

            boolean found = false;
            for (Student student : students.getStudents()) {
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

            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            
            Path resourceDirectory = Paths.get("src", "main", "resources", "dummy-students.xml");
            try (FileWriter writer = new FileWriter(resourceDirectory.toFile())) {
                marshaller.marshal(students, writer);
            }

            return ResponseEntity.ok("Student updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                .body("Failed to update student: " + e.getMessage());
        }
    }
}
