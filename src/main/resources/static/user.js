/* =========================================================
   CAMPUS FACILITY BOOKING
   USER MANAGEMENT JAVASCRIPT
========================================================= */

const API_BASE_URL = "http://localhost:8080";
const USER_API = `${API_BASE_URL}/user`;

const userForm = document.getElementById("userForm");
const userIdInput = document.getElementById("userId");
const firstNameInput = document.getElementById("firstName");
const lastNameInput = document.getElementById("lastName");
const emailInput = document.getElementById("email");
const dateOfBirthInput = document.getElementById("dateOfBirth");
const departmentIdInput = document.getElementById("departmentId");

const createButton = document.getElementById("createButton");
const readButton = document.getElementById("readButton");
const updateButton = document.getElementById("updateButton");
const deleteButton = document.getElementById("deleteButton");
const clearButton = document.getElementById("clearButton");

const userMessage = document.getElementById("userMessage");
const userResult = document.getElementById("userResult");

const mobileMenuBtn = document.getElementById("mobileMenuBtn");
const sidebar = document.querySelector(".sidebar");


document.addEventListener("DOMContentLoaded", () => {

    if (createButton) {
        createButton.addEventListener("click", createUser);
    }

    if (readButton) {
        readButton.addEventListener("click", readUser);
    }

    if (updateButton) {
        updateButton.addEventListener("click", updateUser);
    }

    if (deleteButton) {
        deleteButton.addEventListener("click", deleteUser);
    }

    if (clearButton) {
        clearButton.addEventListener("click", clearForm);
    }

    if (userForm) {
        userForm.addEventListener("submit", (event) => event.preventDefault());
    }

    if (mobileMenuBtn) {
        mobileMenuBtn.addEventListener("click", () => sidebar.classList.toggle("open"));
    }

});


function buildPayload() {

    return {
        userId: userIdInput.value.trim(),
        firstName: firstNameInput.value.trim(),
        lastName: lastNameInput.value.trim(),
        email: emailInput.value.trim(),
        dateOfBirth: dateOfBirthInput.value,
        departmentId: departmentIdInput.value.trim()
    };

}


async function createUser() {

    const payload = buildPayload();

    if (!payload.userId || !payload.firstName || !payload.lastName) {
        showMessage("User ID, first name and last name are required.", "error");
        return;
    }

    try {

        const response = await fetch(`${USER_API}/create`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            throw new Error(`Create failed: ${response.status}`);
        }

        const user = await response.json();

        showMessage("User created successfully.", "success");
        showResult(user);

    } catch (error) {
        console.error("Error creating user:", error);
        showMessage("Could not create the user.", "error");
    }

}


async function readUser() {

    const id = userIdInput.value.trim();

    if (!id) {
        showMessage("Enter a user ID to read.", "error");
        return;
    }

    try {

        const response = await fetch(`${USER_API}/read/${id}`);

        if (!response.ok) {
            throw new Error(`Read failed: ${response.status}`);
        }

        const user = await response.json();

        firstNameInput.value = user.firstName || "";
        lastNameInput.value = user.lastName || "";
        emailInput.value = user.email || "";
        dateOfBirthInput.value = user.dateOfBirth || "";
        departmentIdInput.value = user.departmentId || "";

        showMessage("User loaded.", "success");
        showResult(user);

    } catch (error) {
        console.error("Error reading user:", error);
        showMessage("User not found.", "error");
    }

}


async function updateUser() {

    const payload = buildPayload();

    if (!payload.userId) {
        showMessage("User ID is required to update.", "error");
        return;
    }

    try {

        const response = await fetch(`${USER_API}/update`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            throw new Error(`Update failed: ${response.status}`);
        }

        const user = await response.json();

        showMessage("User updated successfully.", "success");
        showResult(user);

    } catch (error) {
        console.error("Error updating user:", error);
        showMessage("Could not update the user.", "error");
    }

}


async function deleteUser() {

    const id = userIdInput.value.trim();

    if (!id) {
        showMessage("Enter a user ID to delete.", "error");
        return;
    }

    if (!confirm(`Delete user ${id}?`)) {
        return;
    }

    try {

        const response = await fetch(`${USER_API}/delete/${id}`, {
            method: "DELETE"
        });

        if (!response.ok) {
            throw new Error(`Delete failed: ${response.status}`);
        }

        showMessage("User deleted.", "success");
        clearForm();

    } catch (error) {
        console.error("Error deleting user:", error);
        showMessage("Could not delete the user.", "error");
    }

}


function clearForm() {

    if (userForm) {
        userForm.reset();
    }

    if (userResult) {
        userResult.textContent = "";
    }

    showMessage("", "");

}


function showMessage(text, type) {

    if (!userMessage) {
        return;
    }

    userMessage.textContent = text;
    userMessage.className = type;

}


function showResult(user) {

    if (!userResult) {
        return;
    }

    userResult.textContent = JSON.stringify(user, null, 2);

}
