const express = require("express");
const router = express.Router();
const { getRecipeById } = require("../controllers/recipeController");

// Rute untuk mendapatkan resep berdasarkan ID
router.get("/:recipeId", getRecipeById);

module.exports = router;
