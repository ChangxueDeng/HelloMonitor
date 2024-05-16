<script setup>

import {computed, watch} from "vue";
import {reactive} from "vue";
import {get, post} from "@/net/index.js"
import {precessStatus, findByUnit, cpuNameToImage, osNameToIcon, copyIp, rename} from "@/tools/tools.js";
import {ElMessage, ElMessageBox} from "element-plus";
import RuntimeHistory from "@/components/RuntimeHistory.vue";
import {Delete} from "@element-plus/icons-vue";

const location = [
  {name: 'cn', desc: '中国大陆'},
  {name: 'us', desc: '美国'},
  {name: 'jp', desc: '日本'},
  {name: 'de', desc: '德国'},
  {name: 'kr', desc: '韩国'},
  {name: 'hk', desc: '中国香港'},
  {name: 'sg', desc: '新加坡'},
]
const emits = defineEmits(['delete'])
const props = defineProps({
  id : Number,
  update: Function
})
const details = reactive({
  base:{},
  runtime:{
    list:[]
  },
  editNode: false,
})
const nodeEdit = reactive({
  node: '',
  location: ''
})
const enableNodeEdit = ()=> {
  details.editNode = true,
  nodeEdit.node = details.base.node,
  nodeEdit.location = details.base.location
}
function updateDetails() {
  props.update()
  init(props.id)
}
function submitNodeEdit() {
  post('api/monitor/rename-node', {
    clientId : details.base.id,
    node: nodeEdit.node,
    location: nodeEdit.location
  }, ()=> {
    details.editNode = false
    ElMessage.success("修改节点信息成功")
    updateDetails()
  })
}
const init = id => {
  if (id !== -1) {
    details.base = {}
    details.runtime = {list:[]}
    get(`/api/monitor/details?clientId=${id}`, data => {
      Object.assign(details.base, data)
    })
    get(`/api/monitor/runtime-history?clientId=${id}`, data=> {
      Object.assign(details.runtime, data)
    })
  }
}
setInterval(()=> {
  if (props.id !== -1 && details.runtime) {
    get(`api/monitor/runtime-now?clientId=${props.id}`, data => {
      if (details.runtime.list.length >= 360) {
        details.runtime.list.splice(0,-1)

      }
      details.runtime.list.push(data)
    })
  }
}, 10000)

function deleteClient(clientId) {
  ElMessageBox.confirm('确定要删除主机吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    get(`api/monitor/delete?clientId=${clientId}`, () => {
      props.update()
      emits('delete')
      ElMessage.success("删除成功")
    })
  }).catch(() => {})
  }

const now = computed(()=> details.runtime.list[details.runtime.list.length - 1]);
watch(()=> props.id, value => init(value), {immediate:true})

</script>

