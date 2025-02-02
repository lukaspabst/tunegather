<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Header -->
    <div v-if="!showJoinModal" class="p-4 bg-blue-500 text-white flex justify-between items-center">
      <h2 class="text-2xl font-bold">Room: {{ roomId }}</h2>
      <button
          @click="showShareModal = true"
          class="p-2 bg-gray-100 text-blue-500 rounded-full shadow hover:bg-gray-200 transition duration-200"
      >
        <i class="fas fa-share-alt"></i>
      </button>
    </div>

    <!-- Songs List -->
    <div v-if="!showJoinModal" class="p-4">
      <h3 class="text-xl font-semibold mb-4">Songs</h3>
      <ul>
        <li v-for="song in songs" :key="song.id" class="mb-2">
          <div class="flex justify-between items-center bg-white p-2 rounded-lg shadow">
            <span>{{ song.name }}</span>
            <div>
              <button
                  @click="vote(song.id, 'up')"
                  class="px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600 transition duration-200"
              >
                Upvote
              </button>
              <button
                  @click="vote(song.id, 'down')"
                  class="px-2 py-1 bg-red-500 text-white rounded-lg hover:bg-red-600 transition duration-200"
              >
                Downvote
              </button>
            </div>
          </div>
        </li>
      </ul>
    </div>

    <!-- Add Song Button -->
    <div v-if="!showJoinModal" class="p-4">
      <button
          @click="showAddSongDialog = true"
          class="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition duration-200"
      >
        Add Song
      </button>
    </div>

    <!-- Join Room Modal -->
    <div
        v-if="showJoinModal"
        class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center"
    >
      <div class="bg-white p-6 rounded-lg shadow-lg max-w-lg w-full">
        <h3 class="text-xl font-semibold mb-4 text-gray-800">Join Room</h3>
        <div class="mb-4">
          <label class="block text-gray-700 font-medium mb-2">Your Name</label>
          <input
              v-model="userName"
              type="text"
              placeholder="Enter Your Name"
              class="border rounded-lg px-3 py-2 w-full text-gray-800"
          />
        </div>
        <div class="mb-4">
          <label class="block text-gray-700 font-medium mb-2">Room Password</label>
          <input
              v-model="roomPassword"
              type="password"
              placeholder="Enter Password"
              class="border rounded-lg px-3 py-2 w-full text-gray-800"
          />
        </div>
        <div class="text-right space-x-4">
          <button
              @click="joinRoom"
              class="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 font-medium transition duration-200"
          >
            Join Room
          </button>
        </div>
      </div>
    </div>

    <!-- Share Modal -->
    <div
        v-if="showShareModal"
        class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center"
    >
      <div class="bg-white p-6 rounded-lg shadow-lg max-w-sm w-full">
        <h3 class="text-xl font-semibold mb-4 text-gray-800">Share Room</h3>
        <img :src="`data:image/png;base64,${qrCode}`" alt="Room QR Code" class="w-64 h-64 mx-auto mb-4" />
        <p class="text-center text-gray-700 mb-4">Share this link:</p>
        <div class="flex justify-center mb-4">
          <input
              type="text"
              :value="roomLink"
              readonly
              class="border rounded-lg px-2 py-1 w-full text-gray-800 mr-2"
          />
          <button
              @click="copyToClipboard(roomLink)"
              class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 font-medium transition duration-200"
          >
            Copy
          </button>
        </div>
        <div class="mt-4 text-right">
          <button
              @click="showShareModal = false"
              class="px-4 py-2 bg-gray-300 text-gray-800 rounded-lg hover:bg-gray-400 font-medium transition duration-200"
          >
            Close
          </button>
        </div>
      </div>
    </div>

    <!-- Add Song Dialog -->
    <div v-if="showAddSongDialog" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
      <div class="bg-white p-6 rounded-lg shadow-lg max-w-sm w-full">
        <h3 class="text-xl font-semibold mb-4 text-gray-800">Add a Song</h3>
        <input
            v-model="newSongName"
            @input="searchSongs"
            type="text"
            placeholder="Search for a song"
            class="border rounded-lg px-2 py-1 w-full mb-4 text-gray-800"
        />
        <ul v-if="searchResults.length" class="mb-4">
          <li
              v-for="song in searchResults"
              :key="song.id"
              @click="selectSong(song)"
              class="p-2 hover:bg-gray-100 cursor-pointer"
          >
            {{ song.name }} - {{ song.artist }}
          </li>
        </ul>
        <div class="text-right">
          <button
              @click="addSong"
              class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 font-medium transition duration-200"
          >
            Add
          </button>
          <button
              @click="showAddSongDialog = false"
              class="px-4 py-2 bg-gray-300 text-gray-800 rounded-lg hover:bg-gray-400 font-medium transition duration-200 ml-2"
          >
            Cancel
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import axios from 'axios';

