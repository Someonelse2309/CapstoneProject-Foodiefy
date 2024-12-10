const express = require("express");
const router = express.Router();
const authMiddleware = require("../middleware/authMiddleware");
const {
  getToday,
  addMeal,
  getHistory,
} = require("../controllers/nutritionControllerr");

router.get("/today", authMiddleware, getToday);
router.post("/add", authMiddleware, addMeal);
router.get("/history", authMiddleware, getHistory);

module.exports = router;
