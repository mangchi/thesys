import React from 'react';
import { Card, CardContent, Typography } from '@mui/material';
import { LineChart, BarChart, PieChart, ScatterChart } from '@mui/x-charts';

export type ChartType = 'line' | 'bar' | 'pie' | 'scatter';

export interface ChartData {
  x: number | string;
  y: number;
}

export interface ChartProps {
  /** 차트 종류 */
  type: ChartType;
  /** 데이터 배열(x, y) */
  data: ChartData[];
  /** 차트 제목(옵셔널) */
  title?: string;
  /** 차트 너비(기본 600) */
  width?: number;
  /** 차트 높이(기본 300) */
  height?: number;
  /** 추가적인 자식 요소 */
  children?: React.ReactNode;
  /** 스타일 확장 */
  sx?: object;
}

const DEFAULT_WIDTH = 300;
const DEFAULT_HEIGHT = 100;

/**
 * props.type에 따라 여러 종류의 차트를 렌더링하는 컴포넌트
 */
//TODO 수정예정
const Chart = ({ type, data, title, width, height, children, sx, ...rest }: ChartProps) => {
  width = width ?? DEFAULT_WIDTH;
  height = height ?? DEFAULT_HEIGHT;

  const renderChart = () => {
    switch (type) {

      case 'bar':
        return (
          <BarChart
            width={width}
            height={height}
            series={[{ type: 'bar', data, xField: 'x', yField: 'y' }]}
          />
        );

      case 'line':
        return (
          <LineChart
            width={width}
            height={height}
            series={[{ type: 'line', data, xField: 'x', yField: 'y' }]}
          />
        );

      case 'scatter':
        return (
          <ScatterChart
            width={width}
            height={height}
            series={[{ type: 'scatter', data, xField: 'x', yField: 'y' }]}
          />
        );

      case 'pie': {
        const pieData = data.map(({ x, y }) => ({ argument: x, value: y }));
        return (
          <PieChart
            width={width}
            height={height}
            series={[
              {
                type: 'pie',
                data: pieData,
                // argumentField: 'argument',
                // valueField: 'value',
              },
            ]}
          />
        );
      }

      default:
        return null;
    }
  };

  return (
    <Card sx={{ maxWidth: width, margin: '0 auto', ...sx }} {...rest}>
      {title && (
        <CardContent>
          <Typography variant="h6">{title}</Typography>
        </CardContent>
      )}
      <CardContent>{renderChart()}</CardContent>
      {children && <CardContent>{children}</CardContent>}
    </Card>
  );
};

export default Chart;
