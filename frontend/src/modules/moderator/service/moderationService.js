// import apiClient from '@/api/axiosInstance.js'

// export const getModerationContents = () => apiClient.get('/moderation/contents')

// export const approveContent = (id) => apiClient.post(`/moderation/contents/${id}/approve`)

// export const rejectContent = (id, reason) => apiClient.post(`/moderation/contents/${id}/reject`, { reason })


// src/api/moderation.js

export async function getModerationContents() {
    await new Promise(resolve => setTimeout(resolve, 500))
  
    return {
      data: [
        {
          id: 1,
          title: 'Тестовый текст 1',
          author: 'Иван Иванов',
          date: '2025-04-27',
          status: 'На проверке',
          type: 'text',
          content: '<p>Это <strong>пример</strong> текста для проверки.</p>',
        },
        {
          id: 2,
          title: 'Файл документа',
          author: 'Петр Петров',
          date: '2025-04-26',
          status: 'Одобрено',
          type: 'file',
          content: '<p>Файл: <a href="#">document.pdf</a></p>',
        },
        {
          id: 3,
          title: 'Тестирование знаний',
          author: 'Мария Смирнова',
          date: '2025-04-25',
          status: 'Отклонено',
          type: 'test',
          content: '<p>Тест с 10 вопросами.</p>',
        },
      ]
    }
  }
  
  export async function approveContent(id) {
    console.log(`✅ Одобрен контент с id=${id}`)
    await new Promise(resolve => setTimeout(resolve, 300))
  }
  
  export async function rejectContent(id, reason) {
    console.log(`❌ Отклонен контент с id=${id} по причине: ${reason}`)
    await new Promise(resolve => setTimeout(resolve, 300))
  }
  