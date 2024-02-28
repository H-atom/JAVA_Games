<template>
<div style="text-align: center;margin: 10px auto">
  <el-button type="primary" plain @click="start" v-if="!isStart">开始游戏</el-button>
  <h1 v-if="isStart">{{hour}}:{{minute}}:{{second}}</h1>
  <h1 style="color: deepskyblue">{{message}}</h1>
  <el-form :rules="rules" ref="form" :model="form" @keyup.enter.native="sure">
    <el-form-item prop="number">
      <el-input type="text" placeholder="请输入你所猜想的数字" v-model="form.number" maxlength="4" show-word-limit clearable
                style="width: auto;"/>
    </el-form-item>
  </el-form>
  <el-row style="margin:10px auto ">
    <el-button type="primary" round @click="gameOver">结束</el-button>
    <el-button type="success" round @click="sure">确定</el-button>
    <el-button type="warning" round @click="restart">继续</el-button>
    <el-button type="danger" round @click="skip">跳过</el-button>
  </el-row>
</div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "timeCount",
  data(){
    var checkNumber=(rule,value,callback) =>{
      if (value.length<4){
        return callback(new Error("数字不足4位！"))
      }
      for (let i = 0; i < 4; i++) {
        for (let j = i+1; j < 4; j++) {
          if (value[i]===value[j]){
            return callback(new Error("输入数字有重复！"))
          }
        }
      }
      callback();
    }

    return{
      hour:0,
      minute:0,
      second:0,
      isStart:false,
      isPause:false,
      isEnd:false,
      form:{
        number:'',
      },
      trueNumber:'',
      message:"",
      rules:{
        number :[{validator: checkNumber, trigger: 'blur' }],
      }
    }
  },
  props:["user"],
  methods:{
    start(){
      this.isStart=true;
      if (!this.isEnd){
        this.timeCount();
        request.post("/api/game/getNumber").then(res=>{
          if (res.code===200){
            this.trueNumber=res.object;
          }
        })
      }else {
        this.restart();
      }
    },
    timeCount(){
      setInterval(()=>{
        if (!this.isPause){
          this.second++;
          if (this.second===60){
            this.minute++;
            this.second=0;
          }
          if (this.minute===60){
            this.hour++;
            this.minute=0;
          }
        }
      },1000)
    },
    restart(){
        this.second=0;
        this.minute=0;
        this.hour=0;
        this.isPause=false;
      request.post("/api/game/getNumber").then(res=>{
        if (res.code===200){
          this.trueNumber=res.object;
        }
      })
    },
    skip(){
      this.restart();
      let x=(this.hour*60+this.minute)*60+this.second;
      request.post("/api/game/skipScore",{username:this.user,minTime:x})
    },
    sure(){
      this.$refs.form.validate((valid)=>{
        if (valid){
          if (this.trueNumber===this.form.number){
            this.isPause=true;
            this.message="恭喜你，猜对了";
            let x=(this.hour*60+this.minute)*60+this.second;
            request.post("/api/game/checkScore",{username:this.user,minTime:x})
          }else {
            request.post("/api/game/decideNumber",{number:this.form.number,trueNumber:this.trueNumber}).then(res=>{
              if (res.code===200){
                this.message=res.object;
              }
            })
          }
        }else {
          return false;
        }
      })

    },
    gameOver(){
      this.isPause=true;
      this.isStart=false;
      this.isEnd=true;
    }
  }
}
</script>

<style scoped>
/deep/.el-form-item__error{
  position: relative;
}
</style>