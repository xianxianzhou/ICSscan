<template>
  <v-app>
    <v-main class="main">
      <Home/>
    </v-main>
  </v-app>
</template>

<script>
import Home from './views/home/index';

export default {
  name: 'App',

  components: {
    Home,
  },

  data: () => ({
    //
  }),
};
</script>

<style lang="scss" scoped>
.main{
  background-image: url(./assets/image/image_bg.jpg);
  background-size: 100% 100%;
}
 
</style>
