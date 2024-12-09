from flask import Flask, request, jsonify
import tensorflow as tf
# from tensorflow.keras.preprocessing import image
import numpy as np
import json
import io
from PIL import Image

app = Flask(__name__)

# Load model
model = tf.keras.models.load_model('food_classifier.keras')

# Load food data (misalnya dari file JSON)
with open('FoodJSON.json') as f:
    food_data = json.load(f)

@app.route('/predict', methods=['POST'])
def predict():
    try:
        # Check if file is part of request
        if 'file' not in request.files:
            return jsonify({"error": "No file part"}), 400
        
        img_file = request.files['file']
        if img_file.filename == '':
            return jsonify({"error": "No selected file"}), 400
        
        # Convert the file to a BytesIO object
        img_bytes = img_file.read()
        img = Image.open(io.BytesIO(img_bytes))  # Open image using PIL from BytesIO

        # Resize the image to match the model's expected input size (256x256)
        img = img.resize((256, 256))  # Adjust size to (256, 256)
        img_array = np.array(img)  # Convert image to numpy array
        img_array = np.expand_dims(img_array, axis=0)  # Add batch dimension

        # Perform inference
        predictions = model.predict(img_array)[0]  # Get predictions for the first image in batch

        # Threshold for class selection
        threshold = 0.6
        results = []

        # Match predictions with food data
        for i, score in enumerate(predictions):
            if score >= threshold:
                food_info = food_data[i]  # Match with corresponding food from FoodJSON
                food_info['sureness'] = float(score)  # Add prediction confidence as 'sureness'
                results.append(food_info)

        # If no items match the threshold
        if not results:
            return jsonify({"message": "No food item detected with sufficient confidence"}), 200

        # Return the results as JSON in the expected format
        return jsonify(results)
    
    except Exception as e:
        return jsonify({"error": str(e)}), 400

if __name__ == '__main__':
    app.run(debug=True, port=5000)  # Ensure the app is running on port 5000
