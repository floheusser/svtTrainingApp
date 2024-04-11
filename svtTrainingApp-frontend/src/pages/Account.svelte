<script>
    import axios from "axios";
    const api_root = window.location.origin;
    import AccountForm from "./components/AccountForm.svelte";
    import { user, jwt_token } from "../store";

    let appUser = {
        id: "",
        roles: [],
        nickname: "",
        email: $user.email,
        name: "",
        type:""
    };

    function getUserByEmail() {

        var config = {
            method: "get",
            url: api_root + "/api/user/account",
            headers: {Authorization: "Bearer "+$jwt_token},
        };

        axios(config)
            .then(function (response) {
                appUser = response.data;
            })
            .catch(function (error) {
                console.log(error);
            });
    }

    function updateUser() {
        var config = {
            method: "put",
            url: api_root + "/api/user/update",
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer "+$jwt_token
            },
            data: appUser,
        };
        axios(config)
            .then(function (response) {
                appUser = response.data;
                alert("User gespeichert!");
            })
            .catch(function (error) {
                alert(error.response.data.message || "Could not create");
            });
    }

    getUserByEmail();
</script>

<div class="container-xl px-4 mt-4">
    <div class="row"> 
        <h3>Willkommen {appUser.nickname} </h3>
        <ul>
            <li>E-mail: {appUser.email}</li>
            <li>Rollen: {appUser.roles} </li>
        </ul>       
    </div>
    <div class="row">
        <div class="col-xl">
            <div class="card mb-4">
                <div class="card-header">Account</div>
                <div class="card-body">
                    <form>
                        <AccountForm data={appUser} />
                       
                        <button
                            on:click={updateUser}
                            class="btn btn-success"
                            type="button">Speichern</button
                        >
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>        