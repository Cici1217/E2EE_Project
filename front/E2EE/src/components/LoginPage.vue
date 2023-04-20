<template>
  <div>
    Username: <input type="text" v-model="loginInfo.userID" placeholder="Input username"/>
    <br><br>
    Password: <input type="text" v-model="loginInfo.password" placeholder="Input pwd"/>
    <br><br>
    <button v-on:click="login">Login</button>
    <br><br>
  </div>
</template>

<script>
import {useRouter} from "vue-router";
import router from "@/router";
import {useStore} from "vuex";

export default {
  setup() {
    const store = useStore()  // 该方法用于返回store 实例
    console.log("store:::::")
    console.log(store)  // store 实例对象
    const router = useRouter();
    return {store};
  },
  name: "LoginPage",
  data() {
    return {
      loginInfo: {
        userID: '',
        password: '',
        registrationID: '',
      },
      responseResult: {
        uid:'',
        registrationId:'',
        identityKeyPair:'',
        preKeys:'',
        signedPreKey:''
      },
    }
  },
  methods: {
    login() {
      this.$http.post('/login', {
        username: this.loginInfo.userID,
        password: this.loginInfo.password,
      }).then(successResponse => {
        if (successResponse.data.code === 200) {
          this.store.dispatch('registration', this.loginInfo.userID);
          router.replace("/chat");
        } else {
          alert("wrong")
        }
      })
    },
  },
}
</script>

<style></style>
