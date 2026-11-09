package za.ac.cput.campusfacilitybooking.service.impl;

import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import za.ac.cput.campusfacilitybooking.controller.RegistrationRequest;
import za.ac.cput.campusfacilitybooking.domain.User;
import za.ac.cput.campusfacilitybooking.domain.enums.AccountType;
import za.ac.cput.campusfacilitybooking.domain.enums.Role;
import za.ac.cput.campusfacilitybooking.dto.AuthResponse;
import za.ac.cput.campusfacilitybooking.factory.LoginFactory;
import za.ac.cput.campusfacilitybooking.factory.RegisterFactory;
import za.ac.cput.campusfacilitybooking.factory.StaffFactory;
import za.ac.cput.campusfacilitybooking.factory.StudentFactory;
import za.ac.cput.campusfacilitybooking.factory.UserFactory;
import za.ac.cput.campusfacilitybooking.factory.UserRoleFactory;
import za.ac.cput.campusfacilitybooking.repository.DepartmentRepository;
import za.ac.cput.campusfacilitybooking.repository.RegisterRepository;
import za.ac.cput.campusfacilitybooking.repository.StaffRepository;
import za.ac.cput.campusfacilitybooking.repository.StudentRepository;
import za.ac.cput.campusfacilitybooking.repository.UserRepository;
import za.ac.cput.campusfacilitybooking.repository.UserRoleRepository;
import za.ac.cput.campusfacilitybooking.service.LoginService;
import za.ac.cput.campusfacilitybooking.service.RegistrationService;

/*
 * Self-service sign-up, done entirely on the server.
 *
 * Security rules:
 *  - The client never chooses IDs or the role. IDs are random and the
 *    role comes from the email domain: @mycput -> STUDENT, @cput -> LECTURER.
 *    ADMIN can never be self-assigned.
 *  - Existing usernames, emails and student numbers cannot be reused,
 *    so an existing account cannot be overwritten.
 *  - The password is hashed with BCrypt (via LoginService).
 *  - Everything is saved in one transaction: either the whole account
 *    is created or nothing is.
 */
@Service
public class RegistrationServiceImpl implements RegistrationService {

    private static final Pattern USERNAME = Pattern.compile("^[A-Za-z0-9._-]{3,30}$");
    private static final Pattern EMAIL = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private static final int MIN_PASSWORD = 8;
    private static final int MAX_PASSWORD = 64; // BCrypt only uses the first 72 bytes
    private static final int MAX_NAME = 50;

    private final UserRepository userRepository;
    private final RegisterRepository registerRepository;
    private final UserRoleRepository userRoleRepository;
    private final StudentRepository studentRepository;
    private final StaffRepository staffRepository;
    private final DepartmentRepository departmentRepository;
    private final LoginService loginService;

    public RegistrationServiceImpl(UserRepository userRepository,
                                   RegisterRepository registerRepository,
                                   UserRoleRepository userRoleRepository,
                                   StudentRepository studentRepository,
                                   StaffRepository staffRepository,
                                   DepartmentRepository departmentRepository,
                                   LoginService loginService) {
        this.userRepository = userRepository;
        this.registerRepository = registerRepository;
        this.userRoleRepository = userRoleRepository;
        this.studentRepository = studentRepository;
        this.staffRepository = staffRepository;
        this.departmentRepository = departmentRepository;
        this.loginService = loginService;
    }

    @Override
    @Transactional
    public AuthResponse register(RegistrationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Registration details are required.");
        }

        // ---------- Validate ----------
        String firstName = requireText(request.getFirstName(), "First name");
        String lastName = requireText(request.getLastName(), "Last name");
        String username = requireText(request.getUsername(), "Username");
        String email = requireText(request.getEmail(), "Email").toLowerCase(Locale.ROOT);
        String password = request.getPassword();
        String departmentId = requireText(request.getDepartmentId(), "Department");

        if (firstName.length() > MAX_NAME || lastName.length() > MAX_NAME) {
            throw new IllegalArgumentException("Names must be " + MAX_NAME + " characters or fewer.");
        }
        if (!USERNAME.matcher(username).matches()) {
            throw new IllegalArgumentException(
                    "Username must be 3-30 characters: letters, numbers, dots, dashes or underscores.");
        }
        if (!EMAIL.matcher(email).matches()) {
            throw new IllegalArgumentException("Please enter a valid email address.");
        }
        validatePassword(password);

        AccountType accountType = AccountType.fromEmail(email);
        if (accountType == null) {
            throw new IllegalArgumentException(
                    "Use your CPUT email: @mycput.ac.za for students or @cput.ac.za for staff.");
        }
        if (request.getAccountType() != null
                && !request.getAccountType().trim().equalsIgnoreCase(accountType.name())) {
            throw new IllegalArgumentException(accountType == AccountType.STAFF
                    ? "Students must use an @mycput.ac.za email address."
                    : "Staff must use an @cput.ac.za email address.");
        }

        String studentNumber = null;
        if (accountType == AccountType.STUDENT) {
            studentNumber = requireText(request.getStudentNumber(), "Student number");
            if (studentNumber.length() > 20) {
                throw new IllegalArgumentException("Student number is too long.");
            }
        }

        if (!departmentRepository.existsById(departmentId)) {
            throw new IllegalArgumentException("Please select a valid department.");
        }

        // ---------- Must not clash with an existing account ----------
        if (loginService.isUsernameTaken(username)) {
            throw new IllegalStateException("This username is already taken.");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException("An account with this email already exists.");
        }
        if (studentNumber != null && studentRepository.findByStudentNumber(studentNumber).isPresent()) {
            throw new IllegalStateException("This student number is already registered.");
        }

        // ---------- Create the account ----------
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase(Locale.ROOT);
        String userId = "USER-" + suffix;
        LocalDate dateOfBirth = request.getDateOfBirth() != null
                ? request.getDateOfBirth()
                : LocalDate.of(2000, 1, 1);

        User user = UserFactory.createUser(userId, firstName, lastName, email, dateOfBirth, departmentId);
        userRepository.save(user);

        registerRepository.save(RegisterFactory.createRegister(userId, email, LocalDate.now()));

        loginService.create(LoginFactory.createLogin("LOGIN-" + suffix, userId, username, password));

        Role role = accountType == AccountType.STAFF ? Role.LECTURER : Role.STUDENT;
        userRoleRepository.save(UserRoleFactory.createUserRole("UR-" + suffix, userId, role));

        if (accountType == AccountType.STAFF) {
            staffRepository.save(StaffFactory.createStaff("STF-" + suffix, userId));
        } else {
            studentRepository.save(StudentFactory.createStudent("STU-" + suffix, studentNumber, userId));
        }

        AuthResponse response = new AuthResponse(userId, username, email, role.name(), firstName, lastName);
        response.setAccountType(accountType.name());
        return response;
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " is required.");
        }
        return value.trim();
    }

    private static void validatePassword(String password) {
        if (password == null || password.length() < MIN_PASSWORD) {
            throw new IllegalArgumentException("Password must be at least " + MIN_PASSWORD + " characters.");
        }
        if (password.length() > MAX_PASSWORD) {
            throw new IllegalArgumentException("Password must be " + MAX_PASSWORD + " characters or fewer.");
        }
        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must contain at least one number.");
        }
        if (!password.matches(".*[A-Za-z].*")) {
            throw new IllegalArgumentException("Password must contain at least one letter.");
        }
    }
}
