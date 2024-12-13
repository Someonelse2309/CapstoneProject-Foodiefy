const express = require("express");
const router = express.Router();
const { addFavorite, removeFavorite, getFavorites } = require("../controllers/favoritesController");
const authMiddleware = require("../middleware/authMiddleware");

// Menambahkan resep ke favorit
router.post("/", authMiddleware, addFavorite); // Pastikan rutenya menggunakan '/' dan bukan '/favorites'

// Menghapus resep dari favorit
router.delete("/", authMiddleware, removeFavorite); // Pastikan rutenya menggunakan '/' dan bukan '/favorites'

// Mengambil semua resep favorit
router.get("/", authMiddleware, getFavorites); // Pastikan rutenya menggunakan '/' dan bukan '/favorites'

module.exports = router;
