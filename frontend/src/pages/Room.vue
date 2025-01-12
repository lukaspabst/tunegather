<template>
  <div class="min-h-screen bg-gray-50">
    <div class="p-4 bg-blue-500 text-white">
      <h2 class="text-2xl font-bold">Room: {{ roomId }}</h2>
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
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      roomId: this.$route.params.id,
      songs: [],
      loading: true,
      error: null,
    };
  },
  methods: {
    async fetchSongs() {
      try {
        this.loading = true;
        this.error = null;

        const response = await axios.get(`/api/rooms/${this.roomId}/songs`);
        this.songs = response.data;
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
  },
  async created() {
    await this.fetchSongs();
  },
};
</script>

<style scoped>
</style>
