<template>
  <div id="app">
    <picking-header @loadUser="makeUser"></picking-header>
    <div style="display: flex;text-align: center">
      <div style="width: 20%;margin-left: 5%;margin-right: 5%">
        <h3 style="color: firebrick">申请PK列表</h3>
        <el-table :data="opponentArray" height="500" border style="width: 100%;text-align: center;">
          <el-table-column label="昵称" prop="user" width="200" style="height: fit-content"></el-table-column>
          <el-table-column label="操作" width="105">
            <template slot-scope="scope">
              <el-button @click="handleClick(scope.row)" type="text" size="small">同意</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div style="width: 30%;min-width: fit-content">
        <h2 class="h2Title">五子棋</h2>
        <h3>{{victory}}</h3>
        <div class="Fbuttons" style="display: flex;">
          <input type="button" value="重新开始" class="restart" @click="restartGame"/>
                <input type="button" value="结束对局" class="regret" @click="endGame" />
          <!--      <input type="button" value="撤销悔棋" class="undo" @click="undo()" />-->
          <!--      <input type="button" :value="toggle?'切换dom':'切换canvas'" class="toggle" @click="toggleF()" />-->
          <el-input placeholder="请输入对手名称" v-model="searchOpponent" clearable style="margin-left: 20px"></el-input>
          <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
        </div>
        <div v-if="isAgree" style="display: flex;justify-content: center;align-items: center">
          <template v-if="!isConfirm">
            <h3>你的对手{{opponent}}已同意PK，请确认</h3>
            <el-button type="primary" round size="mini" style="width: min-content;height: min-content;margin-left: 20px;" @click="startGame">确认</el-button>
          </template>
          <template v-if="isConfirm">
            <h3>当前交战对手：{{opponent}}</h3>
          </template>
        </div>
        <div v-if="isSearch&&!isAgree" style="display: flex;justify-content: center;align-items: center;position: relative;left: 30%">
          <template v-if="isHaveOpponent">
            <h3 style="color: deepskyblue">昵称：{{searchOpponent}}</h3>
            <el-button type="primary" round @click="askPK" style="width: min-content;height: min-content;margin-left: 20px">请求PK</el-button>
          </template>
          <template v-if="!isHaveOpponent">
            <h3 style="color: brown">没有找到对手，请重新搜索！</h3>
          </template>
        </div>
        <div class="main">
          <canvas v-show="toggle" id="myCanvas" ref="canvas" width="480" height="480">当前浏览器不支持Canvas</canvas>
          <div v-show="!toggle" id="chess" ref="chessBox">
            <!-- <div id="box01"></div>
            <div id="box02"></div> -->
          </div>
        </div>
    </div>
      <div style="width: 30%">
        <div>
          <h3>游戏规则</h3>
          <h5>由请求方执黑子，先下</h5>
        </div>
        <div style="align-content: center">
          <h3>请求列表</h3>
          <div v-if="requestContext.length!==0" style=" box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);padding: 10px;border-radius: 4px;
            margin-left: 20px">
            <h4>你的对手{{opponent}}请求<span style="color: brown">{{requestContext}}</span></h4>
            <el-button type="primary" round size="mini" @click="doChoose(true)">同意</el-button>
            <el-button type="warning" round size="mini" @click="doChoose(false)">拒绝</el-button>
          </div>
          <div v-if="isRestart||isEnd" style=" box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);padding: 10px;border-radius: 4px;
          margin-left: 20px">
            <h4>你请求<span style="color: deepskyblue">{{request}}</span></h4>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>


import request from "@/utils/request";
import PickingHeader from "@/components/pickingHeader";

