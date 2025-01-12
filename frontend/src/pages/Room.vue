<template>
  <div class="min-h-screen bg-gray-50">
    <div class="p-4 bg-blue-500 text-white flex justify-between items-center">
      <h2 class="text-2xl font-bold">Room: {{ roomId }}</h2>
      <button
          @click="showShareModal = true"
          class="p-2 bg-gray-100 text-blue-500 rounded-full shadow hover:bg-gray-200"
      >
        <i class="fas fa-share-alt"></i>
      </button>
    </div>

    <div class="p-4">
      <h3 class="text-xl font-semibold mb-4">Songs</h3>
      <ul>
        <li v-for="song in songs" :key="song.id" class="mb-2">
          <div class="flex justify-between items-center bg-white p-2 rounded-lg shadow">
            <span>{{ song.name }}</span>
            <div>
              <button
                  @click="vote(song.id, 'up')"
                  class="px-2 py-1 bg-green-500 text-white rounded-lg hover:bg-green-600"
              >
                Upvote
              </button>
              <button
                  @click="vote(song.id, 'down')"
                  class="px-2 py-1 bg-red-500 text-white rounded-lg hover:bg-red-600"
              >
                Downvote
              </button>
            </div>
          </div>
        </li>
      </ul>
    </div>

    <div class="p-4">
      <button
          @click="showAddSongDialog = true"
          class="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600"
      >
        Add Song
      </button>
    </div>

    <div>
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
                class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 font-medium"
            >
              Copy
            </button>
          </div>
          <div class="flex justify-around text-gray-700 text-xl">
            <button
                class="px-3 py-2 bg-gray-100 rounded-full hover:bg-gray-200"
                @click="shareOnSocial('facebook')"
            >
              <i class="fab fa-facebook-f"></i>
            </button>
            <button
                class="px-3 py-2 bg-gray-100 rounded-full hover:bg-gray-200"
                @click="shareOnSocial('twitter')"
            >
              <i class="fab fa-twitter"></i>
            </button>
            <button
                class="px-3 py-2 bg-gray-100 rounded-full hover:bg-gray-200"
                @click="shareOnSocial('whatsapp')"
            >
              <i class="fab fa-whatsapp"></i>
            </button>
          </div>
          <div class="mt-4 text-right">
            <button
                @click="showShareModal = false"
                class="px-4 py-2 bg-gray-300 text-gray-800 rounded-lg hover:bg-gray-400 font-medium"
            >
              Close
            </button>
          </div>
        </div>
      </div>

      <!-- Add Song Dialog -->
      <div
          v-if="showAddSongDialog"
          class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center"
      >
        <div class="bg-white p-6 rounded-lg shadow-lg max-w-sm w-full">
          <h3 class="text-xl font-semibold mb-4 text-gray-800">Add a Song</h3>
          <input
              v-model="newSongName"
              type="text"
              placeholder="Enter Song Name"
              class="border rounded-lg px-2 py-1 w-full mb-4 text-gray-800"
          />
          <div class="text-right">
            <button
                @click="addSong"
                class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 font-medium"
            >
              Add
            </button>
            <button
                @click="showAddSongDialog = false"
                class="px-4 py-2 bg-gray-300 text-gray-800 rounded-lg hover:bg-gray-400 font-medium ml-2"
            >
              Cancel
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      roomId: this.$route.params.id,
      songs: [],
      qrCode: "",
      roomLink: ``,
      newSongName: "",
      showShareModal: false,
      showAddSongDialog: false,
      loading: true,
      error: null,
    };
  },
  methods: {
    async fetchSongs() {
      try {
        this.loading = true;
        this.error = null;

        const response = await axios.get(`/api/rooms/${this.roomId}`);
        this.songs = response.data.suggestions;
        this.qrCode = response.data.qrCode;
        this.roomLink = response.data.link;
      } catch (err) {
        console.error("Error fetching songs:", err);
        this.error = "Failed to load songs. Please try again.";
      } finally {
        this.loading = false;
      }
    },
    async vote(songId, type) {
      try {
        await axios.post(`/api/rooms/${this.roomId}/songs/${songId}/vote`, {
          type,
        });
        alert(`You voted ${type} for song ${songId}`);
      } catch (err) {
        console.error("Error voting for song:", err);
        alert("Failed to submit your vote. Please try again.");
      }
    },
    async addSong() {
      if (!this.newSongName.trim()) {
        alert("Please enter a song name.");
        return;
      }
      try {
        const suggestion = {
          id: null,
          title: this.newSongName,
          artist: "Unknown Artist",
          duration: 0,
          cover: "",
          upVotes: 0,
          downVotes: 0,
        };

        await axios.post(`/api/rooms/${this.roomId}/suggestions`, suggestion);
        alert("Song added successfully!");
        this.newSongName = "";
        this.showAddSongDialog = false;
        await this.fetchSongs();
      } catch (err) {
        console.error("Error adding song:", err);
        alert("Failed to add song. Please try again.");
      }
    },
    copyToClipboard(text) {
      navigator.clipboard.writeText(text).then(() => {
        alert("Link copied to clipboard!");
      });
    },
    shareOnSocial(platform) {
      const url = encodeURIComponent(this.roomLink);
      const text = encodeURIComponent("Join my room on TuneGather!");
      const shareUrls = {
        facebook: `https://www.facebook.com/sharer/sharer.php?u=${url}`,
        twitter: `https://twitter.com/intent/tweet?url=${url}&text=${text}`,
        whatsapp: `https://wa.me/?text=${text} ${url}`,
      };
      window.open(shareUrls[platform], "_blank");
    },
  },
  async created() {
    await this.fetchSongs();
  },
};
</script>

<style scoped>
/* Import FontAwesome */
@import url("https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css");

.text-gray-800 {
  color: #1a202c;
}
</style>
