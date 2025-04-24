import { Box, Button, TextField, Typography, Paper, Stack } from '@mui/material';
import { Link } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';

const Login = () => {
  const navigate = useNavigate();

  const handeLogin = () => {
    //TODO: 로그인 검증 필요
    navigate('/');
  };

  return (
    <Box
      sx={{
        height: '100vh',
        width: '100vw',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        backgroundColor: '#f4f6f8',
      }}
    >
      <Paper elevation={3} sx={{ padding: 4, width: 360 }}>
        <Typography variant="h5" gutterBottom>
          Login
        </Typography>

        <Stack spacing={2}>
          <TextField label="Email" variant="outlined" fullWidth type="email" />
          <TextField label="Password" variant="outlined" fullWidth type="password" />
          <Button variant="contained" color="primary" fullWidth onClick={handeLogin}>
            login
          </Button>
        </Stack>
        <Typography variant="body2" align="center" mt={2}>
          계정이 없으신가요?{' '}
          <Link to="/signup" className="text-blue-650 no-underline">
            회원가입
          </Link>
        </Typography>
      </Paper>
    </Box>
  );
};
export default Login;
