-- BCrypt hash of "123456" = $2a$10$JDrIm7iJJTvHdObNC6.lJOjsp3Ys1bfTj3t4oXvOoGY77jZQkLV4e (length 60)
-- Fix all users with wrong password length (120 chars = duplicated hash)

UPDATE mbbank_db.users
SET password = '$2a$10$JDrIm7iJJTvHdObNC6.lJOjsp3Ys1bfTj3t4oXvOoGY77jZQkLV4e'
WHERE LENGTH(password) != 60;

-- Verify
SELECT id, username, role, LENGTH(password) as len FROM mbbank_db.users;
