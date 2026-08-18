import path from 'node:path';

import { pluginModuleFederation } from '@module-federation/rsbuild-plugin';
import { defineConfig } from '@rsbuild/core';
import { pluginSass } from '@rsbuild/plugin-sass';
import { pluginVue } from '@rsbuild/plugin-vue';
import { getAbsoluteFSPath } from 'swagger-ui-dist';

import mfConfig from './module-federation.config.ts';

export default defineConfig({
  root: path.join(import.meta.dirname, 'src/main/webapp/'),
  resolve: {
    alias: {
      vue$: 'vue/dist/vue.esm-bundler.js',
      '@content': path.resolve(import.meta.dirname, './src/main/webapp/content'),
      '@': path.resolve(import.meta.dirname, './src/main/webapp/app'),
    },
  },
  plugins: [pluginVue(), pluginSass(), pluginModuleFederation(mfConfig)],
  tools: {
    rspack: {
      output: {
        chunkFormat: 'array-push',
      },
      cache: {
        type: 'persistent',
        storage: {
          type: 'filesystem',
          directory: path.resolve(import.meta.dirname, './target/rsbuild/'),
        },
      },
    },
  },
  output: {
    cleanDistPath: true,
    distPath: {
      root: path.join(import.meta.dirname, './target/classes/static/'),
    },
    copy: [
      ...['js', 'css', 'html', 'png'].map(ext => ({
        // https://github.com/swagger-api/swagger-ui/blob/v4.6.1/swagger-ui-dist-package/README.md
        context: getAbsoluteFSPath(),
        from: `*.${ext}`,
        to: 'swagger-ui/',
        globOptions: { ignore: ['**/index.html'] },
      })),
      {
        from: path.join(path.dirname(require.resolve('axios/package.json')), 'dist/axios.min.js'),
        to: 'swagger-ui/',
      },
      { from: './swagger-ui/', to: 'swagger-ui/' },
      { from: './content/', to: 'content/' },
      { from: './favicon.ico', to: 'favicon.ico' },
      {
        from: './manifest.webapp',
        to: 'manifest.webapp',
      },
      // jhipster-needle-add-assets-to-rsbuild - JHipster will add/remove third-party resources in this array
      { from: './robots.txt', to: 'robots.txt' },
    ],
  },
  html: {
    template: './index.html',
    scriptLoading: 'defer',
    tags: [
      {
        tag: 'base',
        attrs: { href: '/' },
      },
    ],
  },
  source: {
    entry: {
      index: './app/index.ts',
    },
    define: {
      SERVER_API_URL: '"/"',
      APP_VERSION: `"${process.env.APP_VERSION ? process.env.APP_VERSION : 'DEV'}"`,
    },
  },
  server: {
    port: 3000,
    proxy: Object.fromEntries(
      ['/api', '/management', '/v3/api-docs', '/oauth2', '/login', '/services'].map(res => [
        res,
        {
          target: 'http://localhost:8080',
        },
      ]),
    ),
    historyApiFallback: true,
  },
});
