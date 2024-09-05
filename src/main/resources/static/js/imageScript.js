document.addEventListener('DOMContentLoaded', function() {
    // Example Lection data - this should come from your server or API
    var lection = {
        name: "Example Lection",
        theme: "Theme of the Lection",
        videoUrl: "https://example.com/path/to/your/video-thumbnail.jpg",
        difficultyLevel: "Intermediate",
        viewCount: 1234,
        instructor: "John Doe",
        additionalResources: "Extra resources link"
    };

    // Set dynamic content
    document.getElementById('lection-name').textContent = lection.name;
    document.getElementById('lection-theme').textContent = lection.theme;
    document.getElementById('lection-instructor').textContent = lection.instructor;
    document.getElementById('lection-difficulty').textContent = lection.difficultyLevel;
    document.getElementById('lection-view-count').textContent = lection.viewCount;
    document.getElementById('lection-resources').textContent = lection.additionalResources;

    // Set background image
    var rightContent = document.getElementById('right-content');
    rightContent.style.backgroundImage = 'url(' + lection.videoUrl + ')';

    // Set video link
    var videoLink = document.getElementById('video-link');
    videoLink.href = lection.videoUrl;
});
