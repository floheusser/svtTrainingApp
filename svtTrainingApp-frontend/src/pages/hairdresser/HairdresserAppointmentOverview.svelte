<script>
    import axios from "axios";
    import { jwt_token } from "../../store";
    import MyAppointmentAccordion from "../components/HairdresserMyAppointmentAccordion.svelte";

    const api_root = window.location.origin;

    $: {
        getRequestedAppointmentsByUserEmail();
        getAcceptedAppointmentsByUserEmail();
        getCustomers();
        getHairdresserTasks();
    }

    let myRequestedAppointments = [];
    let myApprovedAppointments = [];
    let customers = [];
    let hairdresserTasks = [];

    function getRequestedAppointmentsByUserEmail() {

        var config = {
            method: "get",
            url: api_root + "/api/appointment/myRequestedAppointments",
            headers: {Authorization: "Bearer "+$jwt_token},
        };

        axios(config)
            .then(function (response) {
                myRequestedAppointments = response.data;
            })
            .catch(function (error) {
                alert("Could not get myRequestedAppointments");
                console.log(error);
            });

    }
    
    function getAcceptedAppointmentsByUserEmail() {

        var config = {
            method: "get",
            url: api_root + "/api/appointment/myApprovedAppointments",
            headers: {Authorization: "Bearer "+$jwt_token},
        };

        axios(config)
            .then(function (response) {
                myApprovedAppointments = response.data;
            })
            .catch(function (error) {
                alert("Could not get myApprovedAppointments");
                console.log(error);
            });

    }

    function getCustomers() {

        var config = {
            method: "get",
            url: api_root + "/api/customers",
            headers: {Authorization: "Bearer "+$jwt_token},
        };

        axios(config)
            .then(function (response) {
                customers = response.data;
            })
            .catch(function (error) {
                alert("Could not get customers");
                console.log(error);
            });
    }

    function getHairdresserTasks() {
        var config = {
            method: "get",
            url: api_root + "/api/hairdresserTasks",
            headers: {Authorization: "Bearer "+$jwt_token},
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
</script>

    <section class="mt-5">
        <div class="container">
            <div class="row d-flex justify-content-center">
                <div class="col-12">
                    <div class="card rounded-3">
                        <div class="card-body p-4">
                            <h4 class="text-center my-3 pb-3">Meine Terminanfragen</h4>
                            {#if myRequestedAppointments.length != 0} 
                            <div class="accordion accordion-flush" id="accordionFlush">
                            {#each myRequestedAppointments as appointment}
                                <MyAppointmentAccordion appointment={appointment} customers={customers} hairdresserTasks={hairdresserTasks} listTypeRequest={true}/>
                            {/each}      
                            </div> 
                            {:else}
                            <h6 class="text-center my-3 pb-3" style="color: red;">Keine Terminanfragen</h6>
                            {/if} 
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>


    <section class="mt-5">
        <div class="container ">
            <div class="row d-flex justify-content-center ">
                <div class="col-12">
                    <div class="card rounded-3">
                        <div class="card-body p-4">
                            <h4 class="text-center my-3 pb-3">Meine Termine</h4>
                            {#if myApprovedAppointments.length != 0} 
                            <div class="accordion accordion-flush" id="accordionFlush">
                            {#each myApprovedAppointments as appointment}
                                <MyAppointmentAccordion appointment={appointment} customers={customers} hairdresserTasks={hairdresserTasks} listTypeRequest={false}/>
                            {/each}
                            </div>
                            {:else}
                            <h6 class="text-center my-3 pb-3" style="color: red;">Keine bevorstehenden Termine</h6>
                            {/if}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
