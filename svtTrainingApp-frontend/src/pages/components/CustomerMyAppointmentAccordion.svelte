<script>
    import axios from "axios";
    import { jwt_token } from "../../store";

    const api_root = window.location.origin;

    export let appointment;
    export let hairdressers;
    export let hairdresserTasks;
    export let listTypeRequest;

    let appointmentStateChange = {
        appointmentId: "",
        state: ""
    };

    
    function updateAppointmentState(appointmentId, state) {
        appointmentStateChange.appointmentId = appointmentId;
        appointmentStateChange.state = state;
        var config = {
            method: "put",
            url: api_root + "/api/appointment/updateAppointmentState",
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer "+$jwt_token
            },
            data: appointmentStateChange,
        };
        axios(config)
            .then(function (response) {
                appointmentStateChange = response.data;
                alert("Appointment State: " + state);
            })
            .catch(function (error) {
                alert("Could not update");
                console.log(error);
            });
            location.reload();
    }

    function formatPrice(price) {
        return price.toFixed(2);
    }

</script>
<div class="accordion-item">
    <h2 class="accordion-header" id="flush-heading{appointment.id}">
        <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#flush-collapse{appointment.id}" aria-expanded="false" aria-controls="flush-collapse{appointment.id}">
            {#each hairdressers as hairdresser}
            {#if hairdresser.id === appointment.hairdresserId}
                {#if listTypeRequest}
                    Anfrage für: {hairdresser.firstname} {hairdresser.lastname}
                {:else}
                    Termin von: {hairdresser.firstname} {hairdresser.lastname} am {appointment.date} um {appointment.time} Uhr
                {/if}
            {/if}
            {/each}
        </button>
    </h2>
    <div id="flush-collapse{appointment.id}" class="accordion-collapse collapse" aria-labelledby="flush-heading{appointment.id}" data-bs-parent="#accordionFlush">
        <div class="accordion-body">
            <ul class="list-group list-group-flush">
                <li class="list-group-item">Datum: {appointment.date}</li>
                <li class="list-group-item">Zeit: {appointment.time} </li>
                <li class= "list-group-item">
                    {#each hairdressers as hairdresser}
                    {#if hairdresser.id === appointment.hairdresserId}
                        Kontakt: Tel.<a href="tel:{hairdresser.phone}">{hairdresser.phone}</a> Email:<a href="mailto:{hairdresser.email}">{hairdresser.email}</a>
                    {/if}
                    {/each}
                </li>
                <li class="list-group-item">
                    Dienstleistungen: 
                    <ul>
                        {#each hairdresserTasks as hairdresserTask}
                            {#if appointment.hairdresserTasks.includes(hairdresserTask.id)}
                                <li >{hairdresserTask.name}</li>
                            {/if}
                        {/each}
                    </ul>
                </li>
                <li class="list-group-item">
                    Preis: {formatPrice(appointment.customerPrice)} CHF
                </li>
                {#if listTypeRequest}
                <li class="list-group-item">
                    <button on:click={updateAppointmentState(appointment.id,"DECLINED")} type="button" class="btn btn-danger float-end m-2"><i
                        class="fa fa-trash"></button>
                </li>
                {/if}
            </ul>
        </div>
    </div>
</div>