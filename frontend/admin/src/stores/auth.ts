import { create } from 'zustand';

interface AuthState {
    isLoggedIn: boolean;
    user: null | { email: string; name: string;  };
    login: (user: { email: string; name: string }) => void;
  logout: () => void;
}

export const useAuthStore = create<AuthState>((set) => ({
    isLoggedIn: false,
    user: null,
    login: (user) => set({ isLoggedIn: true, user }),
    logout: () => set({ isLoggedIn: false, user: null }),
  }));
