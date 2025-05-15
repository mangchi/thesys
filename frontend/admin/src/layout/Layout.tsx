import { Outlet } from 'react-router-dom';
import Sidebar from '../components/Sidebar';

const Layout = () => {
  return (
    <div className="flex">
      <Sidebar />
      <div className="relative h-full w-full">
        <main className="pt-20 pr-16">
          <Outlet />
        </main>
      </div>
    </div>
  );
};

export default Layout;
