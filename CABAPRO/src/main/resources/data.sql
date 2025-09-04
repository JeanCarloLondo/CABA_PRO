-- ================================
-- Sample Data for CABA Pro Project
-- Author: Jean Londoño
-- Date: 2025-08-31
-- ================================

-- Insert sample rankings
INSERT INTO ranking (id, name, fee, description)
VALUES (1, 'FIBA', 150.0, 'International FIBA referees');

INSERT INTO ranking (id, name, fee, description)
VALUES (2, 'National', 100.0, 'Top national referees');

INSERT INTO ranking (id, name, fee, description)
VALUES (3, 'Local', 50.0, 'Local league referees');

-- Insert sample referees (each linked to a ranking)
INSERT INTO referee (id, email, ranking_id, phone_number) VALUES (1, 'fiba.ref@cabapro.com', 1, '123-456-7890');
INSERT INTO referee (id, email, ranking_id, phone_number) VALUES (2, 'nat.ref@cabapro.com', 2, '234-567-8901');
INSERT INTO referee (id, email, ranking_id, phone_number) VALUES (3, 'loc.ref@cabapro.com', 3, '345-678-9012');

-- Insert sample admin
INSERT INTO admins (id, email, name)
VALUES (1, 'admin@cabapro.com', 'Main Admin');

-- Insert sample match
INSERT INTO matches (id, home_team, away_team, match_date, location)
VALUES (1, 'Lions', 'Tigers', '2025-09-15 18:00:00', 'Central Court');

-- Insert sample assignments (PENDING by default)
INSERT INTO assignments (id, referee_id, match_id, admin_id, status, assigned_at)
VALUES (1, 1, 1, 1, 'PENDING', NOW());