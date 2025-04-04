import { ref } from 'vue';

export function useCommands() {
  const commands = [
    { 
      text: '/text', 
      type: 'text',
      label: 'Текст', 
      icon: 'M3 17h18M3 12h18M3 7h18'
    },
    { 
      text: '/image', 
      type: 'image',
      label: 'Изображение', 
      icon: 'M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z' 
    },
    { 
      text: '/file', 
      type: 'file',
      label: 'Файл', 
      icon: 'M15.172 7l-6.586 6.586a2 2 0 102.828 2.828l6.414-6.586a4 4 0 00-5.656-5.656l-6.415 6.585a6 6 0 108.486 8.486L20.5 13'
    }
  ];

  const getFilteredCommands = (query) => {
    if (!query) return commands;
    return commands.filter(cmd =>
      cmd.text.substring(1).toLowerCase().includes(query) ||
      cmd.label.toLowerCase().includes(query)
    );
  };

  return {
    commands,
    getFilteredCommands
  };
}