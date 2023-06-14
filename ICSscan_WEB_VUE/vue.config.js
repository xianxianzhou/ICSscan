const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({

  transpileDependencies: [
    'vuetify'
  ],
  devServer: {
    port: 8180,
    open: true,
    https: false,
    proxy: {
      "/gonapi": {
      target: "http://13.213.149.227:7097",
      changeOrigin: true,
      secure: false,
      pathRewrite: {
        "^/gonapi": "/",
      },
    },

    }
  
  },
})
