const express = require('express');
const router = express.Router();
const { getAbout, getTerms } = require('../controllers/infoController');

// Route for /about
router.get('/about', getAbout);

// Route for /terms
router.get('/terms', getTerms);

module.exports = router;
