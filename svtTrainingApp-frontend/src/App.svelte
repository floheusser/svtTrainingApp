<script>
	import Router from "svelte-spa-router";
	import routes from "./routes";
	import { isAuthenticated, user } from "./store";
	import auth from "./auth.service";
</script>

<div id="app">
	<nav class="navbar navbar-expand-lg bg-light sticky-top">
		<div class="container-fluid">
			<a class="navbar-brand" href="#/">
				<img src="/svt-logo.png" alt="Logo" class="img-fluid" style="max-height: 50px;">
			</a>
			<button
				class="navbar-toggler"
				type="button"
				data-bs-toggle="collapse"
				data-bs-target="#navbarNav"
				aria-controls="navbarNav"
				aria-expanded="false"
				aria-label="Toggle navigation"
			>
				<span class="navbar-toggler-icon" />
			</button>
			<div class="collapse navbar-collapse" id="navbarNav">
				<ul class="navbar-nav">
					{#if $isAuthenticated}
					<li class="nav-item">
						<a
							class="nav-link"
							aria-current="page"
							href="#/account">Account</a
						>
					</li>
					{/if}
					{#if $isAuthenticated && $user.user_roles.includes("trainer")}
					<li class="nav-item">
						<a
							class="nav-link"
							aria-current="page"
							href="#/myTrainings">My Trainings</a
						>
					</li>
					{/if}
					{#if $isAuthenticated && $user.user_roles === "hairdresser"}
					<li class="nav-item">
						<a
							class="nav-link"
							aria-current="page"
							href="#/hairdresserMyAppointments">My Appointments</a
						>
					</li>
					{/if}
				</ul>
			</div>
			<div class="d-flex">
				{#if $isAuthenticated}
					<span class="navbar-text me-2">
						{$user.name}
					</span>
					<button
						type="button"
						class="btn btn-primary"
						href = "#/"
						on:click={auth.logout}>Log Out</button
					>
				{:else}
					<button
						type="button"
						class="btn btn-primary"
						on:click={auth.loginWithPopup}>Log In</button
					>
				{/if}
			</div>
		</div>
	</nav>

	<div class="container">
		<Router {routes} />
	</div>
</div>
