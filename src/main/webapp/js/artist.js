document.addEventListener('DOMContentLoaded', function() {
    const selectArtistButtons = document.querySelectorAll('.select-artist-button');
    const artistCards = document.querySelectorAll('.artist-card');
    const selectedArtistIdInput = document.getElementById('selectedArtistId');
    const saveArtistBtn = document.getElementById('saveArtistBtn');
    const addToBasketBtn = document.getElementById('addToBasketBtn');

    const artistSavedSuccessfully = document.body.dataset.artistSaved === 'true';

    function updateButtonState() {
        const hasSelection = selectedArtistIdInput.value && selectedArtistIdInput.value !== "-1";

        saveArtistBtn.disabled = !hasSelection;

        addToBasketBtn.disabled = !(hasSelection || artistSavedSuccessfully);
    }

    function highlightSelectedArtist() {
        const preselectedArtistId = selectedArtistIdInput.value;
        if (preselectedArtistId && preselectedArtistId !== "-1") {
            artistCards.forEach(card => {
                if (card.dataset.artistId === preselectedArtistId) {
                    card.classList.add('selected');
                } else {
                    card.classList.remove('selected');
                }
            });
        }
    }

    updateButtonState();
    highlightSelectedArtist();

    selectArtistButtons.forEach(button => {
        button.addEventListener('click', function() {
            const artistId = this.dataset.artistId;

            artistCards.forEach(card => {
                card.classList.remove('selected');
            });

            this.closest('.artist-card').classList.add('selected');

            selectedArtistIdInput.value = artistId;

            updateButtonState();
        });
    });

    saveArtistBtn.addEventListener('click', function(event) {
        if (!selectedArtistIdInput.value || selectedArtistIdInput.value === "-1") {
            event.preventDefault();
            toastr.warning("Please select an artist before saving.", "Selection Required!");
        }
    });

    addToBasketBtn.addEventListener('click', function(event) {
        if (!selectedArtistIdInput.value || selectedArtistIdInput.value === "-1") {
            event.preventDefault();
            toastr.warning("Please save your artist selection first.", "Action Required!");
        }
    });
});