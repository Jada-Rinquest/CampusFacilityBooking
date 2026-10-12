package za.ac.cput.campusfacilitybooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import za.ac.cput.campusfacilitybooking.domain.Department;
import za.ac.cput.campusfacilitybooking.service.DepartmentService;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    private final DepartmentService service;

    @Autowired
    public DepartmentController(DepartmentService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<Department> create(@RequestBody Department department) {
        return ResponseEntity.ok(service.create(department));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Department>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Department> read(@PathVariable String id) {
        Department department = service.read(id);
        if (department == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(department);
    }

    @PutMapping("/update")
    public ResponseEntity<Department> update(@RequestBody Department department) {
        return ResponseEntity.ok(service.update(department));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        return ResponseEntity.ok(service.delete(id));
    }
}