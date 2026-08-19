declare const VERSION: string;
declare const SERVER_API_URL: string;
declare const DEVELOPMENT: string;
declare const I18N_HASH: string;

declare module '*.json' {
  const value: any;
  export default value;
}

declare module '*.scss' {
  const content: { [className: string]: string };
  export default content;
}

declare module '*.css' {
  const content: { [className: string]: string };
  export default content;
}

declare module '@blog/entities-routes' {
  const _default: () => import('react').React.JSX.Element;
  export default _default;
}

declare module '@blog/entities-menu' {
  const _default: () => import('react').React.JSX.Element;
  export default _default;
}

declare module '@notification/entities-routes' {
  const _default: () => import('react').React.JSX.Element;
  export default _default;
}

declare module '@notification/entities-menu' {
  const _default: () => import('react').React.JSX.Element;
  export default _default;
}
