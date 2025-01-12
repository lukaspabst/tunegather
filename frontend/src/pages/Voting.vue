<script>
export default {
  data() {
    return {
      roomId: this.$route.params.id,
      songs: [],
      ws: null,
      loading: true,
      error: null,
    };
  },
  methods: {
    async fetchSongs() {
      try {
        this.loading = true;
        this.error = null;

        const response = await fetch(`/api/rooms/${this.roomId}/songs`);
        if (!response.ok) throw new Error("Failed to fetch songs.");
        this.songs = await response.json();
      } catch (err) {
        console.error("Error fetching songs:", err);
        this.error = "Failed to load songs. Please try again.";
      } finally {
        this.loading = false;
      }
    },

    sendVote(songId, voteType) {
      if (this.ws && this.ws.readyState === WebSocket.OPEN) {
        const message = {
          type: "VOTE",
          roomId: this.roomId,
          songId,
          voteType,
        };
        this.ws.send(JSON.stringify(message));
      } else {
        alert("WebSocket connection is not open.");
      }
    },

    handleWebSocketMessage(event) {
      const data = JSON.parse(event.data);

      if (data.type === "VOTE_UPDATE") {
        const updatedSongs = this.songs.map((song) =>
            song.id === data.songId
                ? { ...song, votes: data.votes }
                : song
        );
        this.songs = updatedSongs;
      }

      else if (data.type === "NEW_SONG_PHASE") {
        this.songs.push(data.newSong);
      }
    },
    setupWebSocket() {
      this.ws = new WebSocket(`ws://localhost:8080/api/rooms/${this.roomId}/ws`);

      this.ws.onopen = () => {
        console.log("WebSocket connection opened.");
      };

      this.ws.onmessage = this.handleWebSocketMessage;

      this.ws.onerror = (error) => {
        console.error("WebSocket error:", error);
        this.error = "WebSocket connection failed.";
      };

      this.ws.onclose = () => {
        console.log("WebSocket connection closed.");
      };
    },
  },
  async created() {
    await this.fetchSongs();
    this.setupWebSocket();
  },
  beforeDestroy() {
    if (this.ws) {
      this.ws.close();
    }
  },
};
</script>

<template>
  <div class="flex flex-col items-center justify-center min-h-screen bg-gray-100">
    <h1 class="text-4xl font-bold mb-8">Room {{ roomId }}</h1>

    <div v-if="loading" class="text-lg">Loading songs...</div>
    <div v-if="error" class="text-lg text-red-500">{{ error }}</div>

    <ul v-if="songs.length > 0">
      <li
          v-for="song in songs"
          :key="song.id"
          class="flex items-center justify-between w-96 bg-white shadow p-4 mb-2"
      >
        <span>{{ song.name }}</span>
        <div class="flex space-x-4">
          <button
              @click="sendVote(song.id, 'upvote')"
              class="px-4 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600"
          >
            👍 {{ song.votes.upvote || 0 }}
          </button>
          <button
              @click="sendVote(song.id, 'downvote')"
              class="px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600"
          >
            👎 {{ song.votes.downvote || 0 }}
          </button>
        </div>
      </li>
    </ul>

    <div v-if="songs.length === 0 && !loading && !error" class="text-lg">
      No songs available. Waiting for suggestions...
    </div>
  </div>
</template>

<style scoped>
</style>
