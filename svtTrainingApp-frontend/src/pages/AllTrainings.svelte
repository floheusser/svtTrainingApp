<script>
    import axios from "axios";
    const api_root = window.location.origin;
    import { jwt_token } from "../store";
    import TrainingProtocolForm from "./components/TrainingProtocolForm.svelte";

    let allTrainings = [];

    $: {
        getAllTrainings();
    }

    function getAllTrainings() {
        var config = {
            method: "get",
            url: api_root + "/api/trainings",
            headers: { Authorization: "Bearer " + $jwt_token },
        };

        axios(config)
            .then(function (response) {
                allTrainings = response.data;
            })
            .catch(function (error) {
                console.log(error);
            });
    }
</script>

<div class="container-xl px-4 mt-4">
    <div class="row">
        <h4 class="text-center">Alle Trainings</h4>
        {#if allTrainings.length != 0} 
        <div class="accordion accordion-flush" id="accordionFlush">
            {#each allTrainings as training}
            <div class="accordion-item">
                <h2 class="accordion-header" id="flush-heading{training.id}">
                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#flush-collapse{training.id}" aria-expanded="false" aria-controls="flush-collapse{training.id}">
                         {training.date} {training.trainerName}
                    </button>
                </h2>
                <div id="flush-collapse{training.id}" class="accordion-collapse collapse" aria-labelledby="flush-heading{training.id}" data-bs-parent="#accordionFlush">
                    <div class="accordion-body" style="background-color: white;">
                        <form>
                            <TrainingProtocolForm data={training} readOnly={true}/>
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