<template>
  <div class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
    <div class="bg-white p-6 rounded-lg shadow-lg max-w-sm w-full flex flex-col">

      <div class="mb-4">
        <input
            v-model="localNewSongName"
            @input="debounceSearch"
            type="text"
            placeholder="Search for a song"
            class="border rounded-lg px-2 py-1 w-full text-gray-800"
        />
      </div>

      <div class="flex-1 overflow-y-auto mb-4 max-h-64">
        <ul v-if="searchResults.length" class="space-y-2">
          <li
              v-for="song in searchResults"
              :key="song.id"
              @click="selectSong(song)"
              :class="{'bg-blue-100': song.selected}"
              class="flex items-center p-3 hover:bg-gray-100 rounded-lg cursor-pointer transition duration-200"
          >
            <img
                :src="getCoverUrl(song.cover)"
                alt="Album Cover"
                class="w-12 h-12 rounded-lg object-cover mr-4"
                loading="lazy"
            />
            <div class="flex-1">
              <p class="font-semibold text-gray-800">{{ song.title }}</p>
              <p class="text-sm text-gray-600">{{ song.artist }}</p>
            </div>
            <p class="text-sm text-gray-500 mr-4">{{ formatDuration(song.duration) }}</p>
            <button
                @click.stop="togglePlay(song)"
                class="p-2 text-gray-500 hover:text-gray-800 transition duration-200"
            >
              <i :class="{'fa-play': !song.isPlaying, 'fa-pause': song.isPlaying}" class="fas"></i>
            </button>
          </li>
        </ul>
      </div>
      <div class="text-right">
        <button
            @click="addSong"
            class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 font-medium transition duration-200"
        >
          Add
        </button>
        <button
            @click="cancelAddSong"
            class="px-4 py-2 bg-gray-300 text-gray-800 rounded-lg hover:bg-gray-400 font-medium transition duration-200 ml-2"
        >
          Cancel
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

export default {
  props: {
    roomId: String,
    searchResults: Array,
    newSongName: String,
  },
  data() {
    return {
      localNewSongName: this.newSongName,
      stompClient: null,
      typingTimeout: null,
      selectedSong: null,
    };
  },
  created() {
    this.connectWebSocket();
  },
  watch: {
    newSongName(newVal) {
      this.localNewSongName = newVal;
    },
  },
  methods: {
    connectWebSocket() {
      const socket = new SockJS('http://localhost:8080/ws');
      this.stompClient = Stomp.over(socket);

      this.stompClient.connect({}, () => {
        console.log('WebSocket connected');
        this.stompClient.subscribe('/topic/searchResults', (message) => {
          this.$emit('update-search-results', JSON.parse(message.body));
        });
      });
    },
    debounceSearch() {
      clearTimeout(this.typingTimeout);

      if (this.localNewSongName.length >= 3) {
        this.typingTimeout = setTimeout(() => {
          this.searchSongs();
        }, 500);
      }
    },
    searchSongs() {
      if (this.localNewSongName.trim()) {
        this.stompClient.send('/app/search', {}, JSON.stringify(this.localNewSongName));
      }
    },
    selectSong(song) {
      if (this.selectedSong === song) {
        this.selectedSong = null;
      } else {
        this.selectedSong = song;
      }

      this.searchResults.forEach((item) => {
        item.selected = item === this.selectedSong;
      });
    },
    addSong() {
      if (!this.selectedSong) {
        alert('Please select a song.');
        return;
      }
      this.$emit('add-song', this.selectedSong);
      this.selectedSong = null;
      this.clearSearchResults();
    },
    cancelAddSong() {
      this.$emit('cancel-add-song');
      this.selectedSong = null;
      this.clearSearchResults();
    },
    clearSearchResults() {
      this.localNewSongName = '';
      this.$emit('update-search-results', []);
    },
    formatDuration(durationMs) {
      const minutes = Math.floor(durationMs / 60000);
      const seconds = ((durationMs % 60000) / 1000).toFixed(0);
      return `${minutes}:${seconds.padStart(2, '0')}`;
    },
    togglePlay(song) {
      song.isPlaying = !song.isPlaying;
      if (song.isPlaying) {
        console.log("Playing song:", song.title);
      } else {
        console.log("Pausing song:", song.title);
      }
    },
    getCoverUrl(coverString) {
      try {
        const regex = /url=([^\)]+)/g;
        const matches = [...coverString.matchAll(regex)];

        if (matches.length > 0) {
          const url = matches[0][1].split(',')[0].trim();
          return url;
        } else {
          return '';
        }
      } catch (error) {
        console.error("Error parsing cover string", error);
        return '';
      }
    },
  },
};
</script>

<style scoped>
.bg-white {
  max-width: 600px;
}

.flex-1 {
  overflow-y: auto;
}

ul {
  max-height: 300px;
}

button {
  transition: background-color 0.2s ease;
}
</style>
