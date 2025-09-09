-- ================================
-- Sample Data for CABA Pro Project (safe & portable)
-- ================================

-- 1) Rankings SIN ID explícito
--   (si el nombre es UNIQUE, estos inserts fallarán si ya existen; ver variante "MERGE" más abajo)
INSERT INTO ranking (name, fee, description) VALUES
('FIBA',     150.0, 'International FIBA referees'),
('National', 100.0, 'Top national referees'),
('Local',     50.0, 'Local league referees');

-- 2) Referees: toma el ID del ranking por nombre
INSERT INTO referee (email, ranking_id, phone_number)
SELECT 'fiba.ref@cabapro.com', r.id, '123-456-7890'
FROM ranking r WHERE r.name = 'FIBA';

INSERT INTO referee (email, ranking_id, phone_number)
SELECT 'nat.ref@cabapro.com', r.id, '234-567-8901'
FROM ranking r WHERE r.name = 'National';

INSERT INTO referee (email, ranking_id, phone_number)
SELECT 'loc.ref@cabapro.com', r.id, '345-678-9012'
FROM ranking r WHERE r.name = 'Local';

-- 3) Admin (si la PK "id" es identity, no pongas el id)
INSERT INTO admins (email, name)
VALUES ('admin@cabapro.com', 'Main Admin');

-- 4) Match (si id_match es identity, no pongas el id)
INSERT INTO matches (home_team, away_team, match_date, location)
VALUES ('Lions', 'Tigers', '2025-09-15 18:00:00', 'Central Court');

-- Specialties (H2/HSQL compatible)
MERGE INTO specialties (name, description) KEY(name)
VALUES ('Court Referee', 'Referee on the court making live calls.');

MERGE INTO specialties (name, description) KEY(name)
VALUES ('Table Official', 'Manages game clock, scoreboard, and stats');

-- 5) Assignment: subselects para todas las FKs
INSERT INTO assignments (referee_id, match_id, admin_id, ranking_id, status, assigned_at, assignment_fee)
SELECT
  (SELECT id FROM referee  WHERE email = 'fiba.ref@cabapro.com'),
  (SELECT id_match FROM matches WHERE home_team = 'Lions' AND away_team = 'Tigers' AND match_date = '2025-09-15 18:00:00'),
  (SELECT id FROM admins   WHERE email = 'admin@cabapro.com'),
  (SELECT id FROM ranking  WHERE name  = 'FIBA'),
  'PENDING', CURRENT_TIMESTAMP, 100.00;