
const getDynamicApiUrl = (): string => {
  if (typeof window !== 'undefined' && window.location) {
    return window.location.hostname;
  }
  // Nota: Se o seu backend estiver no Docker na mesma rede, mude para: 'http://nome-do-container-backend:8080'
  return 'localhost'; 
};

const currentHost = getDynamicApiUrl();

export const environment = {
  production: false,
  apiUrl: `http://${currentHost}`
};
