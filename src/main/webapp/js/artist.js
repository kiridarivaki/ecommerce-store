document.addEventListener('DOMContentLoaded', function() {
    let selectedArtistId = null;

    const selectArtistButtons = document.querySelectorAll('.select-artist-button');
    const sketchLink = document.getElementById('sketch-link');
    const basketLink = document.getElementById('basket-link');

    selectArtistButtons.forEach(button => {
        button.addEventListener('click', function() {
            const artistId = this.dataset.artistId;

            selectArtistButtons.forEach(btn => {
                if (btn !== this) {
                    btn.classList.remove('selected');
                    btn.textContent = 'Select Artist';
                }
            });

            this.classList.toggle('selected');
            if (this.classList.contains('selected')) {
                selectedArtistId = artistId;
                this.textContent = 'Selected';
                enableButtons();
            } else {
                selectedArtistId = null;
                this.textContent = 'Select Artist';
                disableButtons();
            }
        });
    });

    function enableButtons() {
        sketchLink.removeAttribute('disabled');
        sketchLink.classList.remove('disabled');
        basketLink.removeAttribute('disabled');
        basketLink.classList.remove('disabled');
    }

    function disableButtons() {
        sketchLink.setAttribute('disabled', 'true');
        sketchLink.classList.add('disabled');
        basketLink.setAttribute('disabled', 'true');
        basketLink.classList.add('disabled');
    }

    disableButtons();
});