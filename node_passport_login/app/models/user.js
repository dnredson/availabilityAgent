var mongoose = require('mongoose');
var Schema = mongoose.Schema,
var bcrypt = require('bcrypt');
    
var UserSchema = new Schema({
username: { type: String, required: true, index: { unique: true } },
password: { type: String, required: true },
name: { type: String, required: true },
role: { type: String, required: true }
});
module.exports = mongoose.model(User, UserSchema);