const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');

const resources = path.resolve(__dirname, 'src/main/resources');

module.exports = (env, argv) => ({
  devtool: argv.mode === 'development' ? 'eval-source-map' : 'none',
  entry: './frontend/index.js',
  output: {
    publicPath: '/',
    filename: 'bundle.js',
    path: path.join(resources, 'static')
  },
  module: {
    rules: [
      {
        test: /\.(js|jsx)$/,
        exclude: /node_modules/,
        use: {
          loader: 'babel-loader'
        }
      }
    ]
  },
  resolve: {
    extensions: ['*', '.js', '.jsx']
  },
  plugins: [
    new CleanWebpackPlugin(),
    new HtmlWebpackPlugin({
      hash: true,
      template: './frontend/index.html',
      filename: path.join(resources, 'templates', 'index.html')
    })
  ]
});
