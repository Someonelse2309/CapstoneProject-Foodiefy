const admin = require("firebase-admin");
const db = admin.firestore();

// Mengambil detail resep berdasarkan recipeId
const getRecipeById = async (req, res) => {
  const recipeId = req.params.recipeId; // Mengambil recipeId dari parameter URL

  try {
    const recipeDoc = await db.collection("recipes").doc(recipeId).get();

    if (!recipeDoc.exists) {
      return res.status(404).json({ error: "Recipe not found" });
    }

    const recipeData = recipeDoc.data();
    return res.status(200).json({ recipeId: recipeDoc.id, ...recipeData });
  } catch (error) {
    return res.status(500).json({ error: error.message });
  }
};

module.exports = { getRecipeById };
