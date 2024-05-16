<script setup>
import {Message, User,Lock} from "@element-plus/icons-vue";
import {reactive, ref} from "vue";
import {copyIp, osNameToIcon} from "@/tools/tools.js";
import {post} from "@/net/index.js";
import {ElMessage} from "element-plus";
defineProps({
  clients: Array
})
const emits = defineEmits(['create'])
const form = reactive({
  username: '',
  password: '',
  email: '',
})
const formRef = ref()
const valid = ref(false)
const onValidate = (prop, isValid) => valid.value = isValid

const validateUsername = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入用户名'))
  } else if(!/^[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(value)){
    callback(new Error('用户名不能包含特殊字符，只能是中文/英文'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    {required: true, message: '请输入用户名', trigger: ['blur', 'change']},
    {validator: validateUsername, trigger: ['blur', 'change']},
    {min: 2, max: 8, message: '用户名的长度必须在2-8个字符之间', trigger: ['blur', 'change']},
  ],
  password: [
    {required: true, message: '请输入密码', trigger: ['blur', 'change']},
    {min: 6, max: 16, message: '密码的长度必须在6-16个字符之间', trigger: ['blur', 'change']}
  ], email: [
    {required: true, message: '请输入邮件地址', trigger: ['blur', 'change']},
    {type: 'email', message: '请输入合法的电子邮件地址', trigger: ['blur', 'change']}
  ]
}
const selectedClients = []
const select = (state, id) => {
  if (state) {
    selectedClients.push(id)
  } else {
    const index = selectedClients.indexOf(id)
    selectedClients.splice(index, 1)
  }
}
function create() {
  formRef.value.validate((valid) => {
    if (selectedClients.length === 0) {
      ElMessage.error("请选择至少一个服务器")
    } else if (valid) {
      post("/api/user/sub/create", {
        username: form.username,
        password: form.password,
        email: form.email,
        clients: selectedClients
      },() => {
        ElMessage.success("创建成功")
        formRef.value.resetFields()
        emits('create')
      }, (message) => {
        ElMessage.warning(message)
      })
    }
  })
}
</script>

<template>
  <div style="padding: 15px 20px; height: 100%;">
    <div style="display: flex; flex-direction: column; height: 100%;">
      <div>
        <div class="title"><i class="fa-solid fa-user-plus"></i>添加子用户</div>
        <div class="desc">子用户同样用于管理服务器，但是只能管理被分配的服务器</div>
        <div>
          <el-form label-position="top" :rules="rules" :model="form"
                   @validate="onValidate" ref="formRef">
            <el-form-item label="用户名" prop="username">
              <el-input type="text" v-model="form.username"
                        :prefix-icon="User" placeholder="子账户用户名" maxlength="16"/>
            </el-form-item>
            <el-form-item label="电子邮件" prop="email">
              <el-input  v-model="form.email"
                        :prefix-icon="Message" placeholder="子账户电子邮件" maxlength="16"/>
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input type="password" v-model="form.password"
                        :prefix-icon="Lock" placeholder="子账户密码" maxlength="16"/>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <el-divider></el-divider>
      <div>
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
                  <span style="margin-right: 10px">公网IP: {{item.ip}}</span>
                </div>
              </div>
            </div>
          </div>
          <el-divider></el-divider>
        </el-scrollbar>

        <div style="text-align: center">
          <el-button @click="create()" type="success" plain size="large">立即创建</el-button>
        </div>
      </div>
    </div>
  </div>
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