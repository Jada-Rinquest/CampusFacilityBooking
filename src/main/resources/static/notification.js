/* =========================================================
   NOTIFICATION MANAGEMENT
========================================================= */

const API_BASE_URL = "http://localhost:8080/notification";

let editingNotificationId = null;


/* =========================================================
   DOM ELEMENTS
========================================================= */

const notificationForm =
    document.getElementById("notificationForm");

const notificationIdInput =
    document.getElementById("notificationId");

const userIdInput =
    document.getElementById("userId");

const notificationTypeIdInput =
    document.getElementById("notificationTypeId");

const sentDateInput =
    document.getElementById("sentDate");

const messageInput =
    document.getElementById("message");

const submitButton =
    document.getElementById("submitButton");

const clearButton =
    document.getElementById("clearButton");

const cancelEditButton =
    document.getElementById("cancelEditButton");

const formTitle =
    document.getElementById("formTitle");


/* Search */

const searchNotificationId =
    document.getElementById("searchNotificationId");

const searchButton =
    document.getElementById("searchButton");

    const editNotificationButton =
        document.getElementById("editNotificationButton");

    const deleteNotificationButton =
        document.getElementById("deleteNotificationButton");


/* Details */

const notificationDetails =
    document.getElementById("notificationDetails");

const detailNotificationId =
    document.getElementById("detailNotificationId");

const detailUserId =
    document.getElementById("detailUserId");

const detailNotificationTypeId =
    document.getElementById("detailNotificationTypeId");

const detailSentDate =
    document.getElementById("detailSentDate");

const detailMessage =
    document.getElementById("detailMessage");


/* Status */

const statusMessage =
    document.getElementById("statusMessage");

const statusText =
    document.getElementById("statusText");


/* =========================================================
   PAGE LOAD
========================================================= */

document.addEventListener("DOMContentLoaded", () => {

    setDefaultDate();

});


/* =========================================================
   DEFAULT DATE
========================================================= */

function setDefaultDate() {

    if (!sentDateInput.value) {

        const today = new Date();

        const year = today.getFullYear();

        const month = String(
            today.getMonth() + 1
        ).padStart(2, "0");

        const day = String(
            today.getDate()
        ).padStart(2, "0");

        sentDateInput.value =
            `${year}-${month}-${day}`;
    }

}


/* =========================================================
   CREATE / UPDATE FORM
========================================================= */

notificationForm.addEventListener(
    "submit",
    async function (event) {

        event.preventDefault();


        const notification = {

            notificationId:
                notificationIdInput.value.trim(),

            userId:
                userIdInput.value.trim(),

            message:
                messageInput.value.trim(),

            sentDate:
                sentDateInput.value,

            notificationTypeId:
                notificationTypeIdInput.value.trim()

        };


        if (!validateNotification(notification)) {
            return;
        }


        if (editingNotificationId) {

            await updateNotification(notification);

        } else {

            await createNotification(notification);

        }

    }
);


/* =========================================================
   VALIDATE NOTIFICATION
========================================================= */

function validateNotification(notification) {

    if (!notification.notificationId) {

        showStatus(
            "Notification ID is required.",
            "error"
        );

        return false;
    }


    if (!notification.userId) {

        showStatus(
            "User ID is required.",
            "error"
        );

        return false;
    }


    if (!notification.notificationTypeId) {

        showStatus(
            "Notification Type ID is required.",
            "error"
        );

        return false;
    }


    if (!notification.sentDate) {

        showStatus(
            "Sent date is required.",
            "error"
        );

        return false;
    }


    if (!notification.message) {

        showStatus(
            "Notification message is required.",
            "error"
        );

        return false;
    }


    return true;

}


/* =========================================================
   CREATE NOTIFICATION
========================================================= */

