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
    event.preventDefault();

    const submitAnswer = document.getElementById("submit-answer").value.trim();
    const resultElement = document.getElementById("result");

    if (!submitAnswer) {
        alert("해독 결과를 입력해주세요!");
        return;
    }

    try {
        console.log("제출 데이터 전송...");
        const submitResponse = await fetch(`/api/stage/${stageId}/submit`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                stageId: stageId,
                submitDecryptText: submitAnswer,
                submitPlainText: null,
                key: null,
                nextStageId: null,
            }),
        });

        if (!submitResponse.ok) {
            const errorData = await submitResponse.json();
            throw new Error(errorData.error || "제출 실패");
        }

        console.log("제출 성공. 검증 시작...");
        const verifyResponse = await fetch(`/api/stage/${stageId}/verify`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        });

        if (!verifyResponse.ok) {
            const errorData = await verifyResponse.json();
            throw new Error(errorData.error || "검증 실패");
        }

        const verifyData = await verifyResponse.json(); // 전체 JSON 데이터 가져오기
        console.log("검증 결과:", verifyData); // 응답 데이터 확인

        const { correct, message } = verifyData;
        console.log("검증 결과:", verifyData);

        if (correct) {
            const popup = document.getElementById("correct-popup");
            console.log("팝업 요소:", popup);
            popup.style.display = "flex";
        } else {
            resultElement.textContent = message;
            resultElement.style.color = "red";
        }
    } catch (error) {
        console.error("제출 및 검증 오류:", error);
        resultElement.textContent = "서버와 통신 중 오류가 발생했습니다.";
        resultElement.style.color = "red";
    }

}

// 팝업 닫기 이벤트 등록
function initializePopupEvents() {
    const stageSelectBtn = document.getElementById("stage-select-btn");
    const popup = document.getElementById("correct-popup");

    if (stageSelectBtn && popup) {
        stageSelectBtn.addEventListener("click", () => {
            console.log("팝업 닫기 버튼 클릭");
            popup.style.display = "none"; // 팝업 숨기기
        });
    } else {
        console.error("팝업 또는 버튼 요소가 없습니다!");
    }
}

// 페이지 로드 시 실행
window.onload = () => {
    if (stageId) {
        startTimer(); // 타이머 시작
        loadCipherText(stageId); // 암호문 로드
        initializePopupEvents(); // 팝업 이벤트 초기화

        // 제출 버튼 클릭 이벤트 등록
        const submitButton = document.getElementById("submit-verify-btn");
        submitButton.addEventListener("click", handleFormSubmit);
    }
};