export default {
  data() {
    return {
      roomId: this.$route.params.id,
      songs: [],
      qrCode: "",
      roomLink: "",
      newSongName: "",
      showShareModal: false,
      showAddSongDialog: false,
      showJoinModal: true,
      userName: "",
      roomPassword: "",
      loading: true,
      error: null,
      stompClient: null, // WebSocket client
      searchResults: [], // Search results array
    };
  },
  created() {
    const userName = localStorage.getItem("userName");
    if (userName) {
      this.userName = userName;
      this.showJoinModal = false;
      this.fetchSongs();
    }
    this.connectWebSocket(); // Initialize WebSocket connection
  },
  methods: {
    async fetchSongs() {
      try {
        this.loading = true;
        this.error = null;

        const response = await axios.get(`/api/rooms/${this.roomId}`);
        this.songs = response.data.suggestions || [];
        this.qrCode = response.data.qrCode || "";
        this.roomLink = response.data.link || "";
      } catch (err) {
        console.error("Error fetching songs:", err);
        this.error = "Failed to load songs. Please try again.";
      } finally {
        this.loading = false;
      }
    },
    async joinRoom() {
      if (!this.userName.trim()) {
        alert("Please enter your name.");
        return;
      }

      try {
        const payload = {
          participant: this.userName,
          password: this.roomPassword || null,
        };

        await axios.post(`/api/rooms/${this.roomId}/participants`, payload);

        localStorage.setItem("userName", this.userName);
        alert("Joined the room successfully!");
        this.showJoinModal = false;
        await this.fetchSongs();
      } catch (err) {
        console.error("Error joining room:", err);
        if (err.response && err.response.status === 404) {
          alert("Room not found. Please check the room code.");
        } else if (err.response && err.response.status === 403) {
          alert("Incorrect room password. Please try again.");
        } else {
          alert("Failed to join room. Please try again.");
        }
      }
    },
    async vote(songId, type) {
      try {
        await axios.post(`/api/rooms/${this.roomId}/songs/${songId}/vote`, { type });
        alert(`You voted ${type} for song ${songId}`);
      } catch (err) {
        console.error("Error voting for song:", err);
        alert("Failed to submit your vote. Please try again.");
      }
    },
    connectWebSocket() {
      const socket = new SockJS('http://localhost:8080/ws'); // Replace with your backend URL
      this.stompClient = Stomp.over(socket);

      this.stompClient.connect({}, () => {
        console.log('WebSocket connected');

        // Subscribe to the search results topic
        this.stompClient.subscribe('/topic/searchResults', (message) => {
          this.searchResults = JSON.parse(message.body);
        });
      });
    },
    searchSongs() {
      if (this.newSongName.trim()) {
        // Send the search query to the backend
        this.stompClient.send('/app/search', {}, JSON.stringify(this.newSongName));
      }
    },
    selectSong(song) {
      this.newSongName = song.name; // Auto-fill the input with the selected song
      this.searchResults = []; // Clear search results
    },
    async addSong() {
      if (!this.newSongName.trim()) {
        alert('Please select a song.');
        return;
      }

      try {
        const song = this.searchResults.find(s => s.name === this.newSongName);
        if (!song) {
          alert('Invalid song selection.');
          return;
        }

        await axios.post(`/api/rooms/${this.roomId}/suggestions`, song);
        alert('Song added successfully!');
        this.newSongName = '';
        this.showAddSongDialog = false;
        await this.fetchSongs();
      } catch (err) {
        console.error('Error adding song:', err);
        alert('Failed to add song. Please try again.');
      }
    },
    copyToClipboard(text) {
      navigator.clipboard.writeText(text).then(() => {
        alert("Link copied to clipboard!");
      });
    },
  },
};
</script>

<style scoped>
@import url("https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css");

.text-gray-800 {
  color: #1a202c;
}
</style>