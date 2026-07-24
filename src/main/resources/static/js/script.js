// ==========================================================
// LOGIN
// ==========================================================

const loginForm = document.getElementById("loginForm");

if (loginForm) {

    loginForm.addEventListener("submit", async function(event) {

        event.preventDefault();

        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;
        const message = document.getElementById("message");

        // Get selected role from URL
        const params = new URLSearchParams(window.location.search);
        const selectedRole = params.get("role");

        if (!selectedRole) {
            message.textContent = "Please select Candidate or Recruiter first.";
            return;
        }

        try {

            const response = await fetch(
                "/api/users/login?role=" + encodeURIComponent(selectedRole),
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        email: email,
                        password: password
                    })
                }
            );

            if (!response.ok) {
                message.textContent =
                    "Invalid credentials or incorrect account role.";
                return;
            }

            const user = await response.json();

            if (!user || !user.id) {
                message.textContent =
                    "Invalid credentials or incorrect account role.";
                return;
            }

            if (user.role !== selectedRole) {

                message.textContent =
                    "This account is not registered as a " +
                    selectedRole.toLowerCase() +
                    ".";

                return;
            }

            localStorage.setItem("loggedInUserId", user.id);
            localStorage.setItem("loggedInUserEmail", user.email);

            if (user.role === "CANDIDATE") {

                window.location.href =
                    "/candidate/dashboard";

            } else if (user.role === "RECRUITER") {

                window.location.href =
                    "/recruiter/dashboard";

            } else {

                message.textContent =
                    "Invalid user role.";

            }

        } catch (error) {

            console.error(error);

            message.textContent =
                "Something went wrong. Please try again.";

        }

    });

}


// ==========================================================
// CANDIDATE REGISTRATION
// ==========================================================

const registerForm = document.getElementById("registerForm");

if (registerForm) {

    registerForm.addEventListener(
        "submit",
        async function(event) {

            event.preventDefault();

            const fullName =
                document.getElementById("name").value;

            const email =
                document.getElementById("email").value;

            const password =
                document.getElementById("password").value;

            const message =
                document.getElementById("message");

            try {

                const response =
                    await fetch("/api/users/register", {

                        method: "POST",

                        headers: {
                            "Content-Type": "application/json"
                        },

                        body: JSON.stringify({

                            fullName: fullName,
                            email: email,
                            password: password,
                            role: "CANDIDATE"

                        })

                    });

                if (!response.ok) {

                    const errorText = await response.text();

                    if (errorText.includes("Email already registered")) {

                        message.textContent =
                            "This email is already registered. Please login instead.";

                    } else {

                        message.textContent =
                            "Registration failed. Please try again.";

                    }

                    return;
                }

                const user = await response.json();

                if (!user || !user.id) {

                    message.textContent =
                        "Registration failed.";

                    return;
                }

                message.textContent =
                    "Account created successfully! Redirecting to login...";

                setTimeout(function() {

                    window.location.href =
                        "/login?role=CANDIDATE";

                }, 1000);

            } catch (error) {

                console.error(error);

                message.textContent =
                    "Something went wrong. Please try again.";

            }

        }

    );

}
// ==========================================================
// RECRUITER REGISTRATION
// ==========================================================

const recruiterRegisterForm =
    document.getElementById("recruiterRegisterForm");

if (recruiterRegisterForm) {

    recruiterRegisterForm.addEventListener(
        "submit",
        async function(event) {

            event.preventDefault();

            const fullName =
                document.getElementById("recruiterName").value;

            const email =
                document.getElementById("recruiterEmail").value;

            const password =
                document.getElementById("recruiterPassword").value;

            const message =
                document.getElementById("recruiterMessage");

            try {

                const response =
                    await fetch("/api/users/register", {

                        method: "POST",

                        headers: {
                            "Content-Type": "application/json"
                        },

                        body: JSON.stringify({

                            fullName: fullName,
                            email: email,
                            password: password,
                            role: "RECRUITER"

                        })

                    });

                if (!response.ok) {

                    const errorText = await response.text();

                    if (errorText.includes("Email already registered")) {

                        message.textContent =
                            "This email is already registered. Please login instead.";

                    } else {

                        message.textContent =
                            "Registration failed. Please try again.";

                    }

                    return;
                }

                const user = await response.json();

                if (!user || !user.id) {

                    message.textContent =
                        "Registration failed.";

                    return;
                }

                message.textContent =
                    "Recruiter account created successfully! Redirecting to login...";

                setTimeout(function() {

                    window.location.href =
                        "/login?role=RECRUITER";

                }, 1000);

            } catch (error) {

                console.error(error);

                message.textContent =
                    "Something went wrong. Please try again.";

            }

        }

    );

}