import { createRouter, createWebHistory } from 'vue-router';
import Home from '../pages/Home.vue';
import Room from '../pages/Room.vue';

const routes = [
    { path: '/', name: 'Home', component: Home },
    { path: '/room/:id', name: 'Room', component: Room, props: true },
    { path: '/:pathMatch(.*)*', redirect: '/' },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
