const express = require('express');
const sqlite = require('better-sqlite3');
const fs = require('fs').promises;
const app = express();
const port = 3000;

const db = new sqlite('database.db');
const apiKeyFilePath = './apiKey.json';

const createBeerTable = db.prepare(`
  CREATE TABLE IF NOT EXISTS beers (
    id INTEGER PRIMARY KEY,
    brand TEXT NOT NULL,
    longitude REAL NOT NULL,
    latitude REAL NOT NULL,
    city TEXT NOT NULL,
    drunkAt TEXT NOT NULL
  )
`);
createBeerTable.run();

app.get('/apikey', async (req, res) => {
    try{
        const apiKeyData = await fs.readFile(apiKeyFilePath, 'utf8');
        const apikeyObject = JSON.parse(apiKeyData);
        res.json (apikeyObject);
    }catch(err){
        res.status(500).json({ error: "Internal server error" });
    }
});

app.listen(port, () => console.log(`Server listening on port ${port}!`));