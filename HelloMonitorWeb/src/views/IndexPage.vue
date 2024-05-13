<script setup>

import router from "@/router/index.js";
import {logout} from "@/net/index.js";


</script>

<template>
  <div>
    <el-button plain type="warning" @click="logout(()=> {router.push('/')})">退出登陆</el-button>
  </div>
</template>

<style scoped>

</style>