
/* =========================================================
   PROFILE PAGE JAVASCRIPT
   Campus Facility Booking System
========================================================= */

const API_BASE_URL = "http://localhost:8080";

let originalUser = null;
let originalValues = {};
let isSaving = false;

/* =========================================================
   PAGE INITIALIZATION
========================================================= */

document.addEventListener("DOMContentLoaded", function () {
    setupMobileMenu();
    setupLogout();
    setupChangePassword();
    setupProfileEditing();
    loadProfile();
});

/* =========================================================
   LOAD PROFILE
========================================================= */

async function loadProfile() {
    const current = window.currentUser;

    if (!current || !current.userId) {
        setDefaultProfile();
        return;
    }

    // Display basic user details while loading.
    showProfile({
        userId: current.userId,
        firstName: current.firstName,
        lastName: current.lastName,
        email: current.email
    }, current);

    try {
        const response = await fetch(
            `${API_BASE_URL}/user/read/${encodeURIComponent(current.userId)}`
        );

        if (!response.ok) {
            throw new Error("Unable to retrieve user information.");
        }

        const user = await response.json();

        originalUser = user;

        showProfile(user, current);
        rememberOriginalValues();

        // Load department name.
        if (user.departmentId) {
            try {
                const departmentResponse = await fetch(
                    `${API_BASE_URL}/department/read/${encodeURIComponent(user.departmentId)}`
                );

                const departmentName = departmentResponse.ok
                    ? (await departmentResponse.json()).name
                    : user.departmentId;

                setText("profileDepartment", departmentName);
                setValue("department", departmentName);
            } catch (error) {
                console.error("Error loading department:", error);
                setValue("department", user.departmentId);
            }
        }

        // Load student number or staff ID.
        const recordType = current.isStudent ? "student" : "staff";

        try {
            const recordResponse = await fetch(
                `${API_BASE_URL}/${recordType}/user/${encodeURIComponent(current.userId)}`
            );

            if (recordResponse.ok) {
                const record = await recordResponse.json();

                if (current.isStudent) {
                    setValue(
                        "studentNumber",
                        record.studentNumber || "N/A"
                    );
                } else {
                    setValue(
                        "staffId",
                        record.staffId || "N/A"
                    );
                }
            }
        } catch (error) {
            console.error("Error loading role details:", error);
        }

    } catch (error) {
        console.error("Error loading profile:", error);

        showStatus(
            "Unable to load profile information.",
            "error"
        );
    }
}

/* =========================================================
   SHOW PROFILE
========================================================= */

function showProfile(user, current) {
    const fullName =
        `${user.firstName || ""} ${user.lastName || ""}`.trim()
        || current.username
        || "User";

    setText("topbarName", fullName);
    setText("topbarRole", current.roleLabel || "User");

    setText("profileName", fullName);
    setText("profileRole", current.roleLabel || "User");
    setText("profileEmail", user.email || "N/A");
    setText("profileUserId", user.userId || "N/A");

    setValue("firstName", user.firstName || "");
    setValue("lastName", user.lastName || "");
    setValue("email", user.email || "");
    setValue("dateOfBirth", user.dateOfBirth || "");
    setValue("userId", user.userId || "");

    setValue("accountRole", current.roleLabel || "User");
    setText(
        "accountType",
        current.isStudent ? "Student" : "Staff"
    );
    setText("accountUserId", user.userId || "N/A");

    const studentGroup =
        document.getElementById("studentNumberGroup");

    const staffGroup =
        document.getElementById("staffIdGroup");

    if (studentGroup) {
        studentGroup.style.display =
            current.isStudent ? "" : "none";
    }

    if (staffGroup) {
        staffGroup.style.display =
            current.isStudent ? "none" : "";
    }

    setAvatarInitials(fullName);
}

/* =========================================================
   PROFILE EDITING
========================================================= */

function setupProfileEditing() {
    const editButton =
        document.getElementById("editProfileBtn");

    const cancelButton =
        document.getElementById("cancelProfileBtn");

    const saveButton =
        document.getElementById("saveProfileBtn");

    if (!editButton || !cancelButton || !saveButton) {
        console.warn(
            "Profile editing buttons were not found in profile.html."
        );
        return;
    }

    editButton.addEventListener("click", function () {
        if (!originalUser) {
            showStatus(
                "Please wait for your profile to finish loading.",
                "error"
            );
            return;
        }

        rememberOriginalValues();
        setEditingMode(true);
    });

    cancelButton.addEventListener("click", function () {
        restoreOriginalValues();
        setEditingMode(false);
    });

    saveButton.addEventListener("click", saveProfile);
}

function getEditableFields() {
    return ["firstName", "lastName", "dateOfBirth"];
}

function rememberOriginalValues() {
    getEditableFields().forEach(function (id) {
        const input = document.getElementById(id);

        if (input) {
            originalValues[id] = input.value;
        }
    });
}

function restoreOriginalValues() {
    getEditableFields().forEach(function (id) {
        const input = document.getElementById(id);

        if (input) {
            input.value = originalValues[id] || "";
        }
    });
}

function setEditingMode(editing) {
    const editButton =
        document.getElementById("editProfileBtn");

    const cancelButton =
        document.getElementById("cancelProfileBtn");

    const saveButton =
        document.getElementById("saveProfileBtn");

    if (editButton) {
        editButton.hidden = editing;
    }

    if (cancelButton) {
        cancelButton.hidden = !editing;
    }

    if (saveButton) {
        saveButton.hidden = !editing;
    }

    getEditableFields().forEach(function (id) {
        const input = document.getElementById(id);

        if (input) {
            input.readOnly = !editing;
        }
    });

    // These fields should not be changed here.
    ["email", "department", "userId"].forEach(function (id) {
        const input = document.getElementById(id);

        if (input) {
            input.readOnly = true;
        }
    });
}

