<script>
    import axios from "axios";
    const api_root = window.location.origin;
    import { user, jwt_token } from "../../store";

    $: {
        getGroups();
    }

    let missingTrainingsRequest = false;
    let fromDate = "";
    let toDate = "";
    let groupName = "";
    let seasonYear = "";
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
            url: api_root + "/api/trainingsMissing/"+groupName+"/"+fromDate+"/"+toDate,
            headers: { Authorization: "Bearer " + $jwt_token },
        };
        console.log(config.url);
        axios(config)
            .then(function (response) {
                missingTrainings = response.data;
                missingTrainingsRequest = true;
                setTimeout(() => window.scrollTo(0, document.body.scrollHeight), 300);
            })
            .catch(function (error) {
                console.log(error);
            });
    }

    function downloadPDF(seasonYear) {
        const config = {
            method: 'get',
            url: api_root + '/api/trainings/pdf/' + seasonYear,
            headers: { Authorization: "Bearer " + $jwt_token },
            responseType: 'blob', 
        };

        axios(config)
            .then((response) => {
                const url = window.URL.createObjectURL(new Blob([response.data]));
                const link = document.createElement('a');
                link.href = url;
                link.setAttribute('download', seasonYear+'_Trainingprotokolle.pdf.pdf');
                document.body.appendChild(link);
                link.click();
                link.parentNode.removeChild(link); 
            })
            .catch((error) => console.log(error));
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
                <div class="card-header">Download Trainingsprotokolle</div>
                <div class="card-body">
                    <div class="row gx-3 mb-3">
                        <div class="alert alert-info" role="alert">
                        <small class="text-muted">Verwenden Sie diese Funktion, um alle Trainingsprotokolle eines bestimmten Jahres heruterzuladen. Falls Sie alle Trainingsprotokolle wollen schreiben Sie "Alle" in das Feld "Saison Jahr" 
                        </small>
                        </div>
                    </div>
                    <div class="row gx-3 mb-3">
                        <div class="col-6">
                            <label class="small mb-1" for="seasonYear">Saison Jahr</label>
                            <input class="form-control" id="seasonYear" type="text" placeholder="z.B 2024" bind:value="{seasonYear}">
                        </div>
                        <div class="col-6 mt-4">
                            <button
                                on:click={downloadPDF(seasonYear)}
                                class="btn btn-primary h-100"
                                type="submit">
                                <i class="fa-solid fa-download"></i>&nbsp;&nbsp;Download</button
                            >
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-xl">
            <div class="card mb-4">
                <div class="card-header">Fehlende Trainingsprotokolle</div>
                <div class="card-body">
                    <div class="row gx-3 mb-3">
                        <div class="alert alert-info" role="alert">
                        <small class="text-muted">Verwenden Sie diese Funktion, um die fehlenden Trainingsprotokolle für eine bestimmte Gruppe in einem bestimmten Zeitraum (Von-Bis) anzuzeigen. 
                        </small>
                        </div>
                    </div>
                    <form>
                        <div class="row gx-3 mb-3">
                            <div class="col-md-6">
                                <label class="small mb-1" for="fromDate">Von</label>
                                <input class="form-control" id="fromDate" type="date" bind:value="{fromDate}">
                            </div>
                            <div class="col-md-6">
                                <label class="small mb-1" for="toDate">Bis</label>
                                <input class="form-control" id="toDate" type="date" bind:value="{toDate}">
                            </div>
                        <div class="row gx-3 mb-3">
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
                    </div>                        
                        <button
                            on:click={getMissingTrainings}
                            class="btn btn-primary"
                            type="submit">Fehlende Protokolle</button
                        >
                    </form>
                </div>
            </div>
        </div>
    </div>
    {#if missingTrainings.length > 0 && missingTrainingsRequest}
        <div class="row mb-3">
            <h5>Folgend die Daten an denen für die Gruppe "{groupName}" noch kein Trainingsprotokoll erstellt wurde:</h5>
        </div>
        {:else if missingTrainingsRequest}
        <div class="row mb-3">
            <div class="alert alert-success" role="alert">
                <h5>Vom {formatDate(fromDate)} bis zum {formatDate(toDate)} wurden alle Trainingsprotokolle der Gruppe "{groupName}" erstellt!</h5>
            </div>
        </div>
    {/if}
    <div class="row">
        <ul class="list-group list-group-flush">
        {#each missingTrainings as date}
            <li class="list-group-item">{formatDate(date)}</li>
        {/each}
        </ul>
    </div>
</div>
