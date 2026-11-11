
/* =========================================================
   STAFF DASHBOARD JAVASCRIPT
   Campus Facility Booking System
========================================================= */

const API_URL = (
    window.location.hostname === "localhost" ||
    window.location.hostname === "127.0.0.1"
)
    ? (window.location.port === "8080"
        ? ""
        : "http://localhost:8080")
    : window.location.origin;


/* =========================================================
   DASHBOARD INITIALIZATION
========================================================= */

document.addEventListener("DOMContentLoaded", () => {

    loadSignedInStaff();
    loadOverview();

    const mobileMenuBtn =
        document.getElementById("mobileMenuBtn");

    const sidebar =
        document.querySelector(".sidebar");

    if (mobileMenuBtn && sidebar) {

        mobileMenuBtn.addEventListener("click", () => {
            sidebar.classList.toggle("open");
        });

    }

});


/* =========================================================
   LOGGED-IN USER
========================================================= */

function getSignedInUser() {

    if (window.currentUser) {
        return window.currentUser;
    }

    try {

        return JSON.parse(
            sessionStorage.getItem("loggedInUser") ||
            localStorage.getItem("loggedInUser") ||
            "null"
        );

    } catch (error) {

        console.error("Unable to read logged-in user:", error);
        return null;

    }

}


/* =========================================================
   LOAD STAFF INFORMATION
========================================================= */

function loadSignedInStaff() {

    const user = getSignedInUser();

    if (!user) {
        return;
    }

    const name =
        `${user.firstName || ""} ${user.lastName || ""}`.trim()
        || user.username
        || "Staff Member";

    const parts = name.split(/\s+/);

    const initials = parts.length > 1
        ? parts[0][0] + parts[parts.length - 1][0]
        : name.substring(0, 2);

    setText("topbarName", name);
    setText("topbarInitials", initials.toUpperCase());

    setText(
        "topbarRole",
        user.role === "ADMIN" ? "Administrator" : "Staff"
    );

    setText(
        "welcomeText",
        `Welcome back, ${user.firstName || name}.`
    );

    setText(
        "staffWelcomeName",
        user.firstName || name
    );

}


/* =========================================================
   DASHBOARD OVERVIEW
========================================================= */

async function loadOverview() {

    const [
        bookings,
        facilities,
        equipment,
        users
    ] = await Promise.all([

        fetchList("booking"),
        fetchList("facility"),
        fetchList("equipment"),
        fetchList("user")

    ]);

    setText("totalBookings", bookings.length);
    setText("totalFacilities", facilities.length);
    setText("totalEquipment", equipment.length);
    setText("totalUsers", users.length);

    /*
       IMPORTANT:

       The notification bell is managed by
       notification-badge.js.

       Do not calculate or overwrite the
       notification count in this file.

       This ensures that all staff and student
       pages show the same unread count.
    */

}


/* =========================================================
   FETCH DATA FROM BACKEND
========================================================= */

async function fetchList(resource) {

    try {

        const response = await fetch(
            `${API_URL}/${resource}/all`,
            {
                cache: "no-store"
            }
        );

        if (!response.ok) {

            throw new Error(
                `Request failed: ${response.status}`
            );

        }

        const data = await response.json();

        return Array.isArray(data) ? data : [];

    } catch (error) {

        console.error(
            `Could not load ${resource}:`,
            error
        );

        return [];

    }

}


/* =========================================================
   UPDATE ELEMENT TEXT
========================================================= */

function setText(elementId, value) {

    const element =
        document.getElementById(elementId);

    if (element) {
        element.textContent = value;
    }

}


/* =========================================================
   SCREEN NAVIGATION
========================================================= */

function showSection(sectionId) {

    const section =
        document.getElementById(sectionId);

    if (!section) {
        return;
    }

    section.style.display = "block";

    window.scrollTo({
        top: section.offsetTop - 20,
        behavior: "smooth"
    });

}


function hideSection(sectionId) {

    const section =
        document.getElementById(sectionId);

    if (section) {
        section.style.display = "none";
    }

}


/* =========================================================
   DEPARTMENT MANAGEMENT
========================================================= */

/* CREATE DEPARTMENT */

