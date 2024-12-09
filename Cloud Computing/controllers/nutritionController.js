const admin = require("firebase-admin");

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
      .collection('nutrition')
      .where('uid', '==', req.uid)
      .where('date', '==', today)
      .get();

    if (snapshot.empty) {
      return res.status(200).json({ meals: [], total: { calories: 0, carbs: 0, protein: 0, fat: 0 } });
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
  const { namaMakanan, berat, waktuMakan, calories, carbs, protein, fat } = req.body;

  try {
    const today = getTodayDate();
    const nutritionData = {
      uid: req.uid,
      date: today,
      namaMakanan,
      berat,
      waktuMakan,
      calories,
      carbs,
      protein,
      fat,
      createdAt: admin.firestore.Timestamp.now(),
    };

    await db.collection('nutrition').add(nutritionData);

    res.status(201).json({ message: 'Meal added successfully' });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

// GET /nutrition/history
exports.getHistory = async (req, res) => {
  try {
    const last7Days = getLast7Days();
    const snapshot = await db
      .collection('nutrition')
      .where('uid', '==', req.uid)
      .where('date', 'in', last7Days)
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
