<script>
    export let data;
    export let readOnly;
</script>
<div class="row gx-3 mb-3">
    <div class="col-md-6">
        <label class="small mb-1" for="inputDate">Datum</label>
        <input class="form-control" id="inputDate" type="date" placeholder="Datum" bind:value="{data.date}" readonly={readOnly}>
    </div>
</div>
<div class="row gx-3 mb-3">
    <div class="col-md-6">
        <label class="small mb-1" for="inputTrainerName">Name Trainer</label>
        <input class="form-control" id="inputTrainerName" type="text" placeholder="Vor-/ Nachname" bind:value="{data.trainerName}" readonly={readOnly}>
    </div>
    <div class="col-md-6">
        <label class="small mb-1" for="inputHelpTrainerName">Name Hilfstrainer</label>
        <input class="form-control" id="inputHelpTrainerName" type="text" placeholder="Vor-/ Nachname" bind:value="{data.helpTrainerName}" readonly={readOnly}>
    </div>
</div>
<div class="row gx-3 mb-3">
    <div class="col-md-6">
        <label class="small mb-1" for="inputGroupName">Gruppe</label>
        <input class="form-control" id="inputGroupName" type="text" placeholder="Gruppenname" bind:value="{data.groupName}" readonly={readOnly}>
    </div>
    <div class="col-md-6">
        <label class="small mb-1" for="inputWeather">Wetter</label>
        <input class="form-control" id="inputWeather" type="text" placeholder="Wetter/ Wind" bind:value="{data.weather}" readonly={readOnly}>
    </div>
</div>
<div class="row gx-3 mb-3">
    <div class="col-md-6">
        <label class="small mb-1" for="inputTrainingContent">Trainingsinhalt</label>
        <textarea class="form-control" id="inputTrainingContent" type="text" placeholder="Trainingsablauf/ Übungen" bind:value="{data.trainingContent}" readonly={readOnly} />
    </div>
    {#if !readOnly}
    <div class="col-md-6">
        <label class="small mb-1" for="inputTrainingContentPicture">Trainingsinhalt (Bild)</label>
        <input type="file" class="form-control-file" id="inputTrainingContentPicture"  bind:value="{data.trainingContentPicture}" >
    </div>
    {/if}
</div>


