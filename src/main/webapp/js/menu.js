.custom-main-navbar {
    background-color: #3c817d;
    box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.32);
    height: 125px;
    display: flex;
    align-items: center;
    padding: 0 15px;
}

.custom-main-navbar .navbar-brand {
    padding: 0;
    margin-right: auto;
    display: flex;
    align-items: center;
}

.custom-main-navbar .navbar-brand img {
    height: 90px;
    width: auto;
    margin-left: 15px;
}

.custom-main-navbar .navbar-toggler {
    border-color: rgba(255, 255, 255, 0.5);
}

.custom-main-navbar .navbar-toggler-icon {
    background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 30 30'%3e%3cpath stroke='rgba%28255, 255, 255, 1%29' stroke-linecap='round' stroke-miterlimit='10' stroke-width='2' d='M4 7h22M4 15h22M4 23h22'/%3e%3c/svg%3e");
}

.custom-main-navbar .navbar-nav .nav-item {
    margin-left: 20px;
}

.custom-main-navbar .custom-nav-link {
    font-family: 'Inter', sans-serif;
    font-weight: 300;
    color: #ffffff;
    font-size: 22px;
    letter-spacing: 2px;
    padding: 10px 15px;
    transition: color 0.3s ease;
    white-space: nowrap;
}

.custom-main-navbar .custom-nav-link:hover,
.custom-main-navbar .custom-nav-link:focus {
    color: #7faaa2;
    text-decoration: none;
}

@media (max-width: 991.98px) {
    .custom-main-navbar .navbar-brand {
        margin-right: 0;
    }

    .custom-main-navbar .navbar-collapse {
        background-color: #2a5553;
        border-radius: 8px;
        margin-top: 10px;
        padding: 10px 0;
    }

    .custom-main-navbar .navbar-nav {
        width: 100%;
    }

    .custom-main-navbar .navbar-nav .nav-item {
        margin: 0;
        text-align: center;
    }

    .custom-main-navbar .custom-nav-link {
        padding: 10px;
    }
}