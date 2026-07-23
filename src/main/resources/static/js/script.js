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
        // Example: /login?role=RECRUITER
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

            // Extra frontend role verification
            if (user.role !== selectedRole) {

                message.textContent =
                    "This account is not registered as a " +
                    selectedRole.toLowerCase() +
                    ".";

                return;
            }

            // Optional local storage
            localStorage.setItem(
                "loggedInUserId",
                user.id
            );

            localStorage.setItem(
                "loggedInUserEmail",
                user.email
            );

            // Role-based redirect
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
let candidateOtpVerified = false;

const sendOtpBtn = document.getElementById("sendOtpBtn");
const verifyOtpBtn = document.getElementById("verifyOtpBtn");
const otpSection = document.getElementById("otpSection");

if (sendOtpBtn) {

    sendOtpBtn.addEventListener("click", async function() {

        const email = document.getElementById("email").value;
        const message = document.getElementById("message");

        if (!email) {
            message.textContent = "Please enter your email first.";
            return;
        }

        try {

            const response = await fetch(
                "/api/users/send-otp?email=" + encodeURIComponent(email),
                {
                    method: "POST"
                }
            );

            if (!response.ok) {
                message.textContent = "Failed to send OTP. Please try again.";
                return;
            }

            candidateOtpVerified = false;
            otpSection.style.display = "block";

            message.textContent =
                "OTP sent successfully. Please check your email.";

        } catch (error) {

            console.error(error);
            message.textContent =
                "Something went wrong while sending OTP.";
        }

    });
}


if (verifyOtpBtn) {

    verifyOtpBtn.addEventListener("click", async function() {

        const email = document.getElementById("email").value;
        const otp = document.getElementById("otp").value;
        const message = document.getElementById("message");

        if (!otp) {
            message.textContent = "Please enter the OTP.";
            return;
        }

        try {

            const response = await fetch(
                "/api/users/verify-otp?email=" +
                encodeURIComponent(email) +
                "&otp=" +
                encodeURIComponent(otp),
                {
                    method: "POST"
                }
            );

            if (!response.ok) {
                candidateOtpVerified = false;
                message.textContent =
                    "Invalid or expired OTP.";
                return;
            }

            candidateOtpVerified = true;

            message.textContent =
                "Email verified successfully!";

        } catch (error) {

            console.error(error);

            candidateOtpVerified = false;

            message.textContent =
                "Something went wrong while verifying OTP.";
        }

    });
}
const registerForm =
    document.getElementById("registerForm");

if (registerForm) {

    registerForm.addEventListener(
        "submit",
        async function(event) {

            event.preventDefault();
			event.preventDefault();

			if (!candidateOtpVerified) {
			    document.getElementById("message").textContent =
			        "Please verify your email before creating an account.";
			    return;
			}

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
                            "Content-Type":
                                "application/json"
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
                const user =
                    await response.json();

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
let recruiterOtpVerified = false;

const recruiterSendOtpBtn =
    document.getElementById("recruiterSendOtpBtn");

const recruiterVerifyOtpBtn =
    document.getElementById("recruiterVerifyOtpBtn");

const recruiterOtpSection =
    document.getElementById("recruiterOtpSection");


if (recruiterSendOtpBtn) {

    recruiterSendOtpBtn.addEventListener("click", async function() {

        const email =
            document.getElementById("recruiterEmail").value;

        const message =
            document.getElementById("recruiterMessage");

        if (!email) {
            message.textContent = "Please enter your email first.";
            return;
        }

        try {

            const response = await fetch(
                "/api/users/send-otp?email=" +
                encodeURIComponent(email),
                {
                    method: "POST"
                }
            );

            if (!response.ok) {
                message.textContent =
                    "Failed to send OTP. Please try again.";
                return;
            }

            recruiterOtpVerified = false;
            recruiterOtpSection.style.display = "block";

            message.textContent =
                "OTP sent successfully. Please check your email.";

        } catch (error) {

            console.error(error);

            message.textContent =
                "Something went wrong while sending OTP.";
        }
    });
}


if (recruiterVerifyOtpBtn) {

    recruiterVerifyOtpBtn.addEventListener("click", async function() {

        const email =
            document.getElementById("recruiterEmail").value;

        const otp =
            document.getElementById("recruiterOtp").value;

        const message =
            document.getElementById("recruiterMessage");

        if (!otp) {
            message.textContent = "Please enter the OTP.";
            return;
        }

        try {

            const response = await fetch(
                "/api/users/verify-otp?email=" +
                encodeURIComponent(email) +
                "&otp=" +
                encodeURIComponent(otp),
                {
                    method: "POST"
                }
            );

            if (!response.ok) {
                recruiterOtpVerified = false;
                message.textContent =
                    "Invalid or expired OTP.";
                return;
            }

            recruiterOtpVerified = true;

            message.textContent =
                "Email verified successfully!";

        } catch (error) {

            console.error(error);

            recruiterOtpVerified = false;

            message.textContent =
                "Something went wrong while verifying OTP.";
        }
    });
}
const recruiterRegisterForm =
    document.getElementById(
        "recruiterRegisterForm"
    );

if (recruiterRegisterForm) {

    recruiterRegisterForm.addEventListener(
        "submit",
        async function(event) {

            event.preventDefault();
			if (!recruiterOtpVerified) {
			    document.getElementById("recruiterMessage").textContent =
			        "Please verify your email before creating an account.";
			    return;
			}

            const fullName =
                document.getElementById(
                    "recruiterName"
                ).value;

            const email =
                document.getElementById(
                    "recruiterEmail"
                ).value;

            const password =
                document.getElementById(
                    "recruiterPassword"
                ).value;

            const message =
                document.getElementById(
                    "recruiterMessage"
                );

            try {

                const response =
                    await fetch(
                        "/api/users/register",
                        {

                            method: "POST",

                            headers: {
                                "Content-Type":
                                    "application/json"
                            },

                            body: JSON.stringify({

                                fullName: fullName,

                                email: email,

                                password: password,

                                role: "RECRUITER"

                            })

                        }
                    );

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
					
                const user =
                    await response.json();

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









