function previewFile() {
    const preview = document.getElementById('filePreview');
    const previewContainer = document.getElementById('previewContainer');
    const file = document.getElementById('fileInput').files[0];

    if (file) {
        preview.src = URL.createObjectURL(file);
        previewContainer.classList.remove('d-none');
    } else {
        previewContainer.classList.add('d-none');
        preview.src = "";
    }
}