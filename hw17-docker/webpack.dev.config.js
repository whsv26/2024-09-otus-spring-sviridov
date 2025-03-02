const HtmlWebpackPlugin = require('html-webpack-plugin')
const path = require('path');
const ForkTsCheckerWebpackPlugin = require("fork-ts-checker-webpack-plugin");

module.exports = {
    entry: './src/ui/index.tsx',
    devtool: 'inline-source-map',
    mode: 'development',
    output: {
        path: path.resolve(__dirname),
        filename: 'bundle.js',
        libraryTarget: 'umd'
    },

    resolve: {
        extensions: ['.tsx', '.ts', '.js'] // allows importing without specifying extensions
    },

    devServer: {
        static: path.resolve(__dirname) + '/src/ui',
        compress: true,
        port: 9000,
        host: 'localhost',
        open: true,
        proxy: {
            '*': {
              target: 'http://localhost:8080',
              secure: false,
              changeOrigin: true
            }
        }
    },

    module: {
        rules: [
            {
                test: /\.(js|ts|tsx)$/,
                exclude: /(node_modules|bower_components|build)/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: [
                            '@babel/preset-env',
                            '@babel/preset-react',
                            '@babel/preset-typescript'
                        ]
                    }
                }
            }
        ]
    },
    plugins: [
        new HtmlWebpackPlugin({
            filename: 'index.html',
            template: 'src/ui/index.html'
        }),
        new ForkTsCheckerWebpackPlugin(),
    ]
}
