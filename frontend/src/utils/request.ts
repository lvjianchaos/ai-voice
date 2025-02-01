import axios from 'axios'

const requst = axios.create({
  // baseURL: import.meta.env.VITE_API_URL,
  timeout: 5000,
})

export default requst
