// https://jestjs.io/docs/en/configuration.html

module.exports = {
  // Automatically clear mock calls and instances between every test
  clearMocks: true,

  // Automatically reset mock state between every test
  resetMocks: true,

  // https://jestjs.io/docs/en/configuration.html#setupfilesafterenv-array
  setupFilesAfterEnv: ['./jest.setup.js']
};
