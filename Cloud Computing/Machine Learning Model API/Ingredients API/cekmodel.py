from flask import Flask, request, jsonify
import tensorflow as tf
import numpy as np
import json
import io
from PIL import Image

app = Flask(__name__)

# Memuat model untuk deteksi bahan makanan
try:
    model_ingredients = tf.keras.models.load_model('ingredients_classifier.keras')
    print("Model loaded successfully.")
    model_ingredients.summary()
except Exception as e:
    print("Error loading model:", str(e))

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

@app.route('/detect_ingredient', methods=['POST'])
def detect_ingredient():
    try:
        # Memeriksa apakah file ada dalam request
        if 'file' not in request.files:
            return jsonify({"error": "No file part"}), 400

        img_file = request.files['file']
        if img_file.filename == '':
            return jsonify({"error": "No selected file"}), 400

        # Mengubah file menjadi objek BytesIO
        img_bytes = img_file.read()
        img = Image.open(io.BytesIO(img_bytes))  # Membuka gambar menggunakan PIL dari BytesIO

        # Jika gambar memiliki lebih dari 3 channel (misalnya RGBA), kita perlu mengubahnya menjadi RGB
        if img.mode == 'RGBA':
            img = img.convert('RGB')

        # Mengubah ukuran gambar sesuai dengan input model (misalnya 256x256)
        img = img.resize((256, 256))

        # Mengonversi gambar menjadi array numpy dan normalisasi ke rentang [0, 1]
        img_array = np.array(img) / 255.0

        # Menambah dimensi batch (menjadi 1x256x256x3)
        img_array = np.expand_dims(img_array, axis=0)

        # Melakukan prediksi dengan model
        predictions = model_ingredients.predict(img_array)[0]  # Ambil prediksi untuk gambar pertama dalam batch
        
        # Menampilkan hasil prediksi untuk debugging
        print("Predictions:", predictions)

        # Menentukan ambang batas untuk tingkat kepercayaan
        threshold = 0.3  # Menggunakan ambang batas lebih rendah untuk debugging
        detected_ingredients = []

        # Cocokkan prediksi dengan data bahan makanan
        for i, score in enumerate(predictions):
            if score >= threshold:
                ingredient_name = ingredients_list[i]
                detected_ingredients.append({
                    "ingredient": ingredient_name,
                    "confidence": float(score)  # Mengubah tipe dari float32 ke float biasa
                })

        # Menampilkan hasil deteksi untuk debugging
        print("Detected ingredients:", detected_ingredients)

        if not detected_ingredients:
            return jsonify({"message": "No ingredients detected with sufficient confidence"}), 200

        return jsonify({
            "detected_ingredients": detected_ingredients
        })

    except Exception as e:
        return jsonify({"error": str(e)}), 400

if __name__ == '__main__':
    app.run(debug=True, port=5000)  # API ini berjalan pada port 5000
