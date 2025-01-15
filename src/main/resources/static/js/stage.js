function selectStage(stageId) {
    const stageContainer = document.getElementById("stage-container");
    const formContainer = document.getElementById("form-container");

    stageContainer.style.display = "none";
    formContainer.style.display = "block";

    document.getElementById("stage-id").value = stageId;
}
