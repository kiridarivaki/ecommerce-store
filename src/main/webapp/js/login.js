document.addEventListener("DOMContentLoaded", function () {
    const passwordInput = document.getElementById("password-input");
    const eyeIcon = document.getElementById("eye-icon");
    const eyeImage = document.getElementById("eye-image");
    const loginButton = document.getElementById("loginButton");

    eyeIcon.addEventListener("click", function () {
      if (passwordInput.type === "password") {
        passwordInput.type = "text";
        eyeImage.src = "img/eye (1).svg";
      } else {
        passwordInput.type = "password";
        eyeImage.src = "img/eye-off-outline-2.svg";
      }
    });

    loginButton.addEventListener("click", function (e) {
      e.preventDefault(); 
      const username = document.getElementById("username").value;
      const password = document.getElementById("password-input").value;

      if (username === "" || password === "") {
        alert("Please fill in both Username and Password fields.");
      } else {
        window.location.href = "preferences.jsp";
      }
    });
  });