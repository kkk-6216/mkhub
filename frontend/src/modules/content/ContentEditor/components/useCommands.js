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
    },
    { 
      text: '/link', 
      type: 'link',
      label: 'Ссылка', 
      icon: 'M13.19 8.688a4.5 4.5 0 0 1 1.242 7.244l-4.5 4.5a4.5 4.5 0 0 1-6.364-6.364l1.757-1.757m13.35-.622 1.757-1.757a4.5 4.5 0 0 0-6.364-6.364l-4.5 4.5a4.5 4.5 0 0 0 1.242 7.244'
    },
    { 
      text: '/code', 
      type: 'code',
      label: 'Код', 
      icon: 'M17.25 6.75 22.5 12l-5.25 5.25m-10.5 0L1.5 12l5.25-5.25m7.5-3-4.5 16.5'
    },
    { 
      text: '/formula', 
      type: 'formula',
      label: 'Формулы', 
      icon: 'M4.745 3A23.933 23.933 0 0 0 3 12c0 3.183.62 6.22 1.745 9M19.5 3c.967 2.78 1.5 5.817 1.5 9s-.533 6.22-1.5 9M8.25 8.885l1.444-.89a.75.75 0 0 1 1.105.402l2.402 7.206a.75.75 0 0 0 1.104.401l1.445-.889m-8.25.75.213.09a1.687 1.687 0 0 0 2.062-.617l4.45-6.676a1.688 1.688 0 0 1 2.062-.618l.213.09'
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



