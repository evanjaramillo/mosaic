/** @type {import('tailwindcss').Config} */
export default {
    content: [
        './src/**/*.{html,js,svelte}'
    ],
    theme: {
        extend: {},
        fontFamily: {
            'navbar': [ 'Orbitron' ]
        }
    },
    plugins: [
        require( 'daisyui' )
    ],
};

