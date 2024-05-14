<script setup>
import {findByUnit} from "@/tools/tools.js"
const props = defineProps({
  data: Object
})
</script>

<template>
  <div class="instance-card">
    <div style="display: flex; justify-content: space-between">
      <div>
        <div class="name">
          <span :class="`flag-icon flag-icon-${data.location}`"></span>
          {{data.name}} <i class="fa-regular fa-pen-to-square"></i>
        </div>
        <div class="os">操作系统: {{data.osName}} {{data.osVersion}}</div>
      </div>
      <div class="status" v-if="data.online">
        <i class="fa-regular fa-circle-play" style="color: #95d475"></i>
        <span> 运行中</span>
      </div>
      <div class="status" v-else-if="!data.online">
        <i class="fa-regular fa-circle-stop" style="color: #3c3d3a"></i>
        <span> 离线</span>
      </div>
    </div>
    <el-divider style="margin: 10px 0"></el-divider>
    <div class="network">
      <span>公网IP: {{data.ip}}</span>
      <i class="fa-solid fa-copy" style="margin-left: 2px"></i>
    </div>

    <div class="hardware">
      <div style="font-size: 13px">处理器: {{data.cpuName}}</div>
      <i class="fa-solid fa-microchip"></i>
      <span style="margin-right: 10px"> {{data.cpuCore}} cpu</span>
      <i class="fa-solid fa-memory"></i>
      <span style="margin-right: 10px"> {{data.memory.toFixed(1)}}GB</span>
      <i class="fa-solid fa-hdd"> </i>
      <span style="margin-right: 10px">  {{ data.disk.toFixed(1)}}GB</span>
    </div>
    <div class="progress">
      <span>CPU: {{(data.cpuUsage * 100).toFixed(1)}}%</span>
      <el-progress status="success" :percentage="data.cpuUsage * 100" :stroke-width="5" :show-text="false"></el-progress>
    </div>
    <div class="progress">
      <span>内存: <b>{{(data.memoryUsage).toFixed(1)}}</b> GB</span>
      <el-progress status="success" :percentage="data.memoryUsage / data.memory * 100" :stroke-width="5" :show-text="false"></el-progress>
    </div>
    <div class="network-speed">
      <div>网络流量</div>
      <div>
        <i class="fa-solid fa-arrow-up"></i>
        <span> {{` ${findByUnit(data.networkUpload, "KB")}/s`}}</span>
        <el-divider direction="vertical"></el-divider>
        <i class="fa-solid fa-arrow-down"></i>
        <span> {{` ${findByUnit(data.networkDownload, "KB")}/s`}}</span>
      </div>
    </div>
  </div>
</template>

<style lang="less" scoped>
:deep(.el-progress-bar__outer) {
  background-color: rgba(149, 212, 117, 0.36);
}
:deep(.el-progress-bar__inner) {
  background-color: #95d475;
}
.dark .instance-card{
  color: #d9d9d9;
}
.instance-card{
  width: 320px;
  padding: 15px;
  background-color: var(--el-bg-color);
  border-radius: 5px;
  box-sizing: border-box;
  color: #606060;
  .name{
    font-size: 15px;
    font-weight: bold;
  }
  .os{
    color: grey;
    font-size: 14px;
  }
  .network{
    font-size: 14px;
    .fa-copy{
      &:hover{
        cursor: pointer;
        color: dodgerblue;
      }
    }
  }
  .hardware{
    margin-top: 5px;
    font-size: 13px;
  }
  .progress{
    margin-top: 10px;
    font-size: 12px;
  }
  .network-speed{
    margin-top: 10px;
    font-size: 12px;
    display: flex;
    justify-content: space-between;
  }
}
</style>