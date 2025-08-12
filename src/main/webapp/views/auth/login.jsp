<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Login</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@200;300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css" />
</head>
<body>
    <div class="desktop">
      <div class="div">
        <h2 class="form-title">USER LOGIN</h2>
        <form action="${pageContext.request.contextPath}/login" method="post">
          <div class="input-section overlap"> <span class="icon-wrapper"><i class="fa-solid fa-user"></i></span>
            <input type="text" class="form-control-custom" id="username" name="username" placeholder="Username">
          </div>
          <div class="input-section overlap-group"> <span class="icon-wrapper"><i class="fa-solid fa-key"></i></span>
            <input type="password" class="form-control-custom" id="password-input" name="password" placeholder="Password">
            <button type="button" class="toggle-password-btn" id="togglePassword">
              <i class="fa-solid fa-eye-slash" id="eyeIcon"></i>
            </button>
          </div>
          <p class="signup-link">
            <a href="${pageContext.request.contextPath}/views/auth/signup.jsp">New here? Sign Up.</a>
          </p>
          <button id="loginButton" type="submit" class="login-button">
            LOG IN
          </button>
        </form>
      </div>
    </div>
    <%@ include file="/jspf/toastr-messages.jspf" %>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/login.js"></script>
</body>
</html>