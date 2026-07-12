package za.ac.cput.campusfacilitybooking.service.impl;

import org.springframework.stereotype.Service;
import za.ac.cput.campusfacilitybooking.domain.Student;
import za.ac.cput.campusfacilitybooking.repository.StudentRepository;
import za.ac.cput.campusfacilitybooking.service.StudentService;

import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repository;

    public StudentServiceImpl(StudentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Student create(Student student) {
        return repository.save(student);
    }

    @Override
    public Student read(String id) {
        Optional<Student> student = repository.findById(id);
        return student.orElse(null);
    }

    @Override
    public Student update(Student student) {
        return repository.save(student);
    }

    @Override
    public boolean delete(String id) {

        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }

        return false;
    }
}