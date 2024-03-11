<script>
    export let data;
    export let hairdresserTasks;
</script>
<div class="row gx-3 mb-3">
    <div class="col-md-12">
        <label class="small mb-1" for="hairdresserTask">Dienstleitungen auswählen</label>
        {#each hairdresserTasks as hairdresserTask}
        <div class="form-check form-switch">
            <input class = "form-check-input" id="hairdresserTask{hairdresserTask.id}"type=checkbox bind:group={data.hairdresserTasks} value={hairdresserTask.id}> 
            <label class="form-check-label" for="hairdresserTask{hairdresserTask.id}">{hairdresserTask.name}</label>
        </div>
        {/each}
    </div>
</div>
<div class="row gx-3 mb-3">
    <div class="col-md-12">
        <label class="small mb-1" for="aboutMeText">Schreibe einen Text über dich</label>
        <textarea  class="form-control" id="aboutMeText"  placeholder="Enter Text about you" bind:value="{data.aboutMeText}" />
    </div>
</div>