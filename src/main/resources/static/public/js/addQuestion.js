let questionIndex = 0;

const radioHandlers = new Map();

// Global object to track event handlers
const _eventHandlers = {};

// Function to add event listener and track it in _eventHandlers
const addListener = (node, event, handler, capture = false) => {
    if (!(event in _eventHandlers)) {
        _eventHandlers[event] = [];
    }
    const existingHandler = _eventHandlers[event].find(entry => entry.node === node && entry.handler === handler);
    if (!existingHandler) {
        _eventHandlers[event].push({ node: node, handler: handler, capture: capture });
        node.addEventListener(event, handler, capture);
    }
};

// Function to remove all listener for a given event type from a specific node
function removeAllListener(targetNode, event, specificHandler = null) {
    const handlers = _eventHandlers[event];
    if (handlers) {
        handlers
            .filter(({ node, handler }) => node === targetNode && (!specificHandler || handler === specificHandler))
            .forEach(({ node, handler, capture }) => node.removeEventListener(event, handler, capture));

        _eventHandlers[event] = handlers.filter(({ node, handler }) => !(node === targetNode && (!specificHandler || handler === specificHandler)));
    }
}

function addNewQuestion() {
    const template = document.getElementById("questionTemplate");
    if (!template) {
        console.error("Template nicht gefunden!");
        return; // Wenn das Template nicht gefunden wird, abbrechen
    }

    const currentIndex = questionIndex;

    const newQuestion = template.cloneNode(true);
    newQuestion.style.display = "block";

    newQuestion.querySelectorAll('[id]').forEach(function (el) {
        let newId = el.id.replace("{{questionIndex}}", currentIndex);
        el.id = newId;
        console.log("Neue ID gesetzt für Element: ", newId);
    });

    newQuestion.querySelectorAll('label[for]').forEach(function (el) {
        let newFor = el.getAttribute('for').replace("{{questionIndex}}", currentIndex.toString());
        el.setAttribute('for', newFor);
        console.log("Neues 'for' Attribut gesetzt für label: ", newFor);
    });

    newQuestion.querySelectorAll('textarea[id]').forEach(function (el) {
        let newId = el.id.replace("{{questionIndex}}", currentIndex);
        el.id = newId;  // Hier die 'id' des Textareas anpassen
        console.log("Neue ID gesetzt für textarea: ", newId);
    });

    newQuestion.querySelectorAll('input[id]').forEach(function (el) {
        let newId = el.id.replace("{{questionIndex}}", currentIndex);
        el.id = newId;
        console.log("Neue ID gesetzt für input: ", newId);
    });

    newQuestion.querySelectorAll('input[name], textarea[name]').forEach(function (el) {
        let newName = el.getAttribute('name').replace("{{questionIndex}}", currentIndex.toString());
        el.setAttribute('name', newName);
        console.log("Neue ID gesetzt für name: ", newName);
    });

    newQuestion.querySelectorAll(`input[type="radio"]`).forEach((radio) => {
        radio.name = "questionType_" + currentIndex;
    });

    newQuestion.querySelectorAll('input[type="radio"]').forEach((radio) => {
        radio.name = "questionType_" + currentIndex;
        const handler = createRadioHandler(radio, currentIndex);
        radioHandlers.set(radio, handler);
        addListener(radio, "click", handler); // Add listener using the new approach
        console.log("Add Event at:" + currentIndex);
    });

    questionIndex++;

    document.querySelector("#examForm").appendChild(newQuestion);

    updateQuestionButtons();
}

function removeQuestion(button) {
    const question = button.closest('.question-block');
    question.remove();

    questionIndex--;

    updateQuestionButtons();
}

function toggleQuestionFields(radio, localQuestionIndex) {
    console.log("toggleQuestionFields aufgerufen mit:", radio.value, localQuestionIndex);

    let choicesContainer = document.getElementById('choicesContainer_' + localQuestionIndex);
    let correctAnswerSC = document.getElementById('correctAnswer_SC_' + localQuestionIndex);
    let correctAnswersMC = document.getElementById('correctAnswers_MC_' + localQuestionIndex);
    let frageText = document.getElementById('freitext_question_' + localQuestionIndex);
    let scmcQuestion = document.getElementById('SCMC-question_' + localQuestionIndex);

    console.log("Gefundene Elemente:", {
        choicesContainer,
        correctAnswerSC,
        correctAnswersMC,
        frageText,
        scmcQuestion
    });

    choicesContainer.style.display = 'none';
    correctAnswerSC.style.display = 'none';
    correctAnswersMC.style.display = 'none';
    frageText.style.display = 'none';
    scmcQuestion.style.display = 'none';

    if (radio.value === 'SC') {
        // SC spezifische Felder anzeigen
        scmcQuestion.style.display = 'block';
        choicesContainer.style.display = 'block';
        correctAnswerSC.style.display = 'block';
    } else if (radio.value === 'MC') {
        // Zeige das MC Feld
        scmcQuestion.style.display = 'block';
        choicesContainer.style.display = 'block';
        correctAnswersMC.style.display = 'block';
    } else if (radio.value === 'FREITEXT') {
        // Zeige Freitext Felder
        frageText.style.display = 'block';
    }
}

