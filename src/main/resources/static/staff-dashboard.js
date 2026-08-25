const API_URL = window.location.origin;


/* =========================
   SCREEN NAVIGATION
========================= */

function showSection(sectionId) {

    document.getElementById(sectionId).style.display = "block";

    window.scrollTo({
        top: document.getElementById(sectionId).offsetTop - 20,
        behavior: "smooth"
    });
}


function hideSection(sectionId) {

    document.getElementById(sectionId).style.display = "none";
}


/* =========================
   DEPARTMENT
========================= */

async function createDepartment() {

    const department = {
        departmentId: document.getElementById("departmentId").value,
        name: document.getElementById("departmentName").value,
        building: document.getElementById("building").value,
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

    if (!confirm("Are you sure you want to delete this department?")) {
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

            document.getElementById(
                "departmentResult"
            ).innerHTML = "No department selected.";

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


/* =========================
   STUDENT
========================= */

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

    if (!confirm("Are you sure you want to delete this student?")) {
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

            document.getElementById(
                "studentResult"
            ).innerHTML = "No student selected.";

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


/* =========================
   MESSAGES
========================= */

function showMessage(
    elementId,
    message,
    type
) {

    const element =
        document.getElementById(elementId);

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