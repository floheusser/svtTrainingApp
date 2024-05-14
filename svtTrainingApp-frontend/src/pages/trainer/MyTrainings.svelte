<script>
    import axios from "axios";
    const api_root = window.location.origin;
    import TrainingProtocolForm from "../components/TrainingProtocolForm.svelte";
    import { user, jwt_token } from "../../store";
    import { each } from "svelte/internal";

    $: {
        getMyTrainings();
    }

    let training = {
        id: "",
        trainerName: "",
        helpTrainerName: "",
        groupName: "",
        date: "",
        weather: "",
        trainingContent: "",
        trainingContentPicture: {}
    }

    let myTrainings = [];
    let newTrainingId = null;

    function createTraining() {
        let formData = new FormData();

        formData.append("trainingData", new Blob([JSON.stringify({
            id: training.id,
            trainerName: training.trainerName,
            helpTrainerName: training.helpTrainerName,
            groupName: training.groupName,
            date: training.date,
            weather: training.weather,
            trainingContent: training.trainingContent,
        })], {
            type: "application/json"
        }));

        if (training.trainingContentPicture && training.trainingContentPicture.size > 0) {
            formData.append("file", training.trainingContentPicture);
        }

        var config = {
            method: "post",
            url: api_root + "/api/user/training",
            headers: {
                Authorization: "Bearer " + $jwt_token
            },
            data: formData,
        };

        axios(config)
            .then(function (response) {
                alert("Training erstellt!");
                newTrainingId = response.data.id; 
                getMyTrainings().then(() => {
                    openAndScrollToNewTraining();
                });
            })
            .catch(function (error) {
                alert(error.response.data.message || "Could not create");
            });
    }

    function openAndScrollToNewTraining() {
        if (newTrainingId) {
            const accordionButton = document.querySelector(`#flush-heading${newTrainingId} .accordion-button`);
            if (accordionButton) {
                accordionButton.click();
                accordionButton.scrollIntoView({ behavior: 'smooth' });
            }
        }
    }

    function updateTraining(myTraining) {
    let formData = new FormData();

    formData.append("trainingData", new Blob([JSON.stringify({
        id: myTraining.id,
        trainerName: myTraining.trainerName,
        helpTrainerName: myTraining.helpTrainerName,
        groupName: myTraining.groupName,
        date: myTraining.date,
        weather: myTraining.weather,
        trainingContent: myTraining.trainingContent,
    })], {
        type: "application/json"
    }));

    if (myTraining.trainingContentPicture && myTraining.trainingContentPicture.size > 0) {
        formData.append("file", myTraining.trainingContentPicture);
    }

    var config = {
        method: "put",
        url: api_root + "/api/user/training",
        headers: {
            Authorization: "Bearer " + $jwt_token
        },
        data: formData,
    };

    axios(config)
        .then(function (response) {
            alert("Training updated!");
            window.location.reload(); 
        })
        .catch(function (error) {
            alert(error.response.data.message || "Could not update");
        });
}


    async function getMyTrainings() {
        const config = {
            method: 'get',
            url: `${api_root}/api/user/myTrainings`,
            headers: { Authorization: `Bearer ${$jwt_token}` },
        };

        try {
            const response = await axios(config);
            myTrainings = response.data;
            myTrainings = await Promise.all(myTrainings.map(async training => {
                return {
                    ...training,
                    imageUrl: await fetchTrainingImage(training.id)
                };
            }));
        } catch (error) {
            console.error('Error fetching trainings:', error);
        }
    }

    async function fetchTrainingImage(trainingId) {
        try {
            const response = await axios.get(`${api_root}/api/user/training/${trainingId}/image`, {
                headers: { Authorization: `Bearer ${$jwt_token}` },
                responseType: 'blob'  
            });
            if (response.data.size > 0) {
                return URL.createObjectURL(response.data);
            } else {
                return 'Kein Bild hochgeladen';
            }
        } catch (error) {
            console.error('Error fetching image:', error);
            return 'Kein Bild hochgeladen';
        }
    }

    function deleteTraining(myTraining) {
    if (confirm(`Sind Sie sicher, dass Sie dieses Trainingprotokoll löschen möchten?`)) {
        var config = {
            method: "delete",
            url: api_root + "/api/user/training/" + myTraining.id,
            headers: { Authorization: "Bearer " + $jwt_token },
        };

        axios(config)
            .then(function (response) {
                window.location.reload();
            })
            .catch(function (error) {
                console.error(error);
                alert("Das Trainingprotokoll konnte nicht gelöscht werden.");
            });
    } 
}
    
    function formatDate(dateStr) {
        const date = new Date(dateStr);
        return `${date.getDate().toString().padStart(2, '0')}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getFullYear()}`;
    }
</script>

<div class="container mt-4">
    <div class="row">
        <div class="col-xl">
            <div class="card mb-4">
                <div class="card-header">Neues Training</div>
                <div class="card-body">
                    <form>
                        <TrainingProtocolForm data={training} />
                        <button
                            on:click={createTraining}
                            class="btn btn-success"
                            type="submit">Training erstellen</button
                        >
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <h4 class="text-center">Meine erstellten Trainingsprotokolle</h4>
        {#if myTrainings.length != 0} 
        <div class="accordion accordion-flush" id="accordionFlush">
            {#each myTrainings as myTraining}
            <div class="accordion-item">
                <h2 class="accordion-header" id="flush-heading{myTraining.id}">
                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#flush-collapse{myTraining.id}" aria-expanded="false" aria-controls="flush-collapse{myTraining.id}">
                        <div class="row">
                            <div class="col-sm">
                                <b>Datum:</b>&nbsp;{formatDate(myTraining.date)}
                            </div>
                            <div class="col-sm">
                                <b>Gruppe:</b>&nbsp;{myTraining.groupName}
                            </div>
                        </div>
                    </button>
                </h2>
                <div id="flush-collapse{myTraining.id}" class="accordion-collapse collapse" aria-labelledby="flush-heading{myTraining.id}" data-bs-parent="#accordionFlush">
                    <div class="accordion-body" style="background-color: white;">
                        <form>
                            <TrainingProtocolForm data={myTraining} />
                            <div class="row mt-5 mb-5">
                                {#if myTraining.imageUrl != 'Kein Bild hochgeladen'}
                                    <img class="img-fluid img-thumbnail mx-auto d-block" style="max-width: 400px; max-height: 600px" src={myTraining.imageUrl} alt={`Trainingsbild für die Gruppe ${myTraining.groupName} am ${myTraining.date}`} />
                                {:else}
                                <div class="alert alert-info text-center" role="alert">
                                    <small class="text-muted">Kein Bild vom Trainingsinhalt vorhanden</small>
                                </div>
                                {/if}
                            </div>
                            <div class="row">
                                <div class="col-6">
                                    <button
                                        on:click={updateTraining(myTraining)}
                                        class="btn btn-primary"
                                        type="button">Update Training</button
                                        >
                                </div>
                                <div class="col-6">
                                    <button
                                        on:click={deleteTraining(myTraining)}
                                        class="btn btn-danger float-end"
                                        type="button">Training Löschen &nbsp;&nbsp;<i class="fa-solid fa-trash-can"></i></button
                                        >
                                </div>
                            </div>
                        </form>
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
