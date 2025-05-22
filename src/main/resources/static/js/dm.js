const baseURL = 'http://localhost:8080';

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
                msgDiv.innerHTML = `<strong>${msg.senderEmail}:</strong> ${msg.content} <br><small>${msg.creationDatetime}</small>`;
                chatArea.appendChild(msgDiv);
            });
            // Set receiver email for sendMessage
            const sendBtn = document.querySelector('.chat-container .chat-input button');
            if (sendBtn) sendBtn.setAttribute('data-receiver-email', email);
            //Subscribe to messages
            subscribeToMessages(email);
        })
        .catch(error => {
            console.error('Error starting chat:', error);
        });
}

function sendMessage() {
    const sendBtn = document.querySelector('.chat-container .chat-input button');
    const receiverEmail = sendBtn.getAttribute('data-receiver-email');
    const chatInput = document.getElementById('text-input');
    console.log('Attempt sending: ' + chatInput.value);
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
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log("Message sent:", data);
            chatInput.value = '';
            startChat(receiverEmail);
        })
        .catch(error => {
            console.error('Error sending message:', error);
        });
}


let sseSource = null;
function subscribeToMessages(email) {
    if (sseSource) sseSource.close();
    sseSource = new EventSource('/sse/messages/' + email);

    sseSource.onmessage = function(event) {
        const msg = JSON.parse(event.data);
        startChat(email);
    };

    sseSource.onerror = function() {
        sseSource.close();
    };
}

document.getElementById('dialogBackdrop').onclick = closeNewChatDialog;
