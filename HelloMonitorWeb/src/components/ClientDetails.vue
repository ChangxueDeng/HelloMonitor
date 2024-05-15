<script setup>

import {watch} from "vue";
import {reactive} from "vue";
import {get} from "@/net/index.js"
import {precessStatus, findByUnit} from "@/tools/tools.js";
import {useClipboard} from "@vueuse/core";
import {ElMessage} from "element-plus";

const props = defineProps({
  id : Number
})
const details = reactive({
  base:{},
  runtime:{}
})
const {copy} = useClipboard()
const copyIp = ()=> copy(details.base.ip).then(() => {ElMessage.success("复制成功")})
watch(()=> props.id, value => {
  if (value !== -1) {
    details.base = {}
    get(`/api/monitor/details?clientId=${value}`, data => {
      Object.assign(details.base, data)
    })
  }
}, {immediate:true})

</script>

<template>
  <div class="client-details" v-loading="Object.keys(details.base).length === 0">
    <div v-if="Object.keys(details.base).length !== 0">
      <div class="title">
        <i class="fa-solid fa-server"></i>服务器信息
      </div>
      <el-divider style="margin: 10px 0"></el-divider>
      <div class="details-list">
        <div>
          <span>服务器ID</span>
          <span>{{details.base.id}}</span>
        </div>
        <div>
          <span>服务器名称</span>
          <span>{{details.base.name}}</span>
        </div>
        <div>
          <span>运行状态</span>
          <span>
            <i class="fa-regular fa-circle-play" style="color: #62e820" v-if="details.base.online"></i>
            <i class="fa-regular fa-circle-stop" style="color: #3c3d3a" v-else></i>
            <span> {{details.base.online ? '运行中' : '离线'}}</span>
          </span>
        </div>
        <div>
          <span>服务器节点</span>
          <span :class="`flag-icon flag-icon-${details.base.location}`"></span>
          <span>{{details.base.name}}</span>
        </div>
        <div>
          <span>公网IP</span>
          <span>{{details.base.ip}}</span>
          <i class="fa-solid fa-copy interact-item" style="margin-left: 2px" @click.stop="copyIp"></i>
        </div>
        <div >
          <span>处理器</span>
          <span> {{details.base.cpuName}}</span>
        </div>
        <div>
          <span>硬件配置信息</span>
          <i class="fa-solid fa-microchip"></i>
          <span >{{`${details.base.cpuCore} CPU核心数 /` }} </span>
          <i class="fa-solid fa-memory"></i>
          <span >{{`${details.base.memory.toFixed(1)} GB 内存容量 / `}}</span>
          <i class="fa-solid fa-hdd"> </i>
          <span >{{`${details.base.disk.toFixed(1)} GB 硬盘容量`}}</span>
        </div>
        <div>
          <span>操作系统</span>
          <span>{{`${details.base.osName} ${details.base.osVersion}`}}</span>
        </div>
      </div>
      <div class="title">
        <i class="fa-solid fa-gauge-high"></i>
        实时监控
      </div>
      <el-divider style="margin: 10px 0"></el-divider>
      <div style="display: flex">
        <el-progress type="dashboard" :width="100" :percentage="20" :status="precessStatus((details.runtime.cpuUsage * 100).toFixed(1))">
          <div style="font-size: 17px; font-weight: bold; color: initial">CPU</div>
          <div style="font-size: 13px; color: grey; margin-top: 5px">20%</div>
        </el-progress>
        <el-progress type="dashboard" :width="100" :percentage="60" :status="precessStatus((details.runtime.memoryUsage * 100).toFixed(1))"
                     style="margin-left: 20px">
          <div style="font-size: 17px; font-weight: bold; color: initial">内存</div>
          <div style="font-size: 13px; color: grey; margin-top: 5px">16.6 GB</div>
        </el-progress>
        <div style="flex: 1; margin-left: 30px; display: flex; flex-direction: column; height: 80px">
          <div style="flex: 1; font-size: 14px">
            <div>实时网络速度</div>
            <div>
              <i class="fa-solid fa-arrow-up" style="color: sandybrown"></i>
              <span> {{` /s`}}</span>
              <el-divider direction="vertical"></el-divider>
              <i class="fa-solid fa-arrow-down" style="color:yellowgreen;"></i>
              <span> {{` /s`}}</span>
            </div>
          </div>
          <div>
            <div style="font-size: 13px; display: flex; justify-content: space-between">
              <div>
                <i class="fa-solid fa-hard-drive"></i>
                <span> 磁盘使用情况 </span>
              </div>
              <div> 6.6 GB / 40.0 GB</div>
            </div>
            <el-progress type="line" status="success" :percentage="60" :show-text="false"></el-progress>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="less" scoped>
:deep(.is-success) {
  .el-progress-bar__outer {
    background-color: rgba(149, 212, 117, 0.36);
  }
  .el-progress-bar__inner{
    background-color: #95d475;
  }
  .el-progress-circle__track {
    stroke: rgba(149, 212, 117, 0.36);
  }
  .el-progress-circle__path {
    stroke: #95d475;
  }
}

:deep(.is-warning) {
  .el-progress-bar__outer {
    background-color: #ffa04622;
  }
  .el-progress-bar__inner{
    background-color: #ffa046;
  }
  .el-progress-circle__track {
    stroke: #ffa04622;
  }
  .el-progress-circle__path {
    stroke: #ffa046;
  }
}
:deep(.is-exception) {
  .el-progress-bar__outer {
    background-color: #ff0000;
  }
  .el-progress-bar__inner{
    background-color: rgba(246, 116, 116, 0.59);
  }
  .el-progress-circle__track {
    stroke: rgba(246, 116, 116, 0.59);
  }
  .el-progress-circle__path {
    stroke: #ff0000;
  }
}
.interact-item{
  transition: .3s;
  &:hover{
    cursor: pointer;
    color: dodgerblue;
    scale: 1.1;
    opacity: 0.9;
  }
}
.client-details{
  height: 100%;
  width: 100%;
  padding: 20px;
  .title{
    font-size: 18px;
    font-weight: bold;
    color: deepskyblue;
  }
  .details-list{
    font-size: 14px;
    & div{
      margin-bottom: 10px;

      & span:first-child{
        color: grey;
        font-size: 13px;
        font-weight: bold;
        width: 120px;
        display: inline-block;
      }
      & span{
        font-weight: bold;
      }
    }
  }
}
</style>