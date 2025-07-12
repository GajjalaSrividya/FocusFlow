import React from 'react';
import { Bar } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  BarElement,
  CategoryScale,
  LinearScale,
  Tooltip,
  Legend,
} from 'chart.js';

ChartJS.register(BarElement, CategoryScale, LinearScale, Tooltip, Legend);

const WeeklyProgressChart = ({ data }) => {
  const chartData = {
    labels: data.map(d => d.week),
    datasets: [
      {
        label: '% Completed',
        data: data.map(d => d.completion),
        backgroundColor: data.map(d =>
          d.completion === 100 ? '#22c55e' : d.completion >= 50 ? '#facc15' : '#ef4444'
        ),
        barThickness: 30,
      },
    ],
  };

  const options = {
    indexAxis: 'x', // Vertical bars
    responsive: true,
    maintainAspectRatio: false,
    scales: {
      y: {
        max: 100,
        beginAtZero: true,
        ticks: {
          callback: (value) => `${value}%`,
        },
        title: {
          display: true,
          text: 'Completion',
        },
      },
      x: {
        ticks: {
          autoSkip: false,
          maxRotation: 0,
          minRotation: 0,
        },
      },
    },
    plugins: {
      legend: { display: false },
      tooltip: {
        callbacks: {
          label: (context) => `${context.parsed.y}% completed`,
        },
      },
    },
  };

  return (
    <div className="mt-6">
      <h2 className="text-lg font-semibold mb-2">📊 Weekly Completion (Last 4 Weeks)</h2>
      <div style={{ height: '200px', width: '100%', maxWidth: '600px' }}>
        <Bar data={chartData} options={options} />
      </div>
    </div>
  );
};

export default WeeklyProgressChart;
