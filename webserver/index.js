const express = require('express');
const sqlite = require('better-sqlite3');
const fs = require('fs').promises;
const app = express();
const port = 3000;

const db = new sqlite('database.sqlite');

// nohup npm start &

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

const createSmartPhoneTable = db.prepare(`
    CREATE TABLE IF NOT EXISTS smartphones (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        manufacturer TEXT,
        model TEXT,
        android_version TEXT,
        api_level INTEGER,
        display TEXT,
        board TEXT,
        bootloader TEXT,
        brand TEXT,
        device TEXT,
        fingerprint TEXT,
        host TEXT,
        product TEXT,
        user TEXT
    )
`);
createSmartPhoneTable.run();

const createHomingPositionTable = db.prepare(`
    CREATE TABLE IF NOT EXISTS homingpositions (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        address TEXT
    )
`);
createHomingPositionTable.run();

app.get('/status', (req, res) =>{
    console.log("GET status")
    res.json({ status: 'ok'});
});

app.get('/beers', (req, res) => {
    console.log("GET Beers");
    const beers = db.prepare('SELECT * FROM beers').all();
    res.json(beers);
});

app.post('/beers', express.json(),(req, res) => {
    console.log("POST a new beer");
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
    console.log("DELETE a beer");
    const {id} = req.params;
    const deleteBeer = db.prepare('DELETE FROM beers WHERE id = ?');
    const info = deleteBeer.run(id);
    if (info.changes === 0) {
        res.status(404).json({error: `Beer with id ${id} not found`});
        return;
    }
    res.json({id});
});

app.post('/smartphones', express.json(), (req, res) => {
    console.log("POST a new smartphone");
    const { manufacturer, model, androidVersion, apiLevel, display, board, bootloader,
        brand, device, fingerprint, host, product, user } = req.body;

    if (!manufacturer || !model || !androidVersion || !apiLevel || !display || !board || !bootloader ||
        !brand || !device || !fingerprint || !host || !product || !user) {
        res.status(400).json({ error: 'Missing required fields' });
        return;
    }

    const insertSmartphone = db.prepare(`
        INSERT INTO smartphones (
            manufacturer, model, android_version, api_level, display, 
            board, bootloader, brand, device, fingerprint, host, product, user
        )
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    `);

    const info = insertSmartphone.run(
        manufacturer, model, androidVersion, apiLevel, display,
        board, bootloader, brand, device, fingerprint, host, product, user
    );

    res.json({
        id: info.lastInsertRowid, manufacturer, model, androidVersion, apiLevel, display, board,
        bootloader, brand, device, fingerprint, host, product, user
    });
});

app.post('/homingposition', express.json(), (req, res) => {
    console.log("POST a new homingposition");
    const { address } = req.body;

    if (!address) {
        res.status(400).json({ error: 'Missing required fields' });
        return;
    }

    const insertSmartphone = db.prepare(`
        INSERT INTO homingpositions (
            address
        )
        VALUES (?)
    `);

    const info = insertSmartphone.run(
        address
    );

    res.json({
        id: info.lastInsertRowid, address
    });
});

app.listen(port, () => console.log(`Server listening!`));
