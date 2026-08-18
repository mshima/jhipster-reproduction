import React from 'react';
import { createRoot } from 'react-dom/client';
import { Provider } from 'react-redux';
import { bindActionCreators } from 'redux';
import { registerRemotes } from '@module-federation/enhanced/runtime';

registerRemotes([
  {
    name: 'blog',
    entry: '/services/blog/remoteEntry.js',
  },
  {
    name: 'notification',
    entry: '/services/notification/remoteEntry.js',
  },
]);

import getStore from 'app/config/store';
import { registerLocale } from 'app/config/translation';
import setupAxiosInterceptors from 'app/config/axios-interceptor';
import { clearAuthentication } from 'app/shared/reducers/authentication';
import ErrorBoundary from 'app/shared/error/error-boundary';
import AppComponent from 'app/app';
import { loadIcons } from 'app/config/icon-loader';

const store = getStore();
registerLocale(store);

const actions = bindActionCreators({ clearAuthentication }, store.dispatch);
setupAxiosInterceptors(() => actions.clearAuthentication('login.error.unauthorized'));

loadIcons();

const rootEl = document.getElementById('root');
const root = createRoot(rootEl!);

root.render(
  <ErrorBoundary>
    <Provider store={store}>
      <AppComponent />
    </Provider>
  </ErrorBoundary>,
);
