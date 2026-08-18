const { ModuleFederationPlugin } = require('@module-federation/enhanced/webpack');

const packageJson = require('../package.json');
// Microfrontend api, should match across gateway and microservices.
const apiVersion = '0.0.1';

const sharedDefaults = { singleton: true, strictVersion: true, requiredVersion: apiVersion };
const shareMappings = (...mappings) => Object.fromEntries(mappings.map(map => [map, { ...sharedDefaults, version: apiVersion }]));

const isValidSemver = version => /^\d/.test(version) || version.startsWith('^') || version.startsWith('~');

const shareDependencies = ({ skipList = [] } = {}) =>
  Object.fromEntries(
    Object.entries(packageJson.dependencies)
      .filter(([dependency]) => !skipList.includes(dependency))
      .map(([dependency, version]) => [
        dependency,
        isValidSemver(version)
          ? { ...sharedDefaults, version, requiredVersion: version }
          : { ...sharedDefaults, version: apiVersion, requiredVersion: apiVersion },
      ]),
  );

module.exports = () => {
  return {
    optimization: {
      moduleIds: 'named',
      chunkIds: 'named',
      runtimeChunk: false,
    },

    plugins: [
      new ModuleFederationPlugin({
        name: 'gateway',
        shareScope: 'default',
        dts: false,
        manifest: false,
        shared: {
          ...shareDependencies(),
          ...shareMappings(
            'app/config/constants',
            'app/config/store',
            'app/shared/error/error-boundary-routes',
            'app/shared/layout/menus/menu-components',
            'app/shared/layout/menus/menu-item',
            'app/shared/reducers',
            'app/shared/reducers/locale',
            'app/shared/reducers/reducer.utils',
            'app/shared/util/date-utils',
            'app/shared/util/entity-utils',
          ),
        },
      }),
    ],
    output: {
      publicPath: 'auto',
      scriptType: 'text/javascript',
    },
  };
};
