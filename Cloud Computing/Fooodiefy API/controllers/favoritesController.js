const admin = require("firebase-admin");
const db = admin.firestore();

// Menambahkan resep ke daftar favorit
const addFavorite = async (req, res) => {
  const userId = req.user.uid; // Mendapatkan ID pengguna dari token
  const recipeId = req.body.recipe_id;

  if (!recipeId) {
    return res.status(400).json({ error: "recipe_id is required" });
  }

  try {
    // Menambahkan resep ke koleksi favorites
    const favoriteRef = db.collection("favorites").doc();
    await favoriteRef.set({
      userId: userId,
      recipeId: recipeId,
      createdAt: admin.firestore.FieldValue.serverTimestamp(), // Timestamp saat resep ditambahkan
    });

    return res.status(201).json({ message: "Recipe added to favorites" });
  } catch (error) {
    return res.status(500).json({ error: error.message });
  }
};

// Menghapus resep dari daftar favorit
const removeFavorite = async (req, res) => {
  const userId = req.user.uid; // Mendapatkan ID pengguna dari token
  const recipeId = req.body.recipe_id;

  if (!recipeId) {
    return res.status(400).json({ error: "recipe_id is required" });
  }

  try {
    // Menghapus resep dari koleksi favorites berdasarkan userId dan recipeId
    const favoritesRef = db.collection("favorites");
    const snapshot = await favoritesRef
      .where("userId", "==", userId)
      .where("recipeId", "==", recipeId)
      .get();

    if (snapshot.empty) {
      return res.status(404).json({ error: "Favorite not found" });
    }

    // Menghapus setiap dokumen yang ditemukan
    const batch = db.batch();
    snapshot.forEach(doc => {
      batch.delete(doc.ref);
    });
    await batch.commit();

    return res.status(200).json({ message: "Recipe removed from favorites" });
  } catch (error) {
    return res.status(500).json({ error: error.message });
  }
};

// Mengambil semua resep favorit
const getFavorites = async (req, res) => {
  const userId = req.user.uid; // Mendapatkan ID pengguna dari token

  try {
    const favoritesRef = db.collection("favorites").where("userId", "==", userId);
    const snapshot = await favoritesRef.get();

    const favorites = [];
    snapshot.forEach(doc => {
      favorites.push({ id: doc.id, ...doc.data() }); // Include the document ID
    });

    return res.status(200).json({ favorites });
  } catch (error) {
    return res.status(500).json({ error: error.message });
  }
};

module.exports = { addFavorite, removeFavorite, getFavorites };
