<script setup>

import PreviewCard from "@/components/PreviewCard.vue";
import {computed, reactive, ref} from "vue";
import {get} from "@/net/index.js";
import ClientDetails from "@/components/ClientDetails.vue";
import Register from "@/components/Register.vue"
import {Plus} from "@element-plus/icons-vue";
import {useRoute} from "vue-router";
import {useStore} from "@/store/index.js";
import Terminal from "@/components/ConnectionCard.vue";
import ConnectionCard from "@/components/ConnectionCard.vue";
const store = useStore()

const route = useRoute()
const location = [
  {name: 'cn', desc: '中国大陆'},
  {name: 'us', desc: '美国'},
  {name: 'jp', desc: '日本'},
  {name: 'de', desc: '德国'},
  {name: 'kr', desc: '韩国'},
  {name: 'hk', desc: '中国香港'},
  {name: 'sg', desc: '新加坡'},
]

const list = ref([])
const selectedLocation = ref([])

const terminals = reactive({
  show: false,
  id: -1
})
const filterList = computed(()=> {
  if (selectedLocation.value.length === 0) {
    return list.value
  } else {
    return list.value.filter(i => selectedLocation.value.indexOf(i.location) >= 0)
  }
})

function getList(){
  if (route.name === 'manage') {
    get('api/monitor/list',(data)=> {
      list.value = data
    })
    console.log(123)
  }

}
function openTerminal(id) {
  terminals.show = true
  terminals.id = id
  detail.show = false
}
const register = reactive({
  show: false,
  token :''
})
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
const refresh = () => {
  get('api/monitor/register', token => register.token =  token)
}

</script>

<template>
  <div class="manage-main">
    <div style="display: flex;justify-content: space-between;align-items: center;">
      <div>
        <div class="title"><i class="fa-solid fa-server"></i>管理服务器列表</div>
        <div class="desc">在这里管理所有服务器实例</div>
      </div>
      <div>
        <el-button :disabled="!store.isAdmin" :icon="Plus" @click="register.show = true" type="primary">新增服务器</el-button>
      </div>
    </div>
    <el-divider style="margin: 10px 0"></el-divider>
    <div style="margin-bottom: 20px">
      <el-checkbox-group v-model="selectedLocation">
        <el-checkbox v-for="i in location" :key="i" :label="i.name" border style="margin-right: 10px">
          <span :class="`flag-icon flag-icon-${i.name}`"></span>
          <span style="font-size: 13px; margin-left: 10px">{{i.desc}}</span>
        </el-checkbox>
      </el-checkbox-group>
    </div>
    <div class="server-list" v-if="list.length">
      <preview-card v-for="item in filterList" :data="item" :update="getList" @click="showDetail(item.id)"></preview-card>
    </div>
    <el-empty v-else-if="list.length === 0" description="客户端列表为空，立即点击右上角按钮添加客户端！"></el-empty>
    <el-drawer v-if="list.length" v-model="detail.show" :show-close="false" :size="700"
               direction="ttb" style="justify-items: center;" @close="detail.id = -1" :with-header="false">
      <client-details :id="detail.id" :update="getList" @delete="getList();detail.show=false" @terminal="openTerminal(detail.id)"></client-details>
    </el-drawer>
    <el-drawer v-model="register.show" @close="register.show = false" direction="ttb"
               style="justify-items: center;" :size="350" :with-header="false" @open="refresh()">
      <register :token="register.token"></register>
    </el-drawer>
    <el-drawer :size="700" v-model="terminals.show" direction="ttb" @close="terminals.show = false; terminals.id = -1"
               style="width: 800px" :close-on-click-modal="false">
      <template #header>
        <div>
          <div style="font-size: 18px; font-weight: bold; color:var(--el-color-primary);">SSH远程连接</div>
          <div style="font-size: 14px;color: grey">远程连接将由服务端完成，在内网环境也可正常使用</div>
          <el-divider style="margin: 10px 0 0 0"></el-divider>
        </div>
      </template>
      <connection-card :id="terminals.id"></connection-card>
    </el-drawer>
  </div>
</template>

<style lang="less" scoped>
:deep(.el-drawer__header) {
  margin-bottom: 0;
}
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