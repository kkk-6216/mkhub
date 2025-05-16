import apiClient from '@/api/axiosInstance.js'

export const getDepartmentsByFacultyId = (id) => apiClient.get(`/departments?facultyId=${id}`);

export const getDepartments = (verbose) => apiClient.get('/departments?verbose=' + verbose);

export const getDepartmentById = (id) => apiClient.get(`/departments/${id}`);

export const createDepartment = (data) => apiClient.post('/departments', data);

export const updateDepartment = (id, data) => apiClient.put(`/departments/${id}`, data);

export const deleteDepartment = (id) => apiClient.delete(`/departments/${id}`);