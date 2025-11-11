require('dotenv').config();

module.exports = {
  PORT: process.env.PORT || 3000,
  SPRING_BASE: process.env.SPRING_BASE || 'http://SPRINGBOOT_HOST:8080/api',
};