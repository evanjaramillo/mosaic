<script>
    import '@fontsource/orbitron/500.css'; // Defaults to weight 400
    import { CesiumTerrainProvider, UrlTemplateImageryProvider } from 'cesium';
    import ViewerStore from '../store/ViewerStore';

    let contextMetadata = [];
    $: viewer = $ViewerStore;

    const fetchConfiguredContexts = async () => {

        const resp = await fetch( '/api/tiles' );
        return resp.json();

    };

    const fetchContextMetadata = async ( contextName ) => {

        const resp = await fetch( `/api/tiles/${contextName}/metadata` );
        return resp.json();

    };

    const terrainSelectEvent = async (metadata) => {
        
        if (!metadata || !viewer) {
            return;
        }
        
        console.log("terrain: ", metadata);

        const context = metadata.otherParameters["_originatingContext"];

        const terrainProvider = CesiumTerrainProvider.fromUrl(`/api/tiles/${context}`, {
            requestVertexNormals: true
        });

        viewer.terrainProvider = await terrainProvider;

    };
    
    const imagerySelectEvent = (metadata) => {
        
        if (!metadata || !viewer) {
            return;
        }
        
        console.log("imagery: ", metadata);
        
        const context = metadata.otherParameters["_originatingContext"];

        const imagery = new UrlTemplateImageryProvider({
            url: `/api/tiles/${context}/{z}/{x}/{reverseY}/data`,
            credit: metadata.attribution,
            minimumLevel: metadata.minZoom,
            maximumLevel: metadata.maxZoom
        });

        viewer.imageryLayers.addImageryProvider(imagery);

    };

    fetchConfiguredContexts().then( ( contexts ) => {

        if ( ! contexts || contexts.length === 0 ) {

            return; // nothing to do.

        }

        for ( let i = 0; i < contexts.length; i ++ ) {

            fetchContextMetadata( contexts[ i ] ).then( ( metadata ) => {

                if (!metadata) {
                    return;
                }

                metadata.otherParameters["_originatingContext"] = contexts[i];
                contextMetadata = [ ...contextMetadata, metadata ];

            } );

        }

    } );

</script>

<div class="navbar bg-base-300">
    <div class="flex-1">
        <span class="font-navbar text-3xl text-accent select-none">Mosaic</span>
    </div>
    <div class="flex-none">
        <div class="dropdown dropdown-end">
            <button class="btn btn-ghost text-accent text-lg font-navbar">Imagery</button>
            <ul class="dropdown-content z-[1] menu p-2 shadow bg-base-100 rounded-box w-52">
                {#each contextMetadata as metadata }
                    <li>
                        <button class="btn btn-xs btn-ghost" on:click={() => imagerySelectEvent(metadata)}>
                            {metadata.name}
                        </button>
                    </li>
                {/each}
            </ul>
        </div>
        <div class="dropdown dropdown-end">
            <button class="btn btn-ghost text-accent text-lg font-navbar">Terrain</button>
            <ul class="dropdown-content z-[1] menu p-2 shadow bg-base-100 rounded-box w-52">
                {#each contextMetadata as metadata }
                    <li>
                        <button class="btn btn-xs btn-ghost" on:click={() => terrainSelectEvent(metadata)}>
                            {metadata.name}
                        </button>
                    </li>
                {/each}
            </ul>
        </div>
    </div>
</div>
