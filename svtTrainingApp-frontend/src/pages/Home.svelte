<script>
    import { isAuthenticated } from "../store";
    import auth from "../auth.service";

</script>
<div class="container mt-5" style="text-align: center;">
    <div class="row">
        <div class="col-12 d-flex flex-column align-items-center justify-content-center">
            <h1 style="color: #333; margin-top: 20px;">SVT Training App</h1>
            <img src="/svt-logo.png" alt="Logo" class="img-fluid" style="max-height: 200px;">
        </div>
    </div>
    <div class="container mt-5" style="text-align: center;">
        <div class="row">
            <div class="col-12 d-flex flex-column align-items-center justify-content-center">
                {#if !$isAuthenticated}
                <button
                    type="button"
                    class="btn btn-primary btn-sm"
                    on:click={auth.loginWithPopup}>Log In</button
                >
                {/if}
            </div>
        </div>
    </div>
</div>
