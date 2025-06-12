document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('quizForm');
    const customAlert = document.getElementById('customAlert');
    const resultText = document.getElementById('resultText');

    form.addEventListener('submit', function (event) {
        event.preventDefault();

        const formData = new FormData(form);


        document.querySelectorAll('.incorrect').forEach(el => {
            el.textContent = '';
            el.style.display = 'none';
        });


        customAlert.style.display = 'none';


        fetch('/lections/submit', {
            method: 'POST',
            body: formData,
            headers: { 'Accept': 'application/json' }
        })
            .then(response => {
                if (!response.ok) throw new Error('Мрежовият отговор не беше успешен');
                return response.json();
            })
            .then(data => {
                let correctAnswers = 0;

                data.questions.forEach(question => {
                    if (question.answeredCorrectly) {
                        correctAnswers++;
                    } else {

                        const errorElement = document.getElementById(`error_${question.id}`);
                        if (errorElement) {
                            errorElement.textContent = 'Неправилен отговор';
                            errorElement.style.display = 'block';
                        }
                    }
                });


                resultText.textContent = `Вие отговорихте правилно на ${correctAnswers} от ${data.questions.length} въпроса.`;
                customAlert.style.display = 'block';
            })
            .catch(error => {
                console.error('Грешка:', error);
                resultText.textContent = 'Имаше проблем при обработката на заявката ви. Моля, опитайте отново.';
                customAlert.style.display = 'block';
            });
    });
});
