document.addEventListener("DOMContentLoaded", () => {
    console.log("JavaScript 로드 완료");

    const stageContainer = document.getElementById("stage-container");
    const formContainer = document.getElementById("form-container");
    const stageIdInput = document.getElementById("stage-id");
    const encryptButton = document.getElementById("encrypt-button");
    const loadingMessage = document.getElementById("loading");

    if (!stageContainer || !formContainer || !encryptButton) {
        console.error("필수 DOM 요소를 찾을 수 없습니다.");
        return;
    }

    // 스테이지 선택 버튼 클릭 시 호출
    window.selectStage = (stageId) => {
        console.log(`스테이지 선택됨: ${stageId}`);
        stageContainer.style.display = "none";
        formContainer.style.display = "block";
        stageIdInput.value = stageId; // 선택된 스테이지 ID 저장
    };

    // 암호화 버튼 클릭 이벤트
    encryptButton.addEventListener("click", async () => {
        console.log("암호화 버튼 클릭됨");

        const stageId = stageIdInput.value;
        const plainText = document.getElementById("plain-text").value;
        const key = document.getElementById("key").value;

        if (!plainText || !key) {
            alert("평문과 키를 모두 입력해주세요.");
            console.error("평문 또는 키가 누락되었습니다.");
            return;
        }

        // 로딩 메시지 표시
        loadingMessage.style.display = "block";

        try {
            console.log(`암호화 요청 시작 (Stage ID: ${stageId}, PlainText: ${plainText}, Key: ${key})`);

            const response = await fetch(`/api/stage/${stageId}/encrypt`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    submitPlainText: plainText,
                    key: key,
                }),
            });

            if (!response.ok) {
                const errorData = await response.json();
                console.error("API 오류 응답:", errorData);
                alert(`암호화 실패: ${errorData.error}`);
                return;
            }

            const data = await response.json();
            console.log("암호화 성공:", data);

            // 성공 시 다음 페이지로 이동
            window.location.href = `/quiz?stageId=${stageId}`;
        } catch (error) {
            console.error("통신 오류:", error);
            alert("서버와 통신 중 오류가 발생했습니다.");
        } finally {
            // 로딩 메시지 숨기기
            loadingMessage.style.display = "none";
        }
    });
});
