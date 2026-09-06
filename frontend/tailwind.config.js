/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        cyber: {
          bg: '#0F172A',
          card: '#1E293B',
          hover: '#334155',
          border: '#334155',
          primary: '#2563EB',
          accent: '#10B981',
          gold: '#F59E0B'
        }
      }
    },
  },
  plugins: [],
}
