// noinspection JSUnusedGlobalSymbols

import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'


export default defineConfig({
    server: {
        proxy: {
            '/api': {
                target: 'http://localhost:8080',
                secure: false,
                changeOrigin: true
            }
        }
    },

    plugins: [ react() ],
    build: {
        outDir: '../backend/target/classes/static/',
    },
})