function updateQuestionButtons() {
    const questions = document.querySelectorAll('.question-block');
    const removeButtons = document.querySelectorAll('.remove-question');

    // Entferne den "remove"-Button von allen Fragen, bevor er neu positioniert wird
    removeButtons.forEach(button => {
        button.style.display = "none"; // Setze alle entfernen Buttons auf unsichtbar
    });

    // Wenn Fragen existieren, dann zeige den Button nur für die letzte Frage
    if (questions.length > 0) {
        const lastQuestion = questions[questions.length - 1];
        const removeButton = lastQuestion.querySelector('.remove-question');
        if (removeButton) {
            removeButton.style.display = "inline-block";  // "remove"-Button nur für die letzte Frage anzeigen
        }
    }
}

function handleRadioClick(radio, currentIndex) {
    console.log("Radio-Klick für Frage " + currentIndex);
    toggleQuestionFields(radio, currentIndex);
}

function createRadioHandler(radio, currentIndex) {
    return function handleClick() {
        handleRadioClick(radio, currentIndex);
    }
}

function extractExamData() {
    const questions = document.querySelectorAll('.question-block');
    const data = [];

    questions.forEach((questionBlock, index) => {
        const questionObj = {
            index: index,
            type: null,
            questionText: null,
            maxPunkte: null,
            choices: [],
            correctAnswer: null,
            correctAnswers: [],
            freeTextAnswer: null
        };

        // Frage-Text extrahieren (wenn vorhanden)
        const freitextEl = questionBlock.querySelector(`#freitext_question_${index}`);
        if (freitextEl && freitextEl.style.display !== 'none') {
            questionObj.questionText = freitextEl.querySelector('textarea')?.value || '';

            const punkteValue = freitextEl.querySelector(`#maxPunkte_${index}`)?.value || '';
            questionObj.maxPunkte = punkteValue ? parseInt(punkteValue, 10) : 0; // Fallback auf 0, falls kein Wert vorhanden
        } else {
            // SC/MC Fragetext
            const scmcEl = questionBlock.querySelector(`#SCMC-question_${index}`);
            const punkte = questionBlock.querySelector(`#maxPunkteSCMC_${index}`);
            if (scmcEl) {
                questionObj.questionText = scmcEl.querySelector('textarea')?.value || '';

                // Punktzahl für SC/MC-Frage extrahieren
                const punkteValue = scmcEl.querySelector(`#maxPunkteSCMC_${index}`)?.value || '';
                questionObj.maxPunkte = punkteValue ? parseInt(punkteValue, 10) : 0; // Fallback auf 0, falls kein Wert vorhanden
            }
        }

        // Frage-Typ bestimmen
        const selectedRadio = questionBlock.querySelector(`input[name="questionType_${index}"]:checked`);
        if (selectedRadio) {
            questionObj.type = selectedRadio.value;
        }

        // Antwortmöglichkeiten extrahieren (für SC und MC)
        const choicesContainer = questionBlock.querySelector(`#choicesContainer_${index}`);
        if (choicesContainer && choicesContainer.style.display !== 'none') {
            const choices = choicesContainer.querySelectorAll('input[type="text"]');
            choices.forEach(input => {
                questionObj.choices.push(input.value);
            });
        }

        // Richtige Antwort für SC
        const correctSC = questionBlock.querySelector(`#correctAnswer_SC_${index}`);
        if (correctSC && correctSC.style.display !== 'none') {
            const selected = correctSC.querySelector('select')?.value;
            questionObj.correctAnswer = selected;
        }

        // Richtige Antworten für MC (Mehrfachauswahl)
        const correctMC = questionBlock.querySelector(`#correctAnswers_MC_${index}`);
        if (correctMC && correctMC.style.display !== 'none') {
            const checkboxes = correctMC.querySelectorAll('input[type="checkbox"]');
            checkboxes.forEach((checkbox, i) => {
                if (checkbox.checked) {
                    questionObj.correctAnswers.push(i); // oder checkbox.value
                }
            });
        }

        // Freitext-Antwort (wenn Nutzer was geschrieben hat)
        if (freitextEl && freitextEl.style.display !== 'none') {
            const answer = freitextEl.querySelector('textarea')?.value || '';
            questionObj.freeTextAnswer = answer;
        }

        data.push(questionObj);
    });

    return data;
}

function submitExamForm() {
    // Extrahieren der Fragen-Daten
    const formData = extractExamData();

    // Setzen der extrahierten Daten in das versteckte Feld
    document.getElementById('examData').value = JSON.stringify(formData);

    // Formular abschicken
    document.getElementById('examForm').submit();
}