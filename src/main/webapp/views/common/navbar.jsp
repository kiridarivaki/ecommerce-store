<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>

<nav class="navbar navbar-expand-lg custom-main-navbar">
    <div class="container-fluid">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/index.jsp">
            <img src="${pageContext.request.contextPath}/img/logo.png" alt="SoleExpressions Logo" width="60" height="80">
        </a>
        <button class="navbar-toggler custom-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse justify-content-end" id="navbarNav">
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link custom-nav-link" href="${pageContext.request.contextPath}/views/common/aboutus.html">ABOUT US</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link custom-nav-link" href="${pageContext.request.contextPath}/views/auth/signup.jsp">SIGN UP</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link custom-nav-link" href="${pageContext.request.contextPath}/views/auth/login.jsp">LOG IN</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link custom-nav-link" href="${pageContext.request.contextPath}/preferences">CUSTOMIZE</a>
                </li>
            </ul>
        </div>
    </div>
</nav>