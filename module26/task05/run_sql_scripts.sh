#!/bin/bash

# Database credentials and details
DB_HOST="localhost"
DB_PORT="5432"
DB_NAME="module26_task05"
DB_USER="pablo"
DB_PASS=""

# Path to SQL files
SQL_DIR="./sql"

# Execute schema.sql
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f "$SQL_DIR/schema.sql"

# Execute data SQL files
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f "$SQL_DIR/users.sql"
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f "$SQL_DIR/posts.sql"
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f "$SQL_DIR/likes.sql"
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f "$SQL_DIR/friendships.sql"

echo "SQL scripts executed successfully."