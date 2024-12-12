const admin = require("firebase-admin");
const jwt = require("jsonwebtoken");
const bcrypt = require("bcryptjs");

// Login menggunakan Google
exports.googleLogin = async (req, res) => {
  const { idToken } = req.body;

  try {
    const decodedToken = await admin.auth().verifyIdToken(idToken);
    const { email, name, picture, uid } = decodedToken;

    const usersRef = admin.firestore().collection("Users");
    let userDoc = await usersRef.where("email", "==", email).limit(1).get();

    if (userDoc.empty) {
      // Daftarkan pengguna baru jika belum ada
      await usersRef.doc(uid).set({
        fullName: name,
        email,
        profilePic: picture,
        createdAt: admin.firestore.Timestamp.now(),
      });
      userDoc = await usersRef.doc(uid).get();
    } else {
      userDoc = userDoc.docs[0];
    }

    // Generate JWT Token
    const token = jwt.sign({ userId: userDoc.id, email }, process.env.JWT_SECRET, { expiresIn: "7d" });

    res.status(200).send({ token, user: userDoc.data() });
  } catch (err) {
    res.status(500).send({ message: "Google login failed", error: err.message });
  }
};

// Login menggunakan Facebook
exports.facebookLogin = async (req, res) => {
  const { accessToken } = req.body;

  try {
    const fbUrl = `https://graph.facebook.com/me?fields=id,name,email,picture&access_token=${accessToken}`;
    const fbResponse = await fetch(fbUrl);
    const fbData = await fbResponse.json();

    const { email, name, picture } = fbData;

    const usersRef = admin.firestore().collection("Users");
    let userDoc = await usersRef.where("email", "==", email).limit(1).get();

    if (userDoc.empty) {
      // Daftarkan pengguna baru jika belum ada
      const newUser = {
        fullName: name,
        email,
        profilePic: picture.data.url,
        createdAt: admin.firestore.Timestamp.now(),
      };

      const newUserRef = usersRef.doc();
      await newUserRef.set(newUser);
      userDoc = await usersRef.doc(newUserRef.id).get();
    } else {
      userDoc = userDoc.docs[0];
    }

    // Generate JWT Token
    const token = jwt.sign({ userId: userDoc.id, email }, process.env.JWT_SECRET, { expiresIn: "7d" });

    res.status(200).send({ token, user: userDoc.data() });
  } catch (err) {
    res.status(500).send({ message: "Facebook login failed", error: err.message });
  }
};

exports.login = async (req, res) => {
    const { email, password, rememberMe } = req.body;
  
    try {
      const usersRef = admin.firestore().collection("Users");
      const userDoc = await usersRef.where("email", "==", email).limit(1).get();
  
      if (userDoc.empty) return res.status(400).send({ message: "User not found" });
  
      const userData = userDoc.docs[0].data();
  
      const isPasswordValid = await bcrypt.compare(password, userData.passwordHash);
      if (!isPasswordValid) return res.status(401).send({ message: "Invalid credentials" });
  
      const expiresIn = rememberMe ? "30d" : "1h";
      const token = jwt.sign({ email: userData.email, userId: userDoc.docs[0].id }, process.env.JWT_SECRET, { expiresIn });
  
      res.status(200).send({ token, user: userData });
    } catch (err) {
      res.status(500).send({ message: "Internal Server Error", error: err.message });
    }
  };
  // Sign Up
exports.signup = async (req, res) => {
    const { fullName, phoneNumber, email, password, gender, age, weight, height, activityLevel } = req.body;
  
    try {
      const usersRef = admin.firestore().collection("Users");
      // Check if the user already exists
      const existingUser = await usersRef.where("email", "==", email).limit(1).get();
  
      if (!existingUser.empty) {
        return res.status(400).send({ message: "Email already in use" });
      }
  
      // Hash the password
      const passwordHash = await bcrypt.hash(password, 10);
  
      // Create a new user document
      const newUser = {
        fullName,
        phoneNumber,
        email,
        passwordHash,
        gender,
        age,
        weight,
        height,
        activityLevel,
        createdAt: admin.firestore.Timestamp.now(),
        updatedAt: admin.firestore.Timestamp.now(),
      };
  
      const newUserRef = usersRef.doc();
      await newUserRef.set(newUser);
  
      // Generate JWT Token
      const token = jwt.sign({ userId: newUserRef.id, email }, process.env.JWT_SECRET, { expiresIn: "7d" });
  
      res.status(201).send({ token, user: newUser });
    } catch (err) {
      res.status(500).send({ message: "Sign up failed", error: err.message });
    }
  };

  
