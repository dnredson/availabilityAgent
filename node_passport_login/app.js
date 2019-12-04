var express             = require("express"),    
mongoose                = require("mongoose"),    
passport                = require("passport"),    
bodyParser              = require("body-parser"),    
User                    = require("./models/user"),    
LocalStrategy           = require("passport-local"),    passportLocalMongoose   = require("passport-local-mongoose")
var app = express();
app.set('view engine','ejs');
mongoose.connect("mongodb://172.31.11.215/dashboard");

app.use(passport.initialize());
app.use(passport.session());
app.use(require("express-session")({    
    secret:"swampufabc",    
    resave: false,    
    saveUninitialized: false
}));

passport.serializeUser(User.serializeUser());
passport.deserializeUser(User.deserializeUser());