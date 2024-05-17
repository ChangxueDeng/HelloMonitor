<script setup>

import PreviewCard from "@/components/PreviewCard.vue";
import {computed, reactive, ref} from "vue";
import {get} from "@/net/index.js";
import ClientDetails from "@/components/ClientDetails.vue";
import Register from "@/components/Register.vue"
import {Plus} from "@element-plus/icons-vue";
import {useRoute} from "vue-router";
import {useStore} from "@/store/index.js";
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
        <div class="title"><i class="fa-solid fa-server"></i>管理主机列表</div>
        <div class="desc">在这里管理所有主机实例</div>
      </div>
      <div>
        <el-button :disabled="!store.isAdmin" :icon="Plus" @click="register.show = true" type="primary">新增主机</el-button>
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
    <el-empty v-else-if="list.length === 0" description="主机列表为空，立即点击右上角按钮添加新主机！"></el-empty>
    <el-drawer v-if="list.length" v-model="detail.show" :show-close="false" :size="700"
               direction="ttb" style="justify-items: center;" @close="detail.id = -1" :with-header="false">
      <client-details :id="detail.id" :update="getList" @delete="getList"></client-details>
    </el-drawer>
    <el-drawer v-model="register.show" @close="register.show = false" direction="ttb"
               style="justify-items: center;" :size="350" :with-header="false" @open="refresh()">
      <register :token="register.token"></register>
    </el-drawer>
  </div>
</template>

<style lang="less" scoped>
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