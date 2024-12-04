const express = require('express');
const admin = require('firebase-admin');
const router = express.Router();
const jwt = require('jsonwebtoken');

// Middleware untuk memverifikasi token JWT
const verifyToken = (req, res, next) => {
  const authHeader = req.headers.authorization;
  if (!authHeader) return res.status(401).json({ error: 'Authorization header missing' });

  const token = authHeader.split(' ')[1];
  jwt.verify(token, process.env.JWT_SECRET, (err, decoded) => {
    if (err) return res.status(403).json({ error: 'Invalid or expired token' });
    req.uid = decoded.uid; // Simpan uid pengguna untuk digunakan dalam permintaan berikutnya
    next();
  });
};

// GET /recipes
router.get('/', async (req, res) => {
  const { ingredients } = req.query; // Ambil bahan dari query string
  const ingredientList = ingredients.split(',').map(ing => ing.trim()); // Pisahkan dan trim setiap bahan

  try {
    const recipesSnapshot = await admin.firestore().collection('recipes').get();
    const recipes = [];

    recipesSnapshot.forEach(doc => {
      recipes.push({ id: doc.id, ...doc.data() });
    });

    // Filter resep berdasarkan bahan
    const matchedRecipes = recipes.filter(recipe =>
      recipe.ingredients.some(ingredient => ingredientList.includes(ingredient))
    );

    res.status(200).json({ recipes: matchedRecipes });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

// GET /recipes/:id
router.get('/:id', async (req, res) => {
  const { id } = req.params;

  try {
    const doc = await admin.firestore().collection('recipes').doc(id).get();
    if (!doc.exists) {
      return res.status(404).json({ error: 'Recipe not found' });
    }

    res.status(200).json({ id: doc.id, ...doc.data() });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

// POST /recipes/favorites
router.post('/favorites', verifyToken, async (req, res) => {
  const { recipeId } = req.body;

  try {
    const favoriteRef = admin.firestore().collection('favorites').doc(req.uid);
    await favoriteRef.set({ recipes: admin.firestore.FieldValue.arrayUnion(recipeId) }, { merge: true });

    res.status(200).json({ message: 'Recipe added to favorites' });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

// GET /recipes/favorites
router.get('/favorites', verifyToken, async (req, res) => {
  try {
    const favoriteRef = admin.firestore().collection('favorites').doc(req.uid);
    const doc = await favoriteRef.get();

    if (!doc.exists || !doc.data().recipes) {
      return res.status(200).json({ favorites: [] });
    }

    const recipeIds = doc.data().recipes;
    const recipesSnapshot = await admin.firestore().collection('recipes').where(admin.firestore.FieldPath.documentId(), 'in', recipeIds).get();

    const favorites = [];
    recipesSnapshot.forEach(recipeDoc => {
      favorites.push({ id: recipeDoc.id, ...recipeDoc.data() });
    });

    res.status(200).json({ favorites });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

// POST /recipes/match
router.post('/match', async (req, res) => {
  const { scannedIngredients, recipeId } = req.body;

  try {
    const recipeDoc = await admin.firestore().collection('recipes').doc(recipeId).get();
    if (!recipeDoc.exists) {
      return res.status(404).json({ error: 'Recipe not found' });
    }

    const recipeData = recipeDoc.data();
    const recipeIngredients = recipeData.ingredients;
    
    const totalIngredients = recipeIngredients.length;
    const matchedIngredients = scannedIngredients.filter(ingredient => recipeIngredients.includes(ingredient));
    const matchedCount = matchedIngredients.length;

    const matchPercentage = ((matchedCount / totalIngredients) * 100).toFixed(2); // Persentase kecocokan

    res.status(200).json({ 
      recipeId, 
      matchPercentage, 
      matchedIngredients, 
      totalIngredients 
    });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

module.exports = router;
