<script>
    import { ImageryLayer, TileMapServiceImageryProvider, Viewer } from 'cesium';
    import { onMount } from 'svelte';
    import ViewerStore from '../store/ViewerStore';

    let viewer = null;

    onMount( async () => {

        viewer = new Viewer( 'cesiumContainer', {
            animation: false,
            baseLayerPicker: false,
            fullscreenButton: false,
            vrButton: false,
            geocoder: false,
            homeButton: false,
            sceneModePicker: false,
            timeline: false,
            navigationHelpButton: false,
            navigationInstructionsInitiallyVisible: false,
            scene3DOnly: true,
            shouldAnimate: true,
            baseLayer: ImageryLayer.fromProviderAsync(
                TileMapServiceImageryProvider.fromUrl('./cesium/Assets/Textures/NaturalEarthII'), {}),
            creditContainer: 'cesiumCreditContainer'
        } );

        ViewerStore.set(viewer);

    } );

</script>

<div id="cesiumContainer" class="h-full w-full overflow-hidden">
</div>

<div id="cesiumCreditContainer" hidden>
</div>
