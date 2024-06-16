<script setup>
import {post} from "@/net/index.js";
import {osNameToIcon} from "@/tools/tools.js";
import {ElMessage} from "element-plus";
const props = defineProps({
  clients: Array,
  id: Number,
})
const emits = defineEmits(['modify'])
const selectedClients = []
const select = (state, id) => {
  if (state) {
    selectedClients.push(id)
  } else {
    const index = selectedClients.indexOf(id)
    selectedClients.splice(index, 1)
  }
}
function modify() {
  post("/api/user/sub/modify-access",{
    subUid: props.id,
    accessList: selectedClients,
  },() => {
    ElMessage.success("分配成功")
    emits('modify')
  })
}
</script>

<template>
  <div class="title"><i class="fa-solid fa-server"></i>分配服务器</div>
  <div class="desc" style="margin-bottom: 10px">子用户同样用于管理服务器，但是只能管理被分配的服务器</div>
  <el-scrollbar style="flex: 1">
    <div v-for="item in clients" class="lite-card">
      <el-checkbox @change="state => select(state, item.id)"></el-checkbox>
      <div style="margin-left: 10px">
        <div>
          <span :class="`flag-icon flag-icon-${item.location}`"></span>
          <span style="font-size: 13px; margin-left: 10px">{{item.name}}</span>
        </div>
        <div class="desc">
          <div style="font-size: 12px;color: grey">
            操作系统:
            <i :style="{color: osNameToIcon(item.osName).color}"
               :class="`fa-brands ${osNameToIcon(item.osName).icon}`"></i>
            {{`${item.osName} ${item.osVersion}`}}
          </div>
          <div style="font-size: 12px;color: grey">
            <span style="margin-right: 10px">公网IP: {{item.publicIp}}</span>
          </div>
        </div>
      </div>
    </div>
    <el-divider></el-divider>
    <div style="text-align: center">
      <el-button type="primary" plain style="width: 150px" @click="modify">确认分配</el-button>
    </div>
  </el-scrollbar>
</template>

<style scoped>
.title{
  font-size: 18px;
  font-weight: bold;
  color: var(--el-color-primary);
}
.desc{
  font-size: 14px;
  color: grey;
}
.lite-card{
  display: flex;
  align-items: center;
  background-color: var(--el-bg-color);
  border-radius: 5px;
  padding: 10px;
  border: 1px solid var(--el-border-color);
  margin-bottom: 10px;
}
</style>