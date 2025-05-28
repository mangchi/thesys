import { useContext, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { styled, useTheme } from '@mui/material/styles';
import Box from '@mui/material/Box';
import Drawer from '@mui/material/Drawer';
import CssBaseline from '@mui/material/CssBaseline';
import MuiAppBar, { AppBarProps as MuiAppBarProps } from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import List from '@mui/material/List';
import Typography from '@mui/material/Typography';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';

import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import InboxIcon from '@mui/icons-material/MoveToInbox';
import MailIcon from '@mui/icons-material/Mail';
import { useAuthStore } from '../stores/auth';
import { Avatar, Button, ClickAwayListener, Divider, SxProps } from '@mui/material';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import { blue } from '@mui/material/colors';
import ThemeToggleButton from './ThemeToggleButton';
import { ColorModeContext } from '../theme/ThemeContext';
import Badge from '@mui/material/Badge';
import GroupIcon from '@mui/icons-material/Group';
import DashboardIcon from '@mui/icons-material/Dashboard';
import QuizIcon from '@mui/icons-material/Quiz';
import DragIndicatorIcon from '@mui/icons-material/DragIndicator';
import PersonIcon from '@mui/icons-material/Person';
import PriorityHighIcon from '@mui/icons-material/PriorityHigh';

const drawerWidth = 240;

interface AppBarProps extends MuiAppBarProps {
  open?: boolean;
}

const AppBar = styled(MuiAppBar, {
  shouldForwardProp: (prop) => prop !== 'open',
})<AppBarProps>(({ theme }) => ({
  transition: theme.transitions.create(['margin', 'width'], {
    easing: theme.transitions.easing.sharp,
    duration: theme.transitions.duration.leavingScreen,
  }),
  variants: [
    {
      props: ({ open }) => open,
      style: {
        // width: `calc(100% - ${drawerWidth}px)`,
        marginLeft: `${drawerWidth}px`,
        transition: theme.transitions.create(['margin', 'width'], {
          easing: theme.transitions.easing.easeOut,
          duration: theme.transitions.duration.enteringScreen,
        }),
      },
    },
  ],
}));

export default function Sidebar() {
  const theme = useTheme();
  const navigate = useNavigate();

  const [open, setOpen] = useState(false);
  const user = useAuthStore((state) => state.user);
  const [dialogOpen, setDialogOpen] = useState(false);

  const { mode } = useContext(ColorModeContext);

  const styles: SxProps = {
    position: 'absolute',
    top: 60,
    // right: 100,
    left: 0.5,
    // position: 'absolute',
    // top: 60,
    // right: '10%',
    // left: 0,
    zIndex: 10,
    width: '160px',
    border: '1px solid #ccc',
    borderRadius: '10px',
    // p: 2,
    padding: '10px',
    bgcolor: 'background.paper',
  };

  const handleDrawerOpen = () => {
    setOpen(true);
  };

  const handleDrawerClose = (newOpen: boolean) => () => {
    setOpen(newOpen);
  };

  const handleDialogOpen = () => {
    setDialogOpen((prev) => !prev);
    // setDialogOpen(true);
  };

  const handleDialogClose = () => {
    setDialogOpen(false);
  };

  const handleLogout = () => {
    useAuthStore.getState().logout(); // 로그아웃 함수 호출
    navigate('/login'); // 로그인 페이지로 이동
    setDialogOpen(false);
  };

  const handleProfileChange = () => {
    navigate('/profile'); // 개인정보 변경 페이지로 이동
    setDialogOpen(false);
  };

  return (
    <Box sx={{ display: 'flex' }}>
      <CssBaseline />
      {/* <AppBar open={open}> */}
      <AppBar position="fixed" open={open}>
        <Toolbar sx={{ display: 'flex', alignItems: 'center' }}>
          <IconButton
            color="inherit"
            aria-label="open drawer"
            onClick={handleDrawerOpen}
            edge="start"
            sx={[
              {
                mr: 2,
              },
              open && { display: 'none' },
            ]}
          >
            <MenuIcon />
          </IconButton>
          <Typography
            sx={{
              position: 'absolute',
              left: '48%',
              // transform: 'translateX(-50%)',
            }}
            variant="h6"
            noWrap
            component="div"
          >
            The System
          </Typography>

          <ClickAwayListener
            mouseEvent="onMouseDown"
            touchEvent="onTouchStart"
            onClickAway={handleDialogClose}
          >
            <Box sx={{ display: 'flex', position: 'relative', marginLeft: 'auto' }}>
              <IconButton>
                <Badge variant="dot" color="error">
                  <MailIcon sx={{ color: 'white' }} />
                </Badge>
              </IconButton>
              <Button
                sx={{
                  cursor: 'pointer',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                }}
                // role="presentation"
                // variant="body1"
                onClick={handleDialogOpen}
              >
                <Avatar sx={{ bgcolor: mode === 'light' ? blue[700] : '#1d1d1d' }}>
                  <AccountCircleIcon sx={{ color: mode === 'light' ? 'white' : 'white' }} />
                </Avatar>
                <Typography sx={{ color: 'white' }}>{user?.name || 'Guest'}</Typography>
                {/* 👤 {user?.name || '게스트'} */}
              </Button>
              {dialogOpen ? (
                <>
                  <Box sx={styles}>
                    <List>
                      <ListItem disablePadding>
                        <ListItemButton onClick={handleProfileChange}>
                          <ListItemText
                            sx={{ color: mode === 'light' ? blue[700] : 'white' }}
                            primary="개인정보 변경"
                          />
                        </ListItemButton>
                      </ListItem>
                      <ListItem disablePadding>
                        <ListItemButton onClick={handleLogout}>
                          <ListItemText
                            sx={{ color: mode === 'light' ? blue[700] : 'white' }}
                            primary="로그아웃"
                          />
                        </ListItemButton>
                      </ListItem>
                    </List>

                    <Button
                      sx={{ color: mode === 'light' ? blue[700] : 'white' }}
                      onClick={handleDialogClose}
                      color="primary"
                    >
                      닫기
                    </Button>
                  </Box>
                </>
              ) : null}
            </Box>
          </ClickAwayListener>
          <ThemeToggleButton />
        </Toolbar>
      </AppBar>

      <Drawer anchor="left" open={open} onClose={handleDrawerClose(false)}>
        <Box sx={{ width: 250 }} role="presentation" onClick={handleDrawerClose(false)}>
          <List>
            {[{ text: 'Dashboard', to: '/dashboard', icon: <DashboardIcon /> }].map(
              ({ text, to }, index) => (
                <ListItem key={text} disablePadding>
                  <ListItemButton component={Link} to={to}>
                    <ListItemIcon>{[<DashboardIcon />][index]}</ListItemIcon>
                    <ListItemText primary={text} />
                  </ListItemButton>
                </ListItem>
              ),
            )}
          </List>
          <Divider />
          <List>
            {[
              { text: 'User', to: '/user', icon: <GroupIcon /> },
              { text: 'Profile', to: '/profile' },
            ].map(({ text, to }, index) => (
              <ListItem key={text} disablePadding>
                <ListItemButton component={Link} to={to}>
                  <ListItemIcon>{[<GroupIcon />, <PersonIcon />][index]}</ListItemIcon>

                  {/* <ListItemIcon>{index % 2 === 0 ? <InboxIcon /> : <GroupIcon />}</ListItemIcon> */}
                  <ListItemText primary={text} />
                </ListItemButton>
              </ListItem>
            ))}
          </List>
          <Divider />
          <List>
            {[
              { text: 'FAQ', to: '/faq', icon: <QuizIcon /> },
              { text: 'Notice', to: '/notice' },
            ].map(({ text, to }, index) => (
              <ListItem key={text} disablePadding>
                <ListItemButton component={Link} to={to}>
                  <ListItemIcon>{[<QuizIcon />, <PriorityHighIcon />][index]}</ListItemIcon>

                  {/* <ListItemIcon>{index % 2 === 0 ? <InboxIcon /> : <GroupIcon />}</ListItemIcon> */}
                  <ListItemText primary={text} />
                </ListItemButton>
              </ListItem>
            ))}
          </List>
          <Divider />
          <List>
            {[{ text: 'Sample', to: '/sample', icon: <DragIndicatorIcon /> }].map(
              ({ text, to }, index) => (
                <ListItem key={text} disablePadding>
                  <ListItemButton component={Link} to={to}>
                    <ListItemIcon>{[<DragIndicatorIcon />][index]}</ListItemIcon>
                    <ListItemText primary={text} />
                  </ListItemButton>
                </ListItem>
              ),
            )}
          </List>
          <Divider />
        </Box>
      </Drawer>
    </Box>
  );
}
