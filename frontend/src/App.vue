<script setup lang="ts">
import {RouterView } from 'vue-router'
import request from './utils/request';

request({
  method: 'GET',
  url: 'user/info'
}).then(res => {
  console.log(res);
})
</script>

<template>
  <RouterView />
</template>

<style scoped>

</style>
