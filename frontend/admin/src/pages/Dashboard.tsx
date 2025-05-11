import Grid from '@mui/material/Grid';
import { Box, Card, CardContent, Tab, Tabs, Typography } from '@mui/material';
import TrendingUpIcon from '@mui/icons-material/TrendingUp';
import PeopleIcon from '@mui/icons-material/People';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import BarChartIcon from '@mui/icons-material/BarChart';
import { PageContainer } from '../components/PageContainer';
import { CommonText } from '../components/CommonText';
import { BarChart, LineChart, PieChart } from '@mui/x-charts';
import { useState } from 'react';

const Dashboard = () => {
  const [tabValue, setTabValue] = useState('1');
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

  // Bar Chart
  const dataset = [
    {
      london: 59,
      paris: 57,
      newYork: 86,
      seoul: 21,
      month: 'Jan',
    },
    {
      london: 50,
      paris: 52,
      newYork: 78,
      seoul: 28,
      month: 'Feb',
    },
    {
      london: 47,
      paris: 53,
      newYork: 106,
      seoul: 41,
      month: 'Mar',
    },
    {
      london: 54,
      paris: 56,
      newYork: 92,
      seoul: 73,
      month: 'Apr',
    },
    {
      london: 57,
      paris: 69,
      newYork: 92,
      seoul: 99,
      month: 'May',
    },
    {
      london: 60,
      paris: 63,
      newYork: 103,
      seoul: 144,
      month: 'June',
    },
    {
      london: 59,
      paris: 60,
      newYork: 105,
      seoul: 319,
      month: 'July',
    },
    {
      london: 65,
      paris: 60,
      newYork: 106,
      seoul: 249,
      month: 'Aug',
    },
    {
      london: 51,
      paris: 51,
      newYork: 95,
      seoul: 131,
      month: 'Sept',
    },
    {
      london: 60,
      paris: 65,
      newYork: 97,
      seoul: 55,
      month: 'Oct',
    },
    {
      london: 67,
      paris: 64,
      newYork: 76,
      seoul: 48,
      month: 'Nov',
    },
    {
      london: 61,
      paris: 70,
      newYork: 103,
      seoul: 25,
      month: 'Dec',
    },
  ];

  const chartSetting = {
    yAxis: [
      {
        label: 'rainfall (mm)',
        width: 60,
      },
    ],
    height: 300,
  };

  const valueFormatter = (value: number | null) => {
    return `${value}mm`;
  };

  //Line Chart
  const margin = { right: 24 };
  const uData = [4000, 3000, 2000, 2780, 1890, 2390, 3490];
  const pData = [2400, 1398, 9800, 3908, 4800, 3800, 4300];
  const xLabels = ['Page A', 'Page B', 'Page C', 'Page D', 'Page E', 'Page F', 'Page G'];

  const handleTabChange = (event: React.SyntheticEvent, newValue: string) => {
    setTabValue(newValue);
  };

  return (
    <PageContainer>
      <Box sx={{ width: '100%' }}>
        <CommonText variantType="title"> Dashboard</CommonText>
        <Tabs value={tabValue} onChange={handleTabChange}>
          <Tab value="1" label="Dashboard1" />
          <Tab value="2" label="DashBoard2" />
          <Tab value="3" label="DashBoard3" />
        </Tabs>
        <p className="my-4" />
        {/* <Typography variant="h5" mb={3}>
        Dashboard
      </Typography> */}
        <Grid container spacing={2} sx={{ marginTop: 2 }}>
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
        <div className="flex">
          <Card sx={{ marginTop: 3, marginRight: 2, width: '30%' }}>
            <CommonText sx={{ padding: 2 }} variantType="body">
              Costs
            </CommonText>

            <PieChart
              series={[
                {
                  data: [
                    { id: 0, value: 10, label: 'series A' },
                    { id: 1, value: 15, label: 'series B' },
                    { id: 2, value: 20, label: 'series C' },
                  ],
                },
              ]}
              width={200}
              height={200}
            />
          </Card>
          <Card sx={{ marginTop: 3, width: '70%' }}>
            <CommonText sx={{ padding: 2 }} variantType="body">
              City
            </CommonText>
            <BarChart
              dataset={dataset}
              xAxis={[{ dataKey: 'month' }]}
              series={[
                { dataKey: 'london', label: 'London', valueFormatter },
                { dataKey: 'paris', label: 'Paris', valueFormatter },
                { dataKey: 'newYork', label: 'New York', valueFormatter },
                { dataKey: 'seoul', label: 'Seoul', valueFormatter },
              ]}
              {...chartSetting}
            />
          </Card>
        </div>
        <Card sx={{ marginTop: 3, marginBottom: 3 }}>
          <CommonText sx={{ padding: 2 }} variantType="body">
            Total
          </CommonText>
          <LineChart
            height={300}
            series={[
              { data: pData, label: 'pv' },
              { data: uData, label: 'uv' },
            ]}
            xAxis={[{ scaleType: 'point', data: xLabels }]}
            yAxis={[{ width: 50 }]}
            margin={margin}
          />
        </Card>
      </Box>
    </PageContainer>
  );
};

export default Dashboard;