async function createDepartment() {

    const department = {

        departmentId:
        document.getElementById("departmentId").value,

        name:
        document.getElementById("departmentName").value,

        building:
        document.getElementById("building").value,

        headOfDepartment:
        document.getElementById("headOfDepartment").value

    };

    try {

        const response = await fetch(
            `${API_URL}/department/create`,
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(department)
            }
        );

        if (!response.ok) {
            throw new Error("Failed to create department.");
        }

        const data = await response.json();

        displayDepartment(data);

        showMessage(
            "departmentMessage",
            "Department created successfully.",
            "success"
        );

    } catch (error) {

        showMessage(
            "departmentMessage",
            error.message,
            "error"
        );

    }

}


/* READ DEPARTMENT */

async function readDepartment() {

    const id =
        document.getElementById("departmentId").value;

    if (!id) {

        showMessage(
            "departmentMessage",
            "Please enter a Department ID.",
            "error"
        );

        return;

    }

    try {

        const response = await fetch(
            `${API_URL}/department/read/${id}`
        );

        if (!response.ok) {
            throw new Error("Department not found.");
        }

        const data = await response.json();

        displayDepartment(data);

        showMessage(
            "departmentMessage",
            "Department found.",
            "success"
        );

    } catch (error) {

        showMessage(
            "departmentMessage",
            error.message,
            "error"
        );

    }

}


/* UPDATE DEPARTMENT */

async function updateDepartment() {

    const department = {

        departmentId:
        document.getElementById("departmentId").value,

        name:
        document.getElementById("departmentName").value,

        building:
        document.getElementById("building").value,

        headOfDepartment:
        document.getElementById("headOfDepartment").value

    };

    try {

        const response = await fetch(
            `${API_URL}/department/update`,
            {
                method: "PUT",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(department)
            }
        );

        if (!response.ok) {
            throw new Error("Failed to update department.");
        }

        const data = await response.json();

        displayDepartment(data);

        showMessage(
            "departmentMessage",
            "Department updated successfully.",
            "success"
        );

    } catch (error) {

        showMessage(
            "departmentMessage",
            error.message,
            "error"
        );

    }

}


/* DELETE DEPARTMENT */

async function deleteDepartment() {

    const id =
        document.getElementById("departmentId").value;

    if (!id) {

        showMessage(
            "departmentMessage",
            "Enter a Department ID first.",
            "error"
        );

        return;

    }

    if (!confirm(
        "Are you sure you want to delete this department?"
    )) {
        return;
    }

    try {

        const response = await fetch(
            `${API_URL}/department/delete/${id}`,
            {
                method: "DELETE"
            }
        );

        if (!response.ok) {
            throw new Error("Failed to delete department.");
        }

        const deleted = await response.json();

        if (deleted === true) {

            showMessage(
                "departmentMessage",
                "Department deleted successfully.",
                "success"
            );

            const result =
                document.getElementById("departmentResult");

            if (result) {
                result.innerHTML = "No department selected.";
            }

        } else {

            showMessage(
                "departmentMessage",
                "Department was not found.",
                "error"
            );

        }

    } catch (error) {

        showMessage(
            "departmentMessage",
            error.message,
            "error"
        );

    }

}


/* DISPLAY DEPARTMENT */

function displayDepartment(department) {

    document.getElementById(
        "departmentResult"
    ).innerHTML = `

        <p>
            <strong>Department ID:</strong>
            ${department.departmentId}
        </p>

        <p>
            <strong>Name:</strong>
            ${department.name}
        </p>

        <p>
            <strong>Building:</strong>
            ${department.building}
        </p>

        <p>
            <strong>Head of Department:</strong>
            ${department.headOfDepartment}
        </p>

    `;

    document.getElementById(
        "departmentId"
    ).value = department.departmentId;

    document.getElementById(
        "departmentName"
    ).value = department.name;

    document.getElementById(
        "building"
    ).value = department.building;

    document.getElementById(
        "headOfDepartment"
    ).value = department.headOfDepartment;

}


/* =========================================================
   STUDENT MANAGEMENT
========================================================= */

/* CREATE STUDENT */

async function createStudent() {

    const departmentId =
        document.getElementById("studentDepartment").value;

    const student = {

        studentId:
        document.getElementById("studentId").value,

        firstName:
        document.getElementById("firstName").value,

        lastName:
        document.getElementById("lastName").value,

        email:
        document.getElementById("email").value,

        studentNumber:
        document.getElementById("studentNumber").value,

        department: {
            departmentId: departmentId
        }

    };

    try {

        const response = await fetch(
            `${API_URL}/student/create`,
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(student)
            }
        );

        if (!response.ok) {
            throw new Error("Failed to create student.");
        }

        const data = await response.json();

        displayStudent(data);

        showMessage(
            "studentMessage",
            "Student created successfully.",
            "success"
        );

    } catch (error) {

        showMessage(
            "studentMessage",
            error.message,
            "error"
        );

    }

}


