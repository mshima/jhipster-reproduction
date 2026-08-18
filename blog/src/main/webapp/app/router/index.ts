import { type RouteRecordRaw, createRouter as createVueRouter, createWebHistory } from 'vue-router';

import { loadRemote } from '@module-federation/runtime';

const Home = () => import('@/core/home/home.vue');
const Error = () => import('@/core/error/error.vue');
import admin from '@/router/admin';
import pages from '@/router/pages';

export const createRouter = () =>
  createVueRouter({
    history: createWebHistory(),
    routes: [
      {
        path: '/',
        name: 'Home',
        component: Home,
      },
      {
        path: '/forbidden',
        name: 'Forbidden',
        component: Error,
        meta: { error403: true },
      },
      {
        path: '/not-found',
        name: 'NotFound',
        component: Error,
        meta: { error404: true },
      },
      ...admin,
      // Load entities route exposed by microfrontend
      // entities,
      ...pages,
    ],
  });

const router = createRouter();

export const lazyRoutes = Promise.all([
  loadRemote<any>('blog/entities-router')
    .then(blogRouter => {
      router.addRoute(blogRouter.default as RouteRecordRaw);
      return blogRouter.default;
    })
    .catch(error => {
      console.log(`Error loading blog menus. Make sure it's up. ${error}`);
    }),
]);

router.beforeResolve(async to => {
  if (!to.matched.length) {
    await lazyRoutes;
    if (router.resolve(to.fullPath).matched.length > 0) {
      return { path: to.fullPath };
    }

    return { path: '/not-found' };
  }
  return true;
});

export default router;
