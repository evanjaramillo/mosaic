<script>
    import {onMount} from "svelte";
    import {Viewer} from "cesium";
    import '../node_modules/cesium/Build/Cesium/Widgets/widgets.css';

    window["CESIUM_BASE_URL"] = './build';

    let viewer;
    onMount( async () => {
        viewer = new Viewer('cesiumContainer', {
            homeButton: false,
            geocoder: false,
            baseLayerPicker: false,
            sceneModePicker: false,
            timeline: false,
            navigationHelpButton: false,
            navigationInstructionsInitiallyVisible: false,
            scene3DOnly: true,
            shouldAnimate: false,
            animation: false,
            creditContainer: 'creditContainer'
        });
    })
</script>

<div id="cesiumContainer">
</div>
<div id="creditContainer" hidden>
</div>
