<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html>
<html>
  <head>
    <link rel="stylesheet" href="./css/index.css"/>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@200;300;400&display=swap" rel="stylesheet">
    <meta name="viewport" content="width=device-width, initial-scale=1">
  </head>
  <body>
    <div class="main-menu">
      <div class="overlap-wrapper">
        <div class="overlap">
          <div class="overlap-group">
            <div class="rectangle"></div>
            <div class="group">
              <a class="button" href="./views/auth/signup.jsp"><div class="signup"><div class="text-wrapper">SIGN UP</div></div></a>
              <a class= "button" href="./views/common/aboutus.html"><div class="aboutus"><div class="text-wrapper">ABOUT US</div></div></a>
              <a class= "button" href="./views/auth/login.jsp"><div class="login"><div class="text-wrapper">LOG IN</div></div></a>
            </div>
            <div class="group-wrapper">
              <div class="overlap-group-wrapper">
                <div class="overlap-group-3">
                  <div class="text-wrapper-2">EXPRESSIONS</div>
                  <div class="text-wrapper-3">SOLE</div>
                </div>
              </div>
            </div>
            <div class="group-2">
              <a class= "button" href="https://twitter.com/"><div class="socials1-wrapper"><img id="socials1" src="img/twitter.svg" /></div></a>
              <a class= "button" href="https://instagram.com/"><div class="socials2-wrapper"><img id="socials2" src="img/instagram.svg" /></div></a>
              <a class= "button" href="https://facebook.com/"><div class="socials3-wrapper"><img id="socials3" src="img/facebook.svg" /></div></a>
              <div class="text-wrapper-4">FIND US</div>
            </div>
          </div>
          <div class="group-3">
            <div class="overlap-3">
              <div class="text-wrapper-5">CUSTOM</div>
              <p class="text-wrapper-7">
                <span class="span">MAKE IT </span> <span class="text-wrapper-6">BORING</span>
              </p>
            </div>
          </div>
          <div class="overlap-4">
            <div class="rectangle-2"></div>
            <div class="group-4">
              <div class="overlap-5">
                <div class="logo"><img src="img/logo.png"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="gotoform"><a class="button" href="${pageContext.request.contextPath}/preferences"><div><div class="text-wrapper-8">GO TO FORM</div></div></a></div>
    </div>
  </body>
</html>
