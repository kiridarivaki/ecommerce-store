function shopProduct() {
    var inputField = document.getElementById("input-box");
    var warning = document.querySelector(".warning");
    if (inputField.value.trim() === "") {
      warning.style.display = "block";
    } else {
      warning.style.display = "none";

      var popup = document.getElementById("popup");
      popup.style.display = "block";

      setTimeout(function() {
        popup.style.display = "none";
      }, 3000);
    }
  }
