const admin = require('firebase-admin');
const serviceAccount = require("../configuration/foodiefy-app-firebase-adminsdk-acmqn-a0d035ba6c.json");

// Initialize Firebase application
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
});

const db = admin.firestore();

module.exports = db;



// const admin = require('firebase-admin');
// const serviceAccount = require('./path/to/your/firebase-service-account-file.json');

// // Inisialisasi aplikasi Firebase
// admin.initializeApp({
//   credential: admin.credential.cert(serviceAccount),
// });

// const db = admin.firestore();

// module.exports = db;
