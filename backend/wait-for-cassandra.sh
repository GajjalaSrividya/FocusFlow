#!/bin/bash

echo "🔄 Waiting for Cassandra to be ready..."

# Wait until Cassandra responds
until nc -z cassandra 9042; do
  echo "❌ Cassandra is unavailable - retrying in 5s..."
  sleep 5
done

echo "✅ Cassandra is up - creating keyspace and tables if needed..."

# Create keyspace
cqlsh cassandra -e "
CREATE KEYSPACE IF NOT EXISTS focusflow
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};
"

# Create tables
cqlsh cassandra -k focusflow -e "
CREATE TABLE IF NOT EXISTS users (
  user_id UUID PRIMARY KEY,
  name TEXT,
  password TEXT,
  email TEXT
);

CREATE TABLE IF NOT EXISTS user_lookup (
  username TEXT PRIMARY KEY,
  user_id TEXT
);

CREATE TABLE IF NOT EXISTS tasks (
  user_id UUID,
  task_date DATE,
  task_name TEXT,
  completed BOOLEAN,
  PRIMARY KEY ((user_id), task_date, task_name)
);
"

echo "✅ Keyspace and tables created."
echo "🚀 Starting the Spring Boot application..."

exec "$@"
