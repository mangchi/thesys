import Grid from '@mui/material/Grid';
import {
  Box,
  Button,
  Card,
  CardContent,
  Checkbox,
  Chip,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  FormControlLabel,
  Paper,
  Tab,
  Tabs,
  Typography,
} from '@mui/material';
import TrendingUpIcon from '@mui/icons-material/TrendingUp';
import PeopleIcon from '@mui/icons-material/People';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import BarChartIcon from '@mui/icons-material/BarChart';
import { PageContainer } from '../components/PageContainer';
import { CommonText } from '../components/CommonText';
import { BarChart, LineChart, PieChart } from '@mui/x-charts';
import { useEffect, useState } from 'react';
import { GenericTabs } from '../components/GenericTabs';
import WeatherWidget from '../components/WeatherWidget';
import {
  FormatAlignLeftOutlined,
  HomeRepairServiceOutlined,
  TrendingDownOutlined,
  TrendingUpOutlined,
} from '@mui/icons-material';
import { blue, red } from '@mui/material/colors';
import { color } from '@mui/system';

const EXPIRY_KEY = 'admin_notice_dismissed_until';

const Dashboard = () => {
  const [noticeOpen, setNoticeOpen] = useState(false);
  const [dontShowDay, setDontShowDay] = useState(false);

  useEffect(() => {
    const expiryStr = localStorage.getItem(EXPIRY_KEY);
    const now = Date.now();

    if (expiryStr) {
      const expiry = parseInt(expiryStr, 10);
      if (expiry > now) {
        // 아직 유효기간 남아 있음
        return;
      }
      // 유효기간 지남 → 팝업 다시 보여주기
    }

    // 처음 방문이거나, 유효기간 지났을 때만 팝업 열기
    setNoticeOpen(true);
  }, []);

  const handleClose = () => {
    const now = Date.now();

    if (dontShowDay) {
      // 하루(24h) 뒤까지 숨기기
      const oneDayMs = 24 * 60 * 60 * 1000;
      localStorage.setItem(EXPIRY_KEY, String(now + oneDayMs));
    } else {
      // 체크 안 했으면 영구 숨기기
      localStorage.removeItem(EXPIRY_KEY);
    }

    setNoticeOpen(false);
  };

  const stats = [
    {
      title: '총 사용자 수',
      value: '1,024명',
      icon: <PeopleIcon fontSize="large" color="primary" />,
      isLoss: true,
      percentage: '10%',
      extra: '100명',
    },
    {
      title: '월간 방문자 수',
      value: '8,560명',
      icon: <TrendingUpIcon fontSize="large" color="success" />,
      percentage: '27.4%',
      extra: '2000명',
    },
    {
      title: '총 주문 수',
      value: '324건',
      icon: <ShoppingCartIcon fontSize="large" color="secondary" />,
      isLoss: true,
      percentage: '56.2%',
      extra: '150건',
    },
    {
      title: '매출 통계',
      value: '₩12,345,000',
      icon: <BarChartIcon fontSize="large" color="action" />,
      percentage: '30.1%',
      extra: '300만원',
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

  // Tab Change Handler
  // const handleTabChange = (event: React.SyntheticEvent, newValue: string) => {
  //   setTabValue(newValue);
  // };

  return (
    <PageContainer>
      <Box sx={{ width: '100%' }}>
        <CommonText variantType="title"> Dashboard</CommonText>

        <p className="my-4" />

        <Grid container spacing={2} sx={{ marginTop: 2 }}>
          {stats.map((item, index) => (
            <Grid size={3} item xs={12} sm={6} md={3} key={index} {...(item as any)}>
              <Card
                variant="outlined"
                sx={{ display: 'flex', alignItems: 'center', padding: 2, height: 140 }}
              >
                <Box sx={{ marginRight: 2, marginBottom: 3 }}>{item.icon}</Box>
                <CardContent sx={{ padding: 0 }}>
                  <Typography variant="subtitle1" color="text.secondary">
                    {item.title}
                  </Typography>
                  <div className="flex">
                    <Typography variant="h6">{item.value}</Typography>
                    <Chip
                      sx={{
                        borderRadius: '5px',
                        ml: 1.25,
                        pl: 1,
                        color: item.isLoss ? blue[500] : red[500],
                      }}
                      variant="outlined"
                      color={item.isLoss ? 'primary' : 'error'}
                      icon={
                        item.isLoss ? (
                          <TrendingDownOutlined style={{ color: blue[500] }} />
                        ) : (
                          <TrendingUpOutlined style={{ color: red[500] }} />
                        )
                      }
                      // label={`${percentage}%`}
                      size="medium"
                      label={item.percentage}
                    />
                  </div>
                  <Box sx={{ pt: 2 }}>
                    <Typography sx={{ display: 'flex' }} variant="body2" color="text.secondary">
                      전월대비 {''}
                      <Typography
                        sx={{ color: item.isLoss ? blue[500] : red[500] }}
                        variant="body2"
                      >
                        {item.extra}
                      </Typography>{' '}
                      {item.isLoss ? '감소' : '증가'}
                    </Typography>
                  </Box>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>

        {/* <Grid container spacing={3}>
          <Grid item xs={12} sm={6} md={3}>
            <WeatherWidget />
          </Grid>
        </Grid> */}

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
          <Card sx={{ marginTop: 3, marginRight: 2, width: '50%' }}>
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

          <WeatherWidget sx={{ marginTop: 3, width: '30%' }} />
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

      <Dialog open={noticeOpen} maxWidth="sm" fullWidth>
        <DialogTitle>📢 공지사항</DialogTitle>
        <DialogContent dividers>
          <Typography variant="body1" gutterBottom>
            안녕하세요, 관리자님!
            <br />
            • 새로운 기능: 사용자 통계 페이지가 추가되었습니다.
            <br />
            • 보안 업데이트: 비밀번호 정책이 강화되었습니다.
            <br />• 다음 점검: 5월 25일(일) 03:00~04:00 예정입니다.
          </Typography>
          <FormControlLabel
            control={
              <Checkbox checked={dontShowDay} onChange={(e) => setDontShowDay(e.target.checked)} />
            }
            label="하루 동안 보지 않기"
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} variant="contained" color="primary">
            확인
          </Button>
        </DialogActions>
      </Dialog>
    </PageContainer>
  );
};

export default Dashboard;
