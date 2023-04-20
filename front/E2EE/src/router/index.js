import {createRouter, createWebHashHistory} from 'vue-router'
import LoginPage from "@/components/LoginPage.vue";
import ChatPage from "@/components/ChatPage.vue";
import Register from "@/components/Register";

const routes = [
    {
        path: '/login',
        name: 'Login',
        component: LoginPage
    },
    {
        path: '/chat',
        name: 'Chat',
        component: ChatPage
    },
    {
        path: '/register',
        name: 'Register',
        component: Register
    }
]

const router = createRouter({
    history: createWebHashHistory(),
    routes
})

export default router
