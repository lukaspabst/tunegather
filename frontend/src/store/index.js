import { createStore } from 'vuex';

const store = createStore({
    state: {
        user: null,
        room: null,
    },
    mutations: {
        setUser(state, user) {
            state.user = user;
        },
        setRoom(state, room) {
            state.room = room;
        },
    },
    actions: {
        login({ commit }, user) {
            commit('setUser', user);
        },
        joinRoom({ commit }, room) {
            commit('setRoom', room);
        },
    },
});

export default store;
