// const passport = require('passport');
// const GoogleStrategy = require('passport-google-oauth20').Strategy;
// const FacebookStrategy = require('passport-facebook').Strategy;
// const User = require('../models/user');

// // Google OAuth Configuration
// passport.use(
//     new GoogleStrategy(
//         {
//             clientID: process.env.GOOGLE_CLIENT_ID,
//             clientSecret: process.env.GOOGLE_CLIENT_SECRET,
//             callbackURL: "/auth/google/callback",
//         },
//         async (accessToken, refreshToken, profile, done) => {
//             try {
//                 let user = await User.findOne({ googleId: profile.id });
//                 if (!user) {
//                     user = await User.create({
//                         googleId: profile.id,
//                         name: profile.displayName,
//                         email: profile.emails[0].value,
//                     });
//                 }
//                 done(null, user);
//             } catch (err) {
//                 done(err, null);
//             }
//         }
//     )
// );

// // Facebook OAuth Configuration
// passport.use(
//     new FacebookStrategy(
//         {
//             clientID: process.env.FACEBOOK_CLIENT_ID,
//             clientSecret: process.env.FACEBOOK_CLIENT_SECRET,
//             callbackURL: "/auth/facebook/callback",
//             profileFields: ['id', 'displayName', 'email'],
//         },
//         async (accessToken, refreshToken, profile, done) => {
//             try {
//                 let user = await User.findOne({ facebookId: profile.id });
//                 if (!user) {
//                     user = await User.create({
//                         facebookId: profile.id,
//                         name: profile.displayName,
//                         email: profile.emails ? profile.emails[0].value : undefined,
//                     });
//                 }
//                 done(null, user);
//             } catch (err) {
//                 done(err, null);
//             }
//         }
//     )
// );

// module.exports = passport;
