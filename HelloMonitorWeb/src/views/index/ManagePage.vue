<script setup>

import PreviewCard from "@/components/PreviewCard.vue";
import {ref} from "vue";
import {get} from "@/net/index.js";

const list = ref([])

function getList(){
  get('api/monitor/list',(data)=> {
    list.value = data
  })
}

getList()
setInterval(getList,10000)

</script>

<template>
  <div class="manage-main">
    <div class="title"><i class="fa-solid fa-server"></i>管理主机列表</div>
    <div class="desc">在这里管理所有主机实例</div>
    <el-divider style="margin: 10px 0"></el-divider>
    <div class="server-list">
      <preview-card v-for="item in list" :data="item"></preview-card>
    </div>

  </div>
</template>

<style scoped>
.manage-main{
  margin: 0 50px;
  .title{
    font-weight: bold;
    font-size: 22px;
  }
  .desc{
    font-size: 15px;
    color: grey;
  }
  .server-list{
    display: flex;
    gap: 20px;
    flex-wrap: wrap;;
  }
}
</style>