const express = require("express");
const bodyParser = require("body-parser");
const admin = require("firebase-admin");
const cors = require("cors");
const dotenv = require('dotenv');

// Load environment variables
dotenv.config();

// Firebase Admin SDK setup
const serviceAccount = require("./configuration/firebase-code.json");
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
});

const db = admin.firestore();

const app = express();
app.use(express.json());
app.use(cors());
app.use(bodyParser.json());

// Import routes
const authRoutes = require("./routes/authRoutes");
app.use("/auth", authRoutes);
const userRoutes = require("./routes/userRoutes");
app.use("/user", userRoutes);
const infoRoutes = require('./routes/infoRoutes');
app.use("/info", infoRoutes);
const nutritionRoutes = require('./routes/nutritionRoutes');
app.use("/nutrition", nutritionRoutes);

// Start server
const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});

module.exports = db;
