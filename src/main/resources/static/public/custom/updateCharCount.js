function updateCharCount(textarea) {
    const count = textarea.value.length;
    const counter = textarea.parentElement.querySelector('.char-count');

    if (!counter) {
        return;
    }

    counter.textContent = count;
    counter.classList.toggle('text-danger', count >= 4500);
}

document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('textarea[oninput="updateCharCount(this)"]')
        .forEach(function (textarea) {
            updateCharCount(textarea);
        });
});
