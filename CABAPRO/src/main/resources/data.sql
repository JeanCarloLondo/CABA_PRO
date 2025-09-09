-- ================================
-- CABA PRO - Seed data for H2
-- Notes:
--  - Uses H2-friendly UPSERTs via MERGE ... KEY(...)
--  - Avoids ON CONFLICT (PostgreSQL) syntax
--  - Avoids inserting explicit IDs for IDENTITY columns
--  - Uses INSERT ... SELECT ... WHERE NOT EXISTS to prevent duplicates
-- ================================

-- ---------- Rankings ----------
MERGE INTO ranking (name, fee, description) KEY(name)
VALUES ('FIBA',     150.0, 'International FIBA referees');

MERGE INTO ranking (name, fee, description) KEY(name)
VALUES ('National', 100.0, 'Top national referees');

MERGE INTO ranking (name, fee, description) KEY(name)
VALUES ('Local',     50.0, 'Local league referees');

-- ---------- Specialties (defaults) ----------
MERGE INTO specialties (name, description) KEY(name)
VALUES ('Court Referee', 'Referee on the court making live calls.');

MERGE INTO specialties (name, description) KEY(name)
VALUES ('Table Official', 'Manages the game clock, scoreboard, and statistics.');

-- ---------- Referees (linked to rankings/specialties) ----------
-- FIBA referee with Court Referee specialty
INSERT INTO referee (email, ranking_id, phone_number, specialty_id)
SELECT 'fiba.ref@cabapro.com', r.id, '123-456-7890',
       (SELECT s.id_specialty FROM specialties s WHERE s.name = 'Court Referee')
FROM ranking r
WHERE r.name = 'FIBA'
  AND NOT EXISTS (SELECT 1 FROM referee WHERE email = 'fiba.ref@cabapro.com');

-- National referee with Table Official specialty
INSERT INTO referee (email, ranking_id, phone_number, specialty_id)
SELECT 'nat.ref@cabapro.com', r.id, '234-567-8901',
       (SELECT s.id_specialty FROM specialties s WHERE s.name = 'Table Official')
FROM ranking r
WHERE r.name = 'National'
  AND NOT EXISTS (SELECT 1 FROM referee WHERE email = 'nat.ref@cabapro.com');

-- Local referee without specialty (null)
INSERT INTO referee (email, ranking_id, phone_number, specialty_id)
SELECT 'loc.ref@cabapro.com', r.id, '345-678-9012', NULL
FROM ranking r
WHERE r.name = 'Local'
  AND NOT EXISTS (SELECT 1 FROM referee WHERE email = 'loc.ref@cabapro.com');

-- Optional extra referee (often used in tests)
INSERT INTO referee (email, ranking_id, phone_number, specialty_id)
SELECT 'perez@esa.co', r.id, '0000000000',
       (SELECT s.id_specialty FROM specialties s WHERE s.name = 'Court Referee')
FROM ranking r
WHERE r.name = 'FIBA'
  AND NOT EXISTS (SELECT 1 FROM referee WHERE email = 'perez@esa.co');

-- ---------- Admin row (domain table, not login user) ----------
MERGE INTO admins (email, name) KEY(email)
VALUES ('admin@cabapro.com', 'Main Admin');

-- ---------- Tournament ----------
MERGE INTO tournaments (name, teams, rounds) KEY(name)
VALUES ('CABA Cup', 'Lions,Tigers', 'Group Stage,Final');

-- ---------- Match (must include tournament_id) ----------
-- One sample match Lions vs Tigers in the CABA Cup
INSERT INTO matches (tournament_id, home_team, away_team, match_date, location)
SELECT t.id, 'Lions', 'Tigers', TIMESTAMP '2025-09-15 18:00:00', 'Central Court'
FROM tournaments t
WHERE t.name = 'CABA Cup'
  AND NOT EXISTS (
    SELECT 1 FROM matches m
    WHERE m.tournament_id = t.id
      AND m.home_team     = 'Lions'
      AND m.away_team     = 'Tigers'
      AND m.match_date    = TIMESTAMP '2025-09-15 18:00:00'
  );

-- ---------- Assignment (links referee to the match) ----------
-- Assign the FIBA referee to the sample match, with FIBA ranking and PENDING status
INSERT INTO assignments
(referee_id, match_id, admin_id, ranking_id, status, assigned_at, assignment_fee)
SELECT
  (SELECT id FROM referee  WHERE email = 'fiba.ref@cabapro.com'),
  (SELECT m.id_match
     FROM matches m
     JOIN tournaments tt ON tt.id = m.tournament_id
    WHERE tt.name = 'CABA Cup'
      AND m.home_team  = 'Lions'
      AND m.away_team  = 'Tigers'
      AND m.match_date = TIMESTAMP '2025-09-15 18:00:00'),
  (SELECT id FROM admins   WHERE email = 'admin@cabapro.com'),
  (SELECT id FROM ranking  WHERE name  = 'FIBA'),
  'PENDING', CURRENT_TIMESTAMP, 100.00
WHERE NOT EXISTS (
  SELECT 1
  FROM assignments a
  WHERE a.match_id = (
          SELECT m2.id_match
          FROM matches m2
          JOIN tournaments tt2 ON tt2.id = m2.tournament_id
          WHERE tt2.name = 'CABA Cup'
            AND m2.home_team  = 'Lions'
            AND m2.away_team  = 'Tigers'
            AND m2.match_date = TIMESTAMP '2025-09-15 18:00:00'
        )
    AND a.referee_id = (SELECT id FROM referee WHERE email = 'fiba.ref@cabapro.com')
);