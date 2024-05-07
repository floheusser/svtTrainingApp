<script>
    import axios from "axios";
    const api_root = window.location.origin;
    import { jwt_token } from "../store";

    let allTrainings = [];
    let groups = [];
    let groupName = "all";

    $:{
        getAllTrainings();
    }

    async function getAllTrainings() {
        const config = {
            method: "get",
            url: `${api_root}/api/trainings`,
            headers: { Authorization: `Bearer ${$jwt_token}` },
        };

        try {
            const response = await axios(config);
            allTrainings = await Promise.all(response.data.map(async training => ({
                ...training,
                imageUrl: await fetchTrainingImage(training.id)
            })));
        } catch (error) {
            console.error('Error fetching trainings:', error);
        }
    }

    async function getTrainingsByGroupName() {
        const config = {
            method: "get",
            url: `${api_root}/api/trainings/`+ groupName,
            headers: { Authorization: `Bearer ${$jwt_token}` },
        };

        try {
            const response = await axios(config);
            allTrainings = await Promise.all(response.data.map(async training => ({
                ...training,
                imageUrl: await fetchTrainingImage(training.id)
            })));
        } catch (error) {
            console.error('Error fetching trainings:', error);
        }
    }

    function getGroups() {
        var config = {
            method: "get",
            url: api_root + "/api/groups",
            headers: { Authorization: "Bearer " + $jwt_token },
        };

        axios(config)
            .then(function (response) {
                groups = response.data;
            })
            .catch(function (error) {
                console.log(error);
            });
    }

    async function fetchTrainingImage(trainingId) {
        try {
            const response = await axios.get(`${api_root}/api/user/training/${trainingId}/image`, {
                headers: { Authorization: `Bearer ${$jwt_token}` },
                responseType: 'blob'  // This ensures the response is handled as a Blob
            });
            return URL.createObjectURL(response.data); // Creates a URL for the blob
        } catch (error) {
            console.error('Error fetching image:', error);
            return 'default-placeholder.png'; // A default placeholder in case of errors
        }
    }

    function formatDate(dateStr) {
        const date = new Date(dateStr);
        return `${date.getDate().toString().padStart(2, '0')}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getFullYear()}`;
    }

    getGroups();
</script>

<div class="container-xl px-4 mt-4">
    <h4 class="text-center">Alle Trainingsprotokolle</h4>
    <div class="row mt-3 justify-content-center">
        <div class="col-8">
            <select class="form-select" id="inputGroupName" bind:value="{groupName}" on:change="{getTrainingsByGroupName}" >
                <option selected value = "all">Alle Trainings</option>
                {#each groups as group}
                    <option value={group.name}>{group.name}</option>
                {/each}
            </select>
        </div>
    </div>    
    <div class="row mt-3">
        {#if allTrainings.length != 0} 
        <div class="accordion accordion-flush" id="accordionFlush">
            {#each allTrainings as training}
            <div class="accordion-item">
                <h2 class="accordion-header" id="flush-heading{training.id}">
                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#flush-collapse{training.id}" aria-expanded="false" aria-controls="flush-collapse{training.id}">
                        <div class="row">
                            <div class="col-sm">
                                <b>Datum:</b>&nbsp;{formatDate(training.date)}
                            </div>
                            <div class="col-sm">
                                <b>Gruppe:</b>&nbsp;{training.groupName}
                            </div>
                        </div>
                    </button>
                </h2>
                <div id="flush-collapse{training.id}" class="accordion-collapse collapse" aria-labelledby="flush-heading{training.id}" data-bs-parent="#accordionFlush">
                    <div class="accordion-body" style="background-color: white;">
                        <div class="row gx-3 mb-3">
                            <div class="col-md-6">
                                Datum: {formatDate(training.date)}
                            </div>
                        </div>
                       <div class="row gx-3 mb-3">
                            <div class="col-md-6">
                                Trainer: {training.trainerName}
                            </div>
                            <div class="col-md-6">
                                Hilfstrainer: {training.helpTrainerName}
                            </div>
                        </div>
                        <div class="row gx-3 mb-3">
                            <div class="col-md-6">
                                Gruppe: {training.groupName}
                            </div>
                            <div class="col-md-6">
                                Wetter/ Wind: {training.weather}
                            </div>
                        </div>
                        {#if training.trainingContent != ""}
                        <div class="row gx-3 mb-3">
                            <div class="col-md-6">
                                Trainingsinhalt: {training.trainingContent}
                            </div>
                        </div>
                        {/if}
                        
                        <div class="row gx-3 mb-3">
                            <img class="img-fluid img-thumbnail mx-auto d-block" style="max-width: 400px; max-height: 600px" src={training.imageUrl} alt={`Trainingsbild für die Gruppe ${training.groupName} am ${training.date}`} />
                        </div>
                    </div>
                </div>
            </div>
            {/each}
        </div>
        {:else}
        <h6 class="text-center my-3 pb-3" style="color: red;">Keine Trainingsprotokolle vorhanden</h6>
        {/if}
    </div>
</div>        