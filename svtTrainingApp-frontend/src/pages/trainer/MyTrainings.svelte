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

    function createTraining() {
        let formData = new FormData();

        // Append each property of the training object to the formData
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

        // Append file to formData if it exists
        if (training.trainingContentPicture && training.trainingContentPicture.size > 0) {
            formData.append("file", training.trainingContentPicture);
        }

        var config = {
            method: "post",
            url: api_root + "/api/user/training",
            headers: {
                // Remove 'Content-Type': 'application/json', let the browser set it
                Authorization: "Bearer " + $jwt_token
            },
            data: formData,
        };

        axios(config)
            .then(function (response) {
                alert("Training erstellt!");
                window.location.reload(); // Reloading the page to reflect changes
            })
            .catch(function (error) {
                alert(error.response.data.message || "Could not create");
            });
    }

    function updateTraining(myTraining) {
    let formData = new FormData();

    // Append each property of the myTraining object to the formData
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

    // Append file to formData if it exists and is changed
    if (myTraining.trainingContentPicture && myTraining.trainingContentPicture.size > 0) {
        formData.append("file", myTraining.trainingContentPicture);
    }

    var config = {
        method: "put",
        url: api_root + "/api/user/training",
        headers: {
            // Remove 'Content-Type': 'application/json', let the browser set it
            Authorization: "Bearer " + $jwt_token
        },
        data: formData,
    };

    axios(config)
        .then(function (response) {
            alert("Training updated!");
            window.location.reload(); // Reloading the page to reflect changes
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
                responseType: 'blob' // This ensures the response is handled as a Blob
            });
            return URL.createObjectURL(response.data); // Creates a URL for the blob
        } catch (error) {
            console.error('Error fetching image:', error);
            return 'default-placeholder.png'; // A default placeholder in case of errors
        }
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
        <h4 class="text-center">Meine Trainings</h4>
        {#if myTrainings.length != 0} 
        <div class="accordion accordion-flush" id="accordionFlush">
            {#each myTrainings as myTraining}
            <div class="accordion-item">
                <h2 class="accordion-header" id="flush-heading{myTraining.id}">
                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#flush-collapse{myTraining.id}" aria-expanded="false" aria-controls="flush-collapse{myTraining.id}">
                        <div class="row">
                            <div class="col-sm">
                                <b>Datum:</b>&nbsp;{myTraining.date}
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
                                <img class="img-fluid img-thumbnail mx-auto d-block" style="max-width: 400px; max-height: 600px" src={myTraining.imageUrl} alt={`Trainingsbild für die Gruppe ${myTraining.groupName} am ${myTraining.date}`} />
                            </div>
                            <button
                                on:click={updateTraining(myTraining)}
                                class="btn btn-primary"
                                type="button">Update Training</button
                                >
                        </form>
                    </div>
                </div>
            </div>
            {/each}
        </div>
        {:else}
        <h6 class="text-center my-3 pb-3" style="color: red;">Keine Trainings erstellt</h6>
        {/if}
    </div>
</div>
