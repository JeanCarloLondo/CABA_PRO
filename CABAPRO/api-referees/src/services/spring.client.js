const axios = require('axios');
const { SPRING_BASE } = require('../config');
const client = axios.create({ baseURL: SPRING_BASE });

module.exports = {
  getDashboard: async (id) => (await client.get(`/referees/${id}/dashboard`)).data,
  getAssignments: async (id) => (await client.get(`/referees/${id}/assignments`)).data,
  acceptAssignment: async (refId, aId) => (await client.put(`/referees/${refId}/assignments/${aId}/accept`)).data,
  rejectAssignment: async (refId, aId) => (await client.put(`/referees/${refId}/assignments/${aId}/reject`)).data,
  getNotifications: async (id) => (await client.get(`/referees/${id}/notifications`)).data,
  markAllNotificationsAsRead: async (id) => (await client.put(`/referees/${id}/notifications/read-all`)).data,
  updateProfile: async (id, data) => (await client.put(`/referees/${id}/profile`, data)).data,
};