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
public class StudentController {

    @Autowired
    private HttpServletRequest request;

    @GetMapping(value = "/students", consumes = {"application/xml", "application/xml;charset=UTF-8"})
    public String getStudents(@RequestBody String xmlData) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Students.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(xmlData);
            Students students = (Students) unmarshaller.unmarshal(reader);

            // Process the received XML data
            for (Student student : students.getStudents()) {
                System.out.println("Received student: " + student);
            }

            return "Student data processed successfully";
        } catch (JAXBException e) {
            e.printStackTrace();
            return "Failed to process student data";
        }
    }

    @PostMapping(value = "/students", 
                consumes = {"application/xml", "application/xml;charset=UTF-8"},
                produces = {"application/xml", "application/xml;charset=UTF-8"})
    public Students createStudents(@RequestBody Students students) {
        // Process the received students
        System.out.println("Creating students:");
        for (Student student : students.getStudents()) {
            System.out.println("Creating student: " + student);
        }
        
        // Return the same students object as response
        return students;
    }

    @GetMapping(value = "/dummy-students", 
                produces = {"application/xml", "application/xml;charset=UTF-8"})
    public String getDummyStudents() {
        try {
            // Load the XML file from resources
            ClassPathResource resource = new ClassPathResource("dummy-students.xml");
            InputStream inputStream = resource.getInputStream();
            return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return "<error>Failed to load dummy data</error>";
        }
    }

    @GetMapping("/test")
    public String test() {
        return "API is working!";
    }

    @PostMapping(value = "/dummy-students", 
                consumes = {
                    "application/xml", 
                    "application/xml;charset=UTF-8",
                    "application/json"
                },
                produces = {
                    "application/xml", 
                    "application/xml;charset=UTF-8",
                    "application/json"
                })
    public ResponseEntity<String> saveDummyStudents(@RequestBody String data) {
        try {
            Students newStudents;
            JAXBContext jaxbContext = JAXBContext.newInstance(Students.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            if (isJsonRequest()) {
                ObjectMapper mapper = new ObjectMapper();
                newStudents = mapper.readValue(data, Students.class);
            } else {
                StringReader reader = new StringReader(data);
                newStudents = (Students) unmarshaller.unmarshal(reader);
            }
            
            // Read existing students from file
            ClassPathResource resource = new ClassPathResource("dummy-students.xml");
            Students existingStudents;
            try {
                existingStudents = (Students) unmarshaller.unmarshal(resource.getInputStream());
            } catch (Exception e) {
                // If file doesn't exist or is empty, create new Students object
                existingStudents = new Students();
            }

            // Initialize lists if null
            if (existingStudents.getStudents() == null) {
                existingStudents.setStudents(new ArrayList<>());
            }
            if (newStudents.getStudents() == null) {
                newStudents.setStudents(new ArrayList<>());
            }

            // Combine existing and new students
            List<Student> combinedList = new ArrayList<>(existingStudents.getStudents());
            combinedList.addAll(newStudents.getStudents());
            existingStudents.setStudents(combinedList);

            // Convert combined students back to XML and save
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            
            // Get the path to resources directory
            Path resourceDirectory = Paths.get("src", "main", "resources", "dummy-students.xml");
            
            // Write the combined XML data to the file
            try (FileWriter writer = new FileWriter(resourceDirectory.toFile())) {
                marshaller.marshal(existingStudents, writer);
            }
            
            return ResponseEntity.ok()
                .body(createErrorResponse("Data successfully appended to dummy-students.xml", isJsonRequest()));
                
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                .body(createErrorResponse("Failed to save data: " + e.getMessage(), isJsonRequest()));
        }
    }

    @GetMapping(value = "/all-students", 
                produces = {
                    "application/xml", 
                    "application/xml;charset=UTF-8",
                    "application/json"
                })
    public ResponseEntity<Students> getAllStudents() {
        try {
            // Load the XML file from resources
            ClassPathResource resource = new ClassPathResource("dummy-students.xml");
            InputStream inputStream = resource.getInputStream();
            
            // Convert XML to Students object
            JAXBContext jaxbContext = JAXBContext.newInstance(Students.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Students students = (Students) unmarshaller.unmarshal(inputStream);
            
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // DELETE endpoint to remove a student by ID
    @DeleteMapping(value = "/dummy-students/{id}",
                  produces = {"application/xml", "application/xml;charset=UTF-8"})
    public ResponseEntity<String> deleteStudent(@PathVariable String id) {
        try {
            // Read existing students
            ClassPathResource resource = new ClassPathResource("dummy-students.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Students.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Students students = (Students) unmarshaller.unmarshal(resource.getInputStream());

            // Remove student with matching ID
            boolean removed = students.getStudents().removeIf(student -> student.getId().equals(id));

            if (!removed) {
                return ResponseEntity.notFound().build();
            }

            // Save updated list
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            
            Path resourceDirectory = Paths.get("src", "main", "resources", "dummy-students.xml");
            try (FileWriter writer = new FileWriter(resourceDirectory.toFile())) {
                marshaller.marshal(students, writer);
            }

            return ResponseEntity.ok()
                .body("<response><message>Student with ID " + id + " deleted successfully</message></response>");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                .body("<error><message>Failed to delete student: " + e.getMessage() + "</message></error>");
        }
    }

    // PUT endpoint to update a student completely
    @PutMapping(value = "/dummy-students/{id}",
                consumes = {"application/xml", "application/xml;charset=UTF-8"},
                produces = {"application/xml", "application/xml;charset=UTF-8"})
    public ResponseEntity<String> updateStudent(@PathVariable String id, @RequestBody Student updatedStudent) {
        try {
            // Read existing students
            ClassPathResource resource = new ClassPathResource("dummy-students.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Students.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Students students = (Students) unmarshaller.unmarshal(resource.getInputStream());

            // Find and update the student
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

            // Save updated list
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            
            Path resourceDirectory = Paths.get("src", "main", "resources", "dummy-students.xml");
            try (FileWriter writer = new FileWriter(resourceDirectory.toFile())) {
                marshaller.marshal(students, writer);
            }

            return ResponseEntity.ok()
                .body("<response><message>Student with ID " + id + " updated successfully</message></response>");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                .body("<error><message>Failed to update student: " + e.getMessage() + "</message></error>");
        }
    }

    // PATCH endpoint to update specific fields of a student
    @PatchMapping(value = "/dummy-students/{id}",
                 consumes = {"application/xml", "application/xml;charset=UTF-8"},
                 produces = {"application/xml", "application/xml;charset=UTF-8"})
    public ResponseEntity<String> patchStudent(@PathVariable String id, @RequestBody Student partialStudent) {
        try {
            // Read existing students
            ClassPathResource resource = new ClassPathResource("dummy-students.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Students.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Students students = (Students) unmarshaller.unmarshal(resource.getInputStream());

            // Find and update only non-null fields
            boolean found = false;
            for (Student student : students.getStudents()) {
                if (student.getId().equals(id)) {
                    if (partialStudent.getName() != null) {
                        student.setName(partialStudent.getName());
                    }
                    if (partialStudent.getLocation() != null) {
                        student.setLocation(partialStudent.getLocation());
                    }
                    if (partialStudent.getPhone() != null) {
                        student.setPhone(partialStudent.getPhone());
                    }
                    if (partialStudent.getCourse() != null) {
                        student.setCourse(partialStudent.getCourse());
                    }
                    found = true;
                    break;
                }
            }

            if (!found) {
                return ResponseEntity.notFound().build();
            }

            // Save updated list
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            
            Path resourceDirectory = Paths.get("src", "main", "resources", "dummy-students.xml");
            try (FileWriter writer = new FileWriter(resourceDirectory.toFile())) {
                marshaller.marshal(students, writer);
            }

            return ResponseEntity.ok()
                .body("<response><message>Student with ID " + id + " patched successfully</message></response>");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                .body("<error><message>Failed to patch student: " + e.getMessage() + "</message></error>");
        }
    }

    // Helper methods for content negotiation
    private boolean isJsonRequest() {
        String contentType = request.getHeader("Content-Type");
        return contentType != null && contentType.contains("application/json");
    }

    private String createErrorResponse(String message, boolean isJson) {
        if (isJson) {
            return String.format("{\"error\": {\"message\": \"%s\"}}", message);
        }
        return String.format("<error><message>%s</message></error>", message);
    }
}
