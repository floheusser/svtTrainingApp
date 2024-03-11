<script>
    import axios from "axios";
    import { user, jwt_token } from "../../store";
    import { push, pop } from "svelte-spa-router";

    const api_root = window.location.origin;
    export let params = {};
    let hairdresser_id;
    let todaymin = new Date().toISOString().substring(0, 10);

    $: {
        hairdresser_id = params.id;
        getHairdresser();
        getHairdresserTasks();
    }
    let appointment = {
        hairdresserId: "",
        customerEmail: "",
        date: "",
        timeHour: "",
        timeMinute: "",
        hairdresserTasks: [],
    };
    let hairdresser = {
        id: "",
        firstname: "",
        lastname: "",
        nickname: "",
        phone: "",
        email: "",
        city: "",
        street: "",
        postCode: "",
        aboutMeText: "",
        hairdresserTasks: [],
    };
    let hairdresserTasks = [];

    function getHairdresser() {
        var config = {
            method: "get",
            url: api_root + "/api/hairdresser/" + hairdresser_id,
            headers: { Authorization: "Bearer " + $jwt_token },
        };

        axios(config)
            .then(function (response) {
                hairdresser = response.data;
            })
            .catch(function (error) {
                alert("Could not get hairdresser");
                console.log(error);
            });
    }

    function getHairdresserTasks() {
        var config = {
            method: "get",
            url: api_root + "/api/hairdresserTasks",
            headers: { Authorization: "Bearer " + $jwt_token },
        };

        axios(config)
            .then(function (response) {
                hairdresserTasks = response.data;
            })
            .catch(function (error) {
                alert("Could not get tasks");
                console.log(error);
            });
    }

    function requestAppointment() {
        appointment.hairdresserId = hairdresser_id;
        appointment.customerEmail = $user.email;
        var config = {
            method: "post",
            url: api_root + "/api/appointment",
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer " + $jwt_token,
            },
            data: appointment,
        };
        axios(config)
            .then(function (response) {
                appointment = response.data;
                alert("Termin angefragt");
            })
            .catch(function (error) {
                alert("Could not create appointment");
                console.log(error);
            });
        push("/hairdresser/" + hairdresser_id);
    }
</script>

<div class="container-xl px-4 mt-4">
    <div class="row justify-content-center">
        <div class="col-xl-5">
            <div class="card mb-4">
                <div class="card-header">Terminanfrage</div>
                <div class="card-body">
                    <form>
                        <div class="row gx-3 mb-3">
                            <label  for="hairdresserTask">Dienstleitungen auswählen</label>
                            {#each hairdresserTasks as hairdresserTask}
                                {#if hairdresser.hairdresserTasks.includes(hairdresserTask.id)}
                                    <div class="form-check form-switch">
                                    <input class = "form-check-input" id="hairdresserTask{hairdresserTask.id}" type=checkbox bind:group={appointment.hairdresserTasks} value={hairdresserTask.id}> 
                                    <label class="form-check-label" for="hairdresserTask{hairdresserTask.id}">{hairdresserTask.name}: <b>{hairdresserTask.price} .- CHF</b></label>
                                </div>
                                {/if}
                            {/each}
                        </div>
                        <div class="row gx-3 mb-3">
                            <div class="col-md-6">
                                <label for="appointmentDate">Datum</label>
                                <input bind:value={appointment.date} class="form-control" id="appointmentDate" type="date" placeholder="dd-mm-yyyy"  min="{todaymin}"/>
                            </div>
                            <div class="col-md-6">
                                    <label for="time">Zeit:</label>
                                    <input bind:value={appointment.time} class="form-control" id="time" type="time" />
                            </div>
                        </div>
                        <hr />
                        <button on:click={() => pop()} class="btn btn-danger" type="button" >Abbrechen</button>
                        <button on:click={requestAppointment} class="btn btn-success" type="button" >Anfragen</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
