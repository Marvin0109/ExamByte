function updateCharCount(textarea) {
    const count = textarea.value.length;
    const counter = textarea.nextElementSibling.querySelector('.char-count');

    counter.textContent = count;

    counter.classList.toggle('text-danger', count >= 4500);
}