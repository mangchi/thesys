import { useState } from 'react';
import {
  Box,
  Button,
  TextField,
  Typography,
  Paper,
  Stack,
  Alert,
  Checkbox,
  FormControlLabel,
  Divider,
} from '@mui/material';
import { Link } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '../stores/auth';
import { FakerUsers } from '../data/user';
import { CommonText } from '../components/CommonText';
import { AuthResponse, SignInPage, type AuthProvider } from '@toolpad/core/SignInPage';
import { useTheme } from '@mui/material/styles';
import { PageContainer } from '../components/PageContainer';
import { blue } from '@mui/material/colors';

const Login = () => {
  const theme = useTheme();
  const navigate = useNavigate();
  const login = useAuthStore((state) => state.login);

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loginError, setLoginError] = useState('');

  const providers = [
    { id: 'github', name: 'GitHub' },
    { id: 'google', name: 'Google' },
    { id: 'credentials', name: 'Email and Password' },
  ];

  // const signIn = (provider: AuthProvider) => void | Promise<AuthResponse> = async (provider) => {
  //   if (provider.id === 'credentials') {
  //     const user = FakerUsers.find((user) => user.email === email && user.password === password);
  //     if (user) {
  //       login({ email: user.email, name: user.name });
  //       navigate('/dashboard');
  //       return undefined;
  //     } else {
  //       return { error: '이메일 또는 비밀번호가 잘못되었습니다.' };
  //     }
  //   }
  //   return new Promise((resolve) => {
  //     setTimeout(() => {
  //       resolve({ error: 'This is a fake error' });
  //     }, 500);
  //   });
  // };

  // const handleLogin = () => {
  //   //TODO: 로그인 검증 필요
  //   const user = FakerUsers.find((user) => user.email === email && user.password === password);
  //   if (user) {
  //     login({ email: user.email, name: user.name });
  //     navigate('/dashboard');
  //   } else {
  //     setLoginError('이메일 또는 비밀번호가 잘못되었습니다.');
  //   }
  // };

  const RememberMeCheckbox = () => {
    const theme = useTheme();
    return (
      <FormControlLabel
        label="Remember me"
        control={
          <Checkbox
            name="remember"
            value="true"
            color="primary"
            sx={{ padding: 0.5, '& .MuiSvgIcon-root': { fontSize: 20 } }}
          />
        }
        slotProps={{
          typography: {
            color: 'textSecondary',
            fontSize: theme.typography.pxToRem(14),
          },
        }}
      />
    );
  };

  const SignUpLink = () => {
    return (
      <Link to="/signup" className="text-blue-650 no-underline">
        Sign up
      </Link>
    );
  };

  const LoginTitle = () => {
    return (
      <Typography variant="h4" sx={{ marginBottom: 2, color: blue[700] }}>
        The System
      </Typography>
    );
  };

  return (
    <Box
      sx={{
        height: '100%',
        width: '100vw',
        // display: 'flex',
        alignItems: 'center',
        flexDirection: 'column',
        justifyContent: 'center',

        // backgroundColor: '#f4f6f8',
      }}
    >
      {/* <Paper elevation={3} sx={{ padding: 4, width: 360 }}>
        <CommonText variantType="subtitle"> 로그인 </CommonText>

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
          <Button
            variant="contained"
            color="primary"
            fullWidth
            onClick={() => signIn({ id: 'credentials', name: 'Email and Password' })}
          >
            login
          </Button>
        </Stack>
        <Typography variant="body2" align="center" mt={2}>
          계정이 없으신가요?{' '}
          <Link to="/signup" className="text-blue-650 no-underline">
            회원가입
          </Link>
        </Typography>
      </Paper> */}
      {/* <Typography sx={{}} variant="h4">
        The System
      </Typography> */}
      <SignInPage
        sx={{}}
        signIn={(provider, formData) => {
          if (provider.id === 'credentials') {
            const email = formData.get('email') as string;
            const password = formData.get('password') as string;

            const user = FakerUsers.find(
              (user) => user.email === email && user.password === password,
            );
            if (user) {
              login({ email: user.email, name: user.name });
              navigate('/dashboard');
              return undefined; // 성공 시 반환 값 없음
            } else {
              return { error: '이메일 또는 비밀번호가 잘못되었습니다.' }; // 에러 메시지 반환
            }
          }

          // 기타 인증 제공자 처리 (예: GitHub, Google)
          return new Promise((resolve) => {
            setTimeout(() => {
              resolve({ error: 'This is a fake error' });
            }, 500);
          });
        }}
        providers={providers}
        slots={{
          rememberMe: RememberMeCheckbox,
          signUpLink: SignUpLink,
          title: LoginTitle,
        }}
        slotProps={{
          form: { noValidate: true },
          emailField: { autoFocus: true },
          rememberMe: {
            control: (
              <Checkbox
                name="tandc"
                value="true"
                color="primary"
                sx={{ py: 1, px: 0.5, '& .MuiSvgIcon-root': { fontSize: 20 } }}
              />
            ),
            color: 'textSecondary',
            label: 'I agree with the T&C',
          },
        }}
      />
    </Box>
  );
};

export default Login;
