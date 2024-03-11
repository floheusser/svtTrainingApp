import Home from "./pages/Home.svelte";
import Account from "./pages/Account.svelte";
import Hairdressers from "./pages/Hairdressers.svelte";
import HairdresserDetails from "./pages/customer/HairdresserDetails.svelte";
import RequestAppointment from "./pages/customer/RequestAppointment.svelte";
import HairdresserAppointmentOverview from "./pages/hairdresser/HairdresserAppointmentOverview.svelte";
import CustomerAppointmentOverview from "./pages/customer/CustomerAppointmentOverview.svelte";

export default {
    '/': Home,
    '/home': Home,
    '/account': Account,

    '/hairdressers': Hairdressers,
    '/hairdresser/:id': HairdresserDetails,
    '/appointment/:id': RequestAppointment,
    '/customerMyAppointments': CustomerAppointmentOverview,

    '/hairdresserMyAppointments': HairdresserAppointmentOverview
}