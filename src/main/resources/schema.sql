CREATE TABLE IF NOT EXISTS players (
id VARCHAR(255) PRIMARY KEY,
name VARCHAR(100) NOT NULL UNIQUE,
balance DECIMAL(10,2) NOT NULL DEFAULT 0,
games_played INT NOT NULL DEFAULT 0,
games_won INT NOT NULL DEFAULT 0,
games_lost INT NOT NULL DEFAULT 0,
INDEX idx_name (name),
INDEX idx_win_rate (games_won, games_played)
) ENGINE= innoDB DEFAULT CHARSET= utf8mb4;