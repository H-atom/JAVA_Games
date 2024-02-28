<template>
  <div id="app">
    <picking-header @loadUser="makeUser"></picking-header>
    <timeCount :user="name"></timeCount>
<!--    <picking-mobile></picking-mobile>-->
  </div>
</template>

<script>

import PickingHeader from "@/components/pickingHeader";
import timeCount from "@/components/timeCount";
// import PickingMobile from "@/components/pickingMobile";
export default {
  name: 'App',
  components: {
    // PickingMobile,
    PickingHeader,
    timeCount
  },
  data(){
    return{
      name:"",
    }
  },
  methods:{
    makeUser(name){
      this.name=name;
    }
  }
}
</script>

<style>

</style>
