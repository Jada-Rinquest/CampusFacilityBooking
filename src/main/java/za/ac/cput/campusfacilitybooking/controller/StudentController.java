package za.ac.cput.campusfacilitybooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.campusfacilitybooking.domain.Student;
import za.ac.cput.campusfacilitybooking.service.StudentService;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService service;

    @PostMapping("/create")
    public Student create(@RequestBody Student student) {
        return service.create(student);
    }

    @GetMapping("/read/{id}")
    public Student read(@PathVariable String id) {
        return service.read(id);
    }

    @PutMapping("/update")
    public Student update(@RequestBody Student student) {
        return service.update(student);
    }

    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable String id) {
        return service.delete(id);
    }
}