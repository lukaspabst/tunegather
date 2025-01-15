<template>
  <div class="flex flex-col items-center justify-center min-h-screen bg-gray-100">
    <h1 class="text-4xl font-bold mb-8">Welcome to SyncSound</h1>
    <div class="space-y-4">
      <div class="space-x-4">
        <button
            @click="showJoinRoomModal = true"
            class="px-6 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600"
        >
          Join Room
        </button>
        <button
            @click="showCreateRoomModal = true"
            class="px-6 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600"
        >
          Create Room
        </button>
      </div>
    </div>

    <!-- Join Room Modal -->
    <div
        v-if="showJoinRoomModal"
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
          <label class="block text-gray-700 font-medium mb-2">Room Code</label>
          <input
              v-model="roomCode"
              type="text"
              placeholder="Enter Room Code"
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
              class="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 font-medium"
          >
            Join Room
          </button>
          <button
              @click="showJoinRoomModal = false"
              class="px-4 py-2 bg-gray-300 text-gray-800 rounded-lg hover:bg-gray-400 font-medium"
          >
            Cancel
          </button>
        </div>
      </div>
    </div>

    <!-- Create Room Modal -->
    <div
        v-if="showCreateRoomModal"
        class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center"
    >
      <div class="bg-white p-6 rounded-lg shadow-lg max-w-lg w-full">
        <h3 class="text-xl font-semibold mb-4 text-gray-800">Room Settings</h3>
        <div class="mb-4">
          <label class="block text-gray-700 font-medium mb-2">Room Title</label>
          <input
              v-model="roomTitle"
              type="text"
              placeholder="Enter Room Title"
              class="border rounded-lg px-3 py-2 w-full text-gray-800"
          />
        </div>
        <div class="mb-4">
          <label class="block text-gray-700 font-medium mb-2">Host Name</label>
          <input
              v-model="hostName"
              type="text"
              placeholder="Enter Your Name"
              class="border rounded-lg px-3 py-2 w-full text-gray-800"
          />
        </div>
        <div class="mb-4">
          <label class="block text-gray-700 font-medium mb-2">Password (Optional)</label>
          <input
              v-model="roomPassword"
              type="password"
              placeholder="Enter Password"
              class="border rounded-lg px-3 py-2 w-full text-gray-800"
          />
        </div>
        <div class="mb-4">
          <label class="block text-gray-700 font-medium mb-2">
            Playlist Entry Percentage
          </label>
          <input
              type="range"
              min="0"
              max="100"
              v-model="playlistThreshold"
              class="w-full"
          />
          <div class="text-right text-gray-600 mt-2">
            {{ playlistThreshold }}%
          </div>
        </div>
        <div class="text-right space-x-4">
          <button
              @click="createRoom"
              class="px-4 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600 font-medium"
          >
            Create Room
          </button>
          <button
              @click="showCreateRoomModal = false"
              class="px-4 py-2 bg-gray-300 text-gray-800 rounded-lg hover:bg-gray-400 font-medium"
          >
            Cancel
          </button>
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
      showJoinRoomModal: false,
      showCreateRoomModal: false,
      roomTitle: "",
      roomPassword: "",
      roomCode: "",
      hostName: "",
      userName: "",
      playlistThreshold: 50,
    };
  },
  methods: {
    async createRoom() {
      if (!this.roomTitle.trim() || !this.hostName.trim()) {
        alert("Please provide a room title and your name.");
        return;
      }

      try {
        const payload = {
          title: this.roomTitle,
          password: this.roomPassword || null,
          host: this.hostName,
          playlistThreshold: this.playlistThreshold,
        };

        const response = await axios.post("/api/rooms", payload);
        alert("Room created successfully!");
        this.showCreateRoomModal = false;
        this.$router.push(`/room/${response.data.id}`);
      } catch (error) {
        console.error("Error creating room:", error);
        alert("Failed to create room. Please try again.");
      }
    },
    async joinRoom() {
      if (!this.roomCode.trim() || !this.userName.trim()) {
        alert("Please provide your name and the room code.");
        return;
      }

      try {
        const payload = {
          participant: this.userName,
          password: this.roomPassword || null,
        };

        await axios.post(`/api/rooms/${this.roomCode}/participants`, payload);
        alert("Joined the room successfully!");
        this.showJoinRoomModal = false;
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
.text-gray-800 {
  color: #1a202c;
}
</style>
