import App from '../components/App.svelte';

describe('App', () => {
  it('should render', () => {
    const target = document.createElement('div');
    new App({
      target,
      props: {
        name: 'Tests'
      }
    });

    expect(target.textContent).toBe('Hello Tests!');
  });
});
