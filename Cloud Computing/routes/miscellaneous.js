const express = require('express');
const router = express.Router();

// Data tentang aplikasi dan syarat ketentuan
const aboutInfo = {
  description: "Aplikasi ini dirancang untuk membantu pengguna dalam mengelola kesehatan dan nutrisi mereka dengan fitur yang lengkap dan mudah digunakan."
};

const termsAndConditions = {
  text: "Syarat dan ketentuan ini mengatur penggunaan aplikasi kami. Dengan menggunakan aplikasi ini, Anda setuju untuk mematuhi syarat dan ketentuan yang berlaku."
};

// GET /about
router.get('/about', (req, res) => {
  res.status(200).json(aboutInfo);
});

// GET /terms
router.get('/terms', (req, res) => {
  res.status(200).json(termsAndConditions);
});

module.exports = router;
