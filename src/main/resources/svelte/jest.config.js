// https://jestjs.io/docs/en/configuration.html

module.exports = {
  // Automatically clear mock calls and instances between every test
  clearMocks: true,

  // Automatically reset mock state between every test
  resetMocks: false,

  transform: {
    '\\.js$': 'babel-jest',
    '\\.svelte$': 'svelte-jest'
  },

  moduleFileExtensions: [
    'js',
    'json',
    'svelte'
  ]
};
