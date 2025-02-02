import { createApp } from 'vue';
import App from './App.vue';
import './assets/tailwind.css';
import router from './router';
import store from './store';

window.onSpotifyWebPlaybackSDKReady = () => {
    console.log('Spotify Web Playback SDK is ready');
    // Any initialization logic here, like initializing the Spotify player
};

const app = createApp(App);
app.use(router);
app.use(store);
app.mount('#app');
