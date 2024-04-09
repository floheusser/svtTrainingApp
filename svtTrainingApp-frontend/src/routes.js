import Home from "./pages/Home.svelte";
import Account from "./pages/Account.svelte";
import MyTrainings from "./pages/trainer/MyTrainings.svelte";
import AllTrainings from "./pages/AllTrainings.svelte"


export default {
    '/': Home,
    '/home': Home,
    '/account': Account,
    '/myTrainings': MyTrainings,
    '/allTrainings': AllTrainings,
}