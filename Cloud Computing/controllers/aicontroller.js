const visionService = require("../services/visionService");
const recommendationService = require("../services/recommendationService");

exports.scanImage = async (req, res, next) => {
  try {
    const imagePath = req.file.path; // Path to uploaded image
    const detectedItems = await visionService.detectIngredients(imagePath);
    res.status(200).json({ detectedItems });
  } catch (error) {
    next(error);
  }
};

exports.getRecommendations = async (req, res, next) => {
    try {
      const { ingredients } = req.query;
      if (!ingredients) {
        return res.status(400).json({ error: "Ingredients are required." });
      }
  
      // Fetch recommendations from Google Custom Search
      const recommendations = await recommendationService.getRecommendations(
        ingredients.split(",")
      );
      res.status(200).json({ recipes: recommendations });
    } catch (error) {
      next(error);
    }
  };
  
  
