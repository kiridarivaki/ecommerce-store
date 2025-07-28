document.addEventListener('DOMContentLoaded', function() {
    const showArtistFieldsButton = document.getElementById('showArtistFieldsButton');
    const artistFieldsDiv = document.getElementById('artistFields');
    const roleInput = document.getElementById('role');

    if (showArtistFieldsButton && artistFieldsDiv && roleInput) {
        showArtistFieldsButton.addEventListener('click', function() {
            if (artistFieldsDiv.style.display === 'none') {
                artistFieldsDiv.style.display = 'block';
                showArtistFieldsButton.textContent = 'I am a regular user';
                roleInput.value = 'artist';
            } else {
                artistFieldsDiv.style.display = 'none';
                showArtistFieldsButton.textContent = 'Are you an artist?';
                roleInput.value = 'user';
                document.querySelectorAll('#artistFields input, #artistFields textarea').forEach(input => input.value = '');
            }
        });
    }

    const togglePassword = document.getElementById('togglePassword');
    const passwordInput = document.getElementById('password');
    const eyeIcon = document.getElementById('eyeIcon');

    if (togglePassword && passwordInput && eyeIcon) {
        togglePassword.addEventListener('click', function() {
            const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
            passwordInput.setAttribute('type', type);
            eyeIcon.classList.toggle('fa-eye');
            eyeIcon.classList.toggle('fa-eye-slash');
        });
    }
});