async function createNotification(notification) {

    try {

        setLoading(true);


        const response = await fetch(
            `${API_BASE_URL}/create`,
            {

                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(notification)

            }
        );


        if (!response.ok) {

            const errorMessage =
                await getErrorMessage(response);

            throw new Error(errorMessage);

        }


        const createdNotification =
            await response.json();


        showStatus(
            "Notification created successfully.",
            "success"
        );


        displayNotificationDetails(
            createdNotification
        );


        clearForm();


    } catch (error) {

        console.error(
            "Error creating notification:",
            error
        );


        showStatus(
            "Unable to create notification: " +
            error.message,
            "error"
        );


    } finally {

        setLoading(false);

    }

}


/* =========================================================
   FIND NOTIFICATION
========================================================= */

searchButton.addEventListener(
    "click",
    async () => {

        const id =
            searchNotificationId.value.trim();


        if (!id) {

            showStatus(
                "Please enter a Notification ID.",
                "error"
            );

            return;
        }


        await findNotification(id);

    }
);


/* =========================================================
   FIND NOTIFICATION BY ID
========================================================= */

async function findNotification(id) {

    try {

        const response = await fetch(
            `${API_BASE_URL}/read/${encodeURIComponent(id)}`
        );


        if (!response.ok) {

            if (response.status === 404) {

                throw new Error(
                    `Notification with ID ${id} was not found.`
                );

            }


            const errorMessage =
                await getErrorMessage(response);

            throw new Error(errorMessage);

        }


        const notification =
            await response.json();


        displayNotificationDetails(
            notification
        );


        showStatus(
            "Notification found successfully.",
            "success"
        );


    } catch (error) {

        console.error(
            "Error finding notification:",
            error
        );


        notificationDetails.classList.add(
            "hidden"
        );


        showStatus(
            error.message,
            "error"
        );

    }

}


/* =========================================================
   DISPLAY NOTIFICATION DETAILS
========================================================= */

function displayNotificationDetails(notification) {

    detailNotificationId.textContent =
        notification.notificationId ?? "-";


    detailUserId.textContent =
        notification.userId ?? "-";


    detailNotificationTypeId.textContent =
        notification.notificationTypeId ?? "-";


    detailSentDate.textContent =
        notification.sentDate ?? "-";


    detailMessage.textContent =
        notification.message ?? "-";


    notificationDetails.classList.remove(
        "hidden"
    );

}


/* =========================================================
   EDIT NOTIFICATION
========================================================= */

async function editNotification(id) {

    try {

        const response = await fetch(
            `${API_BASE_URL}/read/${encodeURIComponent(id)}`
        );


        if (!response.ok) {

            const errorMessage =
                await getErrorMessage(response);

            throw new Error(errorMessage);

        }


        const notification =
            await response.json();


        notificationIdInput.value =
            notification.notificationId ?? "";


        userIdInput.value =
            notification.userId ?? "";


        notificationTypeIdInput.value =
            notification.notificationTypeId ?? "";


        sentDateInput.value =
            notification.sentDate ?? "";


        messageInput.value =
            notification.message ?? "";


        editingNotificationId =
            notification.notificationId;


        formTitle.textContent =
            "Update Notification";


        submitButton.textContent =
            "Update Notification";


        cancelEditButton.classList.remove(
            "hidden"
        );


        notificationForm.scrollIntoView({
            behavior: "smooth",
            block: "start"
        });


        showStatus(
            "Editing notification " +
            notification.notificationId,
            "warning"
        );


    } catch (error) {

        console.error(
            "Error loading notification:",
            error
        );


        showStatus(
            "Unable to load notification: " +
            error.message,
            "error"
        );

    }

}


/* =========================================================
   UPDATE NOTIFICATION
========================================================= */

