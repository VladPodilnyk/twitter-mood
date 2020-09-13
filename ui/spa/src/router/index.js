import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/Status'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Twitter-mood',
      component: HelloWorld
    }
  ],
  mode: 'history'
})
