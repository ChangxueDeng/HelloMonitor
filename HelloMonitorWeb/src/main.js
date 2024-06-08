import { createApp } from 'vue'
import App from './App.vue'
import {createPinia} from "pinia";
import router from "@/router/index.js";
import 'element-plus/dist/index.css'
import axios from "axios";
import 'flag-icon-css/css/flag-icons.min.css'
import piniaPluginPersistedstate from "pinia-plugin-persistedstate"

const pinia = createPinia()
const app = createApp(App)
axios.defaults.baseURL = 'http://47.113.144.109:8082'
import 'element-plus/theme-chalk/dark/css-vars.css'
app.use(pinia)
pinia.use(piniaPluginPersistedstate)
app.use(router)
app.mount("#app")
