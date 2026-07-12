package za.ac.cput.campusfacilitybooking.service.impl;

/* StaffServiceImpl.java
   StaffServiceImpl implementation class
   Author: Milani Sani (230371574)
   Date: 12 July 2026
*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.campusfacilitybooking.domain.Staff;
import za.ac.cput.campusfacilitybooking.domain.enums.StaffRole;
import za.ac.cput.campusfacilitybooking.repository.StaffRepository;
import za.ac.cput.campusfacilitybooking.service.IStaffService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class StaffServiceImpl implements IStaffService {

    @Autowired
    private StaffRepository staffRepository;

    @Override
    public Staff create(Staff staff) {
        return staffRepository.save(staff);
    }

    @Override
    public Staff read(String staffId) {
        return staffRepository.findById(staffId).orElse(null);
    }

    @Override
    public Staff update(Staff staff) {
        if (!staffRepository.existsById(staff.getStaffId())) {
            return null;
        }
        return staffRepository.save(staff);
    }

    @Override
    public boolean delete(String staffId) {
        if (!staffRepository.existsById(staffId)) {
            return false;
        }
        staffRepository.deleteById(staffId);
        return true;
    }

    @Override
    public Set<Staff> getAll() {
        return new HashSet<>(staffRepository.findAll());
    }

    @Override
    public List<Staff> findByFirstName(String firstName) {
        return staffRepository.findByFirstName(firstName);
    }

    @Override
    public List<Staff> findByLastName(String lastName) {
        return staffRepository.findByLastName(lastName);
    }

    @Override
    public List<Staff> findByRole(StaffRole role) {
        return staffRepository.findByRole(role);
    }

    @Override
    public List<Staff> findByDepartmentId(String departmentId) {
        return staffRepository.findByDepartmentDepartmentId(departmentId);
    }

    @Override
    public Staff findByEmail(String email) {
        return staffRepository.findByEmail(email);
    }
}
