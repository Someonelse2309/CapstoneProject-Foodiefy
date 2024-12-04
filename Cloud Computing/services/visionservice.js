const { ImageAnnotatorClient } = require("@google-cloud/vision");
const client = new ImageAnnotatorClient();

exports.detectIngredients = async (imagePath) => {
  const [result] = await client.labelDetection(imagePath);
  const labels = result.labelAnnotations.map((label) => label.description);
  return labels.filter((label) =>
    ["carrot", "onion", "garlic", "potato"].includes(label.toLowerCase())
  );
};
