import { Authority } from '@/shared/jhipster/constants';

const JhiDocsComponent = () => import('@/admin/docs/docs.vue');

export default [
  {
    path: '/admin/docs',
    name: 'JhiDocsComponent',
    component: JhiDocsComponent,
    meta: { authorities: [Authority.ADMIN] },
  },
];
