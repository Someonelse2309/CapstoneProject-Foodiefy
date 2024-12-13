const express = require("express");
const router = express.Router();
const { addFavorite, removeFavorite, getFavorites } = require("../controllers/favoritesController");
const authMiddleware = require("../middleware/authMiddleware");

// Menambahkan resep ke favorit
router.post("/favorites", authMiddleware, addFavorite);

// Menghapus resep dari favorit
router.delete("/favorites", authMiddleware, removeFavorite);

// Mengambil semua resep favorit
router.get("/favorites", authMiddleware, getFavorites);

module.exports = router;
