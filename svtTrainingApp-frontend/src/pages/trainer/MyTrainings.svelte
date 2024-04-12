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
        trainingContentPicture: ""
    }

    let myTrainings = [];

    function createTraining() {
        var config = {
            method: "post",
            url: api_root + "/api/user/training",
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer "+$jwt_token
            },
            data: training,
        };
        axios(config)
            .then(function (response) {
                training = response.data;
                getMyTrainings();
            })
            .catch(function (error) {
                alert(error.response.data.message || "Could not create");
            });
    }

    function updateTraining(myTraining) {
        var config = {
            method: "put",
            url: api_root + "/api/user/training",
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer "+$jwt_token
            },
            data: myTraining,
        };
        axios(config)
            .then(function (response) {
                myTraining = response.data;
                getMyTrainings();
            })
            .catch(function (error) {
                alert(error.response.data.message || "Could not create");
            });
    }


    function getMyTrainings() {
        var config = {
            method: "get",
            url: api_root + "/api/user/myTrainings",
            headers: { Authorization: "Bearer " + $jwt_token },
        };

        axios(config)
            .then(function (response) {
                myTrainings = response.data;
            })
            .catch(function (error) {
                console.log(error);
            });
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
                            type="button">Training erstellen</button
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
                         <b>Gruppe:</b>&nbsp;{myTraining.groupName}&nbsp;<b>Datum:</b>&nbsp; {myTraining.date}
                    </button>
                </h2>
                <div id="flush-collapse{myTraining.id}" class="accordion-collapse collapse" aria-labelledby="flush-heading{myTraining.id}" data-bs-parent="#accordionFlush">
                    <div class="accordion-body" style="background-color: white;">
                        <form>
                            <TrainingProtocolForm data={myTraining} />
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
