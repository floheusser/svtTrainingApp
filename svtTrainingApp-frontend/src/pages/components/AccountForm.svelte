<script>
    export let data;
</script>
<div class="row gx-3 mb-3">
    <div class="col-md-6">
        <label class="small mb-1" for="inputFirstName">Vorname</label>
        <input class="form-control" id="inputFirstName" type="text" placeholder="Enter first name" bind:value="{data.firstname}">
    </div>
    <div class="col-md-6">
        <label class="small mb-1" for="inputLastName">Nachname</label>
        <input class="form-control" id="inputLastName" type="text" placeholder="Enter last name" bind:value="{data.lastname}">
    </div>
</div>
<div class="row gx-3 mb-3">
    <div class="col-md-6">
        <label class="small mb-1" for="email">E-Mail</label>
        <input class="form-control" readonly id="email" type="email" placeholder="Enter E-Mail" bind:value="{data.email}">
    </div>
    <div class="col-md-6">
        <label class="small mb-1" for="phone">Tel.</label>
        <input class="form-control" id="phone" type="text" placeholder="Enter Phonenumber" bind:value="{data.phone}">
    </div>
</div>
<div class="row gx-3 mb-3">
    <div class="col-md-6">
        <label class="small mb-1" for="street">Strasse und Hausnummer</label>
        <input class="form-control" id="street" type="text" placeholder="Enter Street" bind:value="{data.street}">
    </div>
</div>
<div class="row gx-3 mb-3">
    <div class="col-md-6">
        <label class="small mb-1" for="postcode">Postleitzahl</label>
        <input class="form-control" id="postcode" type="number" placeholder="Enter Postcode" bind:value="{data.postCode}">
    </div>
    <div class="col-md-6">
        <label class="small mb-1" for="city">Ort</label>
        <input class="form-control" id="city" type="text" placeholder="Enter City" bind:value="{data.city}">
    </div>
</div>

