<script setup>
import {findByUnit, precessStatus} from "@/tools/tools.js"
import {useClipboard} from "@vueuse/core";
import {ElMessage, ElMessageBox} from "element-plus";
import {post} from "@/net/index.js";
const props = defineProps({
  data: Object,
  update: Function
})
const {copy} = useClipboard()
const copyIp = ()=> copy(props.data.ip).then(() => {ElMessage.success("复制成功")})
function rename() {
  ElMessageBox.prompt('请输入新的服务器主机名称', '修改名称', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputPattern: /^[a-zA-Z0-9_\u4e00-\u9fa5]{1,10}$/,
    inputErrorMessage: '请输入正确的名称,名称只能保护中英文字符、数字和下划线',
    inputValue: props.data.name,
  }).then(({value}) => post('/api/monitor/rename', {
    id: props.data.id, name: value
  }, ()=> {
    ElMessage.success("修改成功")
    props.update()
  }, ()=> {
    ElMessage.error("修改失败")
  }))
}
</script>

<template>
  <div class="instance-card">
    <div style="display: flex; justify-content: space-between">
      <div>
        <div class="name">
          <span :class="`flag-icon flag-icon-${data.location}`"></span>
          {{data.name}} <i class="fa-regular fa-pen-to-square interact-item" @click.stop="rename"></i>
        </div>
        <div class="os">操作系统: {{data.osName}} {{data.osVersion}}</div>
      </div>
      <div class="status" v-if="data.online">
        <i class="fa-regular fa-circle-play" style="color: #62e820"></i>
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
      <i class="fa-solid fa-copy interact-item" style="margin-left: 2px" @click.stop="copyIp"></i>
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
      <el-progress :status="precessStatus(data.cpuUsage * 100)" :percentage="data.cpuUsage * 100" :stroke-width="5" :show-text="false"></el-progress>
    </div>
    <div class="progress">
      <span>内存: <b>{{(data.memoryUsage).toFixed(1)}}</b> GB</span>
      <el-progress :status="precessStatus(data.memoryUsage / data.memory * 100)" :percentage="data.memoryUsage / data.memory * 100" :stroke-width="5" :show-text="false"></el-progress>
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
.instance-card{
  width: 320px;
  padding: 15px;
  background-color: var(--el-bg-color);
  border-radius: 5px;
  box-sizing: border-box;
  color: #606060;
  transition: .3s;
  &:hover{
    cursor: pointer;
    scale: 1.02;
  }
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
.interact-item{
  transition: .3s;
  &:hover{
    cursor: pointer;
    color: dodgerblue;
    scale: 1.1;
    opacity: 0.9;
  }
}
</style>