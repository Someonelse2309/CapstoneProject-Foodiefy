const express = require('express');
const multer = require('multer');
const vision = require('@google-cloud/vision');
const db = require('../server');
const router = express.Router();

// Google Cloud Vision client
const client = new vision.ImageAnnotatorClient({
  keyFilename: './google-cloud-credentials.json',
});

// Middleware untuk file upload
const upload = multer({ storage: multer.memoryStorage() });

// Helper: Estimasi nutrisi berdasarkan nama makanan (Mockup)
const estimateNutrition = (foodName) => {
  const data = {
    "Nasi Goreng": { calories: 300, carbs: 50, protein: 8, fat: 10 },
    "Ayam Bakar": { calories: 200, carbs: 5, protein: 25, fat: 10 },
    "Salad": { calories: 150, carbs: 10, protein: 2, fat: 5 },
  };
  return data[foodName] || { calories: 0, carbs: 0, protein: 0, fat: 0 };
};

// POST /scan/food
router.post('/food', upload.single('image'), async (req, res) => {
  try {
    if (!req.file) return res.status(400).json({ error: 'Image file is required' });

    const [result] = await client.labelDetection(req.file.buffer);
    const labels = result.labelAnnotations.map((label) => label.description);

    const foodName = labels[0]; // Ambil label pertama sebagai nama makanan (mock-up sederhana)
    const nutrition = estimateNutrition(foodName);

    res.status(200).json({
      foodName,
      nutrition,
    });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

// POST /scan/ingredients
router.post('/ingredients', upload.single('image'), async (req, res) => {
  try {
    if (!req.file) return res.status(400).json({ error: 'Image file is required' });

    const [result] = await client.labelDetection(req.file.buffer);
    const labels = result.labelAnnotations.map((label) => label.description);

    res.status(200).json({
      ingredients: labels,
    });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

module.exports = router;
