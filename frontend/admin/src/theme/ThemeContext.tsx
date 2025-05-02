import { createContext, useMemo, useState, ReactNode } from 'react';
import { ThemeProvider, createTheme, PaletteMode, CssBaseline } from '@mui/material';

interface ThemeContextValue {
  mode: PaletteMode;
  toggleMode: () => void;
}

export const ColorModeContext = createContext<ThemeContextValue>({
  mode: 'light',
  toggleMode: () => {},
});

export const CustomThemeProvider = ({ children }: { children: ReactNode }) => {
  // 초기값을 OS 설정에 맞추려면 useMediaQuery('(prefers-color-scheme: dark)')
  const [mode, setMode] = useState<PaletteMode>('light');
  const colorMode = useMemo(
    () => ({
      mode,
      toggleMode: () => {
        setMode((prev) => (prev === 'light' ? 'dark' : 'light'));
      },
    }),
    [mode],
  );

  const theme = useMemo(
    () =>
      createTheme({
        palette: {
          mode,
          primary: { main: '#1976d2' },
          ...(mode === 'dark' && {
            background: { default: '#121212', paper: '#1d1d1d' },
          }),
        },
      }),
    [mode],
  );

  return (
    <ColorModeContext.Provider value={colorMode}>
      <ThemeProvider theme={theme}>
        <CssBaseline />
        {children}
      </ThemeProvider>
    </ColorModeContext.Provider>
  );
};
