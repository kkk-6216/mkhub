import apiClient from '@/api/axiosInstance.js'

export const getFaculties = (verbose) => apiClient.get('/faculties?verbose='+verbose);

export const getFacultyById = (id) => apiClient.get(`/faculties/${id}`);

export const createFaculty = (data) => apiClient.post('/faculties', data);

export const updateFaculty = (id, data) => apiClient.put(`/faculties/${id}`, data);

export const deleteFaculty = (id) => apiClient.delete(`/faculties/${id}`);