<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Sign Up</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/signup.css"/>
</head>
<body>
    <div class="container"> <div class="card"> <div class="card-header"> <h2 class="card-title mb-0">USER SIGN UP</h2> </div>
            <div class="card-body">
            <form action="${pageContext.request.contextPath}/signUp" method="post">
                    <input type="hidden" id="role" name="role" value="user">

                    <div class="mb-3">
                        <label for="username" class="form-label visually-hidden">Username</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="fa-solid fa-user"></i></span>
                            <input type="text" class="form-control" id="username" name="username" placeholder="Username" required>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="password" class="form-label visually-hidden">Password</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="fa-solid fa-key"></i></span>
                            <input type="password" class="form-control" id="password" name="password" placeholder="Password" required>
                            <button class="btn btn-outline-secondary" type="button" id="togglePassword">
                                <i class="fa-solid fa-eye-slash" id="eyeIcon"></i>
                            </button>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="firstName" class="form-label visually-hidden">First Name</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="fa-solid fa-user"></i></span>
                            <input type="text" class="form-control" id="firstName" name="firstName" placeholder="First Name" required>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="lastName" class="form-label visually-hidden">Last Name</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="fa-solid fa-user"></i></span>
                            <input type="text" class="form-control" id="lastName" name="lastName" placeholder="Last Name" required>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="email" class="form-label visually-hidden">Email</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="fa-solid fa-envelope"></i></span>
                            <input type="email" class="form-control" id="email" name="email" placeholder="Email" required>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="phoneNumber" class="form-label visually-hidden">Phone Number</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="fa-solid fa-phone"></i></span>
                            <input type="tel" class="form-control" id="phoneNumber" name="phoneNumber" placeholder="Phone Number" required>
                        </div>
                    </div>

                    <button type="button" id="showArtistFieldsButton" class="btn btn-secondary w-100 mt-3 mb-3">
                        Are you an artist?
                    </button>

                    <div id="artistFields" style="display: none;">
                        <p class="text-center text-muted mb-3 mt-3">Artist Profile Details</p>
                        <div class="mb-3">
                            <label for="style" class="form-label visually-hidden">Art Style</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fa-solid fa-palette"></i></span>
                                <input type="text" class="form-control" id="style" name="style" placeholder="Your artistic style (e.g., Abstract)">
                            </div>
                        </div>
                        <div class="mb-3">
                            <label for="bio" class="form-label visually-hidden">Biography</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fa-solid fa-book-open"></i></span>
                                <textarea class="form-control" id="bio" name="bio" rows="3" placeholder="Tell us about your art and background..."></textarea>
                            </div>
                        </div>
                        <div class="mb-3">
                            <label for="portfolioUrl" class="form-label visually-hidden">Portfolio URL</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fa-solid fa-globe"></i></span>
                                <input type="url" class="form-control" id="portfolioUrl" name="portfolioUrl" placeholder="Link to your online portfolio">
                            </div>
                        </div>
                        <div class="mb-3">
                            <label for="facebook" class="form-label visually-hidden">Facebook URL</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fa-brands fa-facebook"></i></span>
                                <input type="url" class="form-control" id="facebook" name="facebook" placeholder="Your Facebook profile URL">
                            </div>
                        </div>
                        <div class="mb-3">
                            <label for="instagram" class="form-label visually-hidden">Instagram URL</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fa-brands fa-instagram"></i></span>
                                <input type="url" class="form-control" id="instagram" name="instagram" placeholder="Your Instagram profile URL">
                            </div>
                        </div>
                    </div>

                    <button type="submit" class="btn btn-primary w-100 mt-4">SIGN UP</button>
                </form>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/signup.js"></script>
</body>
</html>