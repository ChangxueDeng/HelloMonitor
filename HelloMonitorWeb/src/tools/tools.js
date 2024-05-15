import {useClipboard} from "@vueuse/core";
import {ElMessage, ElMessageBox} from "element-plus";
import {post} from "@/net/index.js";

function findByUnit(value, unit) {
    const units = ['B', 'KB', 'MB', 'GB', 'TB', 'PB']
    let index = units.indexOf(unit)
    while (((value < 1  && value !== 0) || value >= 1024)&&(index >= 0 || index < units.length)){
        if (value >= 1024) {
            value /= 1024
            index++
        } else {
            value *= 1024
            index--
        }
    }
    return `${parseInt(value)} ${units[index]}`
}

function precessStatus(precess){
    if (precess < 50) {
        return 'success'
    } else if (precess < 80){
        return 'warning'
    } else {
        return 'exception'
    }
}
function cpuNameToImage(name) {
    if (name.indexOf("Apple") >= 0) {
        return 'Apple.png'
    } else if (name.indexOf("Intel") >= 0) {
        return 'Intel.png'
    } else if (name.indexOf("AMD") >= 0) {
        return 'AMD.png'
    }
}

function osNameToIcon(name) {
    if (name.indexOf("Ubuntu") >= 0) {
        return {icon: "fa-ubuntu", color: '#db4c1a'}
    }else if (name.indexOf("Centos") >= 0) {
        return {icon: "fa-centos", color: '#9dcd30'}
    } else if (name.indexOf("Windows") >= 0) {
        return {icon: "fa-windows", color: '#3578b9'}
    } else if (name.indexOf("Debian") >= 0) {
        return {icon: "fa-debian", color: '#a80036'}
    } else if (name.indexOf("Redhat") >= 0) {
        return {icon: "fa-redhat", color: '#e60000'}
    } else if (name.indexOf("Fedora") >= 0) {
        return {icon: "fa-fedora", color: '#4585c4'}
    } else if (name.indexOf("Suse") >= 0) {
        return {icon: "fa-suse", color: '#30b977'}
    } else if (name.indexOf("macOS") >= 0) {
        return {icon: "fa-apple", color: 'grey'}
    }
    else {
        return {icon: "fa-linux", color: '#f0c674'}
    }
}
const {copy} = useClipboard()
const copyIp = (ip)=> copy(ip).then(() => {ElMessage.success("复制成功")})
function rename(id,name, success) {
    ElMessageBox.prompt('请输入新的服务器主机名称', '修改名称', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^[a-zA-Z0-9_\u4e00-\u9fa5]{1,10}$/,
        inputErrorMessage: '请输入正确的名称,名称只能保护中英文字符、数字和下划线',
        inputValue: name,
    }).then(({value}) => post('/api/monitor/rename', {
        id: id,
        name: value
    }, ()=> {
        ElMessage.success("修改成功")
        success()
    }, ()=> {
        ElMessage.error("修改失败")
    }))
}
export {findByUnit, precessStatus, cpuNameToImage, osNameToIcon, copyIp, rename}