async function updateNotification(notification) {

    try {

        setLoading(true);


        const response = await fetch(
            `${API_BASE_URL}/update`,
            {

                method: "PUT",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(notification)

            }
        );


        if (!response.ok) {

            const errorMessage =
                await getErrorMessage(response);

            throw new Error(errorMessage);

        }


        const updatedNotification =
            await response.json();


        showStatus(
            "Notification updated successfully.",
            "success"
        );


        displayNotificationDetails(
            updatedNotification
        );


        clearForm();


    } catch (error) {

        console.error(
            "Error updating notification:",
            error
        );


        showStatus(
            "Unable to update notification: " +
            error.message,
            "error"
        );


    } finally {

        setLoading(false);

    }

}

/* =========================================================
   DETAIL ACTION BUTTONS
========================================================= */

editNotificationButton.addEventListener(
    "click",
    async () => {

        const id =
            detailNotificationId.textContent.trim();


        if (!id || id === "-") {

            showStatus(
                "No notification selected.",
                "error"
            );

            return;
        }


        await editNotification(id);

    }
);


deleteNotificationButton.addEventListener(
    "click",
    async () => {

        const id =
            detailNotificationId.textContent.trim();


        if (!id || id === "-") {

            showStatus(
                "No notification selected.",
                "error"
            );

            return;
        }


        await deleteNotification(id);

    }
);


/* =========================================================
   DELETE NOTIFICATION
========================================================= */

async function deleteNotification(id) {

    const confirmed = confirm(
        `Are you sure you want to delete notification "${id}"?`
    );


    if (!confirmed) {
        return;
    }


    try {

        const response = await fetch(
            `${API_BASE_URL}/delete/${encodeURIComponent(id)}`,
            {

                method: "DELETE"

            }
        );


        if (!response.ok) {

            const errorMessage =
                await getErrorMessage(response);

            throw new Error(errorMessage);

        }


        const deleted =
            await response.json();


        if (deleted === true) {

            showStatus(
                "Notification deleted successfully.",
                "success"
            );

        } else {

            showStatus(
                "Notification could not be deleted.",
                "error"
            );

            return;

        }


        notificationDetails.classList.add(
            "hidden"
        );


        if (editingNotificationId === id) {

            clearForm();

        }


    } catch (error) {

        console.error(
            "Error deleting notification:",
            error
        );


        showStatus(
            "Unable to delete notification: " +
            error.message,
            "error"
        );

    }

}


/* =========================================================
   CLEAR FORM
========================================================= */

clearButton.addEventListener(
    "click",
    () => {

        clearForm();

    }
);


function clearForm() {

    notificationForm.reset();


    editingNotificationId = null;


    formTitle.textContent =
        "Create a Notification";


    submitButton.textContent =
        "Create Notification";


    cancelEditButton.classList.add(
        "hidden"
    );


    setDefaultDate();

}


/* =========================================================
   CANCEL EDIT
========================================================= */

cancelEditButton.addEventListener(
    "click",
    () => {

        clearForm();


        showStatus(
            "Edit cancelled.",
            "warning"
        );

    }
);


/* =========================================================
   STATUS MESSAGE
========================================================= */

function showStatus(message, type) {

    statusText.textContent =
        message;


    statusMessage.className =
        `status-message ${type}`;


    statusMessage.classList.remove(
        "hidden"
    );


    setTimeout(() => {

        statusMessage.classList.add(
            "hidden"
        );

    }, 4000);

}


/* =========================================================
   LOADING STATE
========================================================= */

function setLoading(isLoading) {

    if (isLoading) {

        submitButton.disabled = true;


        submitButton.textContent =
            editingNotificationId
                ? "Updating..."
                : "Creating...";

    } else {

        submitButton.disabled = false;


        submitButton.textContent =
            editingNotificationId
                ? "Update Notification"
                : "Create Notification";

    }

}


/* =========================================================
   ERROR HANDLING
========================================================= */

async function getErrorMessage(response) {

    try {

        const data =
            await response.json();


        if (data.message) {
            return data.message;
        }


        if (data.error) {
            return data.error;
        }


        return `Request failed with status ${response.status}.`;


    } catch {

        return `Request failed with status ${response.status}.`;

    }

}