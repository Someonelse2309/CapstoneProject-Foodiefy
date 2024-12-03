const express = require('express');
const admin = require('firebase-admin');
const dotenv = require('dotenv');

// Load environment variables
dotenv.config();

// Firebase Admin SDK setup
const serviceAccount = require("../configuration/foodiefy-app-firebase-adminsdk-acmqn-a0d035ba6c.json");
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
});

const db = admin.firestore();

const app = express();
app.use(express.json());

// Import routes
const authRoutes = require('./routes/auth');
app.use('/auth', authRoutes);
const userRoutes = require('./routes/user');
app.use('/user', userRoutes);
const nutritionRoutes = require('./routes/nutrition');
app.use('/nutrition', nutritionRoutes);
const scanRoutes = require('./routes/scan');
app.use('/scan', scanRoutes);
const recipeRoutes = require('./routes/recipes');
app.use('/recipes', recipeRoutes);
const chatbotRoutes = require('./routes/chatbot');
app.use('/chatbot', chatbotRoutes);
const settingsRoutes = require('./routes/settings');
app.use('/settings', settingsRoutes);
const miscellaneousRoutes = require('./routes/miscellaneous');
app.use('/misc', miscellaneousRoutes);


// Start server
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});

module.exports = db;
