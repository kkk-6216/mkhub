import apiClient from '@/api/axiosInstance.js'

export const getUsers = () => apiClient.get('users');
export const deleteUser = (id) => apiClient.delete(`users/${id}`);
export const getUser = (id) => apiClient.get(`users/${id}`);

export const createUser = (userData) => apiClient.post(`users`, userData);

export const updateUsername = (id, userData) => apiClient.patch(`users/${id}/username`, userData);
export const updateRole = (id, userData) => apiClient.patch(`users/${id}/role`, userData);
export const updateData = (id, userData) => apiClient.patch(`users/${id}/data`, userData);
export const updateAbout = (id, userData) => apiClient.patch(`users/${id}/about`, userData);
export const updateSecurityStatus = (id, userData) => apiClient.patch(`users/${id}/secure-status`, userData);
