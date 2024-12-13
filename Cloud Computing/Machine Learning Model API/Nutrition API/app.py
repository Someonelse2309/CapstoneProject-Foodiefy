from flask import Flask, request, jsonify
import tensorflow as tf
import numpy as np
import json
import io
from PIL import Image
import requests

app = Flask(__name__)

# Load model
model = tf.keras.models.load_model('food_classifier.keras') #masukkan file ini di folder yang sama

# Load food data (misalnya dari file JSON)
with open('FoodJSON.json') as f:
    food_data = json.load(f)

# URL API Node.js
NODE_API_URL = 'http://localhost:8080/nutrition/add' #ganti dengan link deploy gcp

def require_auth(f):
    def decorated(*args, **kwargs):
        # Ambil token dari header
        auth_header = request.headers.get("Authorization")
        if not auth_header or not auth_header.startswith("Bearer "):
            return jsonify({"error": "Authorization header missing or invalid"}), 403
        # Jika diperlukan, Anda dapat memverifikasi token di sini

        return f(*args, **kwargs)
    return decorated

@app.route('/predict', methods=['POST'])
def predict():
    try:
        # Validasi file
        if 'file' not in request.files:
            return jsonify({"error": "No file part"}), 400
        
        img_file = request.files['file']
        if img_file.filename == '':
            return jsonify({"error": "No selected file"}), 400

        # Proses gambar
        img_bytes = img_file.read()
        img = Image.open(io.BytesIO(img_bytes))
        img = img.resize((256, 256))  # Sesuaikan ukuran input model
        img_array = np.array(img)
        img_array = np.expand_dims(img_array, axis=0)

        # Prediksi
        predictions = model.predict(img_array)[0]
        threshold = 0.6
        results = []

        for i, score in enumerate(predictions):
            if score >= threshold:
                food_info = food_data[i]
                # Format food_info according to the specified structure
                formatted_food_info = {
                    "id": food_info["id"],  # Assuming your food_data has 'id'
                    "name": food_info["name"],  # Assuming your food_data has 'name'
                    "calories": float(score * food_info["calories"]),  # Scale based on prediction
                    "carbs": float(score * food_info["carbs"]),
                    "protein": float(score * food_info["protein"]),
                    "fat": float(score * food_info["fat"]),
                    "sureness": float(score)  # Include sureness if needed
                }
                results.append(formatted_food_info)

        if not results:
            return jsonify({"message": "No food item detected with sufficient confidence"}), 200

        # Hasil prediksi
        return jsonify({
            "message": "Food detected successfully",
            "predictions": results  # Kirim semua prediksi ke front-end
        }), 200

    except Exception as e:
        return jsonify({"error": str(e)}), 400
    
@app.route('/submit', methods=['POST'])
def submit_food():
    try:
        # Capture input from request
        namaMakanan = request.json.get('namaMakanan')
        berat = request.json.get('berat')
        waktuMakan = request.json.get('waktuMakan')
        food_info = request.json.get('food_info')

        # Validate input
        if not namaMakanan or not berat or not waktuMakan or not food_info:
            return jsonify({"error": "All fields (namaMakanan, berat, waktuMakan, food_info) are required"}), 400

        try:
            berat = float(berat)  # Convert berat to float
        except ValueError:
            return jsonify({"error": "'berat' must be a numeric value"}), 400

        # Payload for Node.js API
        payload = {
            "berat": berat,
            "waktuMakan": waktuMakan,
            "food_info": {
                "id": food_info["id"],  # Use id from food_info
                "name": food_info["name"],  # Use name from food_info
                "calories": food_info["calories"],
                "carbs": food_info["carbs"],
                "protein": food_info["protein"],
                "fat": food_info["fat"]
            }
        }

        # Log the payload for debugging
        print("Payload to Node.js API:", json.dumps(payload, indent=4))

        # Authorization header
        auth_header = request.headers.get("Authorization")

        # Send data to Node.js with authorization header
        response = requests.post(NODE_API_URL, json=payload, headers={"Authorization": auth_header})

        # Print status code and response from Node.js
        print("Node.js Response Status Code:", response.status_code)

        # Ensure to handle cases where the response is not in JSON format
        try:
            response_data = response.json()
        except ValueError:
            response_data = {"error": "Invalid response from Node.js"}

        print("Node.js Response Body:", response_data)

        if response.status_code == 201:
            return jsonify({"message": "Data sent successfully"}), 200
        else:
            return jsonify({"error": "Failed to send data to Node.js", "details": response_data}), 500
    except Exception as e:
        return jsonify({"error": str(e)}), 400


if __name__ == '__main__':
    app.run(debug=True, port=8080)
