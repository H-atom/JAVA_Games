<template>
  <div style=" background-color:#545c64" id="pickingHeader" ref="pickingHeader">
    <el-dialog title="用户登录" :visible.sync="dialogFormVisible" style="width: 50%;margin: auto;min-width: 800px" @close="reset">
      <enter-message-box ref="resetForm" @enterSuccess="changeVisible" ></enter-message-box>
    </el-dialog>
      <el-menu
          :default-active="activeIndex2"
          class="el-menu-demo"
          mode="horizontal"
          text-color="#fff"
          background-color="#545c64"
          active-text-color="#ffd04b"
          style="margin-left: 35%"
      >
<!--        <el-menu-item index="1" class="el-menu-distance">首页</el-menu-item>-->
<!--        <el-submenu index="2" class="el-menu-distance">-->
<!--          <template slot="title">业务类型</template>-->
<!--          <el-menu-item index="2-1">二手物品</el-menu-item>-->
<!--          <el-menu-item index="2-2">租借物品</el-menu-item>-->
<!--          <el-menu-item index="2-3">捎带物品</el-menu-item>-->
<!--          <el-submenu index="2-4">-->
<!--            <template slot="title">选项4</template>-->
<!--            <el-menu-item index="2-4-1">选项1</el-menu-item>-->
<!--            <el-menu-item index="2-4-2">选项2</el-menu-item>-->
<!--            <el-menu-item index="2-4-3">选项3</el-menu-item>-->
<!--          </el-submenu>-->
<!--        </el-submenu>-->
<!--        <el-menu-item index="3"  class="el-menu-distance">社区</el-menu-item>-->
<!--        <el-menu-item index="4" class="el-menu-distance">帮助中心</el-menu-item>-->
<!--        <el-menu-item>-->
<!--          <el-input-->
<!--              prefix-icon="el-icon-search"-->
<!--              placeholder="搜索"-->
<!--              v-model="input"-->
<!--              clearable>-->
<!--          </el-input>-->
<!--        </el-menu-item>-->
        <el-button type="text" @click="enter" class="el-button" v-if="!visibleEnter">登录</el-button>
          <el-dropdown @command="handleCommand">
            <span><el-button :icon="icon" circle size="mini" style="margin-top: 16px" v-if="visibleEnter"> </el-button></span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="个人资料">个人资料</el-dropdown-item>
<!--              <el-dropdown-item command="最短时间排行榜">最短时间排行榜</el-dropdown-item>-->
<!--              <el-dropdown-item command="数量排行榜">数量排行榜</el-dropdown-item>-->
<!--              <el-dropdown-item command="综合成绩排行榜">综合成绩排行榜</el-dropdown-item>-->
              <el-dropdown-item command="e" divided>退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        <el-drawer
            :title="title"
            :visible.sync="drawer"
            :direction="direction"
            :before-close="handleClose"
            v-if="visibleEnter">
<!--          个人资料-->
          <el-form label-position="right" label-width="80px" v-if="userMessage" style="margin: 5px" :rules="rules" ref="changeMessForm" :model="form">
            <el-button :icon="icon" circle size="large" style="margin-left: 40%"></el-button>
            <el-form-item label="昵称: " label-width="80px" style="align-items: center" prop="newName">
              <template v-if="!changeName">
                <span class="span">{{username}}</span>
                <i class="el-icon-edit" style="margin-left: 5px" @click="changeMess('changeName')"></i>
              </template>
              <template v-if="changeName">
                <el-input placeholder="请输入内容" v-model="form.newName" clearable size="medium" style="width: max-content"></el-input>
                <el-button type="primary" round size="mini" @click="changeUsername">确定</el-button>
              </template>
            </el-form-item>
            <el-form-item label="密码: " label-width="80px" style="align-items: center;">
              <template v-if="!changePassword">
                <span class="span">**********</span>
                <i class="el-icon-edit" style="margin-left: 5px" @click="changeMess('changePassword')"></i>
              </template>
              <template v-if="changePassword">
                <el-form-item prop="password" class="el-form-item">
                  <el-input placeholder="请输入旧密码" v-model="form.password" show-password style="width: max-content"></el-input>
                </el-form-item>
                <el-form-item prop="newPassword" class="el-form-item">
                  <el-input placeholder="请输入密码" v-model="form.newPassword" show-password style="width: max-content"></el-input>
                </el-form-item>
                <el-form-item prop="againPassword" class="el-form-item">
                  <el-input placeholder="请输入密码" v-model="form.againPassword" show-password style="width: max-content"></el-input>
                </el-form-item>
                <el-form-item class="el-form-item">
                  <el-button type="success" round size="mini" @click="changeUserPassword">确定</el-button>
                </el-form-item>
              </template>
            </el-form-item>
<!--            <el-form-item label="最短时间: " label-width="80px" style="align-items: center;">-->
<!--              <span>{{min_time}}</span>-->
<!--            </el-form-item>-->
<!--            <el-form-item label="数量: " label-width="80px" style="align-items: center;">-->
<!--              <span>{{user_number}}</span>-->
<!--            </el-form-item>-->
<!--            <el-form-item label="综合成绩: " label-width="80px" style="align-items: center;">-->
<!--              <span>{{sum_score}}</span>-->
<!--            </el-form-item>-->
          </el-form>
