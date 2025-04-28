import { useCallback } from 'react';
import { AgGridReact } from 'ag-grid-react';
import type { ColDef, GridReadyEvent } from 'ag-grid-community';
import { AllCommunityModule, ModuleRegistry } from 'ag-grid-community';
ModuleRegistry.registerModules([AllCommunityModule]);
import { themeBalham } from 'ag-grid-community';

interface AgGridComponentProps {
  columnDefs: ColDef[];
  rowData: any[];
  /**
   * 그리드 높이 (px 또는 %)
   * 기본값: 400px
   */
  height?: number | string;
}

export function GridComponent({ columnDefs, rowData, height = 400 }: AgGridComponentProps) {
  const onGridReady = useCallback((params: GridReadyEvent) => {
    // 컬럼 너비를 컨테이너에 맞춰 자동 조정
    params.api.sizeColumnsToFit();
  }, []);

  const myTheme = themeBalham.withParams({ accentColor: 'red' });

  return (
    <div style={{ width: '100%', height }}>
      {/* <div className="ag-theme-alpine" style={{ width: '100%', height }}> */}
      <AgGridReact
        theme={myTheme}
        columnDefs={columnDefs}
        rowData={rowData}
        defaultColDef={{
          sortable: true,
          filter: true,
          resizable: true,
        }}
        onGridReady={onGridReady}
      />
    </div>
  );
}
