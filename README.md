# CapstoneProject-Foodiefy

## Product name: “Foodiefy: Suggest Choice for Your Needs“

### Theme: “Health Innovation: Empowering Vulnerable Communities for Health and Well-being”

Foodiefy is an application that leverages artificial intelligence to revolutionize the management of daily nutritional needs.

Here’s what Foodiefy can do: 
* Identify food items from images
* Estimate calorie consumption
* Monitor daily caloric requirements

### Team ID: C242-PS087

### Team members
1. M239B4KY3359 - Nicholas Tanugroho - Universitas Kristen Duta Wacana - Machine Learning - active
2. M239B4KY0503 - Andi Putrayana - Universitas Kristen Duta Wacana - Machine Learning - active
3. M239B4KX0542 - Angela Sekar Widelia - Universitas Kristen Duta Wacana - Machine Learning - active
4. C239B4KX0831 - Beatrice Dwi Putri Rustandi - Universitas Kristen Duta Wacana - Cloud Computing - active
5. C239B4KX0461 - Amelia Putri Aftiana - Universitas Kristen Duta Wacana - Cloud Computing - active
6. A239B4KX2503 - Milka Putri Fibiona - Universitas Kristen Duta Wacana - Mobile Development - active
7. A239B4KX1024 - Debora Patricia Sharon Rembet - Universitas Kristen Duta Wacana - Mobile Development - active

## Machine Learning Learning Path
### Steps
1. Go to kaggle and download the required dataset and put it in the designated folder path
2. Open your local or your desired jupyter notebook IDE and load the code and install all the required modules from the requirements.txt. <br> The object detection on is only applicable using colab 3.10 and tensorflow 2.8.
3. Run the all of the cell and don't forget to save the model

### Datasets
* Food classifier dataset <br>
https://www.kaggle.com/datasets/kmader/food41?select=images
* Ingredients classifier dataset <br>
https://www.kaggle.com/datasets/sergeynesteruk/packed-fruits-and-vegetables-recognition-benchmark/data <br>
https://www.kaggle.com/datasets/liamboyd1/singular-food-items
* Fresh and Rotten Fruits dataset <br>
https://www.kaggle.com/datasets/swoyam2609/fresh-and-stale-classification <br>
https://www.kaggle.com/datasets/raghavrpotdar/fresh-and-stale-images-of-fruits-and-vegetables <br>
https://www.kaggle.com/datasets/sriramr/fruits-fresh-and-rotten-for-classification/data <br>
https://www.kaggle.com/datasets/mdatikurrahman3111/fresh-and-rotten-classification-dataset-3/data 
* Object Detection model dataset <br>
https://www.kaggle.com/datasets/sergeynesteruk/packed-fruits-and-vegetables-recognition-benchmark/data <br>
https://www.kaggle.com/datasets/liamboyd1/singular-food-items <br>
https://www.kaggle.com/datasets/kmader/food41?select=images
### Accuracy and Loss 
* Food classifier model - 83.24% Test Accuracy & 0.0170 Loss 
* Ingredients classifier dataset - 71.84% Test Accuracy & 3.4115 Loss
* Fresh and Rotten Fruits dataset - 88.70% Test Accuracy & 0.2856 Loss
* Object Detection model dataset - 55% Test Accuracy & 0.3106 Loss

## Cloud Computing Learning Path
### Architecture 
<img src="https://raw.githubusercontent.com/Someonelse2309/CapstoneProject-Foodiefy/main/Cloud%20Computing/Architecture.png" width="auto" height=350>

### Steps to Deploy Foodiefy Backend API to App Engine

1. Clone this repository to Google Cloud Shell
   ```bash
   git clone https://github.com/Someone/CapstoneProject-Foodiefy.git
   ```

2. Open editor and move to this folder `Foodiefy API`
   ```bash
   cd CapstoneProject-Foodiefy/Cloud\\ Computing/Foodiefy\\ API
   ```

3. Activate required APIs & services
   ```bash
   gcloud services enable cloudbuild.googleapis.com \
                          run.googleapis.com \
                          firestore.googleapis.com \
                          appengine.googleapis.com
   ```

4. Configure Firestore as your database
    - Set up Firestore in Native mode.
    - Create collections for:
      - App Databases
      - User Databases
      - Recipes Databases
      - Food Databases

5. Run this command to install dependencies
   ```bash
   npm install
   ```

6. Change the configuration in `config.js`, `.env`, and `app.yaml` with new configuration.

7. Deploy your app to App Engine with these commands
   ```bash
   gcloud init
   gcloud app deploy
   ```

### Steps to Deploy Machine Learning Model API to App Engine

1. Clone this repository to Google Cloud Shell
   ```bash
   git clone https://github.com/Someone/CapstoneProject-Foodiefy.git
   ```

2. Open editor and move to this folder `Machine Learning Model API`
   ```bash
   cd CapstoneProject-Foodiefy/Cloud\\ Computing/Machine\\ Learning\\ Model\\ API
   ```

3. Run this command to install dependencies
   ```bash
   npm install
   ```

4. Change the configuration in `app.yaml`, `.env`, and other relevant files with new configuration.

5. Deploy your app to App Engine with these commands
   ```bash
   gcloud init
   gcloud app deploy
   ```

## Mobile Development Learning Path 
### Steps
1. Clone this repository to your device
2. Open In Android studio
3. Build Project
4. Run and debugging the project on your AVD emulator