/* =========================================================
   SAVE PROFILE
========================================================= */

async function saveProfile() {
    if (isSaving || !originalUser) {
        return;
    }

    const firstName =
        document.getElementById("firstName")?.value.trim();

    const lastName =
        document.getElementById("lastName")?.value.trim();

    const dateOfBirth =
        document.getElementById("dateOfBirth")?.value || null;

    if (!firstName || !lastName) {
        showStatus(
            "First name and last name are required.",
            "error"
        );
        return;
    }

    const current = window.currentUser;

    if (!current || current.userId !== originalUser.userId) {
        showStatus(
            "Your login session could not be verified.",
            "error"
        );
        return;
    }

    // Preserve existing user properties.
    // Only change the three editable profile fields.
    const updatedUser = {
        ...originalUser,
        firstName: firstName,
        lastName: lastName,
        dateOfBirth: dateOfBirth
    };

    const saveButton =
        document.getElementById("saveProfileBtn");

    isSaving = true;

    if (saveButton) {
        saveButton.disabled = true;
        saveButton.textContent = "Saving...";
    }

    try {
        const response = await fetch(
            `${API_BASE_URL}/user/update`,
            {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(updatedUser)
            }
        );

        if (!response.ok) {
            const errorText = await response.text();

            throw new Error(
                errorText || "Failed to update profile."
            );
        }

        const savedUser = await response.json();

        originalUser = savedUser;

        showProfile(savedUser, current);
        rememberOriginalValues();
        setEditingMode(false);

        // Refresh locally displayed user information.
        current.firstName = savedUser.firstName;
        current.lastName = savedUser.lastName;
        current.email = savedUser.email;

        showStatus(
            "Profile updated successfully!",
            "success"
        );

    } catch (error) {
        console.error("Error saving profile:", error);

        showStatus(
            "Unable to save profile changes. Check the backend.",
            "error"
        );

    } finally {
        isSaving = false;

        if (saveButton) {
            saveButton.disabled = false;
            saveButton.innerHTML =
                '<i class="fa-solid fa-check"></i> Save Changes';
        }
    }
}

/* =========================================================
   HELPER FUNCTIONS
========================================================= */

function setText(id, value) {
    const element = document.getElementById(id);

    if (element) {
        element.textContent = value ?? "";
    }
}

function setValue(id, value) {
    const element = document.getElementById(id);

    if (element) {
        element.value = value ?? "";
    }
}

/* =========================================================
   DEFAULT PROFILE
========================================================= */

function setDefaultProfile() {
    setText("topbarName", "User");
    setText("topbarRole", "User");

    setText("profileName", "User Name");
    setText("profileRole", "User");

    setValue("accountRole", "User");
    setText("accountType", "User");

    setText("profileUserId", "N/A");
    setValue("userId", "N/A");
    setText("accountUserId", "N/A");

    setAvatarInitials("User");
}

/* =========================================================
   AVATAR INITIALS
========================================================= */

function setAvatarInitials(name) {
    const initialsElement =
        document.getElementById("avatarInitials");

    const topbarAvatar =
        document.getElementById("topbarAvatar");

    const parts = (name || "User").trim().split(/\s+/);

    let initials;

    if (parts.length >= 2) {
        initials =
            parts[0].charAt(0) +
            parts[parts.length - 1].charAt(0);
    } else {
        initials = parts[0].substring(0, 2);
    }

    initials = initials.toUpperCase();

    if (initialsElement) {
        initialsElement.textContent = initials;
    }

    if (topbarAvatar) {
        const span = topbarAvatar.querySelector("span");

        if (span) {
            span.textContent = initials;
        }
    }
}

/* =========================================================
   MOBILE MENU
========================================================= */

function setupMobileMenu() {
    const menuButton =
        document.getElementById("mobileMenuBtn");

    const sidebar =
        document.querySelector(".sidebar");

    if (!menuButton || !sidebar) {
        return;
    }

    menuButton.addEventListener("click", function () {
        sidebar.classList.toggle("open");
    });
}

/* =========================================================
   LOGOUT
========================================================= */

function setupLogout() {
    const logoutButton =
        document.getElementById("logoutButton");

    if (!logoutButton) {
        return;
    }

    logoutButton.addEventListener("click", function () {
        window.location.href = "login.html";
    });
}

/* =========================================================
   CHANGE PASSWORD
========================================================= */

function setupChangePassword() {
    const changePasswordButton =
        document.getElementById("changePasswordButton");

    if (!changePasswordButton) {
        return;
    }

    changePasswordButton.addEventListener(
        "click",
        function () {
            showStatus(
                "Password changing will be connected to the login system.",
                "info"
            );
        }
    );
}

/* =========================================================
   STATUS MESSAGE
========================================================= */

function showStatus(message, type = "info") {
    const statusMessage =
        document.getElementById("statusMessage");

    const statusText =
        document.getElementById("statusText");

    if (!statusMessage || !statusText) {
        return;
    }

    statusText.textContent = message;

    statusMessage.classList.remove(
        "hidden",
        "success",
        "error",
        "info"
    );

    statusMessage.classList.add(type);

    setTimeout(function () {
        statusMessage.classList.add("hidden");
    }, 4000);
}