<template>
    <div class="client-details" v-loading="Object.keys(details.base).length === 0">
      <div v-if="Object.keys(details.base).length !== 0">
        <div style="display: flex; justify-content: space-between">
          <div class="title">
            <i class="fa-solid fa-server"></i>服务器信息
          </div>
          <el-button :icon="Delete" type="danger" plain @click="deleteClient(props.id)">删除此主机</el-button>
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
            <i class="fa-regular fa-pen-to-square interact-item" @click.stop="rename(details.base.id, details.base.name,updateDetails)"></i>
          </div>
          <div>
            <span>运行状态</span>
            <span>
            <i class="fa-regular fa-circle-play" style="color: #62e820" v-if="details.base.online"></i>
            <i class="fa-regular fa-circle-stop" style="color: #3c3d3a" v-else></i>
            <span> {{details.base.online ? '运行中' : '离线'}}</span>
          </span>
          </div>
          <div v-if="!details.editNode">
            <span>服务器节点</span>
            <span :class="`flag-icon flag-icon-${details.base.location}`"></span>
            <span>{{details.base.node}}</span>
            <i class="fa-regular fa-pen-to-square interact-item" @click.stop="enableNodeEdit"></i>
          </div>
          <div v-else>
            <span>服务器节点</span>
            <div style="display: inline-block; height: 15px;">
              <div style="display: flex">
                <el-select v-model="nodeEdit.location" style="width: 80px" size="small">
                  <el-option v-for="item in location" :value="item.name">
                    <span :class="`flag-icon flag-icon-${item.name}`"></span>&nbsp;
                    {{item.desc}}
                  </el-option>
                </el-select>
                <el-input v-model="nodeEdit.node" style="margin-left: 10px"
                          size="small" placeholder="请输入节点名称"></el-input>
                <div style="margin-left: 10px">
                  <i class="fa-solid fa-check interact-item" @click.stop="submitNodeEdit" />
                </div>
              </div>
            </div>
          </div>
          <div>
            <span>公网IP</span>
            <span>{{details.base.ip}}</span>
            <i class="fa-solid fa-copy interact-item" style="margin-left: 2px" @click.stop="copyIp(details.base.ip)"></i>
          </div>
          <div style="display: flex">
            <span>处理器</span>
            <span> {{details.base.cpuName}}</span>
            <el-image style="margin-left: 10px; height: 20px" :src="`/cpu-icons/${cpuNameToImage(details.base.cpuName)}`"></el-image>
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
            <i :style="{color: osNameToIcon(details.base.osName).color}" :class = "`fa-brands ${osNameToIcon(details.base.osName).icon}`"></i>
            <span style="margin-left: 10px">{{`${details.base.osName} ${details.base.osVersion}`}}</span>
          </div>
        </div>
        <div class="title">
          <i class="fa-solid fa-gauge-high"></i>
          实时监控
        </div>
        <el-divider style="margin: 10px 0"></el-divider>
        <div v-if="details.base.online" v-loading="!details.runtime.list.length" style="min-height: 200px">
          <div style="display: flex" v-if="details.runtime.list.length">
            <el-progress type="dashboard" :width="100" :percentage="now.cpuUsage * 100" :status="precessStatus((now.cpuUsage * 100).toFixed(1))">
              <div style="font-size: 17px; font-weight: bold; color: initial">CPU</div>
              <div style="font-size: 13px; color: grey; margin-top: 5px">{{(now.cpuUsage  * 100 ).toFixed(1)}}%</div>
            </el-progress>
            <el-progress type="dashboard" :width="100" :percentage="now.memoryUsage / details.runtime.memory * 100"
                         :status="precessStatus(now.memoryUsage * 100 / details.runtime.memory)"
                         style="margin-left: 20px">
              <div style="font-size: 17px; font-weight: bold; color: initial">内存</div>
              <div style="font-size: 13px; color: grey; margin-top: 5px">{{(now.memoryUsage).toFixed(1)}} GB</div>
            </el-progress>
            <div style="flex: 1; margin-left: 30px; display: flex; flex-direction: column; height: 80px">
              <div style="flex: 1; font-size: 14px">
                <div>实时网络速度</div>
                <div>
                  <i class="fa-solid fa-arrow-up" style="color: sandybrown"></i>
                  <span> {{` ${findByUnit(now.networkUpload, 'KB')}/s`}}</span>
                  <el-divider direction="vertical"></el-divider>
                  <i class="fa-solid fa-arrow-down" style="color:yellowgreen;"></i>
                  <span> {{` ${findByUnit(now.networkDownload, 'KB')}/s`}}</span>
                </div>
              </div>
              <div>
                <div style="font-size: 13px; display: flex; justify-content: space-between">
                  <div>
                    <i class="fa-solid fa-hard-drive"></i>
                    <span> 磁盘使用情况 </span>
                  </div>
                  <div> {{(now.diskUsage).toFixed(1)}} GB / {{(details.runtime.disk).toFixed(1)}}GB</div>
                </div>
                <el-progress type="line" :status="precessStatus(now.diskUsage / details.runtime.disk * 100)" :percentage="60" :show-text="false"></el-progress>
              </div>
            </div>
          </div>
          <el-divider></el-divider>
          <runtime-history :data="details.runtime.list"></runtime-history>
        </div>
        <div v-else>
          <el-empty description="服务器处于离线状态，无法获取实时信息"></el-empty>
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
    background-color: rgba(246, 116, 116, 0.59);
  }
  .el-progress-bar__inner{
    background-color: #ff0000;
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