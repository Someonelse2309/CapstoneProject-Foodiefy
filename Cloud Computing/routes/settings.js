const express = require('express');
const admin = require('firebase-admin');
const router = express.Router();
const { verifyToken } = require('../middleware/auth'); // Middleware untuk verifikasi token JWT

// GET /settings
router.get('/', verifyToken, async (req, res) => {
  const uid = req.user.uid; // Ambil UID dari pengguna yang terverifikasi

  try {
    const userSettingsSnapshot = await admin.firestore().collection('settings').doc(uid).get();
    if (!userSettingsSnapshot.exists) {
      return res.status(404).json({ error: 'Settings not found' });
    }

    const settingsData = userSettingsSnapshot.data();
    res.status(200).json(settingsData);
  } catch (error) {
    console.error('Error fetching settings:', error);
    res.status(500).json({ error: 'Error fetching settings' });
  }
});

// PUT /settings
router.put('/', verifyToken, async (req, res) => {
  const uid = req.user.uid; // Ambil UID dari pengguna yang terverifikasi
  const settingsUpdate = req.body; // Data pengaturan yang ingin diubah

  try {
    await admin.firestore().collection('settings').doc(uid).set(settingsUpdate, { merge: true });
    res.status(200).json({ message: 'Settings updated successfully' });
  } catch (error) {
    console.error('Error updating settings:', error);
    res.status(500).json({ error: 'Error updating settings' });
  }
});

module.exports = router;
