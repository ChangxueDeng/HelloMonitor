<script setup>
import {ref, reactive} from "vue";
import {ElMessage} from "element-plus";
import {post} from "@/net/index.js";
import {Refresh, Switch, Lock, Plus} from "@element-plus/icons-vue";
import router from "@/router/index.js";
import {logout} from "@/net/index.js";

const emailFormRef = ref()
const form = reactive({
  password: '',
  new_password:'',
  new_repeat_password:'',
})
const rule = {
  email:[{required: true, message: '请输入电子邮箱', trigger:['blur', 'change']},
    {type: 'email', message: '请输入合法的邮件地址', trigger: ['blur','change']}],
  code:[{required:true, trigger:['blur','change'],message:'请输入验证码'},
    {min:6, max: 6, message: '验证码为6个字符', trigger: ['blur','change']}],
}
//邮箱信息表单
const emailForm = reactive({
  email: '',
  code: ''
})
const coldTime = ref(0)
const isValidEmail = ref(false)
const onValidateEmail = (prop, isValid) => {
  if (prop === 'email') {
    isValidEmail.value = isValid
  }
}
function sendEmailCode(){
  coldTime.value = 60;
  post(`api/auth/ask-code?&email=${emailForm.email}&type=modify`,{},()=>{
    ElMessage.success(`验证码已发送至指定邮箱:${emailForm.email}，请注意查收`)
    const timer = setInterval(()=> {
      coldTime.value--
      if(coldTime.value === 0) {
        clearInterval(timer)
      }
    }, 1000)
  }, (message)=> {
    ElMessage.warning(message)
    coldTime.value = 0
  })
}

function modifyEmail(){
  emailFormRef.value.validate((isValid)=>{
    if (isValid) {
      post('api/user/reset-email', emailForm, ()=>{
        ElMessage.success("电子邮箱修改成功")
        emailForm.code = ''
      })
    }
  })
}
const formRef = ref()

const validatePassword = (rule, value, callback)=>{
  if (value === '') {
    callback(new Error('请再次输入新密码'))
  } else if (value !== form.new_password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  password:[{
    required:true, message:'请输入原来的密码', trigger:'blur'
  }],
  new_password:[
    {required: true, message:'请输入新的密码', trigger:['blur', 'change']},
    {min: 6, max: 16, message: '密码长度在6-16个字符之间', trigger: ['blur', 'change']}],
  new_repeat_password:[
    {required: true, validator: validatePassword, trigger: ['blur', 'change']}
  ]
}

const valid = ref(false)
const onValidate = (prop, isValid) => valid.value = isValid;

function resetPassword(){
  formRef.value.validate(isValid=> {
    if(isValid) {
      post('api/user/change-password', {
        oldPassword: form.password,
        newPassword: form.new_password,
      }, ()=>{
        ElMessage.success("修改密码成功,请重新登录")
        formRef.value.resetFields()
        logout(()=> router.push('/'))
      }, (message) => {
        ElMessage.warning(message)
        formRef.value.resetFields()
      })
    }
  })
}
</script>

<template>
  <div style="display: flex; gap: 10px; justify-content: center; flex-direction: row; margin-top: 30px;">
    <div>
      <div class="lite-card" style="width: 500px">
        <div class="title"><i class="fa-solid fa-key"></i> 修改密码</div>
        <el-form style="margin: 20px 10px 10px 10px" label-position="left" :model="emailForm" :rules="rule"
                 ref="emailFormRef" @validate="onValidateEmail" label-width="120">
          <el-form-item label="电子邮箱地址" prop="email">
            <el-input v-model="emailForm.email"></el-input>
          </el-form-item>
          <el-row style="width: 100%">
            <el-col :span="18">
              <el-form-item prop="code">
                <el-input placeholder="请输入获取的验证码" v-model="emailForm.code"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5" style="margin-left: 15px">
              <el-button type="primary" plain @click="sendEmailCode()" :disabled="!isValidEmail ||coldTime > 0">{{coldTime > 0 ?
                  '请等待：' + coldTime + '秒' : '获取验证码'}}</el-button>
            </el-col>
          </el-row>
          <div style="display: flex; justify-content: flex-end;">
            <el-button type="primary" :icon="Refresh" plain @click="modifyEmail()"> 保存邮箱</el-button>
          </div>
        </el-form>
      </div>
      <div class="lite-card" style="width: 500px; margin-top: 20px">
        <div class="title"><i class="fa-solid fa-envelope"></i> 修改邮箱</div>
        <el-form label-width="100" style="margin: 20px;" :model="form" :rules="rules" ref="formRef" @validate="onValidate">
          <el-form-item label="当前密码" prop="password">
            <el-input :prefix-icon="Lock" placeholder="当前密码" maxlength="16" minlength="6" v-model="form.password"></el-input>
          </el-form-item>
          <el-form-item label="新密码" prop="new_password">
            <el-input type="password" :prefix-icon="Lock" placeholder="新密码" maxlength="16" minlength="6" v-model="form.new_password"></el-input>
          </el-form-item>
          <el-form-item label="确认新密码" prop="new_repeat_password">
            <el-input type="password" :prefix-icon="Lock" placeholder="确认新密码" maxlength="16"  minlength="6" v-model="form.new_repeat_password"></el-input>
          </el-form-item>
        </el-form>
        <div style="text-align: center">
          <el-button  type="success" plain :icon="Switch" :disabled="!valid" @click="resetPassword()">立即重置密码</el-button>
        </div>
      </div>
    </div>
    <div>
      <div class="lite-card" style="width: 500px">
        <div class="title"><i class="fa-solid fa-users"></i> 子用户管理</div>
        <el-divider></el-divider>
        <el-empty :image-size="100" description="子用户为空">
          <el-button :icon="Plus" type="primary">添加子用户</el-button>
        </el-empty>
      </div>
    </div>
    </div>
</template>

<style scoped>
.lite-card{
  background-color: var(--el-bg-color);
  border-radius: 5px;
  padding: 10px;
}
.title{
  font-size: 18px;
  font-weight: bold;
  color: var(--el-color-primary);
}
</style>