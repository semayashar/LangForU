function shootConfetti(x, y) {
        confetti({
            particleCount: 1500,
            startVelocity: 100,
            spread: 360,
            origin: { x: x, y: y }
        });
    }

    document.addEventListener('DOMContentLoaded', (event) => {
        setTimeout(() => {
            shootConfetti(0.1, 1);
            shootConfetti(0.9, 1);
        }, 2000);
    });