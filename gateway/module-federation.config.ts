import type { createModuleFederationConfig } from '@module-federation/sdk';

import packageJson from './package.json' with { type: 'json' };

// Microfrontend api, should match across gateway and microservices.
const apiVersion = '0.0.1';

const sharedDefaults = { singleton: true, strictVersion: true, requiredVersion: apiVersion };
const shareMappings = (...mappings: string[]) => Object.fromEntries(mappings.map(map => [map, { ...sharedDefaults, version: apiVersion }]));

const shareDependencies = ({ skipList = [] }: { skipList?: string[] } = {}) =>
  Object.fromEntries(
    Object.entries(packageJson.dependencies)
      .filter(([dependency]) => !skipList.includes(dependency))
      .map(([dependency, version]) => [dependency, { ...sharedDefaults, version, requiredVersion: version }]),
  );

export default {
  name: 'gateway',
  exposes: {
    './entities-router': './app/router/entities.ts',
    './entities-menu': './app/entities/entities-menu.vue',
    './i18n-en': './i18n/en/en.js',
    // jhipster-needle-expose
  },
  filename: 'remoteEntry.js',
  shareScope: 'default',
  shared: {
    ...shareDependencies(),
    ...shareMappings('@/shared/jhipster/constants', '@/shared/alert/alert.service', '@/locale/translation.service'),
  },
  dts: false,
  manifest: true,
} as const satisfies Parameters<typeof createModuleFederationConfig>[0];
