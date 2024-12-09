const jwt = require("jsonwebtoken");

module.exports = (req, res, next) => {
  // Ensure the Authorization header exists
  const authHeader = req.headers.authorization;
  if (!authHeader) {
    return res.status(401).json({ message: "Authorization header missing" });
  }

  // Extract the token (assuming format: "Bearer <token>")
  const token = authHeader.split(" ")[1];
  if (!token) {
    return res.status(401).json({ message: "Token not provided in Authorization header" });
  }

  try {
    // Verify the token
    const decoded = jwt.verify(token, process.env.JWT_SECRET);
    req.userId = decoded.userId; // Ensure your token contains a `userId` field
    next(); // Call the next middleware or route handler
  } catch (err) {
    // Handle invalid or expired tokens
    const isExpired = err.name === "TokenExpiredError";
    res.status(401).json({ message: isExpired ? "Token expired" : "Invalid token" });
  }
};


// const jwt = require("jsonwebtoken");

// module.exports = (req, res, next) => {
//   const token = req.headers.authorization;

//   if (!token) return res.status(401).send({ message: "No token provided" });

//   try {
//     const decoded = jwt.verify(token, process.env.JWT_SECRET);
//     req.userId = decoded.userId;
//     next();
//   } catch (err) {
//     res.status(401).send({ message: "Invalid token" });
//   }
// };
