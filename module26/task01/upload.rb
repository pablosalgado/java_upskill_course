#!/usr/bin/env ruby

# This script reads data from CSV files and uploads it to a PostgreSQL database.

require 'csv'
require 'pg'

BATCH_SIZE = 50000

def main
  conn = PG.connect(dbname: 'module26', user: 'pablo', password: '', host: 'localhost')
  
  drop_schema(conn)
  create_schema(conn)
  insert_data(conn)

  conn.close
end

def drop_schema(conn)
  conn.exec('DROP SCHEMA IF EXISTS public CASCADE')
  conn.exec('CREATE SCHEMA public')

  puts 'Schema dropped'
end

def create_schema(conn)
  conn.exec('CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    surname VARCHAR(255),
    birthdate DATE
  )')

  conn.exec('CREATE TABLE posts (
    id SERIAL PRIMARY KEY,
    userId INT,
    text TEXT,
    timestamp TIMESTAMP,
    FOREIGN KEY (userId) REFERENCES users(id)
  )')

  conn.exec('CREATE TABLE friendships (
    userId1 INT,
    userId2 INT,
    timestamp TIMESTAMP,
    FOREIGN KEY (userId1) REFERENCES users(id),
    FOREIGN KEY (userId2) REFERENCES users(id)
  )')

  conn.exec('CREATE TABLE likes (
    userId INT,
    postId INT,
    timestamp TIMESTAMP,
    FOREIGN KEY (userId) REFERENCES users(id),
    FOREIGN KEY (postId) REFERENCES posts(id)
  )')

  conn.exec('CREATE UNIQUE INDEX idx_friendships_userId1_userId2 ON friendships(userId1, userId2)')

  conn.exec('CREATE UNIQUE INDEX idx_likes_userId_postId ON likes(userId, postId)')

  puts 'Schema created'
end

def insert_data(conn)
  insert_users(conn)
  insert_posts(conn)
  insert_friendships(conn)
  insert_likes(conn)
end

def insert_users(conn)
  csv_file = File.expand_path('../src/main/resources/users.csv', __FILE__)
  csv_data = CSV.read(csv_file)

  batch_count = (csv_data.length.to_f / BATCH_SIZE).ceil

  batch_count.times do |batch_index|
    start_index = batch_index * BATCH_SIZE
    end_index = (batch_index + 1) * BATCH_SIZE - 1
    batch = csv_data[start_index..end_index]

    conn.exec('BEGIN')

    batch.each do |row|
      name = row[0]
      surname = row[1]
      birthdate = row[2]

      conn.exec_params('INSERT INTO users (name, surname, birthdate) VALUES ($1, $2, $3)', [name, surname, birthdate])
    end

    conn.exec('COMMIT')
  end

  count = conn.exec('SELECT COUNT(*) FROM users').getvalue(0, 0)
  puts "Total number of created users: #{count}"
end

def insert_posts(conn)
  csv_file = File.expand_path('../src/main/resources/posts.csv', __FILE__)
  csv_data = CSV.read(csv_file)

  batch_count = (csv_data.length.to_f / BATCH_SIZE).ceil

  batch_count.times do |batch_index|
    start_index = batch_index * BATCH_SIZE
    end_index = (batch_index + 1) * BATCH_SIZE - 1
    batch = csv_data[start_index..end_index]

    conn.exec('BEGIN')

    batch.each do |row|
      userId = row[0]
      text = row[1]
      timestamp = row[2]

      conn.exec_params('INSERT INTO posts (userId, text, timestamp) VALUES ($1, $2, $3)', [userId, text, timestamp])
    end

    conn.exec('COMMIT')
  end

  count = conn.exec('SELECT COUNT(*) FROM posts').getvalue(0, 0)
  puts "Total number of created posts: #{count}"
end

def insert_friendships(conn)
  csv_file = File.expand_path('../src/main/resources/friendships.csv', __FILE__)
  csv_data = CSV.read(csv_file)

  batch_count = (csv_data.length.to_f / BATCH_SIZE).ceil

  batch_count.times do |batch_index|
    start_index = batch_index * BATCH_SIZE
    end_index = (batch_index + 1) * BATCH_SIZE - 1
    batch = csv_data[start_index..end_index]

    conn.exec('BEGIN')

    batch.each do |row|
      userId1 = row[0]
      userId2 = row[1]
      timestamp = row[2]

      conn.exec_params('INSERT INTO friendships (userId1, userId2, timestamp) VALUES ($1, $2, $3)', [userId1, userId2, timestamp])
    end

    conn.exec('COMMIT')
  end

  count = conn.exec('SELECT COUNT(*) FROM friendships').getvalue(0, 0)
  puts "Total number of created friendships: #{count}"
end

def insert_likes(conn)
  csv_file = File.expand_path('../src/main/resources/likes.csv', __FILE__)
  csv_data = CSV.read(csv_file)

  batch_count = (csv_data.length.to_f / BATCH_SIZE).ceil

  batch_count.times do |batch_index|
    start_index = batch_index * BATCH_SIZE
    end_index = (batch_index + 1) * BATCH_SIZE - 1
    batch = csv_data[start_index..end_index]

    conn.exec('BEGIN')

    batch.each do |row|
      userId = row[0]
      postId = row[1]
      timestamp = row[2]

      conn.exec_params('INSERT INTO likes (userId, postId, timestamp) VALUES ($1, $2, $3)', [userId, postId, timestamp])
    end

    conn.exec('COMMIT')
  end

  count = conn.exec('SELECT COUNT(*) FROM likes').getvalue(0, 0)
  puts "Total number of created likes: #{count}"
end

main