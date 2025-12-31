import react from '@vitejs/plugin-react';
import path from 'path';
import { defineConfig, loadEnv } from 'vite';
import vitePluginImp from 'vite-plugin-imp';
import svgrPlugin from 'vite-plugin-svgr';

// https://vitejs.dev/config/
export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd());
  console.log("env==>",env);
  const port = parseInt(env.VITE_PORT) || 8888;
  const apiUrl = env.VITE_API_BASE_URL || 'http://localhost:8080';

  return {
    resolve: {
      alias: {
        '@': path.join(__dirname, 'src'),
      },
    },
    define: {
      'process.env': env,
    },
    server: {
      port,
      proxy: {
        '/api': {
          target: apiUrl,
          // changeOrigin: true,
          rewrite: (path) => path.replace(/^\/api/, '/api'),
        },
      },
    },
    plugins: [
      react({
        jsxImportSource: '@emotion/react',
        babel: {
          plugins: ['@emotion/babel-plugin'],
        },
      }),
      vitePluginImp({
        libList: [
          // {
          //   libName: 'antd',
          //   style: name => `antd/es/${name}/style/index.css`,
          // },
          {
            libName: 'lodash',
            libDirectory: '',
            camel2DashComponentName: false,
            style: () => {
              return false;
            },
          },
        ],
      }),
      svgrPlugin({
        svgrOptions: {
          icon: true,
          // ...svgr options (https://react-svgr.com/docs/options/)
        },
      }),
    ],
  };
});
