import svelte from 'rollup-plugin-svelte';
import copy from 'rollup-plugin-copy';
import resolve from 'rollup-plugin-node-resolve';
import commonjs from 'rollup-plugin-commonjs';
import { terser } from 'rollup-plugin-terser';
import postcss from 'rollup-plugin-postcss';
import cssnext from 'postcss-cssnext';
import cssnano from 'cssnano';

const production = !process.env.ROLLUP_WATCH;

export default {
	input: 'frontend/main.js',
	output: {
		sourcemap: !production,
		format: 'iife',
		name: 'app',
		file: 'src/main/resources/static/bundle.js'
	},
	plugins: [
    postcss({
      plugins: [
        cssnext({ warnForDuplicates: false, }),
        cssnano(),
      ],
      extensions: [ '.css' ],
    }),
		svelte({
			// enable run-time checks when not in production
			dev: !production,
			// we'll extract any component CSS out into
			// a separate file — better for performance
			css: css => {
				css.write('src/main/resources/static/bundle.css');
			}
		}),

		// If you have external dependencies installed from
		// npm, you'll most likely need these plugins. In
		// some cases you'll need additional configuration —
		// consult the documentation for details:
		// https://github.com/rollup/rollup-plugin-commonjs
		resolve({
			browser: true,
			dedupe: importee => importee === 'svelte' || importee.startsWith('svelte/')
		}),
		commonjs(),

		// If we're building for production (npm run build
		// instead of npm run dev), minify
		production && terser(),

    copy({
      targets: [
        { src: 'frontend/static/**/*', dest: 'src/main/resources/static' },
        { src: 'frontend/templates/**/*', dest: 'src/main/resources/templates' }
      ]
    })
	],
	watch: {
		clearScreen: false
	}
};
