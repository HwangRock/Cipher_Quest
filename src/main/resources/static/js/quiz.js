// 제한 시간 설정
const totalSeconds = 3600; // 1시간 = 3600초
let remainingSeconds = totalSeconds;

// URL에서 stageId 파라미터 추출
const urlParams = new URLSearchParams(window.location.search);
const stageId = urlParams.get("stageId");

if (!stageId) {
    alert("스테이지 ID가 지정되지 않았습니다!");
    throw new Error("Missing stageId in URL");
}

// 타이머 포맷 함수
function formatTime(seconds) {
    const minutes = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
}

// 타이머 시작 함수
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

// 암호문 로드 함수
function loadCipherText(stageId) {
    const cipherDataElement = document.getElementById("cipher-data");
    fetch(`/api/stage/${stageId}/cipher`)
        .then((response) => {
            if (!response.ok) {
                throw new Error("서버 응답 오류");
            }
            return response.json();
        })
        .then((data) => {
            if (data.encryptedText) {
                cipherDataElement.textContent = data.encryptedText;
                console.log("암호문:", data.encryptedText);
            } else {
                cipherDataElement.textContent = data.message || "암호문을 찾을 수 없습니다.";
                console.error("API 메시지:", data.message || "알 수 없는 오류");
            }
        })
        .catch((error) => {
            console.error("암호문 로드 실패:", error);
            cipherDataElement.textContent = "암호문 로드 실패";
        });
}

// 제출 및 검증 함수
async function handleFormSubmit(event) {
    event.preventDefault(); // 폼의 기본 제출 동작 막기

    const urlParams = new URLSearchParams(window.location.search);
    const stageId = urlParams.get("stageId");

    if (!stageId) {
        alert("스테이지 ID가 지정되지 않았습니다!");
        throw new Error("Missing stageId in URL");
    }

    const submitAnswer = document.getElementById("submit-answer").value.trim();
    const resultElement = document.getElementById("result");

    if (!submitAnswer) {
        alert("해독 결과를 입력해주세요!");
        return;
    }

    try {
        // 1. 제출 데이터를 서버에 저장
        console.log("제출 데이터 전송...");
        const submitResponse = await fetch(`/api/stage/${stageId}/submit`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                stageId: stageId,
                submitDecryptText: submitAnswer, // 올바른 필드명으로 수정
                submitPlainText: null,          // 필요하지 않을 경우 null
                key: null,                      // 필요하지 않을 경우 null
                nextStageId: null,              // 필요하지 않을 경우 null
            }),
        });

        if (!submitResponse.ok) {
            const errorData = await submitResponse.json();
            throw new Error(errorData.error || "제출 실패");
        }

        console.log("제출 성공. 검증 시작...");

        // 2. 검증 API 호출
        const verifyResponse = await fetch(`/api/stage/${stageId}/verify`, {
            method: "GET",
        });

        if (!verifyResponse.ok) {
            const errorData = await verifyResponse.json();
            throw new Error(errorData.error || "검증 실패");
        }

        const verifyData = await verifyResponse.json();
        const message = verifyData.data ? verifyData.data[0] : "결과를 불러올 수 없습니다.";
        resultElement.textContent = message;
        resultElement.style.color = message.includes("정답") ? "green" : "red";

    } catch (error) {
        console.error("제출 및 검증 오류:", error);
        resultElement.textContent = "서버와 통신 중 오류가 발생했습니다.";
        resultElement.style.color = "red";
    }
}



// 페이지 로드 시 실행
// 페이지 로드 시 실행
window.onload = () => {
    if (stageId) {
        startTimer(); // 타이머 시작
        loadCipherText(stageId); // 암호문 로드

        // 폼 제출 이벤트 리스너 등록
        const form = document.getElementById("quiz-form");
        form.addEventListener("submit", handleFormSubmit);

        // 버튼 클릭 이벤트 리스너 등록
        const submitButton = document.getElementById("submit-verify-btn");
        submitButton.addEventListener("click", handleFormSubmit);
    }
};

