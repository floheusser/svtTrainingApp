<script>
    import axios from "axios";
    const api_root = window.location.origin;
    import AccountForm from "./components/AccountForm.svelte";
    import { user, jwt_token } from "../store";
    import HairdresserPortfolioForm from "./components/HairdresserPortfolioForm.svelte";

    $:{
        if($user.user_roles == "hairdresser") {
            person = hairdresser;
        } else {
            person = customer;
        }
    }
    let person = {};

    let customer = {
        id: "",
        firstname: "",
        lastname: "",
        nickname: "",
        phone: "",
        email: $user.email,
        city: "",
        street: "",
        postCode: "",
    };

    let hairdresser = {
        id: "",
        firstname: "",
        lastname: "",
        nickname: "",
        phone: "",
        email: $user.email,
        city: "",
        street: "",
        postCode: "",
        aboutMeText: "",
        hairdresserTasks: [],
    };


    let hairdresserTasks = []; 

    function getModelByUserEmail() {

        var config = {
            method: "get",
            url: api_root + "/api/" + $user.user_roles + "/account",
            headers: {Authorization: "Bearer "+$jwt_token},
        };

        axios(config)
            .then(function (response) {
                person = response.data;
                console.log(person);
            })
            .catch(function (error) {
                alert("Could not get user");
                console.log(error);
            });
    }

    function updatePerson() {
        var config = {
            method: "put",
            url: api_root + "/api/" + $user.user_roles + "/account/update",
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer "+$jwt_token
            },
            data: person,
        };
        axios(config)
            .then(function (response) {
                person = response.data;
                alert("User saved!");
            })
            .catch(function (error) {
                alert(error.response.data.message || "Could not update");
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

    getModelByUserEmail();
    getHairdresserTasks();

</script>

<div class="container-xl px-4 mt-4">
    <div class="row">        
        <div class="col-xl-7">
            <div class="card mb-4">
                <div class="card-header">Account Details (Nickname: {$user.nickname})</div>
                <div class="card-body">
                    <form>
                        <AccountForm data = {person}/>
                        <button on:click={updatePerson} class="btn btn-primary" type="button" >Save changes</button>
                    </form>
                </div>
            </div>
        </div>
        {#if $user.user_roles === "hairdresser"}
            <div class="col-xl-5">
                <div class="card mb-4">
                    <div class="card-header">Portfolio</div>
                    <div class="card-body">
                        <form>
                            <HairdresserPortfolioForm data = {person} hairdresserTasks = {hairdresserTasks}/>
                            <button on:click={updatePerson} class="btn btn-primary" type="button" >Update Portfolio</button>
                        </form>
                    </div>
                </div>
            </div>
        {/if}
    </div>
</div>        