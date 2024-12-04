const express = require('express');
const jwt = require('jsonwebtoken');
const db = require('../server');

const router = express.Router();

// Middleware untuk memverifikasi token JWT
const verifyToken = (req, res, next) => {
  const authHeader = req.headers.authorization;
  if (!authHeader) return res.status(401).json({ error: 'Authorization header missing' });

  const token = authHeader.split(' ')[1];
  jwt.verify(token, process.env.JWT_SECRET, (err, decoded) => {
    if (err) return res.status(403).json({ error: 'Invalid or expired token' });
    req.uid = decoded.uid;
    next();
  });
};

// GET /user/profile
router.get('/profile', verifyToken, async (req, res) => {
  try {
    const userDoc = await db.collection('users').doc(req.uid).get();
    if (!userDoc.exists) return res.status(404).json({ error: 'User profile not found' });

    res.status(200).json(userDoc.data());
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

// PUT /user/profile
router.put('/profile', verifyToken, async (req, res) => {
  const updates = req.body;
  try {
    await db.collection('users').doc(req.uid).set(updates, { merge: true });
    res.status(200).json({ message: 'Profile updated successfully' });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

module.exports = router;
