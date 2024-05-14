<script setup>
defineProps({
  name: String,
  activate: Boolean
})
</script>

<template>
  <div :class="`tab-item ${activate ? 'active' : ''}`">{{name}}</div>
</template>

<style scoped>
.tab-item{
  text-align: center;
  width: 60px;
  height: 60px;
  justify-items: center;
  box-sizing: border-box;
  transition: color .3s;
  line-height: 60px;
  &:hover{
    cursor: pointer;
    color: var(--el-color-primary);
  }
}
.active{
  border-bottom: 2px solid var(--el-color-primary);
}
</style>