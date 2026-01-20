let stompClient = null;
let currentUser = "";
let currentConversationId = null;

function login() {
    currentUser = document
        .getElementById("username")
        .value
        .trim()
        .toLowerCase();
    if (!currentUser) {
        alert("Enter username");
        return;
    }
    fetch(`/api/login?username=${currentUser}`, { method: "POST" })
        .then(() => loadChats());
}

function loadChats() {
    fetch(`/api/conversations/${currentUser}`)
        .then(res => res.json())
        .then(chats => {
            const list = document.getElementById("chatList");
            list.innerHTML = "";

            if (chats.length === 0) {
                list.innerHTML =
                    "<div style='padding:10px'>No chats found</div>";
                return;
            }

            chats.forEach(c => {
                const div = document.createElement("div");
                div.className = "chat-item";
                div.innerText = c.name;
                div.onclick = () => openChat(c.id);
                list.appendChild(div);
            });
        });
}
function openChat(conversationId) {
    currentConversationId = conversationId;

    document.getElementById("messages").innerHTML = "";
    document.getElementById("chatHeader").innerText =
        "Chat #" + conversationId;
    fetch(`/api/messages/${conversationId}`)
        .then(res => res.json())
        .then(msgs => msgs.forEach(showMessage));

    connectWebSocket(conversationId);
}

function connectWebSocket(conversationId) {
    if (stompClient) {
        stompClient.disconnect();
    }
    const socket = new SockJS("http://localhost:8080/ws");
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function () {
        stompClient.subscribe(
            `/topic/conversation/${conversationId}`,
            msg => showMessage(JSON.parse(msg.body))
        );
    });
}
function sendMessage() {
    const content = document.getElementById("messageInput").value;

    if (!content || !currentConversationId) {
        return;
    }

    stompClient.send(
        "/app/chat.send",
        {},
        JSON.stringify({
            conversationId: currentConversationId,
            senderUsername: currentUser,
            content: content
        })
    );

    document.getElementById("messageInput").value = "";
}
function showMessage(msg) {
    const messagesDiv = document.getElementById("messages");

    const div = document.createElement("div");
    div.classList.add("message");

    if (msg.sender === currentUser) {
        div.classList.add("sent");
    } else {
        div.classList.add("received");
    }
    div.innerHTML =
        "<div class='sender'>" + msg.sender + "</div>" +
        "<div>" + msg.content + "</div>";
    messagesDiv.appendChild(div);
    messagesDiv.scrollTop = messagesDiv.scrollHeight;
}
