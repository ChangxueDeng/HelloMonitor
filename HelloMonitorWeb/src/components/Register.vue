<script setup>
import axios from "axios";

defineProps({
  token: String
})
</script>

<template>
<div class="register">
  <div class="title"><i class="fa-regular fa-square-plus" />添加新主机</div>
  <div class="desc">请按照步骤完成新主机添加</div>
  <el-divider style="margin: 10px 0"></el-divider>
  <div class="sub-title">1.部署客户端</div>
  <div class="desc">在需要监控的服务器上运行监控客户端程序，客户端程序依赖于Java17运行环境，请提前安装</div>
  <div class="sub-title" style="margin-top: 10px">2.输入监控服务器地址</div>
  <div class="desc">此地址用于客户端实时上报运行时状态</div>
  <el-input v-model="axios.defaults.baseURL" readonly></el-input>
  <div class="sub-title" style="margin-top: 10px">3.输入授权码</div>
  <div class="desc">授权码用于验证身份，客户端所有请求都必须携带此参数</div>
  <el-input :model-value="token" readonly></el-input>
</div>
</template>

<style scoped>
.register{
  margin: 15px 15px;
  .title{
    font-size: 18px;
    font-weight: bold;
  }
  .sub-title{
    font-size: 16px;
    font-weight: bold;
    color: dodgerblue;
  }
  .desc{
    font-size: 13px;
    color: grey;
    line-height: 16px;
  }
}
</style>