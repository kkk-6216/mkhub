import apiClient from '@/api/axiosInstance.js'

export const getCoursesByDepartmentId = (id) => apiClient.get(`/courses?departmentId=${id}`);

export const getCourses = () => apiClient.get('/courses');

export const createCourse = (data) => apiClient.post('/courses', data);

export const updateCourse = (id, data) => apiClient.patch('/courses/'+id, data);

export const deleteCourse = (id) => apiClient.delete('/courses/'+id);
