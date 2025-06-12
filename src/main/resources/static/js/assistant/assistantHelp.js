function getAssistantHelp(questionId) {
    const questionElement = document.getElementById(`question_${questionId}`);
    const question = questionElement ? questionElement.innerText.trim() : "Няма наличен въпрос.";

    const answerInputs = Array.from(document.querySelectorAll(`input[name="question_${questionId}"]`));
    const isMultipleChoice = answerInputs.length > 0;

    const lectionName = document.querySelector('input[name="lectionId"]')?.value || "Неизвестна лекция";
    const themeName = document.querySelector('h2')?.innerText || "Неизвестна тема";

    let sentence = "";

    if (isMultipleChoice) {
        const answers = answerInputs.map(input => {
            const labelText = input.closest(".switch-wrap")?.querySelector("p")?.innerText;
            return labelText || input.value;
        }).filter(Boolean);

        const selectedInput = answerInputs.find(input => input.checked);
        const userAnswer = selectedInput ? selectedInput.value : null;

        if (userAnswer) {
            sentence = `Въпрос с избираем отговор: "${question}". Потребителят е избрал: "${userAnswer}". Налични отговори: "${answers.join(', ')}". Лекция: "${lectionName}", тема: "${themeName}". Анализирай дали избраният отговор е верен. Ако е, обясни защо. Ако е грешен, уточни кой е правилният и какво означават останалите отговори.`;
        } else {
            sentence = `Въпрос с избираем отговор: "${question}". Потребителят не е избрал отговор. Налични опции: "${answers.join(', ')}". Лекция: "${lectionName}", тема: "${themeName}". Посочи правилния отговор, аргументирай избора и обясни значението на останалите отговори.`;
        }
    } else {
        const input = document.querySelector(`input[name="question_${questionId}"]`);
        const userInput = input ? input.value.trim() : "";

        if (userInput.length > 0) {
            sentence = `Въпрос със свободен отговор: "${question}". Въведен отговор: "${userInput}". Лекция: "${lectionName}", тема: "${themeName}". Прецени дали отговорът е правилен. Ако е верен – обясни защо. Ако е грешен – посочи какъв трябва да бъде правилният, какви елементи липсват или са неправилни, и при какви обстоятелства (ако има такива) може да се използва въведеният отговор.`;
        } else {
            sentence = `Въпрос със свободен отговор: "${question}". Отговор не е въведен. Лекция: "${lectionName}", тема: "${themeName}". Опиши как трябва да бъде изграден валиден отговор на този въпрос. Включи примерен отговор и уточни какви компоненти са задължителни.`;
        }
    }

    const responseBox = document.getElementById(`response_${questionId}`);
    const responseText = responseBox.querySelector('.sevi-response-text');

    responseBox.style.display = 'block';
    responseText.innerText = 'Моля, изчакайте...';

    fetch('/chat/question-help', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ question: sentence })
    })
    .then(response => response.json())
    .then(data => {
        let cleanReply = data.reply || '';


        cleanReply = cleanReply.replace(/```[a-z]*\n?/gi, '').replace(/```/g, '');


        cleanReply = cleanReply.replace(/^###\s*(.*)$/gm, '**$1**');


        cleanReply = cleanReply
            .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
            .replace(/\*(.*?)\*/g, '<em>$1</em>')
            .replace(/__(.*?)__/g, '<u>$1</u>')
            .replace(/\n/g, '<br>');

        responseText.innerHTML = cleanReply;
    })
    .catch(error => {
        console.error('Грешка:', error);
        responseText.innerText = 'Възникна грешка при извличането на помощта.';
    });
}

function closeAssistantHelp(questionId) {
    const responseBox = document.getElementById(`response_${questionId}`);
    if (responseBox) {
        responseBox.style.display = 'none';
    }
}
