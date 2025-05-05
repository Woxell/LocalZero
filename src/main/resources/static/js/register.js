const baseURL = 'http://localhost:8080';

document.getElementById("submit").addEventListener('click', (event) => {
    event.preventDefault(); // Prevent default form submission
    submitForm();
});

function submitForm(){
    console.log("Submit button clicked");

    const data = {
        name: document.getElementById('name').value,
        email: document.getElementById('email').value,
        password: document.getElementById('password').value,
        pfpURL: document.getElementById('profilePicture').value
    };

    if(!data.name || !data.email || !data.password) {
        alert("Please fill in all fields");
        return;
    }

    const jsonData = JSON.stringify(data);
    fetch(baseURL + '/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: jsonData
    })
    .then(response => {
        //Handling response
    })
}