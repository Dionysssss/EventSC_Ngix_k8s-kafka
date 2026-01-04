Local test via port-forward
===========================

Ensure port-forward is running
-----------------------------
- Auth: `kubectl -n eventsc port-forward svc/auth-service 8081:8081`
- Event: `kubectl -n eventsc port-forward svc/event-service 8080:8080`
- Keep these terminals open; without them local 127.0.0.1 will not reach the services.

Quick login and event creation
------------------------------
```bash
# Login to get token
TOKEN=$(curl -s -X POST http://127.0.0.1:8081/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user5@example.com","password":"password123"}' | jq -r '.token')
echo "TOKEN=$TOKEN"

# Create event
curl -i -X POST http://127.0.0.1:8080/events \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "eventName": "Test Event",
    "eventLocation": { "latitude": 34.02, "longitude": -118.28 },
    "eventDescription": "Test event from curl",
    "eventDate": "2024-10-15",
    "eventTime": "18:00:00",
    "eventCreatorId": 136
  }'

# List events
curl -i http://127.0.0.1:8080/events
```
