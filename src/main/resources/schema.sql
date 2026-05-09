CREATE TABLE IF NOT EXISTS time_slots (
    slot_id      INTEGER PRIMARY KEY AUTOINCREMENT,
    stylist_name TEXT NOT NULL,
    slot_date    TEXT NOT NULL,
    start_time   TEXT NOT NULL,
    service      TEXT NOT NULL,
    is_blocked   INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS appointments (
    appointment_id INTEGER PRIMARY KEY AUTOINCREMENT,
    customer_name  TEXT NOT NULL,
    customer_email TEXT NOT NULL,
    customer_phone TEXT,
    slot_id        INTEGER NOT NULL,
    stylist_name   TEXT NOT NULL,
    slot_date      TEXT NOT NULL,
    start_time     TEXT NOT NULL,
    service        TEXT NOT NULL
);