export default {
  name: 'App',
  data() {
    return{
      pieceMapArr: [], //记录棋盘落子情况
      color:'black', //棋子颜色
      step: 0, //记录当前步数
      checkMode: [ //输赢检查方向模式
        [1, 0], //水平
        [0, 1], //竖直
        [1, 1], //左斜线
        [1, -1], //右斜线
      ],
      flag: false,
      victory: '',
      history: [], //历史记录位置
      historyVal: [], //历史记录不被删除数组
      stepHistory: 0,
      domPiece:[], //
      toggle: true, //true为canvas,false为dom
      start:true,
      isWhite:false,
      opponent:'',
      searchOpponent:'',
      user:'',
      isHaveOpponent:false,
      isSearch:false,
      isBack:false,
      opponentArray:[],
      isFind:false,
      isAgree:false,
      isPut:false,
      isConfirm:false,
      isStart:false,
      requestContext:'',
      isRestart:false,
      isEnd:false,
      request:"",
    }

  },
  mounted(){
    const myCanvas = document.getElementById("myCanvas");
    if (!myCanvas.getContext) {
      alert("当前浏览器不支持Canvas.");
      this.toggle = false;
      this.drawpieceBoardDom();
    } else {
      console.log("当前浏览器支持Canvas", this.toggle)
      this.drawpieceBoard();
      const canvas = this.$refs.canvas;
      // 添加点击监听事件

        canvas.addEventListener("click", e => {
          if (this.isConfirm){
            if (this.flag) {
              alert("游戏结束,请重新开始~");
              return;
            }
            //判断点击范围是否越出棋盘
            if (e.offsetX < 25 || e.offsetX > 450 || e.offsetY < 25 || e.offsetY > 450) {
              return;
            }
            if (this.start){
              this.isPut=true;
              let dx = Math.floor((e.offsetX + 15) / 30) * 30;
              let dy = Math.floor((e.offsetY + 15) / 30) * 30;
              console.log('this.pieceMapArr 数组', this.pieceMapArr)
              if (this.pieceMapArr[dx / 30 - 1][dy / 30 - 1] === 0) {
                // console.log('落下棋子', dx, dy, this.pieceColor[this.step % 2])

                //历史记录位置

                this.history.length++;
                this.drawPiece(dx, dy, this.color); //落下棋子
                this.pieceMapArr[dx / 30 - 1][dy / 30 - 1] = this.color;
                this.history.push({
                  dx,
                  dy,
                  color: this.color
                });
                this.historyVal.push({
                  dx,
                  dy,
                  color: this.color
                });
                this.start=false;

                request.post("/api/game/wuziqi",{user:this.user,x:dx,y:dy,color:this.color,opponent:this.opponent});
                this.stepHistory++
                //检查当前玩家是否赢了游戏
                for (var i = 0; i < 4; i++) {
                  this.checkWin(dx / 30 - 1, dy / 30 - 1, this.color, this.checkMode[i]);
                }
                this.step++;
              } else {
                alert("不能落在有棋子的地方！");
              }
            }
          }
        });

      this.changeAction();

    }
  },
  components: {
    PickingHeader,
  },
  methods: {
    makeUser(name){
      this.user=name;
    },
    askPK(){
      if (this.user.length===0){
        alert("请先登录！")
      }else{
        this.color="black";
        // request.post("/api/game/clearQZ",{user:this.user,opponent:this.searchOpponent})
        this.restartInit();
        request.post("/api/game/askPK",{user:this.user,opponent:this.searchOpponent})
      }
    },
    startGame(){
      this.isConfirm=true;
      this.isBack=true;
      this.isAgree=true;
      this.isStart=true;
      this.start=true;
      this.restartInit();
      // request.post("/api/game/clearQZ",{user:this.user,opponent:this.opponent})
    },
    restartGame(){
      if (this.isStart){
        this.isRestart=true;
        this.isEnd=false;
        this.request="重新开始";
        request.post("/api/game/storeRequest",{user:this.user,opponent:this.opponent,flag:2})
      }
    },
    endGame(){
      // this.isConfirm=false;
      // this.isAgree=false;
      // this.isBack=false;
      if (this.isStart){
        this.isEnd=true;
        this.isRestart=false;
        this.request="结束对局";
        request.post("/api/game/storeRequest",{user:this.user,opponent:this.opponent,flag:1})
      }

    },
    doChoose(e){//对请求进行处理，true为同意，false为拒绝
      if (e){
        request.post("/api/game/storeRequest",{user:this.user,opponent:this.opponent,flag:3})
        this.restartInit();
        if (this.requestContext==="结束对局"){
          this.isConfirm=false;
          this.isAgree=false;
          request.post("/api/game/clearRequestPK",{user:this.user,opponent:this.opponent})
          request.post("/api/game/clearPKAgree",{user:this.user,opponent:this.opponent})
          this.isBack=false;
          this.opponentArray=[];
        }else {
          this.isAgree=true;
          this.isConfirm=true;
        }
      }
      else {
        request.post("/api/game/storeRequest",{user:this.user,opponent:this.opponent,flag:4})
      }
      this.requestContext="";
    },
    handleClick(e){
      this.opponent=e.user;
      this.isFind=true;
      this.color="white";
      this.isAgree=true;
      request.post("/api/game/isSure",{user:this.user,opponent:this.opponent})
      this.restartInit();
    },
    search(){
      request.post("/api/game/search",{user:this.searchOpponent}).then(res=>{
        if (res.code===200){
          this.isSearch=true;
          this.isHaveOpponent=true;
        }else if (res.code===500){
          this.isSearch=true;
          this.isHaveOpponent=false;
        }
      })
    },
    changeAction(){
        setInterval(()=>{
          //找到想要PK的对手列表
          if (!this.isBack && this.user.length!==0){
            request.post("/api/game/askPKUser",{user:this.user}).then(res=>{
              if (res.code===200){
                this.isBack=true;
                this.opponentArray=res.object;
              }
            })
          }

          //搜寻对手发来的请求
          if (this.isStart&&this.user.length!==0&&this.opponent.length!==0){
            request.post("/api/game/findRequest",{user:this.user,opponent:this.opponent}).then(res=>{
              if (res.code===200){
                if (res.object===1){
                  this.requestContext="结束对局";
                  this.isStart=false;
                }else if (res.object===2){
                  this.requestContext="重新开始";
                }else if (res.object===3){
                  if (this.isEnd){
                    this.isConfirm=false;
                    this.isAgree=false;
                    this.isBack=false;
                    this.isSearch=false;
                    this.isStart=false;
                    this.isEnd=false;
                  }else if (this.isRestart){
                    this.isAgree=true;
                    this.isConfirm=true;
                    this.isRestart=false;
                  }
                  alert("对手同意了你的请求")
                  this.restartInit();
                }else if (res.object===4){
                  alert("对手拒绝了你的请求")
                }
              }
            })
          }
          //找到对手
          if (!this.isAgree&&this.searchOpponent.length!==0){
            request.post("/api/game/isFind",{user:this.user,opponent:this.searchOpponent}).then(res=>{
              if (res.code===200){
                this.isAgree=true;
                this.opponent=res.object;
              }
            })
          }

          //加载对手棋子的位置，并提醒该自己执子了
          if ((!this.isPut||!this.start)&&this.opponent.length!==0){
          request.post("/api/game/backData",{user:this.opponent}).then(res=>{
            if (res.code===200) {
              this.start = true;
              this.history.length++;
              let dx = res.object.x;
              let dy = res.object.y;
              let color = res.object.color;
              console.log(dx,dy,color)
              this.drawPiece(dx, dy, color); //落下棋子
              this.pieceMapArr[dx / 30 - 1][dy / 30 - 1] = color;
              this.history.push({
                dx,
                dy,
                color: this.color
              });
              this.historyVal.push({
                dx,
                dy,
                color: color
              });
              this.stepHistory++;
              for (let i = 0; i < 4; i++) {
                this.checkWin(dx / 30 - 1, dy / 30 - 1, color, this.checkMode[i]);
              }
              this.step++;
              alert("该你啦！")
            }
          })
          }
          },1000);

    },
    toggleF() {
      this.toggle = !this.toggle;
      if (!this.toggle) {
        // console.log("当前---------------1")
        // let elem = document.getElementById('box01');
        // if (elem !== null) {
        // 	elem.parentNode.removeChild(elem);
        // 	let elem02 = document.getElementById('box02');
        // 	elem02.parentNode.removeChild(elem02);
        // }
        // this.drawpieceBoardDom();
        this.restartInit()
      } else {
        this.restartInit()
        // this.drawpieceBoard();
      }
    },
    //初始化棋盘数组
    pieceArr() {
      for (let i = 0; i < 15; i++) {
        this.pieceMapArr[i] = [];
        for (let j = 0; j < 15; j++) {
          this.pieceMapArr[i][j] = 0;
        }
      }
    },
    //重新开始
    restartInit() {
      this.victory='';
      if (!this.toggle) {
        // console.log("-----dom-------")
        var elem = document.querySelector('#box01');
        // console.log("elem",elem)
        if (elem != null ) {
          elem.parentNode.removeChild(elem);
          let elem02 = document.querySelector('#box02');
          elem02.parentNode.removeChild(elem02);
        }
        this.drawpieceBoardDom();
        this.flag = false;
        this.step = 0;
        this.stepHistory = 0;
        this.historyVal = [];
        this.history = [];
      } else {
        //重画
        this.repaint();
        // 绘制棋盘
        this.drawpieceBoard();
        this.flag = false;
        this.step = 0;
        this.stepHistory = 0;
        this.historyVal = [];
        this.history = [];
      }
    },
    //---------canvas----------
    // 绘制棋盘
    drawpieceBoard() {
      //初始化棋盘数组
      this.pieceArr();
      //canvas 绘制
      let canvas = this.$refs.canvas
      // 调用canvas元素的getContext 方法访问获取2d渲染的上下文
      let context = canvas.getContext("2d");
      context.strokeStyle = '#666'
      for (let i = 0; i < 15; i++) {
        //落在方格(canvas 的宽高是450)
        // context.moveTo(15 + i * 30, 15)
        // context.lineTo(15 + i * 30, 435)
        // context.stroke()
        // context.moveTo(15, 15 + i * 30)
        // context.lineTo(435, 15 + i * 30)
        // context.stroke()
        //落在交叉点(480)
        context.beginPath();
        context.moveTo((i + 1) * 30, 30);
        context.lineTo((i + 1) * 30, canvas.height - 30);
        context.closePath();
        context.stroke();
        context.beginPath();
        context.moveTo(30, (i + 1) * 30);
        context.lineTo(canvas.width - 30, (i + 1) * 30);
        context.closePath();
        context.stroke();
      }
    },
    //绘制棋子
    drawPiece(x, y, color) {
      let canvas = this.$refs.canvas
      let context = canvas.getContext("2d");
      context.beginPath(); //开始一条路径或重置当前的路径
      context.arc(x, y, 15, 0, Math.PI * 2, false);
      context.closePath();
      context.fillStyle = color;
      context.fill();
    },
    //胜负判断函数
    checkWin(x, y, color, mode) {
      let count = 1; //记录
      for (let i = 1; i < 5; i++) {
        if (this.pieceMapArr[x + i * mode[0]]) {
          if (this.pieceMapArr[x + i * mode[0]][y + i * mode[1]] === color) {
            count++;
          } else {
            break;
          }
        }
      }
      for (let j = 1; j < 5; j++) {
        if (this.pieceMapArr[x - j * mode[0]]) {
          if (this.pieceMapArr[x - j * mode[0]][y - j * mode[1]] === color) {
            count++;
          } else {
            break;
          }
        }
      }
      // console.log('胜负判断函数', count)
      // console.log('color', color)
      if (count >= 5) {
        if (color === 'black') {
          this.victory = "黑子棋方胜利！";
        } else {
          this.victory = "白子棋方胜利！";
        }
        // 游戏结束
        // console.log('游戏结束')
        this.flag = true;
      }
    },
    //重画函数
    repaint() {
      //重画
      let canvas = this.$refs.canvas;
      let context = canvas.getContext("2d");
      context.fillStyle = "bisque";
      context.fillRect(0, 0, canvas.width, canvas.height);
      context.beginPath();
      context.closePath();
    },

    //悔棋:
    // canvas 创建一个二维数组，下棋或者悔棋都操作这个数组。操作完数据，把画布全清，重新用数据画一个棋盘。
    // dom   二维数组删除数组最后一项, 先清空棋子的填充颜色,在渲染上颜色
    regret() {
      if (!this.toggle) {
        // console.log("-----dom------this.domPiece",this.domPiece)
        if (this.history.length && !this.flag) {
          this.history.pop(); //删除数组最后一项
          console.log("-----dom------this.history", this.history)
          //重画
          this.pieceArr();
          // let elem = document.getElementById('box01');
          // if (elem !== null) {
          // 	elem.parentNode.removeChild(elem);
          // 	let elem02 = document.getElementById('box02');
          // 	elem02.parentNode.removeChild(elem02);
          // } //这个太耗性能了
          // this.drawpieceBoardDom();
          // 清空棋子的填充颜色
          this.domPiece.forEach(e => {
            e.forEach(qz => {
              qz.style.backgroundColor = '';
            })
          });
          // 渲染棋子颜色
          this.history.forEach(e => {
            this.domPiece[e.m][e.n].style.backgroundColor = e.color
            this.pieceMapArr[e.m][e.n] = e.color;
          });
          this.step--
        } else {
          alert("已经不能悔棋了~")
        }

      } else {
        if (this.history.length && !this.flag) {
          this.history.pop(); //删除数组最后一项
          //重画
          this.repaint();
          // 绘制棋盘
          this.drawpieceBoard();
          //绘制棋子
          this.history.forEach(e => {
            this.drawPiece(e.dx, e.dy, e.color)
            this.pieceMapArr[e.dx / 30 - 1][e.dy / 30 - 1] = e.color;
          });
          this.step--
        } else {
          alert("已经不能悔棋了~")
        }
      }
    },
    //撤销悔棋
    undo() {
      if (!this.toggle) {
        // console.log("-----dom------this.domPiece",this.domPiece)
        if ((this.historyVal.length > this.history.length) && !this.flag) {
          this.history.push(this.historyVal[this.step])
          console.log("-----dom------this.history", this.history)
          // 清空棋子的填充颜色
          this.domPiece.forEach(e => {
            e.forEach(qz => {
              qz.style.backgroundColor = '';
            })
          });
          // 渲染棋子颜色
          this.history.forEach(e => {
            this.domPiece[e.m][e.n].style.backgroundColor = e.color
            this.pieceMapArr[e.m][e.n] = e.color;
          });
          this.step++
        } else {
          alert("不能撤销悔棋了~")
        }

      } else {
        if ((this.historyVal.length > this.history.length) && !this.flag) {
          this.history.push(this.historyVal[this.step])
          //重画
          this.repaint();
          // 绘制棋盘
          this.drawpieceBoard();
          this.history.forEach(e => {
            this.drawPiece(e.dx, e.dy, e.color)
            this.pieceMapArr[e.dx / 30 - 1][e.dy / 30 - 1] = e.color;
          });
          this.step++
        } else {
          alert("不能撤销悔棋了~")
        }
      }
    },

    // -----------dom-----------
    drawpieceBoardDom() {
      // console.log("this", this)
      let that = this;
      //调用初始化棋盘数组函数
      that.pieceArr();
      //创建一个容器
      const box = document.querySelector("#chess");
      const box01 = document.createElement("div");
      box01.setAttribute("id", "box01");
      box.appendChild(box01);
      //画棋盘
      const chess01 = document.querySelector("#box01");
      const box02 = document.createElement("div");
      box02.setAttribute("id", "box02");
      box.appendChild(box02);
      let arr = new Array();
      for (let i = 0; i < 14; i++) {
        arr[i] = new Array();
        for (let j = 0; j < 14; j++) {
          arr[i][j] = document.createElement("div");
          arr[i][j].setAttribute("class", "squre");
          box02.appendChild(arr[i][j]);
        }
      }
      //画棋子
      let arr01 = this.domPiece;
      for (let i = 0; i < 15; i++) {
        arr01[i] = new Array();
        for (let j = 0; j < 15; j++) {
          arr01[i][j] = document.createElement("div");
          arr01[i][j].setAttribute("class", "qz");
          chess01.appendChild(arr01[i][j]);
        }
      }
      // console.log("this.domPiece",this.domPiece)
      // 填充颜色和判断
      for (let m = 0; m < 15; m++) {
        for (let n = 0; n < 15; n++) {
          arr01[m][n].onclick = function() {
            //判断游戏是否结束
            if (!that.flag) {
              if (that.pieceMapArr[m][n] === 0) {
                //黑白交换下棋
                // console.log(this);
                // console.log('落下棋子', that.pieceColor[that.step % 2])
                //确保填充颜色正确进行了判断
                if (this.className === "qz" && that.step % 2 === 0 && this.style.backgroundColor === "") {
                  //下棋填充黑颜色
                  this.style.backgroundColor = that.pieceColor[that.step % 2];
                  //写入棋盘数组
                  that.pieceMapArr[m][n] = that.pieceColor[that.step % 2];
                  //历史记录位置
                  that.history.length = that.step;
                  that.history.push({
                    m,
                    n,
                    color: that.pieceColor[that.step % 2]
                  });
                  that.historyVal.push({
                    m,
                    n,
                    color: that.pieceColor[that.step % 2]
                  });
                  that.stepHistory++
                  console.log('this.history', that.history);
                } else if (this.className == "qz" && that.step % 2 != 0 && this.style.backgroundColor == "") {
                  //下棋填充白颜色
                  this.style.backgroundColor = that.pieceColor[that.step % 2];
                  //写入棋盘数组
                  that.pieceMapArr[m][n] = that.pieceColor[that.step % 2];
                  //历史记录位置
                  that.history.length = that.step;
                  that.history.push({
                    m,
                    n,
                    color: that.pieceColor[that.step % 2]
                  });
                  that.historyVal.push({
                    m,
                    n,
                    color: that.pieceColor[that.step % 2]
                  });
                  that.stepHistory++
                  console.log('this.history', that.history);
                }
                //检查当前是否赢了
                for (var i = 0; i < 4; i++) {
                  that.checkWin(m, n, that.pieceColor[that.step % 2], that.checkMode[i]);
                }
                that.step++;
                // console.log('that.step', that.step);
              } else {
                alert("不能落在有棋子的地方！");
                return;
              }
            } else {
              // that.flag = true;
              alert("游戏结束,请重新开始~");
              return;
            }
          }
        }
      }

    }
  }
}
</script>

