const express = require('express');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const admin = require('firebase-admin');

const router = express.Router();
const admin = require('firebase-admin');

// Helper function to generate JWT
const generateToken = (uid) => {
  return jwt.sign({ uid }, process.env.JWT_SECRET, { expiresIn: '1h' });
};

// Signup
router.post('/signup', async (req, res) => {
  const { nama, nomorHP, email, password } = req.body;
  try {
    const hashedPassword = await bcrypt.hash(password, 10);
    const userRecord = await admin.auth().createUser({
      displayName: nama,
      email,
      phoneNumber: nomorHP,
      password: hashedPassword,
    });

    res.status(201).json({ message: 'User registered successfully', userId: userRecord.uid });
  } catch (error) {
    res.status(400).json({ error: error.message });
  }
});

// Login
router.post('/login', async (req, res) => {
  const { email, password } = req.body;
  try {
    const userRecord = await admin.auth().getUserByEmail(email);
    const user = await admin.auth().getUser(userRecord.uid);
    
    const passwordValid = await bcrypt.compare(password, user.passwordHash);
    if (!passwordValid) throw new Error('Invalid credentials');

    const token = generateToken(user.uid);
    res.status(200).json({ token, user: { uid: user.uid, email: user.email, name: user.displayName } });
  } catch (error) {
    res.status(401).json({ error: error.message });
  }
});

// Google Login
router.post('/google-login', async (req, res) => {
  const { token } = req.body;
  try {
    const decodedToken = await admin.auth().verifyIdToken(token);
    const firebaseToken = generateToken(decodedToken.uid);
    res.status(200).json({ token: firebaseToken, user: decodedToken });
  } catch (error) {
    res.status(401).json({ error: 'Invalid Google token' });
  }
});

// Facebook Login
router.post('/facebook-login', async (req, res) => {
  const { token } = req.body;
  try {
    const decodedToken = await admin.auth().verifyIdToken(token);
    const firebaseToken = generateToken(decodedToken.uid);
    res.status(200).json({ token: firebaseToken, user: decodedToken });
  } catch (error) {
    res.status(401).json({ error: 'Invalid Facebook token' });
  }
});

// Forgot Password
router.post('/forgot-password', async (req, res) => {
  const { email } = req.body;
  try {
    const link = await admin.auth().generatePasswordResetLink(email);
    res.status(200).json({ message: 'Password reset email sent', link });
  } catch (error) {
    res.status(400).json({ error: error.message });
  }
});

// Reset Password
router.post('/reset-password', async (req, res) => {
  const { token, newPassword } = req.body;
  try {
    const decodedToken = await admin.auth().verifyIdToken(token);
    await admin.auth().updateUser(decodedToken.uid, { password: newPassword });
    res.status(200).json({ message: 'Password updated successfully' });
  } catch (error) {
    res.status(400).json({ error: error.message });
  }
});



exports.verifyToken = async (req, res, next) => {
  const token = req.headers.authorization?.split('Bearer ')[1];

  if (!token) {
    return res.status(403).json({ error: 'Unauthorized' });
  }

  try {
    const decodedToken = await admin.auth().verifyIdToken(token);
    req.user = decodedToken; // Simpan data pengguna yang terverifikasi
    next();
  } catch (error) {
    console.error('Error verifying token:', error);
    res.status(403).json({ error: 'Unauthorized' });
  }
};


module.exports = router;



// const express = require('express');
// const bcrypt = require('bcryptjs');
// const jwt = require('jsonwebtoken');
// const passport = require('passport');
// const User = require('../models/user');

// const router = express.Router();

// // Secret for JWT
// const JWT_SECRET = process.env.JWT_SECRET || 'your_jwt_secret';

// // Register a new user
// router.post('/register', async (req, res) => {
//     const { name, email, password, phone } = req.body;

//     try {
//         let user = await User.findOne({ email });
//         if (user) return res.status(400).json({ msg: 'User already exists' });

//         const hashedPassword = await bcrypt.hash(password, 10);
//         user = new User({ name, email, password: hashedPassword, phone });
//         await user.save();

//         const token = jwt.sign({ user: { id: user.id } }, JWT_SECRET, { expiresIn: '1h' });
//         res.json({ token });
//     } catch (err) {
//         res.status(500).json({ msg: 'Server error' });
//     }
// });

// // Login with email and password
// router.post('/login', async (req, res) => {
//     const { email, password } = req.body;

//     try {
//         const user = await User.findOne({ email });
//         if (!user) return res.status(400).json({ msg: 'Invalid credentials' });

//         const isMatch = await bcrypt.compare(password, user.password);
//         if (!isMatch) return res.status(400).json({ msg: 'Invalid credentials' });

//         const token = jwt.sign({ user: { id: user.id } }, JWT_SECRET, { expiresIn: '1h' });
//         res.json({ token });
//     } catch (err) {
//         res.status(500).json({ msg: 'Server error' });
//     }
// });

// // Google OAuth Login
// router.get('/google', passport.authenticate('google', { scope: ['profile', 'email'] }));

// router.get(
//     '/google/callback',
//     passport.authenticate('google', { session: false }),
//     (req, res) => {
//         const token = jwt.sign({ user: { id: req.user.id } }, JWT_SECRET, { expiresIn: '1h' });
//         res.json({ token });
//     }
// );

// // Facebook OAuth Login
// router.get('/facebook', passport.authenticate('facebook', { scope: ['email'] }));

// router.get(
//     '/facebook/callback',
//     passport.authenticate('facebook', { session: false }),
//     (req, res) => {
//         const token = jwt.sign({ user: { id: req.user.id } }, JWT_SECRET, { expiresIn: '1h' });
//         res.json({ token });
//     }
// );

// module.exports = router;
