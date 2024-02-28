<template>
<div id="pickingMobile">
  <el-carousel id="el-carousel" :interval="4000" type="card" height="250px">
    <el-carousel-item v-for="item in 3" :key="item">
      <h3 class="medium">{{ item }}</h3>
    </el-carousel-item>
  </el-carousel>
</div>
</template>

<script>
export default {
  name: "pickingMobile"
}
</script>

<style scoped>

#el-carousel{
  background-image: url("../assets/R-C.jpg");
  background-repeat: no-repeat;
  background-size: 100%;
}
.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-image: url("../assets/mobile.jpg");
  background-repeat: no-repeat;
}

.el-carousel__item:nth-child(2n+1) {
  /*background-color: #d3dce6;*/
}
</style>