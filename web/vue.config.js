module.exports = {
  devServer: {
    disableHostCheck: true,
    port: 2323,
    open: true,
    overlay: {
      warnings: false,
      errors: true,
    },
    proxy: {
      '/vuetify-api': {
        // //target: `http://localhost:${port}/mock`,
        target: 'http://192.168.101.123:6565',
        // target: `http://47.112.11.221:8687`,
        changeOrigin: true,
        pathRewrite: {
          '^/vuetify-api': '',
        },
      },
      '/tmpfile': {
        target: 'http://192.168.101.123:6565',
      },
    },
  },
  transpileDependencies: ['vuetify'],
  publicPath: '/vuetify/',
}
