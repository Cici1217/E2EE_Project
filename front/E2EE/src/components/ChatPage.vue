<template>
  <div>
    <input type="text" v-model="message.destinationUserId" placeholder="destinationUserID">
    <input type="text" v-model="message.destinationRegistrationId" placeholder="destinationRegistrationID">
    <input type="text" v-model="message.groupId" placeholder="groupId">;
    <input type="text" v-model="message.myMsg" placeholder="myMsg">
    <button v-on:click="send">Send</button>
    <button v-on:click="get">GetKeys</button>
    <div>{{ recvMsg }}</div>
    <div>{{ msg }}</div>
    <div>
      <button v-on:click="onStore">Store</button>
      <button v-on:click="onCheck">Check</button>
      <button v-on:click="onDelete">Delete</button>
    </div>
  </div>
</template>

<script>
import {useStore} from 'vuex'
import router from "@/router"; // 引入useStore 方法
export default {
  setup() {
    const store = useStore()  // 该方法用于返回store 实例
    console.log(store)  // store 实例对象
    return {store}
  },
  name: "ChatPage",
  created() {
    console.log('created')
    this.initWebSocket()
  },
  destroyed() {
    //销毁
    this.websocketClose();
  },
  data() {
    return {
      message: {
        groupId: -1,
        destinationUserId: '',
        destinationRegistrationId: '',
        myMsg: '',
      },
      websocket: null,
      recvMsg: {},
      msg: '',
    }
  },
  methods: {
    send() {
      console.log('send')

      if (this.message.groupId !== -1) {
        this.$http.post('/groupOrIndividual', {
          groupId: this.message.groupId
        }).then(async e => {
          for (const key in e.data){
            // alert(key+' '+e.data[key])
            let getBundleResult = await this.store.dispatch('get-key-bundle-of', key);
            if(getBundleResult){
              this.message.destinationUserId = key;
              this.message.destinationRegistrationId = e.data[key];
              this.message.groupId=-1;
              let cipherText = await this.store.dispatch('encrypt-message', this.message);
              await this.websocketSend(JSON.stringify(cipherText));
            }
            // this.store.dispatch('get-key-bundle-of', key)
            //     .then(result => {
            //         if(result){
            //             this.message.destinationUserId = key;
            //             this.message.destinationRegistrationId = e.data[key];
            //             this.message.groupId=-1
            //             this.store.dispatch('encrypt-message', this.message)
            //                 .then(res => {
            //                     alert('send'+res)
            //                     this.websocketSend(JSON.stringify(res));
            //                 });
            //         }
            //     });
          }
        })

      } else {
        alert('not group')
        alert(JSON.stringify(this.message))
        this.store.dispatch('encrypt-message', this.message)
            .then(res => {
              this.websocketSend(JSON.stringify(res));
            });
      }


    },
    get() {
      this.$http.get('/keyOf/' + this.message.destinationUserId)
          .then(response => {
            console.log((response));
            this.recvMsg = response.data;
            this.store.dispatch('process-key', response.data)
                .then(res => {
                  if (res === true) {
                    console.log("ok");
                    return 'ok';
                  }
                });
          })

    },
    // onConfirm() {
    //   //需要传输的数据
    //   let data = {
    //     code: 1,
    //     item: '传输数据'
    //   }
    //   this.websocketSend(JSON.stringify(data))
    // },
    initWebSocket() { // 初始化websocket
      let url = 'ws://localhost:9090/websocket/' + this.store.getters.getUserId;
      alert(url)
      this.websock = new WebSocket(url);
      this.websock.onmessage = this.websocketOnMessage
      this.websock.onerror = this.websocketOnError
      // this.websock.onopen = this.websocketOnOpen
      this.websock.onclose = this.websocketClose
    },
    // websocketOnOpen() { // 连接建立之后执行send方法发送数据
    //   let data = {
    //     code: 0,
    //     msg: {
    //       userId: this.store.getters.getUserId,
    //       registrationId: this.store.getters.getRegistrationId,
    //     }
    //   }
    //   // alert('post' + JSON.stringify(data))
    //   // this.websocketSend(JSON.stringify(data))
    // },
    websocketOnError() {
      console.log('WebSocket连接失败')
    },
    websocketOnMessage(e) { // 数据接收
      alert('receive' + JSON.parse(e.data))
      // console.log(e);
      // console.log('数据接收' + e.data)
      let newMsg = JSON.parse(e.data);
      alert(newMsg)
      console.log(newMsg);
      let decrypted;
      console.log("prepare to decrypt")
      // this.store.dispatch('check-info')
      this.store.dispatch('decrypt-message', newMsg)
          .then(res => {
            console.log(res);
            decrypted = res;
            this.msg = res;
          });
      // console.log(decrypted);
      // alert('你有新消息'+ decrypted);
    },
    websocketSend(Data) { // 数据发送
      this.websock.send(Data)
      alert('send' + Data)

    },
    websocketClose(e) {  // 关闭
      console.log('已关闭连接', e)
    },
    onStore() {
      this.store.dispatch('store-info')
          .then(resCode => {
            console.log(resCode);
          });
    },
    onCheck() {
      this.store.dispatch('check-info', this.message.destinationUserId)
          .then(resObj => {
            console.log(resObj);
          });
    },
    onDelete() {
      this.store.dispatch('delete-info', this.message.destinationUserId)
          .then(res => {
            console.log(res);
          })
    }
  },
}
</script>

<style scoped>

</style>