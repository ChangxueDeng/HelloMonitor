import {defineStore} from "pinia";

export const useStore = defineStore('general', {
    state:() => {
        return{
            user: {
                username: '',
                email: '',
                role: ''
            }
        }
    },
    getters:{
        isAdmin(){
            return this.user.role === 'admin'
        }
    },
    persist: true
})