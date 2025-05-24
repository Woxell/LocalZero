const baseURL = 'https://localzero.fly.dev';

document.getElementById("submit").addEventListener('click', (event) => {
    event.preventDefault(); // Prevent default form submission
    submitForm();
});

function submitForm() {
    console.log("Submit button clicked");

    const data = {
        name: document.getElementById('name').value,
        email: document.getElementById('email').value,
        password: document.getElementById('password').value,
        profilePicture: document.getElementById('profilePicture').value
    };

    if (!data.name || !data.email || !data.password) {
        alert("Please fill in all fields");
        return;
    }

    console.log(data);

    const jsonData = JSON.stringify(data);
    fetch(baseURL + '/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: jsonData
    })
        .then(response => {
            if (response.redirected) {
                window.location.href = response.url;
            }
        })
}