### App Screenshots
1. Authentication/Registration Page <br> <br>
   <img src="https://raw.githubusercontent.com/Someonelse2309/CapstoneProject-Foodiefy/main/Mobile%20Development/Assets/3.png" width="auto" height=350>
   <img src="https://raw.githubusercontent.com/Someonelse2309/CapstoneProject-Foodiefy/main/Mobile%20Development/Assets/4.png" width="auto" height=350>
   <img src="https://raw.githubusercontent.com/Someonelse2309/CapstoneProject-Foodiefy/main/Mobile%20Development/Assets/5.png" width="auto" height=350> <br> <br>
2. Body Metrics Page <br> <br>
   <img src="https://raw.githubusercontent.com/Someonelse2309/CapstoneProject-Foodiefy/main/Mobile%20Development/Assets/6.png" width="auto" height=350>
   <img src="https://raw.githubusercontent.com/Someonelse2309/CapstoneProject-Foodiefy/main/Mobile%20Development/Assets/7.png" width="auto" height=350>
   <img src="https://raw.githubusercontent.com/Someonelse2309/CapstoneProject-Foodiefy/main/Mobile%20Development/Assets/8.png" width="auto" height=350> <br>
   <img src="https://raw.githubusercontent.com/Someonelse2309/CapstoneProject-Foodiefy/main/Mobile%20Development/Assets/9.png" width="auto" height=350>
   <img src="https://raw.githubusercontent.com/Someonelse2309/CapstoneProject-Foodiefy/main/Mobile%20Development/Assets/10.png" width="auto" height=350>
   <img src="https://raw.githubusercontent.com/Someonelse2309/CapstoneProject-Foodiefy/main/Mobile%20Development/Assets/11.png" width="auto" height=350> <br> <br>
3. Dashboard Page <br> <br>
   <img src="https://raw.githubusercontent.com/Someonelse2309/CapstoneProject-Foodiefy/main/Mobile%20Development/Assets/12.png" width="auto" height=350>
   <img src="https://raw.githubusercontent.com/Someonelse2309/CapstoneProject-Foodiefy/main/Mobile%20Development/Assets/13.png" width="auto" height=350> 
   <img src="https://raw.githubusercontent.com/Someonelse2309/CapstoneProject-Foodiefy/main/Mobile%20Development/Assets/19.png" width="auto" height=350> <br>
   <img src="https://raw.githubusercontent.com/Someonelse2309/CapstoneProject-Foodiefy/main/Mobile%20Development/Assets/20.png" width="auto" height=350> <br> <br>
4. Scan Food Page <br> <br>
   <img src="https://raw.githubusercontent.com/Someonelse2309/CapstoneProject-Foodiefy/main/Mobile%20Development/Assets/14.png" width="auto" height=350>
   <img src="https://raw.githubusercontent.com/Someonelse2309/CapstoneProject-Foodiefy/main/Mobile%20Development/Assets/15.png" width="auto" height=350>
   <img src="https://raw.githubusercontent.com/Someonelse2309/CapstoneProject-Foodiefy/main/Mobile%20Development/Assets/16.png" width="auto" height=350> <br>
   <img src="https://raw.githubusercontent.com/Someonelse2309/CapstoneProject-Foodiefy/main/Mobile%20Development/Assets/17.png" width="auto" height=350>
   <img src="https://raw.githubusercontent.com/Someonelse2309/CapstoneProject-Foodiefy/main/Mobile%20Development/Assets/18.png" width="auto" height=350> <br> <br>
5. Scan Ingredients Page <br> <br>
   <img src="https://raw.githubusercontent.com/Someonelse2309/CapstoneProject-Foodiefy/main/Mobile%20Development/Assets/21.png" width="auto" height=350>
   <img src="https://raw.githubusercontent.com/Someonelse2309/CapstoneProject-Foodiefy/main/Mobile%20Development/Assets/22.png" width="auto" height=350>
   <img src="https://raw.githubusercontent.com/Someonelse2309/CapstoneProject-Foodiefy/main/Mobile%20Development/Assets/23.png" width="auto" height=350> <br>
   <img src="https://raw.githubusercontent.com/Someonelse2309/CapstoneProject-Foodiefy/main/Mobile%20Development/Assets/24.png" width="auto" height=350> <br> <br>
6. Favourite Recipe, History, and Notification Page <br> <br>
   <img src="https://raw.githubusercontent.com/Someonelse2309/CapstoneProject-Foodiefy/main/Mobile%20Development/Assets/26.png" width="auto" height=350>
   <img src="https://raw.githubusercontent.com/Someonelse2309/CapstoneProject-Foodiefy/main/Mobile%20Development/Assets/27.png" width="auto" height=350>
   <img src="https://raw.githubusercontent.com/Someonelse2309/CapstoneProject-Foodiefy/main/Mobile%20Development/Assets/28.png" width="auto" height=350> <br> <br>
7. Settings Page <br> <br>
   <img src="https://raw.githubusercontent.com/Someonelse2309/CapstoneProject-Foodiefy/main/Mobile%20Development/Assets/29.png" width="auto" height=350>
   <img src="https://raw.githubusercontent.com/Someonelse2309/CapstoneProject-Foodiefy/main/Mobile%20Development/Assets/30.png" width="auto" height=350>
