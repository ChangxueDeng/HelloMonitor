<script setup>
import {ref, onMounted, onBeforeUnmount} from "vue";
import {ElMessage} from "element-plus";
import {AttachAddon} from "xterm-addon-attach/src/AttachAddon";
import {Terminal} from "xterm";
import "xterm/css/xterm.css";

const props = defineProps({
  id:Number
})
function getToken() {
  let authItemName = 'access_token'
  const str = localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName)
  if(!str) return null
  const authObj = JSON.parse(str)
  return authObj.token
}

const socket = new WebSocket(`ws://127.0.0.1:8081/terminal/${props.id}?${getToken()}`)

const emits = defineEmits(['dispose'])
socket.onclose = event => {
  if (event.code !== 1000) {
    ElMessage.warning(`连接失败: ${event.reason}`)
  } else{
    ElMessage.success('连接断开')
  }
  emits('dispose')
}
const attach = new AttachAddon(socket);

const terminalRef = ref(null)
const term = new Terminal({
  lineHeight: 1.2,
  rows: 29,
  cols: 100,
  fontSize: 13,
  fontFamily: "Monaco, Menlo, Consolas, 'Courier New', monospace",
  fontWeight: "bold",
  theme: {
    background: '#000000'
  },
  // 光标闪烁
  cursorBlink: true,
  cursorStyle: 'underline',
  scrollback: 100,
  tabStopWidth: 4,
})
term.loadAddon(attach)

onMounted(()=>{
  term.open(terminalRef.value)
  term.focus()
})

onBeforeUnmount(()=> {
  socket.close()
  term.dispose()
})
</script>

<template>
  <div ref="terminalRef" class="xterm">
  </div>
</template>

<style lang="less" scoped>
:deep(.xterm){
  height: 550px;
}

</style>