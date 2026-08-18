import React, { Suspense } from 'react';
import { Route } from 'react-router';
import { loadRemote } from '@module-federation/enhanced/runtime';

import LoginRedirect from 'app/modules/login/login-redirect';
import Logout from 'app/modules/login/logout';
import Home from 'app/modules/home/home';
import EntitiesRoutes from 'app/entities/routes';
import PrivateRoute from 'app/shared/auth/private-route';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import PageNotFound from 'app/shared/error/page-not-found';
import { Authority } from 'app/shared/jhipster/constants';

const loading = <div>loading ...</div>;

const Admin = React.lazy(() => import(/* webpackChunkName: "administration" */ 'app/modules/administration'));

const BlogRoutes = React.lazy(() => loadRemote<any>('blog/entities-routes').catch(() => import('app/shared/error/error-loading')));

const NotificationRoutes = React.lazy(() =>
  loadRemote<any>('notification/entities-routes').catch(() => import('app/shared/error/error-loading')),
);

const AppRoutes = () => {
  return (
    <div className="view-routes">
      <Suspense fallback={loading}>
        <ErrorBoundaryRoutes>
          <Route index element={<Home />} />
          <Route path="logout" element={<Logout />} />
          <Route
            path="admin/*"
            element={
              <PrivateRoute hasAnyAuthorities={[Authority.ADMIN]}>
                <Admin />
              </PrivateRoute>
            }
          />
          <Route path="sign-in" element={<LoginRedirect />} />
          <Route
            path="blog/*"
            element={
              <PrivateRoute hasAnyAuthorities={[Authority.USER]}>
                <BlogRoutes />
              </PrivateRoute>
            }
          />
          <Route
            path="notification/*"
            element={
              <PrivateRoute hasAnyAuthorities={[Authority.USER]}>
                <NotificationRoutes />
              </PrivateRoute>
            }
          />
          <Route
            path="*"
            element={
              <PrivateRoute hasAnyAuthorities={[Authority.USER]}>
                <EntitiesRoutes />
              </PrivateRoute>
            }
          />
          <Route path="*" element={<PageNotFound />} />
        </ErrorBoundaryRoutes>
      </Suspense>
    </div>
  );
};

export default AppRoutes;
