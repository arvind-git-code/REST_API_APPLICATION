package com.arvind.restapitest.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@XmlRootElement(name = "student")
public class Student {
    private String id;
    private String name;
    private String location;
    private String phone;
    private List<String> course;

    @XmlElement
    @JsonProperty
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlElement
    @JsonProperty
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    @JsonProperty
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @XmlElement
    @JsonProperty
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @XmlElement(name = "course")
    @JsonProperty("course")
    public List<String> getCourse() {
        return course;
    }

    public void setCourse(List<String> course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "Student{id='" + id + '\'' +
               ", name='" + name + '\'' +
               ", location='" + location + '\'' +
               ", phone='" + phone + '\'' +
               ", course=" + course + '}';
    }
}
