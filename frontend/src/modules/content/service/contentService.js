import apiClient from "@/api/axiosInstance.js";

/**
 * Сохраняет черновик урока.
 *
 * @param {Object} payload - Данные для отправки.
 * @param {number} payload.lessonId - ID урока (в path и в параметре).
 * @param {number} payload.authorId - ID пользователя.
 * @param {string} payload.blocksJson - JSON строка с блоками.
 * @param {Object} payload.files - Объект с файлами (ключ — имя поля, значение — File).
 *
 * @returns {Promise<void>} Ответ от сервера.
 */
export const saveContentDraft = async ({ lessonId, authorId, blocksJson, files }) => {
    try {
        const formData = new FormData();
        formData.append("userId", String(authorId));
        formData.append("blocks", blocksJson);

        // Добавляем файлы под ключом "files"
        for (const [key, file] of Object.entries(files)) {
            console.log(`${key}:`, file);
            if (file instanceof File && file.size > 0) {
                formData.append("files", file, key); // Используем "files" как имя поля, key как имя файла
            } else {
                console.warn(`Skipping invalid file for key: ${key}`);
            }
        }

        // Логируем содержимое FormData
        for (const [key, value] of formData.entries()) {
            console.log(`${key}:`, value);
        }

        const response = await apiClient.post(`/lessons/${lessonId}/offer`, formData);
        return response.data;
    } catch (error) {
        console.error("Ошибка при сохранении черновика:", error);
        throw new Error(error.response?.data?.message || "Failed to save draft");
    }
};