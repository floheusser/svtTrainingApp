<script>
    import axios from "axios";
    import { jwt_token} from "../store";
    import { querystring } from "svelte-spa-router";
    
    const api_root = window.location.origin;

    let currentPage;
    let nrOfPages = 0;
    let defaultPageSize = 4;
    let hairdressers = [];
    let hairdresserTasks = [];
    let searchHairdresserTasks;
    let city;

    $:{
      let searchParams = new URLSearchParams($querystring);
      if (searchParams.has("page")) {
        currentPage = searchParams.get("page");
      } else {
        currentPage = "1";
      }
      getHairdressers();
      getHairdresserTasks();
    }

    function getHairdressers() {
      let query = "?pageSize=" + defaultPageSize + " &pageNumber=" + currentPage;

      if (city) {
        query += "&city=" + city;
      }
      if (searchHairdresserTasks) {
        query += "&hairdresserTasks=" + searchHairdresserTasks;
      }
      console.log(query);
        var config = {
            method: "get",
            url: api_root + "/api/hairdressers"+ query,
        };

        axios(config)
            .then(function (response) {
                hairdressers = response.data.content;
                nrOfPages = response.data.totalPages;
            })
            .catch(function (error) {
                alert("Could not get hairdressers");
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

    function resetFilter() {
      location.reload();
    }
</script>

<h1 class = "mt-3">Suche Coiffeur</h1>
<div class="container mt-3">
  
    <div class="card mb-4">
      <div class="card-header">Filter</div>
      <div class="card-body">
        <div class="row">
          <div class="col-4">
            <label class="small mb-1" for="hairdresserTask">Dienstleitung auswählen</label>
            {#each hairdresserTasks as hairdresserTask}
            <div class="form-check">
                <input class = "form-check-input" type="radio" id="hairdresserTask{hairdresserTask.id}" bind:group={searchHairdresserTasks} value={hairdresserTask.id}> 
                <label class="form-check-label" for="hairdresserTask{hairdresserTask.id}">{hairdresserTask.name}</label>
            </div>
            {/each}
          </div>
          <div class="col-3">
            <label class="small mb-1" for="city">Ort eingeben</label>
            <input class="form-control" id="city" type="text" placeholder="Ort" bind:value={city} />
          </div>
        </div>
          <div class="col-12 d-flex justify-content-end">
            <button class="btn btn-primary" style="margin-right: 10px;" on:click="{resetFilter}">Reset Filter</button>
            <a class="btn btn-primary" href={"#/hairdressers?page=1&city="+ city + "&hairdresserTasks="+ searchHairdresserTasks} role="button">Find</a>
          </div>
      </div>
    </div>
  <div class="card mb-4">
    <div class="card-header">Coiffeure</div>
    <div class="card-body">
      <div class="row">
        <table class="table">
            <thead>
                <tr>
                    <th scope="col">Firstname</th>
                    <th scope="col">Lastname</th>
                    <th scope="col">Stadt</th>
                    <th scope="col">Dienstleistungen</th>
                    <th scope="col"></th>
                </tr>
            </thead>
            <tbody>
                {#each hairdressers as hairdresser}
                    <tr>
                        <td>{hairdresser.firstname}</td>
                        <td>{hairdresser.lastname}</td>
                        <td>{hairdresser.city}</td>
                        <td>
                          {#each hairdresserTasks as hairdresserTask}
                          {#if hairdresser.hairdresserTasks.includes(hairdresserTask.id)}
                            <i class="fa fa-scissors" aria-hidden="true"></i>  {hairdresserTask.name} &nbsp;
                          {/if}
                        {/each}
                        </td>
                        <td><a href= {"#/hairdresser/"+ hairdresser.id} role="button" class="btn btn-outline-dark float-end">Ansehen</a></td>
                    </tr>
                {/each}
            </tbody>
        </table>
        <nav>
          <ul class="pagination">
          {#each Array(nrOfPages) as _, i}
            <li class="page-item">
              <a class="page-link" class:active={currentPage == i + 1} href={"#/hairdressers?page=" + (i + 1)}>{i + 1} </a>
            </li>
          {/each}
          </ul>
        </nav>
      </div>
    </div>
  </div>
</div>
