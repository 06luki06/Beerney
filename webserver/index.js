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

app.get('/status', (req, res) =>
    res.json({ status: 'ok'}));

app.get('/beers', (req, res) => {
    const beers = db.prepare('SELECT * FROM beers').all();
    res.json(beers);
});

app.post('/beers', express.json(),(req, res) => {
    const { brand, longitude, latitude, city, drunkAt } = req.body;
    if (!brand || !longitude || !latitude || !city || !drunkAt) {
        res.status(400).json({ error: 'Missing required fields' });
        return;
    }
    const insertBeer = db.prepare(`
        INSERT INTO beers (brand, longitude, latitude, city, drunkAt)
        VALUES (?, ?, ?, ?, ?)
    `);
    const info = insertBeer.run(brand, longitude, latitude, city, drunkAt);

    res.json({ id: info.lastInsertRowid, brand, longitude, latitude, city, drunkAt });
});

app.delete('/beers/:id', (req, res) => {
    const {id} = req.params;
    const deleteBeer = db.prepare('DELETE FROM beers WHERE id = ?');
    const info = deleteBeer.run(id);
    if (info.changes === 0) {
        res.status(404).json({error: `Beer with id ${id} not found`});
        return;
    }
    res.json({id});
});

app.listen(port, () => console.log(`Server listening!`));