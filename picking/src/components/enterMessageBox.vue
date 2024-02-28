<template>
<div>
    <el-form :model="ruleForm"
             status-icon
             :rules="rules"
             ref="ruleForm"
             label-width="100px"
             class="demo-ruleForm"
             @keyup.enter.native="submitForm(ruleForm)">
      <el-form-item label="昵称" prop="username" class="el-form-item">
        <el-input type="text" v-model="ruleForm.username" autocomplete="off" clearable class="el-input"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password" class="el-form-item">
        <el-input type="password" v-model="ruleForm.password" autocomplete="off" clearable ></el-input>
      </el-form-item>
      <el-form-item class="el-form-item">
        <el-button type="text">忘记密码</el-button>
        <el-button type="text" style="margin-left: 60px">手机验证码登录</el-button>
      </el-form-item>
      <el-form-item class="el-form-item">
        <el-button type="primary" @click="submitForm(ruleForm)">确定</el-button>
        <el-button style="margin-left: 50px">注册</el-button>
      </el-form-item>
    </el-form>
</div>
</template>

<script>
import request from "@/utils/request";


export default {
  name: "enterMessageBox",

  data() {
    // let phone = /^1[2,3,4,5,7,8,9][0-9]{9}$/
    var checkNumber = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('昵称不能为空'));
      }
      callback();
      // setTimeout(() => {
      //   if (!Number.isInteger(value)) {
      //     callback(new Error('请输入数字值'));
      //   }else if (!phone.test(value)){
      //     callback(new Error("电话号码格式错误"))
      //   }else {
      //     callback();
      //   }
      // }, 1000);
    };
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else {
        if (this.ruleForm.password !== '') {
          this.$refs.ruleForm.validateField('checkPass');
        }
        callback();
      }
    };
    return {
      ruleForm: {
        username:'',
        password:'',
      },
      rules: {
        username: [
          { validator: checkNumber, trigger: 'blur' }
        ],
        password: [
          { validator: validatePass, trigger: 'blur' }
        ],

      }
    };
  },
//props:['vis','dia'],
  methods: {
    submitForm(formName) {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          request.post("/api/game/login", formName).then(res=>{
          if (res.code===500){
            alert("密码错误，请重新登录！")
          }else if (res.code===200){
            this.$emit("enterSuccess",res.object.username,true)//直接注册后登录
          }else if (res.code===201){
            this.$emit("enterSuccess",res.object.username,true)//已注册后登录
          }
        })
        } else {
          return false;
        }
      });
    },
    resetForm(){
      this.$refs.ruleForm.resetFields();
    }
  }
}
</script>

<style scoped>
.demo-ruleForm{
  width: 100%;
}
.el-input{
  width: 80%;
}
.el-form-item{
margin-top: -12px;
}

</style>