function togglePopup() {
    const popup = document.getElementById('loginPopup');
    popup.style.display = (popup.style.display === 'block') ? 'none' : 'block';
}

window.onclick = function(event) {
    const popup = document.getElementById('loginPopup');
    if (event.target !== popup && !popup.contains(event.target) && event.target.className !== 'nav-link') {
        popup.style.display = 'none';
    }
}