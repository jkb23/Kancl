const messageElement = document.querySelector('#js-message');

const fileUpload = document.querySelector('#js-file-uploader');
const profileTrigger = document.querySelector('#js-profile-trigger');
const profileBackground = document.querySelector('#js-profile-pic');


profileTrigger.addEventListener('click', function (event) {
    event.preventDefault();
    fileUpload.click();
});

const input = document.getElementById('js-file-uploader')

input.addEventListener('change', (event) => {
    const target = event.target
    if (target.files && target.files[0]) {

        const maxAllowedSize = 2 * 1024 * 1024;
        if (target.files[0].size > maxAllowedSize) {
            target.value = ''
            alert('File cannot exceed 2MB in size')
        }
    }
})

fileUpload.addEventListener("change", function (event) {
    if (fileUpload.files && fileUpload.files[0]) {
        let reader = new FileReader();
        reader.onload = function (event) {
            profileBackground.childNodes[0].nodeValue = "";
            profileBackground.style.backgroundImage = "url('" + event.target.result + "')";
        }
        reader.readAsDataURL(fileUpload.files[0]);
    }
});

