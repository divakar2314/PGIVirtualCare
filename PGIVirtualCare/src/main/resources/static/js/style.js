function toggleFileInput(show) {
    document.getElementById('fileInput').style.display = show ? 'block' : 'none';
    if (!show) {
      document.getElementById('fileUpload').value = ''; // Clear the file if No is selected
    }
  }