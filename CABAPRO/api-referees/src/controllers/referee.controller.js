const spring = require('../services/spring.client');
const photos = require('../data/refereePhotos.json');

exports.dashboard = async (req, res) => {
  try {
    const id = parseInt(req.params.id);
    const data = await spring.getDashboard(id);

    // search for photo
    const match = photos.find(p => p.id === id);
    if (match) {
      data.referee.photoUrl = match.photoUrl;
    }

    res.json(data);
  } catch (err) {
    console.error(err.message);
    res.status(500).json({ error: 'Error fetching dashboard' });
  }
};

exports.assignments = async (req, res) => {
  try {
    const data = await spring.getAssignments(req.params.id);
    res.json(data);
  } catch (err) {
    res.status(500).json({ error: 'Error fetching assignments' });
  }
};

exports.accept = async (req, res) => {
  try {
    const result = await spring.acceptAssignment(req.params.refId, req.params.aId);
    res.json(result);
  } catch (err) {
    res.status(500).json({ error: 'Error accepting assignment' });
  }
};

exports.reject = async (req, res) => {
  try {
    const result = await spring.rejectAssignment(req.params.refId, req.params.aId);
    res.json(result);
  } catch (err) {
    res.status(500).json({ error: 'Error rejecting assignment' });
  }
};

exports.notifications = async (req, res) => {
  try {
    const result = await spring.getNotifications(req.params.id);
    res.json(result);
  } catch (err) {
    res.status(500).json({ error: 'Error fetching notifications' });
  }
};