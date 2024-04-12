<script>
    export let data;
    export let edit;

    const trainerValues = [
		{ label: 'Trainer', value: 'trainer' },
		{ label: 'Hilfstrainer', value: 'helptrainer' }
	];
    $: {
        console.log(edit);
    }
</script>
<div class="row gx-3 mb-3">
    <div class="col-md-6">
        <label class="small mb-1" for="inputName">Vor-/Nachname</label>
        <input class="form-control" id="inputName" type="text" placeholder="Vor-/Nachname" bind:value="{data.name}" disabled={edit}>
    </div>
    <div class="col-md-6">
        <label class="small mb-1" for="email">E-Mail</label>
        <input class="form-control" disabled id="email" type="email" placeholder="Enter E-Mail" bind:value="{data.email}">
    </div>
</div>
<div class="row gx-3 mb-3">
    <div class="col-md-6">
        {#each trainerValues as value}
        <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" id="type{value.value}" bind:group={data.type} value="{value.value}" disabled="{edit}">
            <label class="form-check-label" for="type{value.value}">{value.label}</label>
        </div>
        {/each}
    </div>
</div>

