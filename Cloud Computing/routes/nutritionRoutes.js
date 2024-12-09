const express = require('express');
const router = express.Router();
const { verifyToken } = require('../middleware/authMiddleware');
const { getToday, addMeal, getHistory } = require('../controllers/nutritionController');

router.get('/today', verifyToken, getToday);
router.post('/add', verifyToken, addMeal);
router.get('/history', verifyToken, getHistory);

module.exports = router;
