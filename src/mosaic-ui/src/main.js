// @ts-nocheck
import App from './App.svelte';
import './app.css';

// cesium styles
import '../node_modules/cesium/Build/Cesium/Widgets/widgets.css';

// cesium base url
window[ 'CESIUM_BASE_URL' ] = './cesium/';

const app = new App( {
    target: document.getElementById( 'app' ),
} );

export default app;
