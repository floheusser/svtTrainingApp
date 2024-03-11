<script>
    import axios from "axios";
    import { isAuthenticated, user } from "../../store";

    const api_root = window.location.origin;
    export let params = {};
    let hairdresser_id;
    $: {
        hairdresser_id = params.id;
        getHairdresser();
        getHairdresserTasks();
    }
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
<div class="container ">
  <div class="row d-flex justify-content-center align-items-center h-100">
    <div class="col-12">
      <div class="card">
          <div class=" text-white d-flex flex-row" style="background-color: #000; height:200px;">
            <div class="ms-4 mt-5 d-flex flex-column" style="width: 150px;">
              <!-- svelte-ignore a11y-img-redundant-alt -->
              <img src="/avatar.png"
                alt="Generic placeholder image" class="img-fluid img-thumbnail mt-4 mb-2"
                style="width: 150px; z-index: 1" >
            </div>
            <div class="ms-3" style="margin-top: 130px;">
              <h5>{hairdresser.firstname} {hairdresser.lastname}</h5>
              <p>{hairdresser.city}</p>
            </div>       
          </div>
          <div class="card-body p-4 text-black">
            {#if $isAuthenticated && $user.user_roles === "customer"}
              <a role="button" type="button" class="btn btn-outline-dark float-end" href="#/appointment/{hairdresser.id}">Termin Anfragen</a>
            {/if}
              <div class="mb-3 mt-3">
              <p class="lead fw-normal mb-1">About Me</p>
              <div class="p-4" style="background-color: #f8f9fa;">
                <p class="font-italic mb-1">{hairdresser.aboutMeText}</p>
              </div>
            </div>
            <div class="mb-3">
                <p class="lead fw-normal mb-1">Dienstleisungen</p>
                <div class="p-4" style="background-color: #f8f9fa;">
                    <ul class="list-group list-group-flush">
                        {#each hairdresserTasks as hairdresserTask}
                            {#if hairdresser.hairdresserTasks.includes(hairdresserTask.id)}
                                <li class="list-group-item">{hairdresserTask.name}</li>
                            {/if}
                        {/each}
                    </ul>
                </div>
              </div>
            <!--
            <div class="d-flex justify-content-between align-items-center mb-4">
              <p class="lead fw-normal mb-0">Portfolio</p>
            </div>
            <div class="row g-2">
              <div class="col mb-2">
                <img src="https://mdbcdn.b-cdn.net/img/Photos/Lightbox/Original/img%20(112).webp"
                  alt="image 1" class="w-100 rounded-3">
              </div>
              <div class="col mb-2">
                <img src="https://mdbcdn.b-cdn.net/img/Photos/Lightbox/Original/img%20(107).webp"
                  alt="image 1" class="w-100 rounded-3">
              </div>
            </div>
            <div class="row g-2">
              <div class="col">
                <img src="https://mdbcdn.b-cdn.net/img/Photos/Lightbox/Original/img%20(108).webp"
                  alt="image 1" class="w-100 rounded-3">
              </div>
              <div class="col">
                <img src="https://mdbcdn.b-cdn.net/img/Photos/Lightbox/Original/img%20(114).webp"
                  alt="image 1" class="w-100 rounded-3">
              </div>
            </div>-->
          </div>
      </div>
    </div>
  </div>
</div>