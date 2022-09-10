const url = 'http://localhost:8081';
const apiGameUrl = url + '/api/v1/animal-game';

async function playGame(){
    alert("Пожалуйста, отвечайте на вопросы в формате да/нет");
    let properties = await getAllProperties();
    guessTheAnimal(properties[0]);
}

async function guessTheAnimal(firstProperty) {
    let isAnimal = false;
    let property = firstProperty;

    while(!isAnimal){
        const answer = prompt(`Это животное ${property}?`);
        const isTypical = isTrue(answer);
        
        let assumption = await getAssumption(property, isTypical);
        
        if(assumption.isAnimal){
            const isCorrect = prompt(`Это ${assumption.value}. Я угадал?`);

            if (!isTrue(isCorrect)) {
                const realAnimal = prompt('Какое животное ты загадал?');
                const difference = prompt(`Чем ${realAnimal} отличается от ${assumption.value}?`);
                const differenceObj = {correctAnimal: realAnimal, oldProperty: property, newProperty: difference, isOldTypical: isTypical}

                sendDifference(differenceObj);
            }
            isAnimal = true;
        } else{
            property = assumption.value;
        }
    }

    playAgain(firstProperty);
};

async function getAllProperties() {
    const response = await fetch(apiGameUrl + '/properties');
    const properties = await response.json();
    return properties;
}

async function getAssumption(property, answer) {
    const body = {name: property, isTypical: answer};
    const response = await fetch(apiGameUrl + '/assumption', {
        method: 'POST',
        body: JSON.stringify(body),
        headers: {
            'Content-Type': 'application/json'
        }
    });
    const prompt = await response.json()
    return prompt;
}

async function sendDifference(difference) {
    const response = await fetch(apiGameUrl + '/difference', {
        method: 'POST',
        body: JSON.stringify(difference),
        headers: {
            'Content-Type': 'application/json'
        }
    });
}

function learn(isCorrect){
    if (!isTrue(isCorrect)) {
        const realAnimal = prompt('Какое животное ты загадал?');
        const difference = prompt(`Чем ${realAnimal} отличается от ${animal.value}?`);
        const differenceObj = {correctAnimal: realAnimal, oldProperty: property, newProperty: difference, isOldTypical: isTypical}
        
        sendDifference(differenceObj);
    }
}

function playAgain(firstProperty) {
    var playAgain = confirm('Хочешь сыграть еще раз?');
    if (playAgain) {
        guessTheAnimal(firstProperty);
    }
}

function isTrue(stringAnswer) {
    return stringAnswer === 'да' ? true : false;
}

