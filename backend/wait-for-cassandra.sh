#!/bin/bash

echo "🔄 Waiting for Cassandra to be ready..."

# Wait until Cassandra responds to a TCP request on port 9042
until nc -z cassandra 9042; do
  echo "❌ Cassandra is unavailable - retrying in 5s..."
  sleep 5
done

echo "✅ Cassandra is up - starting the application"

# Run the command passed as arguments (e.g., Java)
exec "$@"
