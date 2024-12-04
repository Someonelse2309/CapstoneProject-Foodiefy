import admin from 'firebase-admin';
import dotenv from 'dotenv';

// Load environment variables
dotenv.config();

// Firebase Admin SDK setup
const serviceAccount = require('./configuration/foodiefy-app-firebase-adminsdk-acmqn-a0d035ba6c.json');
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
});

const db = admin.firestore();

const createUser = async () => {
  const userRef = db.collection('Users').doc(); // Create a new document reference
  await userRef.set({
    fullName: "John Doe",
    phoneNumber: "+621234567890",
    email: "john.doe@example.com",
    passwordHash: "$2b$10$...",
    gender: "male",
    age: 28,
    weight: 70.5,
    height: 175.0,
    activityLevel: "active",
    createdAt: admin.firestore.Timestamp.fromDate(new Date()),
    updatedAt: admin.firestore.Timestamp.fromDate(new Date()),
  });
  console.log(`User created with ID: ${userRef.id}`);
};

