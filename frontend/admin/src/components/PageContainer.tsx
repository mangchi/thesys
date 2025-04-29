import React from 'react';
import { Box, BoxProps } from '@mui/material';

interface PageContainerProps extends BoxProps {
  children: React.ReactNode;
}

export function PageContainer({ children, sx, ...rest }: PageContainerProps) {
  return (
    <Box
      sx={{
        // maxWidth: '1280px',
        // margin: '0 auto',
        // padding: '2rem',
        width: '100%',
        p: '3',
        ...sx,
      }}
      {...rest}
    >
      {children}
    </Box>
  );
}
