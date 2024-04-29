<script>
    import axios from "axios";
    const api_root = window.location.origin;
    import { user, jwt_token } from "../../store";

    $: {
        getGroups();
    }

    let startdate = "";
    let groupName = "";
    let groups = [];
    let missingTrainings = [];

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

    function getMissingTrainings() {
        var config = {
            method: "get",
            url: api_root + "/api/trainingsMissing/"+groupName+"/"+startdate,
            headers: { Authorization: "Bearer " + $jwt_token },
        };
        console.log(config.url);
        axios(config)
            .then(function (response) {
                missingTrainings = response.data;
            })
            .catch(function (error) {
                console.log(error);
            });
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
                <div class="card-header">Admin</div>
                <div class="card-body">
                    <form>
                        <div class="row gx-3 mb-3">
                            <div class="col-md-6">
                                <label class="small mb-1" for="startdate">Datum von:</label>
                                <input class="form-control" id="startdate" type="date" bind:value="{startdate}">
                            </div>
                            <div class="col-md-6">
                                <label class="small mb-1" for="groupName">Gruppe</label>
                                <select class="form-select" id="groupName" bind:value="{groupName}">
                                    <option selected> </option>
                                    {#each groups as group}
                                        <option value={group.name}>{group.name}</option>
                                    {/each}
                                </select>
                            </div>
                        </div>                        
                        <button
                            on:click={getMissingTrainings}
                            class="btn btn-primary"
                            type="submit">Missing Protocols</button
                        >
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <ul class="list-group list-group-flush">
        {#each missingTrainings as date}
            <li class="list-group-item">{formatDate(date)}</li>
        {/each}
        </ul>
    </div>
</div>
