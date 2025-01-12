<template>
  <div class="flex flex-col items-center justify-center min-h-screen bg-gray-100">
    <h1 class="text-4xl font-bold mb-8">Welcome to SyncSound</h1>
    <div class="space-y-4">
      <input
          v-model="roomCode"
          type="text"
          placeholder="Enter Room Code"
          class="px-4 py-2 border rounded-lg w-64"
      />
      <div class="space-x-4">
        <button
            @click="joinRoom"
            class="px-6 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600"
        >
          Join Room
        </button>
        <button
            @click="createRoom"
            class="px-6 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600"
        >
          Create Room
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
export default {
  data() {
    return {
      roomCode: "",
    };
  },
  methods: {
    async createRoom() {
      try {
        const host = "host1";
        const response = await axios.post("/api/rooms", null, {
          params: { host },
        });
        const newRoom = response.data;
        this.$router.push(`/room/${newRoom.id}`);
      } catch (error) {
        console.error("Error creating room:", error);
        alert("Failed to create room. Please try again.");
      }
    },
    async joinRoom() {
      if (!this.roomCode.trim()) {
        alert("Please enter a room code!");
        return;
      }

      try {
        const participant = "user1";
        await axios.post(`/api/rooms/${this.roomCode}/participants`, null, {
          params: { participant },
        });
        this.$router.push(`/room/${this.roomCode}`);
      } catch (error) {
        console.error("Error joining room:", error);
        if (error.response && error.response.status === 404) {
          alert("Room not found. Please check the room code and try again.");
        } else {
          alert("Failed to join room. Please try again.");
        }
      }
    },
  },
};
</script>

<style scoped>
</style>
