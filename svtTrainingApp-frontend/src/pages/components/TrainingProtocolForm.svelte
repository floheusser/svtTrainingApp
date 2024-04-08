<script>
    export let data;
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
        <input class="form-control" id="inputTrainerName" type="text" placeholder="Vor-/ Nachname" bind:value="{data.trainerName}">
    </div>
    <div class="col-md-6">
        <label class="small mb-1" for="inputHelpTrainerName">Name Hilfstrainer</label>
        <input class="form-control" id="inputHelpTrainerName" type="text" placeholder="Vor-/ Nachname" bind:value="{data.helpTrainerName}">
    </div>
</div>
<div class="row gx-3 mb-3">
    <div class="col-md-6">
        <label class="small mb-1" for="inputGroupName">Gruppe</label>
        <input class="form-control" id="inputGroupName" type="text" placeholder="Gruppenname" bind:value="{data.groupName}">
    </div>
    <div class="col-md-6">
        <label class="small mb-1" for="inputWeather">Wetter</label>
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
        <input type="file" class="form-control-file" id="inputTrainingContentPicture"  bind:value="{data.trainingContentPicture}">
    </div>
</div>


