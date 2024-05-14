<script setup>

import router from "@/router/index.js";
import {logout} from "@/net/index.js";
import {Moon, Sunny} from "@element-plus/icons-vue";
import {ref} from "vue";
import {useDark} from "@vueuse/core";
import Tabltem from "@/components/Tabltem.vue";
import {useRoute} from "vue-router";

const route = useRoute()
const dark = ref(useDark())
</script>

<template>
<!--  <div>-->
<!--    <el-button plain type="warning" @click="logout(()=> {router.push('/')})">退出登陆</el-button>-->
<!--  </div>-->
  <div>
    <el-container class="main-container">
      <el-header class="main-header">
        <el-image style="height: 30px;" src="https://element-plus.org/images/element-plus-logo.svg"></el-image>
        <div class="tabs">
          <Tabltem name="导航1" :activate="route.name === 'manage'" @click="router.push('/index'); tabActive = 1"></Tabltem>
          <Tabltem name="导航2" :activate="route.name === 'security'" @click="router.push('/index/security'); tabActive = 2"></Tabltem>
          <el-switch  style="margin: 0 20px" v-model="dark" active-color="#424242" :active-action-icon="Moon" :inactive-action-icon="Sunny">
          </el-switch>
          <el-dropdown>
            <el-avatar></el-avatar>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>
                  <div @click="logout(()=> {router.push('/')})">退出登陆</div>
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main class="main-main">
        <router-view v-slot="{ Component }">
          <transition name=".el-zoom-in-center" mode="out-in">
            <component :is="Component"></component>
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </div>
</template>

<style lang="less" scoped>
.main-container{
  width: 100vw;
  height: 100vh;
  .main-header{
    height: 60px;
    background-color: var(--el-bg-color);
    border-bottom: 1px solid var(--el-border-color);
    display: flex;
    align-items: center;
    .tabs{
      display: flex;
      height: 55px;
      flex: 1;
      gap: 10px;
      justify-content: right;
      align-items: center;
    }
  }
  .main-main{
    height: 100%;
    background: #F0F2F5;
  }

}
.dark .main-container .main-main{
  background-color: #1D1D1D;
}
</style>