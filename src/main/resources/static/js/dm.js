const baseURL = 'https://localzero.se';

function openNewChatDialog() {
    document.getElementById('dialogBackdrop').style.display = 'block';
    document.getElementById('newChatDialog').style.display = 'block';
}

function closeNewChatDialog() {
    document.getElementById('dialogBackdrop').style.display = 'none';
    document.getElementById('newChatDialog').style.display = 'none';
}

function startChat(email) {
    closeNewChatDialog();

    fetch(baseURL + '/messages/' + email)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log("Chat started with " + email, data);

            const chatArea = document.querySelector('.chat-container .chat-area');
            if (!chatArea) return;
            chatArea.innerHTML = ''; // Clear previous messages

            data.forEach(msg => {
                const msgDiv = document.createElement('div');
                msgDiv.className = 'chat-message';

                // Format datetime as YYYY-MM-DD HH:mm
                const date = new Date(msg.creationDatetime);
                const formatted = date.getFullYear() + '-' +
                    String(date.getMonth() + 1).padStart(2, '0') + '-' +
                    String(date.getDate()).padStart(2, '0') + ' ' +
                    String(date.getHours()).padStart(2, '0') + ':' +
                    String(date.getMinutes()).padStart(2, '0');

                let contentHtml;
                if (msg.content === "image" && msg.id) {
                    // Display image
                    contentHtml = `<img src="${baseURL}/messages/image/${msg.id}" alt="Image" style="max-width:200px; max-height:200px;" />`;
                } else {
                    // Display text
                    contentHtml = msg.content;
                }

                msgDiv.innerHTML = `<strong>${msg.senderEmail}:</strong> ${contentHtml} <br><small>${formatted}</small>`;
                chatArea.appendChild(msgDiv);
            });

            // Set receiver email for sendMessage
            const sendBtn = document.getElementById('send-button');
            if (sendBtn) sendBtn.setAttribute('receiver-email', email);

            //Make receiver subscribe to messages
            subscribeToMessages(clientEmail, email);
            refreshDmList()
        })
        .catch(error => {
            console.error('Error starting chat:', error);
        });
}

function sendMessage() {
    const sendBtn = document.getElementById('send-button');
    const receiverEmail = sendBtn.getAttribute('receiver-email');
    const chatInput = document.getElementById('text-input');
    console.log('Attempt sending: ' + chatInput.value);

    if (selectedImageFile) {
        // Send image
        /*

        const formData = new FormData();
        formData.append('senderEmail', clientEmail);
        formData.append('receiverEmail', receiverEmail);
        formData.append('file', selectedImageFile);

        fetch(baseURL + '/messages/image', {
            method: 'POST',
            body: formData
        })
            .then(response => {
                if (!response.ok) throw new Error('Network response was not ok');
                return response.json();
            })
            .then(data => {
                selectedImageFile = null;
                document.getElementById('image-select-button').innerHTML = '📎';
                chatInput.value = '';
                chatInput.disabled = false;
                startChat(receiverEmail);
            })
            .catch(error => {
                console.error('Error sending image:', error);
            });

            */
    } else {
        // Send text message
        const message = chatInput.value;
        if (!message) {
            alert("Please enter a message");
            return;
        }
        const data = {
            receiverEmail: receiverEmail,
            content: message
        };
        fetch(baseURL + '/messages', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
            .then(response => {
                if (!response.ok) throw new Error('Network response was not ok');
                return response.json();
            })
            .then(data => {
                chatInput.value = '';
                startChat(receiverEmail);
            })
            .catch(error => {
                console.error('Error sending message:', error);
            });
    }
}

let selectedImageFile = null;

function handleImageSelected(event) {
    const file = event.target.files[0];
    if (file) {
        selectedImageFile = file;
        document.getElementById('image-select-button').innerHTML = '✅';
        document.getElementById('text-input').value = file.name;
        document.getElementById('text-input').disabled = true;
        alert('Selected image: ' + file.name);
    } else {
        selectedImageFile = null;
        document.getElementById('image-select-button').innerHTML = '📎';
        document.getElementById('text-input').value = '';
        document.getElementById('text-input').disabled = false;
    }
}

let sseSource = null;

function subscribeToMessages(clientEmail, receiverEmail) {
    if (sseSource) sseSource.close();
    sseSource = new EventSource('/sse/messages/' + clientEmail);
    console.log(clientEmail + ' subscribed to messages');
    sseSource.onmessage = function (event) {
        const msg = JSON.parse(event.data);
        refreshDmList();
        if (receiverEmail) {
            startChat(receiverEmail);
        }
    };

    sseSource.onerror = function () {
        console.error('Error subscribing to messages');
        sseSource.close();
    };
}

function refreshDmList() {
    fetch(baseURL + '/messages/chatpartners')
        .then(response => response.json())
        .then(chatPartners => {
            const dmList = document.querySelector('.dm-list');
            dmList.innerHTML = '';
            chatPartners.forEach(email => {
                const div = document.createElement('div');
                div.className = 'dm-list-item';
                div.setAttribute('onclick', `startChat('${email}')`);
                div.textContent = email;
                dmList.appendChild(div);
            });
        })
        .catch(error => {
            console.error('Error refreshing dm-list:', error);
        });
}

const clientEmail = document.getElementById('clientEmail').value;
document.getElementById('dialogBackdrop').onclick = closeNewChatDialog;
subscribeToMessages(clientEmail, null);
refreshDmList();

document.getElementById('text-input').addEventListener('keydown', function (e) {
    if (e.key === 'Enter' && !e.shiftKey) {
        e.preventDefault();
        sendMessage();
    }
});
