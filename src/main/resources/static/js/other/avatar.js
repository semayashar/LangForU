 document.addEventListener("DOMContentLoaded", function () {
        const avatarModal = document.getElementById("avatarModal");
        const openAvatarModal = document.getElementById("openAvatarModal");
        const closeAvatarModal = document.getElementById("closeAvatarModal");
        const avatarList = document.getElementById("avatarList");
        const saveAvatar = document.getElementById("saveAvatar");
        let selectedAvatar = null;

        openAvatarModal.addEventListener("click", () => {
            avatarModal.classList.add("show");
            loadAvatars();
        });

        closeAvatarModal.addEventListener("click", () => {
            avatarModal.classList.remove("show");
        });

        function loadAvatars() {
            const gender = "female";
            fetch(`/avatars/${gender}`)
                .then(response => response.json())
                .then(avatars => {
                    avatarList.innerHTML = "";
                    avatars.forEach(avatar => {
                        const avatarElem = document.createElement("img");
                        avatarElem.src = `/img/avatars/${gender}/${avatar}`;
                        avatarElem.classList.add("avatar-item");
                        avatarElem.addEventListener("click", () => selectAvatar(avatarElem));
                        avatarList.appendChild(avatarElem);
                    });
                })
                .catch(error => console.error('Error loading avatars:', error));
        }

        function selectAvatar(element) {
            document.querySelectorAll(".avatar-item").forEach(item => item.classList.remove("selected"));
            element.classList.add("selected");
            selectedAvatar = element.src;
        }

        saveAvatar.addEventListener("click", () => {
            if (selectedAvatar) {
                fetch("/user/save-avatar", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ profilePicture: selectedAvatar })
                }).then(response => {
                    if (response.ok) {
                        alert("Аватарът е успешно запазен!");
                        avatarModal.classList.remove("show");
                    } else {
                        alert("Грешка при запазването на аватара.");
                    }
                });
            } else {
                alert("Моля, изберете аватар!");
            }
        });
    });