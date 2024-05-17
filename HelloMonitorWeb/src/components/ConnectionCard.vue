<script setup>
import {reactive, ref, watch} from "vue";
import {get, post} from "@/net/index.js";
import {ElMessage} from "element-plus";
import Terminal from "@/components/Terminal.vue";
const props = defineProps({
  id: Number,
})
const connection = reactive({
  ip:'',
  port:22,
  username:'',
  password:'',
  loading:false
})
const rules = {
  ip: [{required:true,message:'请输入ip地址',trigger:'blur'}],
  port: [{required:true,message:'请输入端口号',trigger:'blur'}],
  username: [{required:true,message:'请输入用户名',trigger:'blur'}],
  password: [{required:true,message:'请输入密码',trigger:'blur'}]
}
const formRef = ref()
const state = ref(1)

function getSshConfig(id) {
  connection.ip = ''
  get(`api/monitor/ssh?clientId=${id}`, data => {
    Object.assign(connection, data)
    connection.loading = true
  })
}
function savaSshConnection() {
  formRef.value.validate(valid => {
    if (valid) {
      post('api/monitor/ssh-sava',{
        id:props.id,
        ip:connection.ip,
        port:connection.port,
        username:connection.username,
        password:connection.password
      }, ()=> {
        state.value = 2
        ElMessage.success('连接保存成功，正在连接...')

      })
    }
  })
}
watch(() => props.id,id => {
  connection.loading = false
  state.value = 1
  if (id !== -1) {
    getSshConfig(id)
  }
}, {immediate:true})
</script>


<template>
  <div style="width: 100%; height: 100%">
    <div class="login" v-loading="!connection.loading" v-if="state === 1">
      <i style="font-size: 50px" class="fa-solid fa-terminal"></i>
      <div style="margin-top: 10px;font-weight: bold;font-size: 20px">客户端连接信息</div>
      <el-form style="width: 400px;margin: 20px auto" :model="connection"
               :rules="rules" ref="formRef" label-width="150" label-position="left">
        <el-divider style="margin: 10px 0"></el-divider>
        <div style="display: flex;gap: 10px">
          <el-form-item style="width: 100%" label="服务器IP地址" prop="ip">
            <el-input v-model="connection.ip"/>
          </el-form-item>
          <el-form-item style="width: 80px" prop="port" label-width="0">
            <el-input placeholder="端口" v-model="connection.port"/>
          </el-form-item>
        </div>
        <el-form-item prop="username" label="登录用户名">
          <el-input placeholder="请输入用户名..." v-model="connection.username"/>
        </el-form-item>
        <el-form-item prop="password" label="登录密码">
          <el-input placeholder="请输入密码..." type="password" v-model="connection.password"/>
        </el-form-item>
        <el-divider style="margin: 10px 0"></el-divider>
        <el-button type="success" plain @click="savaSshConnection" style="width: 150px">立即连接</el-button>
      </el-form>
    </div>
    <div v-if="state === 2">
      <div style="overflow: hidden;padding: 0 10px 10px 10px">
        <terminal :id="props.id" @dispose="state = 1"/>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login {
  text-align: center;
  padding-top: 50px;
  height: 100%;
  box-sizing: border-box;
}
</style>