/* READ STUDENT */

async function readStudent() {

    const id =
        document.getElementById("studentId").value;

    if (!id) {

        showMessage(
            "studentMessage",
            "Please enter a Student ID.",
            "error"
        );

        return;

    }

    try {

        const response = await fetch(
            `${API_URL}/student/read/${id}`
        );

        if (!response.ok) {
            throw new Error("Student not found.");
        }

        const data = await response.json();

        displayStudent(data);

        showMessage(
            "studentMessage",
            "Student found.",
            "success"
        );

    } catch (error) {

        showMessage(
            "studentMessage",
            error.message,
            "error"
        );

    }

}


/* UPDATE STUDENT */

async function updateStudent() {

    const departmentId =
        document.getElementById("studentDepartment").value;

    const student = {

        studentId:
        document.getElementById("studentId").value,

        firstName:
        document.getElementById("firstName").value,

        lastName:
        document.getElementById("lastName").value,

        email:
        document.getElementById("email").value,

        studentNumber:
        document.getElementById("studentNumber").value,

        department: {
            departmentId: departmentId
        }

    };

    try {

        const response = await fetch(
            `${API_URL}/student/update`,
            {
                method: "PUT",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(student)
            }
        );

        if (!response.ok) {
            throw new Error("Failed to update student.");
        }

        const data = await response.json();

        displayStudent(data);

        showMessage(
            "studentMessage",
            "Student updated successfully.",
            "success"
        );

    } catch (error) {

        showMessage(
            "studentMessage",
            error.message,
            "error"
        );

    }

}


/* DELETE STUDENT */

async function deleteStudent() {

    const id =
        document.getElementById("studentId").value;

    if (!id) {

        showMessage(
            "studentMessage",
            "Enter a Student ID first.",
            "error"
        );

        return;

    }

    if (!confirm(
        "Are you sure you want to delete this student?"
    )) {
        return;
    }

    try {

        const response = await fetch(
            `${API_URL}/student/delete/${id}`,
            {
                method: "DELETE"
            }
        );

        if (!response.ok) {
            throw new Error("Failed to delete student.");
        }

        const deleted = await response.json();

        if (deleted === true) {

            showMessage(
                "studentMessage",
                "Student deleted successfully.",
                "success"
            );

            const result =
                document.getElementById("studentResult");

            if (result) {
                result.innerHTML = "No student selected.";
            }

        } else {

            showMessage(
                "studentMessage",
                "Student was not found.",
                "error"
            );

        }

    } catch (error) {

        showMessage(
            "studentMessage",
            error.message,
            "error"
        );

    }

}


/* DISPLAY STUDENT */

function displayStudent(student) {

    let departmentText = "Not assigned";

    if (student.department) {

        departmentText =
            student.department.departmentId;

    }

    document.getElementById(
        "studentResult"
    ).innerHTML = `

        <p>
            <strong>Student ID:</strong>
            ${student.studentId}
        </p>

        <p>
            <strong>Student Number:</strong>
            ${student.studentNumber}
        </p>

        <p>
            <strong>Name:</strong>
            ${student.firstName}
            ${student.lastName}
        </p>

        <p>
            <strong>Email:</strong>
            ${student.email}
        </p>

        <p>
            <strong>Department:</strong>
            ${departmentText}
        </p>

    `;

    document.getElementById(
        "studentId"
    ).value = student.studentId;

    document.getElementById(
        "studentNumber"
    ).value = student.studentNumber;

    document.getElementById(
        "firstName"
    ).value = student.firstName;

    document.getElementById(
        "lastName"
    ).value = student.lastName;

    document.getElementById(
        "email"
    ).value = student.email;

    if (student.department) {

        document.getElementById(
            "studentDepartment"
        ).value =
            student.department.departmentId;

    }

}


/* =========================================================
   DISPLAY SUCCESS AND ERROR MESSAGES
========================================================= */

function showMessage(elementId, message, type) {

    const element =
        document.getElementById(elementId);

    if (!element) {
        return;
    }

    element.style.display = "block";
    element.textContent = message;

    if (type === "success") {

        element.style.backgroundColor = "#DCFCE7";
        element.style.color = "#166534";

    } else {

        element.style.backgroundColor = "#FEE2E2";
        element.style.color = "#991B1B";

    }

}