<!--          排行榜-->
<!--          <el-table-->
<!--              :data="tableData"-->
<!--              height="250"-->
<!--              v-if="userRanking"-->
<!--              border-->
<!--              style="width: 100%">-->
<!--            <el-table-column-->
<!--                prop="username"-->
<!--                label="昵称"-->
<!--                width="180">-->
<!--            </el-table-column>-->
<!--            <el-table-column-->
<!--                v-if="minTime"-->
<!--                prop="minTime"-->
<!--                label="最短时间"-->
<!--                width="180">-->
<!--            </el-table-column>-->
<!--            <el-table-column-->
<!--                v-if="number"-->
<!--                prop="number"-->
<!--                label="数量"-->
<!--                width="180">-->
<!--            </el-table-column>-->
<!--            <el-table-column-->
<!--                v-if="sumScore"-->
<!--                prop="sumScore"-->
<!--                label="综合成绩"-->
<!--                width="180">-->
<!--            </el-table-column>-->
<!--            <el-table-column-->
<!--                prop="ranking"-->
<!--                label="排名">-->
<!--            </el-table-column>-->
<!--          </el-table>-->
        </el-drawer>
      </el-menu>
  </div>
</template>

<script>
import EnterMessageBox from "@/components/enterMessageBox";
import request from "@/utils/request";
export default {
  name: "pickingHeader",
  components: { EnterMessageBox},
  data(){
    var checkAgainPassword=(rule,value,callback)=>{
      if (!value){
        return callback(new Error("密码输入为空！"))
      }else if (value!==this.newPassword){
        return callback(new Error("密码输入不一致！"))
      }
      callback();
    }

    var checkPassword=(rule,value,callback)=>{
      if (!value){
        return callback(new Error("密码输入为空！"))
      }else if (value!==this.userPassword){
        return callback(new Error("密码输入错误！"))
      }
      callback();
    }
    var checkNull=(rule,value,callback)=>{
      if (!value){
        return callback(new Error("内容输入为空！"))
      }
      callback();
    }
    return{
      activeIndex2: '1',
      input:'',
      dialogFormVisible:false,
      visibleEnter:false,
      icon:'el-icon-user-solid',
      drawer: false,
      direction: 'rtl',
      title:'',
      username:'',
      tableData: [],
      minTime:false,
      number:false,
      sumScore:false,
      userMessage:false,
      userRanking:false,
      min_time:"",
      user_number:"",
      sum_score:"",
      changeName:false,
      changePassword:false,
      userPassword:"",
      form:{
        newName:"",
        password:"",
        newPassword:"",
        againPassword:"",
      },
      rules: {
        newName:[{validator:checkNull, trigger: 'blur' }],
        password:[{validator:checkPassword, trigger: 'blur' }],
        newPassword:[{validator:checkNull, trigger: 'blur' }],
        againPassword:[{validator:checkAgainPassword, trigger: 'blur' }],
      }
    }
  },
  methods: {
    // handleSelect(key, keyPath) {
      // console.log(key, keyPath);
    // },
    enter(){
      this.dialogFormVisible=true;
    },
    reset(){
      this.$refs.resetForm.resetForm();
    },
    changeVisible(username,visible){
      this.visibleEnter=visible;
      this.dialogFormVisible=false;
      this.username=username;
      this.$emit("loadUser",username);
    },
   handleCommand(command) {
     // this.$message('click on item ' + command);
     this.title=command;
     this.drawer=true;
     this.changePassword=false;
     this.changeName=false;
     this.form.password="";
     this.form.againPassword="";
     this.form.newPassword="";
     if (command==="最短时间排行榜"){
       this.userMessage=false;
       this.userRanking=true;
       request.get("/api/game/sortByMinTime").then(res=>{
       this.tableData=res;
     })
       this.minTime=true;
       this.number=false;
       this.sumScore=false;
     }else if (command==="数量排行榜"){
       this.userMessage=false;
       this.userRanking=true;
       request.get("/api/game/sortByNumber").then(res=>{
         this.tableData=res;
       })
       this.number=true;
       this.sumScore=false;
       this.minTime=false;
     }else if (command==="综合成绩排行榜"){
       this.userMessage=false;
       this.userRanking=true;
       request.get("/api/game/sortBySumScore").then(res=>{
         this.tableData=res;
       })
       this.sumScore=true;
       this.number=false;
       this.minTime=false;
     }else if (command==="个人资料"){
       this.userRanking=false;
       this.userMessage=true;
       this.form.newName=this.username;
       request.post("/api/game/userMessage",{username:this.username}).then(res=>{
         this.userPassword=res.password;
         this.username=res.username;
         this.min_time=res.minTime;
         this.user_number=res.number;
         this.sum_score=res.sumScore;
       })
     }else if (command==="e"){
       this.drawer=false;
       this.changeVisible("",false)
     }
   },
    handleClose(done) {
      this.$confirm('确认关闭？')
          .then(()=> {
            done();
          })
          .catch(()=> {});
    },
    changeMess(change){
      if (change==="changeName"){
        this.changeName=true;
      }else if (change==="changePassword"){
        this.changePassword=true;
      }
    },
    changeUsername(){
      request.post("/api/game/changeUsername",{username:this.username,newName:this.newName}).then(res=>{
        if (res.code===202){
          this.username=res.object;
        }
      });
    },
    changeUserPassword(){
      this.$refs.changeMessForm.validate((valid)=>{
        if (valid){

          // request.post("/api/game/changePassword",{newPassword:this.form.againPassword,username:this.username}).then(res=>{
          //   if (res.code===200){
          //     this.userPassword=res.object;
          //   }
          // })
        }else {
          return false;
        }
      })
    }
 }
}
</script>

<style scoped>
#pickingHeader{
  width: 100%;
  padding: 0;
  margin: 0;
}
.el-menu-distance{
  margin-left: 50px;
}
/deep/.el-input__inner{
  height: 33px;
  line-height: 33px;
}
.el-button{
  font-size: medium;
  font-weight: bolder;
  margin-top: 10px;
  margin-left: 20px;
}
.span{
  color: deepskyblue;
}
.el-form-item{
  margin-top: 12px;
}
</style>