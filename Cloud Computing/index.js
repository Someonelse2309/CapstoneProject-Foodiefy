// // Import required modules
// const express = require('express');
// const bodyParser = require('body-parser');
// const mongoose = require('mongoose');
// const passport = require('passport');
// require('./config/passport');
// const dotenv = require('dotenv');

// // Load environment variables
// dotenv.config();

// // Import route handlers
// const authRoutes = require('./routes/auth');

// // Initialize the app
// const app = express();

// // Connect to MongoDB
// mongoose
//     .connect(process.env.MONGO_URI, { useNewUrlParser: true, useUnifiedTopology: true })
//     .then(() => console.log('MongoDB connected'))
//     .catch((err) => console.error('MongoDB connection error:', err));

// // Middleware
// app.use(bodyParser.json()); // Parse incoming JSON requests
// app.use(passport.initialize()); // Initialize Passport.js

// // Register routes
// app.use('/auth', authRoutes);

// // Default route (optional)
// app.get('/', (req, res) => {
//     res.send('Welcome to AI Recipe App API');
// });

// // Start the server
// const PORT = process.env.PORT || 5000;
// app.listen(PORT, () => console.log(`Server running on http://localhost:${PORT}`));
