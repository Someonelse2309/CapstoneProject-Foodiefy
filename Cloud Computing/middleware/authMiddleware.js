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
    req.uid = decoded.userId; // Change this line to use req.uid instead of req.userId
    next(); // Call the next middleware or route handler
  } catch (err) {
    // Handle invalid or expired tokens
    const isExpired = err.name === "TokenExpiredError";
    res.status(401).json({ message: isExpired ? "Token expired" : "Invalid token" });
  }
};
