const express = require('express');
const admin = require('firebase-admin');
const router = express.Router();

// Fungsi untuk memproses pesan dan mendapatkan balasan
const getChatbotResponse = async (message) => {
  // Logika sederhana untuk mengelola pesan
  // Di sini, Anda bisa menggunakan model AI atau API eksternal untuk mendapatkan balasan yang lebih kompleks
  const responses = {
    'hi': 'Hello! How can I help you today?',
    'bye': 'Goodbye! Have a great day!',
    'how are you?': 'I am just a program, but thank you for asking!',
  };

  return responses[message.toLowerCase()] || "I'm sorry, I didn't understand that.";
};

// POST /chatbot/message
router.post('/message', async (req, res) => {
  const { message } = req.body;

  if (!message) {
    return res.status(400).json({ error: 'Message is required' });
  }

  try {
    const response = await getChatbotResponse(message);
    res.status(200).json({ reply: response });
  } catch (error) {
    res.status(500).json({ error: 'Error processing message' });
  }
});

module.exports = router;
