const bookContainer = document.getElementById('book-container');
const loadingSpinner = document.getElementById('loading');
const errorBox = document.getElementById('error');
const successBox = document.getElementById('add-success');

function fetchBooks() {
    loadingSpinner.style.display = 'block';
    bookContainer.innerHTML = '';
    bookContainer.style.display = 'none';
    errorBox.style.display = 'none';

    fetch(`${BACKEND_URL}/api/v1/books`)
        .then(resp => {
            if (!resp.ok) throw new Error("Failed to load");
            return resp.json();
        })
        .then(books => {
            loadingSpinner.style.display = 'none';
            bookContainer.style.display = 'flex';
            books.forEach(book => {
                const card = document.createElement('div');
                card.className = 'col';
                card.innerHTML = `
                        <div class="card h-100 shadow-sm">
                            <div class="card-body">
                                <h5 class="card-title">${book.name}</h5>
                                <p class="card-text text-muted">${i18n.bookBy} ${book.author}</p>
                            </div>
                        </div>
                    `;
                bookContainer.appendChild(card);
            });
        })
        .catch(err => {
            console.error(err);
            loadingSpinner.style.display = 'none';
            errorBox.style.display = 'block';
        });
}

document.getElementById('add-book-form').addEventListener('submit', function (e) {
    e.preventDefault();
    const name = document.getElementById('book-name').value.trim();
    const author = document.getElementById('book-author').value.trim();

    fetch(`${BACKEND_URL}/api/v1/books`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({name, author})
    })
        .then(resp => {
            if (resp.status === 201) return resp.json();
            else throw new Error("Invalid input");
        })
        .then(newBook => {
            document.getElementById('book-name').value = '';
            document.getElementById('book-author').value = '';
            successBox.style.display = 'block';
            setTimeout(() => successBox.style.display = 'none', 2000);
            fetchBooks();

            // Subscribe to SSE stream
            const evtSource = new EventSource(`${BACKEND_URL}/api/books/updates`);
            evtSource.onmessage = function (e) {
                const toastBody = document.getElementById('sseToastBody');

                // If your server sends JSON: parse it, else just use e.data directly
                let msg;
                try {
                    const update = JSON.parse(e.data);
                    msg = `Book "${update.bookId}" is ready! Status: ${update.status}`;
                } catch {
                    msg = e.data;
                }

                toastBody.textContent = msg;

                // Show toast using Bootstrap API
                const toastElement = document.getElementById('sseToast');
                const toast = new bootstrap.Toast(toastElement, {delay: 5000}); // auto hide after 5s
                toast.show();

                // Optionally close SSE connection if it is a one-shot
                evtSource.close();
            };
            evtSource.onerror = function (e) {
                console.log("SSE connection closed or interrupted");
                evtSource.close();
            };
        })
        .catch(err => {
            console.error(err);
            alert("Failed to add book: " + err.message);
        });
});

// Refresh books when Available Books tab is shown
document.getElementById('list-tab').addEventListener('shown.bs.tab', fetchBooks);

// Initial load
fetchBooks();

