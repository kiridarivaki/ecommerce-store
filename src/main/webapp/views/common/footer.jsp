<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@200;300;400&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css" />
</head>
<body>
<footer class="simple-footer">
    <div class="footer-section logo-area">
        <img class="footer-logo" src="${pageContext.request.contextPath}/img/logo.png" alt="SoleExpressions Logo"/>
        <a href="${pageContext.request.contextPath}/index.jsp" class="footer-link">HOME</a>
        <a href="${pageContext.request.contextPath}/views/common/aboutus.html" class="footer-link">ABOUT US</a>
    </div>

    <div class="footer-section social-area">
        <h3 class="footer-heading">FIND US</h3>
        <div class="social-icons">
            <a href="https://instagram.com/" class="social-icon-link"><i class="fa-brands fa-instagram"></i></a>
            <a href="https://facebook.com/" class="social-icon-link"><i class="fa-brands fa-facebook-f"></i></a>
            <a href="https://twitter.com/" class="social-icon-link"><i class="fa-brands fa-x-twitter"></i></a>
        </div>
    </div>

    <div class="footer-section contact-area">
        <h3 class="footer-heading">CONTACT US</h3>
        <div class="contact-items">
            <div class="contact-item">
                <i class="fa-solid fa-location-dot"></i>
                <a href="https://www.google.com/maps?q=28ης Οκτωβρίου 76, Αθήνα 104 34" class="contact-link">28ης Οκτωβρίου 76</a>
            </div>
            <div class="contact-item">
                <i class="fa-solid fa-envelope"></i>
                <a href="mailto:info@expressions.com" class="contact-link">info@xpressions.com</a>
            </div>
            <div class="contact-item">
                <i class="fa-solid fa-phone"></i>
                <a href="tel:2105878634" class="contact-link">2105878634</a>
            </div>
        </div>
    </div>
</footer>
</body>
</html>