<style>
body {
  margin: 0;
  padding: 0;
}
#center{
  padding-left: 30%;
  width: 500px;
}
.h2Title{
  text-align: center;
}
#app h3{
  color: red;
}
.Fbuttons{
  margin-bottom: 1rem;
}
.main{
  background-color: bisque;
  width: 30rem;
}
.restart,.regret,.undo{
  background: bisque;
  padding: 6px 10px;
  border-radius: 6px;
  font-size: 12px;
  cursor: pointer;
}
#chess {
  position: relative;
  width: 440px;
  height: 450px;
  padding-left: 30px;
  padding-top: 30px;
  background-color: bisque;
}

#chess .squre {
  width: 28px;
  height: 28px;
  border: 1px solid #666;
  float: left;
}

#box01 .squre:hover {
  background-color: pink;
}

#box01 {
  position: absolute;
  margin: 0 auto;
  width: 450px;
  height: 450px;
  top: 15px;
  left: 15px;
}

#box01 .qz {
  /* width: 28px;
  height: 28px; */
  width: 30px;
  height: 30px;
  border: 0px solid #C7C7C7;
  float: left;
  border-radius: 50%;
  /* margin: 1px; */
}

#box01 .qz:hover {
  background-color: pink;
}
.toggle{
  float: right;
}
</style>
