import apiClient from '@/api/axiosInstance.js'

export const getLessons = () => apiClient.get('/lessons');

export const createLesson = (data) => apiClient.post('/lessons', data);

export const updateLesson = (id, data) => apiClient.patch('/lessons/'+id, data);

export const deleteLesson = (id) => apiClient.delete('/lessons/'+id);

export const createLessonDetails = (id, data) => apiClient.post('/lessons/'+id+'/details', data);

export const getLessonDetails = (id) => apiClient.get(`/lessons/${id}/details`);