var express = require('express');
 const { PrismaClient }= require("@prisma/client") 
var router = express.Router();
 const prisma = new PrismaClient()
var historyData

/* GET home page. */
router.get('/', function(req, res, next) {
  console.log("Accès route principale");
  res.render('index', { title: 'Projet multidisciplinaire 2' });
});

router.get('/data', function(req, res, next) {
  retriveData();
  res.render('data', { title: 'Projet multidisciplinaire 2', historyData:historyData });
});

router.get('/api/data', (req,res)=>{
  retriveData();
  res.json({historyData:historyData});
});

router.get('/history', function(req, res, next) {
  retriveData();
  res.render('history', { title: 'Projet multidisciplinaire 2', historyData:historyData });
});

 async function retriveData(){
   historyData = await prisma.History.findMany()
   console.log(historyData)
 }

module.exports = router; 
