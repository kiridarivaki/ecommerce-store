let currentCost = 0;
let selectedShoeBaseCost = 0;
let selectedShoeId = null;

document.addEventListener('DOMContentLoaded', function () {
    const modelSelect = document.getElementById('modelButton');
    const sizeContainer = document.getElementById('sizeButton');
    const mainColorInput = document.getElementById('mcolor');
    const soleColorInput = document.getElementById('scolor');
    const laceColorContainer = document.getElementById('laceColorButton');
    const submitButton = document.querySelector('button.next');
    const selectedShoeIdInput = document.getElementById('selectedShoeId');
    const selectedSizeInput = document.getElementById('selectedSize');
    const preferencesForm = document.getElementById('preferencesForm');

    disableOtherFormElements();

    modelSelect.addEventListener('change', async function () {
        const selectedOption = this.options[this.selectedIndex];
        const shoeId = selectedOption.value;
        const baseCost = parseFloat(selectedOption.dataset.baseCost || '0');

              console.log("--- Model Change Event Fired ---"); // CHECK 2
                console.log("Selected Option Object:", selectedOption); // CHECK 3
                console.log("Extracted Shoe ID from option.value:", shoeId); // CHECK 4
                console.log("Extracted Base Cost from dataset:", baseCost);

        if (!shoeId) {
            resetForm();
            return;
        }

        selectedShoeId = shoeId;
        selectedShoeBaseCost = baseCost;
        selectedShoeIdInput.value = shoeId;

        try {
            const response = await fetch(`${preferencesForm.action}?action=getShoeSizes&shoeId=${shoeId}`);
            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Failed to fetch shoe sizes.');
            }
            const availableSizes = await response.json();
            populateSizes(availableSizes);
        } catch (error) {
            console.error("Error fetching shoe sizes:", error);
            sizeContainer.innerHTML = '<p style="color: red;">Error loading sizes. Please try again.</p>';
            resetForm();
            return;
        }

        enableOtherFormElements();
        updateTotalCost();
    });

    sizeContainer.addEventListener('change', function (event) {
        if (event.target.name === 'size') {
            selectedSizeInput.value = event.target.value;
            updateTotalCost();
        }
    });

    mainColorInput.addEventListener('input', updateTotalCost);
    soleColorInput.addEventListener('input', updateTotalCost);
    laceColorContainer.addEventListener('change', function (event) {
        if (event.target.name === 'selectedLaceColor') {
            updateTotalCost();
        }
    });

    submitButton.addEventListener('click', function(event) {
        event.preventDefault();

        const selectedModel = document.getElementById('modelButton').value;
        const sizeChecked = Array.from(document.getElementsByName('size')).some(radio => radio.checked);
        const laceColorChecked = Array.from(document.getElementsByName('selectedLaceColor')).some(radio => radio.checked);

        if (!selectedModel) {
            alert('Please select a shoe model.');
            return;
        }
        if (!sizeChecked) {
            alert('Please select a shoe size.');
            return;
        }
        if (!laceColorChecked) {
            alert('Please pick a lace color.');
            return;
        }

        preferencesForm.submit();
    });

    function populateSizes(sizes) {
        sizeContainer.innerHTML = '';
        selectedSizeInput.value = '';

        if (sizes.length === 0) {
            sizeContainer.innerHTML = '<p>No sizes available for this model.</p>';
            return;
        }

        sizes.forEach(size => {
            const radioOption = document.createElement('div');
            radioOption.classList.add('radio-option');
            radioOption.innerHTML = `
                <input type="radio" name="size" id="size-${size.replace('.', '-')}" value="${size}">
                <span class="checkmark"></span>
                <div class="button-label">${size}</div>
            `;
            sizeContainer.appendChild(radioOption);
        });
    }

    function disableOtherFormElements() {
        const elementsToDisable = document.querySelectorAll('.form-container input, .form-container select:not(#modelButton), .form-container button.next');
        elementsToDisable.forEach(element => {
            element.disabled = true;
        });
    }

    function enableOtherFormElements() {
        const elementsToEnable = document.querySelectorAll('.form-container input, .form-container select:not(#modelButton), .form-container button.next');
        elementsToEnable.forEach(element => {
            element.disabled = false;
        });
    }

    function resetForm() {
        currentCost = 0;
        selectedShoeBaseCost = 0;
        selectedShoeId = null;
        document.getElementById('selectedShoeId').value = '';
        document.getElementById('selectedSize').value = '';
        document.getElementById('mcolor').value = '#000000';
        document.getElementById('scolor').value = '#000000';
        Array.from(document.getElementsByName('selectedLaceColor')).forEach(radio => radio.checked = false);
        populateSizes([]);
        disableOtherFormElements();
        updateCostDisplay();
    }

    function updateTotalCost() {
        let total = selectedShoeBaseCost;

        const selectedSizeRadio = document.querySelector('input[name="size"]:checked');
        if (selectedSizeRadio) {
            const size = parseFloat(selectedSizeRadio.value);
            if (size >= 39 && size <= 41) {
                total += 50;
            } else if (size === 44 || size === 45) {
                total += 40;
            } else if (size === 36) {
                total -= 20;
            } else if (size === 37) {
                total -= 10;
            }
        }

        const mainColor = document.getElementById('mcolor').value;
        if (!(mainColor === '#000000' || mainColor === '#ffffff')) {
            total += 20;
        }

        const soleColor = document.getElementById('scolor').value;
        if (!(soleColor === '#000000' || soleColor === '#ffffff')) {
            total += 15;
        }

        const selectedLaceColorRadio = document.querySelector('input[name="selectedLaceColor"]:checked');
        if (selectedLaceColorRadio) {
            const laceColor = selectedLaceColorRadio.value;
            if (laceColor === 'yellow' || laceColor === 'blue' || laceColor === 'red') {
                total += 12;
            } else if (laceColor === 'pink' || laceColor === 'purple' || laceColor === 'orange') {
                total += 15;
            }
        }

        currentCost = total;
        updateCostDisplay();
    }

    function updateCostDisplay() {
        document.getElementById('costDisplay').innerText = currentCost.toFixed(2) + ' $';
        document.getElementById('cost').value = currentCost.toFixed(2);
    }
});