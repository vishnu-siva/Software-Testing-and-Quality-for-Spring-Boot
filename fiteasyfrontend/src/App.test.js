import { render, screen } from '@testing-library/react';
import App from './App';

test('renders FitEase title', () => {
  render(<App />);
  const titleElement = screen.getByText(/FitEase/i);
  expect(titleElement).toBeInTheDocument();
});
