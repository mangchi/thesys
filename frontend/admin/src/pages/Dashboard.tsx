import Grid from '@mui/material/Grid';
import { Box, Card, CardContent, Typography } from '@mui/material';
import TrendingUpIcon from '@mui/icons-material/TrendingUp';
import PeopleIcon from '@mui/icons-material/People';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import BarChartIcon from '@mui/icons-material/BarChart';

const Dashboard = () => {
  const stats = [
    {
      title: '총 사용자 수',
      value: '1,024명',
      icon: <PeopleIcon fontSize="large" color="primary" />,
    },
    {
      title: '월간 방문자 수',
      value: '8,560명',
      icon: <TrendingUpIcon fontSize="large" color="success" />,
    },
    {
      title: '총 주문 수',
      value: '324건',
      icon: <ShoppingCartIcon fontSize="large" color="secondary" />,
    },
    {
      title: '매출 통계',
      value: '₩12,345,000',
      icon: <BarChartIcon fontSize="large" color="action" />,
    },
  ];

  return (
    <Box>
      {/* <Box sx={{ mt: 8, px: 3 }}> */}
      {/* // <Box sx={{ padding: 3 }}> */}
      <Typography variant="h5" mb={3}>
        Dashboard
      </Typography>
      <Grid container spacing={3}>
        {stats.map((item, index) => (
          // <Grid item xs={12} sm={6} md={3} key={index}>
          <Grid item xs={12} sm={6} md={3} key={index} {...(item as any)}>
            <Card variant="outlined" sx={{ display: 'flex', alignItems: 'center', padding: 2 }}>
              <Box sx={{ marginRight: 2 }}>{item.icon}</Box>
              <CardContent sx={{ padding: 0 }}>
                <Typography variant="subtitle2" color="text.secondary">
                  {item.title}
                </Typography>
                <Typography variant="h6">{item.value}</Typography>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
};

export default Dashboard;
