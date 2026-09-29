package za.ac.cput.campusfacilitybooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import za.ac.cput.campusfacilitybooking.domain.Student;
import za.ac.cput.campusfacilitybooking.service.StudentService;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService service;

    @Autowired
    public StudentController(StudentService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<Student> create(@RequestBody Student student) {
        return ResponseEntity.ok(service.create(student));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Student>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Student> read(@PathVariable String id) {
        Student student = service.read(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PutMapping("/update")
    public ResponseEntity<Student> update(@RequestBody Student student) {
        return ResponseEntity.ok(service.update(student));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        return ResponseEntity.ok(service.delete(id));
    }
}