package za.ac.cput.campusfacilitybooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.campusfacilitybooking.domain.Department;
import za.ac.cput.campusfacilitybooking.service.DepartmentService;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentService service;

    @PostMapping("/create")
    public Department create(@RequestBody Department department) {
        return service.create(department);
    }

    @GetMapping("/read/{id}")
    public Department read(@PathVariable String id) {
        return service.read(id);
    }

    @PutMapping("/update")
    public Department update(@RequestBody Department department) {
        return service.update(department);
    }

    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable String id) {
        return service.delete(id);
    }
}
