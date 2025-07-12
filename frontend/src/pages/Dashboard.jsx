import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Calendar from "react-calendar";
import "react-calendar/dist/Calendar.css";
import WeeklyProgressChart from "./WeeklyProgressChart";

const API = import.meta.env.VITE_API_URL;

const Dashboard = () => {
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [quote, setQuote] = useState("Stay motivated!");
  const [tasks, setTasks] = useState([]);
  const [isFirstLogin, setIsFirstLogin] = useState(false);
  const [newTaskDate, setNewTaskDate] = useState("");
  const [newTaskDesc, setNewTaskDesc] = useState("");
  const [selectedDate, setSelectedDate] = useState(new Date());
  const [editIndex, setEditIndex] = useState(null);
  const [editDescription, setEditDescription] = useState("");
  const [showCalendar, setShowCalendar] = useState(false);
  const [completionPercentage, setCompletionPercentage] = useState(0);
  const [weeklyData, setWeeklyData] = useState([]);
  const [upcomingTasks, setUpcomingTasks] = useState([]);
  const [showUpcoming, setShowUpcoming] = useState(false);

  const decodeToken = (token) => {
    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(
        atob(base64).split('').map(c =>
          '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
        ).join('')
      );
      return JSON.parse(jsonPayload);
    } catch (error) {
      return {};
    }
  };

  const formatLocalDate = (dateObj) => {
    const year = dateObj.getFullYear();
    const month = String(dateObj.getMonth() + 1).padStart(2, "0");
    const day = String(dateObj.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`;
  };

  const localQuotes = [
    "Push yourself, because no one else is going to do it for you.",
    "Success doesn’t just find you. You have to go out and get it.",
    "Great things never come from comfort zones.",
    "Wake up with determination. Go to bed with satisfaction.",
    "Believe in yourself. You are braver than you think."
  ];

  const fetchQuote = () => {
    const randomIndex = Math.floor(Math.random() * localQuotes.length);
    setQuote(localQuotes[randomIndex]);
  };

  const fetchTasksForDate = async (dateObj) => {
    const token = localStorage.getItem("token");
    const date = formatLocalDate(dateObj);
    try {
      const res = await axios.get(`${API}/api/tasks/date/${date}`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      setTasks(res.data);
      if (res.data.length > 0) {
        const completed = res.data.filter((task) => task.completed).length;
        const percent = Math.round((completed / res.data.length) * 100);
        setCompletionPercentage(percent);
      } else {
        setCompletionPercentage(0);
      }
    } catch (error) {
      console.error("Failed to fetch tasks", error);
    }
  };

  const checkIfNewUser = async () => {
    const token = localStorage.getItem("token");
    try {
      const res = await axios.get(`${API}/api/tasks/history`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      setIsFirstLogin(res.data.length === 0);
    } catch (err) {
      console.error("Failed to fetch task history", err);
    }
  };

  const markAsComplete = async (date, taskName) => {
    const token = localStorage.getItem("token");
    await axios.put(`${API}/api/tasks/complete/${date}/${taskName}`, {}, {
      headers: { Authorization: `Bearer ${token}` }
    });
    fetchTasksForDate(selectedDate);
    fetchWeeklyProgress();
    fetchUpcomingTasks();
  };

  const deleteTask = async (date, taskName) => {
    const token = localStorage.getItem("token");
    await axios.delete(`${API}/api/tasks/delete/${date}/${taskName}`, {
      headers: { Authorization: `Bearer ${token}` }
    });
    fetchTasksForDate(selectedDate);
    fetchWeeklyProgress();
    fetchUpcomingTasks();
  };

  const handleAddTask = async () => {
    const token = localStorage.getItem("token");
    if (!newTaskDate || !newTaskDesc) return alert("Fill all fields!");
    const today = formatLocalDate(new Date());
    if (newTaskDate < today) return alert("Cannot add tasks for past dates!");

    const newTask = {
      key: { taskDate: newTaskDate, taskName: newTaskDesc },
      completed: false
    };

    try {
      await axios.post(`${API}/api/tasks/create`, newTask, {
        headers: { Authorization: `Bearer ${token}` }
      });
      setNewTaskDate(""); setNewTaskDesc("");
      if (newTaskDate === formatLocalDate(selectedDate)) {
        fetchTasksForDate(selectedDate);
      }
      fetchWeeklyProgress();
      fetchUpcomingTasks();
    } catch (error) {
      alert("Failed to add task.");
    }
  };

  const handleUpdateTask = async (task) => {
    const token = localStorage.getItem("token");
    try {
      await axios.put(`${API}/api/tasks/update`, {
        key: {
          taskDate: task.key.taskDate,
          oldTaskName: task.key.taskName,
          taskName: editDescription
        },
        completed: task.completed
      }, {
        headers: { Authorization: `Bearer ${token}` }
      });
      setEditIndex(null);
      fetchTasksForDate(selectedDate);
      fetchWeeklyProgress();
      fetchUpcomingTasks();
    } catch (error) {
      alert("Failed to update task.");
    }
  };

  const fetchWeeklyProgress = async () => {
    const token = localStorage.getItem("token");
    try {
      const res = await axios.get(`${API}/api/tasks/history`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      const tasks = res.data;

      const getWeekRange = (dateStr) => {
        const date = new Date(dateStr);
        const day = date.getDay();
        const diffToMonday = day === 0 ? -6 : 1 - day;
        const monday = new Date(date); monday.setDate(date.getDate() + diffToMonday);
        const sunday = new Date(monday); sunday.setDate(monday.getDate() + 6);
        const format = (d) => d.toLocaleDateString("en-US", { month: "short", day: "numeric" });
        return `${format(monday)} – ${format(sunday)}`;
      };

      const weekMap = {};
      tasks.forEach((t) => {
        const weekLabel = getWeekRange(t.key.taskDate);
        if (!weekMap[weekLabel]) weekMap[weekLabel] = [];
        weekMap[weekLabel].push(t);
      });

      const weeklyCompletion = Object.entries(weekMap).map(([label, tasks]) => {
        const completed = tasks.filter((t) => t.completed).length;
        return {
          week: label,
          completion: tasks.length === 0 ? 0 : Math.round((completed / tasks.length) * 100),
        };
      });

      const getWeekStartDate = (label) => new Date(label.split("–")[0] + ", 2025");
      setWeeklyData(
        weeklyCompletion.sort((a, b) => getWeekStartDate(a.week) - getWeekStartDate(b.week)).slice(-4)
      );
    } catch (err) {
      console.error("Failed to fetch weekly progress", err);
    }
  };

  const fetchUpcomingTasks = async () => {
    const token = localStorage.getItem("token");
    try {
      const res = await axios.get(`${API}/api/tasks/week`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setUpcomingTasks(res.data);
    } catch (error) {
      console.error("Failed to fetch upcoming tasks", error);
    }
  };

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token) return navigate("/login");
    const userData = decodeToken(token);
    setUsername(userData.sub);
    fetchQuote();
    checkIfNewUser();
    fetchTasksForDate(selectedDate);
    fetchWeeklyProgress();
    fetchUpcomingTasks();
  }, []);
  return (
    <div className="relative p-6 max-w-4xl mx-auto bg-white shadow-md rounded-lg mt-6 mb-10 border">
      <button
        onClick={() => {
          localStorage.removeItem("token");
          navigate("/login");
        }}
        className="absolute top-4 right-4 bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 shadow"
      >
        🚪 Logout
      </button>

      <h1 className="text-3xl font-bold text-gray-800 mb-1">
        {isFirstLogin ? `Welcome, ${username} 👋` : `Welcome back, ${username} 👋`}
      </h1>
      <p className="text-gray-500 italic mb-4">"{quote}"</p>

      <div className="mb-6">
        <button
          className="text-blue-600 hover:underline"
          onClick={() => setShowCalendar(!showCalendar)}
        >
          📅 {showCalendar ? "Hide Calendar" : "Show Calendar"}
        </button>
        {showCalendar && (
          <div className="mt-3 border rounded p-2 bg-gray-100 inline-block">
            <Calendar
              onChange={(date) => {
                setSelectedDate(date);
                fetchTasksForDate(date);
              }}
              value={selectedDate}
            />
          </div>
        )}
      </div>

      <div>
        <h2 className="text-xl font-semibold text-gray-800 mb-2">➕ Add New Task</h2>
        <div className="flex flex-col sm:flex-row gap-3">
          <input
            type="date"
            value={newTaskDate}
            onChange={(e) => setNewTaskDate(e.target.value)}
            min={formatLocalDate(new Date())}
            className="border p-2 rounded w-full sm:w-auto"
          />
          <input
            type="text"
            placeholder="Task description"
            value={newTaskDesc}
            onChange={(e) => setNewTaskDesc(e.target.value)}
            className="border p-2 rounded w-full sm:flex-grow"
          />
          <button
            onClick={handleAddTask}
            className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
          >
            Add
          </button>
        </div>
      </div>

      <h2 className="mt-6 text-xl font-semibold text-gray-800">
        🗓️ Tasks for {formatLocalDate(selectedDate)}
      </h2>

      <div className="my-4">
        <p className="mb-1 font-medium text-gray-700">
          📈 Completion: {completionPercentage}% ({tasks.filter(t => t.completed).length} of {tasks.length})
        </p>
        <div className="w-full bg-gray-200 rounded-full h-4">
          <div
            className="bg-green-500 h-4 rounded-full transition-all duration-300"
            style={{ width: `${completionPercentage}%` }}
          ></div>
        </div>
      </div>

      <table className="w-full mt-4 border text-sm">
        <thead>
          <tr className="bg-gray-100 text-gray-700">
            <th className="p-2 text-left">Task</th>
            <th className="text-left">Status</th>
            <th className="text-left">Actions</th>
          </tr>
        </thead>
        <tbody>
          {tasks.length === 0 ? (
            <tr><td colSpan="3" className="text-center py-3 text-gray-500">No tasks for this date</td></tr>
          ) : (
            tasks.map((task, index) => (
              <tr key={task.key.taskDate + task.key.taskName} className="border-t">
                <td className="p-2">
                  {editIndex === index ? (
                    <input
                      className="border px-2 py-1 rounded"
                      value={editDescription}
                      onChange={(e) => setEditDescription(e.target.value)}
                    />
                  ) : (
                    task.key.taskName
                  )}
                </td>
                <td>{task.completed ? "✅ Done" : "⏳ Pending"}</td>
                <td className="space-x-3">
                  {editIndex === index ? (
                    <>
                      <button onClick={() => handleUpdateTask(task)} className="text-green-600 hover:underline">💾 Save</button>
                      <button onClick={() => setEditIndex(null)} className="text-gray-600 hover:underline">❌ Cancel</button>
                    </>
                  ) : (
                    <>
                    {!task.completed && (
                      <button onClick={() => markAsComplete(task.key.taskDate, task.key.taskName)} className="text-green-600 hover:underline">✅</button>)}
                      {!task.completed && (
  <button
    onClick={() => {
      setEditIndex(index);
      setEditDescription(task.key.taskName);
    }}
    className="text-blue-600 hover:underline"
  >
    ✏️
  </button>
)}
{!task.completed && (
                      <button onClick={() => deleteTask(task.key.taskDate, task.key.taskName)} className="text-red-600 hover:underline">❌</button>)}
                    </>
                  )}
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>

      {weeklyData.length > 0 && <WeeklyProgressChart data={weeklyData} />}

      <div className="mt-6">
        <button
          onClick={() => setShowUpcoming(!showUpcoming)}
          className="text-blue-600 hover:underline mb-2"
        >
          📅 {showUpcoming ? "Hide" : "Show"} Upcoming Week Tasks
        </button>
        {showUpcoming && (
          <div className="border rounded p-4 bg-gray-50">
            <h2 className="text-lg font-semibold mb-3">🗓️ Tasks for Upcoming 7 Days</h2>
            {upcomingTasks.length === 0 ? (
              <p className="text-gray-500">No tasks scheduled for next 7 days.</p>
            ) : (
              <ul className="list-disc pl-5 space-y-1">
                {upcomingTasks.map((task, index) => (
                  <li key={index}>
                    <strong>{task.key.taskDate}:</strong> {task.key.taskName}{" "}
                    {task.completed ? "✅" : "⏳"}
                  </li>
                ))}
              </ul>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default Dashboard;
