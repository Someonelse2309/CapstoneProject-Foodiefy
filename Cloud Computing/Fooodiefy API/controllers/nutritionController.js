const admin = require("firebase-admin");
const db = admin.firestore();

// Helper: Mendapatkan tanggal hari ini dalam format ISO
const getTodayDate = () => {
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  return today.toISOString();
};

// Helper: Mendapatkan tanggal 7 hari terakhir
const getLast7Days = () => {
  const today = new Date();
  const dates = [];
  for (let i = 0; i < 7; i++) {
    const date = new Date(today);
    date.setDate(today.getDate() - i);
    date.setHours(0, 0, 0, 0);
    dates.push(date.toISOString());
  }
  return dates;
};

// GET /nutrition/today
exports.getToday = async (req, res) => {
  try {
    const today = getTodayDate();
    const snapshot = await db
      .collection("nutrition")
      .where("uid", "==", req.uid)
      .where("date", "==", today)
      .get();

    if (snapshot.empty) {
      return res.status(200).json({
        meals: [],
        total: { calories: 0, carbs: 0, protein: 0, fat: 0 },
      });
    }

    const meals = [];
    let total = { calories: 0, carbs: 0, protein: 0, fat: 0 };

    snapshot.forEach((doc) => {
      const data = doc.data();
      meals.push(data);
      total.calories += data.calories || 0;
      total.carbs += data.carbs || 0;
      total.protein += data.protein || 0;
      total.fat += data.fat || 0;
    });

    res.status(200).json({ meals, total });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};


// POST /nutrition/add
exports.addMeal = async (req, res) => {
  console.log('Received request:', req.body);
  
  const { berat, waktuMakan, food_info } = req.body;

  try {
    if (!req.uid) {
      console.error("User ID is missing in the request");
      return res.status(401).json({ message: "User ID is required" });
    }

    if (!food_info || typeof food_info.id !== 'string' || !berat || !waktuMakan || 
        !food_info.calories || !food_info.carbs || 
        !food_info.protein || !food_info.fat || typeof food_info.name !== 'string') {
      console.error("Input validation failed:", { food_info, berat, waktuMakan });
      return res.status(400).json({ message: "All fields are required and must be valid" });
    }

    const foodId = food_info.id;
    const foodDoc = await db.collection("food").doc(foodId).get();
    if (!foodDoc.exists) {
      return res.status(404).json({ message: "Food item not found" });
    }

    const parsedBerat = parseFloat(berat);
    if (isNaN(parsedBerat)) {
      return res.status(400).json({ message: "'berat' must be a numeric value" });
    }

    const portionFactor = parsedBerat / 100;

    const nutritionData = {
      uid: req.uid,
      date: getTodayDate(),
      namaMakanan: food_info.name,
      berat: parsedBerat,
      waktuMakan,
      calories: portionFactor * food_info.calories,
      carbs: portionFactor * food_info.carbs,
      protein: portionFactor * food_info.protein,
      fat: portionFactor * food_info.fat,
      createdAt: admin.firestore.Timestamp.now(),
    };

    await db.collection("nutrition").add(nutritionData);
    res.status(201).json({ message: "Meal added successfully" });
  } catch (error) {
    console.error('Error adding meal:', error.message, error.stack);
    res.status(500).json({ error: error.message });
  }
};


// GET /nutrition/history
exports.getHistory = async (req, res) => {
  try {
    const last7Days = getLast7Days();
    const snapshot = await db
      .collection("nutrition")
      .where("uid", "==", req.uid)
      .where("date", "in", last7Days)
      .get();

    if (snapshot.empty) {
      return res.status(200).json({ history: [] });
    }

    const history = {};

    snapshot.forEach((doc) => {
      const data = doc.data();
      if (!history[data.date]) {
        history[data.date] = {
          meals: [],
          total: { calories: 0, carbs: 0, protein: 0, fat: 0 },
        };
      }

      history[data.date].meals.push(data);
      history[data.date].total.calories += data.calories || 0;
      history[data.date].total.carbs += data.carbs || 0;
      history[data.date].total.protein += data.protein || 0;
      history[data.date].total.fat += data.fat || 0;
    });

    res.status(200).json({ history });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};
