// 제한 시간 카운트다운 (초 단위로 설정)
const totalSeconds = 3600; // 1시간 = 3600초
let remainingSeconds = totalSeconds;

function formatTime(seconds) {
    const minutes = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
}

function startTimer() {
    const timerElement = document.getElementById("timer");
    timerElement.innerText = `제한 시간: ${formatTime(remainingSeconds)}`;

    const timerInterval = setInterval(() => {
        remainingSeconds--;
        timerElement.innerText = `제한 시간: ${formatTime(remainingSeconds)}`;

        if (remainingSeconds <= 0) {
            clearInterval(timerInterval);
            alert("제한 시간이 종료되었습니다!");
            timerElement.innerText = "제한 시간: 00:00";
        }
    }, 1000);
}

// 타이머 시작
window.onload = startTimer;
