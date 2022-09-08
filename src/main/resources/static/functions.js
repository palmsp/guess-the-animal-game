const url = 'http://localhost:8081';
const apiGameUrl = url + '/api/v1/animal-game';

async function guessTheAnimal() {
    let animalProperties = new Array();

    let properties = await getAllProperties();

    for (const property of properties) {
        const isTypicalStr = prompt(`Это животное ${property}?`);

        const animalProperty = new AnimalProperty(property, isTrue(isTypicalStr));
        animalProperties.push(animalProperty);

        let animal = await guessedAnimal(animalProperties);

        if (animal.name !== 'unknown') {
            const isRightStr = prompt(`Это ${animal.name}. Я угадал?`);

            if (!isTrue(isRightStr)) {
                const realAnimal = prompt('Какое животное ты загадал?');
                const difference = prompt(`Чем ${realAnimal} отличается от ${animal.name}:`);
                animalProperties.push(new AnimalProperty(difference, true));
                sendRealAnimal(realAnimal, animalProperties);
                sendDifference(difference);
            }
            break;
        }
    }

    playAgain();
};

async function getAllProperties() {
    const response = await fetch(apiGameUrl + '/properties');
    const properties = await response.json();
    return properties;
}

async function guessedAnimal(animalProperties) {
    const response = await fetch(apiGameUrl + '/guessedAnimal', {
        method: 'POST',
        body: JSON.stringify(animalProperties),
        headers: {
            'Content-Type': 'application/json'
        }
    });
    const animal = await response.json()
    return animal;
}

async function sendRealAnimal(animalName, animalProperties) {
    const body = {name: animalName, properties: animalProperties};
    const response = await fetch(apiGameUrl + '/realAnimal', {
        method: 'POST',
        body: JSON.stringify(body),
        headers: {
            'Content-Type': 'application/json'
        }
    });
}

async function sendDifference(difference) {
    const response = await fetch(apiGameUrl + '/difference', {
        method: 'POST',
        body: difference,
        headers: {
            'Content-Type': 'application/json'
        }
    });
}

function playAgain() {
    var playAgain = confirm('Хочешь сыграть еще раз?');
    if (playAgain) {
        guessTheAnimal();
    }
}

function AnimalProperty(name, typical) {
    this.name = name;
    this.typical = typical;
}

function isTrue(stringAnswer) {
    return stringAnswer === 'да' ? true : false;
}

