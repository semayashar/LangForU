$(document).ready(function () {
    // Load previous chat messages, if applicable
    loadChatHistory();

    // Handle form submission
    $('#chat-form').submit(function (event) {
        event.preventDefault();

        const userInput = $('#userInput').val().trim();
        if (!userInput) return;

        // Append user message
        appendMessage('user-message', userInput, userAvatar || '/img/avatars/user.png');

        // Clear input and disable UI
        $('#userInput').val('').prop('disabled', true);
        $('button[type="submit"]').prop('disabled', true);

        // Show loader
        appendLoader();

        // Send message to backend
        $.ajax({
            url: '/chat/send',
            type: 'POST',
            data: { userInput: userInput },
            success: function (response) {
                removeLoader();
                const formatted = formatMessage(response);
                appendMessage('assistant-message', formatted, '/img/Sevi/Sevi - L.png');
            },
            error: function () {
                removeLoader();
                appendMessage('assistant-message', '⚠️ Грешка при връзката със сървъра.', '/img/Sevi/Sevi - L.png');
            },
            complete: function () {
                $('#userInput').prop('disabled', false);
                $('button[type="submit"]').prop('disabled', false);
            }
        });
    });

    // New chat button handler
    $('#new-chat-btn').click(function () {
        $('#chat-box').empty();
        $('#userInput').prop('disabled', false);
        $('button[type="submit"]').prop('disabled', false);
        appendMessage('assistant-message', 'Севи - Виртуална Асистентка е на линия. Започнете нов разговор!', '/img/Sevi/Sevi - L.png');
    });
});

/**
 * Format assistant message with markdown-like formatting.
 */
function formatMessage(text) {
    if (!text) return '';
    return text
        .replace(/```[a-z]*\n?/gi, '')
        .replace(/```/g, '')
        .replace(/^###\s*(.*)$/gm, '<strong>$1</strong>')
        .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
        .replace(/\*(.*?)\*/g, '<em>$1</em>')
        .replace(/__(.*?)__/g, '<u>$1</u>')
        .replace(/\n/g, '<br>');
}

/**
 * Append a chat message to the chat box.
 */
function appendMessage(className, message, avatarUrl) {
    const html = `
        <div class="chat-message ${className}">
            <img src="${avatarUrl}" alt="Avatar" class="chat-avatar" />
            <div class="chat-bubble">${message}</div>
        </div>`;
    $('#chat-box').append(html).scrollTop($('#chat-box')[0].scrollHeight);
}

/**
 * Add typing loader bubble.
 */
function appendLoader() {
    const loaderHtml = `
        <div id="loading-dots" class="chat-message assistant-message">
            <img src="/img/Sevi/Sevi - L.png" alt="Assistant Avatar" class="chat-avatar" />
            <div class="chat-bubble">
                <div class="loader"></div>
            </div>
        </div>`;
    $('#chat-box').append(loaderHtml).scrollTop($('#chat-box')[0].scrollHeight);
}

/**
 * Remove typing loader.
 */
function removeLoader() {
    $('#loading-dots').remove();
}

/**
 * Load chat history - implement based on your app logic.
 */
function loadChatHistory() {
    // Optional: Load messages from session or database
}
