const express = require("express");
const { signup, login, googleLogin, facebookLogin } = require("../controllers/authController");
const router = express.Router();

router.post("/signup", signup);
router.post("/login", login);
router.post("/google-login", googleLogin);
router.post("/facebook-login", facebookLogin);

module.exports = router;
