import { defineConfig } from 'vite';
import { svelte } from '@sveltejs/vite-plugin-svelte';
import { viteStaticCopy } from 'vite-plugin-static-copy';

// https://vitejs.dev/config/
export default defineConfig( {
    plugins: [
        viteStaticCopy( {
            targets: [
                { src: 'node_modules/cesium/Build/Cesium/Assets', dest: './cesium/' },
                { src: 'node_modules/cesium/Build/Cesium/ThirdParty', dest: './cesium/' },
                { src: 'node_modules/cesium/Build/Cesium/Widgets', dest: './cesium/' },
                { src: 'node_modules/cesium/Build/Cesium/Workers', dest: './cesium/' }
            ]
        } ),
        svelte()
    ]
} );
