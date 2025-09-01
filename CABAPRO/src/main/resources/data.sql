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
INSERT INTO referee (id, email, ranking_id) VALUES (1, 'fiba.ref@cabapro.com', 1);
INSERT INTO referee (id, email, ranking_id) VALUES (2, 'nat.ref@cabapro.com', 2);
INSERT INTO referee (id, email, ranking_id) VALUES (3, 'loc.ref@cabapro.com', 3);
