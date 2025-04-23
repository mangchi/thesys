import { Outlet } from 'react-router-dom';
import Sidebar from '../components/Sidebar';

const Layout = () => {
  return (
    <div className="flex">
      {/* <div className="relative flex h-screen"> */}
      {/* <div style={{ display: 'flex' }}> */}
      <Sidebar />
      <div className="relative h-screen">
        <main
          className="  pt-20 "
          // style={{ flexGrow: 1, padding: '24px' }}
          // style={{ flexGrow: 1, padding: '24px', marginLeft: 240 }}
        >
          <Outlet />
        </main>
      </div>
    </div>
  );
};

export default Layout;
