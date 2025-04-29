import { Typography, TypographyProps, useTheme } from '@mui/material';

type TypographyType = 'title' | 'subtitle' | 'body' | 'caption';

interface TypograpyComponentProps extends TypographyProps {
  variantType?: TypographyType;
}
//TODO: 수정중
export function CommonText({
  variantType = 'body',
  children,
  sx,
  ...rest
}: TypograpyComponentProps) {
  const theme = useTheme();

  const stylesByType = {
    title: {
      fontSize: '2rem',
      fontWeight: 700,
      padding: '0.5rem 0',
    },
    subtitle: {
      fontSize: '1.5rem',
      fontWeight: 600,
      color: theme.palette.text.secondary,
      padding: '0.5rem 0',
    },
    body: {
      fontSize: '1rem',
      fontWeight: 400,
    },
    caption: {
      fontSize: '0.75rem',
      fontWeight: 300,
      color: theme.palette.text.disabled,
    },
  };

  return (
    <Typography
      sx={{
        ...stylesByType[variantType],
        ...sx,
      }}
      {...rest}
    >
      {children}
    </Typography>
  );
}
