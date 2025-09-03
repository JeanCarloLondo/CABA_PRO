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
