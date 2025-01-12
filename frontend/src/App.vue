<template>
  <div id="app" class="flex flex-col min-h-screen bg-gray-900 text-gray-100">
    <!-- Navbar -->
    <header class="bg-gray-800 shadow-lg sticky top-0 z-50">
      <div class="container mx-auto flex items-center justify-between py-4 px-6">
        <div class="flex items-center">
          <h1 class="ml-3 text-2xl font-bold text-indigo-400 tracking-wide">
            SyncSound
          </h1>
        </div>

        <!-- Desktop Menu -->
        <nav class="hidden md:flex space-x-6">
          <router-link
              to="/"
              class="text-gray-300 hover:text-indigo-400 transition"
          >
            Home
          </router-link>
          <router-link
              to="/about"
              class="text-gray-300 hover:text-indigo-400 transition"
          >
            About
          </router-link>
          <router-link
              to="/contact"
              class="text-gray-300 hover:text-indigo-400 transition"
          >
            Contact
          </router-link>
        </nav>

        <!-- Mobile Menu Toggle -->
        <button
            @click="toggleMenu"
            class="md:hidden text-gray-300 focus:outline-none"
        >
          <svg
              xmlns="http://www.w3.org/2000/svg"
              class="h-6 w-6"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
          >
            <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M4 6h16M4 12h16M4 18h16"
            />
          </svg>
        </button>
      </div>

      <!-- Mobile Menu -->
      <nav
          v-if="menuOpen"
          class="md:hidden bg-gray-800 text-gray-300 space-y-4 py-4 px-6 border-t border-gray-700"
      >
        <router-link
            to="/"
            class="block hover:text-indigo-400 transition"
            @click="closeMenu"
        >
          Home
        </router-link>
        <router-link
            to="/about"
            class="block hover:text-indigo-400 transition"
            @click="closeMenu"
        >
          About
        </router-link>
        <router-link
            to="/contact"
            class="block hover:text-indigo-400 transition"
            @click="closeMenu"
        >
          Contact
        </router-link>
      </nav>
    </header>

    <!-- Main Content -->
    <main class="flex-grow">
      <router-view />
    </main>

    <!-- Footer -->
    <footer class="bg-gray-800 text-gray-300 py-8">
      <div class="container mx-auto grid grid-cols-1 md:grid-cols-3 gap-8 px-6">
        <!-- About -->
        <div>
          <h3 class="text-lg font-semibold text-indigo-400 mb-4">About Us</h3>
          <p class="text-sm leading-relaxed">
            SyncSound is your go-to platform for collaborative music
            experiences. Create rooms, vote for songs, and sync music with your
            friends in real time.
          </p>
        </div>

        <!-- Quick Links -->
        <div>
          <h3 class="text-lg font-semibold text-indigo-400 mb-4">Quick Links</h3>
          <ul class="space-y-2">
            <li>
              <router-link
                  to="/"
                  class="hover:text-indigo-400 transition text-sm"
              >
                Home
              </router-link>
            </li>
            <li>
              <router-link
                  to="/about"
                  class="hover:text-indigo-400 transition text-sm"
              >
                About
              </router-link>
            </li>
            <li>
              <router-link
                  to="/contact"
                  class="hover:text-indigo-400 transition text-sm"
              >
                Contact
              </router-link>
            </li>
          </ul>
        </div>

        <!-- Social Media -->
        <div>
          <h3 class="text-lg font-semibold text-indigo-400 mb-4">
            Follow Us
          </h3>
          <ul class="flex space-x-4">
            <li>
              <a
                  href="https://twitter.com"
                  target="_blank"
                  rel="noopener noreferrer"
                  class="hover:text-indigo-400 transition"
              >
                Twitter
              </a>
            </li>
            <li>
              <a
                  href="https://facebook.com"
                  target="_blank"
                  rel="noopener noreferrer"
                  class="hover:text-indigo-400 transition"
              >
                Facebook
              </a>
            </li>
            <li>
              <a
                  href="https://instagram.com"
                  target="_blank"
                  rel="noopener noreferrer"
                  class="hover:text-indigo-400 transition"
              >
                Instagram
              </a>
            </li>
          </ul>
        </div>
      </div>

      <div class="text-center text-sm text-gray-500 mt-8">
        &copy; 2025 SyncSound. All rights reserved.
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref } from "vue";

const menuOpen = ref(false);

function toggleMenu() {
  menuOpen.value = !menuOpen.value;
}

function closeMenu() {
  menuOpen.value = false;
}
</script>
