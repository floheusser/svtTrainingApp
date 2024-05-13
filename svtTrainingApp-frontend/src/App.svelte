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
				<ul class="navbar-nav me-auto mb-2 mb-lg-0">
					{#if $isAuthenticated}
					<li class="nav-item">
						<a
							class="nav-link"
							aria-current="page"
							href="#/account">Account</a
						>
					</li>
					<li class="nav-item">
						<a
							class="nav-link"
							aria-current="page"
							href="#/allTrainings">All Trainings</a
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
					{#if $isAuthenticated && $user.user_roles.includes("admin")}
					<li class="nav-item">
						<a
							class="nav-link"
							aria-current="page"
							href="#/adminPage">Admin Page</a
						>
					</li>
					{/if}
					
				</ul>
				<ul class="navbar-nav">
					<li class="nav-item">
						{#if $isAuthenticated}
						<span class="navbar-text me-2">
							{$user.name}
						</span>
						<button
							type="button"
							class="btn btn-primary btn-sm"
							href = "#/"
							on:click={auth.logout}>Log Out</button
						>
						{:else}
						<button
							type="button"
							class="btn btn-primary btn-sm"
							on:click={auth.loginWithPopup}>Log In</button
						>
						{/if}
					</li>
				</ul>
			</div>
		</div>
	</nav>

	<div class="container">
		<Router {routes} />
	</div>
</div>
