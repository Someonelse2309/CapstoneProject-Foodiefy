from flask import Flask, request, jsonify
import tensorflow as tf
import numpy as np
import io
from PIL import Image

app = Flask(__name__)

# Load model untuk deteksi bahan makanan
try:
    model_ingredients = tf.keras.models.load_model('ingredients_classifier.keras')
    print("Model ingredients loaded successfully.")
    model_ingredients.summary()
except Exception as e:
    print("Error loading ingredients model:", str(e))

# Load model untuk deteksi kesegaran
try:
    model_freshness = tf.keras.models.load_model('freshness_classifier.keras')
    print("Model freshness loaded successfully.")
    model_freshness.summary()
except Exception as e:
    print("Error loading freshness model:", str(e))

# Daftar bahan makanan (list yang diberikan)
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

@app.route('/predict', methods=['POST'])
def predict():
    try:
        # Memeriksa apakah file ada dalam request
        if 'file' not in request.files:
            return jsonify({"error": "No file part"}), 400

        img_file = request.files['file']
        if img_file.filename == '':
            return jsonify({"error": "No selected file"}), 400

        # Membuka gambar
        img_bytes = img_file.read()
        img = Image.open(io.BytesIO(img_bytes))

        # Jika gambar memiliki lebih dari 3 channel (misalnya RGBA), ubah ke RGB
        if img.mode == 'RGBA':
            img = img.convert('RGB')

        # Resize gambar untuk masing-masing model
        img_ingredients = img.resize((256, 256))  
        img_freshness = img.resize((256, 256))    

        # Preprocess gambar menjadi array numpy dan normalisasi
        img_ingredients_array = np.array(img_ingredients) / 255.0
        img_freshness_array = np.array(img_freshness) / 255.0

        # Tambah dimensi batch
        img_ingredients_array = np.expand_dims(img_ingredients_array, axis=0)
        img_freshness_array = np.expand_dims(img_freshness_array, axis=0)

        # Prediksi jenis bahan
        ingredients_predictions = model_ingredients.predict(img_ingredients_array)[0]
        detected_ingredients = []
        threshold_ingredients = 0.3  # Ambang batas untuk ingredients
        for i, score in enumerate(ingredients_predictions):
            if score >= threshold_ingredients:
                detected_ingredients.append(ingredients_list[i])

        # Prediksi kesegaran
        freshness_prediction = model_freshness.predict(img_freshness_array)[0]
        # print("Freshness Prediction Output:", freshness_prediction)
        freshness_status = "fresh" if freshness_prediction[0] < 0.5 else "rotten"
        # freshness_status = "fresh" if np.argmax(freshness_prediction) == 0 else "rotten"

        # Respons JSON
        response = {
            "ingredients name": detected_ingredients,
            "freshness": freshness_status
        }

        return jsonify(response), 200

    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(debug=True, port=5000)
