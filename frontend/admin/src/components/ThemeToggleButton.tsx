import React, { useContext } from 'react';
import { IconButton, Tooltip } from '@mui/material';
import { Brightness4, Brightness7 } from '@mui/icons-material';
import { ColorModeContext } from '../theme/ThemeContext';

export default function ThemeToggleButton() {
  const { mode, toggleMode } = useContext(ColorModeContext);
  return (
    <Tooltip title="모드 전환">
      <IconButton onClick={toggleMode} color="inherit">
        {mode === 'light' ? <Brightness4 /> : <Brightness7 />}
      </IconButton>
    </Tooltip>
  );
}
