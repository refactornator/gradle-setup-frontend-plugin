import svelte from 'rollup-plugin-svelte';
import copy from 'rollup-plugin-copy';
import resolve from 'rollup-plugin-node-resolve';
import commonjs from 'rollup-plugin-commonjs';
import { terser } from 'rollup-plugin-terser';
import postcss from 'rollup-plugin-postcss';
import cssnext from 'postcss-cssnext';
import cssnano from 'cssnano';
import clean from 'rollup-plugin-delete';

const production = !process.env.ROLLUP_WATCH;

const staticResourceFolder = 'src/main/resources/static/';
const templatesResourceFolder = 'src/main/resources/templates/';

export default {
	input: 'frontend/main.js',
	output: {
		sourcemap: !production,
		format: 'iife',
		name: 'app',
		file: `${staticResourceFolder}bundle.js`
	},
	plugins: [
	  clean({
	    targets: [`${staticResourceFolder}*`, `${templatesResourceFolder}*`]
	  }),
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
				css.write(`${staticResourceFolder}bundle.css`);
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
        { src: 'frontend/static/**/*', dest: staticResourceFolder },
        { src: 'frontend/templates/**/*', dest: templatesResourceFolder }
      ]
    })
	],
	watch: {
		clearScreen: false
	}
};
