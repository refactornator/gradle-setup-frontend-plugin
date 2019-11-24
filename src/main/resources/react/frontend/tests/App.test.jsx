import React from 'react';
import {render} from '@testing-library/react';

import App from '../components/App';

describe('App', () => {
  it('prints Hello World', () => {
    const {getByText} = render(<App />);

    expect(getByText('Hello World!')).toBeInTheDocument();
  });
});
