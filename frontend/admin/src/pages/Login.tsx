import { Box, Button, TextField, Typography, Paper, Stack, Alert } from '@mui/material';
import { Link } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '../stores/auth';
import { FakerUsers } from '../data/user';
import { useState } from 'react';
import { CommonText } from '../components/CommonText';

const Login = () => {
  const navigate = useNavigate();
  const login = useAuthStore((state) => state.login);

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loginError, setLoginError] = useState('');

  const handleLogin = () => {
    //TODO: 로그인 검증 필요
    const user = FakerUsers.find((user) => user.email === email && user.password === password);
    if (user) {
      login({ email: user.email, name: user.name });
      navigate('/dashboard');
    } else {
      setLoginError('이메일 또는 비밀번호가 잘못되었습니다.');
    }
  };
  // navigate('/dashboard');

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
        <CommonText variantType="subtitle"> 로그인 </CommonText>
        {/* <Typography variant="h5" gutterBottom>
          Login
        </Typography> */}

        <Stack spacing={2}>
          <TextField
            label="Email"
            variant="outlined"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            type="email"
            fullWidth
          />
          <TextField
            label="Password"
            variant="outlined"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            fullWidth
            type="password"
          />
          {loginError && <Alert severity="error">{loginError}</Alert>}
          <Button variant="contained" color="primary" fullWidth onClick={handleLogin}>
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
