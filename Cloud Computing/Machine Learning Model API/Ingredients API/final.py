from flask import Flask, request, jsonify
import tensorflow as tf
import numpy as np
import io
from PIL import Image
from google.cloud import firestore

app = Flask(__name__)

# Load model untuk deteksi bahan makanan
try:
    model_ingredients = tf.keras.models.load_model('ingredients_classifier.keras')
    print("Model ingredients loaded successfully.")
except Exception as e:
    print("Error loading ingredients model:", str(e))

# Load model untuk deteksi kesegaran
try:
    model_freshness = tf.keras.models.load_model('freshness_classifier.keras')
    print("Model freshness loaded successfully.")
except Exception as e:
    print("Error loading freshness model:", str(e))

# Daftar bahan makanan
ingredients_list = [
    "apple", "apricot", "avocado", "bacon", "bagel", "banana", "beans", "beef", "beet", 
    "blackberries", "bread", "broccoli", "butter", "cabbage", "carrot", "cauliflower", 
    "celery", "cheese", "cherries", "chicken", "chocolate", "coconut", "corn", "crab", 
    "cranberries", "cucumber", "daikon", "dates", "eggs", "fish", "garlic", "grape", 
    "green_bell_pepper", "ham", "honey", "kiwi", "lemon", "lettuce", "lime", "mango", 
    "melon", "milk", "mushroom", "nectarine", "onion", "pear", "peking", "pepper", "plum", 
    "pomegranate", "pomelo", "potato", "pumpkin", "raddish", "raspberries", "red_bell_pepper", 
    "rhubarb", "Rice", "strawberry", "sausage", "shallot", "spinach", "sweetpotato", "tangerine", 
    "tofu", "tomato", "watermelon", "yellow_bell_pepper", "yogurt", "zucchini"
]

# Setup Firestore client
db = firestore.Client.from_service_account_json("firebase-code.json")

@app.route('/predict', methods=['POST'])
def predict():
    try:
        # Check if the file exists in the request
        if 'file' not in request.files:
            return jsonify({"error": "No file part"}), 400

        img_file = request.files['file']
        if img_file.filename == '':
            return jsonify({"error": "No selected file"}), 400

        # Open the image
        img_bytes = img_file.read()
        img = Image.open(io.BytesIO(img_bytes))

        # Convert to RGB if the image has more than 3 channels (e.g., RGBA)
        if img.mode == 'RGBA':
            img = img.convert('RGB')

        # Resize the image for each model
        img_ingredients = img.resize((256, 256))

        # Preprocess the image into a numpy array and normalize
        img_ingredients_array = np.array(img_ingredients) / 255.0
        img_ingredients_array = np.expand_dims(img_ingredients_array, axis=0)

        print("Image array shape for ingredients:", img_ingredients_array.shape)

        # Predict ingredient types
        ingredients_predictions = model_ingredients.predict(img_ingredients_array)[0]
        print("Ingredients predictions:", ingredients_predictions)
        
        detected_ingredients = []
        threshold_ingredients = 0.2  # Lower threshold for testing
        for i, score in enumerate(ingredients_predictions):
            if score >= threshold_ingredients:
                detected_ingredients.append(ingredients_list[i])

        # Log detected ingredients
        print("Detected ingredients:", detected_ingredients)

        # Predict freshness for each detected ingredient
        freshness_results = []
        for ingredient in detected_ingredients:
            img_freshness = img.resize((256, 256))
            img_freshness_array = np.array(img_freshness) / 255.0
            img_freshness_array = np.expand_dims(img_freshness_array, axis=0)

            freshness_prediction = model_freshness.predict(img_freshness_array)[0]
            freshness_status = "fresh" if freshness_prediction[0] < 0.5 else "rotten"
            freshness_results.append({"ingredient": ingredient, "freshness": freshness_status})

        # Fetch recipes from Firestore
        recipes_ref = db.collection("recipes")
        recipes = []

        if detected_ingredients:
            for doc in recipes_ref.stream():
                recipe = doc.to_dict()
                # Check if any detected ingredient is in the recipe's ingredients
                for ingredient in recipe.get('ingredients', []):
                    if ingredient['name'].lower() in detected_ingredients:
                        recipes.append(recipe)
                        break  # Avoid adding the same recipe multiple times

        # JSON Response
        response = {
            "ingredients name": detected_ingredients,
            "freshness": freshness_results,
            "recipes": recipes
        }

        return jsonify(response), 200

    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(debug=True, port=5000)
