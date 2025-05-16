
import apiClient from "@/api/axiosInstance.js";

export const updateUserAbout = async (userAbout) => {
    try {
        console.log(userAbout);
        return await apiClient.patch(`users/about`, userAbout);
    } catch (error) {
        console.error("Error updating about:", error);
        throw error;
    }
};

export const getUserAbout = async () => {
    try {
        const response = await apiClient.get(`users/about`);
        return response.data;
    } catch (error) {
        console.error("Error fetching user about:", error);
        throw error;
    }
};

export const updateUserData = async (userData) => {
    try {
        return await apiClient.patch(`users/details`, userData);
    } catch (error) {
        console.error("Error updating details:", error);
        throw error;
    }
};

export const getUserData = async () => {
    try {
        const response = await apiClient.get(`users/details`);
        return response.data;
    } catch (error) {
        console.error("Error fetching user details:", error);
        throw error;
    }
};
