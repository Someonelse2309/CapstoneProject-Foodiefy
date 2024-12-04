const axios = require("axios");

// Load API Key and Search Engine ID from environment variables
const GOOGLE_API_KEY = process.env.GOOGLE_API_KEY;
const SEARCH_ENGINE_ID = process.env.SEARCH_ENGINE_ID;

exports.getRecommendations = async (ingredients) => {
  const query = ingredients.join(", "); // Combine ingredients into a search query
  const apiUrl = `https://www.googleapis.com/customsearch/v1?q=${encodeURIComponent(
    query
  )}&cx=${SEARCH_ENGINE_ID}&key=${GOOGLE_API_KEY}`;

  try {
    const response = await axios.get(apiUrl);
    const items = response.data.items || [];
    // Map results to a simpler format
    return items.map((item) => ({
      title: item.title,
      link: item.link,
      snippet: item.snippet,
    }));
  } catch (error) {
    console.error("Error fetching recipes from Google:", error.message);
    throw new Error("Failed to fetch recipes. Please try again later.");
  }
};
