import apiClient from "@/api/axiosInstance.js";

const AdminService = {
    getEurekaOverview() {
        return apiClient.get('/admin/eureka/services');
    }
}

export default AdminService
