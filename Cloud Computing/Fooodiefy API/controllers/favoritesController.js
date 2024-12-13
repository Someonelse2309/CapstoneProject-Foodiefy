const admin = require("firebase-admin");
const db = admin.firestore();

// Menambahkan resep ke daftar favorit
const addFavorite = async (req, res) => {
  const userId = req.uid; // Menggunakan req.uid dari middleware
  const recipeId = req.body.recipe_id;

  if (!recipeId) {
    return res.status(400).json({ error: "recipe_id is required" });
  }

  try {
    // Validasi apakah recipeId ada di koleksi 'recipes'
    const recipeDoc = await db.collection("recipes").doc(recipeId).get();
    if (!recipeDoc.exists) {
      return res.status(404).json({ error: "Recipe not found" });
    }

    // Menambahkan resep ke koleksi favorites
    const favoriteRef = db.collection("favorites").doc();
    await favoriteRef.set({
      userId: userId,
      recipeId: recipeId,
      createdAt: admin.firestore.FieldValue.serverTimestamp(),
    });

    return res.status(201).json({ message: "Recipe added to favorites" });
  } catch (error) {
    return res.status(500).json({ error: error.message });
  }
};

// Menghapus resep dari daftar favorit
const removeFavorite = async (req, res) => {
  const userId = req.uid; // Menggunakan req.uid dari middleware
  const recipeId = req.body.recipe_id;

  if (!recipeId) {
    return res.status(400).json({ error: "recipe_id is required" });
  }

  try {
    const favoritesRef = db.collection("favorites");
    const snapshot = await favoritesRef
      .where("userId", "==", userId)
      .where("recipeId", "==", recipeId)
      .get();

    if (snapshot.empty) {
      return res.status(404).json({ error: "Favorite not found" });
    }

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
  const userId = req.uid; // Menggunakan req.uid dari middleware

  try {
    const favoritesRef = db.collection("favorites").where("userId", "==", userId);
    const snapshot = await favoritesRef.get();

    const favorites = [];
    for (const doc of snapshot.docs) {
      const data = doc.data();
      const recipeDoc = await db.collection("recipes").doc(data.recipeId).get();
      if (recipeDoc.exists) {
        favorites.push({
          id: doc.id,
          ...data,
          recipeDetails: recipeDoc.data(), // Menyertakan detail resep
        });
      }
    }

    return res.status(200).json({ favorites });
  } catch (error) {
    return res.status(500).json({ error: error.message });
  }
};

module.exports = { addFavorite, removeFavorite, getFavorites };
