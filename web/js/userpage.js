const passwordCurrent = document.getElementById('password-current');
const passwordCurrentRepeat = document.getElementById('password-current-repeat');
const passwordNew = document.getElementById('password-new');
const submitBtn = document.getElementById('submit');

const isCorrectOldPassword = function (pwd) {
    // For simplicity, let's assume the old password is 'oldPass123'.
    // In a real-world scenario, this should be checked asynchronously with a server.
    return pwd === 'oldPass123';
};

const checkPasswords = function () {
    if (enableSubmitIfAllPasswordFieldsAreEmpty()) {
        return
    }

    disableSubmitIfAnyPasswordFieldIsNotEmpty()

    /*if (!isCorrectOldPassword(passwordCurrent.value)) {
        console.log("Incorrect current password");
        return;
    }*/

    submitBtn.disabled = !(newPasswordMatchesPattern() && oldPasswordsAreEqual() && newPasswordIsLongEnough())
};

const newPasswordMatchesPattern = function () {
    const pattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[\d@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    if (pattern.test(passwordNew.value)) {
        return true
    }
    passwordNew.style.color = "red";
    passwordNew.style.outlineColor = "red";

    return false
};

const oldPasswordsAreEqual = function () {
    if (passwordCurrent.value === passwordCurrentRepeat.value) {
        passwordCurrentRepeat.style.color = "black";
        passwordCurrentRepeat.style.outlineColor = "#080891";

        return true
    }
    passwordCurrentRepeat.style.color = "red";
    passwordCurrentRepeat.style.outlineColor = "red";

    return false
};

const newPasswordIsLongEnough = function () {
    if (passwordNew.value.length >= 8) {
        passwordNew.style.color = "black";
        passwordNew.style.outlineColor = "#080891";

        return true
    }
    passwordNew.style.color = "red";
    passwordNew.style.outlineColor = "red";

    return false
};

const enableSubmitIfAllPasswordFieldsAreEmpty = function () {
    if (passwordCurrent.value === "" && passwordNew.value === "" && passwordCurrentRepeat.value === "") {
        submitBtn.disabled = false;
        passwordCurrentRepeat.style.color = "black";
        passwordCurrentRepeat.style.outlineColor = "#080891";
        passwordNew.style.color = "black";
        passwordNew.style.outlineColor = "#080891";
        passwordCurrent.style.color = "black";
        passwordCurrent.style.outlineColor = "#080891";

        return true
    }

    return false
}

const disableSubmitIfAnyPasswordFieldIsNotEmpty = function () {
    if (passwordCurrent.value !== "" || passwordNew.value !== "" || passwordCurrentRepeat.value !== "") {
        submitBtn.disabled = true;
    }
}
