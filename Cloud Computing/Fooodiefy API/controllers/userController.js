const admin = require("firebase-admin");

exports.getProfile = async (req, res) => {
  const userId = req.userId;

  try {
    const userDoc = await admin.firestore().collection("Users").doc(userId).get();

    if (!userDoc.exists) return res.status(404).send({ message: "User not found" });

    res.status(200).send(userDoc.data());
  } catch (err) {
    res.status(500).send({ message: "Internal Server Error", error: err.message });
  }
};

exports.updateProfile = async (req, res) => {
  const userId = req.userId;
  const updates = req.body;

  try {
    const userRef = admin.firestore().collection("Users").doc(userId);
    await userRef.update({
      ...updates,
      updatedAt: admin.firestore.Timestamp.now(),
    });

    res.status(200).send({ message: "Profile updated successfully" });
  } catch (err) {
    res.status(500).send({ message: "Internal Server Error", error: err.message });
  }
};
