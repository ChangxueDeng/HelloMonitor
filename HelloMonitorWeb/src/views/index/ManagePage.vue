<script setup>

import PreviewCard from "@/components/PreviewCard.vue";
import {reactive, ref} from "vue";
import {get} from "@/net/index.js";
import ClientDetails from "@/components/ClientDetails.vue";

const list = ref([])

function getList(){
  get('api/monitor/list',(data)=> {
    list.value = data
  })
}

getList()
setInterval(getList,10000)
const detail = reactive({
  show: false,
  id: -1
})
const showDetail = (id) => {
  detail.show = true
  detail.id = id
}
</script>

<template>
  <div class="manage-main">
    <div class="title"><i class="fa-solid fa-server"></i>管理主机列表</div>
    <div class="desc">在这里管理所有主机实例</div>
    <el-divider style="margin: 10px 0"></el-divider>
    <div class="server-list">
      <preview-card v-for="item in list" :data="item" :update="getList" @click="showDetail(item.id)"></preview-card>
    </div>
    <el-drawer v-if="list.length" v-model="detail.show" :show-close="false" :size="600"
               direction="ttb" style="justify-items: center;" @close="detail.id = -1" :with-header="false">
      <client-details :id="detail.id" :update="getList"></client-details>
    </el-drawer>
  </div>
</template>

<style scoped>
:deep(.el-drawer) {
  position: absolute;
  margin: auto auto;
  width: 600px;
  top: 50%;
  transform: translateY(-50%);
  padding: 0;
  border-radius: 10px;
}
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