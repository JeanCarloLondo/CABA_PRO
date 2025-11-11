# Etapa de build
FROM node:20-alpine AS build

# Crear carpeta de trabajo
WORKDIR /app

# Copiar los archivos de dependencias
COPY ../package*.json ./

# Instalar dependencias
RUN npm install

# Copiar el resto del código
COPY .. .

# Exponer el puerto
EXPOSE 3000

# Comando de inicio
CMD ["node", "src/server.js"]