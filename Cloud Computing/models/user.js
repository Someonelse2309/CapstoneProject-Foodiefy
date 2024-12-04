// const mongoose = require('mongoose');
// const bcrypt = require("bcrypt");
// // const UserSchema = new mongoose.Schema({
// //     name: { type: String, required: true },
// //     email: { type: String, required: true, unique: true },
// //     password: { type: String },
// //     googleId: { type: String },
// //     facebookId: { type: String },
// //     phone: { type: String },
// // });

// // module.exports = mongoose.model('User', UserSchema);

// const userSchema = new mongoose.Schema(
//   {
//     name: {
//       type: String,
//       required: true,
//       trim: true,
//     },
//     email: {
//       type: String,
//       required: true,
//       unique: true,
//       trim: true,
//     },
//     password: {
//       type: String,
//       required: function () {
//         return !this.googleId && !this.facebookId; // Password is required only if not using OAuth
//       },
//     },
//     phone: {
//       type: String,
//       trim: true,
//     },
//     googleId: {
//       type: String,
//     },
//     facebookId: {
//       type: String,
//     },
//     age: {
//       type: Number,
//       min: 0,
//     },
//     gender: {
//       type: String,
//       enum: ["Male", "Female", "Other"],
//     },
//     height: {
//       type: Number, // in cm
//     },
//     weight: {
//       type: Number, // in kg
//     },
//     activityLevel: {
//       type: String,
//       enum: ["Inactive", "Normal", "Active"],
//     }
//   },
//   {
//     timestamps: true, // Automatically manage createdAt and updatedAt fields
//   }
// );

// // Hash the password before saving the user to the database
// userSchema.pre("save", async function (next) {
//   if (this.isModified("password")) {
//     const salt = await bcrypt.genSalt(10);
//     this.password = await bcrypt.hash(this.password, salt);
//   }
//   next();
// });

// // Compare entered password with the hashed password in the database
// userSchema.methods.comparePassword = async function (password) {
//   return bcrypt.compare(password, this.password);
// };

// const User = mongoose.model("User", userSchema);
// module.exports = User;

