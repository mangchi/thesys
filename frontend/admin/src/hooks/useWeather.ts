import { useQuery } from "@tanstack/react-query";

interface KmaItem {
    category: string;  // TMP, SKY, POP 등
    fcstDate: string;  // 예보일자 YYYYMMDD
    fcstTime: string;  // 예보시각 HHMM
    fcstValue: string; // 예보값
}

interface ForecastSummary {
    time: string;        // HH:mm
    temperature: string; // ℃
    sky: string;         // 구름 상태 (1=맑음,3=구름많음,4=흐림)
    rainProb: string;    // 강수확률 %
}

async function fetchKmaForecast(): Promise<ForecastSummary[]> {
    const serviceKey = import.meta.env.VITE_KMA_SERVICE_KEY;
    if (!serviceKey) {
        throw new Error('KMA 서비스 키가 설정되어 있지 않습니다.');
    }

    // 1. 기준 날짜/시간 계산
    const now = new Date();
    const pad = (n: number) => n.toString().padStart(2, '0');
    const base_date = `${now.getFullYear()}${pad(now.getMonth() + 1)}${pad(now.getDate())}`;

    // 기상청 API는 0200, 0500, 0800, 1100, 1400, 1700, 2000, 2300 제공
    const times = ['2300', '2000', '1700', '1400', '1100', '0800', '0500', '0200'];
    const curHHmm = pad(now.getHours()) + pad(now.getMinutes());
    const base_time = times.find(t => t <= curHHmm) || '2300';

    // 2. URL 구성 (서울 nx=60, ny=127)
    const params = new URLSearchParams({
        serviceKey,
        pageNo: '1',
        numOfRows: '1000',
        dataType: 'JSON',
        base_date,
        base_time,
        nx: '60',
        ny: '127',
    });
    const url = `https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?${params}`;

    const res = await fetch(url);
    if (!res.ok) throw new Error('KMA 단기예보 API 요청 실패');
    const json = await res.json();
    const items: KmaItem[] = json.response.body.items.item;

    // 3. 필요한 카테고리별로 그룹핑해서 요약 생성
    // 가장 이른 6개 예보 시점만 추출
    const nextTimes = Array.from(new Set(items.map(i => i.fcstTime))).slice(0, 6);
    const summary: ForecastSummary[] = nextTimes.map(time => {
        const at = items.filter(i => i.fcstTime === time);
        const temp = at.find(i => i.category === 'TMP')?.fcstValue ?? '-';
        const sky = at.find(i => i.category === 'SKY')?.fcstValue ?? '-';
        const pop = at.find(i => i.category === 'POP')?.fcstValue ?? '-';
        return {
            time: `${time.slice(0, 2)}:${time.slice(2)}`,
            temperature: `${temp}°C`,
            sky,
            rainProb: `${pop}%`,
        };
    });

    return summary;
}

export function useWeather() {
    return useQuery<KmaItem, Error>({
        queryKey: ['kma-forecast'],
        queryFn: fetchKmaForecast,
    });
}