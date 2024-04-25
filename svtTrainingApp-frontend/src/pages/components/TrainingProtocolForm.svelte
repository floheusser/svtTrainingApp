<script>
    export let data;
    
    import axios from "axios";
    const api_root = window.location.origin;
    import { user, jwt_token } from "../../store";

    $: {
        getGroups();
        getAllHelpTrainers();
        getAllTrainers();
    }

    let groups = [];
    let trainers = [];
    let helptrainers = [];

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

    
    function getAllTrainers() {
        var config = {
            method: "get",
            url: api_root + "/api/user/trainers",
            headers: { Authorization: "Bearer " + $jwt_token },
        };

        axios(config)
            .then(function (response) {
                trainers = response.data;
            })
            .catch(function (error) {
                console.log(error);
            });
    }

    function getAllHelpTrainers() {
        var config = {
            method: "get",
            url: api_root + "/api/user/helptrainers",
            headers: { Authorization: "Bearer " + $jwt_token },
        };

        axios(config)
            .then(function (response) {
                helptrainers = response.data;
            })
            .catch(function (error) {
                console.log(error);
            });
    }
</script>
<div class="row gx-3 mb-3">
    <div class="col-md-6">
        <label class="small mb-1" for="inputDate">Datum</label>
        <input class="form-control" id="inputDate" type="date" placeholder="Datum" bind:value="{data.date}">
    </div>
</div>
<div class="row gx-3 mb-3">
    <div class="col-md-6">
        <label class="small mb-1" for="inputTrainerName">Name Trainer</label>
        <select class="form-select" id="inputTrainerName" bind:value="{data.trainerName}">
            <option selected> </option>
            {#each trainers as trainer}
                <option value={trainer.name}>{trainer.name}</option>
            {/each}
        </select>
    </div>
    <div class="col-md-6">
        <label class="small mb-1" for="inputHelpTrainerName">Name Hilfstrainer</label>
        <select class="form-select" id="inputHelpTrainerName" bind:value="{data.helpTrainerName}">
            <option selected> </option>
            {#each helptrainers as trainer}
                <option value={trainer.name}>{trainer.name}</option>
            {/each}
        </select>
    </div>
</div>
<div class="row gx-3 mb-3">
    <div class="col-md-6">
        <label class="small mb-1" for="inputGroupName">Gruppe</label>
        <select class="form-select" id="inputGroupName" bind:value="{data.groupName}">
            <option selected> </option>
            {#each groups as group}
                <option value={group.name}>{group.name}</option>
            {/each}
        </select>
    </div>
    <div class="col-md-6">
        <label class="small mb-1" for="inputWeather">Wetter/ Wind</label>
        <input class="form-control" id="inputWeather" type="text" placeholder="Wetter/ Wind" bind:value="{data.weather}">
    </div>
</div>
<div class="row gx-3 mb-3">
    <div class="col-md-6">
        <label class="small mb-1" for="inputTrainingContent">Trainingsinhalt</label>
        <textarea class="form-control" id="inputTrainingContent" type="text" placeholder="Trainingsablauf/ Übungen" bind:value="{data.trainingContent}" />
    </div>
    <div class="col-md-6">
        <label class="small mb-1" for="inputTrainingContentPicture">Trainingsinhalt (Bild)</label>
        <br/>
        <input type="file" accept="image/*" id="inputTrainingContentPicture" on:change="{(event) => data.trainingContentPicture = event.target.files[0]}" />
    </div>
</div>


